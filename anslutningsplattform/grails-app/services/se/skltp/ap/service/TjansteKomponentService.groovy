package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.Tjanstekomponent
import se.skltp.ap.services.dto.domain.FunktionkontaktDTO
import se.skltp.ap.services.dto.domain.PersonkontaktDTO
import se.skltp.ap.services.dto.domain.TjanstekomponentDTO

@Transactional
class TjansteKomponentService {
	
	def takService

    def fakeTakTjanstekomponents = [
            ["SE2321000016-A21S", "SLL - Prosang"],
            ["SE2321000016-A22S", "HSF SLL - Remittera"],
            ["SE2321000016-A1DB", "SLL - Obstetrix"],
            ["SE2321000016-9119", "SLL - FlexLab"],
            ["SE2321000016-6RK5", "SLL - TakeCare"]
    ]

    @Transactional(readOnly = true)
    List<TjanstekomponentDTO> query(String takId, String queryString, int limit) {
		// search both TAK and AP data to support cases where
		// 1. there is an order in AP to set up a new TjansteKomponent in TAK, but the order hasn't been processed yet
		// 2. there is an existing TjansteKomponent in TAK that was registered before AP was live

		// search AP
        def domainServiceComponents = Tjanstekomponent.findAllByHsaIdIlikeOrBeskrivningIlike("%$queryString%", "%$queryString%")
        def tjanstekomponentDTOs = domainServiceComponents.collect {
            new TjanstekomponentDTO(
                    hsaId: it.hsaId,
                    beskrivning: it.beskrivning
            )
        }
		
		if (tjanstekomponentDTOs.size() < limit) {
			// also search TAK
			//List<TjansteKomponentDTO> takList = takService.freeTextSearchTjansteKomponent(takId, queryString, limit)
			// add TAK data to result, prioritize AP data that is richer
			
			// TODO: do not add duplicates ...
			
			// TODO: tmp hardcoded ...

			def takList = new ArrayList()
			fakeTakTjanstekomponents.each {
				takList.add(new TjanstekomponentDTO(hsaId: it[0], beskrivning: it[1]))
			}

            tjanstekomponentDTOs.addAll(takList)
		}
		
		
		// apply limit
		if (tjanstekomponentDTOs.size() > limit) {
            tjanstekomponentDTOs.take(limit)
		}

        tjanstekomponentDTOs
    }

    TjanstekomponentDTO findByHsaId(String hsaId) {
        def domainServiceComponent = Tjanstekomponent.findByHsaId(hsaId)
        if (!domainServiceComponent) {
            def tmp = fakeTakTjanstekomponents.find {
                it[0] == hsaId
            }
            return new TjanstekomponentDTO(hsaId: tmp[0], beskrivning: tmp[1])
        }
        new TjanstekomponentDTO(
                hsaId: domainServiceComponent.hsaId,
                beskrivning: domainServiceComponent.beskrivning,
                organisation: domainServiceComponent.organisation,
                ipadress: domainServiceComponent.ipadress,
                pingForConfigurationURL: domainServiceComponent.pingForConfigurationURL,
                huvudansvarigKontakt: new PersonkontaktDTO(
                        hsaId: domainServiceComponent.huvudansvarigKontakt?.hsaId,
                        namn: domainServiceComponent.huvudansvarigKontakt?.namn,
                        epost: domainServiceComponent.huvudansvarigKontakt?.epost,
                        telefon: domainServiceComponent.huvudansvarigKontakt?.telefon
                ),
                tekniskKontakt: new PersonkontaktDTO(
                        hsaId: domainServiceComponent.tekniskKontakt?.hsaId,
                        namn: domainServiceComponent.tekniskKontakt?.namn,
                        epost:  domainServiceComponent.tekniskKontakt?.epost,
                        telefon: domainServiceComponent.tekniskKontakt?.telefon
                ),
                tekniskSupportKontakt: new FunktionkontaktDTO(
                        epost: domainServiceComponent.tekniskSupportkontakt?.epost,
                        telefon: domainServiceComponent.tekniskSupportkontakt?.telefon
                )
        )
//        new TjansteKomponentDTO(
//                hsaId: domainServiceComponent.hsaId,
//                namn: domainServiceComponent.beskrivning,
//                organisation: domainServiceComponent.organisation,
//                huvudAnsvarigNamn: domainServiceComponent.huvudansvarigKontakt.namn,
//                huvudAnsvarigEpost: domainServiceComponent.huvudansvarigKontakt.epost,
//                huvudAnsvarigTelefon: domainServiceComponent.huvudansvarigKontakt.telefon,
//                tekniskKontaktEpost: domainServiceComponent.tekniskKontakt.epost,
//                tekniskKontaktNamn: domainServiceComponent.tekniskKontakt.namn,
//                tekniskKontaktTelefon: domainServiceComponent.tekniskKontakt.telefon,
//                funktionsBrevladaEpost: domainServiceComponent.tekniskSupportkontakt.epost,
//                funktionsBrevladaTelefon: domainServiceComponent.tekniskSupportkontakt.telefon,
//                ipadress: domainServiceComponent.ipadress,
//                pingForConfiguration: domainServiceComponent.pingForConfigurationURL
//        )
    }

    boolean update(TjanstekomponentDTO dto) {
//        def tjanstekomponent = Tjanstekomponent.findByHsaId(dto.hsaId)
//        if (tjanstekomponent != null) {
//            tjanstekomponent.beskrivning = dto.namn
//            tjanstekomponent.organisation = dto.organisation
//            tjanstekomponent.ipadress = dto.ipadress
//            tjanstekomponent.pingForConfigurationURL = dto.pingForConfiguration
//
//            //TODO: need to handle changed contacts
//            tjanstekomponent.huvudansvarigKontakt.namn = dto.huvudAnsvarigNamn
//            tjanstekomponent.huvudansvarigKontakt.epost = dto.huvudAnsvarigEpost
//            tjanstekomponent.huvudansvarigKontakt.telefon = dto.huvudAnsvarigTelefon
//
//            tjanstekomponent.tekniskKontakt.namn = dto.tekniskKontaktNamn
//            tjanstekomponent.tekniskKontakt.epost = dto.tekniskKontaktEpost
//            tjanstekomponent.tekniskKontakt.telefon = dto.tekniskKontaktTelefon
//
//            tjanstekomponent.tekniskSupportkontakt.epost = dto.funktionsBrevladaEpost
//            tjanstekomponent.tekniskSupportkontakt.telefon = dto.funktionsBrevladaTelefon
//
//            tjanstekomponent.save()
//            return true
//        }
//        false
    }
}
