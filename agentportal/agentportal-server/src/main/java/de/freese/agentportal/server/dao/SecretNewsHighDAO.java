// Created: 14.12.2012
package de.freese.agentportal.server.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import de.freese.agentportal.common.model.SecretNews;

/**
 * @author Thomas Freese
 */
@Stateless
// (name = "secureNewsDAO", mappedName = "secureNewsDAO")
@LocalBean
// @Secured
public class SecretNewsHighDAO extends AbstractSecretNewsDAO
{
    /**
     * Erstellt ein neues {@link SecretNewsHighDAO} Object.
     */
    public SecretNewsHighDAO()
    {
        super();
    }

    /**
     * @see de.freese.agentportal.server.dao.AbstractSecretNewsDAO#getSecurityLevel()
     */
    @Override
    protected int getSecurityLevel()
    {
        return SecretNews.SECURITY_LEVEL_HIGH;
    }
}
