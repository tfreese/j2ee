/**
 * Created: 10.05.2013
 */

package de.freese.j2ee.rest;

import de.freese.j2ee.interceptor.MyLogging;
import de.freese.j2ee.model.Kunde;
import de.freese.j2ee.persistence.MyEntityManager;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Stateless
// (name="Example", mappedName="ejb/SimpleBeanJNDI")
@Remote(IKundenService.class)
public class KundenService implements IKundenService
{
	/**
	 * 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StartUp.class);
	/**
	 * 
	 */
	@Inject
	@MyEntityManager
	private EntityManager entityManager = null;

	/**
	 * Erstellt ein neues {@link KundenService} Object.
	 */
	public KundenService()
	{
		super();
	}

	/**
	 * @see de.freese.j2ee.rest.IKundenService#getData()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@MyLogging
	public List<Kunde> getData()
	{
		LOGGER.info("");

		Query query = this.entityManager.createQuery("select k from Kunde k");
		List<Kunde> kunden = query.getResultList();

		return kunden;
	}
}
