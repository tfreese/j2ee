// Created: 12.01.2010
/**
 * 12.01.2010
 */
package de.freese.ldap2jdbc.server;

import de.freese.littlemina.core.acceptor.NioSocketAcceptor;

/**
 * @author Thomas Freese
 */
public class TestLDAP
{
    /**
     * @param args String[]
     */
    public static void main(final String[] args)
    {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();

        try
        {
            acceptor.setHandler(new LdapHandler());

            acceptor.bind(3899);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            // acceptor.dispose();
        }
    }
}
