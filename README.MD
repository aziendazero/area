# Sistema di Autorizzazione e Accreditamento delle Strutture Sanitarie, Socio-Sanitarie e Sociali
___

L’applicativo per la gestione dei procedimenti di autorizzazione e accreditamento delle strutture sanitarie, socio-sanitarie e sociali della Regione del Veneto (A.re.A.) è formato da due differenti moduli software:

* Modulo gestionale, che permette di censire le entità del Sistema (Titolari, Unità di Offerta, Strutture fisiche), e consiste in un applicativo sviluppato in Java, basato su RDBMS (Oracle).

* Modulo documentale, che permette di gestire la presentazione delle domande di Au/Ac e la documentazione costituita nello svolgimento dei procedimenti di Au/Ac, che consiste in un applicativo sviluppato in Java, che utilizza la piattaforma documentale eXtraWay. La gestione dei processi viene effettuata con BPM Bonita Open Solution, integrato con il sistema di gestione documentale.

La parte documentale si basa dunque sul database XML nativo Extraway, che è già utilizzato dalla Regione Veneto come piattaforma documentale (Console documentale O.P.E.R.A., progetto sistema documentale regionale Do.Ge.).

L’applicativo AuAc è una web application scritta in Java, utilizzando il framework Spring MVC, che si interfaccia con eXtraWay tramite webservice e con l’RDBMS tramite JPA.

I moduli utilizzati dalla Web Application (applicativo documentale A.re.A.) sono i seguenti:
    • Database XML eXtraWay Server;
    • Piattaforma documentale eXtraWay Platform;

Per i requisiti software e hardware di installazione di eXtraWay Server e della eXtraWay Platform è possibile fare riferimento alla wiki3D
(http://wiki.3di.it/doku.php?id=documentazione_3di:extraway_os:installazione_linux).

Sebbene non sia necessario installare le due applicazioni sulla stessa macchina, è conveniente utilizzare la stessa macchina e lo stesso server container per entrambe le applicazioni.

##### Integrazioni con altri sistemi regionali
A.re.A. è stato integrato all’interno dei sistemi regionali già esistenti, dove possibile. In particolar modo sono state effettuate queste integrazioni:
* Integrazione con il CAS, sistema di single-sign-on utilizzato dalla Regione del Veneto, per gestire l’autenticazione degli utenti in A.re.A.
* Integrazione con le Anagrafiche regionali relative a Comuni, Province, Cap, Codice Catastali
* Integrazione con la gestione regionale del sistema di monitoraggio delle Rete di Assistenza del NSIS (flussi ministeriali HSP, STS, RIA)
* Integrazione con il sistema degli Atti Aziendali regionale, per la gestione degli organigrammi
* Integrazione con il Portale Sanità Regione del Veneto (https://salute.regione.veneto.it/web/area)

##### Utenti del Sistema
Gli utenti del Sistema saranno utenti interni alla Regione, appartenenti alle Direzioni Regionali Sanitarie/Socio-Sanitarie e Sociali, e utenti esterni (Titolari di strutture Sanitarie, Socio-Sanitarie, Sociali della Regione Veneto).
Il numero di utenti stimato che utilizzerà l’applicativo a regime è di circa 8.400 utenti per quanto riguarda i Titolari e i relativi Operatori; ogni Titolare potrà a sua volta registrare una serie di anagrafiche interne per la gestione di una parte della domanda (requisiti di autovalutazione).
Considerando anche gli utenti interni regionali, il sistema potrebbe contare a regime circa 10.000 utenti. Il picco di lavoro sarà massimo in prossimità delle date di scadenza per la presentazione delle domande di Au/Ac, più contenuto nelle fasi di valutazione delle domande o di preparazione delle stesse.
___

#### Configurazione

Tutte le configurazioni si trovano all'interno del file _auac.properties_

```properties_
# Configurazione server eXtraWay
xwHost=127.0.0.1
xwPort=4859
xwUser=xwservice
xwPassword=reader
xwDb=xdocwaydoc

# Configurazione webservices
xwWsUrl=http://localhost:8080/3diws/services/eXtraWay
xwWsAuthUser=admin
xwWsAuthPassword=tomcat

# Configurazione notifiche via email
abilitaInvioMail=S
#smtpHost=smtp-relay.gmail.com
#smtpPort=25
smtpHost=smtp.fqdn.net
smtpPort=25
smtpProtocol=smtp
smtpUser=
smtpPwd=
Debug=S
mailPrincipale=root@3di.it
mailDebug=root@3di.it
nome_persona=xwservice xwservice
nome_uff=Amministrazione

# Configurazione Oracle RDBMS
db.driverClassName=oracle.jdbc.OracleDriver
db.url=jdbc:oracle:thin:@//127.0.0.1:1521/AUACPDB
db.username=AUAC_USR
db.password=auac_pwd

# Configurazione Applicativo
san_reg_ven.url=https://auac.fqdn/SanitaRegioneVenetoDebug/SanitaRegioneVeneto.htm
#uguale in tutti i db test produzione nostri e regione
ambito_ambulatoriale_clientid=E2CC3968-5803-9F1A-E040-007F010142E2
#Impostare S per abilitare
abilitaPerformanceMonitor=N
casLogoutUrl=https://cas.regione.veneto.it/cas/logout
#Indica la data per cui le domande create dopo tale data avranno la gestione dei requisiti delle strutture
dataCreazioneDomandeDopoCuiInserireRequisitiStrutture=20170622
dataCreazioneDomandeDopoCuiGestireListeRequisitiGeneraliSR=20170906
dataCreazioneDomandeDopoCuiInserireRequisitiEdifici=20171007
dataCreazioneDomandeDopoCuiGestireRequisitiDiversificatiPerTipoDomanda=20190924

#Import domanda requisiti from csv
creazioneDomandaRequisitiDaCsv=true
creazioneDomandaRequisitiDaCsvPathCsv=/opt/it-3di/temp
creazioneDomandaRequisitiDaCsvTipoRispostaClientid=FBF2FC86-7B42-C301-E040-A8C083676BF5
creazioneDomandaRequisitiDaCsvListRequisitiClientid=48C5CCA0-6DF4-7B6E-C171-B80AF0EE8017

# Configurazione Bonita BPM
bonita.bonitaviewserverurl=https://auac.fqdn/Bonita7View
bonita.serverurl=https://auac.fqdn
bonita.applicationname=bonita
bonita.serverformurl=
bonita.serverformapplicationname=
bonita.sessioncookiedomain=
bonita.sessioncookiename=JSESSIONID
bonita.admin.username=admin
bonita.admin.password=bonita_pwd
bonita.users.password=password
```

#### Status del progetto

- stabile

#### Limitazioni sull'utilizzo del progetto:

Il presente modulo della piattaforma documentale è stato realizzato facendo uso di Oracle RDBMS, Bonita BPM 6.x e eXtraWay XML Database.

___
#### Detentore del copyright:
Regione del Veneto
___
#### Soggetto incaricato del mantenimento del progetto open source:
| 3D Informatica srl |
| :------------------- |
| Via Speranza, 35 - 40068 S. Lazzaro di Savena |
| Tel. 051.450844 - Fax 051.451942 |
| http://www.3di.it |

___
#### Indirizzo e-mail a cui inviare segnalazioni di sicurezza:
tickets@3di.it
