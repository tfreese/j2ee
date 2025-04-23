package de.freese.liberty.cdi;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Named
@RequestScoped
public class FacultyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyController.class);

    @Inject
    @Recursive
    private Faculty faculty;

    // @Max(5)
    private int n = 3;

    private long result;

    public void calculate() {
        LOGGER.info("calculate: {}", getN());

        setResult(faculty.getResult(getN()));
    }

    public int getN() {
        return n;
    }

    public long getResult() {
        return result;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("faculty ref: {}", faculty);
    }

    public void setN(final int n) {
        this.n = n;
    }

    public void setResult(final long result) {
        this.result = result;
    }
}
