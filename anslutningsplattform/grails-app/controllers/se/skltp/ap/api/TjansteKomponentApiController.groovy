package se.skltp.ap.api

import grails.converters.JSON
import se.skltp.ap.services.dto.domain.PersonkontaktDTO
import se.skltp.ap.services.dto.domain.TjanstekomponentDTO

import javax.servlet.http.HttpServletRequest

class TjansteKomponentApiController {
	
	public static final int DEFAULT_FREE_TEXT_SEARCH_LIMIT = 10

    def tjansteKomponentService
    def kontaktService
    def certificateExtractionService

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
            def returnCode = tjansteKomponentService.update(dto, getCurrentUser(request))
            if (returnCode == -1) {
                render(status: 400)
            } else if (returnCode == 0) {
               def m = [action:'none']
                render m as JSON
            } else {
                def m = [action:'email']
                render m as JSON
            }
        }
    }

    def post(TjanstekomponentDTO tjanstekomponentDTO) {
        log.debug tjanstekomponentDTO
        if (tjanstekomponentDTO.hasErrors()) {
            response.status = 400
            render tjanstekomponentDTO.errors as JSON
        } else {
            def success = tjansteKomponentService.create(tjanstekomponentDTO, getCurrentUser(request))
            render(status : success ? 201 : 400)
        }
    }

    private PersonkontaktDTO getCurrentUser(HttpServletRequest request) {
        def hsaId = certificateExtractionService.extractUserHsaId(request)
        getPersonkontaktDTO(hsaId)
    }

    private PersonkontaktDTO getPersonkontaktDTO(String hsaId) {
        def personkontaktDTO = kontaktService.findPersonkontaktDTOByHsaId(hsaId)
        if (personkontaktDTO == null) {
            personkontaktDTO = new PersonkontaktDTO(
                    hsaId: hsaId
            )
        }
        log.debug("$personkontaktDTO")
        personkontaktDTO
    }
}
