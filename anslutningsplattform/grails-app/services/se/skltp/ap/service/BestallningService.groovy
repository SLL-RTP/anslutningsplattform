package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.Bestallning
import se.skltp.ap.Driftmiljo
import se.skltp.ap.Funktionkontakt
import se.skltp.ap.Konsumentanslutning
import se.skltp.ap.Konsumentbestallning
import se.skltp.ap.LogiskAdress
import se.skltp.ap.Nat
import se.skltp.ap.Personkontakt
import se.skltp.ap.Producentanslutning
import se.skltp.ap.Producentbestallning
import se.skltp.ap.Tjanstekomponent
import se.skltp.ap.UppdateradKonsumentanslutning
import se.skltp.ap.UppdateradProducentanslutning
import se.skltp.ap.services.dto.TjansteDomanDTO
import se.skltp.ap.services.dto.domain.BestallningDTO
import se.skltp.ap.services.dto.domain.DriftmiljoDTO
import se.skltp.ap.services.dto.domain.FunktionkontaktDTO
import se.skltp.ap.services.dto.domain.KonsumentbestallningDTO
import se.skltp.ap.services.dto.domain.LogiskAdressDTO
import se.skltp.ap.services.dto.domain.NatDTO
import se.skltp.ap.services.dto.domain.PersonkontaktDTO
import se.skltp.ap.services.dto.domain.ProducentbestallningDTO
import se.skltp.ap.services.dto.domain.TjanstekomponentDTO

@Transactional
class BestallningService {

    def mailingService

    def freemarkerConfiguration

    def grailsApplication

    def rivTaService

    def handleBestallning(BestallningDTO bestallningDTO) {
        def bestallning = insertBestallning(bestallningDTO)
        emailBestallning(bestallning)
    }

    def emailBestallning(Bestallning bestallning) {
		def confirmationEmailActivated = grailsApplication.config.order.confirmation.email.activated
        def confirmationToAddress = bestallning.bestallare.epost
        def confirmationSubject = grailsApplication.config.order.confirmation.email.subject
        def orderToAddress = grailsApplication.config.order.email.address
        def orderSubject = grailsApplication.config.order.email.subject

        def bestallningMailContent = createBestallningMailContent(bestallning)
        log.info("Sending mail ... with body:\n${bestallningMailContent}")

        if (bestallning) {
            try { //order email
                mailingService.send(null, orderToAddress, orderSubject, bestallningMailContent)
                log.info("order mail sent.")
            }
            catch (Exception e) {
                log.error("Failed to send order mail", e)
            }
			
			if (confirmationEmailActivated) {
	            try { //confirmation email
	                mailingService.send(null, confirmationToAddress, confirmationSubject, bestallningMailContent)
	                log.info("confirmation mail sent.")
	            }
	            catch (Exception e) {
	                log.error("Failed to send confirmation mail", e)
	            }
			}
        }
    }

    private String createBestallningMailContent(Bestallning bestallning) {
        //prepare lookup map so we can get svensktKortNamn for doman based on kontrakt
        Map<String, TjansteDomanDTO> domanLookup = new HashMap<>();
        def domaner = rivTaService.listTjansteDoman()
        bestallning.producentbestallning?.producentanslutningar?.each { pa ->
            domanLookup.put(pa.tjanstekontraktNamnrymd, domaner.find { pa.tjanstekontraktNamnrymd.contains(it.tjansteDomanId) })
        }
        bestallning.producentbestallning?.uppdateradProducentanslutningar?.each { pa ->
            domanLookup.put(pa.tjanstekontraktNamnrymd, domaner.find { pa.tjanstekontraktNamnrymd.contains(it.tjansteDomanId) })
        }
        bestallning.konsumentbestallningar?.each { konsumentbestallning ->
            konsumentbestallning.konsumentanslutningar?.each { ka ->
                domanLookup.put(ka.tjanstekontraktNamnrymd, domaner.find { ka.tjanstekontraktNamnrymd.contains(it.tjansteDomanId) })
            }
            konsumentbestallning.uppdateradKonsumentanslutningar?.each { ka ->
                domanLookup.put(ka.tjanstekontraktNamnrymd, domaner.find { ka.tjanstekontraktNamnrymd.contains(it.tjansteDomanId) })
            }
        }
        def template = freemarkerConfiguration.getTemplate("mail.ftl")
        def writer = new StringWriter()
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put('bestallning', bestallning)
        templateParams.put('domanLookup', domanLookup)
        template.process(templateParams, writer)
        writer.toString()
    }
    private insertBestallning(BestallningDTO bestallningDTO) {
        def driftmiljo = getOrCreate(bestallningDTO.driftmiljo)
        def bestallning = new Bestallning(
                status: 'NY', //TODO: handle status
                driftmiljo: driftmiljo,
                bestallare: upsert(bestallningDTO.bestallare),
                bestallareRoll: bestallningDTO.bestallareRoll,
                otherInfo: bestallningDTO.otherInfo
        )



        if (bestallningDTO.producentbestallning) {
            bestallning.producentbestallning = insertProducentBestallning(bestallningDTO.producentbestallning, driftmiljo);
        }

        bestallningDTO.konsumentbestallningar?.each {
            bestallning.addToKonsumentbestallningar(insertKonsumentBestallning(it, driftmiljo))
        }

        bestallning.save()


    }

