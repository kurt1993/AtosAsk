(function () {
    angular.module('askAtosApp')
        .controller('QuestionsController', QuestionsController);

    QuestionsController.$inject =
        ['$state', 'Questions', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function QuestionsController($state, Questions, ParseLinks, AlertService, pagingParams, paginationConstants) {
        var vm = this;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        loadFirstQuestions();

        function loadFirstQuestions() {
            Questions.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, setQuestions, onError);
        }

        function sort() {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate) {
                result.push('id');
            }
            return result;
        }

        function setQuestions(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.questions = data;
            vm.page = pagingParams.page;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
