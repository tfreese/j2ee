// Created: 14.02.2017
package de.freese.j2ee.liberty.spring;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import jakarta.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * curl -X GET -H "Content-type: application/json" -H "Accept: application/json" -v "http://localhost:PORT/liberty-spring/sysdate"<br>
 * curl -X GET "http://localhost:PORT/liberty-spring/sysdate"
 *
 * @author Thomas Freese
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestService {
    @Resource
    private DataSource dataSource;

    /**
     * <a href="http://localhost:PORT/liberty-spring/sysdate">localhost</a>
     */
    @GetMapping("/sysdate")
    public LocalDateTime getSysdate() throws Exception {
        final LocalDateTime sysDate;

        // select CURRENT_TIMESTAMP
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("VALUES (CURRENT_TIMESTAMP)")) {
            rs.next();
            // sysDate = rs.getTimestamp(1).toLocalDateTime();
            sysDate = rs.getObject(1, LocalDateTime.class);
        }

        return sysDate;
        // return sysDate + " at " + InetAddress.getLocalHost().getHostName();
    }

    /**
     * <a href="http://localhost:PORT/liberty-spring/ping">localhost</a>
     */
    @GetMapping("/ping")
    public boolean ping() {
        return true;
    }
}
