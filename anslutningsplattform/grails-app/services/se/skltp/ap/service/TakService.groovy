package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.service.tak.TakCache
import se.skltp.ap.service.tak.TakCacheServices
import se.skltp.ap.service.tak.TakCacheServicesImpl
import se.skltp.ap.service.tak.TakSyncCacheCallback
import se.skltp.ap.service.tak.m.PersistenceEntity
import se.skltp.ap.service.tak.m.TjanstekontraktDTO
import se.skltp.ap.service.tak.m.VirtualiseringDTO
import se.skltp.ap.service.tak.persistence.TakCacheFilePersistenceImpl
import se.skltp.ap.service.tak.persistence.TakCachePersistenceServices
import se.skltp.ap.services.dto.AdressDTO
import se.skltp.ap.services.dto.AnslutningStatusDTO
import se.skltp.ap.services.dto.LogiskAdressStatusDTO
import se.skltp.ap.services.dto.TakRoutingEntryDTO
import se.skltp.ap.services.dto.TjansteKontraktDTO
import se.skltp.ap.services.dto.domain.LogiskAdressDTO
import se.skltp.ap.services.dto.domain.TjanstekomponentDTO
import se.skltp.ap.util.TjanstekontraktUtil
import se.skltp.tak.vagvalsinfo.wsdl.v2.AnropsAdressInfoType
import se.skltp.tak.vagvalsinfo.wsdl.v2.TjanstekomponentInfoType
import se.skltp.tak.vagvalsinfo.wsdl.v2.VagvalsInfoType

import javax.annotation.PostConstruct

@Transactional(readOnly = true)
class TakService {

	def grailsApplication

	def takRoutingMap
	def takCacheMap

    def rivTaService
    def hsaService

	// don't do lazy init, we want to make sure that TAK-caches are populated
	// and available at startup to make sure config is ok
	boolean lazyInit = false

	@PostConstruct
	def init() {
		log.debug("init TAK routing from config ...")
		def confMap = grailsApplication.getFlatConfig()
		takRoutingMap = getTakRoutingMap(confMap)
		log.debug("done init TAK routing from config")

		log.debug("init TAK caches ...")
		takCacheMap = new HashMap()
		def takCacheEndpoints = new ArrayList<String>();

		takRoutingMap.values().each {
			takCacheMap.putAt(it.id, new TakCacheServicesImpl(it.url, it.id))
			takCacheEndpoints.add(it.url);
		}

		/**
		 * TODO: Better way, no point in itterating once more over config.
		 */

		def takCacheLocation;
		confMap.keySet().each {
			if(it.equals("tak.cache.location")) {
				takCacheLocation = confMap.get(it);
			}
		}

		log.debug("TakCache Location: " + takCacheLocation)

		TakCachePersistenceServices fileCache = new TakCacheFilePersistenceImpl(takCacheLocation);

		TakCache.sync(takCacheEndpoints,
			new TakSyncCacheCallback() {
				public void onSyncComplete(List<PersistenceEntity> persitencesEntitys) {
					fileCache.persistEndpoints(persitencesEntitys);
					log.debug("TakCache is synched")
				}

				public void onSyncSuccess(java.lang.String endpoint) {
					log.debug("Successfully cached: " + endpoint)
				}

				public void onSyncError(java.lang.String endpoint, java.lang.Exception error) {
					log.error("Failed to sync: " + endpoint, error);
					TakCache.loadPersistedCache(fileCache.getEndpoint(endpoint))
				}
			}
		);

		

		log.debug("done init TAK caches")
	}

