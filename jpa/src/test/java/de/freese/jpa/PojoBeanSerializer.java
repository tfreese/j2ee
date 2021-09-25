package de.freese.jpa;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.Generated;
import javax.validation.Constraint;

import com.google.common.base.Function;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.Serializer;
import com.querydsl.codegen.SerializerConfig;
import com.querydsl.codegen.Supertype;
import com.querydsl.core.util.BeanUtils;
import com.querydsl.sql.Column;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.codegen.support.PrimaryKeyData;

/**
 * {@link Serializer} um POJOs aus der Datenbank zu erzeugen.
 *
 * @author Thomas Freese
 */
public class PojoBeanSerializer implements Serializer
{
    /**
     *
     */
    protected static final Comparator<Annotation> COMPARATOR_ANNOTATION = (a1, a2) -> a1.toString().compareTo(a2.toString());
    /**
     *
     */
    private static final Function<Property, Parameter> PROPERTY_TO_PARAMETER = input -> new Parameter(input.getName(), input.getType());

    /**
     * Erzeugt eine Annotation-Instanz.
     *
     * @param annotationType {@link Class}
     *
     * @return {@link Annotation}
     */
    protected static <A extends Annotation> A createAnnotationInstance(final Class<A> annotationType)
    {
        return createAnnotationInstance(annotationType, Collections.emptyMap());
    }

    /**
     * Erzeugt eine Annotation-Instanz mit der Möglichkeit die Attribute zu setzen.
     *
     * @param annotationType {@link Class}
     * @param customValues {@link Map}
     *
     * @return {@link Annotation}
     */
    protected static <A extends Annotation> A createAnnotationInstance(final Class<A> annotationType, final Map<String, Object> customValues)
    {
        Map<String, Object> values = new HashMap<>();

        // Default Values extrahieren.
        for (Method method : annotationType.getDeclaredMethods())
        {
            values.put(method.getName(), method.getDefaultValue());
        }

        // Definierte Values setzen.
        values.putAll(customValues);

        // return (A) sun.reflect.annotation.AnnotationParser.annotationForMap(annotationType, values);
        // TODO
        return null;
    }

    /**
     *
     */
    private boolean addFullConstructor;
    /**
     *
     */
    private boolean includeValidationAnnotations;
    /**
     *
     */
    private final List<Class<?>> interfaces = new ArrayList<>();
    /**
     *
     */
    private String javadocSuffix = "";
    /**
     *
     */
    private ClassType superType;

    /**
     * Erstellt ein neues {@link PojoBeanSerializer} Object.
     */
    public PojoBeanSerializer()
    {
        this(" ist ein generiertes Pojo.");
    }

    /**
     * Erstellt ein neues {@link PojoBeanSerializer} Object.
     *
     * @param javadocSuffix String; Prefix für JavaDoc des Klassen-Headers.
     */
    public PojoBeanSerializer(final String javadocSuffix)
    {
        super();

        setJavadocSuffix(javadocSuffix);
    }

    /**
     * @param iface {@link Class}
     */
    public void addInterface(final Class<?> iface)
    {
        if (!getInterfaces().contains(iface))
        {
            getInterfaces().add(iface);
        }
    }

    /**
     * Liefert alle Annotations der Klasse.<br>
     *
     * @param model {@link EntityType}
     *
     * @return {@link List}
     */
    protected List<Annotation> getClassAnnotations(final EntityType model)
    {
        // @formatter:off
        List<Annotation> classAnnotations = model.getAnnotations().stream()
            .sorted(COMPARATOR_ANNOTATION)
            .collect(Collectors.toList());
        // @formatter:on

        return classAnnotations;
    }

    /**
     * Liefert alle Annotations eines Klassen-Attributs.<br>
     * Default-Filter für: {@link Column}
     *
     * @param property {@link Property}
     *
     * @return {@link List}
     */
    protected List<Annotation> getFieldAnnotations(final Property property)
    {
        // @formatter:off
        List<Annotation> fieldAnnotations = property.getAnnotations().stream()
            .filter(a -> !(a instanceof Column))
            .filter(a-> isIncludeValidationAnnotations() ? true : !a.annotationType().isAnnotationPresent(Constraint.class))
            .sorted(COMPARATOR_ANNOTATION)
            .collect(Collectors.toList());
        // @formatter:on

        return fieldAnnotations;
    }

