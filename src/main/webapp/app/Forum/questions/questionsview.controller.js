(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .controller('QuestionsViewController', QuestionsViewController);

    QuestionsViewController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'forum', 'Questions'];

    function QuestionsViewController($scope, $rootScope, $stateParams, previousState, forum, Questions) {
        var vm = this;

        vm.questions = forum;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('askAtosApp:questionsUpdate', function(event, result) {
            vm.questions = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

