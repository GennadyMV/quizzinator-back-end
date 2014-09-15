var ANSWER_READER = (function(){
	var _public = {};

	var readers = {
		open_question: function(item){
			var item_id = $(item).attr('data-itemId');
			var value = $(item).find('.open-question-value').val();

			return {
				id: item_id,
				value: value
			}
		}
	}

	_public.read_answers = function(options){
		var quiz_element = options.quiz;
		var answers = [];

		$(quiz_element).find('.quiz-item').each(function(){
			var item = $(this);
			var type = $(item).attr('data-type');

			answers.push(readers[type](item));
		});

		return answers;
	}

	return _public;
})();