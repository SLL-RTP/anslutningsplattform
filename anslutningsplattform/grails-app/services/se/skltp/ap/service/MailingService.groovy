package se.skltp.ap.service

import grails.transaction.Transactional

@Transactional(readOnly = true)
class MailingService {
	
	def grailsApplication
	
	def send(String fromAddress, String toAddress, String subjectField, String bodyPlainText) {

        if (!fromAddress) {
            fromAddress = grailsApplication.config.smtp.from.address
        }
		if(!toAddress) {
			toAddress = grailsApplication.config.smtp.to.address
		}

        log.debug("sending email from $fromAddress to $toAddress with subject '$subjectField' and body:\n$bodyPlainText")

		// uses the mail-plugin: http://grails.org/plugin/mail
		try {
			sendMail {
				async true
				to toAddress
				from fromAddress
				subject subjectField
				body bodyPlainText
			}
		} catch (Exception e) {
			log.error("could not send mail", e) //TODO: currently catching exceptions to not mess with the transaction.
		}
	}
}
