package se.skltp.av

import static se.skltp.av.util.AvUtil.*
import groovy.transform.ToString;

/**
 * Currently named User instead of Anvandare to map with ShiroDbRealm
 *
 */
@ToString
class User {
		
	String namn
	String username
	String passwordHash
	String epost
	String telefonNummer
	Date datumSkapad
	Date datumUppdaterad

	static hasMany = [ roles: Role, permissions: String, tjansteKomponenter: TjansteKomponent ]

	static constraints = {
		namn nullable: false, blank: false 
		username nullable: false, blank: false, unique: true
		passwordHash nullable: false, blank: false
		datumSkapad nullable: false
		datumUppdaterad nullable: true
		epost email: true, blank: false
		telefonNummer nullable: true
	}

	def beforeInsert() {
		if (!datumSkapad) {
			datumSkapad = new Date()
		}
	}
	
	def beforeUpdate() {
		datumUppdaterad = new Date()
	}
}
