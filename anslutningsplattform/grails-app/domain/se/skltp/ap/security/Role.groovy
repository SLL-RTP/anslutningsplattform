package se.skltp.ap.security

/**
 * Created by martin on 27/03/15.
 */
class Role {
    String name

    static hasMany = [ users: User, permissions: String ]
    static belongsTo = User

    static constraints = {
        name(nullable: false, blank: false, unique: true)
    }
}