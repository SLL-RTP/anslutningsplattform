package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.Bestallning
import se.skltp.ap.Driftmiljo
import se.skltp.ap.Funktionkontakt
import se.skltp.ap.Konsumentanslutning
import se.skltp.ap.Konsumentbestallning
import se.skltp.ap.LogiskAdress
import se.skltp.ap.Personkontakt
import se.skltp.ap.Producentanslutning
import se.skltp.ap.Producentbestallning
import se.skltp.ap.Tjanstekomponent
import se.skltp.ap.UppdateradKonsumentanslutning
import se.skltp.ap.UppdateradProducentanslutning
import se.skltp.ap.services.dto.domain.BestallningDTO
import se.skltp.ap.services.dto.domain.DriftmiljoDTO
import se.skltp.ap.services.dto.domain.FunktionkontaktDTO
import se.skltp.ap.services.dto.domain.KonsumentbestallningDTO
import se.skltp.ap.services.dto.domain.LogiskAdressDTO
import se.skltp.ap.services.dto.domain.PersonkontaktDTO
import se.skltp.ap.services.dto.domain.ProducentbestallningDTO
import se.skltp.ap.services.dto.domain.TjanstekomponentDTO

@Transactional
class BestallningService {

    def mailingService

    def freemarkerConfiguration

    def handleBestallning(BestallningDTO bestallningDTO) {
        def bestallning = insertBestallning(bestallningDTO)
        emailBestallning(bestallning)
    }

    def emailBestallning(Bestallning bestallning) {
        //NOT: testing with gmail requires:
        // 1. a non google-apps/enterprise mail account (see pt 2)
        // 2. allowing "less secure apps" using:
        //   https://www.google.com/settings/security/lesssecureapps
        //   logged in to your gmail account
        // 3. fromAddress to be your own address for the gmail account you are using
        // 4. config in Config.groovy grails{ mail{: username/password
        //def fromAddress = "hakan.dahl.demo1@gmail.com"
        //def fromAddress = "martinsjunkbox@gmail.com"
        def toAddress = bestallning.bestallare.epost
        def subjectField = "AP TEST subject"
        //def bodyPlainText = "AP TEST body"
        // TODO: prettify this!
        //def bodyPlainText = request.JSON.toString()
        def bestallningMailContent = createBestallningMailContent(bestallning)
        def success = false
        log.info("Sending mail ... with body:\n${bestallningMailContent}")

        if (bestallning) {
            try {
                mailingService.send(null, toAddress, subjectField, bestallningMailContent)
                success = true
                log.info("Mail sent.")
            }
            catch (Exception e) {
                log.error("Failed to send mail", e)
            }
        }
    }

    private String createBestallningMailContent(Bestallning bestallning) {
        def template = freemarkerConfiguration.getTemplate("mail.ftl")
        def writer = new StringWriter()
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put('bestallning', bestallning)
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

    private Personkontakt upsert(PersonkontaktDTO dto) {
        def personkontakt = fromDTO(dto)
        def dbPersonkontakt = Personkontakt.findByEpost(dto.epost)
        if (dbPersonkontakt != null) {
            personkontakt.id = dbPersonkontakt.id
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
                    ipadress: dto.ipadress,
                    pingForConfigurationURL: dto.pingForConfigurationURL,
                    huvudansvarigKontakt: dto.huvudansvarigKontakt != null ? fromDTO(dto.huvudansvarigKontakt) : null,
                    tekniskKontakt: dto.tekniskKontakt != null ? fromDTO(dto.tekniskKontakt) : null,
                    tekniskSupportkontakt: dto.tekniskSupportKontakt != null ? fromDTO(dto.tekniskSupportKontakt) : null
            ).save()
        }
        tjanstekomponent
    }

}
