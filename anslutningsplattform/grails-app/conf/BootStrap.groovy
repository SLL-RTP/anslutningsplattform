import org.apache.shiro.crypto.hash.Sha256Hash
import se.skltp.ap.Funktionkontakt
import se.skltp.ap.Personkontakt
import se.skltp.ap.Nat
import se.skltp.ap.Tjanstekomponent
import se.skltp.ap.security.Role
import se.skltp.ap.security.User

class BootStrap {

    def grailsApplication

    def init = { servletContext ->

        environments {
            production {

            }
            development {
                def pk1 = new Personkontakt(
                        hsaId: "pk-hsa-1",
                        namn: "Kapo Kapo",
                        epost: "kapo@kapo.com",
                        telefon: "031170920"
                )

                def pk2 = new Personkontakt(
                        hsaId: "pk-hsa-2",
                        namn: "Hlava Hlava",
                        epost: "hlava@hlava.com",
                        telefon: "031170920"
                )

                def fk1 = new Funktionkontakt(
                        epost: "info@help.org",
                        telefon: "031170920"
                )

                def nat = Nat.findById("internet")
                if (nat == null) {
                    nat = new Nat(namn: "Internet")
                    nat.id = "internet"
                    nat.save(failOnError: true)
                }

                def tk1 = new Tjanstekomponent(
                        hsaId: "SE165565594230-0016",
                        beskrivning: "NPÖ 2",
                        organisation: "someOrg",
                        ipadress: "127.0.0.1",
                        pingForConfigurationURL: "http://ping.for.configuration.org/",
                        huvudansvarigKontakt: pk1,
                        tekniskKontakt: pk2,
                        tekniskSupportkontakt: fk1,
                        nat: nat
                )
                tk1.save(failOnError: true)

                //Roles
                def adminRole = new Role(name: 'ADMINISTRATÖR')
                adminRole.addToPermissions("Role:*")
                adminRole.addToPermissions("User:*")
                adminRole.addToPermissions("Bestallning:*")
                adminRole.addToPermissions("Driftmiljo:*")
                adminRole.addToPermissions("Funktionkontakt:'")
                adminRole.addToPermissions("Konsumentanslutning:*")
                adminRole.addToPermissions("Konsumentbestallning:*")
                adminRole.addToPermissions("LogiskAdress:*")
                adminRole.addToPermissions("Personkontakt:*")
                adminRole.addToPermissions("Producentanslutning:*")
                adminRole.addToPermissions("Producentbestallning:*")
                adminRole.addToPermissions("Tjanstekomponent:*")
                adminRole.addToPermissions("UppdateradKonsumentanslutning:*")
                adminRole.addToPermissions("UppdateradProducentanslutning:*")
                adminRole.save(failOnError: true)

                //Admin user
                def adminUser = new User(
                        namn: "Agda Andersson",
                        username: "admin",
                        epost: "admin@lorumipsum.nu",
                        datumSkapad: new Date(),
                        passwordHash: new Sha256Hash("password").toHex())

                adminUser.addToRoles(adminRole)
                adminUser.save(failOnError: true)

            }
        }
    }

    def destroy = {
    }
}
