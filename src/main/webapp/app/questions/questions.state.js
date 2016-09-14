(function () {
    angular.module('askAtosApp')
        .config(configRoutes);

    configRoutes.$inject = ['$stateProvider'];
    function configRoutes($stateProvider) {
        $stateProvider
            .state('questions', {
            parent: 'entity',
            url: '/questions?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'askAtosApp.ask.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/questions/questions.html',
                    controller: 'QuestionsController',
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
            .state('questionsview', {
                parent: 'entity',
                url: '/questions/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'askAtosApp.ask.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/questions/questionsview.html',
                        controller: 'QuestionsViewController',
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

            .state('questions.new', {
                parent: 'questions',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/questions/post.html',
                        controller: 'QuestionsController',
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
    }
})();
