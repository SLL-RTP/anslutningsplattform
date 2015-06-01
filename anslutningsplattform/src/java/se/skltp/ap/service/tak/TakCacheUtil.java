package se.skltp.ap.service.tak;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import se.skltp.ap.service.tak.m.AnropsBehorighetDTO;
import se.skltp.ap.service.tak.m.TjanstekomponentDTO;
import se.skltp.ap.service.tak.m.TjanstekontraktDTO;
import se.skltp.ap.service.tak.m.VirtualiseringDTO;
import se.skltp.tak.vagvalsinfo.wsdl.v2.AnropsBehorighetsInfoIdType;
import se.skltp.tak.vagvalsinfo.wsdl.v2.AnropsBehorighetsInfoType;
import se.skltp.tak.vagvalsinfo.wsdl.v2.TjanstekomponentInfoType;
import se.skltp.tak.vagvalsinfo.wsdl.v2.TjanstekontraktInfoType;
import se.skltp.tak.vagvalsinfo.wsdl.v2.VirtualiseringsInfoIdType;
import se.skltp.tak.vagvalsinfo.wsdl.v2.VirtualiseringsInfoType;

/**
 * Utility class to ease transformation.
 */
public final class TakCacheUtil {
	
	/**
	 * Equals function with null checks.
	 * @param dateOne
	 * @param dateTwo
	 * @return
	 */
	public static boolean equals(final Date dateOne, final Date dateTwo) {
		if(dateOne == null && dateTwo == null) {
			return true;
		}
		if(dateOne != null && dateTwo != null) {
			if(dateOne.getTime() == dateTwo.getTime()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Transformation between JAXB and DTO
	 * @param type
	 * @return
	 */
	public static VirtualiseringDTO map(final VirtualiseringsInfoType type) {
		if(type == null) {
			return null;
		}
		return new VirtualiseringDTO(
				type.getAdress(), 
				toDate(type.getFromTidpunkt()), 
				type.getReceiverId(), 
				type.getRivProfil(), 
				type.getTjansteKontrakt(), 
				toDate(type.getTomTidpunkt()), 
				getId(type.getVirtualiseringsInfoId()));
	}
	
	/**
	 * Transformation between JAXB and DTO
	 * @param type
	 * @return
	 */
	public static AnropsBehorighetDTO map(final AnropsBehorighetsInfoType type) {
		if(type == null) {
			return null;
		}
		return new AnropsBehorighetDTO(
				getId(type.getAnropsBehorighetsInfoId()),
				toDate(type.getFromTidpunkt()), 
				toDate(type.getTomTidpunkt()), 
				type.getReceiverId(), 
				type.getSenderId(), 
				type.getTjansteKontrakt());
	}
	
	/**
	 * Transformation between JAXB and DTO
	 * @param type
	 * @return
	 */
	public static TjanstekontraktDTO map(final TjanstekontraktInfoType type) {
		if(type == null) {
			return null;
		}
		return new TjanstekontraktDTO(type.getBeskrivning(), type.getMajorVersion(),type.getMinorVersion(), type.getNamnrymd());
	}

	/**
	 * Transformation between JAXB and DTO
	 * @param type
	 * @return
	 */
	public static TjanstekomponentDTO map(final TjanstekomponentInfoType type) {
		if(type == null) {
			return null;
		}
		return new TjanstekomponentDTO(type);
	}

	/**
	 * Transformation between java.Date and XMLGregorianCalendar
	 * @param date
	 * @return
	 */
	public static Date toDate(final XMLGregorianCalendar date) {
		try {
			if(date == null) {
				return null;
			}
			return date.toGregorianCalendar().getTime();
		} catch (Exception err) {
			return null;
		}
	}
	
	/**
	 * Util to extract id-value from type
	 * @param type
	 * @return
	 */
	public static String getId(final VirtualiseringsInfoIdType type) {
		if(type == null) {
			return null;
		}
		return type.getValue();
	}
	
	/**
	 * Util to extract id-value from type
	 * @param type
	 * @return
	 */
	public static String getId(final AnropsBehorighetsInfoIdType type) {
		if(type == null) {
			return null;
		}
		return type.getValue();
	}
}
