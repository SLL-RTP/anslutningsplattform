import se.skltp.av.*
import se.skltp.av.util.BestallningsStatus;

import org.apache.shiro.crypto.hash.Sha256Hash

class BootStrap {

	def grailsApplication

	def init = { servletContext ->

		environments {
			production {

			}
			development {
				//Roles
				def adminRole = new Role(name: 'ADMINISTRATÖR')
				adminRole.addToPermissions("BestallningsHistorik:index")
				adminRole.addToPermissions("LogiskAdress:index")
				adminRole.addToPermissions("Role:*")
				adminRole.addToPermissions("User:*")
				adminRole.addToPermissions("Tjanstekomponent:index")
				adminRole.save(failOnError:true)

				def tkAnsvarigRole = new Role(name: 'TJÄNSTEKOMPONENTANSVARIG')
				tkAnsvarigRole.addToPermissions("BestallningsHistorik:*")
				tkAnsvarigRole.addToPermissions("LogiskAdress:*")
				tkAnsvarigRole.addToPermissions("Tjanstekomponent:*")
				tkAnsvarigRole.addToPermissions("ProdcentAnslutning:*")
				tkAnsvarigRole.addToPermissions("KonsumentAnslutning:*")
				tkAnsvarigRole.save(failOnError:true)


				//Admin user
				def adminUser = new User(
						namn: "Agda Andersson",
						username: "admin",
						epost: "admin@lorumipsum.nu",
						datumSkapad: new Date(),
						passwordHash: new Sha256Hash("password").toHex())

				adminUser.addToRoles(adminRole)
				adminUser.save(failOnError:true)

				//Tjanstekomponent responsible user
				def tkAnsvarig = new User(
						namn: "Jöns Jönsson",
						username: "user",
						epost: "user@lorumipsum.nu",
						datumSkapad: new Date(),
						passwordHash: new Sha256Hash("password").toHex())

				tkAnsvarig.addToRoles(tkAnsvarigRole)
				tkAnsvarig.save(failOnError:true)

                def tk1 = new TjansteKomponent(
                        hsaId: "SE165565594230-0016",
                        namn: "NPÖ 2",
                        huvudAnsvarigNamn: "Kapo Kapo",
                        huvudAnsvarigEpost: "kapo@kapo.com",
                        huvudAnsvarigTelefon: "1234",
                        tekniskKontaktEpost: "kontakten@lorumipsum.nu",
                        tekniskKontaktNamn: "Tolvan Tolvansson",
                        tekniskKontaktTelefon: "1234567890",
                        funktionsBrevladaEpost: "funktionsbrevladan@lorumipsum.nu",
                        funktionsBrevladaTelefon: "0987654321",
                        ipadress: "",
                        user: tkAnsvarig).save(failOnError:true)

                def tk2 = new TjansteKomponent(
                        hsaId: "SE5565594230-B3B",
                        namn: "Tieto Lifecare",
                        huvudAnsvarigNamn: "Hlava Hlava",
                        huvudAnsvarigEpost: "hlava@hlava.com",
                        huvudAnsvarigTelefon: "1234",
                        tekniskKontaktEpost: "kontakten@lorumipsum.nu",
                        tekniskKontaktNamn: "Tolvan Tolvansson",
                        tekniskKontaktTelefon: "1234567890",
                        funktionsBrevladaEpost: "funktionsbrevladan@lorumipsum.nu",
                        funktionsBrevladaTelefon: "0987654321",
                        ipadress: "",
                        user: tkAnsvarig).save(failOnError:true)

                def tk3 = new TjansteKomponent(
                        hsaId: "SE2321000016-532S",
                        namn: "MVK",
                        huvudAnsvarigNamn: "Cabeza Cabeza",
                        huvudAnsvarigEpost: "cabeza@cabeza.com",
                        huvudAnsvarigTelefon: "1234",
                        tekniskKontaktEpost: "kontakten@lorumipsum.nu",
                        tekniskKontaktNamn: "Tolvan Tolvansson",
                        tekniskKontaktTelefon: "1234567890",
                        funktionsBrevladaEpost: "funktionsbrevladan@lorumipsum.nu",
                        funktionsBrevladaTelefon: "0987654321",
                        ipadress: "",
                        user: tkAnsvarig).save(failOnError:true)

                def tk4 = new TjansteKomponent(
                        hsaId: "SE2321000016-6SW1",
                        namn: "L - Remittera",
                        huvudAnsvarigNamn: "머리",
                        huvudAnsvarigEpost: "meoli@meoli.com",
                        huvudAnsvarigTelefon: "1234",
                        tekniskKontaktEpost: "kontakten@lorumipsum.nu",
                        tekniskKontaktNamn: "Tolvan Tolvansson",
                        tekniskKontaktTelefon: "1234567890",
                        funktionsBrevladaEpost: "funktionsbrevladan@lorumipsum.nu",
                        funktionsBrevladaTelefon: "0987654321",
                        ipadress: "",
                        user: tkAnsvarig).save(failOnError:true)

				new ProducentBestallning(
						miljo: "TEST",
						status: BestallningsStatus.NY,
						tjansteKomponent: tk1).save(failOnError:true)

				new ProducentBestallning(
						miljo: "SIT",
						status: BestallningsStatus.NY,
						tjansteKomponent: tk1).save(failOnError:true)

                new DriftMiljo(
                        namn: 'Test'
                ).save(failOnError: true)

                new DriftMiljo(
                        namn: 'QA'
                ).save(failOnError: true)

                new DriftMiljo(
                        namn: 'Production'
                ).save(failOnError: true)
			}
		}
	}
	
	def destroy = {
	}
}
