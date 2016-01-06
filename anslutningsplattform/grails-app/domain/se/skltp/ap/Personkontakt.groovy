package se.skltp.ap

import groovy.transform.ToString

/**
 * Created by martin on 24/03/15.
 */
@ToString(includeNames=true, includeFields = true)
class Personkontakt {
    String hsaId
    String namn
    String epost
    String telefon
    String otherInfo

    static hasMany = [
            bestallningar: Bestallning
    ]

    static constraints = {
        epost blank: false //should also be unique but not possible since we also use this entity embedded in Tjanstekomponent
        hsaId nullable: true
        otherInfo nullable: true
    }
}
