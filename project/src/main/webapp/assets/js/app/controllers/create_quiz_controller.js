QuizApp.controller('CreateQuizController', ['$scope', 'QuizAPI', function($scope, QuizAPI){
	$scope.quiz = {
		title: '',
		items: []
	}

	$scope.get_item_template = function(item){
		templates = {
			'open_question': '/assets/js/app/views/widgets/open_question.html',
			'text_container': '/assets/js/app/views/widgets/text_container.html',
			'multiple_choice_question': '/assets/js/app/views/widgets/multiple_choice_question.html'
		}

		return templates[item.item_type];
	}

	$scope.add_open_question = function(){
		$scope.quiz.items.push({
			question: '',
			item_type: 'open_question'
		});
	}

	$scope.add_text_container = function(){
		$scope.quiz.items.push({
			content: '',
			item_type: 'text_container'
		});
	}

	$scope.add_multiple_choice_question = function(){
		$scope.quiz.items.push({
			question: '',
			options: [],
			new_option: {},
			item_type: 'multiple_choice_question'
		});
	}

	$scope.remove_item = function(index){
		$scope.quiz.items.splice(index, 1);
	}

	$scope.add_option = function(item){
		item.options.push({
			title: item.new_option.title
		});

		item.new_option = {};
	}

	$scope.remove_option = function(item, index){
		item.options.splice(index, 1);
	}

	$scope.save_quiz = function(){
		var quiz = $scope.quiz;
		quiz.items = JSON.stringify(quiz.items);

		QuizAPI.create_quiz({
			quiz: quiz,
			done: function(){
				$scope.quiz = {
					title: '',
					items: []
				}
				
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