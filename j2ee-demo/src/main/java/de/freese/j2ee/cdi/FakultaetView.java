package de.freese.j2ee.cdi;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.Max;

/**
 * @author Thomas Freese
 */
@Named
@RequestScoped
public class FakultaetView {
    private long fakt;

    @Inject
    @Recursive
    private Fakultaet fakultaet;

    @Max(5)
    private int n;

    public String calculate() {
        System.out.println("Test " + getN());
        setFakt(this.fakultaet.getFakultaet(getN()));

        return null;
    }

    public long getFakt() {
        return this.fakt;
    }

    public int getN() {
        return this.n;
    }

    public void setFakt(final long fakt) {
        this.fakt = fakt;
    }

    public void setN(final int n) {
        this.n = n;
    }
}
