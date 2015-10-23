package se.skltp.ap.services.dto.domain

import grails.validation.Validateable
import groovy.transform.ToString

/**
 * Created by martin on 25/03/15.
 */
@Validateable
@ToString
class ProducentbestallningDTO {
    List<ProducentanslutningDTO> producentanslutningar
    List<UppdateradProducentanslutningDTO> uppdateradProducentanslutningar
    TjanstekomponentDTO tjanstekomponent
    String namnPaEtjanst

    static constraints = {
        tjanstekomponent nullable: false
        namnPaEtjanst nullable: true
    }
}
