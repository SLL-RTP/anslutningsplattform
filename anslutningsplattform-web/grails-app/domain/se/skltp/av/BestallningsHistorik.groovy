package se.skltp.av

import static se.skltp.av.util.AvUtil.*
import groovy.transform.ToString;


@ToString
class BestallningsHistorik {
	
	String status
	Date datum
	String senastUppdateradAv
	
	//To cascade deletes when ProducentBestallning is removed
	static belongsTo = [producentBestallning: ProducentBestallning, konsumentBestallning: KonsumentBestallning]

    static constraints = {
		status(nullable: false, blank: false)
		datum(nullable: false, blank: false)
		senastUppdateradAv(nullable: false, blank: false)
		producentBestallning nullable: true
		konsumentBestallning nullable: true
    }
	
	def beforeInsert() {
		if (datum == NULL_DATE) {
			datum = new Date()
		}
	}
}
