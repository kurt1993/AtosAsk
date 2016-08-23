(function() {
    'use strict';
    angular
        .module('askAtosApp')
        .factory('Ask', Ask);

    Ask.$inject = ['$resource'];

    function Ask ($resource) {
        var resourceUrl =  'api/asks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
