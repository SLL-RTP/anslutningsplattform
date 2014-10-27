package se.skltp.av

class ProducentAnslutning {
	
	String rivTaProfile
	String url
	String tjansteKontrakt
	Date validFromTime
	Date validToTime
	
	static belongsTo = [producentBestallning: ProducentBestallning]
	
	static hasMany = [logiskAdresser: LogiskAdress]

    static constraints = {
		url(nullable: false, blank: false)
		tjansteKontrakt(nullable: false, blank: false)
    }
	
	String toString(){
		return "$tjansteKontrakt, $url"
	}
}
