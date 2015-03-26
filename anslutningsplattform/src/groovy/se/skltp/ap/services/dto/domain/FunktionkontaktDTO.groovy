package se.skltp.ap.services.dto.domain

import grails.validation.Validateable
import groovy.transform.ToString

/**
 * Created by martin on 25/03/15.
 */
@Validateable
@ToString
class FunktionkontaktDTO {
    String epost
    String telefon

    static constraints = {
        epost email: true, blank: false
        telefon nullable: false, blank: false
    }
}
