package se.skltp.av.service

import grails.transaction.Transactional
import se.skl.tp.hsa.cache.HsaCache
import se.skl.tp.hsa.cache.HsaCacheImpl
import se.skl.tp.hsa.cache.HsaNode;
import se.skltp.av.services.dto.HsaDTO


@Transactional(readOnly = true)
class HsaService {
	
	def grailsApplication
	
	private HsaCache hsaCache

    def freeTextSearch(String searchText, int maxNoOfHits) {
		
		def resultFromCache = getHsaCache().freeTextSearch(searchText, maxNoOfHits);
		
		def result = []
		resultFromCache.each{
			result.add(new HsaDTO(hsaId: it.hsaId, dn: it.dn, namn: it.name))
		}
		
		return result;
    }
	
	String getNameForHsaId(String hsaId) {
		// TODO: this is ugly - uses an implementation method NOT found in the HsaCache interface
		HsaNode internalHsaNode = getHsaCache().getNode(hsaId)
		internalHsaNode ? internalHsaNode.getName() : "NAMN SAKNAS (ingen träff i HSA för HSA-id: $hsaId)"
	}
	
	def getHsaCache(){
		if(!hsaCache){
			def hsaFiles = grailsApplication.config.hsa.hsacache.files

			log.info "HSA cache not loaded, lets load from files: ${hsaFiles}"

			hsaCache = new HsaCacheImpl(hsaFiles as String[]);
		}
		return hsaCache
	}
	
	
}
