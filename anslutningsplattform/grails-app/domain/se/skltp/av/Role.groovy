package se.skltp.av

import groovy.transform.ToString;

/**
 * Currently named Role instead of Roll to map with ShiroDbRealm
 * 
 */
@ToString
class Role {
    String name

    static hasMany = [ anvandare: User, permissions: String ]
    static belongsTo = User

    static constraints = {
        name nullable: false, blank: false, unique: true
    }
}
