/**
 *
 */
package de.freese.jpa;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
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
import com.querydsl.core.util.BeanUtils;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Generated;

/**
 * @author Thomas Freese
 */
public class MyQueryDSLBeanSerializer implements Serializer
{
    /**
     *
     */
    private static final Function<Property, Parameter> propertyToParameter = new Function<Property, Parameter>()
    {
        /**
         * @see com.google.common.base.Function#apply(Object)
         */
        @Override
        public Parameter apply(final Property input)
        {
            return new Parameter(input.getName(), input.getType());
        }
    };

    /**
     *
     */
    private boolean addFullConstructor = false;

    /**
     *
     */
    private boolean addHashcodeEquals = true;

    /**
     *
     */
    private boolean addToString = true;

    /**
     *
     */
    private final List<Class<?>> interfaces = Lists.newArrayList();

    /**
     *
     */
    private final String javadocSuffix;

    /**
     *
     */
    private boolean printSupertype = false;

    /**
     *
     */
    private final boolean propertyAnnotations;

    /**
     * Create a new BeanSerializer
     */
    public MyQueryDSLBeanSerializer()
    {
        this(true, " ist ein Querydsl bean typ.");
    }

    /**
     * Create a new BeanSerializer
     * <p>
     *
     * @param propertyAnnotations boolean
     */
    public MyQueryDSLBeanSerializer(final boolean propertyAnnotations)
    {
        this(propertyAnnotations, " ist ein Querydsl bean typ.");
    }

    /**
     * Create a new BeanSerializer
     * <p>
     *
     * @param propertyAnnotations boolean
     * @param javadocSuffix       String
     */
    public MyQueryDSLBeanSerializer(final boolean propertyAnnotations, final String javadocSuffix)
    {
        super();

        this.propertyAnnotations = propertyAnnotations;
        this.javadocSuffix = javadocSuffix;
    }

    /**
     * Create a new BeanSerializer with the given javadoc suffix
     * <p>
     *
     * @param javadocSuffix String
     */
    public MyQueryDSLBeanSerializer(final String javadocSuffix)
    {
        this(true, javadocSuffix);
    }

    /**
     * @param model  {@link EntityType}
     * @param writer {@link CodeWriter}
     * <p>
     * @throws IOException Falls was schief geht.
     */
    protected void addFullConstructor(final EntityType model, final CodeWriter writer) throws IOException
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
        writer.beginConstructor(model.getProperties(), propertyToParameter);
        writer.line("super();").nl();

        for (Property property : model.getProperties())
        {
            writer.line("this.", property.getEscapedName(), " = ", property.getEscapedName(), ";");
        }

