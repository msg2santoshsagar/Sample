(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('NotesController', NotesController);

    NotesController.$inject = ['$scope', '$state', 'Notes'];

    function NotesController ($scope, $state, Notes) {
        var vm = this;
        
        vm.notes = [];

        loadAll();

        function loadAll() {
            Notes.query(function(result) {
                vm.notes = result;
            });
        }
    }
})();
