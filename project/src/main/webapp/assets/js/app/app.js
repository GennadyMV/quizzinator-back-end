var QuizApp = angular.module('QuizApp', ['ngRoute']);

QuizApp.config(['$routeProvider', function($routeProvider){
	$routeProvider.when('/quiz/new', {
		templateUrl: '/assets/js/app/views/quiz/new.html',
		controller: 'CreateQuizController'
	}).otherwise({
		redirectTo: '/quiz/new'
	});
}]);

