(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('EmployeeEmpDeleteController',EmployeeEmpDeleteController);

    EmployeeEmpDeleteController.$inject = ['$uibModalInstance', 'entity', 'Employee'];

    function EmployeeEmpDeleteController($uibModalInstance, entity, Employee) {
        var vm = this;

        vm.employee = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Employee.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
