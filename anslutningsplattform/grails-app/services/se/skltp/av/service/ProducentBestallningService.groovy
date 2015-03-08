package se.skltp.av.service

import grails.transaction.Transactional
import org.apache.shiro.crypto.hash.Sha256Hash
import se.skltp.av.BestallningsHistorik
import se.skltp.av.LogiskAdress
import se.skltp.av.ProducentBestallning
import se.skltp.av.TjansteKomponent
import se.skltp.av.User
import se.skltp.av.services.dto.*
import se.skltp.av.util.BestallningsStatus

@Transactional(readOnly = true)
class ProducentBestallningService {
	
    def listProducentBestallning() {
        def producentBestallningar = ProducentBestallning.list()
        producentBestallningar.collect {
			
			def serviceDomain = new TjansteDomanDTO()
			
			def serviceComponent = new TjansteKomponentDTO()
			
			def tKomp = it.tjansteKomponent
			def serviceConsumer = new TjansteKomponentDTO(
				hsaId: tKomp.hsaId,
				namn: tKomp.namn,
				//huvudAnsvarigNamn
				//huvudAnsvarigEpost
				//huvudAnsvarigTelefon
				tekniskKontaktEpost: tKomp.tekniskKontaktEpost,
				tekniskKontaktNamn: tKomp.tekniskKontaktNamn,
				tekniskKontaktTelefon: tKomp.tekniskKontaktTelefon,
				funktionsBrevladaEpost: tKomp.funktionsBrevladaEpost,
				funktionsBrevladaTelefon: tKomp.funktionsBrevladaTelefon,
				ipadress: tKomp.ipadress
				//pingForConfiguration
			)
			
			def targetEnvironment = new DriftMiljoDTO()

			def client = new AnsvarigDTO()

			def producentBestallningDTO = new ProducentBestallningDTO(
				id: it.id,
				status: it.status,
				serviceDomain: serviceDomain,
				serviceComponent: serviceComponent,
				serviceConsumer: serviceConsumer,
				targetEnvironment: targetEnvironment,
				client: client)
        }
    }

    @Transactional
    def createProducentBestallning(ProducentBestallningDTO producentBestallningDTO) {
        doCreateProducentBestallning(producentBestallningDTO, BestallningsStatus.NY)
    }

    @Transactional
    def createUpdateProducentBestallning(ProducentBestallningDTO producentBestallningDTO) {
        doCreateProducentBestallning(producentBestallningDTO, BestallningsStatus.UPPDATERING)
    }

    def private doCreateProducentBestallning(ProducentBestallningDTO producentBestallningDTO, BestallningsStatus status) {
        User user = upsertUser(producentBestallningDTO)
        if (!user) {
            //TODO: User is not saved correctly, what to do?
        }
        TjansteKomponent tjansteKomponent = upsertTjansteKomponent(producentBestallningDTO, user)
        if (!tjansteKomponent) {
            //TODO: TjansteKomponent is not saved correctly, what to do?
        }
        ProducentBestallning producentBestallning = insertProducentBestallning(producentBestallningDTO, tjansteKomponent, status)
        if (!producentBestallning) {
            //TODO: ProducentBestallning is not saved correctly, what to do?
        }
        createProducentBestallningHistorik(producentBestallning, user.epost)
        log.debug "Producentbestallning updated in database, lets return success"
        return producentBestallning
    }

    def private dtoToPersistedLogiskAdress = { logiskAdressDTO ->
        def logiskAdress = LogiskAdress.findByHsaId(logiskAdressDTO.hsaId)
        if (!logiskAdress) {
            logiskAdress = new LogiskAdress(
                    hsaId: logiskAdressDTO.hsaId
            ).save()
        }
        logiskAdress
    }

