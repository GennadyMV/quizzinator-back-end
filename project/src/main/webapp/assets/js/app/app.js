var QuizApp = angular.module('QuizApp', ['ngRoute', 'textAngular']);

QuizApp.config(['$routeProvider', function($routeProvider){
	$routeProvider
	.when('/quiz/new', {
		templateUrl: '/assets/js/app/views/quiz/new.html',
		controller: 'ManageQuizController'
	})
	.when('/quiz/all', {
		templateUrl: '/assets/js/app/views/quiz/index.html',
		controller: 'QuizListController'
	})
	.when('/quiz/:quizId/edit', {
		templateUrl: '/assets/js/app/views/quiz/edit.html',
		controller: 'ManageQuizController'
	})
	.when('/quiz/:quizId/default-answers', {
		templateUrl: '/assets/js/app/views/quiz/default_answers.html',
		controller: 'QuizDefaultAnswersController'
	})
	.when('/quiz/:quizId/share', {
		templateUrl: '/assets/js/app/views/quiz/share.html',
		controller: 'ShareQuizController'
	})
	.otherwise({
		redirectTo: '/quiz/new'
	});
}]);

