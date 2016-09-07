(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .controller('TagsDetailController', TagsDetailController);

    TagsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tags'];

    function TagsDetailController($scope, $rootScope, $stateParams, previousState, entity, Tags) {
        var vm = this;

        vm.tags = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('askAtosApp:tagsUpdate', function(event, result) {
            vm.tags = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
