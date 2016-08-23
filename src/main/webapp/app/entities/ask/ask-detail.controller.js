(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .controller('AskDetailController', AskDetailController);

    AskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Ask'];

    function AskDetailController($scope, $rootScope, $stateParams, previousState, entity, Ask) {
        var vm = this;

        vm.ask = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('askAtosApp:askUpdate', function(event, result) {
            vm.ask = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
