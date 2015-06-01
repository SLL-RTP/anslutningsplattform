package se.skltp.ap.api

import grails.rest.RestfulController

class HsaApiController{
	
	static namespace = 'v1'

	static responseFormats = ['json', 'xml']
	
	static allowedMethods = [index: "GET"]

	def hsaService
	
	//http://localhost:8080/anslutningsverktyg/api/hsa?query=Ericsson&max=2

	def index(final String query, final Integer max) {

		log.debug params

		params.max = Math.min(max ?: 10, 100)
		params.query = query ?: "SE"
		respond hsaService.freeTextSearch(params.query, params.max)
	}
}
