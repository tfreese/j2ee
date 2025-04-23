// Created: 21.05.2013
package de.freese.liberty.persistence;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.enterprise.inject.Produces;

/**
 * @author Thomas Freese
 */
public final class PersistenceProducer {
    /**
     * <pre>{@code
     * @Inject
     * @MyDataSource
     * private DataSource dataSource;
     * }</pre>
     */
    @Produces
    @MyDataSource
    @Resource(mappedName = "jdbc/dbDS")
    private DataSource dataSource;

    // @Produces
    // @MyEntityManager
    // @PersistenceContext(unitName = "my-pu")
    // private EntityManager entityManager;

    private PersistenceProducer() {
        super();
    }
}
