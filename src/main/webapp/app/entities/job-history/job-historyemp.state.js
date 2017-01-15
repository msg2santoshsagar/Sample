(function() {
    'use strict';

    angular
        .module('sampleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('job-historyemp', {
            parent: 'entity',
            url: '/job-historyemp',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sampleApp.jobHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-history/job-historiesemp.html',
                    controller: 'JobHistoryEmpController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('jobHistory');
                    $translatePartialLoader.addPart('language');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('job-historyemp-detail', {
            parent: 'entity',
            url: '/job-historyemp/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sampleApp.jobHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-history/job-historyemp-detail.html',
                    controller: 'JobHistoryEmpDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('jobHistory');
                    $translatePartialLoader.addPart('language');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'JobHistory', function($stateParams, JobHistory) {
                    return JobHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'job-historyemp',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('job-historyemp-detail.edit', {
            parent: 'job-historyemp-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-history/job-historyemp-dialog.html',
                    controller: 'JobHistoryEmpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobHistory', function(JobHistory) {
                            return JobHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-historyemp.new', {
            parent: 'job-historyemp',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-history/job-historyemp-dialog.html',
                    controller: 'JobHistoryEmpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startDate: null,
                                endDate: null,
                                language: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('job-historyemp', null, { reload: 'job-historyemp' });
                }, function() {
                    $state.go('job-historyemp');
                });
            }]
        })
        .state('job-historyemp.edit', {
            parent: 'job-historyemp',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-history/job-historyemp-dialog.html',
                    controller: 'JobHistoryEmpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobHistory', function(JobHistory) {
                            return JobHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-historyemp', null, { reload: 'job-historyemp' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-historyemp.delete', {
            parent: 'job-historyemp',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-history/job-historyemp-delete-dialog.html',
                    controller: 'JobHistoryEmpDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['JobHistory', function(JobHistory) {
                            return JobHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-historyemp', null, { reload: 'job-historyemp' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
