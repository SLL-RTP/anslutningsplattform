package se.skltp.ap.services.dto.domain

import grails.validation.Validateable
import groovy.transform.ToString

/**
 * Created by martin on 25/03/15.
 */
@Validateable
@ToString
class TjanstekomponentDTO {
    String hsaId
    String beskrivning
    String organisation
    String ipadress
    String pingForConfigurationURL
    PersonkontaktDTO huvudansvarigKontakt
    PersonkontaktDTO tekniskKontakt
    FunktionkontaktDTO tekniskSupportKontakt

    static constraints = {
        hsaId nullable: false
        //beskrivning TODO: ??
        organisation nullable: false, blank: false
        ipadress nullable: false, blank: false
        pingForConfigurationURL nullable: false, blank: false
        huvudansvarigKontakt nullable: false
        tekniskKontakt nullable: false
        tekniskSupportKontakt nullable: false
    }
}
