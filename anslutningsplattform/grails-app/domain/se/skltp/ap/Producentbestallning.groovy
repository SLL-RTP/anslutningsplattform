package se.skltp.ap

import groovy.transform.ToString

/**
 * Created by martin on 25/03/15.
 */
@ToString(includeNames=true, includeFields = true)
class Producentbestallning {
    Tjanstekomponent tjanstekomponent
    String namnPaEtjanst

    static hasMany = [
            producentanslutningar: Producentanslutning,
            uppdateradProducentanslutningar: UppdateradProducentanslutning
    ]

    static constraints = {
        tjanstekomponent nullable: false
        producentanslutningar nullable: true
        uppdateradProducentanslutningar nullable: true
        namnPaEtjanst nullable: true
    }
}

