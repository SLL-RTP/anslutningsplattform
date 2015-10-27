class UrlMappings {

    static mappings = {

        //Controllers
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }
        //API
        group "/api", {

            "/bestallning"(controller: 'bestallningApi') {
                action = [POST: "createProducentBestallning"]
                format = "json"
            }

            "/serviceProducerConnectionOrders"(version: '1.0', namespace: 'v1', controller: 'producentBestallningApi') {
                action = [POST: "save", GET: "list"]
                format = "json"
            }

            "/updateServiceProducerConnectionOrders"(version: '1.0', namespace: 'v1', controller: 'uppdateraProducentBestallningApi') {
                action = [POST: "save"]
                format = "json"
            }

            "/hsa"(version: '1.0', namespace: 'v1', controller: 'hsaApi')

            "/serviceComponents"(version: '1.0', controller: 'tjansteKomponentApi') {
                action = [GET: "query", POST: "post"]
                format = "json"
                "/serviceComponents/$id"(version: '1.0', controller: 'tjansteKomponentApi') {
                    action = [GET: "get", PUT: "put"]
                    format = "json"
                }
            }

            "/serviceComponents/$serviceComponentId/$environmentId/$serviceContractNamespace/$majorVersion/$minorVersion/logicalAddresses"(version: '1.0', controller: 'logiskAdressApi') {
                action = [GET: "getForContract"]
                format = "json"
            }

            "/serviceComponents/$serviceComponentId/$environmentId/$serviceContractNamespace/$majorVersion/$minorVersion/address"(version: '1.0', controller: 'adressApi') {
                action = [GET: "getAddress"]
                format = "json"
            }

            "/serviceDomains"(version: '1.0', controller: 'tjansteDomanApi') {
                action = [GET: "list"]
                format = "json"
            }

            "/serviceContracts"(version: '1.0', controller: 'tjansteKontraktApi') {
                action = [GET: "query"]
                format = "json"
            }

            "/logicalAddresses"(version: '1.0', controller: 'logiskAdressApi') {
                action = [GET: "query"]
                format = "json"
            }

            "/logicalAddresses/$hsaId"(version: '1.0', controller: 'logiskAdressApi') {
                action = [GET: "getForHsaId"]
                format = "json"
            }

            "/driftmiljos"(controller: 'driftMiljoApi') {
                action = [GET: "list"]
                format = "json"
            }

            "/nat"(controller: 'natApi') {
                action = [GET: "list"]
                format = "json"
            }

            "/currentUser"(controller: 'currentUserApi') {
                action = [GET: "get"]
                format = "json"
            }

            "/anslutningar/konsument"(controller: 'anslutningStatusApi') {
                action = [GET: "getKonsumentanslutningar"]
                format = "json"
            }

            "/anslutningar/producent"(controller: 'anslutningStatusApi') {
                action = [GET: "getProducentanslutningar"]
                format = "json"
            }
        }

        "/"(view: "/index")
        "500"(view: '/error')
    }

}