package de.freese.liberty.cdi;

/**
 * @author Thomas Freese
 */
@Recursive
public class RecursiveFaculty implements Faculty {
    @Override
    public long getResult(final int value) {
        if (value > 1) {
            return value * getResult(value - 1);
        }

        return 1;
    }
}
