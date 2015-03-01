package se.skltp.av

import groovy.transform.ToString;

@ToString
class DriftMiljo {

    String namn

    static constraints = {
        namn nullable: false, blank: false
    }
}
