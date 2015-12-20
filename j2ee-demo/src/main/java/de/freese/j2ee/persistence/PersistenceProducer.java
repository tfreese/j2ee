/**
 * Created: 21.05.2013
 */

package de.freese.j2ee.persistence;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

/**
 * @author Thomas Freese
 */
public class PersistenceProducer
{
	/**
	 * 
	 */
	@Produces
	@MyDataSource
	@Resource(mappedName = "jdbc/hsqldb-memory")
	private DataSource dataSource = null;

	/**
	 * 
	 */
	@Produces
	@MyEntityManager
	@PersistenceContext(unitName = "j2eeJPA")
	private EntityManager entityManager = null;

	/**
	 * Erstellt ein neues {@link PersistenceProducer} Object.
	 */
	public PersistenceProducer()
	{
		super();
	}
}
