package se.skltp.ap

import groovy.transform.ToString

/**
 * Created by martin on 24/03/15.
 */
@ToString(includeNames=true, includeFields = true)
class Producentanslutning {
    String rivtaProfil
    String url
    String tjanstekontraktNamnrymd
    int tjanstekontraktMajorVersion
    int tjanstekontraktMinorVersion
    Date giltigFranTid
    Date giltigTillTid

    static hasMany = [nyaLogiskaAdresser: LogiskAdress]

    static constraints = {
        rivtaProfil nullable: false, blank: false
        url nullable: false, blank: false
        tjanstekontraktNamnrymd nullable: false, blank: false
        giltigFranTid nullable: false
        giltigTillTid nullable: false
        nyaLogiskaAdresser nullable: true
    }

    static mapping = {
        nyaLogiskaAdresser joinTable: [name: 'producentanslutning_logisk_adress', column: 'ny_logisk_adress_id', key: 'producentanslutning_id']
        tablePerConcreteClass true
        id generator: 'hilo' //https://jira.grails.org/browse/GRAILS-10849?focusedCommentId=80548&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-80548
    }

}
