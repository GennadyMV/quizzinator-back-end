QuizApp.controller('CreateQuizController', ['$scope', 'QuizAPI', function($scope, QuizAPI){
	$scope.quiz = {
		title: '',
		items: []
	}

	$scope.new_item = {
		open_question: {
			title: ''
		}
	}

	$scope.add_open_question = function(){
		$scope.quiz.items.push({
			title: $scope.new_item.open_question.title,
			order: 0
		});

		$scope.new_item.open_question.title = '';
	}

	$scope.save_quiz = function(){
		$scope.message = {
			content: 'The quiz has been saved!',
			type: 'success'
		};
		/*
			QuizAPI.create_quiz({
				done: function(){
					$scope.message_success = 'The quiz has been saved!';
				},
				fail: function(){
					$scope.message_danger = 'Error saving the quiz!';
				}
			});
		*/
	}
}]);