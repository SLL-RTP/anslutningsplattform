package se.skltp.ap.services.dto.domain

import grails.validation.Validateable
import groovy.transform.ToString

/**
 * Created by martin on 26/03/15.
 */
@Validateable
@ToString
class UppdateradKonsumentanslutningDTO extends KonsumentanslutningDTO {
    List<String> befintligaLogiskaAdresser
    List<String> borttagnaLogiskaAdresser
}
