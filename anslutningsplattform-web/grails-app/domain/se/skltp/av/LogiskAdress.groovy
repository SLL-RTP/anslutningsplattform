package se.skltp.av

import groovy.transform.ToString;

@ToString
class LogiskAdress {
	
	String hsaId
	
	static belongsTo = [ProducentAnslutning, KonsumentAnslutning]
	
	static hasMany = [producentAnslutning: ProducentAnslutning, konsumentAnslutning: KonsumentAnslutning]

    static constraints = {
		hsaId unique: true, nullable: false, blank: false
    }
	
}
