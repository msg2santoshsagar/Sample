(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('StudentHomeController', StudentHomeController);

    StudentHomeController.$inject = ['$scope', '$state', 'Student'];

    function StudentHomeController ($scope, $state, Student) {
        var vm = this;
        
        vm.students = [];

        loadAll();

        function loadAll() {
            Student.query(function(result) {
                vm.students = result;
          
            });
        }
    }
})();
