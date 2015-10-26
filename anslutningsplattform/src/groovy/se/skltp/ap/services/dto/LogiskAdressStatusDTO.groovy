package se.skltp.ap.services.dto

import groovy.transform.ToString

/**
 * @author Martin Samuelsson
 */
@ToString(includeNames=true, includeFields = true)
class LogiskAdressStatusDTO {
    String hsaId
    String namn
    boolean possible
    boolean enabled
}
