package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.services.dto.HsaDTO;
import se.skltp.ap.services.dto.domain.LogiskAdressDTO

@Transactional(readOnly = true)
class LogiskAdressService {

    def hsaService
	def takService

    List<LogiskAdressDTO> freeTextSearch(String queryString, int limit, String takId) {
        List<HsaDTO> hsaDTOList = hsaService.freeTextSearch(queryString, limit)
        def logiskAdressList = hsaDTOList.collect {
            new LogiskAdressDTO(
                    hsaId: it.hsaId,
                    namn: it.namn
            )
        }
        if (logiskAdressList.size() < limit) {
            log.debug "found ${logiskAdressList.size()} logiska adresser in HSA, also trying TAK"
            def takLogiskaAdresser = takService.freeTextSearchLogiskAdresser(takId, queryString, limit)
            for (LogiskAdressDTO logiskAdressDTO in takLogiskaAdresser) {
                if (!logiskAdressList.find {it.hsaId == logiskAdressDTO.hsaId}) { //prevent duplicates
                    logiskAdressList.push(logiskAdressDTO)
                }
            }
            log.debug "after TAK search we now have ${logiskAdressList.size()} logiska adresser"
        } else {
            log.debug "not searching TAK since we found enough results in HSA"
        }
        logiskAdressList
    }

    List<LogiskAdressDTO> getByEnvironmentAndServiceDomain(String environmentId, String serviceDomainId) {
		//getLogiskAdressMockDTOs(null)
		
		// get all logical addresses for domain from TAK (no description in TAK-API)
		List<String> logicalAddress = takService.getLogicalAddressByServiceDomainNS(environmentId, serviceDomainId)
		// try to find a name/description for logical address from HSA
		List dtoList = new ArrayList()
		logicalAddress.each {
			String name = hsaService.getNameForHsaId(it)
			dtoList.add(new LogiskAdressDTO(hsaId: it, namn: name))
		}
		dtoList
    }

    LogiskAdressDTO getLogiskAdressForHsaId(String takId, String hsaId) {
        def name = hsaService.getNameForHsaId(hsaId)
        if (name.contains('SAKNAS')) {
            name = takService.getNameForHsaId(takId, hsaId)
        }
        new LogiskAdressDTO(hsaId: hsaId, namn: name)
    }

    List<LogiskAdressDTO> getForServiceContract(String serviceComponentId, String environmentId, String serviceContractNamespace, int majorVersion) {		 
		takService.getForServiceContract(serviceComponentId, environmentId, serviceContractNamespace, majorVersion)
    }

    def getKonsumentanslutningarForDoman(String takId, String serviceConsumerHSAId, String serviceDomainNS) {
        takService.getKonsumentanslutningarForDoman(takId, serviceConsumerHSAId, serviceDomainNS)
    }

}
