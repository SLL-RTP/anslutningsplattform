package se.skltp.av.service

import grails.transaction.Transactional
import se.skltp.av.DriftMiljo
import se.skltp.av.services.dto.DriftMiljoDTO

@Transactional(readOnly = true)
class DriftMiljoService {

    def list() {
        def driftMiljos = DriftMiljo.findAll()
        driftMiljos.collect {
            new DriftMiljoDTO(
                    id: it.id,
                    namn: it.namn
            )
        }
    }
}
