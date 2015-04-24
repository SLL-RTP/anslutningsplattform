package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.Funktionkontakt
import se.skltp.ap.Personkontakt
import se.skltp.ap.Tjanstekomponent
import se.skltp.ap.services.dto.TjansteKomponentDTO
import se.skltp.ap.services.dto.domain.FunktionkontaktDTO
import se.skltp.ap.services.dto.domain.PersonkontaktDTO
import se.skltp.ap.services.dto.domain.TjanstekomponentDTO

@Transactional
class TjansteKomponentService {
	
	def takService

    @Transactional(readOnly = true)
    List<TjanstekomponentDTO> query(String takId, String queryString, int limit) {
		// search both TAK and AP data to support cases where
		// 1. there is an order in AP to set up a new TjansteKomponent in TAK, but the order hasn't been processed yet
		// 2. there is an existing TjansteKomponent in TAK that was registered before AP was live

		// search AP
        def domainServiceComponents = Tjanstekomponent.findAllByHsaIdIlikeOrBeskrivningIlike("%$queryString%", "%$queryString%", [max: limit])
        def tjanstekomponentDTOs = domainServiceComponents.collect {
            new TjanstekomponentDTO(
                    id: it.id,
                    hsaId: it.hsaId,
                    beskrivning: it.beskrivning
            )
        }

        List<TjansteKomponentDTO> takList = takService.freeTextSearchTjansteKomponent(takId, queryString, limit)
        takList.each {
            def tjkDTO = tjanstekomponentDTOs.find { tjkDTO ->
                tjkDTO.hsaId.equalsIgnoreCase(it.hsaId)
            }
            if (tjkDTO != null) { //found a match from DB
                tjkDTO.beskrivning = it.namn
            } else {
                tjanstekomponentDTOs.add(new TjanstekomponentDTO(
                        hsaId: it.hsaId.toUpperCase(),
                        beskrivning: it.namn
                ))
            }
        }
        if (tjanstekomponentDTOs.size() > limit) {
            tjanstekomponentDTOs.take(limit)
        }
        tjanstekomponentDTOs
    }

    TjanstekomponentDTO findByHsaId(String hsaId, String takId) {
        def domainServiceComponent = Tjanstekomponent.findByHsaId(hsaId)
        if (!domainServiceComponent) {
            def tjansteKomponents = takService.freeTextSearchTjansteKomponent(takId, hsaId, 1) //TODO: handle limit
            if (tjansteKomponents != null && !tjansteKomponents.isEmpty()) {
                return new TjanstekomponentDTO(hsaId: tjansteKomponents[0].hsaId.toUpperCase(), beskrivning: tjansteKomponents[0].namn)
            } else {
                return null
            }
        }
        new TjanstekomponentDTO(
                id: domainServiceComponent.id,
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
    }

    boolean update(TjanstekomponentDTO dto) {
        def tjanstekomponent = Tjanstekomponent.findByHsaId(dto.hsaId)
        log.debug "$tjanstekomponent"
        if (tjanstekomponent) {
            tjanstekomponent.beskrivning = dto.beskrivning
            tjanstekomponent.organisation = dto.organisation
            tjanstekomponent.ipadress = dto.ipadress
            tjanstekomponent.pingForConfigurationURL = dto.pingForConfigurationURL
            tjanstekomponent.huvudansvarigKontakt = fromDTO(dto.huvudansvarigKontakt)
            tjanstekomponent.tekniskKontakt = fromDTO(dto.tekniskKontakt)
            tjanstekomponent.tekniskSupportkontakt = fromDTO(dto.tekniskSupportKontakt)
            tjanstekomponent.save()
            return true
        }
        false
    }

    boolean create(TjanstekomponentDTO dto) {
        if (Tjanstekomponent.findByHsaId(dto.hsaId)) { //it already exists
            log.debug "unable to create Tjanstekomponent with hsaId ${dto.hsaId} since it already exists in DB"
            return false //TODO: handle this better
        }
        new Tjanstekomponent(
                hsaId: dto.hsaId,
                beskrivning: dto.beskrivning,
                organisation: dto.organisation,
                ipadress: dto.ipadress,
                pingForConfigurationURL: dto.pingForConfigurationURL,
                huvudansvarigKontakt: fromDTO(dto.huvudansvarigKontakt),
                tekniskKontakt: fromDTO(dto.tekniskKontakt),
                tekniskSupportkontakt: fromDTO(dto.tekniskSupportKontakt)
        ).save()
        true
    }

    private Personkontakt fromDTO(PersonkontaktDTO dto) {
        return new Personkontakt(
                namn: dto.namn,
                hsaId: dto.hsaId,
                epost: dto.epost,
                telefon: dto.hsaId
        )
    }

    private Funktionkontakt fromDTO(FunktionkontaktDTO dto) {
        return new Funktionkontakt(
                epost: dto.epost,
                telefon: dto.telefon
        )
    }
}
