package se.skltp.ap.service.tak;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.skltp.ap.service.tak.m.AnropsBehorighetDTO;
import se.skltp.ap.service.tak.m.TjanstekomponentDTO;
import se.skltp.ap.service.tak.m.TjanstekontraktDTO;
import se.skltp.ap.service.tak.m.VirtualiseringDTO;
import se.skltp.tak.vagvalsinfo.wsdl.v2.TjanstekomponentInfoType;

public class TakCacheServicesImpl implements TakCacheServices {
	private static final Logger log = LoggerFactory.getLogger(TakCacheServicesImpl.class);
	
	private final String endpoint;
	private final String id;
	
	public TakCacheServicesImpl(final String endpoint, final String id) {
		this.endpoint = endpoint;
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getEndpoint() {
		return this.endpoint;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Date lastSynched() {
		return TakCache.lastSynched(this.endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<AnropsBehorighetDTO> getAllAnropsBehorigheter() {
		return TakCache.getAnropsBehorighet(this.endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<TjanstekontraktDTO> getAllTjanstekontrakt() {
		return TakCache.getTjanstekontrakt(this.endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<VirtualiseringDTO> getAllVirtualiseringar() {
		return TakCache.getVirtualisering(this.endpoint);
	}

	@Override
	public List<TjanstekomponentDTO> getAllTjanstekomponenter() {
		return TakCache.getTjanstekomponent(this.endpoint);
	}	

}
