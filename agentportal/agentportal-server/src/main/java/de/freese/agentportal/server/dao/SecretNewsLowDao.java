// Created: 14.12.2012
package de.freese.agentportal.server.dao;

import de.freese.agentportal.common.model.SecretNews;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;

/**
 * @author Thomas Freese
 */
@Stateless
@LocalBean
// @Unsecured
public class SecretNewsLowDao extends AbstractSecretNewsDao
{
    /**
     * @see AbstractSecretNewsDao#getSecurityLevel()
     */
    @Override
    protected int getSecurityLevel()
    {
        return SecretNews.SECURITY_LEVEL_LOW;
    }
}