package se.skltp.ap

import groovy.transform.ToString

/**
 * Created by martin on 25/03/15.
 */
@ToString(includeNames=true, includeFields = true)
class Bestallning {
    String bestallareRoll
    String status
    Driftmiljo driftmiljo
    Personkontakt bestallare
    Producentbestallning producentbestallning

    static hasMany = [
            konsumentbestallningar: Konsumentbestallning
    ]

    static constraints = {
        bestallareRoll nullable: false, blank: false
        status nullable: false, blank: false
        driftmiljo nullable: false
        bestallare nullable: false
        producentbestallning nullable: true
        konsumentbestallningar nullable: true
    }
}
