package de.freese.acc.client;

import de.freese.acc.ejb.client.IDepotStorage;
import de.freese.acc.ejb.client.ISecurityStorage;
import de.freese.acc.ejb.client.ITrader;
import javafx.application.Application;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

/**
 * @author Thomas Freese
 */
public class Main
{
	/**
	 * 
	 */
	@Resource(lookup = "jms/accConnectionFactory")
	public static TopicConnectionFactory connectionFactory = null;

	/**
	 * 
	 */
	@EJB
	public static IDepotStorage depotStorage = null;

	/**
	 * 
	 */
	@Resource(lookup = "jms/securities")
	public static Topic securitiesTopic = null;

	/**
	 * 
	 */
	@EJB
	public static ISecurityStorage securityStorage = null;

	/**
	 * 
	 */
	@Resource(lookup = "jms/depots")
	public static Topic topic = null;

	/**
	 * 
	 */
	@EJB
	public static ITrader trader = null;

	/**
	 * @param args String[]
	 */
	public static void main(final String[] args)
	{
		Application.launch(JavaFxMain.class, args);
	}
}
