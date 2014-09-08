var QuizApp = angular.module('QuizApp', ['ngRoute']);

QuizApp.config(['$routeProvider', function($routeProvider){
	$routeProvider
	.when('/quiz/new', {
		templateUrl: '/assets/js/app/views/quiz/new.html',
		controller: 'CreateQuizController'
	})
	.when('/quiz/all', {
		templateUrl: '/assets/js/app/views/quiz/index.html',
		controller: 'QuizListController'
	})
	.otherwise({
		redirectTo: '/quiz/new'
	});
}]);

