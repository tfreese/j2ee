// Created: 04.07.2018
package de.freese.jpa;

import java.lang.annotation.Annotation;
import java.util.List;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.Serializer;
import com.querydsl.sql.ColumnMetadata;

import de.freese.jpa.codegen.CacheImpl;
import de.freese.jpa.codegen.CacheableImpl;
import de.freese.jpa.codegen.ColumnImpl;
import de.freese.jpa.codegen.DynamicInsertImpl;
import de.freese.jpa.codegen.DynamicUpdateImpl;
import de.freese.jpa.codegen.EntityImpl;
import de.freese.jpa.codegen.IdImpl;
import de.freese.jpa.codegen.TableImpl;

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
        classAnnotations.add(new EntityImpl());
        classAnnotations.add(new TableImpl(getSchema(model), getTable(model)));
        classAnnotations.add(new DynamicInsertImpl());
        classAnnotations.add(new DynamicUpdateImpl());
        classAnnotations.add(new CacheableImpl());
        classAnnotations.add(new CacheImpl(CacheConcurrencyStrategy.READ_WRITE, model.getSimpleName()));

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
            fieldAnnotations.add(new IdImpl());
        }

        fieldAnnotations.add(new ColumnImpl(columnMetadata.getName(), columnMetadata.isNullable()));

        return fieldAnnotations;
    }

    /**
     * Liefert das Schema der Tabelle.
     *
     * @param model {@link EntityType}
     *
     * @return String
     */
    protected String getSchema(final EntityType model)
    {
        return (String) model.getData().get("schema");
    }
}
