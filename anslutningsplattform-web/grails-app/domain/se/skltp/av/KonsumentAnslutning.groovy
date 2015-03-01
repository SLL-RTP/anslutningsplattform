package se.skltp.av

import static se.skltp.av.util.AvUtil.*
import groovy.transform.ToString;


@ToString
class KonsumentAnslutning {
	
	String tjansteKontrakt
	Date validFromTime
	Date validToTime
	
	static belongsTo = [konsumentBestallning: KonsumentBestallning]
	
	static hasMany = [logiskAdresser: LogiskAdress]

    static constraints = {
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
