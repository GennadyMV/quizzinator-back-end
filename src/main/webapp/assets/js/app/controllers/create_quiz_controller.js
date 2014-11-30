QuizApp.controller('CreateQuizController', ['$scope', '$location', 'QuizAPI', function($scope, $location, QuizAPI){
	$scope.save_quiz = function(){
		QuizAPI.create_quiz({
			quiz: $scope.$parent.quiz,
			success: function(quiz){
				$scope.$parent.quiz = {
					title: '',
          			reviewable: false,
					reviewRounds: 1,
					items: []
				};

				$scope.message = {
					content: 'The quiz has been saved!',
					type: 'success'
				};

				$location.path('quiz/' + quiz.id + '/edit');
			},
			error: function(){
				$scope.message = {
					content: 'Error saving the quiz!',
					type: 'danger'
				};
			}
		});
	};
}]);
