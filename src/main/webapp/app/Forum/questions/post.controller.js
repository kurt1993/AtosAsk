/**
 * Created by a630797 on 16/09/2016.
 */

(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .controller('PostController', PostController);

    PostController.$inject = [ '$scope', '$stateParams', 'Questions'];

    function PostController ( $scope, $stateParams, Questions) {
        var vm = this;

        vm.save = save;

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
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();


