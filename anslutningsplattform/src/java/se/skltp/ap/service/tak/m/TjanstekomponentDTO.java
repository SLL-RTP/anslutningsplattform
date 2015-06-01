package se.skltp.ap.service.tak.m;

import se.skltp.tak.vagvalsinfo.wsdl.v2.TjanstekomponentInfoType;

/**
 * Just wrap the TAK interface type to make it cachable.
 * 
 * @author hakan
 */
public class TjanstekomponentDTO extends TjanstekomponentInfoType implements
		CacheableDTO {

	public TjanstekomponentDTO(TjanstekomponentInfoType tkType) {
		super();
		this.hsaId = tkType.getHsaId();
		this.beskrivning = tkType.getBeskrivning();
		this.anropsAdressInfo = tkType.getAnropsAdressInfo();
		this.anropsbehorighetInfo = tkType.getAnropsbehorighetInfo();
	}

	@Override
	public String getId() {
		return hsaId;
	}

}
