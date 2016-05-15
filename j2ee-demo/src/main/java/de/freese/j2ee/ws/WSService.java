/**
 * Created: 17.05.2013
 */
package de.freese.j2ee.ws;

import de.freese.j2ee.model.Kunde;
import de.freese.j2ee.model.KundeList;
import de.freese.j2ee.persistence.MyEntityManager;
import de.freese.j2ee.rest.StartUp;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.ws.WebServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Stateless
@WebService(name = "webServiceKunden")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class WSService
{
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StartUp.class);

    /**
     *
     */
    @Resource
    private WebServiceContext context = null;

    /**
     *
     */
    @Inject
    @MyEntityManager
    private EntityManager entityManager = null;

    /**
     * Erstellt ein neues {@link WSService} Object.
     */
    public WSService()
    {
        super();
    }

    /**
     * @return {@link List}
     */
    @SuppressWarnings("unchecked")
    @WebMethod(operationName = "getKunden")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public KundeList getData()
    {
        LOGGER.info("");

        Query query = this.entityManager.createQuery("select k from Kunde k");
        List<Kunde> kunden = query.getResultList();

        KundeList kundeList = new KundeList();
        kundeList.setKunden(kunden);

        return kundeList;
    }
}
