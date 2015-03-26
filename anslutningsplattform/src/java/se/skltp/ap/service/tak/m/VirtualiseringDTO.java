package se.skltp.ap.service.tak.m;

import java.util.Date;

public class VirtualiseringDTO implements CacheableDTO {
	
	private final String address;
	private final Date fromTidpunkt;
	private final String reciverId;
	private final String rivProfil;
	private final String tjanstekontrakt;
	private final Date tomTidpunkt;
	private final String id; //PK
	
	public VirtualiseringDTO(final String address, final Date fromTidpunkt, final String reciverId,
			final String rivProfil, final String tjanstekontrakt, final Date tomTidpunkt, final String id) {
		this.address = address;
		this.fromTidpunkt = fromTidpunkt;
		this.reciverId = reciverId;
		this.rivProfil = rivProfil;
		this.tjanstekontrakt = tjanstekontrakt;
		this.tomTidpunkt = tomTidpunkt;
		this.id = id;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public Date getFromTidpunkt() {
		return this.fromTidpunkt;
	}
	
	public String getReciverId() {
		return this.reciverId;
	}
	
	public String getRivProfil() {
		return this.rivProfil;
	}
	
	public String getTjanstekontrakt() {
		return this.tjanstekontrakt;
	}
	
	public Date getTomTidpunkt() {
		return this.tomTidpunkt;
	}
	
	public String getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return "VirtualiseringDTO [address=" + address + ", fromTidpunkt="
				+ fromTidpunkt + ", reciverId=" + reciverId + ", rivProfil="
				+ rivProfil + ", tjanstekontrakt=" + tjanstekontrakt
				+ ", tomTidpunkt=" + tomTidpunkt + ", id=" + id + "]";
	}
}
