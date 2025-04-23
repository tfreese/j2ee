package de.freese.liberty.cdi;

import java.util.function.IntToLongFunction;

/**
 * @author Thomas Freese
 */
public interface Faculty extends IntToLongFunction {
    @Override
    default long applyAsLong(final int value) {
        return getResult(value);
    }

    long getResult(int value);
}
