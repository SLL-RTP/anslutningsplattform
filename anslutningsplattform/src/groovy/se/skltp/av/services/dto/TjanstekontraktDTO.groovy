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
    List<LogiskAdressDTO> logicalAddresses
    List<LogiskAdressDTO> newLogicalAddresses
    List<LogiskAdressDTO> removedLogicalAddresses
    AdressDTO address

}
