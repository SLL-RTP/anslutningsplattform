package se.skltp.ap

import groovy.transform.ToString

/**
 * Created by martin on 24/03/15.
 */
@ToString(includeNames=true, includeFields = true)
class Tjanstekomponent {
    String hsaId
    String beskrivning
    String organisation
    String ipadress
    String pingForConfigurationURL
    Personkontakt huvudansvarigKontakt
    Personkontakt tekniskKontakt
    Funktionkontakt tekniskSupportkontakt

    static hasMany = [driftmiljoer: Driftmiljo]

    static constraints = {
        hsaId blank: false, unique: true
        beskrivning nullable: true, blank: false
        organisation nullable: true, blank: false
        ipadress nullable: true, blank: false
        pingForConfigurationURL nullable: true, blank: false
        huvudansvarigKontakt nullable: true
        tekniskKontakt nullable: true
        tekniskSupportkontakt nullable: true
    }
}
