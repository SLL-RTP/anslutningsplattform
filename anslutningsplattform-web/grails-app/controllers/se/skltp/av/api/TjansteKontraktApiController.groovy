package se.skltp.av.api

import grails.rest.RestfulController
import se.skltp.av.services.dto.TjansteKontraktDTO

class TjansteKontraktApiController extends RestfulController {

    TjansteKontraktApiController() {
        super(TjansteKontraktDTO)
    }
	
	def tjansteKontraktService

    def query() {
        log.debug params
        def serviceContracts = tjansteKontraktService.getServiceContracts(params.hsaId, params.environmentId, params.serviceDomainId)
        respond serviceContracts.collect {
            it.properties.minus(it.properties.findAll { it.value == null}) //hack to get rid of null values in the api
        }
    }
}