	/**
	 * Read TAK routing config from configuration, expects config in
	 * conf/Config.groovy like:
	 * <pre>
	 * tak.env.id.'1' = 'ntjp-prod'
	 * tak.env.name.'1' = 'NTjP PROD'
	 * tak.env.url.'1' = 'http://TODO-PROD'
	 *
	 * tak.env.id.'2' = 'ntjp-qa'
	 * tak.env.name.'2' = 'NTjP QA'
	 * tak.env.url.'2' = 'http://TODO-QA'
	 *
	 * tak.env.id.'3' = 'ntjp-test'
	 * tak.env.name.'3' = 'NTjP TEST'
	 * tak.env.url.'3' = 'http://TODO-TEST'
	 * </pre>
	 *
	 * @param confMap
	 * @return
	 */
	def getTakRoutingMap(confMap) {
		def confTakMap = new HashMap()

		// populate map from configuration properties
		confMap.keySet().each {
			if (it.startsWith('tak.env.')) {
				log.debug("found TAK property: " + it + " = " + confMap.get(it))
				def takEnvNr = it.substring(it.lastIndexOf('.') + 1, it.length())

				TakRoutingEntryDTO dto = confTakMap.get(takEnvNr)
				if (dto == null) {
					dto = new TakRoutingEntryDTO()
					confTakMap.put(takEnvNr, dto)
				}

				if (it.startsWith('tak.env.id.')) {
					dto.id = confMap.get(it)
				}
				else if (it.startsWith('tak.env.name.')) {
					dto.name = confMap.get(it)
				}
				else if (it.startsWith('tak.env.url.')) {
					dto.url = confMap.get(it)
				}
				else {
					throw new IllegalArgumentException("Bad TAK-configuration, property name format unknown: " + it)
				}
			}
		}

		// validate entries in map, check that all values are initialized/non-null
		confTakMap.keySet().each {
			def dto = confTakMap.get(it)
			if (dto.id == null || dto.name == null || dto.url == null) {
				throw new IllegalArgumentException("Bad TAK-configuration, a property is null for env nr: " + it
					+ ", id: " + id + ", name: " + name + ", url: " + url)
			}
		}

		// create a lookup map with id as key (now that we have caught all config)
		def takMap = new HashMap()
		confTakMap.values().each {
			takMap.put(it.id, it)
			log.debug("TAK routing map entry: key: " + it.id + ", value: " + it)
		}

		takMap
	}



// BEGIN: PUBLIC METHODS
	List<TakRoutingEntryDTO> getTakRoutingEntriesList() {
		new ArrayList(takRoutingMap.values())
	}

	List<String> getLogicalAddressByServiceDomainNS(String takId, String serviceDomainNS) {
		TakCacheServices tak = takCacheMap.get(takId)
		List<VirtualiseringDTO> virtualiseringar = tak.getAllVirtualiseringar()
		List logicalAddressForDomain = new ArrayList()
		virtualiseringar.each {
			if (it.tjanstekontrakt.contains(serviceDomainNS)) {
				logicalAddressForDomain.add(it.reciverId)
			}
		}
		logicalAddressForDomain
	}

	List<TjanstekontraktDTO> getAllTjanstekontrakt(String takId) {
		TakCacheServices tak = takCacheMap.get(takId)
		List<TjanstekontraktDTO> list = tak.getAllTjanstekontrakt()
		list
	}
	 
	List<TjanstekontraktDTO> getTjanstekontraktByHsaId(String takId, String hsaId) {
		TakCacheServices tak = takCacheMap.get(takId)
				
		// find all tjanstekontraktNamnrymder for hsaId
		Set<String> tjanstekontraktNamnrymder = new HashSet<String>()
		List<se.skltp.ap.service.tak.m.TjanstekomponentDTO> tks = tak.getAllTjanstekomponenter()
		tks.each { tk ->
			if (tk.hsaId.toLowerCase().equals(hsaId.toLowerCase())) {
				List<AnropsAdressInfoType> aaits = tk.getAnropsAdressInfo()
				aaits.each { aait ->
					List<VagvalsInfoType> vits = aait.getVagvalsInfo()
					vits.each { vit ->
						tjanstekontraktNamnrymder.add(vit.getTjanstekontraktNamnrymd()) 
					}
				}
			}
		}
		
		// build response
		List<TjanstekontraktDTO> contracts = new ArrayList<TjanstekontraktDTO>()
		tjanstekontraktNamnrymder.each {			
			// Note: tjanstekontraktNamnrymd looks like:
			//   urn:riv:crm:scheduling:GetAvailableDatesResponder:1
			String beskrivning = ""
			String majorVersion = TjanstekontraktUtil.extractMajorVersionFromTakNamnrymd(it)
			String minorVersion
			String namnrymd = it
			TjanstekontraktDTO dto = new TjanstekontraktDTO(beskrivning, majorVersion, minorVersion, namnrymd)
			contracts.add(dto) 
		}
		
		contracts
	}

