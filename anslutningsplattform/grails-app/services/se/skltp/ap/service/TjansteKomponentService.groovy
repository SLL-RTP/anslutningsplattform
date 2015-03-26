package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.Tjanstekomponent
import se.skltp.ap.services.dto.TjansteKomponentDTO

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
    List<TjansteKomponentDTO> query(String takId, String queryString, int limit) {
		// search both TAK and AP data to support cases where
		// 1. there is an order in AP to set up a new TjansteKomponent in TAK, but the order hasn't been processed yet
		// 2. there is an existing TjansteKomponent in TAK that was registered before AP was live

		// search AP
        def domainServiceComponents = Tjanstekomponent.findAllByHsaIdIlikeOrBeskrivningIlike("%$queryString%", "%$queryString%")
        def tjansteKomponentDTOs = domainServiceComponents.collect {
            new TjansteKomponentDTO(
                    hsaId: it.hsaId,
                    namn: it.beskrivning
            )
        }
		
		if (tjansteKomponentDTOs.size() < limit) {
			// also search TAK
			//List<TjansteKomponentDTO> takList = takService.freeTextSearchTjansteKomponent(takId, queryString, limit)
			// add TAK data to result, prioritize AP data that is richer
			
			// TODO: do not add duplicates ...
			
			// TODO: tmp hardcoded ...

			def takList = new ArrayList()
			fakeTakTjanstekomponents.each {
				takList.add(new TjansteKomponentDTO(hsaId: it[0], namn: it[1]))
			}

            tjansteKomponentDTOs.addAll(takList)
		}
		
		
		// apply limit
		if (tjansteKomponentDTOs.size() > limit) {
            tjansteKomponentDTOs.take(limit)
		}

        tjansteKomponentDTOs
    }

    TjansteKomponentDTO findByHsaId(String hsaId) {
        def domainServiceComponent = Tjanstekomponent.findByHsaId(hsaId)
        if (!domainServiceComponent) {
            def tmp = fakeTakTjanstekomponents.find {
                it[0] == hsaId
            }
            return new TjansteKomponentDTO(hsaId: tmp[0], namn: tmp[1])
        }
        new TjansteKomponentDTO(
                hsaId: domainServiceComponent.hsaId,
                namn: domainServiceComponent.beskrivning,
                organisation: domainServiceComponent.organisation,
                huvudAnsvarigNamn: domainServiceComponent.huvudansvarigKontakt.namn,
                huvudAnsvarigEpost: domainServiceComponent.huvudansvarigKontakt.epost,
                huvudAnsvarigTelefon: domainServiceComponent.huvudansvarigKontakt.telefon,
                tekniskKontaktEpost: domainServiceComponent.tekniskKontakt.epost,
                tekniskKontaktNamn: domainServiceComponent.tekniskKontakt.namn,
                tekniskKontaktTelefon: domainServiceComponent.tekniskKontakt.telefon,
                funktionsBrevladaEpost: domainServiceComponent.tekniskSupportkontakt.epost,
                funktionsBrevladaTelefon: domainServiceComponent.tekniskSupportkontakt.telefon,
                ipadress: domainServiceComponent.ipadress,
                pingForConfiguration: domainServiceComponent.pingForConfigurationURL
        )
    }

    boolean update(TjansteKomponentDTO dto) {
        def tjanstekomponent = Tjanstekomponent.findByHsaId(dto.hsaId)
        if (tjanstekomponent != null) {
            tjanstekomponent.beskrivning = dto.namn
            tjanstekomponent.organisation = dto.organisation
            tjanstekomponent.ipadress = dto.ipadress
            tjanstekomponent.pingForConfigurationURL = dto.pingForConfiguration

            //TODO: need to handle changed contacts
            tjanstekomponent.huvudansvarigKontakt.namn = dto.huvudAnsvarigNamn
            tjanstekomponent.huvudansvarigKontakt.epost = dto.huvudAnsvarigEpost
            tjanstekomponent.huvudansvarigKontakt.telefon = dto.huvudAnsvarigTelefon

            tjanstekomponent.tekniskKontakt.namn = dto.tekniskKontaktNamn
            tjanstekomponent.tekniskKontakt.epost = dto.tekniskKontaktEpost
            tjanstekomponent.tekniskKontakt.telefon = dto.tekniskKontaktTelefon

            tjanstekomponent.tekniskSupportkontakt.epost = dto.funktionsBrevladaEpost
            tjanstekomponent.tekniskSupportkontakt.telefon = dto.funktionsBrevladaTelefon

            tjanstekomponent.save()
            return true
        }
        false
    }
}
