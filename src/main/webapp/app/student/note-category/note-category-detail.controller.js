(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('NoteCategoryDetailController', NoteCategoryDetailController);

    NoteCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'NoteCategory', 'Notes'];

    function NoteCategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, NoteCategory, Notes) {
        var vm = this;

        vm.noteCategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sampleApp:noteCategoryUpdate', function(event, result) {
            vm.noteCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
