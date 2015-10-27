package se.skltp.ap.api

class AnslutningStatusApiController {

    def logiskAdressService

    def getKonsumentanslutningar() {
        respond logiskAdressService.getKonsumentanslutningarForDoman(params.environmentId, params.serviceConsumerHsaId, params.serviceDomainId)
    }

    def getProducentanslutningar() {
        respond logiskAdressService.getProducentanslutningarForDoman(params.environmentId, params.serviceProducerHsaId, params.serviceDomainId)
    }
}
