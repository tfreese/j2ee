package de.freese.spring.embedded.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QMyTable is a Querydsl query type for QMyTable
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMyTable extends com.mysema.query.sql.RelationalPathBase<QMyTable> {

    private static final long serialVersionUID = -1582736107;

    public static final QMyTable myTable = new QMyTable("MY_TABLE");

    public final DateTimePath<java.sql.Timestamp> datum = createDateTime("DATUM", java.sql.Timestamp.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<QMyTable> sysPk10092 = createPrimaryKey(id);

    public QMyTable(String variable) {
        super(QMyTable.class, forVariable(variable), "PUBLIC", "MY_TABLE");
    }

    public QMyTable(Path<? extends QMyTable> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "MY_TABLE");
    }

    public QMyTable(PathMetadata<?> metadata) {
        super(QMyTable.class, metadata, "PUBLIC", "MY_TABLE");
    }

}

