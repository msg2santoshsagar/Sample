(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('NotesDialogController', NotesDialogController);

    NotesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Notes', 'NoteCategory'];

    function NotesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Notes, NoteCategory) {
        var vm = this;

        vm.notes = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.notecategories = NoteCategory.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.notes.id !== null) {
                Notes.update(vm.notes, onSaveSuccess, onSaveError);
            } else {
                Notes.save(vm.notes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sampleApp:notesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
