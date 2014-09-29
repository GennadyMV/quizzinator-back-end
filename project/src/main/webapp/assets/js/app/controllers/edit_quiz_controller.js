QuizApp.controller('EditQuizController', ['$scope', '$routeParams', 'QuizAPI', function($scope, $routeParams, QuizAPI){
	QuizAPI.get_quiz({ 
		id: $routeParams.quizId,
		success: function(quiz){
			$scope.$parent.quiz = quiz;
		},
		error: function(){

		}
	});
}]);