	List<TjanstekomponentDTO> freeTextSearchTjansteKomponent(String takId, String queryString, int limit) {
		List<TakCacheServices> taks
		log.debug "freeTextSearchTjansteKomponent takId: $takId"
		if (takId != null) {
			taks = [takCacheMap.get(takId)] as List<TakCacheServices>
		} else {
			log.debug "no takId, will search all TAKs"
			taks = new ArrayList<>(takCacheMap.values())
		}
		//TakCacheServices tak = takCacheMap.get(takId)
		// no search optimizations, just do a linear search for now ...
		// NOTE: be careful to store/cache the TAK data locally - TAK data is locally
		// owned by the cache which must be refreshed at repeated intervals
		List<TjanstekomponentDTO> searchResult = new ArrayList<TjanstekomponentDTO>();
		taks.each {
			println "searching for $queryString in ${it.getId()}"
			for (TjanstekomponentInfoType tki : it.getAllTjanstekomponenter()) {
				if (tki.hsaId.toLowerCase().contains(queryString.toLowerCase())
						|| tki.beskrivning?.toLowerCase()?.contains(queryString.toLowerCase())) {
					def beskrivning = tki.beskrivning
					def organisation = null
					if (beskrivning.indexOf(' - ') > -1) {
						organisation = beskrivning.substring(0, beskrivning.indexOf(' - '))
						beskrivning = beskrivning.substring(beskrivning.indexOf(' - ') + 3)
					}
					searchResult.add(new TjanstekomponentDTO(hsaId: tki.hsaId, beskrivning: beskrivning, organisation: organisation))
					if (searchResult.size() == limit) {
						break;
					}
				}
			}
		}
		searchResult
	}
	
	// serviceContractNamespace (from RIV TA), example: crm:scheduling:GetSubjectOfCareSchedule
	List<LogiskAdressDTO> getForServiceContract(String serviceComponentId, String environmentId, String rivTaNamnrymd, int majorVersion) {
		TakCacheServices tak = takCacheMap.get(environmentId)
		
		List<LogiskAdressDTO> logiskAdressList = new ArrayList<LogiskAdressDTO>()		
		List<TjanstekomponentDTO> tks = tak.getAllTjanstekomponenter()
		tks.each { tk ->
			if (tk.hsaId.equals(serviceComponentId)) {
				List<AnropsAdressInfoType> aaits = tk.getAnropsAdressInfo()
				aaits.each { aait ->
					List<VagvalsInfoType> vits = aait.getVagvalsInfo()
					vits.each { vit ->
						if (TjanstekontraktUtil.isNamnrymdEqual(rivTaNamnrymd,
								majorVersion.toString(), vit.getTjanstekontraktNamnrymd())) {
							logiskAdressList.add(new LogiskAdressDTO(
								hsaId: vit.getLogiskAdressHsaId(),namn: vit.getLogiskAdressBeskrivning()))
						}
					}
				}
			}
		}
		logiskAdressList
	}
	
    AdressDTO getAdressByTjanstekontraktAndHsaId(String environmentId, String rivTaNamnrymd, String majorVersion, String hsaId) {		
		TakCacheServices tak = takCacheMap.get(environmentId)
		List<TjanstekomponentDTO> tks = tak.getAllTjanstekomponenter()
		AdressDTO result = new AdressDTO(url: "Ej konfigurerad", rivProfil: 'RIVTABP21')
		boolean found = false		
		tks.each { tk ->
			if (tk.hsaId.equals(hsaId)) {
				List<AnropsAdressInfoType> aaits = tk.getAnropsAdressInfo()
				aaits.each { aait ->
					List<VagvalsInfoType> vits = aait.getVagvalsInfo()
					vits.each { vit ->
						if (TjanstekontraktUtil.isNamnrymdEqual(rivTaNamnrymd,
								majorVersion.toString(), vit.getTjanstekontraktNamnrymd())) {
							result = new AdressDTO(url: aait.adress, rivProfil: aait.rivtaProfilNamn)
						}
					}
				}
			}
		}
		result
    }