    /**
     * Liefert den JavaDoc-Text des Klassen-Attributs.
     *
     * @param property {@link Property}
     *
     * @return String[]; Jede Zeile ist ein Array-Element
     */
    protected String[] getFieldJavaDoc(final Property property)
    {
        StringJoiner joiner = new StringJoiner("; ");
        StringBuilder javaDoc = new StringBuilder("");

        // property.getAnnotations().forEach(System.out::println);

        ColumnMetadata columnMetadata = getMetaData(property);

        javaDoc.append("Column: " + columnMetadata.getName() + "; ");

        if (columnMetadata.hasJdbcType())
        {
            joiner.add(JDBCType.valueOf(columnMetadata.getJdbcType()).toString());
        }

        joiner.add(columnMetadata.isNullable() ? "nullable" : "not null");

        if (columnMetadata.hasSize())
        {
            joiner.add("size = " + columnMetadata.getSize());
        }

        if (columnMetadata.hasDigits())
        {
            joiner.add("digits = " + columnMetadata.getDigits());
        }

        // NotNull notNull = property.getAnnotation(NotNull.class);
        //
        // if (notNull != null)
        // {
        // joiner.add("not null");
        // }
        //
        // Null nul = property.getAnnotation(Null.class);
        //
        // if (nul != null)
        // {
        // joiner.add("nullable");
        // }
        //
        // Size size = property.getAnnotation(Size.class);
        //
        // if (size != null)
        // {
        // if (size.min() != 0)
        // {
        // joiner.add("size_min = " + size.min());
        // }
        //
        // if (size.max() != Integer.MAX_VALUE)
        // {
        // joiner.add("size_max = " + size.max() + "");
        // }
        // }

        List<String> primaryKeyColumns = getPrimaryKeyColumns(property.getDeclaringType());

        if (primaryKeyColumns.contains(columnMetadata.getName()))
        {
            joiner.add("Primary Key");
        }

        javaDoc.append(joiner.toString());

        return new String[]
        {
                javaDoc.toString()
        };
    }

    /**
     * Liefert alle Klassennamen der Annotations für den Import-Bereich.
     *
     * @param model {@link EntityType}
     *
     * @return {@link Set}
     */
    protected Set<String> getImportClasses(final EntityType model)
    {
        Set<String> imports = new TreeSet<>();

        for (Annotation annotation : getClassAnnotations(model))
        {
            imports.add(annotation.annotationType().getName());
        }

        for (Property property : model.getProperties())
        {
            for (Annotation annotation : getFieldAnnotations(property))
            {
                imports.add(annotation.annotationType().getName());
            }
        }

        return imports;
    }

    /**
     * @return {@link List}
     */
    protected List<Class<?>> getInterfaces()
    {
        return this.interfaces;
    }

    /**
     * @return String
     */
    protected String getJavadocSuffix()
    {
        return this.javadocSuffix;
    }

    /**
     * Liefert den Namen des Klassen-Atribbuts.
     *
     * @param property {@link Property}
     *
     * @return {@link ColumnMetadata}
     */
    protected ColumnMetadata getMetaData(final Property property)
    {
        ColumnMetadata columnMetadata = (ColumnMetadata) property.getData().get("COLUMN");

        return columnMetadata;
        // Column column = property.getAnnotation(Column.class);
        // String name = null;
        //
        // if (column != null)
        // {
        // name = column.value();
        // }
        // else
        // {
        // name = "";
        // }
        //
        // return name;
    }

    /**
     * Liefert die Spalten des PrimaryKeys.
     *
     * @param model {@link EntityType}
     *
     * @return {@link List}
     */
    @SuppressWarnings("unchecked")
    protected List<String> getPrimaryKeyColumns(final EntityType model)
    {
        Collection<PrimaryKeyData> primaryKeyData = (Collection<PrimaryKeyData>) model.getData().get(PrimaryKeyData.class);
        List<String> columns = new ArrayList<>();

        if (primaryKeyData != null)
        {
            primaryKeyData.stream().flatMap(pkd -> pkd.getColumns().stream()).forEach(columns::add);
        }

        return columns;
    }

    /**
     * Liefert den {@link Supertype} des Models.
     *
     * @param model {@link EntityType}
     *
     * @return {@link Type}
     */
    protected Type getSuperType(final EntityType model)
    {
        Type type = null;

        // if (this.printSupertype && (model.getSuperType() != null))
        // {
        // type = model.getSuperType().getType();
        // }
        if (this.superType != null)
        {
            type = this.superType;
        }

        return type;
    }

