(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('NoteCategoryController', NoteCategoryController);

    NoteCategoryController.$inject = ['$scope', '$state', 'NoteCategory'];

    function NoteCategoryController ($scope, $state, NoteCategory) {
        var vm = this;
        
        vm.noteCategories = [];

        loadAll();

        function loadAll() {
            NoteCategory.query(function(result) {
                vm.noteCategories = result;
            });
        }
    }
})();