    def getProducentanslutningarForDoman(String takId, String serviceProducerHSAId, String serviceDomainNS) {
        TakCacheServices tak = takCacheMap.get(takId)
        def tjanstekontraktMap = [:] //temporary object to fill with TAK data

        tak.getAllTjanstekontrakt().findAll {
            log.debug "${it.namnrymd} ${it.majorVersion} ${it.beskrivning}"
            it.namnrymd.contains(serviceDomainNS)
        }.each {
            tjanstekontraktMap[it.namnrymd] = [:]
        }

        tak.getAllTjanstekomponenter().find {
            it.hsaId.equals(serviceProducerHSAId)
        }?.getAnropsAdressInfo()?.each { anropsAdressInfo ->
            anropsAdressInfo.getVagvalsInfo().findAll {
                it.tjanstekontraktNamnrymd.contains(serviceDomainNS)
            }.each {
                tjanstekontraktMap[it.tjanstekontraktNamnrymd][it.logiskAdressHsaId] = [enabled: true, possible: true]
                tjanstekontraktMap[it.tjanstekontraktNamnrymd]['adress'] = [url: anropsAdressInfo.adress, rivtaProfil: anropsAdressInfo.rivtaProfilNamn]
            }
        }

        def anslutningStatuses = tjanstekontraktMap.keySet().findResults { takTjanstekontrakt ->
            def tjanstekontrakt = getRivTaTjanstekontrakt(takTjanstekontrakt as String, serviceDomainNS)
            if (tjanstekontrakt != null) {
                def url = null, rivtaProfil = null
                if (tjanstekontraktMap[takTjanstekontrakt]['adress']) {
                    url = tjanstekontraktMap[takTjanstekontrakt]['adress']['url']
                    rivtaProfil = tjanstekontraktMap[takTjanstekontrakt]['adress']['rivtaProfil']
                }
                AnslutningStatusDTO kas = new AnslutningStatusDTO(
                        tjanstekontraktNamn: tjanstekontrakt.namn,
                        tjanstekontraktNamnrymd: tjanstekontrakt.namnrymd,
                        tjanstekontraktMajorVersion: tjanstekontrakt.majorVersion,
                        tjanstekontraktMinorVersion: tjanstekontrakt.minorVersion,
                        installeratIDriftmiljo: true,
                        producentUrl: url,
                        producentRivtaProfil: rivtaProfil
                )
                kas.logiskAdressStatuses = tjanstekontraktMap[takTjanstekontrakt].keySet().findResults { logiskAdressHsaId ->
                    if (logiskAdressHsaId == 'adress') return null
                    LogiskAdressStatusDTO logiskAdressStatusDTO =
                            new LogiskAdressStatusDTO(
                                    hsaId: logiskAdressHsaId,
                                    namn: getNameForHsaIdInternal(takId, logiskAdressHsaId)
                            )
                    //noinspection GroovyDoubleNegation
                    logiskAdressStatusDTO.possible = !!tjanstekontraktMap[takTjanstekontrakt][logiskAdressHsaId]['possible']
                    //noinspection GroovyDoubleNegation
                    logiskAdressStatusDTO.enabled = !!tjanstekontraktMap[takTjanstekontrakt][logiskAdressHsaId]['enabled']
                    logiskAdressStatusDTO
                }
                kas
            } else {
                null
            }
        }

        //add kontrakts possibly not installed in environment
        rivTaService.getTjansteKontraktForDoman(serviceDomainNS).each { rivtjanstekontrakt ->
            if (!anslutningStatuses.find { (it.tjanstekontraktNamnrymd == rivtjanstekontrakt.namnrymd
                && it.tjanstekontraktMajorVersion == rivtjanstekontrakt.majorVersion
                && it.tjanstekontraktMinorVersion == rivtjanstekontrakt.minorVersion)}) {
                anslutningStatuses << new AnslutningStatusDTO(
                        tjanstekontraktNamn: rivtjanstekontrakt.namn,
                        tjanstekontraktNamnrymd: rivtjanstekontrakt.namnrymd,
                        tjanstekontraktMajorVersion: rivtjanstekontrakt.majorVersion,
                        tjanstekontraktMinorVersion: rivtjanstekontrakt.minorVersion,
                        installeratIDriftmiljo: false)
            }
        }

        anslutningStatuses

    }

