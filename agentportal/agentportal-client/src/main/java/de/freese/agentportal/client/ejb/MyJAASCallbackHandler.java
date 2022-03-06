// Created: 16.12.2012
package de.freese.agentportal.client.ejb;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;

/**
 * LoginCallback Handler für den EJB Client.
 *
 * @author Thomas Freese
 */
public class MyJAASCallbackHandler implements CallbackHandler
{
    /**
     *
     */
    private static String password;
    /**
     *
     */
    private static String userRealm;
    /**
     *
     */
    private static String username;

    /**
     * @param username String
     * @param password String
     * @param userRealm String
     */
    public static void setCredential(final String username, final String password,
                                     final String userRealm)
    {
        synchronized (MyJAASCallbackHandler.class)
        {
            MyJAASCallbackHandler.username = username;
            MyJAASCallbackHandler.password = password;
            MyJAASCallbackHandler.userRealm = userRealm;
        }
    }

    /**
     * Erstellt ein neues {@link MyJAASCallbackHandler} Object.
     */
    public MyJAASCallbackHandler()
    {
        super();
    }

    /**
     * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
     */
    @Override
    public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException
    {
        synchronized (MyJAASCallbackHandler.class)
        {
            for (Callback current : callbacks)
            {
                if (current instanceof RealmCallback rcb)
                {
                    rcb.setText(MyJAASCallbackHandler.userRealm);
                }
                else if (current instanceof NameCallback ncb)
                {
                    ncb.setName(MyJAASCallbackHandler.username);
                }
                else if (current instanceof PasswordCallback pcb)
                {
                    pcb.setPassword(MyJAASCallbackHandler.password.toCharArray());
                }
                else
                {
                    throw new UnsupportedCallbackException(current);
                }
            }
        }
    }
}
