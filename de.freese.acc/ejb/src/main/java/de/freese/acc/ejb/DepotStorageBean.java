package de.freese.acc.ejb;

import de.freese.acc.ejb.client.IDepotStorage;
import de.freese.acc.model.Depot;
import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author Thomas Freese
 */
@Stateless
@Remote(IDepotStorage.class)
public class DepotStorageBean implements IDepotStorage
{
	/**
	 * 
	 */
	@Resource(lookup = "jms/accConnectionFactory")
	private ConnectionFactory connectionFactory = null;

	/**
	 * 
	 */
	@PersistenceContext
	private EntityManager entityManager = null;

	/**
	 * 
	 */
	@Resource(lookup = "jms/depots")
	private Topic topic = null;

	/**
	 * Erstellt ein neues {@link DepotStorageBean} Object.
	 */
	public DepotStorageBean()
	{
		super();
	}

	/**
	 * @see de.freese.acc.ejb.client.IDepotStorage#mailAddressUsed(java.lang.String)
	 */
	@Override
	public boolean mailAddressUsed(final String eMailAddress)
	{
		TypedQuery<Number> namedQuery = this.entityManager.createNamedQuery("mailAddressUsed", Number.class);
		namedQuery.setParameter("mailAddress", eMailAddress);
		Number count = namedQuery.getSingleResult();

		return count.intValue() > 0;
	}

	/**
	 * @param depot {@link Depot}
	 */
	private void notifyClients(final Depot depot)
	{
		System.out.println("DepotStorageBean.notifyClients()");

		try
		{
			Connection connection = this.connectionFactory.createConnection();
			Session session = connection.createSession(true, 0);
			MessageProducer messageProducer = session.createProducer(this.topic);
			connection.start();
			messageProducer.send(session.createObjectMessage(depot));
			messageProducer.close();
			session.close();
			connection.close();
			System.out.println("Sending successful!");
		}
		catch (JMSException ex)
		{
			System.out.println(ex);
		}
	}

	/**
	 * @see de.freese.acc.ejb.client.IDepotStorage#register(java.lang.String, java.lang.String)
	 */
	@Override
	public Depot register(final String eMailAddress, final String password)
	{
		Depot depot = new Depot(eMailAddress, password);
		this.entityManager.persist(depot);
		notifyClients(depot);

		return depot;
	}
}
