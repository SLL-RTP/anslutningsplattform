package se.skltp.ap.services.dto.domain

import grails.validation.Validateable
import groovy.transform.ToString
/**
 * Created by martin on 25/03/15.
 */
@Validateable
@ToString
class BestallningDTO {
    DriftmiljoDTO driftmiljo
    PersonkontaktDTO bestallare
    String bestallareRoll
    ProducentbestallningDTO producentbestallning
    List<KonsumentbestallningDTO> konsumentbestallningar
    List<NatDTO> nat
    String otherInfo

    static constraints = {
        driftmiljo nullable: false
        bestallare nullable: false
        bestallareRoll nullable: true
        producentbestallning nullable: true, validator: { val, obj ->
            if (val == null && (obj.konsumentbestallningar == null || obj.konsumentbestallningar.empty)) {
                return false
            }
            true
        }
        konsumentbestallningar nullable: true
        nat nullable: false
        otherInfo nullable: true, blank: true
    }
}
