Start Application-Client:

Siehe glassfish-launch-appclient.png

1.  Launch - Start über Web-Starter:	http://${SERVER}:${PORT}/acc/acc 
										http://${SERVER}:${PORT}/acc/client-0.0.1-SNAPSHOT  
											
2.  Download Client Stubs: 
	Start App-Client:   ${GLASSFISH4}/glassfish/bin/appclient -jar accClient/client-0.0.1-SNAPSHOTClient.jar
						${JBOSS}/bin/.appClient.sh --host localhost myear.ear#appclient.jar arg1	

	