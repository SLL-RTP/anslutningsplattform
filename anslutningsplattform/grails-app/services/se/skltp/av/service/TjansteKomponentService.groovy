package se.skltp.av.service

import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper
import se.skltp.av.TjansteKomponent
import se.skltp.av.services.dto.TjansteKomponentDTO

@Transactional
class TjansteKomponentService {

    @Transactional(readOnly = true)
    List<TjansteKomponentDTO> query(String queryString) {
        def domainServiceComponents = TjansteKomponent.findAllByHsaIdIlikeOrNamnIlike("%$queryString%", "%$queryString%")
        domainServiceComponents.collect {
            new TjansteKomponentDTO(
                    hsaId: it.hsaId,
                    namn: it.namn,
            )
        }
    }

    TjansteKomponentDTO findByHsaId(String hsaId) {
        def domainServiceComponent = TjansteKomponent.findByHsaId(hsaId)
        new TjansteKomponentDTO(
                hsaId: domainServiceComponent.hsaId,
                namn: domainServiceComponent.namn,
                huvudAnsvarigNamn: domainServiceComponent.huvudAnsvarigNamn,
                huvudAnsvarigEpost: domainServiceComponent.huvudAnsvarigEpost,
                huvudAnsvarigTelefon: domainServiceComponent.huvudAnsvarigTelefon,
                tekniskKontaktEpost: domainServiceComponent.tekniskKontaktEpost,
                tekniskKontaktNamn: domainServiceComponent.tekniskKontaktNamn,
                tekniskKontaktTelefon: domainServiceComponent.tekniskKontaktTelefon,
                funktionsBrevladaEpost: domainServiceComponent.funktionsBrevladaEpost,
                funktionsBrevladaTelefon: domainServiceComponent.funktionsBrevladaTelefon,
                ipadress: domainServiceComponent.ipadress
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
