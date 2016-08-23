(function() {
    'use strict';

    angular
        .module('askAtosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ask', {
            parent: 'entity',
            url: '/ask?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'askAtosApp.ask.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ask/asks.html',
                    controller: 'AskController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ask');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ask-detail', {
            parent: 'entity',
            url: '/ask/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'askAtosApp.ask.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ask/ask-detail.html',
                    controller: 'AskDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ask');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Ask', function($stateParams, Ask) {
                    return Ask.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ask',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ask-detail.edit', {
               parent: 'ask-detail',
               url: '/detail/edit',
               data: {
                   authorities: ['ROLE_USER']
               },
               onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                   $uibModal.open({
                       templateUrl: 'app/entities/ask/ask-dialog.html',
                       controller: 'AskDialogController',
                       controllerAs: 'vm',
                       backdrop: 'static',
                       size: 'lg',
                       resolve: {
                           entity: ['Ask', function(Ask) {
                               return Ask.get({id : $stateParams.id}).$promise;
                           }]
                       }
                   }).result.then(function() {
                       $state.go('^', {}, { reload: false });
                   }, function() {
                       $state.go('^');
                   });
               }]
           })
        .state('ask.new', {
            parent: 'ask',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ask/ask-dialog.html',
                    controller: 'AskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                content: null,
                                tags: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ask', null, { reload: true });
                }, function() {
                    $state.go('ask');
                });
            }]
        })
        .state('ask.edit', {
            parent: 'ask',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ask/ask-dialog.html',
                    controller: 'AskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ask', function(Ask) {
                            return Ask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ask', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ask.delete', {
            parent: 'ask',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ask/ask-delete-dialog.html',
                    controller: 'AskDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ask', function(Ask) {
                            return Ask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ask', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
