// Created: 14.12.2012
package de.freese.agentportal.server.dao;

import java.util.List;

import jakarta.ejb.Local;

import de.freese.agentportal.common.model.SecretNews;

/**
 * @author Thomas Freese
 */
@Local
public interface ISecretNewsDao {
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
