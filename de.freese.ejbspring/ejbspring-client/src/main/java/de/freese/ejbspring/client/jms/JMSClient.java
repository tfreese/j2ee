/**
 * Created on 22.03.2015 09:23:02
 */
package de.freese.ejbspring.client.jms;

/**
 * @author Thomas Freese
 */
public class JMSClient
{
    /**
     * @param args String[]
     * <p>
     * @throws Exception Falls was schief geht.
     */
    public static void main(final String[] args) throws Exception
    {
        new Thread(new MoneyTransferRequest(100.56)).start();
        new Thread(new MoneyTransferResponse()).start();

//        new Thread(new MoneyTransferRequest(null)).start();
//        new Thread(new MoneyTransferResponse()).start();
    }
}
