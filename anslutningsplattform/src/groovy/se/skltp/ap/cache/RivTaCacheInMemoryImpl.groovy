package se.skltp.ap.cache

import static groovy.json.JsonOutput.prettyPrint
import static groovy.json.JsonOutput.toJson

class RivTaCacheInMemoryImpl implements RivTaCache {

    private Map<String, RivTaTjansteDoman> cache;
    String cacheFile

    @Override
    List<RivTaTjansteDoman> getDomaner() {
        if (cache == null || cache.isEmpty()) {
            init(cacheFile)
        }
        return new ArrayList<RivTaTjansteDoman>(cache.values())
    }

    @Override
    List<RivTaTjansteKontrakt> getTjansteKontraktForDoman(String domanId) {
        if (cache == null || cache.isEmpty()) {
            init(cacheFile)
        }
        cache[domanId].tjanstekontrakt
    }

    private void init(String filePath) {
        def domainsNode = new XmlSlurper().parse(new FileInputStream(filePath))
        cache = domainsNode.domain.collectEntries { domain ->
            String domainId = domain.@name.text()
            def serviceContracts = domain.release.depthFirst().collect { it }.flatten().findResults {
                it.name() == 'serviceContract' ? it : null
            }.collect { serviceContract ->
                def majorVersion = 0
                def minorVersion = 0
                try {
                    majorVersion = serviceContract.@version.text()
                            .substring(0, serviceContract.@version.text().indexOf('.')).toInteger() //expect version to be in the form of n.n, e.g. 1.0
                } catch (RuntimeException e) {
                    //TODO: what to do if we can not parse majorVersion ?
                }
                try {
                    minorVersion = serviceContract.@version.text()
                            .substring(serviceContract.@version.text().indexOf('.') + 1).toInteger() //expect version to be in the form of n.n, e.g. 1.0
                } catch (RuntimeException e) {
                    //TODO: what to do if we can not parse minorVersion?
                }
                new RivTaTjansteKontrakt(
                        namn: serviceContract.text(),
                        namnrymd: "$domainId:${serviceContract.text()}", //TODO: how to get namnrymd?
                        majorVersion: majorVersion,
                        minorVersion: minorVersion
                )
            }.unique(false)

            def rivTaTjansteDoman = new RivTaTjansteDoman(
                    id: domainId,
                    svensktNamn: domain.@swedish_name.text().trim(),
                    svensktKortNamn: domain.@swedish_nickname.text().trim(),
                    tjanstekontrakt: serviceContracts
            )
            [(domainId): rivTaTjansteDoman]
        }
        logCache()
    }

    private void logCache() {
        println prettyPrint(toJson(cache))
    }
}
