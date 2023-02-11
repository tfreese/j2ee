package de.freese.j2ee.cdi;

/**
 * @author Thomas Freese
 */
@Recursive
public class RecursiveFakultaet implements Fakultaet {
    /**
     * @see Fakultaet#getFakultaet(int)
     */
    @Override
    public long getFakultaet(final int n) {
        if (n > 1) {
            return n * getFakultaet(n - 1);
        }

        return 1;
    }
}
