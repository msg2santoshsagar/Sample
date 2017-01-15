(function() {
    'use strict';

    angular
        .module('sampleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('student', {
            abstract: true,
            parent: 'app'
        });
    }
})();
