// Created: 14.12.2012
package de.freese.agentportal.server.dao;

import java.util.List;

import de.freese.agentportal.common.model.SecretNews;
import jakarta.ejb.Local;

/**
 * @author Thomas Freese
 */
@Local
public interface ISecretNewsDao
{
    /**
     * @param id long
     *
     * @return {@link SecretNews}
     */
    SecretNews get(long id);

    /**
     * @return {@link List}
     */
    List<SecretNews> getNews();
}
