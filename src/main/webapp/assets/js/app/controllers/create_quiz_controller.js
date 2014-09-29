QuizApp.controller('CreateQuizController', ['$scope', '$location', 'QuizAPI', function($scope, $location, QuizAPI){
	$scope.save_quiz = function(){
		QuizAPI.create_quiz({
			quiz: $scope.$parent.quiz,
			success: function(){
				$scope.$parent.quiz = {
					title: '',
                    reviewable: false,
					items: []
				}
				
				$scope.message = {
					content: 'The quiz has been saved!',
					type: 'success'
				};
			},
			error: function(){
				$scope.message = {
					content: 'Error saving the quiz!',
					type: 'danger'
				};
			}
		});
	}
}]);