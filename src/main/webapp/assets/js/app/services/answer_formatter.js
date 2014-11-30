QuizApp.service('AnswerFormatter', ['$sce', '$rootScope', function($sce, $rootScope){
	var _public = {};
        var _apiurl;

	var _ignorable_output_types = ['code_sample', 'image', 'peer_reviews', 'text_container'];

	var basic_input_formatter = function(item){
		return {
			question: item.question,
			value: '',
			item_type: item.item_type,
                        event_handler: function(action, child, value){
                            var obj = {action: action, element: item.item_type + '_' + item.index, child: child, value: value, actionTime: $.now()};
                            //console.log('action: ' + obj.action + ', element: ' + obj.element + ', child: ' + obj.child + ', val: ' + obj.value );
                            $rootScope._event_buffer.push(obj);
                        }
		};
	};

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
				};
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
		},
                image: function(item){
                        var format = basic_input_formatter(item);
                        format['imageUrl'] = _apiurl + "/images/" + item.imageId;
                        delete format['value'];
                        delete format['question'];

                        return format;
                },
		sketchpad: function(item){
			var format = basic_input_formatter(item);
			delete format['question'];

			format['title'] = item.title;

			return format;
		},
		peer_reviews: function(item){
			var format = basic_input_formatter(item);
			format['count'] = item.count;
			delete format['question'];
			delete format['value'];
			return format;
		},
		my_peer_reviews: function(item){
			var format = basic_input_formatter(item);
			delete format['question'];
			delete format['value'];
			return format;
		}
	};

	/*function basic_output_formatter(item){
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
		},
		sketchpad: function(item){
			var format = basic_output_formatter(item);
			format['question'] = item.title;
			return format;
		}
	}*/

	_public.input = function(quiz, apiurl){
        _apiurl = apiurl;

		var formatted = {
			title: quiz.title,
			answered: quiz.answered,
			id: quiz.id,
			is_open: quiz.isOpen,
			answering_expired: quiz.answeringExpired,
			reviewing_expired: quiz.reviewingExpired,
                        improving_possible: quiz.answerImprovingPossible,
                        can_answer: !quiz.answeringExpired || quiz.answered && quiz.answerImprovingPossible,
                        deadline: quiz.answerDeadline,
                        review_deadline: quiz.reviewDeadline,
                        improve_deadline: quiz.answerImproveDeadline,
			my_latest_answer: quiz.myLatestAnswer ? angular.fromJson(angular.fromJson(quiz.myLatestAnswer).answer) : null,
			items: [],
                        event_handler: function (action, state){
                            var obj = {action: 'click', element: 'quiz', value: state ? 'expanded' : 'collapsed', actionTime: $.now()};
                            $rootScope._event_buffer.push(obj);
                        }
		};

		var items = angular.fromJson(quiz.items);
		
                var i = 0;
		items.forEach(function(item){
			if(typeof input_formatters[item.item_type] === 'function'){
                                item.index = i;
                                item = input_formatters[item.item_type](item);
				formatted.items.push(item);
                                i++;
			}
		});

		return formatted;
	};

	_public.output = function(quiz){
		var answers = [];

		if (!quiz) return [];
		for(var i = 0; i < quiz.items.length; i++){
			var item = quiz.items[i];
			item.index = i;

			if(_ignorable_output_types.indexOf(item.item_type) < 0){
				answers.push(item);
			}
		}
                
		return answers;
	};

	return _public;
}]);
