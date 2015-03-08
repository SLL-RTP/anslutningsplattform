package se.skltp.av.api

import se.skltp.av.services.dto.ProducentBestallningDTO

class UppdateraProducentBestallningApiController{

    static namespace = 'v1'

    def producentBestallningService

    def save(ProducentBestallningDTO producentBestallningDTO) {

        log.debug "API, a save requested for (uppdatera)producentbestallning: $producentBestallningDTO"

        def bestallning = producentBestallningService.createUpdateProducentBestallning(producentBestallningDTO)

        //TODO: send email here!?

        render status: bestallning ? 201 : 500
    }
}
