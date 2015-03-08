package se.skltp.av.api

import grails.rest.RestfulController
import se.skltp.av.services.dto.AdressDTO

class AdressApiController extends RestfulController {

    def takService

    AdressApiController() {
        super(AdressDTO)
    }

    def getAddress(String serviceComponentId, String environmentId, String serviceContractNamespace, String majorVersion, String minorVersion) {
        def adressDTO = takService.getAdressByTjanstekontractAndHsaId(environmentId, serviceContractNamespace, majorVersion, minorVersion, serviceComponentId)
        respond adressDTO.properties.minus(adressDTO.properties.findAll { it.value == null })
    }
}
