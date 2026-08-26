package de.freese.jpa;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.annotations.IdGeneratorType;

/**
 * Sequence won't be generated and must exist.
 *
 * @author Thomas Freese
 * @since 15.05.2024
 */
@IdGeneratorType(BlockSequenceGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface BlockSequence {
    int blockSize() default 5;

    String name();
}
