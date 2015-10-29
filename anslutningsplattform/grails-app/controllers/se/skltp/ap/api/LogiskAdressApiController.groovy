package se.skltp.ap.api

import grails.converters.JSON
import grails.rest.RestfulController
import se.skltp.ap.services.dto.domain.LogiskAdressDTO

class LogiskAdressApiController extends RestfulController {

    public static final int DEFAULT_HSA_FREE_TEXT_SEARCH_LIMIT = 10 //TODO: what is a good default? externalize?
    def logiskAdressService

    LogiskAdressApiController() {
        super(LogiskAdressDTO)
    }

    def query() {
        log.debug params
        def result = []
        if (params.query && params.environmentId) { //TODO: also require environment here?
            //free text search
            result = logiskAdressService.freeTextSearch(params.query, params.hasProperty('limit') ? params.getInt('limit') : DEFAULT_HSA_FREE_TEXT_SEARCH_LIMIT, params.environmentId)
        } else if (params.serviceDomainId && params.environmentId) {
            //get logical addresses currently in use
            result = logiskAdressService.getByEnvironmentAndServiceDomain(params.environmentId, params.serviceDomainId)
        }
        respond result
    }

    def getForContract(String serviceComponentId, String environmentId, String serviceContractNamespace, int majorVersion, int minorVersion) {
		// TODO: minorVersion is not needed here!
        respond logiskAdressService.getForServiceContract(serviceComponentId, environmentId, serviceContractNamespace, majorVersion)
    }

    def getForHsaId(String hsaId) {
        if (params.environmentId) {
            def logiskAddressDTO = logiskAdressService.getLogiskAdressForHsaId(params.environmentId, hsaId)
            if (!logiskAddressDTO.namn.contains('SAKNAS')) {
                //seems that you can not 'respond' a single object that does not contain a 'Long id'
                render logiskAddressDTO as JSON
            } else {
                render(status: 404)
            }
        } else {
            render(status: 404)
        }
    }

}