    def getKonsumentanslutningarForDoman(String takId, String serviceConsumerHSAId, String serviceDomainNS) {
        TakCacheServices tak = takCacheMap.get(takId)
        def tjanstekontraktMap = [:] //temporary object to fill with TAK data
        tak.getAllVirtualiseringar().findAll {
            it.tjanstekontrakt.contains(serviceDomainNS)
        }.each {
            if(!tjanstekontraktMap[it.tjanstekontrakt]) {
                tjanstekontraktMap[it.tjanstekontrakt] = [:]
            }
            tjanstekontraktMap[it.tjanstekontrakt][it.reciverId] = [possible: true]
        }
        tak.getAllAnropsBehorigheter().findAll {
            it.senderId == serviceConsumerHSAId && it.tjanstekontrakt.contains(serviceDomainNS)
        }.each {
            tjanstekontraktMap[it.tjanstekontrakt][it.reciverId].put('active', true)
        }
        //create DTO:s based on our temporary map
        tjanstekontraktMap.keySet().findResults { takTjanstekontrakt ->
            def tjanstekontrakt = getRivTaTjanstekontrakt(takTjanstekontrakt as String, serviceDomainNS)
            if (tjanstekontrakt != null) {
                AnslutningStatusDTO kas = new AnslutningStatusDTO(
                        tjanstekontraktNamn: tjanstekontrakt.namn,
                        tjanstekontraktNamnrymd: tjanstekontrakt.namnrymd,
                        tjanstekontraktMajorVersion: tjanstekontrakt.majorVersion,
                        tjanstekontraktMinorVersion: tjanstekontrakt.minorVersion,
                        installeratIDriftmiljo: true
                )
                kas.logiskAdressStatuses = tjanstekontraktMap[takTjanstekontrakt].keySet().collect { logiskAdressHsaId ->
                    LogiskAdressStatusDTO logiskAdressStatusDTO =
                            new LogiskAdressStatusDTO(
                                    hsaId: logiskAdressHsaId,
                                    namn: getNameForHsaIdInternal(takId, logiskAdressHsaId)
                            )
                    //noinspection GroovyDoubleNegation
                    logiskAdressStatusDTO.possible = !!tjanstekontraktMap[takTjanstekontrakt][logiskAdressHsaId]['possible']
                    //noinspection GroovyDoubleNegation
                    logiskAdressStatusDTO.enabled = !!tjanstekontraktMap[takTjanstekontrakt][logiskAdressHsaId]['active']
                    logiskAdressStatusDTO
                }
                kas
            } else {
                null
            }
        }
    }

