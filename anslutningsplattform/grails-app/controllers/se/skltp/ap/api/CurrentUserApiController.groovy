package se.skltp.ap.api

import se.skltp.ap.services.dto.domain.PersonkontaktDTO
import sun.security.provider.X509Factory

import javax.security.auth.x500.X500Principal
import javax.servlet.http.HttpServletRequest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

/**
 * Created by martin on 28/03/15.
 */
class CurrentUserApiController {

    def subjectDNHsaIdPattern = ~'OID.2.5.4.5=(.*?),'
    def grailsApplication
    def kontaktService

    def get() {
        if (grailsApplication.config.x509.enabled) {
            respond getCurrentUser(request)
        } else {
            log.warn("x509 disabled, using dummy hsaId")
            respond getPersonkontaktDTO("HSA-SUNESUS-PEKT")
        }
    }

    private PersonkontaktDTO getCurrentUser(HttpServletRequest request) {
        String headerName = grailsApplication.config.x509.cert.header
        def header = request.getHeader(headerName)
        def certificate = createX509Certificate(header)
        def subjectDN = certificate.getSubjectX500Principal().getName(X500Principal.RFC1779)
        log.debug("subjectDN: $subjectDN")
        def matcher = subjectDNHsaIdPattern.matcher(subjectDN)
        if (matcher.hasGroup()) {
            String hsaId = matcher[0][1]
            getPersonkontaktDTO(hsaId)
        } else {
            log.error("could not extract hsaId from subjectDN")
            new PersonkontaktDTO()
        }
    }

    def getPersonkontaktDTO(String hsaId) {
        def personkontaktDTO = kontaktService.findPersonkontaktDTOByHsaId(hsaId)
        if (personkontaktDTO == null) {
            personkontaktDTO = new PersonkontaktDTO(
                    hsaId: hsaId
            )
        }
        log.debug("$personkontaktDTO")
        personkontaktDTO
    }

    private X509Certificate createX509Certificate(String x509pem) {
        byte [] decoded = x509pem.replaceAll(X509Factory.BEGIN_CERT, "").replaceAll(X509Factory.END_CERT, "").decodeBase64()
        def stream = new ByteArrayInputStream(decoded)
        def certificateFactory = CertificateFactory.getInstance("X.509")
        (X509Certificate) certificateFactory.generateCertificate(stream)
    }
}
