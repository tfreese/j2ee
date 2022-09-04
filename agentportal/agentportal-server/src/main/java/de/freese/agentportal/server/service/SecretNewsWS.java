// // Created: 24.05.2012
//
// package de.freese.agentportal.server.service;
//
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;
//
// import javax.annotation.Resource;
// import javax.ejb.Stateless;
// import javax.jws.WebMethod;
// import javax.jws.WebService;
// import javax.jws.soap.SOAPBinding;
// import javax.persistence.EntityManager;
// import javax.persistence.PersistenceContext;
// import javax.xml.ws.WebServiceContext;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import de.freese.agentportal.common.model.SecretNewsEntity;
//
// /**
// * @author Thomas Freese
// */
// @Stateless
// @WebService
// @SOAPBinding(style = SOAPBinding.Style.RPC)
// // @WebContext(urlPattern = "SecretNewsWebService", authMethod = "BASIC", transportGuarantee =
// // "CONFIDENTIAL", secureWSDLAccess = true)
// // contextRoot = "secretws",
// // @SecurityDomain(value = "AgentPortalDomain", unauthenticatedPrincipal = "nobody")
// // @RolesAllowed(
// // {
// // "AgentPortalRoleHigh", "AgentPortalRoleLow"
// // })
// public class SecretNewsWS
// {
// /**
// *
// */
// private final static Logger LOGGER = LoggerFactory.getLogger(SecretNewsWS.class);
//
// /**
// *
// */
// @Resource
// private WebServiceContext context = null;
//
// /**
// *
// */
// @PersistenceContext(unitName = "SecretNewsJPA")
// private EntityManager em = null;
//
// /**
// * Erstellt ein neues {@link SecretNewsWS} Object.
// */
// public SecretNewsWS()
// {
// super();
// }
//
// /**
// * @return {@link List}
// */
// private List<SecretNewsEntity> createNews()
// {
// List<SecretNewsEntity> news = new ArrayList<>();
// Date today = new Date();
//
// SecretNewsEntity entity = new SecretNewsEntity();
// entity.setId(1L);
// entity.setSecurityLevel(SecretNewsEntity.SECURITY_LEVEL_LOW);
// entity.setTitle("Johnny English verhaftet");
// entity.setTimestamp(today);
// entity.setText("Unser Agent Johnny English wurde wieder einmal bei einer geheimen Mission festgenommen. Weitere Informationen sind nicht bekannt und folgen demnächst.");
// news.add(entity);
//
// entity = new SecretNewsEntity();
// entity.setId(2L);
// entity.setSecurityLevel(SecretNewsEntity.SECURITY_LEVEL_HIGH);
// entity.setTitle("James Bond pensioniert");
// entity.setTimestamp(today);
// entity.setText("Unser Agent James Bond wurde mit dem heutigen Tag in Pension geschickt. In unzähligen Missionen stand er seinen Mann, um sein Vaterland zu verteidigen.");
// news.add(entity);
//
// return news;
// }
//
// /**
// * Zugriff nur nach Berechtigung.
// *
// * @return {@link List}
// */
// @WebMethod
// // @RolesAllowed("AgentPortalRoleHigh")
// public List<SecretNewsEntity> getAllSecretNews4High()
// {
// logCallerInfo();
//
// // SecretNewsDAO dao = new SecretNewsDAO(this.em);
// // List<SecretNewsEntity> news = dao.getAllSecretNews4High();
// List<SecretNewsEntity> news = createNews();
//
// return news;
// }
//
// /**
// * Zugriff nur nach Berechtigung.
// *
// * @return {@link List}
// */
// @WebMethod
// // @RolesAllowed(
// // {
// // "AgentPortalRoleHigh", "AgentPortalRoleLow"
// // })
// public List<SecretNewsEntity> getAllSecretNews4Low()
// {
// logCallerInfo();
//
// // SecretNewsDAO dao = new SecretNewsDAO(this.em);
// // List<SecretNewsEntity> news = dao.getAllSecretNews4Low();
// List<SecretNewsEntity> news = createNews();
//
// return news;
// }
//
// /**
// *
// */
// private void logCallerInfo()
// {
// StringBuilder sb = new StringBuilder();
// sb.append("user=");
//
// try
// {
// sb.append(this.context.getUserPrincipal().getName());
// sb.append(", role=");
//
// if (this.context.isUserInRole("AgentPortalRoleHigh"))
// {
// sb.append("AgentPortalRoleHigh");
// }
// else if (this.context.isUserInRole("AgentPortalRoleLow"))
// {
// sb.append("AgentPortalRoleLow");
// }
// else
// {
// sb.append("<unknown>");
// }
// }
// catch (Exception ex)
// {
// sb.append("<unknown>");
// }
//
// SecretNewsWS.LOGGER.info("called from {}", sb.toString());
// }
// }
