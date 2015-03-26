package se.skltp.av.service

import java.util.List
import se.skltp.av.services.dto.TjansteKomponentDTO

import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper
import se.skltp.av.TjansteKomponent
import se.skltp.av.services.dto.TjansteKomponentDTO

@Transactional
class TjansteKomponentService {
	
	def takService

    @Transactional(readOnly = true)
    List<TjansteKomponentDTO> query(String takId, String queryString, int limit) {
		// search both TAK and AP data to support cases where
		// 1. there is an order in AP to set up a new TjansteKomponent in TAK, but the order hasn't been processed yet   
		// 2. there is an existing TjansteKomponent in TAK that was registered before AP was live
		
		// search AP
        def domainServiceComponents = TjansteKomponent.findAllByHsaIdIlikeOrNamnIlike("%$queryString%", "%$queryString%")
        domainServiceComponents.collect {
            new TjansteKomponentDTO(
                    hsaId: it.hsaId,
                    namn: it.namn,
            )
        }
		
		if (domainServiceComponents.size() < limit) {
			// also search TAK
			//List<TjansteKomponentDTO> takList = takService.freeTextSearchTjansteKomponent(takId, queryString, limit)
			// add TAK data to result, prioritize AP data that is richer
			
			// TODO: do not add duplicates ...
			
			// TODO: tmp hardcoded ...
			def fakeTakTjanstekomponents = [
				["SE2321000016-A21S", "SLL - Prosang"],
				["SE2321000016-A22S", "HSF SLL - Remittera"],
				["SE2321000016-A1DB", "SLL - Obstetrix"],
				["SE2321000016-9119", "SLL - FlexLab"],
				["SE2321000016-6RK5", "SLL - TakeCare"]
			]			 
			def takList = new ArrayList()
			fakeTakTjanstekomponents.each {
				takList.add(new TjansteKomponentDTO(hsaId: it[0], namn: it[1]))
			}			
			
			domainServiceComponents.addAll(takList)
		}
		
		
		// apply limit
		if (domainServiceComponents.size() > limit) {
			domainServiceComponents.take(limit)
		}
		
		domainServiceComponents
    }

    TjansteKomponentDTO findByHsaId(String hsaId) {
        def domainServiceComponent = TjansteKomponent.findByHsaId(hsaId)
        new TjansteKomponentDTO(
                hsaId: domainServiceComponent.hsaId,
                namn: domainServiceComponent.namn,
                organisation: domainServiceComponent.organisation,
                huvudAnsvarigNamn: domainServiceComponent.huvudAnsvarigNamn,
                huvudAnsvarigEpost: domainServiceComponent.huvudAnsvarigEpost,
                huvudAnsvarigTelefon: domainServiceComponent.huvudAnsvarigTelefon,
                tekniskKontaktEpost: domainServiceComponent.tekniskKontaktEpost,
                tekniskKontaktNamn: domainServiceComponent.tekniskKontaktNamn,
                tekniskKontaktTelefon: domainServiceComponent.tekniskKontaktTelefon,
                funktionsBrevladaEpost: domainServiceComponent.funktionsBrevladaEpost,
                funktionsBrevladaTelefon: domainServiceComponent.funktionsBrevladaTelefon,
                ipadress: domainServiceComponent.ipadress,
                pingForConfiguration: domainServiceComponent.pingForConfiguration
        )
    }

    boolean update(TjansteKomponentDTO dto) {
        def dbTjk = TjansteKomponent.findByHsaId(dto.hsaId)
        if (dbTjk != null) {
            use(InvokerHelper) {
                dbTjk.setProperties(dto.properties)
            }
            dbTjk.save()
            return true
        }
        false
    }
}
