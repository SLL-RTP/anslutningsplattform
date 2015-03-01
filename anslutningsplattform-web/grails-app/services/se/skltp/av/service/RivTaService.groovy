package se.skltp.av.service

import grails.transaction.Transactional
import se.skltp.av.services.dto.TjansteDomanDTO
import se.skltp.av.services.dto.TjansteKontraktDTO

@Transactional(readOnly = true)
class RivTaService {

    def rivTaCache

    List<TjansteKontraktDTO> getTjansteKontraktForDoman(String serviceDomainId) {
        def kontraktForDoman = rivTaCache.getTjansteKontraktForDoman(serviceDomainId)
        kontraktForDoman.collect {
            new TjansteKontraktDTO(
                    namn: it.namn,
                    namnrymd: it.namnrymd,
                    majorVersion: it.majorVersion,
                    minorVersion: it.minorVersion
            )
        }
    }

    List<TjansteDomanDTO> listTjansteDoman() {
        rivTaCache.getDomaner().collect {
            new TjansteDomanDTO(
                    tjansteDomanId: it.id,
            )
        }
    }

}
