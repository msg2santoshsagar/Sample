(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('EmployeeEmpDetailController', EmployeeEmpDetailController);

    EmployeeEmpDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Employee', 'JobHistory'];

    function EmployeeEmpDetailController($scope, $rootScope, $stateParams, previousState, entity, Employee, JobHistory) {
        var vm = this;

        vm.employee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sampleApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
