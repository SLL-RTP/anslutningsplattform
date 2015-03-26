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
        beskrivning nullable: false, blank: false
        organisation nullable: false, blank: false
        ipadress nullable: false, blank: false
        pingForConfigurationURL nullable: false, blank: false
        huvudansvarigKontakt nullable: false
        tekniskKontakt nullable: false
        tekniskSupportkontakt nullable: false
    }
}
