// Created: 22 März 2025
package de.freese.liberty.login;

import java.util.concurrent.TimeUnit;

import jakarta.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <a href="http://localhost:7080/liberty-login/page">localhost</a>
 *
 * @author Thomas Freese
 */
@Stateless
public class LoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    public boolean login(final String user, final String password) {
        try {
            TimeUnit.SECONDS.sleep(3);
        }
        catch (InterruptedException ex) {
            LOGGER.error(ex.getMessage(), ex);

            // Restore interrupted state.
            Thread.currentThread().interrupt();
        }

        return "a".equals(user);
    }
}
