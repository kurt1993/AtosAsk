(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tags', {
            parent: 'entity',
            url: '/tags',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'askAtosApp.tags.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tags/tags.html',
                    controller: 'TagsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tags');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tags-detail', {
            parent: 'entity',
            url: '/tags/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'askAtosApp.tags.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tags/tags-detail.html',
                    controller: 'TagsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tags');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tags', function($stateParams, Tags) {
                    return Tags.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tags',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tags-detail.edit', {
               parent: 'tags-detail',
               url: '/detail/edit',
               data: {
                   authorities: ['ROLE_USER']
               },
               onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                   $uibModal.open({
                       templateUrl: 'app/entities/tags/tags-dialog.html',
                       controller: 'TagsDialogController',
                       controllerAs: 'vm',
                       backdrop: 'static',
                       size: 'lg',
                       resolve: {
                           entity: ['Tags', function(Tags) {
                               return Tags.get({id : $stateParams.id}).$promise;
                           }]
                       }
                   }).result.then(function() {
                       $state.go('^', {}, { reload: false });
                   }, function() {
                       $state.go('^');
                   });
               }]
           })
        .state('tags.new', {
            parent: 'tags',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tags/tags-dialog.html',
                    controller: 'TagsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libele: null,
                                description: null,
                                domaine: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tags', null, { reload: true });
                }, function() {
                    $state.go('tags');
                });
            }]
        })
        .state('tags.edit', {
            parent: 'tags',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tags/tags-dialog.html',
                    controller: 'TagsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tags', function(Tags) {
                            return Tags.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tags', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tags.delete', {
            parent: 'tags',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tags/tags-delete-dialog.html',
                    controller: 'TagsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tags', function(Tags) {
                            return Tags.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tags', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
