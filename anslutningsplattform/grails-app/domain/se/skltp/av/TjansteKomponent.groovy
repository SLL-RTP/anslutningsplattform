package se.skltp.av

import groovy.transform.ToString

@ToString(includeNames=true, includeFields = true)
class TjansteKomponent {

    String hsaId
    String namn
    String organisation
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

    static belongsTo = [user: User]

	static hasMany = [producentBestallningar: ProducentBestallning]

    static constraints = {
      hsaId blank: false, unique: true
      namn nullable: true, blank: true
      organisation nullable: false, blank: false
      tekniskKontaktEpost email: true, nullable: false, blank: false
	  tekniskKontaktNamn nullable: false, blank: false
	  tekniskKontaktTelefon nullable: true, blank: false
	  funktionsBrevladaEpost blank: false
	  funktionsBrevladaTelefon nullable: false, blank: false
	  ipadress nullable: true, blank: false
      pingForConfiguration nullable: true, blank: false
    }
}
