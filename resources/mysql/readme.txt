Generate DDL from Grails (2.4.3) by:

1. Temporarily add dialect to the DataSource.groovy top dataSource declaration

dataSource {
    dialect = org.hibernate.dialect.MySQL5InnoDBDialect
    pooled = true

2. run: grails schema-export ddl-mysql5.sql
