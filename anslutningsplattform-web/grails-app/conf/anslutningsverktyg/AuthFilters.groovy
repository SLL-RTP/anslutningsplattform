package anslutningsverktyg

class AuthFilters {
    def grailsApplication

    def filters = {
        api(uri: '/api/**') {
            before = {
                def apiToken = grailsApplication?.config?.api?.auth?.token
                def requestToken = request.getHeader('x-ap-auth')

                if(requestToken == apiToken) {
                    return true
                } else {
                    response.setStatus(401)
                    return false
                }
            }
        }
    }
}
