package se.skltp.av.services.dto

import groovy.transform.ToString
import se.skltp.av.util.BestallningsStatus

@ToString(includeNames=true, includeFields = true)
class ProducentBestallningDTO {
	
	long id
	
	boolean slaFullfilled
	String otherInfo
	BestallningsStatus status
	
	TjansteDomanDTO serviceDomain
    TjansteKomponentDTO serviceComponent
	TjansteKomponentDTO serviceConsumer
    DriftMiljoDTO targetEnvironment

	AnsvarigDTO client
	List<TjansteKontraktDTO> serviceContracts
}