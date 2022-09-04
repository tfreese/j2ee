// Created: 25.05.2013
package de.freese.agentportal.client.ejb;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author Thomas Freese
 */
class TestClientEJB
{
    /**
     * @throws Exception Falls was schiefgeht.
     */
    @Test
    void glassfishAccess() throws Exception
    {
        // TODO
        // glassfish/lib/appserv-rt.jar im ClassPath braucht keine Config für lokale Maschine.
        // Context ctx = new InitialContext();
        //
        // ISecretNewsService service =
        // (ISecretNewsService) ctx
        // .lookup("java:global/de.freese.agentportal.server/SecretNewsEJB!de.freese.agentportal.common.service.ISecretNewsService");
        // List<SecretNews> news = service.getAllSecretNews4High();
        //
        // for (SecretNews secretNews : news)
        // {
        // System.out.println(secretNews);
        // }

        assertTrue(true);
    }
}
