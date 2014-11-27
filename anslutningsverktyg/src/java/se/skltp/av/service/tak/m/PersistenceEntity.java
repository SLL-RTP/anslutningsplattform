package se.skltp.av.service.tak.m;

import java.util.Date;
import java.util.List;

import se.skltp.tak.vagvalsinfo.wsdl.v2.AnropsBehorighetsInfoType;
import se.skltp.tak.vagvalsinfo.wsdl.v2.TjanstekontraktInfoType;
import se.skltp.tak.vagvalsinfo.wsdl.v2.VirtualiseringsInfoType;

public class PersistenceEntity {
	
	private final String endpoint;
	private final Date synched;
	private final List<VirtualiseringsInfoType> virtualiseringar;
	private final List<TjanstekontraktInfoType> tjanstekontrakt;
	private final List<AnropsBehorighetsInfoType> anropsbehorighet;
	
	public PersistenceEntity(String endpoint, Date synched,
			List<VirtualiseringsInfoType> virtualiseringar,
			List<TjanstekontraktInfoType> tjanstekontrakt,
			List<AnropsBehorighetsInfoType> anropsbehorighet) {
		this.endpoint = endpoint;
		this.synched = synched;
		this.virtualiseringar = virtualiseringar;
		this.tjanstekontrakt = tjanstekontrakt;
		this.anropsbehorighet = anropsbehorighet;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public Date getSynched() {
		return synched;
	}

	public List<VirtualiseringsInfoType> getVirtualiseringar() {
		return virtualiseringar;
	}

	public List<TjanstekontraktInfoType> getTjanstekontrakt() {
		return tjanstekontrakt;
	}

	public List<AnropsBehorighetsInfoType> getAnropsbehorighet() {
		return anropsbehorighet;
	}
	
	
}
