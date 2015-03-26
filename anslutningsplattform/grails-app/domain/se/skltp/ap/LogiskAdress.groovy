package se.skltp.ap

import groovy.transform.ToString

/**
 * Created by martin on 24/03/15.
 */
@ToString(includeNames=true, includeFields = true)
class LogiskAdress {
    String hsaId

    static constraints = {
        hsaId nullable: false, blank: false, unique: true
    }
}
