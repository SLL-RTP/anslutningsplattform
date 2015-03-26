package se.skltp.ap.security

/**
 * Created by martin on 27/03/15.
 */
class User {
    String username
    String passwordHash
    String namn
    String epost
    String datumSkapad

    static hasMany = [ roles: Role, permissions: String ]

    static constraints = {
        username(nullable: false, blank: false, unique: true)
    }
}