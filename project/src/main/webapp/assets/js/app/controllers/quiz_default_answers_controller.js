QuizApp.controller('QuizDefaultAnswersController', ['$scope', '$routeParams', 'AnswerFormatter', 'QuizAPI', function($scope, $routeParams, AnswerFormatter, QuizAPI){
	$scope.quiz_id = $routeParams.quizId;

	QuizAPI.get_quiz({ 
		id: $routeParams.quizId,
		success: function(quiz){
			$scope.quiz = AnswerFormatter.input(quiz);
			console.log($scope.quiz);
		},
		error: function(){

		}
	});

	$scope.get_item_template = function(item){
		return '/assets/js/app/views/quiz/default_answers_templates/' + item.item_type + '.html';
	}


}]);