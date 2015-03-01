package se.skltp.av.service



import spock.lang.*

/**
 *
 */
class HsaServiceSpec extends Specification {

	def hsaService

    def setup() {
    }

    def cleanup() {
    }

    void "search hsa id"() {

		expect:
			numberOfResults == hsaService.freeTextSearch(hsaId, -1).size();
		where:
			hsaId 				| numberOfResults
			"SE0000000002-1234"	| 1

    }

	void "search dn"() {

		expect:
			numberOfResults == hsaService.freeTextSearch(dn, -1).size();
		where:
			dn	 			| numberOfResults
			"Nässjö VC DLM"	| 1
			"Nässjö VC"		| 2

	}

	void "search hsa id and dn"() {

		expect:
			numberOfResults == hsaService.freeTextSearch(hsaIdAndDn, -1).size();
		where:
			hsaIdAndDn 								| numberOfResults
			"SE 1234 Höglandets sjukvårdsområde"	| 5

	}

	void "search max nr of hits"() {

		expect:
			numberOfResults == hsaService.freeTextSearch(hsaId, maxNumberOfHits).size();
		where:
			hsaId 		| maxNumberOfHits  | numberOfResults
			"SE"		| 2		   		   | 2
			"SE"		| 3		   		   | 3

	}

	void "search no match"() {

		expect:
			numberOfResults == hsaService.freeTextSearch(hsaId, -1).size();
		where:
			hsaId 				| numberOfResults
			"NOT FOUND"			| 0
			""					| 0
			null				| 0

	}
}