    private insertProducentBestallning(ProducentbestallningDTO producentbestallningDTO, Driftmiljo driftmiljo) {
        if (producentbestallningDTO == null) {
            return null
        }
        new Producentbestallning(
                tjanstekomponent: getOrCreate(producentbestallningDTO.tjanstekomponent).addToDriftmiljoer(driftmiljo),
                namnPaEtjanst: producentbestallningDTO.namnPaEtjanst,
                producentanslutningar: producentbestallningDTO.producentanslutningar.collect {
                    new Producentanslutning(
                            rivtaProfil: it.rivtaProfil,
                            url: it.url,
                            tjanstekontraktNamnrymd: it.tjanstekontraktNamnrymd,
                            tjanstekontraktMajorVersion: it.tjanstekontraktMajorVersion,
                            tjanstekontraktMinorVersion: it.tjanstekontraktMinorVersion,
                            giltigFranTid: new Date(),
                            giltigTillTid: new Date(),
                            nyaLogiskaAdresser: it.nyaLogiskaAdresser.collect { logiskAdress ->
                                getOrCreate(logiskAdress)
                            }
                    )
                }.toSet(),
                uppdateradProducentanslutningar: producentbestallningDTO.uppdateradProducentanslutningar.collect {
                    new UppdateradProducentanslutning(
                            rivtaProfil: it.rivtaProfil,
                            url: it.url,
                            tidigareRivtaProfil: it.tidigareRivtaProfil,
                            tidigareUrl: it.tidigareUrl,
                            tjanstekontraktNamnrymd: it.tjanstekontraktNamnrymd,
                            tjanstekontraktMajorVersion: it.tjanstekontraktMajorVersion,
                            tjanstekontraktMinorVersion: it.tjanstekontraktMinorVersion,
                            giltigFranTid: new Date(),
                            giltigTillTid: new Date(),
                            nyaLogiskaAdresser: it.nyaLogiskaAdresser.collect { logiskAdress ->
                                getOrCreate(logiskAdress)
                            },
                            befintligaLogiskaAdresser: it.befintligaLogiskaAdresser.collect { logiskAdress ->
                                getOrCreate(logiskAdress)
                            },
                            borttagnaLogiskaAdresser: it.borttagnaLogiskaAdresser.collect { logiskAdress ->
                                getOrCreate(logiskAdress)
                            }
                    )
                }.toSet()
        ).save()
    }

