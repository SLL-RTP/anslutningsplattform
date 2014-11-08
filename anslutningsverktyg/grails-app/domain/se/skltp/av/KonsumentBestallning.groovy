package se.skltp.av

class KonsumentBestallning {
	
	String miljo

	String status
	
	static belongsTo = [tjansteKomponent: TjansteKomponent]
		
	static hasMany = [bestallningsHistorik: BestallningsHistorik, konsumentAnslutning: KonsumentAnslutning]
	
    static constraints = {
		miljo nullable: false, blank: false 
		status nullable: false, blank: false
    }
}
