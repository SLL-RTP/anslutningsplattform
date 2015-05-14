class NoCacheFilters {

    def filters = {
        all(uri: '/api/**') {
            before = {
                response.setHeader('Cache-Control', 'no-cache, no-store, must-revalidate')
                response.setHeader('Pragma', 'no-cache')
                response.setDateHeader('Expires', 0)
            }
        }
    }
}
