package de.freese.liberty.interceptor.compress;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jakarta.ws.rs.NameBinding;

/**
 * @author Thomas Freese
 * @since 14.03.2025
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCompress {
}
