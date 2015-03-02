package se.skltp.av.api

import se.skltp.av.ProducentBestallning
import se.skltp.av.services.dto.ProducentBestallningDTO

class ProducentBestallningApiController{

	static namespace = 'v1'

	def mailingService

	def producentBestallningService

	def list() {
		log.debug "API, a request to list producentbestallningar, params: $params"
		
		respond producentBestallningService.listProducentBestallning()
	}

	def get(long id) {
		
		log.debug "API, a request for producentbestallning: $id, params: $params"
		
		def producentBestallning = ProducentBestallning.get(id)

		if(ProducentBestallning == null) {
			render status:404
		}
		else {

			def producentBestallningDTO = new ProducentBestallningDTO(
					id: producentBestallning.id,
					status: producentBestallning.status,
					miljo: producentBestallning.miljo
					)

			return [producentBestallning: producentBestallningDTO]
		}
	}



	def save(ProducentBestallningDTO producentBestallningDTO) {
		
		log.debug "API, a save requested for producentbestallning: $producentBestallningDTO"

		def producentBestallning = producentBestallningService.createProducentBestallning(producentBestallningDTO)
		

		//NOT: testing with gmail requires:
		// 1. a non google-apps/enterprise mail account (see pt 2)
		// 2. allowing "less secure apps" using:
		//   https://www.google.com/settings/security/lesssecureapps
		//   logged in to your gmail account
		// 3. fromAddress to be your own address for the gmail account you are using
		// 4. config in Config.groovy grails{ mail{: username/password 
		//def fromAddress = "hakan.dahl.demo1@gmail.com"

		def fromAddress = "johanna.essen@callistaenterprise.se"

		def toAddress

		if(producentBestallning && producentBestallning?.tjansteKomponent?.user?.username) {
			toAddress = producentBestallning?.tjansteKomponent?.user?.username
			log.debug "Mailing with toaddress: $toAddress"
		}


		def subjectField = "AP TEST subject"
		//def bodyPlainText = "AP TEST body"
		// TODO: prettify this!
		//def bodyPlainText = request.JSON.toString()
		
		def success = false
		log.info("Sending mail ... with body:\n${producentBestallningDTO}")

		if (producentBestallning) {
			try {
				mailingService.send(fromAddress, toAddress, subjectField, producentBestallningDTO.toString())
				success = true
				log.info("Mail sent.")
			}
			catch (Exception e) {
				log.error("Failed to send mail", e)
			}
		}

		render status: success ? 201 : 500
	}
}
