package se.skltp.av

import groovy.transform.ToString;

@ToString
class ProducentBestallning {
	
	String miljo
	
	String status

    String roleOfClient
	
	static belongsTo = [tjansteKomponent: TjansteKomponent]
		
	static hasMany = [bestallningsHistorik: BestallningsHistorik, producentAnslutning: ProducentAnslutning]
	
    static constraints = {
		miljo nullable: false, blank: false
		status nullable: false, blank: false
    }
}
