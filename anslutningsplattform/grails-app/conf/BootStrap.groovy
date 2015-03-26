import org.apache.shiro.crypto.hash.Sha256Hash
import se.skltp.ap.Funktionkontakt
import se.skltp.ap.Personkontakt
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
                ).save(failOnError: true)

                def pk2 = new Personkontakt(
                        hsaId: "pk-hsa-2",
                        namn: "Hlava Hlava",
                        epost: "hlava@hlava.com",
                        telefon: "031170920"
                ).save(failOnError: true)

                def fk1 = new Funktionkontakt(
                        epost: "info@help.org",
                        telefon: "031170920"
                ).save(failOnError: true)

                def tk1 = new Tjanstekomponent(
                        hsaId: "SE165565594230-0016",
                        beskrivning: "NPÖ 2",
                        organisation: "someOrg",
                        ipadress: "127.0.0.1",
                        pingForConfigurationURL: "http://ping.for.configuration.org/",
                        huvudansvarigKontakt: pk1,
                        tekniskKontakt: pk1,
                        tekniskSupportkontakt: fk1
                ).save(failOnError: true)

                //Roles
                def adminRole = new Role(name: 'ADMINISTRATÖR')
                adminRole.addToPermissions("Role:*")
                adminRole.addToPermissions("User:*")
                adminRole.addToPermissions("Bestallning:index")
                adminRole.addToPermissions("Driftmiljo:index")
                adminRole.addToPermissions("Funktionkontakt:index")
                adminRole.addToPermissions("Konsumentanslutning:index")
                adminRole.addToPermissions("Konsumentbestallning:index")
                adminRole.addToPermissions("LogiskAdress:index")
                adminRole.addToPermissions("Personkontakt:index")
                adminRole.addToPermissions("Producentanslutning:index")
                adminRole.addToPermissions("Producentbestallning:index")
                adminRole.addToPermissions("Tjanstekomponent:index")
                adminRole.addToPermissions("UppdateradKonsumentanslutning:index")
                adminRole.addToPermissions("UppdateradProducentanslutning:index")
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
