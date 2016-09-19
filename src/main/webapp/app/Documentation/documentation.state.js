/**
 * Created by a630797 on 19/09/2016.
 */
(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('documentation', {
            abstract: true,
            parent: 'app'
        });
    }
})();
