package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.services.dto.HsaDTO;
import se.skltp.ap.services.dto.LogiskAdressDTO

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

    List<LogiskAdressDTO> getForServiceContract(String serviceComponentId, String environmentId, String serviceContractNamespace, int majorVersion, int minorVersion) {
        getLogiskAdressMockDTOs()
    }

    private static final Random rnd = new Random()
    private static getLogiskAdressMockDTOs() {
        [
                new LogiskAdressDTO(
                        hsaId: 'SE2321000065-7330028000014',
                        namn: 'Kronobergs landsting'
                ),
                new LogiskAdressDTO(
                        hsaId: 'SE2321000065-7365566019822',
                        namn: 'Kronoberg Achima Care'
                ),
                new LogiskAdressDTO(
                        hsaId: 'SE2321000065-7365565696222',
                        namn: 'Kronoberg HälsoRingen Vård'
                ),
                new LogiskAdressDTO(
                        hsaId: 'SE2321000065-7330028091937',
                        namn: 'Kronoberg Smålandshälsan i Ljungby'
                ),
                new LogiskAdressDTO(
                        hsaId: 'SE2321000057-493K',
                        namn: 'Jönköpings landsting'
                ),
                new LogiskAdressDTO(
                        hsaId: 'SE2321000057-5SRJ',
                        namn: 'Jönköping Bräcke Diakoni VC Lokstallarna'
                ),
                new LogiskAdressDTO(
                        hsaId: 'SE2321000057-6SV4',
                        namn: 'Jönköping A6 Ögonklinik'
                ),
                new LogiskAdressDTO(
                        hsaId: 'SE2321000073-1000',
                        namn: 'Kalmars landsting'
                ),
                new LogiskAdressDTO(
                        hsaId: 'SE2321000073-4C87',
                        namn: 'Kalmar Husläkarcentrum Kalmar'
                ),
                new LogiskAdressDTO(
                        hsaId: 'SE2321000073-4C86',
                        namn: 'Kalmar Virserums läkarhus'
                ),
        ].findAll {
            rnd.nextBoolean()
        }

    }
}
