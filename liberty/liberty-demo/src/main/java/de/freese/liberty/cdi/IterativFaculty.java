package de.freese.liberty.cdi;

/**
 * @author Thomas Freese
 */
@Iterative
public class IterativFaculty implements Faculty {
    @Override
    public long getResult(final int value) {
        long result = 1;

        for (int i = 1; i <= value; i++) {
            result *= i;
        }

        return result;
    }
}
