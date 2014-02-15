/**
 * Created: 23.05.2013
 */

package de.freese.j2ee.rest;

import de.freese.j2ee.model.Kunde;
import java.util.List;

/**
 * @author Thomas Freese
 */
// @Remote
public interface IKundenService
{
	/**
	 * @return {@link List}
	 */
	public List<Kunde> getData();
}
