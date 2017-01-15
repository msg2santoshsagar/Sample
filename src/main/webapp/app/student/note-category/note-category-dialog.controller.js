(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('NoteCategoryDialogController', NoteCategoryDialogController);

    NoteCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NoteCategory', 'Notes'];

    function NoteCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NoteCategory, Notes) {
        var vm = this;

        vm.noteCategory = entity;
        vm.clear = clear;
        vm.save = save;
        vm.notes = Notes.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.noteCategory.id !== null) {
                NoteCategory.update(vm.noteCategory, onSaveSuccess, onSaveError);
            } else {
                NoteCategory.save(vm.noteCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sampleApp:noteCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
