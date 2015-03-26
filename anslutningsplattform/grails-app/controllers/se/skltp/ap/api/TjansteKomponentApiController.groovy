package se.skltp.ap.api

import grails.converters.JSON
import se.skltp.ap.services.dto.TjansteKomponentDTO

class TjansteKomponentApiController {
	
	public static final int DEFAULT_FREE_TEXT_SEARCH_LIMIT = 10

    def tjansteKomponentService

    def query() {
        log.debug params
        //TODO: Hämta bara namn och hsaId
		//TODO: hardwired takId ... until we get it in the request ...
		def takId = "ntjp-test"
		if ("production".equals(System.getProperty("grails.env", "dev"))) {
			takId = "sll-qa"
		}
		
        def serviceComponentDTOs = tjansteKomponentService.query(takId, params.query, params.hasProperty('limit') ? params.getInt('limit') : DEFAULT_FREE_TEXT_SEARCH_LIMIT)
        respond serviceComponentDTOs.collect {
            it.properties.minus(it.properties.findAll { it.value == null }) //hack to get rid of null values in the api
        }
    }

    def get(String id) {
        log.debug params
        def serviceComponentDTO = tjansteKomponentService.findByHsaId(id)
        respond serviceComponentDTO.properties.minus(serviceComponentDTO.properties.findAll { it.value == null }) //hack to get rid of null values in the api
    }

    def put(String id) {
        def dto = new TjansteKomponentDTO(JSON.parse(request))
        def success = tjansteKomponentService.update(dto)
        render(status: success ? 204 : 400)
    }
}
