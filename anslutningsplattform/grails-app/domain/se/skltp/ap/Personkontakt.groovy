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

    static hasMany = [
            bestallningar                         : Bestallning,
            tekniskKontaktTjanstekomponenter      : Tjanstekomponent,
            huvudansvarigKontaktTjanstekomponenter: Tjanstekomponent
    ]

    static mappedBy = [
            huvudansvarigKontaktTjanstekomponenter: 'huvudansvarigKontakt',
            tekniskKontaktTjanstekomponenter      : 'tekniskKontakt'
    ]

    static constraints = {
        epost unique: true, blank: false
    }
}
