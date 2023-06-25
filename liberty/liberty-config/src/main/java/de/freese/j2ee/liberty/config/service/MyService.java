// Created: 26.05.2018
package de.freese.j2ee.liberty.config.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Thomas Freese
 */
public interface MyService {
    LocalDateTime getSysDate() throws SQLException;

    Map<String, String> getSystemProperties();
}
