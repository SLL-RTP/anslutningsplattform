# Anslutningsplattform
Backend-applikation till frontend-applikationen Anslutningsverktyg.

## Kör applikation i utvecklingsläge
	cd anslutningsplattform
	grails run-app

## Bygga projekt
	mvn clean install

## Release
	mvn release:prepare
	TODO: spara distribution! (på bintray?)

## Deploy till Tomcat
1. Konfigurera Tomcat enligt resources/tomcat/README_tomcat_deploy.txt
2. Kopiera applikationens data-katalog:
	anslutningsplattform/grails-app/conf/config.dir.external/data
till den data-katalog som konfigurerats i Tomcat
3. I målmijön, konfigurera instansspecifik information
Utgå från en template i data-katalogen:
	anslutningsplattform-config-override.groovy_template-prod.txt
och kopiera den till (i data-katalogen):
	anslutningsplattform-config-override.groovy
samt konfigurera TAK-URL:er, databas-koppling mm i filen.

Exempel på konfiguration av flera TAK-miljöer finns i template enligt ovan.


## Datakällor
Applikationen använder följande datakällor:

1. RIV-TA domäner och kontrakt (från lokal fil, master info på rivta.se)
	
	anslutningsplattform/grails-app/conf/config.dir.external/data/domains.xml

2. HSA-information (från lokal fil, extraherat från öppen data plattform)

	anslutningsplattform/grails-app/conf/config.dir.external/data/hsacache.xml

script finns i katalogen: resources/hsa

3. TAK (Tjänsteadresseringskatalog) (via webservice)
En installation av applikationen kan hämta information från flera TAK-instanser, t ex kan en produktionsinstallation av applikationen stödja beställningar för olika miljöer som produktion och acceptanstest.
