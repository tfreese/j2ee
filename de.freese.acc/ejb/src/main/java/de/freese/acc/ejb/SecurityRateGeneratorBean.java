package de.freese.acc.ejb;

import de.freese.acc.model.Rate;
import de.freese.acc.model.Security;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.annotation.Resource;
import javax.ejb.Schedule;
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
public class SecurityRateGeneratorBean
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
	@Resource(lookup = "jms/securities")
	private Topic topic = null;

	/**
	 * Erstellt ein neues {@link SecurityRateGeneratorBean} Object.
	 */
	public SecurityRateGeneratorBean()
	{
		super();
	}

	/**
	 * @param security {@link Security}
	 * @return {@link Rate}
	 */
	private Rate createNewRateFor(final Security security)
	{
		Rate latestRate = security.getLatestRate();
		float rateChange = determineRateChange(latestRate.getRate());

		return new Rate(new Date(), rateChange);
	}

	/**
	 * @param latestRate float
	 * @return float
	 */
	private float determineRateChange(final float latestRate)
	{
		Random random = new Random(System.currentTimeMillis() + (int) latestRate);
		int sign = 1;

		if (random.nextBoolean())
		{
			sign = -1;
		}

		float rateChange = (latestRate * 0.1f) * sign * (random.nextInt(100) / 100.0f);

		return latestRate + rateChange;
	}

	/**
	 * 
	 */
	@Schedule(hour = "*", minute = "*", second = "0")
	public void generateSecurityRates()
	{
		System.out.println("SecurityRateGeneratorBean.generateSecurityRates()");

		TypedQuery<Security> query = this.entityManager.createNamedQuery("findAllSecurities", Security.class);
		List<Security> securities = query.getResultList();

		for (Security security : securities)
		{
			security.getRates().add(createNewRateFor(security));
			this.entityManager.persist(security);
		}

		notifyClients();
	}

	/**
	 * 
	 */
	private void notifyClients()
	{
		System.out.println("SecurityRateGeneratorBean.notifyClients()");

		try (Connection connection = this.connectionFactory.createConnection())
		{
			try (Session session = connection.createSession(true, 0))
			{
				try (MessageProducer messageProducer = session.createProducer(this.topic))
				{
					connection.start();
					messageProducer.send(session.createTextMessage("Securities updated."));
				}
			}

			System.out.println("Sending successful!");
		}
		catch (JMSException ex)
		{
			System.out.println(ex);
		}
	}
}
