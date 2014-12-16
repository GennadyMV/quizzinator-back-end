QuizApp.controller('QuizListController', ['$scope', 'QuizAPI', '$location', function($scope, QuizAPI, $location){

	QuizAPI.get_quizes({
		success: function(quizes){
			$scope.quizes = quizes;
		}
	});

	/**
	*	Clones the given quiz
	* @params quiz to be cloned
	*/
	$scope.clone_quiz = function(quiz){
		QuizAPI.clone_quiz({
			id: quiz.id,
			success: function(quiz){
				$scope.message = {
					content: 'The quiz has been clone!',
					type: 'success'
				};

				$location.path('quiz/' + quiz.id + '/edit');
			},
			error: function(){
				$scope.message = {
					content: 'Error clone the quiz!',
					type: 'danger'
				};
			}
		});
	}

}]);
