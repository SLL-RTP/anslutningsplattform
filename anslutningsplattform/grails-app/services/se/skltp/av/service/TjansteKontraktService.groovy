package se.skltp.av.service

import se.skltp.av.service.tak.m.TjanstekontraktDTO
import java.util.List
import grails.transaction.Transactional
import se.skltp.av.services.dto.TjansteKontraktDTO


@Transactional(readOnly = true)
class TjansteKontraktService {

    def rivTaService
	def takService

    List<TjansteKontraktDTO> getServiceContracts(String hsaId, String environmentId, String serviceDomainId) {
		log.debug("getServiceContracts for: hsaId: ${hsaId}, environmentId: ${environmentId}, serviceDomainId: ${serviceDomainId}")
		
        List<TjansteKontraktDTO> serviceContractsInDomain = rivTaService.getTjansteKontraktForDoman(serviceDomainId)
		log.debug("RIVTA: number of serviceContractsInDomain: " + serviceContractsInDomain.size())
		
		// find out which service contracts are installed in environment
		List<se.skltp.av.service.tak.m.TjanstekontraktDTO> serviceContractsForEnvironment =
			takService.getAllTjanstekontrakt(environmentId)
		log.debug("TAK: total number of serviceContractsForEnvironment: " + serviceContractsForEnvironment.size())
		
		// find out which service contracts are installed for a service producer (hsaId)
		List<se.skltp.av.service.tak.m.TjanstekontraktDTO> serviceContractsForHsaId =
			takService.getTjanstekontraktByHsaId(environmentId, hsaId)
		log.debug("TAK: total number of serviceContractsForHsaId: " + serviceContractsForHsaId.size())
						
		// enrich final result with info from TAK
		serviceContractsInDomain.each {
			// TODO: match from - to dates for virtualiseringar when finding contacts for HSA-id?
			markIfContractMatchForEnvironment(it, serviceContractsForEnvironment)
			
			markIfContractMatchForHsaId(it, serviceContractsForHsaId)
						
			//log.debug("marked environment status for service contract: ${it}")
		}
		
		serviceContractsInDomain
    }
	
	void markIfContractMatchForEnvironment(TjansteKontraktDTO dto,
		List<se.skltp.av.service.tak.m.TjanstekontraktDTO> list) {
		list.each {
			if (namespaceEqual(dto, it)) {
				dto.installedInEnvironment = true
				log.debug("markIfContractMatchForEnvironment: match for: ${dto.namnrymd}")
			}
		}
	}

	void markIfContractMatchForHsaId(TjansteKontraktDTO dto,
		List<se.skltp.av.service.tak.m.TjanstekontraktDTO> list) {
		list.each {
			if (namespaceEqual(dto, it)) {
				dto.installedForProducerHsaId = true
				log.debug("markIfContractMatchForHsaId: match for: ${dto.namnrymd}")
			}
		}
	}
	
	// match RIV-TA info against TAK-info where (example):
	// RIV-TA: namnrymd: crm:scheduling:GetSubjectOfCareSchedule
	// RIV-TA: majorVersion: 1
	// TAK: namnrymd: urn:riv:crm:scheduling:GetSubjectOfCareSchedule:1:rivtabp21
	// TAK: majorVersion: 1
	boolean namespaceEqual(TjansteKontraktDTO rivTaDTO,
			se.skltp.av.service.tak.m.TjanstekontraktDTO takDTO) {

		takDTO.namnrymd.contains(rivTaDTO.namnrymd) && takDTO.majorVersion == rivTaDTO?.majorVersion?.toString()
	}

}
