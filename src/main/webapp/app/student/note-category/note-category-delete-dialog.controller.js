(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('NoteCategoryDeleteController',NoteCategoryDeleteController);

    NoteCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'NoteCategory'];

    function NoteCategoryDeleteController($uibModalInstance, entity, NoteCategory) {
        var vm = this;

        vm.noteCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NoteCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
