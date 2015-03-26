package se.skltp.ap.services.dto.domain

import grails.validation.Validateable
import groovy.transform.ToString

/**
 * Created by martin on 26/03/15.
 */
@Validateable
@ToString
class KonsumentanslutningDTO {
    String tjanstekontraktNamnrymd
    int tjanstekontraktMajorVersion
    int tjanstekontraktMinorVersion
    List<String> nyaLogiskaAdresser

    static constraints = {
        tjanstekontraktNamnrymd nullable: false, blank: false
        tjanstekontraktMajorVersion nullable: false, blank: false
        tjanstekontraktMinorVersion nullable: false, blank: false
        //nyaLogiskaAdresser TODO: ??
    }
}
