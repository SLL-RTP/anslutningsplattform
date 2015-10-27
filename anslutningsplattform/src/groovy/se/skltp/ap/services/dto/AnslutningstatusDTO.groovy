package se.skltp.ap.services.dto

import groovy.transform.ToString

/**
 * @author Martin Samuelsson
 */
@ToString(includeNames=true, includeFields = true)
class AnslutningStatusDTO {
    String tjanstekontraktNamn
    String tjanstekontraktNamnrymd
    int tjanstekontraktMajorVersion
    int tjanstekontraktMinorVersion
    Date giltigFranTid
    Date giltigTillTid
    boolean installeratIDriftmiljo
    List<LogiskAdressStatusDTO> logiskAdressStatuses
}
