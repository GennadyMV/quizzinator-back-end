QuizApp.controller('CreateQuizController', ['$scope', 'QuizAPI', function($scope, QuizAPI){
	$scope.quiz = {
		title: '',
		items: []
	}

	$scope.new_item = {
		open_question: {
			question: ''
		}
	}

	$scope.add_open_question = function(){
		$scope.quiz.items.push({
			question: $scope.new_item.open_question.question,
			item_order: 0,
			item_type: 'open_question'
		});

		$scope.new_item.open_question.question = '';
	}

	$scope.save_quiz = function(){
		console.log($scope.quiz);
		
		QuizAPI.create_quiz({
			quiz: $scope.quiz,
			done: function(){
				$scope.message = {
					content: 'The quiz has been saved!',
					type: 'success'
				};

				$scope.$apply();
			},
			fail: function(){
				$scope.message = {
					content: 'Error saving the quiz!',
					type: 'danger'
				};

				$scope.$apply();
			}
		});
	}
}]);