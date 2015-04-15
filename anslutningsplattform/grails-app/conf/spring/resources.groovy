// Place your Spring DSL code here

import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean
import se.skltp.ap.cache.RivTaCacheInMemoryImpl

beans = {

	/* Default excludes for api v1 */
	final v1_DEFAULT_EXCLUDES = ['class']

    rivTaCache(RivTaCacheInMemoryImpl) {
        cacheFile = grailsApplication.config.rivta.cache.file
    }

    freemarkerConfiguration(FreeMarkerConfigurationFactoryBean) {
        templateLoaderPaths = ["classpath:grails-app/conf/templates"]
    }
}
