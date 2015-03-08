package se.skltp.av.services.dto

import groovy.transform.ToString


@ToString(includeNames=true, includeFields = true)
class AnsvarigDTO {
	long id
	String name
    String hsaId
    String role //TODO: maybe create some enum?
	String email
	String phone
}
