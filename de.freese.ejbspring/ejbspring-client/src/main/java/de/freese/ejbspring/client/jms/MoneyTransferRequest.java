package de.freese.ejbspring.client.jms;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author Thomas Freese
 */
public class MoneyTransferRequest implements Runnable
{
    /**
     *
     */
    private final Double betrag;

    /**
     * @param betrag Double
     */
    public MoneyTransferRequest(final Double betrag)
    {
        super();

        this.betrag = betrag;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        try
        {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(System.getProperty("broker.url", "tcp://localhost:12345"));

            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer =
                    session.createProducer(session.createQueue(System.getProperty("transfer.request.destination", "transfer.request.destination")));

            TextMessage message = session.createTextMessage(this.betrag != null ? this.betrag.toString() : null);
            message.setJMSReplyTo(session.createQueue(System.getProperty("transfer.response.destination", "transfer.response.destination")));

            producer.send(message);

            producer.close();
            session.close();
            connection.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