        writer.end();
    }

    /**
     * @param model  {@link EntityType}
     * @param writer {@link CodeWriter}
     * <p>
     * @throws IOException Falls was schief geht.
     */
    protected void addHashcodeEquals(final EntityType model, final CodeWriter writer) throws IOException
    {
        // hashCode
        writer.javadoc("@see java.lang.Object#hashCode()");
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.INT, "hashCode");
        writer.line("final int prime = 31;");
        writer.line("int result = 1;");

        for (Property property : model.getProperties())
        {
            if (property.getType().getCategory() == TypeCategory.ARRAY)
            {
                writer.line("result = prime * result + Arrays.hashCode(", property.getEscapedName(), ");");
            }
            else
            {
                writer.line("result = prime * result + ((this.", property.getEscapedName(), " == null) ? 0 : this.", property.getEscapedName(), ".hashCode());");
            }
        }

        writer.nl().line("return result;");
        writer.end();

        // equals
        writer.javadoc("@see java.lang.Object#equals(java.lang.Object)");
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.BOOLEAN_P, "equals", new Parameter("obj", Types.OBJECT));
        writer.line("if (this == obj) return true;");
        writer.line("if (obj == null) return false;");
        writer.line("if (!(obj instanceof ", model.getSimpleName(), ")) return false;");
        writer.nl();
        writer.line(model.getSimpleName(), " other = (", model.getSimpleName(), ") obj;");

        for (Property property : model.getProperties())
        {
            writer.nl();

            if (property.getType().getCategory() == TypeCategory.ARRAY)
            {
                writer.line("if (!Arrays.equals(this.", property.getEscapedName(), ", other.", property.getEscapedName(), ") return false;");
            }
            else
            {
                writer.line("if(this.", property.getEscapedName(), " == null) {");
                writer.line("	if (other.", property.getEscapedName(), " != null) return false;");
                writer.line("} else if(!this.", property.getEscapedName(), ".equals(other.", property.getEscapedName(), ")) return false;");
            }
        }

        writer.nl().line("return true;");
        writer.end();
    }

    /**
     * @param iface Class
     */
    public void addInterface(final Class<?> iface)
    {
        if (!this.interfaces.contains(iface))
        {
            this.interfaces.add(iface);
        }
    }

    /**
     * @param model  {@link EntityType}
     * @param writer {@link CodeWriter}
     * <p>
     * @throws IOException Falls was schief geht.
     */
    protected void addToString(final EntityType model, final CodeWriter writer) throws IOException
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
                writer.append("Arrays.toString(").append(property.getEscapedName()).append(");");
            }
            else
            {
                writer.append("this.").append(property.getEscapedName()).append(");");
            }

            writer.nl();
            delim = ",";
        }

        writer.line("sb.append(\"]\");");
        writer.nl();
        writer.line("return sb.toString();");
        writer.end();
    }

    /**
     * @param model {@link EntityType}
     * <p>
     * @return {@link Set}
     */
    private Set<String> getAnnotationTypes(final EntityType model)
    {
        Set<String> imports = new TreeSet<>();

        for (Annotation annotation : model.getAnnotations())
        {
            imports.add(annotation.annotationType().getName());
        }

        if (this.propertyAnnotations)
        {
            for (Property property : model.getProperties())
            {
                for (Annotation annotation : property.getAnnotations())
                {
                    imports.add(annotation.annotationType().getName());
                }
            }
        }

        return imports;
    }

    /**
     * @see com.querydsl.codegen.Serializer#serialize(com.querydsl.codegen.EntityType, com.querydsl.codegen.SerializerConfig, com.mysema.codegen.CodeWriter)
     */
    @Override
    public void serialize(final EntityType model, final SerializerConfig serializerConfig, final CodeWriter writer) throws IOException
    {
        String simpleName = model.getSimpleName();

        // package
        if (!model.getPackageName().isEmpty())
        {
            writer.packageDecl(model.getPackageName());
        }

        // imports
        Set<String> importedClasses = getAnnotationTypes(model);

        for (Class<?> iface : this.interfaces)
        {
            importedClasses.add(iface.getName());
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

        if (this.addToString && model.hasArrays())
        {
            importedClasses.add(Arrays.class.getName());
        }

        writer.importClasses(importedClasses.toArray(new String[importedClasses.size()]));

        // javadoc
        writer.javadoc(simpleName + this.javadocSuffix, "Table: " + model.getData().get("table"));

        // header
        for (Annotation annotation : model.getAnnotations())
        {
            writer.annotation(annotation);
        }

        writer.line("@Generated(\"", getClass().getName(), "\")");

        if (!this.interfaces.isEmpty())
        {
            Type superType = null;

            if (this.printSupertype && (model.getSuperType() != null))
            {
                superType = model.getSuperType().getType();
            }

            Type[] interfaceTypes = new Type[this.interfaces.size()];

            for (int i = 0; i < interfaceTypes.length; i++)
            {
                interfaceTypes[i] = new ClassType(this.interfaces.get(i));
            }

            writer.beginClass(model, superType, interfaceTypes);
        }
        else if (this.printSupertype && (model.getSuperType() != null))
        {
            writer.beginClass(model, model.getSuperType().getType());
        }
        else
        {
            writer.beginClass(model);
        }

        if (this.interfaces.contains(Serializable.class))
        {
            // UUID uuid = UUID.randomUUID();
            // long oid = (uuid.getMostSignificantBits() >> 32) ^
            // uuid.getMostSignificantBits();
            // oid ^= (uuid.getLeastSignificantBits() >> 32) ^
            // uuid.getLeastSignificantBits();
            long oid = simpleName.hashCode();
            // System.out.println(oid);

            writer.javadoc("");
            writer.line("private static final long serialVersionUID = ", "" + oid, "L;").nl();
        }

        // fields
        Comparator<Annotation> annotationComparator = (a1, a2)
                ->
                {
                    return a1.toString().compareTo(a2.toString());
        };

        for (Property property : model.getProperties())
        {
            writer.javadoc("");

            if (this.propertyAnnotations)
            {
                List<Annotation> fieldAnnotations = new ArrayList<>(property.getAnnotations());
                Collections.sort(fieldAnnotations, annotationComparator);

                // for (Annotation annotation : property.getAnnotations())
                for (Annotation annotation : fieldAnnotations)
                {
                    writer.annotation(annotation);
                }
            }

            writer.privateField(property.getType(), property.getEscapedName());
        }

        // public empty constructor
        writer.javadoc("Default Constructor");
        writer.beginConstructor();
        writer.line("super();");
        writer.end();

        if (this.addFullConstructor)
        {
            addFullConstructor(model, writer);
        }

        // accessors
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

        if (this.addToString)
        {
            addToString(model, writer);
        }

        if (this.addHashcodeEquals)
        {
            addHashcodeEquals(model, writer);
        }

        writer.end();
    }

    /**
     * @param addFullConstructor boolean
     */
    public void setAddFullConstructor(final boolean addFullConstructor)
    {
        this.addFullConstructor = addFullConstructor;
    }

    /**
     * @param addHashcodeEquals boolean
     */
    public void setAddHashcodeEquals(final boolean addHashcodeEquals)
    {
        this.addHashcodeEquals = addHashcodeEquals;
    }

    /**
     * @param addToString boolean
     */
    public void setAddToString(final boolean addToString)
    {
        this.addToString = addToString;
    }

    /**
     * @param printSupertype boolean
     */
    public void setPrintSupertype(final boolean printSupertype)
    {
        this.printSupertype = printSupertype;
    }
}
