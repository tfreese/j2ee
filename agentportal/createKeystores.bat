@echo off
setlocal

cd /D %~dp0

echo Server Keystore anlegen:
pushd "jboss"
keytool -genkey -alias jbossas -keystore server.keystore -keyalg rsa -keysize 4096 -validity 3650

echo Oeffentliches Certifikat fuer Clients exportieren:
keytool -export -alias jbossas -keystore server.keystore -file %~dp0jbossas.cert
popd

echo Certifikat des Servers in KeyStore des Clients importieren:
pushd "agent-portal-client/src/main/resources"
keytool -import -alias jbossas -file %~dp0jbossas.cert -keystore client.keystore

endlocal