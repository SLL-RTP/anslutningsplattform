dataSource {
    pooled = true
    jmxExport = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
    flush.mode = 'manual' // OSIV session flush mode outside of transactional context
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    test {
        dataSource {
            dbCreate = "create-drop"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    production {
        // TODO: tmp in-mem db to get things going ...
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }        
        /*
        TODO: doesn't work with override config for some reason ...
        dataSource {
            // MySQL
            //
            // Ref: http://grails.github.io/grails-doc/2.4.3/ref/Plug-ins/dataSource.html
            // Ref: http://grails.github.io/grails-doc/2.4.3/guide/single.html#dataSource
            //
            pooled = true
            username = "ap_user"
            password = "TODO_CHANGE_ME"
            url = "jdbc:mysql://localhost:3306/anslutningsplattform?autoReconnect=true"
            driverClassName = "com.mysql.jdbc.Driver"
            dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
            properties {
                maxActive = 50
                minIdle = 10
                maxIdle = 25
                maxWait = 10000
                validationQuery = "SELECT 1"
                //validationQueryTimeout = 5000
                validationInterval = 60000
                testOnBorrow = true
                testWhileIdle = false
                testOnReturn = false
            }
        }
        */
    }
}
