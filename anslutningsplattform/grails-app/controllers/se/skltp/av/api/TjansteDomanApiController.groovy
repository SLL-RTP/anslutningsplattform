package se.skltp.av.api

import grails.rest.RestfulController
import se.skltp.av.services.dto.TjansteDomanDTO

class TjansteDomanApiController extends RestfulController {

    TjansteDomanApiController() {
        super(TjansteDomanDTO)
    }

    def rivTaService

    def list() {
        log.debug params
        def serviceDomains = rivTaService.listTjansteDoman()
        respond serviceDomains.collect {
            it.properties.minus(it.properties.findAll { it.value == null}) //hack to get rid of null values in the api
        }
    }
}