    private insertKonsumentBestallning(KonsumentbestallningDTO konsumentbestallningDTO, Driftmiljo driftmiljo) {
        if (konsumentbestallningDTO == null) {
            return null
        }
        new Konsumentbestallning(
                tjanstekomponent: getOrCreate(konsumentbestallningDTO.tjanstekomponent).addToDriftmiljoer(driftmiljo),
                namnPaEtjanst: konsumentbestallningDTO.namnPaEtjanst,
                konsumentanslutningar: konsumentbestallningDTO.konsumentanslutningar.collect {
                    new Konsumentanslutning(
                            tjanstekontraktNamnrymd: it.tjanstekontraktNamnrymd,
                            tjanstekontraktMajorVersion: it.tjanstekontraktMajorVersion,
                            tjanstekontraktMinorVersion: it.tjanstekontraktMinorVersion,
                            giltigFranTid: new Date(),
                            giltigTillTid: new Date(),
                            nyaLogiskaAdresser: it.nyaLogiskaAdresser.collect { logiskAdress ->
                                getOrCreate(logiskAdress)
                            }
                    )
                }.toSet(),
                uppdateradKonsumentanslutningar: konsumentbestallningDTO.uppdateradKonsumentanslutningar.collect {
                    new UppdateradKonsumentanslutning(
                            tjanstekontraktNamnrymd: it.tjanstekontraktNamnrymd,
                            tjanstekontraktMajorVersion: it.tjanstekontraktMajorVersion,
                            tjanstekontraktMinorVersion: it.tjanstekontraktMinorVersion,
                            giltigFranTid: new Date(),
                            giltigTillTid: new Date(),
                            nyaLogiskaAdresser: it.nyaLogiskaAdresser.collect { logiskAdress ->
                                getOrCreate(logiskAdress)
                            },
                            befintligaLogiskaAdresser: it.befintligaLogiskaAdresser.collect { logiskAdress ->
                                getOrCreate(logiskAdress)
                            },
                            borttagnaLogiskaAdresser: it.borttagnaLogiskaAdresser.collect { logiskAdress ->
                                getOrCreate(logiskAdress)
                            }
                    )
                }.toSet()
        )
    }

    private LogiskAdress getOrCreate(LogiskAdressDTO dto) {
        def logiskAdress = LogiskAdress.findByHsaId(dto.hsaId)
        if (logiskAdress == null) {
            logiskAdress = new LogiskAdress(
                    hsaId: dto.hsaId,
                    namn: dto.namn
            ).save()
        }
        logiskAdress
    }

    private Driftmiljo getOrCreate(DriftmiljoDTO dto) {
        def driftmiljo = Driftmiljo.findById(dto.id)
        if (driftmiljo == null) {
            driftmiljo = new Driftmiljo(
                    namn: dto.namn
            )
            driftmiljo.id = dto.id
            driftmiljo.save()
        }
        driftmiljo
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

    private Personkontakt upsert(PersonkontaktDTO dto) {
        def personkontakt = fromDTO(dto)
        def dbPersonkontakt = null;
        if (personkontakt.hsaId?.trim()) { //lookup by hsaId
            dbPersonkontakt = Personkontakt.findByHsaId(personkontakt.hsaId)
        }
        if (dbPersonkontakt == null) { //not found by hsaId, try epost
            dbPersonkontakt = Personkontakt.findByEpost(personkontakt.epost);
        }
        if (dbPersonkontakt != null) { //if found, merge values from personkontakt
            dbPersonkontakt.with {
                it.epost = personkontakt.epost
                it.hsaId = personkontakt.hsaId
                it.namn = personkontakt.namn
                it.telefon = personkontakt.telefon
            }
            personkontakt = dbPersonkontakt
        }
        personkontakt.save()
    }

    private Personkontakt fromDTO(PersonkontaktDTO dto) {
        return new Personkontakt(
                epost: dto.epost,
                namn: dto.namn,
                hsaId: dto.hsaId,
                telefon: dto.telefon
        )
    }

    private Funktionkontakt fromDTO(FunktionkontaktDTO dto) {
        return new Funktionkontakt(
                epost: dto.epost,
                telefon: dto.telefon
        )
    }

    private Tjanstekomponent getOrCreate(TjanstekomponentDTO dto) {
        def tjanstekomponent = Tjanstekomponent.findByHsaId(dto.hsaId)
        if (tjanstekomponent == null) {
            tjanstekomponent = new Tjanstekomponent(
                    hsaId: dto.hsaId,
                    beskrivning: dto.beskrivning,
                    organisation: dto.organisation,
                    producentIpadress: dto.producentIpadress,
                    producentDnsNamn: dto.producentDnsNamn,
                    konsumentIpadress: dto.konsumentIpadress,
                    pingForConfigurationURL: dto.pingForConfigurationURL,
                    huvudansvarigKontakt: dto.huvudansvarigKontakt != null ? fromDTO(dto.huvudansvarigKontakt) : null,
                    tekniskKontakt: dto.tekniskKontakt != null ? fromDTO(dto.tekniskKontakt) : null,
                    tekniskSupportkontakt: dto.tekniskSupportKontakt != null ? fromDTO(dto.tekniskSupportKontakt) : null,
                    nat: dto.nat != null ? dto.nat.collect { natDto ->
                        getOrCreate(natDto)
                    } : new ArrayList<Nat>()
            ).save()
        }
        tjanstekomponent
    }

}