    /**
     * Liefert den Namen der Tabelle.
     *
     * @param model {@link EntityType}
     *
     * @return String
     */
    protected String getTable(final EntityType model)
    {
        return (String) model.getData().get("table");
    }

    /**
     * @return boolean
     */
    protected boolean isAddFullConstructor()
    {
        return this.addFullConstructor;
    }

    /**
     * true = javax.validation.constraints.* Annotations mit einbauen.<br>
     * Es wird nach dem Vorkommen von {@link Constraint} geschaut.
     *
     * @return boolean
     */
    protected boolean isIncludeValidationAnnotations()
    {
        return this.includeValidationAnnotations;
    }

    /**
     * @see com.querydsl.codegen.Serializer#serialize(com.querydsl.codegen.EntityType, com.querydsl.codegen.SerializerConfig, com.mysema.codegen.CodeWriter)
     */
    @Override
    public final void serialize(final EntityType model, final SerializerConfig serializerConfig, final CodeWriter writer) throws IOException
    {
        serialize01Package(writer, model);
        serialize02Imports(writer, model);
        serialize03ClassHeader(writer, model);
        serialize04Class(writer, model);
        serialize05Fields(writer, model);
        serialize06Constructor(writer, model);
        serialize07Methods(writer, model);

        serialize08HashcodeEquals(writer, model);
        serialize09ToString(writer, model);

        writer.end();
    }

    /**
     * @param writer {@link CodeWriter}
     * @param model {@link EntityType}
     *
     * @throws IOException Falls was schief geht.
     */
    protected void serialize01Package(final CodeWriter writer, final EntityType model) throws IOException
    {
        // 2018-06-09 11.55.49,586
        writer.line(String.format("// Created: %1$tY-%1$tm-%1$td %1$tH.%1$tM.%1$tS,%1$tL", new Date()));

        if (!model.getPackageName().isEmpty())
        {
            writer.packageDecl(model.getPackageName());
        }
    }

    /**
     * @param writer {@link CodeWriter}
     * @param model {@link EntityType}
     *
     * @throws IOException Falls was schief geht.
     */
    protected void serialize02Imports(final CodeWriter writer, final EntityType model) throws IOException
    {
        Type superType = getSuperType(model);

        Set<String> importedClasses = getImportClasses(model);

        for (Class<?> iface : this.interfaces)
        {
            importedClasses.add(iface.getName());
        }

        if (superType != null)
        {
            importedClasses.add(superType.getFullName());
        }

        importedClasses.add(Generated.class.getName());

        if (model.hasLists())
        {
            importedClasses.add(List.class.getName());
        }

        if (model.hasCollections())
        {
            importedClasses.add(Collection.class.getName());
        }

        if (model.hasSets())
        {
            importedClasses.add(Set.class.getName());
        }

        if (model.hasMaps())
        {
            importedClasses.add(Map.class.getName());
        }

        // Für toString()
        Optional<Property> optional = model.getProperties().stream().filter(p -> p.getType().getCategory() == TypeCategory.ARRAY).findFirst();

        if (optional.isPresent())
        {
            importedClasses.add(Arrays.class.getName());
        }

        writer.importClasses(importedClasses.toArray(new String[0]));
    }

    /**
     * @param writer {@link CodeWriter}
     * @param model {@link EntityType}
     *
     * @throws IOException Falls was schief geht.
     */
    protected void serialize03ClassHeader(final CodeWriter writer, final EntityType model) throws IOException
    {
        writer.javadoc(model.getSimpleName() + getJavadocSuffix(), "Table: " + getTable(model));

        List<Annotation> classAnnotations = getClassAnnotations(model);

        for (Annotation annotation : classAnnotations)
        {
            writer.annotation(annotation);
        }

        writer.line("@Generated(\"", getClass().getName(), "\")");
    }

