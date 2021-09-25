// Created: 08.12.2015
package de.freese.jpa.jdbc;

import java.sql.Connection;

import javax.inject.Provider;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * {@code SpringConnectionProvider} is a Provider implementation which provides a transactionally bound connection.
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
public class SpringConnectionProvider implements Provider<Connection>
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
     * @see javax.inject.Provider#get()
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
