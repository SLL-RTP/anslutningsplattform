package se.skltp.av

import groovy.transform.ToString;

import java.util.Date;

import static se.skltp.av.util.AvUtil.*


@ToString
class ProducentAnslutning {
	
	String rivTaProfile
	String url
	String tjansteKontrakt
	Date validFromTime
	Date validToTime
	
	//To cascade deletes when ProducentBestallning is removed
	static belongsTo = [producentBestallning: ProducentBestallning]
	
	static hasMany = [logiskaAdresser: LogiskAdress, nyaLogiskaAdresser: LogiskAdress, borttagnaLogiskaAdresser: LogiskAdress]

    static constraints = {
		rivTaProfile nullable: false, blank: false 
		url nullable: false, blank: false
		tjansteKontrakt nullable: false, blank: false
		validFromTime nullable: false, blank: false
		validToTime nullable: false, blank: false
    }
	
	def beforeInsert() {
		if (validFromTime == NULL_DATE) {
			validFromTime = new Date()
		}
		
		if (validToTime == NULL_DATE) {
			validToTime = new Date() + hundredYears
		}
	}
}
