QuizApp.controller('ManageQuizController', ['$scope', function($scope){

	$scope.quiz = {
		title: '',
		reviewable: true,
		reviewRounds: 1,
		items: []
	};

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
			name: 'Multiple choice question with explanations',
			creator: function(){
				$scope.quiz.items.push({
					question: '',
					options: [],
					new_option: {},
					item_type: 'multiple_choice_question_expl'
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
		},
                		{
			name: 'Peer reviews',
			creator: function(){
				$scope.quiz.items.push({
					item_type: 'peer_reviews',
                    count: 5
            	});
			}
		},
		{
			name: 'My peer reviews',
			creator: function(){
				$scope.quiz.items.push({
					item_type: 'my_peer_reviews'
				});
			}
		}
	];

	/**
	* Gives the template file of the given item
	* @param item which template is needed
	* @return path to the template
	*/
	$scope.get_item_template = function(item){
		return '/assets/js/app/views/widgets/' + item.item_type + '.html';
	};

	/**
	*	Add a checkbox to a given item
	* @param item to which checkbox will be added
	*/
	$scope.add_checkbox = function(item){
		item.checkboxes.push({
			title: item.new_checkbox.title
		});

		item.new_checkbox = {};
	};

	/**
	* Removes the given item
	* @param item to be removed
	*/
	$scope.remove_item = function(index){
		$scope.quiz.items.splice(index, 1);
	};

	/**
	* Add a option to a given item
	* @param item to which option will be added
	*/
	$scope.add_option = function(item){
		item.options.push({
			title: item.new_option.title
		});

		item.new_option = {};
	};
        
        /**
         * Adds a title, explanation and correctness to a given item.
         * 
         * @param {type} item
         * @returns {undefined}
         */
        $scope.add_option_expl = function(item){
		item.options.push({
			title: item.new_option.title,
                        explanation: item.new_option.explanation,
                        correct: item.new_option.correct
		});

		item.new_option = {};
	};

	/**
	* Removes option from given item in given index
	* @param item from which the option will be removed and the index of the option
	*/
	$scope.remove_option = function(item, index){
		item.options.splice(index, 1);
	};

	/**
	* Removes checkbox from given item in given index
	* @param item from which the checkbox will be removed and the index of the option
	*/
	$scope.remove_checkbox = function(item, index){
		item.checkboxes.splice(index, 1);
	};

}]);
