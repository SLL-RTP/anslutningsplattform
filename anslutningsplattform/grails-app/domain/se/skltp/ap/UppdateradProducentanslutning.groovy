package se.skltp.ap

import groovy.transform.ToString

/**
 * Created by martin on 24/03/15.
 */
@ToString(includeNames=true, includeFields = true, includeSuper = true)
class UppdateradProducentanslutning extends Producentanslutning {

    //String tidigareRivtaProfil
    //String tidigareUrl

    static hasMany = [
            befintligaLogiskaAdresser: LogiskAdress,
            borttagnaLogiskaAdresser: LogiskAdress
    ]

    static constraints = {
        befintligaLogiskaAdresser nullable: true
        borttagnaLogiskaAdresser nullable: true
    }
}