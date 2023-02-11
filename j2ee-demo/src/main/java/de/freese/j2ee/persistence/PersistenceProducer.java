// Created: 21.05.2013
package de.freese.j2ee.persistence;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * @author Thomas Freese
 */
public class PersistenceProducer {
    @Produces
    @MyDataSource
    @Resource(mappedName = "jdbc/hsqldb-memory")
    private DataSource dataSource;

    @Produces
    @MyEntityManager
    @PersistenceContext(unitName = "j2eeJPA")
    private EntityManager entityManager;
}
