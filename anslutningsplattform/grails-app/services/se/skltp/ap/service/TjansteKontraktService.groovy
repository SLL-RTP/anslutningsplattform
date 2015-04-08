package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.services.dto.TjansteKontraktDTO
import se.skltp.ap.util.TjanstekontraktUtil;


@Transactional(readOnly = true)
class TjansteKontraktService {

    def rivTaService
	def takService

    List<TjansteKontraktDTO> getServiceContracts(String hsaId, String environmentId, String serviceDomainId) {
		log.debug("getServiceContracts for: hsaId: ${hsaId}, environmentId: ${environmentId}, serviceDomainId: ${serviceDomainId}")
		
        List<TjansteKontraktDTO> serviceContractsInDomain = rivTaService.getTjansteKontraktForDoman(serviceDomainId)
		log.debug("RIVTA: number of serviceContractsInDomain: " + serviceContractsInDomain.size())
		
		// find out which service contracts are installed in environment
		List<se.skltp.ap.service.tak.m.TjanstekontraktDTO> serviceContractsForEnvironment =
			takService.getAllTjanstekontrakt(environmentId)
		log.debug("TAK: total number of serviceContractsForEnvironment: " + serviceContractsForEnvironment.size())
		
		// find out which service contracts are installed for a service producer (hsaId)
		List<se.skltp.ap.service.tak.m.TjanstekontraktDTO> serviceContractsForHsaId =
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
		List<se.skltp.ap.service.tak.m.TjanstekontraktDTO> list) {
		list.each {
			if (namespaceEqual(dto, it)) {
				dto.installedInEnvironment = true
				log.debug("markIfContractMatchForEnvironment: match for: ${dto.namnrymd}")
			}
		}
	}

	void markIfContractMatchForHsaId(TjansteKontraktDTO dto,
		List<se.skltp.ap.service.tak.m.TjanstekontraktDTO> list) {
		list.each {
			if (namespaceEqual(dto, it)) {
				dto.installedForProducerHsaId = true
				log.debug("markIfContractMatchForHsaId: match for: ${dto.namnrymd}")
			}
		}
	}
	
	boolean namespaceEqual(TjansteKontraktDTO rivTaDTO,
			se.skltp.ap.service.tak.m.TjanstekontraktDTO takDTO) {

		TjanstekontraktUtil.isNamnrymdEqual(rivTaDTO.namnrymd, rivTaDTO.majorVersion.toString(),
			takDTO.namnrymd)
	}

}
