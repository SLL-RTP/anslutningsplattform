package se.skltp.ap.services.dto.domain

import grails.validation.Validateable
import groovy.transform.ToString

/**
 * Created by martin on 25/03/15.
 */
@Validateable
@ToString
class ProducentanslutningDTO {
    String rivtaProfil
    String url
    String tjanstekontraktNamnrymd
    int tjanstekontraktMajorVersion
    int tjanstekontraktMinorVersion
    Date giltigFranTid
    Date giltigTillTid
    List<LogiskAdressDTO> nyaLogiskaAdresser

    static constraints = {
        rivtaProfil nullable: false, blank: false
        url nullable: false, blank: false
        tjanstekontraktNamnrymd nullable: false, blank: false
        tjanstekontraktMajorVersion nullable: false, blank: false
        tjanstekontraktMinorVersion nullable: false, blank: false
        //giltigFranTid TODO: ??
        //giltigTillTid TODO: ??
        //nyaLogiskaAdresser TODO: ??
    }
}
