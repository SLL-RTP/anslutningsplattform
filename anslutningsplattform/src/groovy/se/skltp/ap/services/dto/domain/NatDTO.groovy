package se.skltp.ap.services.dto.domain

import grails.validation.Validateable
import groovy.transform.ToString

/**
 * Created by martin on 07/05/15.
 */
@Validateable
@ToString
class NatDTO {
    String id
    String namn

    static constraints = {
        id nullable: false, blank: false
    }
}
