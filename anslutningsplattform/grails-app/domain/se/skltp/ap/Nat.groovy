package se.skltp.ap

import groovy.transform.ToString

/**
 * Created by martin on 07/05/15.
 */
@ToString(includeNames=true, includeFields = true)
class Nat {
    String id
    String namn

    static constraints = {
        id nullable: false, blank: false, unique: true
        namn nullable: false, blank: false
    }

    static mapping = {
        id generator: 'assigned'
    }
}
