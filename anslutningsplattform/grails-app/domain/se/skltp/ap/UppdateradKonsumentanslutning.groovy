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

    static mapping = {
        //nyaLogiskaAdresser joinTable: [name: 'uppdaterad_konsumentanslutning_logisk_adress', column: 'ny_logisk_adress_id', key: 'uppdaterad_konsumentanslutning_id'] FIXME: does not work as intended, unable to define joinTable for inherited properties
        befintligaLogiskaAdresser joinTable: [name: 'uppdaterad_konsumentanslutning_logisk_adress', column: 'befintlig_logisk_adress_id', key: 'uppdaterad_konsumentanslutning_id']
        borttagnaLogiskaAdresser joinTable: [name: 'uppdaterad_konsumentanslutning_logisk_adress', column: 'borttagen_logisk_adress_id', key: 'uppdaterad_konsumentanslutning_id']
    }
}
