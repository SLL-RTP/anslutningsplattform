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

    static mapping = {
        nyaLogiskaAdresser joinTable: [name: 'konsumentanslutning_logisk_adress', column: 'ny_logisk_adress_id', key: 'konsumentanslutning_id']
        tablePerConcreteClass true
        id generator: 'hilo' //https://jira.grails.org/browse/GRAILS-10849?focusedCommentId=80548&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-80548
    }

}
