(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .controller('AskDialogController', AskDialogController);

    AskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ask'];

    function AskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Ask) {
        var vm = this;

        vm.ask = entity;
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
            if (vm.ask.id !== null) {
                Ask.update(vm.ask, onSaveSuccess, onSaveError);
            } else {
                Ask.save(vm.ask, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('askAtosApp:askUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
