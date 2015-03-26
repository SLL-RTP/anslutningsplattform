package se.skltp.ap.services.dto.domain

import grails.validation.Validateable
import groovy.transform.ToString

/**
 * Created by martin on 25/03/15.
 */
@Validateable
@ToString
class PersonkontaktDTO {
    String hsaId
    String namn
    String epost
    String telefon

    static constraints = {
        hsaId nullable: true, blank: false
        namn nullable: false, blank: false
        epost nullable: false, email: true, blank: false
        telefon nullable: false, blank: false
    }
}
