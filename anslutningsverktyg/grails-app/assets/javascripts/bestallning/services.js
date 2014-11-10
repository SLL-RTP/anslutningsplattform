'use strict';

app.service('Bestallning', ['$resource', function($resource) {
    var controller = "bestallning";

    return $resource('/anslutningsverktyg/api/bestallning/:id', {id: '@id'}, {
        list: {method: 'GET', isArray: true}
    });
}]);