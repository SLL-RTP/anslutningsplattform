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
    String ipadress
    String pingForConfigurationURL
    PersonkontaktDTO huvudansvarigKontakt
    PersonkontaktDTO tekniskKontakt
    FunktionkontaktDTO tekniskSupportKontakt

    static constraints = {
        id nullable: true
        hsaId nullable: false
        beskrivning nullable: false
        organisation nullable: true, blank: true
        ipadress nullable: true, blank: true
        pingForConfigurationURL nullable: true, blank: true
        huvudansvarigKontakt nullable: false
        tekniskKontakt nullable: false
        tekniskSupportKontakt nullable: false
    }
}
