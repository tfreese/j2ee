package de.freese.agentportal.common.service;

import java.util.List;

import de.freese.agentportal.common.model.SecretNews;

/**
 * @author Thomas Freese
 */
public interface ISecretNewsService
{
	/**
	 * Zugriff nur nach Berechtigung.
	 * 
	 * @return {@link List}
	 */
	public List<SecretNews> getAllSecretNews4High();

	/**
	 * Zugriff nur nach Berechtigung.
	 * 
	 * @return {@link List}
	 */
	public List<SecretNews> getAllSecretNews4Low();
}
