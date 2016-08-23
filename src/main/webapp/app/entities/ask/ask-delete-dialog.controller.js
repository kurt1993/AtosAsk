(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .controller('AskDeleteController',AskDeleteController);

    AskDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ask'];

    function AskDeleteController($uibModalInstance, entity, Ask) {
        var vm = this;

        vm.ask = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Ask.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
