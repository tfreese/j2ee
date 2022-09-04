// Created: 08.12.2015
package de.freese.jpa.jdbc;

import java.sql.Connection;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * {@code SpringConnectionProvider} is a Provider implementation which provides a transactional bound connection.
 * <p>
 * Usage example
 * </p>
 *
 * <pre>
 * {
 *     &#064;code
 *     Provider&lt;Connection&gt; provider = new SpringConnectionProvider(dataSource());
 *     SQLQueryFactory queryFactory = com.querydsl.sql.SQLQueryFactory(configuration, provider);
 * }
 * </pre>
 * <p>
 *
 * @author Thomas Freese
 */
public class SpringConnectionProvider implements Supplier<Connection>
{
    /**
     *
     */
    private final DataSource dataSource;

    /**
     * @param dataSource {@link DataSource}
     */
    public SpringConnectionProvider(final DataSource dataSource)
    {
        super();

        this.dataSource = dataSource;
    }

    /**
     * @see java.util.function.Supplier#get()
     */
    @Override
    public Connection get()
    {
        Connection connection = DataSourceUtils.getConnection(this.dataSource);

        if (!DataSourceUtils.isConnectionTransactional(connection, this.dataSource))
        {
            throw new IllegalStateException("Connection is not transactional");
        }

        return connection;
    }
}