    /**
     * @param writer {@link CodeWriter}
     * @param model {@link EntityType}
     *
     * @throws IOException Falls was schief geht.
     */
    protected void serialize04Class(final CodeWriter writer, final EntityType model) throws IOException
    {
        Type superType = getSuperType(model);
        Type[] interfaceTypes = null;

        if (!this.interfaces.isEmpty())
        {
            interfaceTypes = new Type[this.interfaces.size()];

            for (int i = 0; i < interfaceTypes.length; i++)
            {
                interfaceTypes[i] = new ClassType(this.interfaces.get(i));
            }
        }

        writer.beginClass(model, superType, interfaceTypes);

        if (getInterfaces().contains(Serializable.class))
        {
            // UUID uuid = UUID.randomUUID();
            // long oid = (uuid.getMostSignificantBits() >> 32) ^ uuid.getMostSignificantBits();
            // oid ^= (uuid.getLeastSignificantBits() >> 32) ^ uuid.getLeastSignificantBits();
            long oid = (model.getPackageName() + "." + model.getSimpleName()).hashCode();

            writer.javadoc("");
            writer.line("private static final long serialVersionUID = ", "" + oid, "L;").nl();
        }
    }

    /**
     * Schreibt alle Klassen-Attribute.
     *
     * @param writer {@link CodeWriter}
     * @param model {@link EntityType}
     *
     * @throws IOException Falls was schief geht.
     */
    protected void serialize05Fields(final CodeWriter writer, final EntityType model) throws IOException
    {
        for (Property property : model.getProperties())
        {
            String[] javaDoc = getFieldJavaDoc(property);

            writer.javadoc(javaDoc);

            List<Annotation> fieldAnnotations = getFieldAnnotations(property);

            for (Annotation annotation : fieldAnnotations)
            {
                writer.annotation(annotation);
            }

            writer.privateField(property.getType(), property.getEscapedName());
        }
    }

    /**
     * @param writer {@link CodeWriter}
     * @param model {@link EntityType}
     *
     * @throws IOException Falls was schief geht.
     */
    protected void serialize06Constructor(final CodeWriter writer, final EntityType model) throws IOException
    {
        writer.javadoc("Default Constructor");
        writer.beginConstructor();
        writer.line("super();");
        writer.end();

        if (isAddFullConstructor())
        {
            serializeConstructorFull(writer, model);
        }
    }

    /**
     * @param writer {@link CodeWriter}
     * @param model {@link EntityType}
     *
     * @throws IOException Falls was schief geht.
     */
    protected void serialize07Methods(final CodeWriter writer, final EntityType model) throws IOException
    {
        for (Property property : model.getProperties())
        {
            String propertyName = property.getEscapedName();

            // getter
            writer.javadoc("@return " + property.getType());
            writer.beginPublicMethod(property.getType(), "get" + BeanUtils.capitalize(propertyName));
            writer.line("return ", propertyName, ";");
            writer.end();

            // setter
            Parameter parameter = new Parameter(propertyName, property.getType());
            writer.javadoc("@param " + propertyName + " " + property.getType());
            writer.beginPublicMethod(Types.VOID, "set" + BeanUtils.capitalize(propertyName), parameter);
            writer.line("this.", propertyName, " = ", propertyName, ";");
            writer.end();
        }
    }

    /**
     * @param writer {@link CodeWriter}
     * @param model {@link EntityType}
     *
     * @throws IOException Falls was schief geht.
     */
    protected void serialize08HashcodeEquals(final CodeWriter writer, final EntityType model) throws IOException
    {
        // hashCode
        writer.javadoc("@see java.lang.Object#hashCode()");
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.INT, "hashCode");
        writer.line("final int prime = 31;");
        writer.line("int result = 1;");
        writer.nl();

        for (Property property : model.getProperties())
        {
            if (property.getType().getCategory() == TypeCategory.ARRAY)
            {
                writer.line("result = prime * result + Arrays.hashCode(", property.getEscapedName(), ");");
            }
            else
            {
                writer.line("result = prime * result + ((this.", property.getEscapedName(), " == null) ? 0 : this.", property.getEscapedName(),
                        ".hashCode());");
            }
        }

        writer.nl().line("return result;");
        writer.end();

