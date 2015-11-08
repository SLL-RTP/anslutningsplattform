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
    Nat nat

    static hasMany = [
            driftmiljoer: Driftmiljo,
    ]
    static embedded = ['huvudansvarigKontakt', 'tekniskKontakt', 'tekniskSupportkontakt']

    static constraints = {
        hsaId blank: false, unique: true
        beskrivning nullable: false, blank: false
        organisation nullable: true, blank: true
        ipadress nullable: true, blank: true
        pingForConfigurationURL nullable: true, blank: true
        huvudansvarigKontakt nullable: true
        tekniskKontakt nullable: true
        tekniskSupportkontakt nullable: true
        nat nullable: true
    }
}
