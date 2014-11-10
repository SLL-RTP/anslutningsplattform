package se.skltp.av

import grails.converters.JSON

class BestallningController {

    def index() {}

    def list() {
        def someData = ['data': '1', 'data2': 2]

        render someData as JSON
    }
}
