// Created: 26.05.2018
package de.freese.j2ee.liberty.config.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 * @author Thomas Freese
 */
// @Local
public interface MyService
{
    /**
     * @return {@link Date}
     *
     * @throws SQLException Falls was schiefgeht.
     */
    public Date getSysDate() throws SQLException;

    /**
     * @return {@link Map}
     */
    public Map<String, String> getSystemProperties();
}
