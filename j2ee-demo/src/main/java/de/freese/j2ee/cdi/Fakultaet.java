package de.freese.j2ee.cdi;

/**
 * @author Thomas Freese
 */
@FunctionalInterface
public interface Fakultaet
{
    /**
     * @param n int
     *
     * @return long
     */
    long getFakultaet(int n);
}
