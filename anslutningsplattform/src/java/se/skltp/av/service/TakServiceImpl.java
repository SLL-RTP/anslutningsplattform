package se.skltp.av.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.skltp.tak.vagvalsinfo.wsdl.v2.SokVagvalsInfoInterface;
import se.skltp.tak.vagvalsinfo.wsdl.v2.SokVagvalsServiceSoap11LitDocService;
import se.skltp.tak.vagvalsinfo.wsdl.v2.TjanstekontraktInfoType;
import se.skltp.tak.vagvalsinfo.wsdl.v2.VirtualiseringsInfoType;

public class TakServiceImpl {

	private Logger log = LoggerFactory.getLogger(TakServiceImpl.class);
	private SokVagvalsInfoInterface webServiceProxy;

	public TakServiceImpl(String url) {

		log.info("Creates a TakService based on the TAK-URL: {}", url);
		try {
			webServiceProxy = new SokVagvalsServiceSoap11LitDocService(new URL(url)).getSokVagvalsSoap11LitDocPort();
		} catch (MalformedURLException e) {
			log.error("Failed to create proxy to TAK web service: " + url, e);
			throw new RuntimeException(e);
		}
	}

	public List<Tjanstekontrakt> getAllTjanstekontrakt() {
		List<TjanstekontraktInfoType> response = webServiceProxy.hamtaAllaTjanstekontrakt(null).getTjanstekontraktInfo();

		List<Tjanstekontrakt> result = new ArrayList<>();

		if (response == null) return result;

		for (final TjanstekontraktInfoType tk: response) {
			result.add(new Tjanstekontrakt() {
				public String getNamnrymd()    {return tk.getNamnrymd();}
				public String getVersion()     {return tk.getMajorVersion();}
				public String getBeskrivning() {return tk.getBeskrivning();}
			});
		}
		return result;
	}
	
	public List<Tjanstekontrakt> getAllTjanstekontrakt(String tjansteDoman) {

		List<Tjanstekontrakt> result = new ArrayList<>();

		for (final Tjanstekontrakt tk: getAllTjanstekontrakt()) {
			if(tk.getNamnrymd().contains(tjansteDoman)){
				result.add(tk);
			}
		}
		return result;
	}

	public List<ProducentAnslutning> getAllProducentAnslutningar(String id) {

		List<ProducentAnslutning> result = new ArrayList<>();

		// Return empty result of no id is specified
		if (id == null || id.length() == 0) return result;

		List<VirtualiseringsInfoType> response = webServiceProxy.hamtaAllaVirtualiseringar(null).getVirtualiseringsInfo();

		// Return empty result of no result is found
		if (response == null) return result;

		for (final VirtualiseringsInfoType pa: response) {
			// FIXME: Fix for the incorrect data model in TAK. Accept all records that has a receiverId that starts with the given producer id
			// FIXME: Problem with XMLGregorianCalender and JSON rendering, for now using toString()
			if (pa.getReceiverId().toUpperCase().startsWith(id)) {
				result.add(new ProducentAnslutning() {
					public String getUrl() {return pa.getAdress();}
					public String getRivProfil() {return pa.getRivProfil();}
					public String getTjansteKontrakt() {return pa.getTjansteKontrakt();}
					public String getFromTidpunkt() {return pa.getFromTidpunkt().toString();}
					public String getTomTidpunkt() {return pa.getTomTidpunkt().toString();}
				});
			}
		}
		return result;
	}
}
