(function () {
    angular.module('askAtosApp')
        .config(configRoutes);

    configRoutes.$inject = ['$stateProvider'];
    function configRoutes($stateProvider) {
        $stateProvider
            .state('questions', {
            parent: 'forum',
            url: '/questions?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'askAtosApp.ask.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/Forum/questions/questions.html',
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
                    $translatePartialLoader.addPart('questions');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }

        })
            .state('post', {
                parent: 'forum',
                url: '/post',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/Forum/questions/post.html',
                        controller: 'PostController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('post');
                        return $translate.refresh();
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'post',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('questionsview', {
                parent: 'forum',
                url: '/questions/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'askAtosApp.questions.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/Forum/questions/questionsview.html',
                        controller: 'QuestionsViewController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('questions');
                        return $translate.refresh();
                    }],
                    forum: ['$stateParams', 'Questions', function($stateParams, Questions) {
                        return Questions.get({id : $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'questions',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
    }
})();
