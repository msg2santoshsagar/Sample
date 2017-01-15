(function() {
    'use strict';

    angular
        .module('sampleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('importer', {
            parent: 'app',
            url: '/importer',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/importer/importer.html',
                    controller: 'importerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('importer');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
