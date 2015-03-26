package se.skltp.ap

import groovy.transform.ToString

/**
 * Created by martin on 25/03/15.
 */
@ToString(includeNames=true, includeFields = true, includeSuper = true)
class UppdateradKonsumentanslutning extends Konsumentanslutning {

    static hasMany = [
            befintligaLogiskaAdresser: LogiskAdress,
            borttagnaLogiskaAdresser : LogiskAdress
    ]

    static constraints = {
        befintligaLogiskaAdresser nullable: true
        borttagnaLogiskaAdresser nullable: true
    }
}
