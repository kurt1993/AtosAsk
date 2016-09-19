/**
 * Created by a630797 on 16/09/2016.
 */

(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .controller('PostController', PostController);

    PostController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'forum', 'Questions'];

    function PostController ($timeout, $scope, $stateParams, $uibModalInstance, forum, Questions) {
        var vm = this;

        vm.questions = forum;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.questions.id !== null) {
                Questions.update(vm.questions, onSaveSuccess, onSaveError);
            } else {
                Questions.save(vm.questions, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('askAtosApp:questionsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();


