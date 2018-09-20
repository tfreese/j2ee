// Created: 14.02.2017
package de.freese.j2ee.liberty.spring;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.sql.DataSource;
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
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RestService
{
    /**
     *
     */
    @Resource
    private DataSource dataSource = null;

    /**
     * Erzeugt eine neue Instanz von {@link RestService}
     */
    public RestService()
    {
        super();
    }

    /**
     * http://localhost:PORT/liberty-spring/ping
     *
     * @return boolean
     */
    @GetMapping("/ping")
    public boolean ping()
    {
        return true;
    }

    /**
     * http://localhost:PORT/liberty-spring/sysdate
     *
     * @return String
     * @throws Exception Falls was schief geht.
     */
    @GetMapping("/sysdate")
    public String sysdate() throws Exception
    {
        String sysDate = null;
        // sysDate =
        // LocalDateTime.now().toString() + " at " + InetAddress.getLocalHost().getHostName() + "/" + InetAddress.getLocalHost().getCanonicalHostName();

        try (Connection con = this.dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("VALUES (CURRENT_DATE)"))
        {
            rs.next();
            sysDate = rs.getDate(1).toString();
        }

        return sysDate + " at " + InetAddress.getLocalHost().getHostName();
    }
}
