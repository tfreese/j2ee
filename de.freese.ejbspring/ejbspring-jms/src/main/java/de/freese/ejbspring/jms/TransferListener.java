package de.freese.ejbspring.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import de.freese.ejbspring.facade.IMoneyTransferFacade;
import de.freese.ejbspring.facade.IMoneyTransferResponse;
import de.freese.ejbspring.facade.impl.MoneyTransferRequestImpl;

/**
 * JMS-listener zum Geldtransfer.
 * 
 * @author Thomas Freese
 */
public class TransferListener implements MessageListener
{
	/**
     *
     */
	private JmsTemplate jmsTemplate = null;

	/**
     *
     */
	private IMoneyTransferFacade transferFacade = null;

	/**
     *
     */
	public TransferListener()
	{
		super();
	}

	/**
	 * @param message Message
	 */
	@Override
	public synchronized void onMessage(final Message message)
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
	 * @param destination - where to reply to
	 * @param code - 0 for success, non-zero otherwise
	 * @param message - message regarding operation status
	 */
	private void replyTo(final Destination destination, final int code, final String message)
	{
		if (destination != null)
		{
			this.jmsTemplate.send(destination, new MessageCreator()
			{
				@Override
				public Message createMessage(final Session session) throws JMSException
				{
					MapMessage mapMessage = session.createMapMessage();
					mapMessage.setIntProperty("code", code);
					mapMessage.setStringProperty("msg", message);

					return mapMessage;
				}
			});
		}
	}

	/**
	 * @param jmsTemplate JmsTemplate
	 */
	public void setJmsTemplate(final JmsTemplate jmsTemplate)
	{
		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * @param transferFacade {@link IMoneyTransferFacade}
	 */
	public void setTransferFacade(final IMoneyTransferFacade transferFacade)
	{
		this.transferFacade = transferFacade;
	}
}
