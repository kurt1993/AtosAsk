(function() {
    angular.module('askAtosApp')
           .service('Questions', Questions);

    Questions.$inject = ['$resource'];

    function Questions($resource) {
        return $resource('api/questions/:id', {}, {
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
