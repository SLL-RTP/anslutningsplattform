package se.skltp.av.service

import grails.transaction.Transactional

import org.apache.shiro.crypto.hash.Sha256Hash

import se.skltp.av.BestallningsHistorik
import se.skltp.av.ProducentBestallning
import se.skltp.av.TjansteKomponent
import se.skltp.av.User
import se.skltp.av.services.dto.AnsvarigDTO
import se.skltp.av.services.dto.DriftMiljoDTO
import se.skltp.av.services.dto.ProducentBestallningDTO
import se.skltp.av.services.dto.TjansteDomanDTO
import se.skltp.av.services.dto.TjansteKomponentDTO
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
				serviceComponent:
				serviceComponent,
				serviceConsumer: serviceConsumer,
				targetEnvironment: targetEnvironment,
				client: client)
        }
    }

    @Transactional
    def createProducentBestallning(producentBestallningDTO) {

		User user = upsertUser(producentBestallningDTO)
		
		if(!user){
			//TODO: User is not saved correctly, what to do?
		}
		
		TjansteKomponent tjansteKomponent = upsertTjansteKomponent(producentBestallningDTO, user)
		if(!tjansteKomponent){
			//TODO: TjansteKomponent is not saved correctly, what to do?
		}
		
		ProducentBestallning producentBestallning = upsertProducentBestallning(producentBestallningDTO, tjansteKomponent)
		if(!producentBestallning){
			//TODO: ProducentBestallning is not saved correctly, what to do?
		}
		
		//create producentanslutningar
		
		
		createProducentBestallningHistorik(producentBestallning, user.epost)

		log.debug "Producentbestallning updated in database, lets return success"

		return producentBestallning
    }

	private ProducentBestallning upsertProducentBestallning(producentBestallningDTO, tjansteKomponent){

		ProducentBestallning producentBestallning = ProducentBestallning.get(producentBestallningDTO.id)

		if(!producentBestallning){
			log.debug "ProducentBestallning not found in database, create a new one: $producentBestallningDTO"
			
			producentBestallning = new ProducentBestallning()
			producentBestallning.setStatus(BestallningsStatus.NY.toString())
		}else{
			producentBestallning.setStatus(BestallningsStatus.UPPDATERAD.toString())
		}

		producentBestallning.setTjansteKomponent(tjansteKomponent)
		producentBestallning.setMiljo(producentBestallningDTO.targetEnvironment.namn)
		
		if(!producentBestallning.validate()){
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
			tekniskKontaktEpost = serviceComponent.tekniskKontaktEpost
			tekniskKontaktNamn = serviceComponent.tekniskKontaktNamn
			tekniskKontaktTelefon = serviceComponent.tekniskKontaktTelefon
			funktionsBrevladaEpost = serviceComponent.funktionsBrevladaEpost
			funktionsBrevladaTelefon = serviceComponent.funktionsBrevladaTelefon
			huvudAnsvarigEpost = serviceComponent.huvudAnsvarigEpost
			huvudAnsvarigNamn = serviceComponent.huvudAnsvarigNamn
			huvudAnsvarigTelefon = serviceComponent.huvudAnsvarigTelefon
			ipadress = serviceComponent.ipadress
		}

		if(!tjansteKomponent.validate()){
			log.error "Tjanstekomponent does not contain all mandatory attributes!"
			log.error tjansteKomponent.errors
		}
		
		return tjansteKomponent.save()
	}
	
	private User upsertUser(producentBestallningDTO){	
		
		AnsvarigDTO ansvarig = producentBestallningDTO.client
		
		User user = User.findByUsername(ansvarig.email)
		
		if(!user){
			log.debug "Tjanstekomponent responsible user not found in database, create a new one: $ansvarig"
			
			user = new User(username: ansvarig.email, passwordHash: new Sha256Hash("changeme").toHex())
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
