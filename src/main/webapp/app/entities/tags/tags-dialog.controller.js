(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .controller('TagsDialogController', TagsDialogController);

    TagsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tags'];

    function TagsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tags) {
        var vm = this;

        vm.tags = entity;
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
            if (vm.tags.id !== null) {
                Tags.update(vm.tags, onSaveSuccess, onSaveError);
            } else {
                Tags.save(vm.tags, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('askAtosApp:tagsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