    def freeTextSearchLogiskAdresser(String takId, String query, int limit) {
        def lowerCaseQuery = query.toLowerCase()
        def results = []
        TakCacheServices tak = takCacheMap.get(takId)
        for (tjanstekomponentDTO in tak.getAllTjanstekomponenter()) {
            if (tjanstekomponentDTO.hsaId.toLowerCase().contains(lowerCaseQuery)
                    || tjanstekomponentDTO.getBeskrivning().toLowerCase().contains(lowerCaseQuery)) {
                addLogiskAdressIfNotFound(results, tjanstekomponentDTO.hsaId, tjanstekomponentDTO.beskrivning)
            }
            if (results.size() >= limit) {
                break;
            }
            for (anropsbehorighetInfo in tjanstekomponentDTO.anropsbehorighetInfo) {
                if (anropsbehorighetInfo.logiskAdressHsaId.toLowerCase().contains(lowerCaseQuery)
                        || anropsbehorighetInfo.logiskAdressBeskrivning.toLowerCase().contains(lowerCaseQuery)) {
                    addLogiskAdressIfNotFound(results, anropsbehorighetInfo.logiskAdressHsaId, anropsbehorighetInfo.logiskAdressBeskrivning)
                }
                if (results.size() >= limit) {
                    break;
                }
            }
            if (results.size() >= limit) {
                break;
            }
            for (anropsAdressInfo in tjanstekomponentDTO.anropsAdressInfo) {
                for (vagvalsInfo in anropsAdressInfo.vagvalsInfo) {
                    if (vagvalsInfo.logiskAdressHsaId.toLowerCase().contains(lowerCaseQuery)
                        || vagvalsInfo.logiskAdressBeskrivning.toLowerCase().contains(lowerCaseQuery)) {
                        addLogiskAdressIfNotFound(results, vagvalsInfo.logiskAdressHsaId, vagvalsInfo.logiskAdressBeskrivning)
                    }
                    if (results.size() >= limit) {
                        break;
                    }
                }
                if (results.size() >= limit) {
                    break;
                }
            }
            if (results.size() >= limit) {
                break;
            }
        }
        results
    }

    def addLogiskAdressIfNotFound(List<LogiskAdressDTO> dtos, String hsaId, String beskrivning) {
        if (!dtos.find { it.hsaId == hsaId }) {
            dtos.push(new LogiskAdressDTO(hsaId: hsaId, namn: beskrivning))
        }
    }

    String getNameForHsaId(String takId, String hsaId) {
        def name = 'NAMN SAKNAS'
        TakCacheServices tak = takCacheMap.get(takId)
        for (tjanstekomponentDTO in tak.getAllTjanstekomponenter()) {
            if (tjanstekomponentDTO.hsaId == hsaId) {
                name = tjanstekomponentDTO.getBeskrivning()
                break
            } else {
                for (anropsbehorighetInfo in tjanstekomponentDTO.anropsbehorighetInfo) {
                    if (anropsbehorighetInfo.logiskAdressHsaId == hsaId) {
                        name = anropsbehorighetInfo.logiskAdressBeskrivning
                        break
                    }
                }
                for (anropsAdressInfo in tjanstekomponentDTO.anropsAdressInfo) {
                    def found = false
                    for (vagvalsInfo in anropsAdressInfo.vagvalsInfo) {
                        if (vagvalsInfo.logiskAdressHsaId == hsaId) {
                            name = vagvalsInfo.logiskAdressBeskrivning
                            found = true
                            break
                        }
                    }
                    if (found) {
                        break
                    }
                }
            }
        }
        name
    }

    private String getNameForHsaIdInternal(String takId, String hsaId) {
        def name = hsaService.getNameForHsaId(hsaId)
        if (name.contains('SAKNAS')) {
            name = getNameForHsaId(takId, hsaId)
        }
        name
    }

    private TjansteKontraktDTO getRivTaTjanstekontrakt(String takTjanstekontrakt, String serviceDomainNS) {
        def rivTjanstekontrakt = rivTaService.getTjansteKontraktForDoman(serviceDomainNS).find {
            return TjanstekontraktUtil.isNamnrymdEqual(it.namnrymd, it.majorVersion as String, takTjanstekontrakt)
        }
        if (!rivTjanstekontrakt) {
            log.warn "could not find tjanstekontrakt (RIV) for $takTjanstekontrakt in $serviceDomainNS"
            log.warn "tjanstekontrakt (RIV) i $serviceDomainNS:"
            rivTaService.getTjansteKontraktForDoman(serviceDomainNS).each {
                log.warn "$it"
            }
        }
        rivTjanstekontrakt
    }

// END: PUBLIC METHODS


}
