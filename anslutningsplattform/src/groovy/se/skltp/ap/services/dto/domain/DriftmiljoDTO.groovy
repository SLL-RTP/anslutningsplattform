package se.skltp.ap.services.dto.domain

import grails.validation.Validateable
import groovy.transform.ToString

/**
 * Created by martin on 27/03/15.
 */
@Validateable
@ToString
class DriftmiljoDTO {
    String id
    String namn

    static constraints = {
        id nullable: false, blank: false
    }
}
