(function() {
    'use strict';

    angular
        .module('sampleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('employeeemp', {
            parent: 'entity',
            url: '/employeeemp',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sampleApp.employee.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee/employeesemp.html',
                    controller: 'EmployeeEmpController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employee');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('employeeemp-detail', {
            parent: 'entity',
            url: '/employeeemp/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sampleApp.employee.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee/employeeemp-detail.html',
                    controller: 'EmployeeEmpDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employee');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Employee', function($stateParams, Employee) {
                    return Employee.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'employeeemp',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('employeeemp-detail.edit', {
            parent: 'employeeemp-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee/employeeemp-dialog.html',
                    controller: 'EmployeeEmpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Employee', function(Employee) {
                            return Employee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employeeemp.new', {
            parent: 'employeeemp',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee/employeeemp-dialog.html',
                    controller: 'EmployeeEmpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                email: null,
                                phoneNumber: null,
                                dateOfBirth: null,
                                hireDate: null,
                                salary: null,
                                commissionPct: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('employeeemp', null, { reload: 'employeeemp' });
                }, function() {
                    $state.go('employeeemp');
                });
            }]
        })
        .state('employeeemp.edit', {
            parent: 'employeeemp',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee/employeeemp-dialog.html',
                    controller: 'EmployeeEmpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Employee', function(Employee) {
                            return Employee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('employeeemp', null, { reload: 'employeeemp' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employeeemp.delete', {
            parent: 'employeeemp',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee/employeeemp-delete-dialog.html',
                    controller: 'EmployeeEmpDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Employee', function(Employee) {
                            return Employee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('employeeemp', null, { reload: 'employeeemp' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
