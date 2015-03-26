package se.skltp.ap.api

import grails.converters.JSON
import se.skltp.ap.services.dto.domain.BestallningDTO

/**
 * Created by martin on 25/03/15.
 */
class BestallningApiController {

    def bestallningService

    def createProducentBestallning(BestallningDTO bestallningDTO) {
        if (bestallningDTO.hasErrors()) {
            render bestallningDTO.errors as JSON
        } else {
            log.debug "createProducentBestallning: $bestallningDTO"
            bestallningService.handleBestallning(bestallningDTO)
            render status: 201
        }
    }
}
