(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('JobEmpController', JobEmpController);

    JobEmpController.$inject = ['$scope', '$state', 'Job'];

    function JobEmpController ($scope, $state, Job) {
        var vm = this;
        
        vm.jobs = [];

        loadAll();

        function loadAll() {
            Job.query(function(result) {
                vm.jobs = result;
            });
        }
    }
})();
