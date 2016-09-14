(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .controller('QuestionsViewController', QuestionsViewController);

    QuestionsViewController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Questions'];

    function QuestionsViewController($scope, $rootScope, $stateParams, previousState, entity, Questions) {
        var vm = this;

        vm.questions = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('askAtosApp:questionsUpdate', function(event, result) {
            vm.questions = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

