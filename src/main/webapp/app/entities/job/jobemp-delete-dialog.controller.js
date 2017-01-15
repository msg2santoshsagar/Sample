(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('JobEmpDeleteController',JobEmpDeleteController);

    JobEmpDeleteController.$inject = ['$uibModalInstance', 'entity', 'Job'];

    function JobEmpDeleteController($uibModalInstance, entity, Job) {
        var vm = this;

        vm.job = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Job.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
