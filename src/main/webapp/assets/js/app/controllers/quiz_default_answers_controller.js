QuizApp.controller('QuizDefaultAnswersController', ['$scope', '$routeParams', 'AnswerFormatter', 'QuizAPI', function($scope, $routeParams, AnswerFormatter, QuizAPI){
	$scope.quiz_id = $routeParams.quizId;

	QuizAPI.get_quiz({ 
		id: $routeParams.quizId,
		success: function(quiz){
			$scope.quiz = AnswerFormatter.input(quiz);
		},
		error: function(){
		}
	});

	$scope.get_item_template = function(item){
		return '/assets/js/app/views/quiz/answers/' + item.item_type + '.html';
	}

	$scope.create_default_answer = function() {
		QuizAPI.create_default_answer({
			answers: AnswerFormatter.output($scope.quiz),
			id: $routeParams.quizId,
			success: function(){
				$scope.message = {
					content: 'Default answers have been submitted.',
					type: 'success'
				}
			},
			error: function(){
				$scope.message = {
					content: 'Error in submitting default answers.',
					type: 'danger'
				}
			}
		});
	}


}]);