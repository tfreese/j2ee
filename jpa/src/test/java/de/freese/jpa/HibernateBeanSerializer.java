/**
 * Created: 04.07.2018
 */

package de.freese.jpa;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.Serializer;
import com.querydsl.sql.ColumnMetadata;

/**
 * {@link Serializer} um Hibernate Entities aus der Datenbank zu erzeugen.
 *
 * @author Thomas Freese
 */
public class HibernateBeanSerializer extends PojoBeanSerializer
{
    /**
     * Erstellt ein neues {@link HibernateBeanSerializer} Object.
     */
    public HibernateBeanSerializer()
    {
        super(" ist ein generiertes Hibernate Entity.");
    }

    /**
     * @see de.freese.jpa.PojoBeanSerializer#getClassAnnotations(com.querydsl.codegen.EntityType)
     */
    @Override
    protected List<Annotation> getClassAnnotations(final EntityType model)
    {
        List<Annotation> classAnnotations = super.getClassAnnotations(model);
        classAnnotations.add(createAnnotationInstance(Entity.class));

        Map<String, Object> values = new HashMap<>();
        values.put("name", getTable(model));
        values.put("schema", getSchema(model));
        classAnnotations.add(createAnnotationInstance(Table.class, values));

        classAnnotations.add(createAnnotationInstance(DynamicInsert.class));
        classAnnotations.add(createAnnotationInstance(DynamicUpdate.class));
        classAnnotations.add(createAnnotationInstance(Cacheable.class));

        values = new HashMap<>();
        values.put("usage", CacheConcurrencyStrategy.READ_WRITE);
        values.put("region", model.getSimpleName());
        classAnnotations.add(createAnnotationInstance(Cache.class, values));

        return classAnnotations;
    }

    /**
     * @see de.freese.jpa.PojoBeanSerializer#getFieldAnnotations(com.querydsl.codegen.Property)
     */
    @Override
    protected List<Annotation> getFieldAnnotations(final Property property)
    {
        List<Annotation> fieldAnnotations = super.getFieldAnnotations(property);

        ColumnMetadata columnMetadata = getMetaData(property);
        List<String> primaryKeyColumns = getPrimaryKeyColumns(property.getDeclaringType());

        if (primaryKeyColumns.contains(columnMetadata.getName()))
        {
            fieldAnnotations.add(createAnnotationInstance(Id.class));
        }

        Map<String, Object> values = new HashMap<>();
        values.put("name", columnMetadata.getName());
        values.put("nullable", columnMetadata.isNullable());
        fieldAnnotations.add(createAnnotationInstance(Column.class, values));

        return fieldAnnotations;
    }

    /**
     * Liefert das Schema der Tabelle.
     *
     * @param model {@link EntityType}
     * @return String
     */
    protected String getSchema(final EntityType model)
    {
        return (String) model.getData().get("schema");
    }
}
