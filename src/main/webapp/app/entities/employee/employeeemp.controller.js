(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('EmployeeEmpController', EmployeeEmpController);

    EmployeeEmpController.$inject = ['$scope', '$state', 'Employee'];

    function EmployeeEmpController ($scope, $state, Employee) {
        var vm = this;
        
        vm.employees = [];

        loadAll();

        function loadAll() {
            Employee.query(function(result) {
                vm.employees = result;
            });
        }
    }
})();
