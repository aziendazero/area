Abilitare SSL sul tomcat vedi file server.xml i cui punti importanti sono

    <Connector connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443"/>

    <Connector SSLEnabled="true" clientAuth="false" keystoreFile="C:\certificati\tomcat" 
    	keystorePass="sslssl" maxThreads="150" port="8443" 
    	protocol="org.apache.coyote.http11.Http11Protocol" scheme="https" secure="true" 
    	sslProtocol="TLS"/>

Per autenticare su bonita ed evitare l'errore 
	javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated
utilizzare java 1.7.0_75

utilizzare tomcat 7.0.53

ottenere i certificati di Bonita e del CAS tramite browser, selezionare il lucchetto in alto a sinistra
a fianco dell'URL.

Una volta salvati i file .crt lanciare il comando seguente per importarli nella JRE:

> ${PATH_JRE}/keytool -import -keystore ${path_JRE}/lib/security/cacerts -file ${PATH_CERTIFICATO_CAS} -alias ${ALIAS_CAS}
> ${PATH_JRE}/keytool -import -keystore ${path_JRE}/lib/security/cacerts -file ${PATH_CERTIFICATO_BONITA} -alias ${ALIAS_BONITA}

la password di default è changeit
