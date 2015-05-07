package se.skltp.ap.service

import grails.transaction.Transactional
import se.skltp.ap.services.dto.domain.NatDTO

import javax.annotation.PostConstruct

@Transactional(readOnly = true)
class NatService {

    boolean lazyInit = false

    def grailsApplication

    List<NatDTO> natDTOs

    @PostConstruct
    void init() {
        log.debug 'init nat from config'
        natDTOs = grailsApplication.config.nat.collect {id, namn ->
            new NatDTO(
                    id: id,
                    namn: namn
            )
        }
    }

    List<NatDTO> getConfiguredNatDTOs() {
        return natDTOs
    }
}
