package se.skltp.ap

import groovy.transform.ToString

/**
 * Created by martin on 25/03/15.
 */
@ToString(includeNames=true, includeFields = true)
class Konsumentanslutning {
    String tjanstekontraktNamnrymd
    int tjanstekontraktMajorVersion
    int tjanstekontraktMinorVersion
    Date giltigFranTid
    Date giltigTillTid

    static hasMany = [nyaLogiskaAdresser: LogiskAdress]

    static constraints = {
        tjanstekontraktNamnrymd nullable: false, blank: false
        giltigFranTid nullable: false
        giltigTillTid nullable: false
        nyaLogiskaAdresser nullable: true
    }

}
