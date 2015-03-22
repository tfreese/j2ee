package de.freese.ejbspring.jms;

import de.freese.ejbspring.facade.IMoneyTransferFacade;
import de.freese.ejbspring.facade.IMoneyTransferResponse;
import de.freese.ejbspring.facade.impl.MoneyTransferRequestImpl;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 *
 * JMS-listener zum Geldtransfer.
 *
 * @author Thomas Freese
 *
 */
public class TransferListener implements MessageListener
{
    /**
     *
     */
    private IMoneyTransferFacade transferFacade = null;

    /**
     *
     */
    private JmsTemplate jmsTemplate = null;

    /**
     *
     */
    public TransferListener()
    {
        super();
    }

    /**
     *
     * @param transferService IMoneyTransferFacade
     */
    public void setTransferFacade(IMoneyTransferFacade transferFacade)
    {
        this.transferFacade = transferFacade;
    }

    /**
     *
     * @param jmsTemplate JmsTemplate
     */
    public void setJmsTemplate(JmsTemplate jmsTemplate)
    {
        this.jmsTemplate = jmsTemplate;
    }

    /**
     *
     * @param message Message
     */
    @Override
    public synchronized void onMessage(Message message)
    {
        if (message instanceof MapMessage)
        {
            MapMessage mapMessage = (MapMessage) message;

            try
            {
                String konto = mapMessage.getString("konto");
                Double betrag = mapMessage.getDouble("betrag");

                IMoneyTransferResponse response = this.transferFacade.transfer(new MoneyTransferRequestImpl(konto, betrag));

                replyTo(message.getJMSReplyTo(), response.getKontostand().intValue(), "OK");
            }
            catch (Throwable th)
            {
                th.printStackTrace();

                try
                {
                    replyTo(message.getJMSReplyTo(), -1, th.getMessage());
                }
                catch (JMSException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Reply to a destination with a certain status code and status message
     *
     * @param destination
     *                    - where to reply to
     * @param code
     *                    - 0 for success, non-zero otherwise
     * @param message
     *                    - message regarding operation status
     */
    private void replyTo(Destination destination, final int code,
                         final String message)
    {
        if (destination != null)
        {
            this.jmsTemplate.send(destination, new MessageCreator()
            {
                public Message createMessage(Session session)
                        throws JMSException
                {
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setIntProperty("code", code);
                    mapMessage.setStringProperty("msg", message);

                    return mapMessage;
                }
            });
        }
    }
}
