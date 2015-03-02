package se.skltp.av.service


import se.skltp.av.User
import se.skltp.av.services.dto.AnsvarigDTO
import se.skltp.av.services.dto.DriftMiljoDTO
import se.skltp.av.services.dto.ProducentBestallningDTO
import se.skltp.av.services.dto.TjansteDomanDTO
import se.skltp.av.services.dto.TjansteKomponentDTO
import spock.lang.*

/**
 *
 */
class ProducentBestallningServiceSpec extends Specification {

	def producentBestallningService

	def setup() {
	}

	def cleanup() {
	}

	void "create new responsible user for tjanstekomponent when not in db"() {

		setup:
			def serviceDomain = new TjansteDomanDTO()
			def serviceComponent = new TjansteKomponentDTO()
			def serviceConsumer = new TjansteKomponentDTO()
			def miljo = new DriftMiljoDTO()
			
			def client = new AnsvarigDTO(name: 'Agda Andersson', email: 'agda.andersson@apbackend.dummy', phone: '1234567890')
	
			def producentBestallningDTO = new ProducentBestallningDTO(serviceDomain: serviceDomain, serviceComponent: 
				serviceComponent, serviceConsumer: serviceConsumer, targetEnvironment: miljo, client: client)
			
			producentBestallningService.createProducentBestallning(producentBestallningDTO)

		when:
			def size = User.list().size
		then:
			size == 1
		
	}
}
