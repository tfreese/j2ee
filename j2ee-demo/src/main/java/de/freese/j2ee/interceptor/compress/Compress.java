// Created: 14 März 2025
package de.freese.j2ee.interceptor.compress;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jakarta.ws.rs.NameBinding;

/**
 * @author Thomas Freese
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Compress {
}
