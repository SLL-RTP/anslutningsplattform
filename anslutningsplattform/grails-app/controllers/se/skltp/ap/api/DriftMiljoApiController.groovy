package se.skltp.ap.api

import se.skltp.ap.services.dto.domain.DriftmiljoDTO

class DriftMiljoApiController {
	
	def takService

    def list() {
		def driftMiljos = []
		takService.getTakRoutingEntriesList().each {
			def dto = new DriftmiljoDTO(id: it.id, namn: it.name)
			driftMiljos << dto
		}
		respond driftMiljos
    }
}
