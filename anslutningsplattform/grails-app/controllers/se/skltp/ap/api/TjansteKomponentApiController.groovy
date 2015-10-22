package se.skltp.ap.api

import grails.converters.JSON
import se.skltp.ap.services.dto.domain.TjanstekomponentDTO
class TjansteKomponentApiController {
	
	public static final int DEFAULT_FREE_TEXT_SEARCH_LIMIT = 10

    def tjansteKomponentService

    def query() {
        log.debug params
        respond tjansteKomponentService.query(params.takId, params.query, params.hasProperty('limit') ? params.getInt('limit') : DEFAULT_FREE_TEXT_SEARCH_LIMIT)
    }

    def get(String id) {
        log.debug params
        respond tjansteKomponentService.findByHsaId(id, params.takId)
    }

    def put(String id, TjanstekomponentDTO dto) {
        log.debug dto
        if (dto.hasErrors()) {
            response.status = 400
            render dto.errors as JSON
        } else {
            def success = tjansteKomponentService.update(dto)
            render(status: success ? 204 : 400)
        }
    }

    def post(TjanstekomponentDTO tjanstekomponentDTO) {
        log.debug tjanstekomponentDTO
        if (tjanstekomponentDTO.hasErrors()) {
            response.status = 400
            render tjanstekomponentDTO.errors as JSON
        } else {
            def success = tjansteKomponentService.create(tjanstekomponentDTO)
            render(status : success ? 201 : 400)
        }
    }
}
