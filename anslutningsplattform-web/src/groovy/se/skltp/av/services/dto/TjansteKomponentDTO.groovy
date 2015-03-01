package se.skltp.av.services.dto

import groovy.transform.ToString
@ToString(includeNames=true, includeFields = true)
class TjansteKomponentDTO {
    String hsaId
    String namn
    String huvudAnsvarigNamn
    String huvudAnsvarigEpost
    String huvudAnsvarigTelefon
    String tekniskKontaktEpost
    String tekniskKontaktNamn
    String tekniskKontaktTelefon
    String funktionsBrevladaEpost
    String funktionsBrevladaTelefon
    String ipadress
	String pingForConfiguration
}
