package de.freese.j2ee.cdi;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.validation.constraints.Max;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Named
@RequestScoped
public class FakultaetView {
    private static final Logger LOGGER = LoggerFactory.getLogger(FakultaetView.class);

    // @Inject
    // @Recursive
    private final Fakultaet fakultaet;

    private long fakt;

    // @Max(5)
    private int n;

    public FakultaetView(final Fakultaet fakultaet, @Max(5) final int n) {
        super();

        this.fakultaet = fakultaet;
        this.n = n;
    }

    public String calculate() {
        LOGGER.info("Test {}", getN());

        setFakt(fakultaet.getFakultaet(getN()));

        return null;
    }

    public long getFakt() {
        return fakt;
    }

    public int getN() {
        return n;
    }

    public void setFakt(final long fakt) {
        this.fakt = fakt;
    }

    public void setN(final int n) {
        this.n = n;
    }
}
