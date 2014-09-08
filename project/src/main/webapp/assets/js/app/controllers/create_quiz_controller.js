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
			itemOrder: 0,
			itemType: 'open_question'
		});

		$scope.new_item.open_question.question = '';
	}

	$scope.save_quiz = function(){
		var quiz = {
			title: $scope.quiz.title,
			openQuestions: $.grep($scope.quiz.items, function(item){ return item.item_type == 'open_question' })
		}

		QuizAPI.create_quiz({
			quiz: quiz,
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