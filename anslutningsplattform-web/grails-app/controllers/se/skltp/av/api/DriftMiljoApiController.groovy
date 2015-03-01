package se.skltp.av.api

import grails.rest.RestfulController
import se.skltp.av.services.dto.DriftMiljoDTO

class DriftMiljoApiController extends RestfulController {
	
	def takService

    DriftMiljoApiController() {
        super(DriftMiljoDTO)
    }

    def list() {
		def driftMiljos = []
		takService.getTakRoutingEntriesList().each {
			def dto = new DriftMiljoDTO(id: it.id, namn: it.name)
			driftMiljos << dto
		}
		respond driftMiljos
    }
}
