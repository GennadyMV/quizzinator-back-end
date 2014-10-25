QuizApp.controller('ManageQuizController', ['$scope', function($scope){
	$scope.quiz = {
		title: '',
		reviewable: true,
		reviewRounds: 1,
		items: []
	}

	$scope.widgets = [
		{
			name: 'Open question',
			creator: function(){
				$scope.quiz.items.push({
					question: '',
					item_type: 'open_question'
				});
			}
		},
		{
			name: 'Text container',
			creator: function(){
				$scope.quiz.items.push({
					content: '',
					item_type: 'text_container'
				});
			}
		},
		{
			name: 'Code sample',
			creator: function(){
				$scope.quiz.items.push({
					item_type: 'code_sample',
					code: '',
					language: 'java'
				});
			}
		},
		{
			name: 'Checkbox question',
			creator: function(){
				$scope.quiz.items.push({
					question: '',
					item_type: 'checkbox_question',
					checkboxes: [],
					new_checkbox: {}
				});
			}
		},
		{
			name: 'Multiple choice question',
			creator: function(){
				$scope.quiz.items.push({
					question: '',
					options: [],
					new_option: {},
					item_type: 'multiple_choice_question'
				});
			}
		},
		{
			name: 'Scale question',
			creator: function(){
				$scope.quiz.items.push({
					item_type: 'scale_question',
					title: '',
					min: {
						value: 1,
						title: ''
					},
					max: {
						value: 7,
						title: ''
					},
					questions: ''
				});
			}
		},
		{
			name: 'Slider question',
			creator: function(){
				$scope.quiz.items.push({
					item_type: 'slider_question',
					min: {
						value: 1,
						title: ''
					},
					max: {
						value: 7,
						title: ''
					},
					question: ''
				});
			}
		},
		{
			name: 'Sketchpad',
			creator: function(){
				$scope.quiz.items.push({
					item_type: 'sketchpad',
					title: ''
				});
			}
		},
		{
			name: 'Image',
			creator: function(){
				$scope.quiz.items.push({
					item_type: 'image',
        	imageId: undefined
				});
			}
		}
	];

	$scope.get_item_template = function(item){
		return '/assets/js/app/views/widgets/' + item.item_type + '.html';
	}

	$scope.add_checkbox = function(item){
		item.checkboxes.push({
			title: item.new_checkbox.title
		});

		item.new_checkbox = {};
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

	$scope.remove_checkbox = function(item, index){
		item.checkboxes.splice(index, 1);
	}
}]);