        // equals
        writer.javadoc("@see java.lang.Object#equals(java.lang.Object)");
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.BOOLEAN_P, "equals", new Parameter("obj", Types.OBJECT));

        writer.line("if (this == obj) {");
        writer.line("    return true;");
        writer.line("}");
        writer.nl();

        writer.line("if (obj == null) {");
        writer.line("    return false;");
        writer.line("}");
        writer.nl();

        writer.line("if (!(obj instanceof ", model.getSimpleName(), ")) {");
        writer.line("    return false;");
        writer.line("}");
        writer.nl();

        writer.line(model.getSimpleName(), " other = (", model.getSimpleName(), ") obj;");

        for (Property property : model.getProperties())
        {
            writer.nl();

            if (property.getType().getCategory() == TypeCategory.ARRAY)
            {
                writer.line("if (!Arrays.equals(get", property.getEscapedName(), ", other.", property.getEscapedName(), ") {");
                writer.line("    return false;");
                writer.line("}");
            }
            else
            {
                writer.line("if(this.", property.getEscapedName(), " == null) {");
                writer.line("    if (other.", property.getEscapedName(), " != null) {");
                writer.line("        return false;");
                writer.line("    }");
                writer.line("}");
                writer.line("else if(!this.", property.getEscapedName(), ".equals(other.", property.getEscapedName(), ")) {");
                writer.line("        return false;");
                writer.line("}");
            }
        }

        writer.nl().line("return true;");
        writer.end();
    }

    /**
     * @param writer {@link CodeWriter}
     * @param model {@link EntityType}
     *            <p>
     *
     * @throws IOException Falls was schief geht.
     */
    protected void serialize09ToString(final CodeWriter writer, final EntityType model) throws IOException
    {
        writer.javadoc("@see java.lang.Object#toString()");
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.STRING, "toString");

        writer.line("StringBuilder sb = new StringBuilder();");
        writer.line("sb.append(\"", model.getSimpleName(), " [\");");

        String delim = "";

        for (Property property : model.getProperties())
        {
            writer.beginLine("sb.append(\"", delim, property.getEscapedName(), " = \").append(");

            if (property.getType().getCategory() == TypeCategory.ARRAY)
            {
                writer.append("Arrays.toString(this.").append(property.getEscapedName());
                // writer.append("Arrays.toString(").append(BeanUtils.capitalize(property.getName())).append("()");
            }
            else
            {
                writer.append("this.").append(property.getEscapedName());
                // writer.append("get").append(BeanUtils.capitalize(property.getName())).append("()");
            }

            writer.append(");");
            writer.nl();
            delim = ",";
        }

        writer.line("sb.append(\"]\");");
        writer.nl();

        writer.line("return sb.toString();");
        writer.end();
    }

    /**
     * @param writer {@link CodeWriter}
     * @param model {@link EntityType}
     *            <p>
     *
     * @throws IOException Falls was schief geht.
     */
    protected void serializeConstructorFull(final CodeWriter writer, final EntityType model) throws IOException
    {
        int i = 0;
        String[] javadocParameter = new String[model.getProperties().size() + 2];
        javadocParameter[i++] = "Full Constructor";
        javadocParameter[i++] = "";

        for (Property property : model.getProperties())
        {
            javadocParameter[i++] = "@param " + property.getEscapedName() + " " + property.getType();
        }

        writer.javadoc(javadocParameter);
        writer.beginConstructor(model.getProperties(), PROPERTY_TO_PARAMETER);
        writer.line("super();").nl();

        for (Property property : model.getProperties())
        {
            writer.line("this.", property.getEscapedName(), " = ", property.getEscapedName(), ";");
        }

        writer.end();
    }

    /**
     * Konstruktor mit allen Parametern einbauen.
     *
     * @param addFullConstructor boolean
     */
    public void setAddFullConstructor(final boolean addFullConstructor)
    {
        this.addFullConstructor = addFullConstructor;
    }

    /**
     * true = javax.validation.constraints.* Annotations mit einbauen.<br>
     * Es wird nach dem Vorkommen von {@link Constraint} geschaut.
     *
     * @param includeValidationAnnotations boolean
     */
    public void setIncludeValidationAnnotations(final boolean includeValidationAnnotations)
    {
        this.includeValidationAnnotations = includeValidationAnnotations;
    }

    /**
     * Prefix für JavaDoc des Klassen-Headers.<br>
     * Beispiel: " ist ein generiertes Pojo."
     *
     * @param javadocSuffix String
     */
    public void setJavadocSuffix(final String javadocSuffix)
    {
        this.javadocSuffix = javadocSuffix;
    }

    /**
     * Superklasse von der die Pojos erben sollen.
     *
     * @param supertype Class
     */
    public void setSuperType(final Class<?> supertype)
    {
        this.superType = new ClassType(supertype);
    }
}
