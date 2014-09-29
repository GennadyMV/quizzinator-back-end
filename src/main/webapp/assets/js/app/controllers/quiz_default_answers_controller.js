QuizApp.controller('QuizDefaultAnswersController', ['$scope', '$routeParams', function($scope, $routeParams){
	$scope.quiz_id = $routeParams.quizId;
}]);