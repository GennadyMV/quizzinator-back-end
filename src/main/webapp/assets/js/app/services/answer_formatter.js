QuizApp.service('AnswerFormatter', ['$sce', function($sce){
	var _public = {};

	var basic_input_formatter = function(item, type){
		return {
			question: item.question,
			value: '',
			item_type: item.item_type
		}
	}

	var input_formatters = {
		open_question: function(item){
			var format = basic_input_formatter(item);
			format['max_length'] = item.max_length;

			return format;
		},
		multiple_choice_question: function(item){
			var format = basic_input_formatter(item);
			format['options'] = item.options;

			return format;
		},
		checkbox_question: function(item){
			var format = basic_input_formatter(item);
			format['checkboxes'] = $.map(item.checkboxes, function(checkbox){
				return {
					title: checkbox.title,
					value: false
				}
			});

			return format;
		},
		text_container: function(item){
			var format = basic_input_formatter(item);
			format['content'] = $sce.trustAsHtml(item.content);
			delete format['value'];

			return format;
		},
		code_sample: function(item){
			var format = basic_input_formatter(item);
			format['code'] = item.code;
			delete format['value'];

			return format;
		},
		scale_question: function(item){
			var format = basic_input_formatter(item);

			delete format['value'];
			delete format['question'];

			format['title'] = item.title;
			format['questions'] = [];
			format['min'] = item.min;
			format['max'] = item.max;
			format['scale'] = [];

			for(var i = item.min.value; i <= item.max.value; i++){
				format['scale'].push(i);
			}

			questions = item.questions.split('\n');
			questions.forEach(function(question){
				format['questions'].push({
					question: question,
					value: null
				});
			});

			return format;
		},
		slider_question: function(item){
			var format = basic_input_formatter(item);

			format['value'] = Math.ceil(Math.abs(item.min.value - item.max.value) / 2);
			format['min'] = item.min;
			format['max'] = item.max;

			return format;
		}
	}

	function basic_output_formatter(item){
		return {
			question: item.question,
			value: item.value,
			item_type: item.item_type
		}
	}

	var output_formatters = {
		open_question: function(item){
			var format = basic_output_formatter(item);

			if(item['max_length']){
				format['value'] = format['value'].substring(0, item['max_length']);
			}

 			return format;
		},
		scale_question: function(item){
			var format = basic_output_formatter(item);

			format['question'] = item['title'];
			format['value'] = item['questions'];

			return format;
		},
		multiple_choice_question: function(item){
			return basic_output_formatter(item);
		},
		checkbox_question: function(item){
			var format = basic_output_formatter(item);

			var chosen = $.grep(item.checkboxes, function(checkbox){
				return checkbox.value;
			});

			format['value'] = $.map(chosen, function(checkbox){
				return checkbox.title;
			});

			return format;
		},
		slider_question: function(item){
			return basic_output_formatter(item);
		}
	}

	_public.input = function(quiz){
		var formatted = {
			title: quiz.title,
			answered: quiz.answered,
			id: quiz.id,
			is_open: quiz.isOpen,
			items: []
		}

		var items = angular.fromJson(quiz.items);
		items.forEach(function(item){
			if(typeof input_formatters[item.item_type] === 'function'){
				formatted.items.push(input_formatters[item.item_type](item));
			}
		});

		return formatted;
	}

	_public.output = function(quiz){
		var answers = [];

		if (!quiz) return [];

		quiz.items.forEach(function(item){
			console.log(item.item_type);

			if(typeof output_formatters[item.item_type] === 'function'){
				answers.push(output_formatters[item.item_type](item));
			}
		});

		return answers;
	}

	return _public;
}]);
