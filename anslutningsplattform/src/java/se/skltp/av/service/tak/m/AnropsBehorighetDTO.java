package se.skltp.av.service.tak.m;

import java.util.Date;


public class AnropsBehorighetDTO implements CacheableDTO {

	private final String id; //PK
	private final Date fromTidpunkt;
	private final Date tomTidpunkt;
	private final String reciverId;
	private final String senderId;
	private final String tjanstekontrakt;

	public AnropsBehorighetDTO(final String id, final Date fromTidpunkt,
			final Date tomTidpunkt, final String reciverId, final String senderId, final String tjanstekontrakt) {
		this.id = id;
		this.fromTidpunkt = fromTidpunkt;
		this.tomTidpunkt = tomTidpunkt;
		this.reciverId = reciverId;
		this.senderId = senderId;
		this.tjanstekontrakt = tjanstekontrakt;
	}
	
	public String getId() {
		return this.id;
	}
	
	public Date getFromTidpunkt() {
		return this.fromTidpunkt;
	}
	
	public Date getTomTidpunkt() {
		return this.tomTidpunkt;
	}
	
	public String getReciverId() {
		return this.reciverId;
	}
	
	public String getSenderId() {
		return this.senderId;
	}
	
	public String getTjanstekontrakt() {
		return this.tjanstekontrakt;
	}

	@Override
	public String toString() {
		return "AnropsBehorighetDTO [id=" + id + ", fromTidpunkt="
				+ fromTidpunkt + ", tomTidpunkt=" + tomTidpunkt
				+ ", reciverId=" + reciverId + ", senderId=" + senderId
				+ ", tjanstekontrakt=" + tjanstekontrakt + "]";
	}
}
