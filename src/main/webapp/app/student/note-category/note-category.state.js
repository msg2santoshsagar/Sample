(function() {
    'use strict';

    angular
        .module('sampleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('note-category', {
            parent: 'student',
            url: '/note-category',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sampleApp.noteCategory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/student/note-category/note-categories.html',
                    controller: 'NoteCategoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('noteCategory');
                    $translatePartialLoader.addPart('language');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('note-category-detail', {
            parent: 'student',
            url: '/note-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sampleApp.noteCategory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/student/note-category/note-category-detail.html',
                    controller: 'NoteCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('noteCategory');
                    $translatePartialLoader.addPart('language');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'NoteCategory', function($stateParams, NoteCategory) {
                    return NoteCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'note-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('note-category-detail.edit', {
            parent: 'note-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/student/note-category/note-category-dialog.html',
                    controller: 'NoteCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NoteCategory', function(NoteCategory) {
                            return NoteCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('note-category.new', {
            parent: 'note-category',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/student/note-category/note-category-dialog.html',
                    controller: 'NoteCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                category: null,
                                language: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('note-category', null, { reload: 'note-category' });
                }, function() {
                    $state.go('note-category');
                });
            }]
        })
        .state('note-category.edit', {
            parent: 'note-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/student/note-category/note-category-dialog.html',
                    controller: 'NoteCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NoteCategory', function(NoteCategory) {
                            return NoteCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('note-category', null, { reload: 'note-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('note-category.delete', {
            parent: 'note-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/student/note-category/note-category-delete-dialog.html',
                    controller: 'NoteCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NoteCategory', function(NoteCategory) {
                            return NoteCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('note-category', null, { reload: 'note-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
