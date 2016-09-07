(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .controller('TagsDeleteController',TagsDeleteController);

    TagsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tags'];

    function TagsDeleteController($uibModalInstance, entity, Tags) {
        var vm = this;

        vm.tags = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tags.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
