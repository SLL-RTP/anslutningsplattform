class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "/anslutning"(view: "/anslutning/index")
        "/bestallning"(view: "/bestallning/index")
        "500"(view:'/error')

        group "/api", {
            "/bestallning"(controller: 'bestallning') {
                action = [GET: 'list']
                format = "json"
            }
        }
	}
}
