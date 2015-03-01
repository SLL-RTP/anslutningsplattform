package se.skltp.av.services.dto

import groovy.transform.ToString

@ToString(includeNames=true, includeFields = true)
class TjansteKontraktDTO {
    String namn
    String namnrymd
    int majorVersion
    int minorVersion
    boolean installedInEnvironment
	boolean installedForProducerHsaId
	
	/*
	String toString() {
		getClass().toString() +
		"\n  namn: ${namn}" +
		"\n  namnrymd: ${namnrymd}" +
		"\n  majorVersion: ${majorVersion}" +
		"\n  minorVersion: ${minorVersion}" +
		"\n  installedInEnvironment: ${installedInEnvironment}" +
		"\n  installedForProducerHsaId: ${installedForProducerHsaId}" 
	}
	*/
}
