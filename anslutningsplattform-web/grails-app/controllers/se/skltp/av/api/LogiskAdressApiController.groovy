package se.skltp.av.api

import grails.rest.RestfulController
import se.skltp.av.services.dto.LogiskAdressDTO

class LogiskAdressApiController extends RestfulController {

    public static final int DEFAULT_HSA_FREE_TEXT_SEARCH_LIMIT = 10 //TODO: what is a good default? externalize?
    def logiskAdressService

    LogiskAdressApiController() {
        super(LogiskAdressDTO)
    }

    def query() {
        log.debug params
        def result = []
        if (params.query) { //TODO: also require environment here?
            //free text search
            result = logiskAdressService.freeTextSearch(params.query, params.hasProperty('limit') ? params.getInt('limit') : DEFAULT_HSA_FREE_TEXT_SEARCH_LIMIT)
        } else if (params.serviceDomainId && params.environmentId) {
            //get logical addresses currently in use
            result = logiskAdressService.getByEnvironmentAndServiceDomain(params.environmentId, params.serviceDomainId)
        }
        respond result
    }
}
