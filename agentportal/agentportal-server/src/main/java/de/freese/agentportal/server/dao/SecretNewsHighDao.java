// Created: 14.12.2012
package de.freese.agentportal.server.dao;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;

import de.freese.agentportal.common.model.SecretNews;

/**
 * @author Thomas Freese
 */
@Stateless
// (name = "secureNewsDAO", mappedName = "secureNewsDAO")
@LocalBean
// @Secured
public class SecretNewsHighDao extends AbstractSecretNewsDao {
    /**
     * @see AbstractSecretNewsDao#getSecurityLevel()
     */
    @Override
    protected int getSecurityLevel() {
        return SecretNews.SECURITY_LEVEL_HIGH;
    }
}
