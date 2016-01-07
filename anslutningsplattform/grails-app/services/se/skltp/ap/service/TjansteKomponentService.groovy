package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.Funktionkontakt
import se.skltp.ap.Nat
import se.skltp.ap.Personkontakt
import se.skltp.ap.Tjanstekomponent
import se.skltp.ap.services.dto.domain.FunktionkontaktDTO
import se.skltp.ap.services.dto.domain.NatDTO
import se.skltp.ap.services.dto.domain.PersonkontaktDTO
import se.skltp.ap.services.dto.domain.TjanstekomponentDTO

@Transactional
class TjansteKomponentService {
	
	def takService

    def kontaktService

    def mailingService

    def freemarkerConfiguration

    def grailsApplication

    @Transactional(readOnly = true)
    List<TjanstekomponentDTO> query(String takId, String queryString, int limit) {
		// search both TAK and AP data to support cases where
		// 1. there is an order in AP to set up a new TjansteKomponent in TAK, but the order hasn't been processed yet
		// 2. there is an existing TjansteKomponent in TAK that was registered before AP was live

		// search AP

        def criteria = Tjanstekomponent.createCriteria()
        def domainServiceComponents = criteria {
            or {
                ilike "hsaId", "%$queryString%"
                ilike "beskrivning", "%$queryString%"
                ilike "organisation", "%$queryString%"
            }
            maxResults limit
        }
        def tjanstekomponentDTOs = domainServiceComponents.collect {
            new TjanstekomponentDTO(
                    id: it.id,
                    hsaId: it.hsaId,
                    beskrivning: it.beskrivning,
                    organisation: it.organisation
            )
        }

        List<TjanstekomponentDTO> takList = takService.freeTextSearchTjansteKomponent(takId, queryString, limit)
        takList.each {
            def tjkDTO = tjanstekomponentDTOs.find { tjkDTO ->
                tjkDTO.hsaId.equalsIgnoreCase(it.hsaId)
            }
            if (tjkDTO == null) { //not found in DB, just add
                tjanstekomponentDTOs.add(new TjanstekomponentDTO(
                        hsaId: it.hsaId.toUpperCase(),
                        beskrivning: it.beskrivning,
                        organisation: it.organisation
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
                return new TjanstekomponentDTO(
                        hsaId: tjansteKomponents[0].hsaId,
                        beskrivning: tjansteKomponents[0].beskrivning,
                        organisation: tjansteKomponents[0].organisation)
            } else {
                return null
            }
        }
        def tjanstekomponentDTO = new TjanstekomponentDTO(
                id: domainServiceComponent.id,
                hsaId: domainServiceComponent.hsaId,
                beskrivning: domainServiceComponent.beskrivning,
                organisation: domainServiceComponent.organisation,
                producentIpadress: domainServiceComponent.producentIpadress,
                producentDnsNamn: domainServiceComponent.producentDnsNamn,
                konsumentIpadress: domainServiceComponent.konsumentIpadress,
                pingForConfigurationURL: domainServiceComponent.pingForConfigurationURL,
                huvudansvarigKontakt: new PersonkontaktDTO(
                        hsaId: domainServiceComponent.huvudansvarigKontakt?.hsaId,
                        namn: domainServiceComponent.huvudansvarigKontakt?.namn,
                        epost: domainServiceComponent.huvudansvarigKontakt?.epost,
                        telefon: domainServiceComponent.huvudansvarigKontakt?.telefon,
                        otherInfo: domainServiceComponent.huvudansvarigKontakt?.otherInfo
                ),
                tekniskKontakt: new PersonkontaktDTO(
                        hsaId: domainServiceComponent.tekniskKontakt?.hsaId,
                        namn: domainServiceComponent.tekniskKontakt?.namn,
                        epost: domainServiceComponent.tekniskKontakt?.epost,
                        telefon: domainServiceComponent.tekniskKontakt?.telefon,
                        otherInfo: domainServiceComponent.tekniskKontakt?.otherInfo
                ),
                tekniskSupportKontakt: new FunktionkontaktDTO(
                        epost: domainServiceComponent.tekniskSupportkontakt?.epost,
                        telefon: domainServiceComponent.tekniskSupportkontakt?.telefon
                ),
                nat: domainServiceComponent.nat ? new NatDTO(id: domainServiceComponent.nat.id, namn: domainServiceComponent.nat.namn) : null
        )
    }

    /**
     * @param dto
     * @return -1 if no tjanstekomponent with hsaId was found, 0 if successful, 1 if successful and email is sent
     */
    int update(TjanstekomponentDTO dto, PersonkontaktDTO bestallare) {
        def tjanstekomponent = Tjanstekomponent.findByHsaId(dto.hsaId)
        log.debug "$tjanstekomponent"
        log.debug "$bestallare"
        if (tjanstekomponent) {
            def shouldGenerateEmail = false
            if (tjanstekomponent.beskrivning != dto.beskrivning
                    || tjanstekomponent.organisation != dto.organisation
                    || tjanstekomponent.producentIpadress != dto.producentIpadress
                    || tjanstekomponent.producentDnsNamn != dto.producentDnsNamn
                    || tjanstekomponent.pingForConfigurationURL != dto.pingForConfigurationURL
                    || tjanstekomponent.konsumentIpadress != dto.konsumentIpadress
                    || tjanstekomponent.nat?.id != dto.nat.id) {
                //this update should trigger email since more stuff than the contacts have changed
                shouldGenerateEmail = true
            }
            def oldTjanstekomponent = copyTjanstekomponent(tjanstekomponent)
            tjanstekomponent.beskrivning = dto.beskrivning
            tjanstekomponent.organisation = dto.organisation
            tjanstekomponent.producentIpadress = dto.producentIpadress
            tjanstekomponent.producentDnsNamn = dto.producentDnsNamn
            tjanstekomponent.konsumentIpadress = dto.konsumentIpadress
            tjanstekomponent.pingForConfigurationURL = dto.pingForConfigurationURL
            tjanstekomponent.huvudansvarigKontakt = fromDTO(dto.huvudansvarigKontakt)
            tjanstekomponent.tekniskKontakt = fromDTO(dto.tekniskKontakt)
            tjanstekomponent.tekniskSupportkontakt = fromDTO(dto.tekniskSupportKontakt)
            tjanstekomponent.nat = dto.nat ? getOrCreate(dto.nat) : null
            tjanstekomponent.save()
            log.debug("should trigger email: $shouldGenerateEmail")
            log.debug("old: $oldTjanstekomponent")
            log.debug("new: $tjanstekomponent")
            if (shouldGenerateEmail) {
                emailUpdatedTjanstekomponent(oldTjanstekomponent, tjanstekomponent, bestallare)
                return 1
            } else {
                return 0
            }
        }
        -1
    }

    boolean create(TjanstekomponentDTO dto, PersonkontaktDTO bestallare) {
        if (Tjanstekomponent.findByHsaId(dto.hsaId)) { //it already exists
            log.debug "unable to create Tjanstekomponent with hsaId ${dto.hsaId} since it already exists in DB"
            return false //TODO: handle this better
        }
        Tjanstekomponent tjanstekomponent = fromDTO(dto)
        tjanstekomponent = tjanstekomponent.save()
        emailNewTjanstekomponent(tjanstekomponent, bestallare)
        true
    }

    def emailNewTjanstekomponent(Tjanstekomponent tjanstekomponent, PersonkontaktDTO bestallare) {
        def mailContent = createMailContentForNewTjanstekomponent(tjanstekomponent, bestallare)
        def tjanstekomponentToAddress = grailsApplication.config.tjanstekomponent.email.address
        def tjanstekomponentSubject = grailsApplication.config.tjanstekomponent.email.subject
        mailingService.send(null, tjanstekomponentToAddress, tjanstekomponentSubject, mailContent)

    }

    def emailUpdatedTjanstekomponent(Tjanstekomponent oldTjanstekomponent, Tjanstekomponent newTjanstekomponent, PersonkontaktDTO bestallare) {
        def mailContent = createMailContentForUpdatedTjanstekomponent(oldTjanstekomponent, newTjanstekomponent, bestallare)
        def tjanstekomponentToAddress = grailsApplication.config.tjanstekomponent.email.address
        def tjanstekomponentSubject = grailsApplication.config.tjanstekomponent.email.subject
        mailingService.send(null, tjanstekomponentToAddress, tjanstekomponentSubject, mailContent)

    }

    private Tjanstekomponent copyTjanstekomponent(Tjanstekomponent input) {
        def tjanstekomponent = new Tjanstekomponent(
                hsaId: input.hsaId,
                beskrivning: input.beskrivning,
                organisation: input.organisation,
                producentIpadress: input.producentIpadress,
                producentDnsNamn: input.producentDnsNamn,
                konsumentIpadress: input.konsumentIpadress,
                pingForConfigurationURL: input.pingForConfigurationURL
        )
        if (input.nat) {
            tjanstekomponent.nat = new Nat(namn: input.nat.namn)
            tjanstekomponent.nat.id = input.nat.id
        }
        tjanstekomponent
    }

    private Tjanstekomponent fromDTO(TjanstekomponentDTO dto) {
        def tjanstekomponent = new Tjanstekomponent(
                hsaId: dto.hsaId,
                beskrivning: dto.beskrivning,
                organisation: dto.organisation,
                producentIpadress: dto.producentIpadress,
                producentDnsNamn: dto.producentDnsNamn,
                konsumentIpadress: dto.konsumentIpadress,
                pingForConfigurationURL: dto.pingForConfigurationURL,
                huvudansvarigKontakt: fromDTO(dto.huvudansvarigKontakt),
                tekniskKontakt: fromDTO(dto.tekniskKontakt),
                tekniskSupportkontakt: fromDTO(dto.tekniskSupportKontakt),
                nat: dto.nat ? getOrCreate(dto.nat) : null
        )
        tjanstekomponent
    }

    private Personkontakt fromDTO(PersonkontaktDTO dto) {
        return new Personkontakt(
                namn: dto.namn,
                hsaId: dto.hsaId,
                epost: dto.epost,
                telefon: dto.telefon,
                otherInfo: dto.otherInfo
        )
    }

    private Funktionkontakt fromDTO(FunktionkontaktDTO dto) {
        return new Funktionkontakt(
                epost: dto.epost,
                telefon: dto.telefon
        )
    }

    private Nat getOrCreate(NatDTO dto) {
        def nat = Nat.findById(dto.id)
        if (nat == null) {
            nat = new Nat(namn: dto.namn)
            nat.id = dto.id
            nat.save()
        }
        nat
    }

    private String createMailContentForNewTjanstekomponent(Tjanstekomponent tjanstekomponent, PersonkontaktDTO bestallare) {
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put('tjanstekomponent', tjanstekomponent)
        templateParams.put('bestallare', bestallare)
        def template = freemarkerConfiguration.getTemplate("newTjanstekomponentMail.ftl")
        def writer = new StringWriter()
        template.process(templateParams, writer)
        writer.toString()
    }

    private String createMailContentForUpdatedTjanstekomponent(Tjanstekomponent oldTjanstekomponent, Tjanstekomponent newTjanstekomponent, PersonkontaktDTO bestallare) {
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put('oldTjanstekomponent', oldTjanstekomponent)
        templateParams.put('newTjanstekomponent', newTjanstekomponent)
        templateParams.put('bestallare', bestallare)
        def template = freemarkerConfiguration.getTemplate("updatedTjanstekomponentMail.ftl")
        def writer = new StringWriter()
        template.process(templateParams, writer)
        writer.toString()
    }
}
