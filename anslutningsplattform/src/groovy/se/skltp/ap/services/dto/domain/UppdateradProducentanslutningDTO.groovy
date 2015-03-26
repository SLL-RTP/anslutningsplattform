package se.skltp.ap.services.dto.domain

import grails.validation.Validateable
import groovy.transform.ToString

/**
 * Created by martin on 25/03/15.
 */
@Validateable
@ToString
class UppdateradProducentanslutningDTO extends ProducentanslutningDTO {
    List<String> befintligaLogiskaAdresser
    List<String> borttagnaLogiskaAdresser
}