    private ProducentBestallning insertProducentBestallning(ProducentBestallningDTO producentBestallningDTO, TjansteKomponent tjansteKomponent, BestallningsStatus status) {
        ProducentBestallning producentBestallning = new ProducentBestallning()
        producentBestallning.setStatus(status.toString())
        producentBestallning.setTjansteKomponent(tjansteKomponent)
        producentBestallning.setMiljo(producentBestallningDTO.targetEnvironment.namn)
        producentBestallning.producentAnslutning = []
        producentBestallning.roleOfClient = producentBestallningDTO.client.role
        producentBestallningDTO.serviceContracts.each {
            se.skltp.av.ProducentAnslutning pa = new se.skltp.av.ProducentAnslutning(
                    rivTaProfile: it.address.rivProfil,
                    url: it.address.url,
                    tjansteKontrakt: it.namnrymd,
                    validFromTime: new Date(0),
                    validToTime: new Date(0),
                    logiskaAdresser: it.logicalAddresses.collect(dtoToPersistedLogiskAdress),
                    nyaLogiskaAdresser: it.newLogicalAddresses.collect(dtoToPersistedLogiskAdress),
                    borttagnaLogiskaAdresser: it.removedLogicalAddresses.collect(dtoToPersistedLogiskAdress),
                    producentBestallning: producentBestallning
            )
            producentBestallning.producentAnslutning.add(pa)
        }
        if (!producentBestallning.validate()) {
            log.error "ProducentBestallning does not contain all mandatory attributes!"
            log.error producentBestallning.errors
        }

        return producentBestallning.save()
    }
	
	private TjansteKomponent upsertTjansteKomponent(producentBestallningDTO, user){
		
		TjansteKomponentDTO serviceComponent = producentBestallningDTO.serviceComponent
		TjansteKomponent tjansteKomponent = TjansteKomponent.findByHsaId(serviceComponent.hsaId)
		
		if(!tjansteKomponent){
			log.debug "Tjanstekomponent not found in database, create a new one: $serviceComponent"
			
			tjansteKomponent = new TjansteKomponent()
		}

		tjansteKomponent.with {
			it.user = user
			hsaId = serviceComponent.hsaId
			it.namn = serviceComponent.namn
            organisation = serviceComponent.organisation
			tekniskKontaktEpost = serviceComponent.tekniskKontaktEpost
			tekniskKontaktNamn = serviceComponent.tekniskKontaktNamn
			tekniskKontaktTelefon = serviceComponent.tekniskKontaktTelefon
			funktionsBrevladaEpost = serviceComponent.funktionsBrevladaEpost
			funktionsBrevladaTelefon = serviceComponent.funktionsBrevladaTelefon
			huvudAnsvarigEpost = serviceComponent.huvudAnsvarigEpost
			huvudAnsvarigNamn = serviceComponent.huvudAnsvarigNamn
			huvudAnsvarigTelefon = serviceComponent.huvudAnsvarigTelefon
			ipadress = serviceComponent.ipadress
            pingForConfiguration = serviceComponent.pingForConfiguration
		}

		if(!tjansteKomponent.validate()){
			log.error "Tjanstekomponent does not contain all mandatory attributes!"
			log.error tjansteKomponent.errors
		}
		
		return tjansteKomponent.save()
	}
	
	private User upsertUser(producentBestallningDTO){	
		
		AnsvarigDTO ansvarig = producentBestallningDTO.client
		
		User user = User.findByUsername(ansvarig.hsaId)
		
		if(!user){
			log.debug "Tjanstekomponent responsible user not found in database, create a new one: $ansvarig"
			
			user = new User(username: ansvarig.hsaId, passwordHash: new Sha256Hash("changeme").toHex())
		}

		user.with {
			namn = ansvarig.name
			epost = ansvarig.email
			telefonNummer = ansvarig.phone
		}

		if(!user.validate()){
			log.error "User does not contain all mandatory attributes!"
			log.error user.errors
		}
		
		return user.save()
	}
	
	private BestallningsHistorik createProducentBestallningHistorik(producentBestallning, epost){
		BestallningsHistorik history = new BestallningsHistorik(
			status: producentBestallning.status,
			producentBestallning: producentBestallning,
			senastUppdateradAv: epost,
			datum: new Date() //TODO look over datumSkapad and datumUppdaterad...these could be done in hibernate event handlers instead
		)
		
		if(!history.validate()){
			log.error "BestallningsHistorik does not contain all mandatory attributes!"
			log.error history.errors
		}
		
		return history.save()
		
		log.debug "Producentbestallning history data created after changes by user ${epost} in producentBestallning: $producentBestallning"
	}
}
