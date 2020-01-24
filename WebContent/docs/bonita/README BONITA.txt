ATTENZIONE NON PIU'
- Form da utilizzare come form di conferma in tutti i form di bonita
	form di conferma\auac_confirm.html
MA
- Form da utilizzare come form di default di conferma da sostituire al form che si trova in webapps\bonita\WEB-INF\classes\html
	form di conferma\bonita_default_confirm.html

- Il file humantasksaved.js che si trova nella cartella "Script js sito bonita", personalizzato per ogni sito, va aggiunto al path
	\webapps\bonita\portal\scripts\auac
	
- Il file css\auac.css va copiato nelle folder:
	BONITA_HOME\client\platform\tenant-template\work\theme\portal\css
	BONITA_HOME\client\tenants\1\work\theme\portal\css
	
- Nei files:
	BONITA_HOME\client\platform\tenant-template\work\theme\portal\BonitaForm.html
	BONITA_HOME\client\tenants\1\work\theme\portal\BonitaForm.html
	va aggiunto il seguente codice
	<!-- css personalizzati -->
	<link type="text/css" rel="stylesheet" href="themeResource?theme=portal&location=css/auac.css&v=6.4.0"/>

- Copiare il file auac.properties nella webapp bonita nella cartella bonita/WEB-INF/classes

- Per modificare la dimensione dei files che si possono inserire nei form di bonita modificare la property, espressa in MB,
form.attachment.max.size 
nel file forms-config.properties presente in BONITA_HOME\client\tenants\1\conf e in BONITA_HOME\client\platform\tenant-template\conf, e riavviare Bonita BPM

- Per modificare la durata della sessione del BPM Engine occorre modificare il file
BONITA_HOME\server\platform\conf\services\cfg-bonita-session-impl.xml 
impostando nel bean sessionService la sessionDuration, espressa in millisecondi, al valore che si vuole, per esempio per una sessione di 4 ore impostare come segue:
	<bean id="sessionService" class="org.bonitasoft.engine.session.impl.SessionServiceImpl">
		<constructor-arg name="sessionProvider" ref="sessionProvider" />
		<constructor-arg name="applicationName" value="BPM" />
		<constructor-arg name="logger" ref="platformTechnicalLoggerService" />
		<property name="sessionDuration" value="14400000" />
	</bean>
inoltre accedendo ai form di bonita direttamente sul portale tramite URL occorre modificare anche la sessione della webapp bonita su tomcat aggiungendo al file web.xml la sezione
	<session-config>
		<session-timeout>240</session-timeout>
	</session-config>
impostata in minuti nel caso riportato sono state impostate 4 ore 
