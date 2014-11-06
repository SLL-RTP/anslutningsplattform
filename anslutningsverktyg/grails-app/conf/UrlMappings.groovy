class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

				"/angular"(view:"/angular")
        "/"(view:"/index")
        "500"(view:'/error')
	}
}
