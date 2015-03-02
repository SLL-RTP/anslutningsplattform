package se.skltp.av.service.tak.m;


public class TjanstekontraktDTO implements CacheableDTO {
	
	private final String beskrivning;
	private final String majorVersion;
	private final String minorVersion;
	private final String namnrymd; //PK
	
	public TjanstekontraktDTO(final String beskrivning, final String majorVersion, 
			final String minorVersion, final String namnrymd) {
		this.beskrivning = beskrivning;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.namnrymd = namnrymd;
	}
	
	public String getBeskrivning() {
		return this.beskrivning;
	}
	
	public String getMajorVersion() {
		return this.majorVersion;
	}
	
	public String getMinorVersion() {
		return this.minorVersion;
	}
	
	public String getNamnrymd() {
		return this.namnrymd;
	}
	
	public String getId() {
		return this.namnrymd;
	}

	@Override
	public String toString() {
		return "TjanstekontraktDTO [beskrivning=" + beskrivning
				+ ", majorVersion=" + majorVersion + ", minorVersion="
				+ minorVersion + ", namnrymd=" + namnrymd + "]";
	}
}
