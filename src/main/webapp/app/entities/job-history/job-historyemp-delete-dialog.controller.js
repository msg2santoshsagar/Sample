(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('JobHistoryEmpDeleteController',JobHistoryEmpDeleteController);

    JobHistoryEmpDeleteController.$inject = ['$uibModalInstance', 'entity', 'JobHistory'];

    function JobHistoryEmpDeleteController($uibModalInstance, entity, JobHistory) {
        var vm = this;

        vm.jobHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            JobHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
