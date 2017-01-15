(function() {
    'use strict';

    angular
        .module('sampleApp')
        .controller('JobHistoryEmpController', JobHistoryEmpController);

    JobHistoryEmpController.$inject = ['$scope', '$state', 'JobHistory'];

    function JobHistoryEmpController ($scope, $state, JobHistory) {
        var vm = this;
        
        vm.jobHistories = [];

        loadAll();

        function loadAll() {
            JobHistory.query(function(result) {
                vm.jobHistories = result;
            });
        }
    }
})();
