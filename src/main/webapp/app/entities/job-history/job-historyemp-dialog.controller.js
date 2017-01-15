(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('JobHistoryEmpDialogController', JobHistoryEmpDialogController);

    JobHistoryEmpDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'JobHistory', 'Employee', 'Job'];

    function JobHistoryEmpDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, JobHistory, Employee, Job) {
        var vm = this;

        vm.jobHistory = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.employees = Employee.query();
        vm.jobs = Job.query({filter: 'jobhistory-is-null'});
        $q.all([vm.jobHistory.$promise, vm.jobs.$promise]).then(function() {
            if (!vm.jobHistory.job || !vm.jobHistory.job.id) {
                return $q.reject();
            }
            return Job.get({id : vm.jobHistory.job.id}).$promise;
        }).then(function(job) {
            vm.jobs.push(job);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.jobHistory.id !== null) {
                JobHistory.update(vm.jobHistory, onSaveSuccess, onSaveError);
            } else {
                JobHistory.save(vm.jobHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sampleApp:jobHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
