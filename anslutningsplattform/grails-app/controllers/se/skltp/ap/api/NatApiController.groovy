package se.skltp.ap.api

class NatApiController {

    def natService

    def list() {
        respond natService.getConfiguredNatDTOs()
    }
}
