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
    Date getSysDate() throws SQLException;

    Map<String, String> getSystemProperties();
}
