package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.Personkontakt
import se.skltp.ap.services.dto.domain.PersonkontaktDTO

/**
 * Created by martin on 08/04/15.
 */
@Transactional
class KontaktService {

    @Transactional(readOnly = true)
    PersonkontaktDTO findPersonkontaktDTOByHsaId(String hsaId) {
        def personkontakt = Personkontakt.findByHsaId(hsaId)
        if (personkontakt != null) {
            return new PersonkontaktDTO(
                    hsaId: personkontakt.hsaId,
                    epost: personkontakt.epost,
                    telefon: personkontakt.telefon)
        }
        return null;
    }
}
