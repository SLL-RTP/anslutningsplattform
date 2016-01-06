package se.skltp.ap.service

import sun.security.provider.X509Factory

import javax.security.auth.x500.X500Principal
import javax.servlet.http.HttpServletRequest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

/**
 * @author Martin Samuelsson
 */
class CertificateExtractionService {

    def subjectDNHsaIdPattern = ~'OID.2.5.4.5=(.*?),'

    def grailsApplication

    public String extractUserHsaId(HttpServletRequest request) {
        if (grailsApplication.config.x509.enabled) {
            String headerName = grailsApplication.config.x509.cert.header
            def header = request.getHeader(headerName)
            def certificate = createX509Certificate(header)
            def subjectDN = certificate.getSubjectX500Principal().getName(X500Principal.RFC1779)
            log.debug("subjectDN: $subjectDN")
            def matcher = subjectDNHsaIdPattern.matcher(subjectDN)
            if (matcher.hasGroup()) {
                String hsaId = matcher[0][1]
                return hsaId
            } else {
                log.error("could not extract hsaId from subjectDN")
                return 'HSAID-NOT-FOUND'
            }
        } else {
            log.warn("'grailsApplication.config.x509.enabled' false or undefined, will not try to extract hsaId")
            return 'HSAID-NOT-FOUND'
        }
    }

    private X509Certificate createX509Certificate(String x509pem) {
        byte [] decoded = x509pem.replaceAll(X509Factory.BEGIN_CERT, "").replaceAll(X509Factory.END_CERT, "").decodeBase64()
        def stream = new ByteArrayInputStream(decoded)
        def certificateFactory = CertificateFactory.getInstance("X.509")
        (X509Certificate) certificateFactory.generateCertificate(stream)
    }

}
