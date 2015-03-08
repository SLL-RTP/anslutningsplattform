package se.skltp.av.service

import se.skltp.av.services.dto.AdressDTO

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import se.skltp.av.service.tak.TakCacheServices
import se.skltp.av.service.tak.TakCacheServicesImpl
import se.skltp.av.service.tak.TakSyncCacheCallback
import se.skltp.av.service.tak.TakCache
import se.skltp.av.service.tak.m.AnropsBehorighetDTO
import se.skltp.av.service.tak.m.TjanstekontraktDTO
import se.skltp.av.service.tak.m.VirtualiseringDTO
import se.skltp.av.service.tak.m.PersistenceEntity
import se.skltp.av.service.tak.persistence.TakCacheFilePersistenceImpl
import se.skltp.av.service.tak.persistence.TakCachePersistenceServices

import se.skltp.av.services.dto.LogiskAdressDTO;
import se.skltp.av.services.dto.TakRoutingEntryDTO
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TakService {

	def grailsApplication

	def takRoutingMap
	def takCacheMap

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
					log.error("Failed to sync: " + endpoint);
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
		// create lookup-map for service contracts
		Map<String, TjanstekontraktDTO> contractsMap = new HashMap()
		getAllTjanstekontrakt(takId).each {
			contractsMap.put(it.namnrymd, it)
		}
		// find virtualizations for hsaId
		List<TjanstekontraktDTO> contracts = new ArrayList<TjanstekontraktDTO>()
		List<VirtualiseringDTO> virtualiseringar = tak.getAllVirtualiseringar()
		virtualiseringar.each {
			if (it.reciverId.equalsIgnoreCase(hsaId)) {
				// lookup contract
				TjanstekontraktDTO contract = contractsMap.get(it.tjanstekontrakt)
				if (contract) {
					contracts.add(contract)
				}
			}
		}
		contracts
	}

    AdressDTO getAdressByTjanstekontractAndHsaId(String takId, String namnrymd, String majorVersion, String minorVersion, String hsaId) {
        new AdressDTO(
                url: 'http://dummy.com/',
                rivProfil: 'RIVTABP21'
        )
    }

// END: PUBLIC METHODS


}
