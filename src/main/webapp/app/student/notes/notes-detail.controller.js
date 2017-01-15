(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('NotesDetailController', NotesDetailController);

    NotesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Notes', 'NoteCategory'];

    function NotesDetailController($scope, $rootScope, $stateParams, previousState, entity, Notes, NoteCategory) {
        var vm = this;

        vm.notes = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sampleApp:notesUpdate', function(event, result) {
            vm.notes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
