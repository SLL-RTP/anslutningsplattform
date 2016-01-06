package se.skltp.ap.api

import se.skltp.ap.services.dto.domain.PersonkontaktDTO

import javax.servlet.http.HttpServletRequest

/**
 * Created by martin on 28/03/15.
 */
class CurrentUserApiController {

    def kontaktService
    def certificateExtractionService

    def get() {
        respond getCurrentUser(request)
    }

    private PersonkontaktDTO getCurrentUser(HttpServletRequest request) {
        def hsaId = certificateExtractionService.extractUserHsaId(request)
        getPersonkontaktDTO(hsaId)
    }

    def getPersonkontaktDTO(String hsaId) {
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
