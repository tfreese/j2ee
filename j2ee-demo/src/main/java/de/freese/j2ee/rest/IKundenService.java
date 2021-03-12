/**
 * Created: 23.05.2013
 */

package de.freese.j2ee.rest;

import java.util.List;
import de.freese.j2ee.model.Kunde;

/**
 * @author Thomas Freese
 */
// @Remote
@FunctionalInterface
public interface IKundenService
{
    /**
     * @return {@link List}
     */
    public List<Kunde> getData();
}
