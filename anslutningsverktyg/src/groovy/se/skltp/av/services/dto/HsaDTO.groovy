package se.skltp.av.services.dto

import grails.validation.Validateable;
import groovy.transform.ToString;

@Validateable
@ToString
class HsaDTO {
	
	String hsaId
	String dn
	String namn

}
