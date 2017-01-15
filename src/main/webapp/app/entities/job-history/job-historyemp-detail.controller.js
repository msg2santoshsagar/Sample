(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('JobHistoryEmpDetailController', JobHistoryEmpDetailController);

    JobHistoryEmpDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'JobHistory', 'Employee', 'Job'];

    function JobHistoryEmpDetailController($scope, $rootScope, $stateParams, previousState, entity, JobHistory, Employee, Job) {
        var vm = this;

        vm.jobHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sampleApp:jobHistoryUpdate', function(event, result) {
            vm.jobHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
