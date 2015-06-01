package se.skltp.ap

import groovy.transform.ToString

/**
 * Created by martin on 25/03/15.
 */
@ToString(includeNames=true, includeFields = true)
class Konsumentbestallning {
    Tjanstekomponent tjanstekomponent

    static hasMany = [
            konsumentanslutningar: Konsumentanslutning,
            uppdateradKonsumentanslutningar: UppdateradKonsumentanslutning
    ]

    static constraints = {
        tjanstekomponent nullable: false
        konsumentanslutningar nullable: true
        uppdateradKonsumentanslutningar nullable: true
    }
}
