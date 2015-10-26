package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.services.dto.HsaDTO;
import se.skltp.ap.services.dto.domain.LogiskAdressDTO

@Transactional(readOnly = true)
class LogiskAdressService {

    def hsaService
	def takService

    List<LogiskAdressDTO> freeTextSearch(String queryString, int limit) {
		//getLogiskAdressMockDTOs(queryString)

        List<HsaDTO> hsaDTOList = hsaService.freeTextSearch(queryString, limit)
        hsaDTOList.collect {
            new LogiskAdressDTO(
                    hsaId: it.hsaId,
                    namn: it.namn
            )
        }        
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

    List<LogiskAdressDTO> getForServiceContract(String serviceComponentId, String environmentId, String serviceContractNamespace, int majorVersion) {		 
		takService.getForServiceContract(serviceComponentId, environmentId, serviceContractNamespace, majorVersion)
    }

    def getKonsumentanslutningarForDoman(String takId, String serviceConsumerHSAId, String serviceDomainNS) {
        takService.getKonsumentanslutningarForDoman(takId, serviceConsumerHSAId, serviceDomainNS)
    }

}
