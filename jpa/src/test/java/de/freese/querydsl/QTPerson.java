package de.freese.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;
import java.sql.Types;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;

/**
 * QTPerson is a Querydsl query type for TPerson
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QTPerson extends com.querydsl.sql.RelationalPathBase<TPerson>
{
    /**
     *
     */
    private static final long serialVersionUID = -1631657421;

    /**
     *
     */
    public static final QTPerson tPerson = new QTPerson("T_PERSON");

    /**
     *
     */
    public final NumberPath<Long> myId = createNumber("myId", Long.class);

    /**
     *
     */
    public final StringPath name = createString("name");

    /**
     *
     */
    public final com.querydsl.sql.PrimaryKey<TPerson> sysPk10107 = createPrimaryKey(this.myId);

    /**
     *
     */
    public final StringPath vorname = createString("vorname");

    /**
     * Erstellt ein neues {@link QTPerson} Object.
     *
     * @param path {@link Path}
     */
    public QTPerson(final Path<? extends TPerson> path)
    {
        super(path.getType(), path.getMetadata(), "PUBLIC", "T_PERSON");

        addMetadata();
    }

    /**
     * Erstellt ein neues {@link QTPerson} Object.
     *
     * @param metadata {@link PathMetadata}
     */
    public QTPerson(final PathMetadata metadata)
    {
        super(TPerson.class, metadata, "PUBLIC", "T_PERSON");
        addMetadata();
    }

    /**
     * Erstellt ein neues {@link QTPerson} Object.
     *
     * @param variable String
     */
    public QTPerson(final String variable)
    {
        super(TPerson.class, forVariable(variable), "PUBLIC", "T_PERSON");

        addMetadata();
    }

    /**
     * Erstellt ein neues {@link QTPerson} Object.
     *
     * @param variable String
     * @param schema String
     * @param table String
     */
    public QTPerson(final String variable, final String schema, final String table)
    {
        super(TPerson.class, forVariable(variable), schema, table);

        addMetadata();
    }

    /**
     *
     */
    public void addMetadata()
    {
        addMetadata(this.myId, ColumnMetadata.named("MY_ID").withIndex(1).ofType(Types.BIGINT).withSize(64).notNull());
        addMetadata(this.name, ColumnMetadata.named("NAME").withIndex(2).ofType(Types.VARCHAR).withSize(25));
        addMetadata(this.vorname, ColumnMetadata.named("VORNAME").withIndex(3).ofType(Types.VARCHAR).withSize(25));
    }
}
