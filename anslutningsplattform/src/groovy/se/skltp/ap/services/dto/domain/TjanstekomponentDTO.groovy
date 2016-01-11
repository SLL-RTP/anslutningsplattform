package se.skltp.ap.services.dto.domain

import grails.validation.Validateable
import groovy.transform.ToString

/**
 * Created by martin on 25/03/15.
 */
@Validateable
@ToString
class TjanstekomponentDTO {
    Long id
    String hsaId
    String beskrivning
    String organisation
    String producentIpadress
    String producentDnsNamn
    String konsumentIpadress
    String pingForConfigurationURL
    PersonkontaktDTO huvudansvarigKontakt
    PersonkontaktDTO tekniskKontakt
    FunktionkontaktDTO tekniskSupportKontakt
    NatDTO nat
    String otherInfo

    static constraints = {
        id nullable: true
        hsaId nullable: false
        beskrivning nullable: false
        organisation nullable: true, blank: true
        producentIpadress nullable: true, blank: true
        producentDnsNamn nullable: true, blank: true
        konsumentIpadress nullable: true, blank: true
        pingForConfigurationURL nullable: true, blank: true
        huvudansvarigKontakt nullable: true
        tekniskKontakt nullable: true
        tekniskSupportKontakt nullable: true
        nat nullable: true
        otherInfo nullable: true, blank: true
    }
}
