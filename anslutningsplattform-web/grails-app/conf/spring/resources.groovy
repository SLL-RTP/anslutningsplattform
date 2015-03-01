// Place your Spring DSL code here

import grails.rest.render.json.JsonRenderer
import grails.rest.render.json.JsonCollectionRenderer
import se.skltp.av.api.v1.*
import se.skltp.av.*
import se.skltp.av.cache.RivTaCacheInMemoryImpl
import se.skltp.av.services.dto.*;

beans = {

	/* Default excludes for api v1 */
	final v1_DEFAULT_EXCLUDES = ['class']

	producentBestallningV1Renderer(JsonRenderer, ProducentBestallningDTO){
		excludes = v1_DEFAULT_EXCLUDES
	}

	producentBestallningV1CollectionRenderer(JsonCollectionRenderer, ProducentBestallningDTO){
		excludes = v1_DEFAULT_EXCLUDES
	}
	
	tjanstekomponentV1Renderer(JsonRenderer, TjansteKomponent){
		excludes = v1_DEFAULT_EXCLUDES
	}
	
	tjanstekomponentV1CollectionRenderer(JsonCollectionRenderer, TjansteKomponent){
		excludes = v1_DEFAULT_EXCLUDES
	}

    rivTaCache(RivTaCacheInMemoryImpl) {
        cacheFile = grailsApplication.config.rivta.cache.file
    }
}
