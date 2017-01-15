(function() {
    'use strict';

    angular
        .module('sampleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('studentHome', {
            parent: 'student',
            url: '/studentHome',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sampleApp.student.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/student/layouts/student-home.html',
                    controller: 'StudentHomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('student');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
            }

})();
