package se.skltp.ap.api

import grails.rest.RestfulController
import se.skltp.ap.services.dto.AdressDTO

class AdressApiController extends RestfulController {

    def takService

    AdressApiController() {
        super(AdressDTO)
    }

    def getAddress(String serviceComponentId, String environmentId, String serviceContractNamespace, String majorVersion, String minorVersion) {
		// TODO: minorVersion is not needed here! 
        def adressDTO = takService.getAdressByTjanstekontraktAndHsaId(environmentId, serviceContractNamespace, majorVersion, serviceComponentId)
        respond adressDTO.properties.minus(adressDTO.properties.findAll { it.value == null })
    }
}
