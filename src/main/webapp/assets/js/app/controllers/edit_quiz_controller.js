QuizApp.controller('EditQuizController', ['$scope', '$routeParams', 'QuizAPI', function($scope, $routeParams, QuizAPI){
	QuizAPI.get_quiz({
		id: $routeParams.quizId,
		success: function(quiz){
			$scope.$parent.quiz = quiz;
		},
		error: function(){

		}
	});

    $scope.edit_quiz = function(){
		QuizAPI.edit_quiz({
			quiz: $scope.$parent.quiz,
			success: function(){
				$scope.message = {
					content: 'The quiz has been edited!',
					type: 'success'
				};
			},
			error: function(){
				$scope.message = {
					content: 'Error edit the quiz!',
					type: 'danger'
				};
			}
		});
	}
}]);
