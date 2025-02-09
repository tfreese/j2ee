// Created: 14.02.2017
package de.freese.j2ee.liberty.spring;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
    public String getSysdate() throws Exception {
        final String sysDate;

        try (Connection con = this.dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("VALUES (CURRENT_TIMESTAMP)")) {
            rs.next();
            sysDate = rs.getTimestamp(1).toString();
        }

        return sysDate + " at " + InetAddress.getLocalHost().getHostName();
    }

    /**
     * <a href="http://localhost:PORT/liberty-spring/ping">localhost</a>
     */
    @GetMapping("/ping")
    public boolean ping() {
        return true;
    }
}
