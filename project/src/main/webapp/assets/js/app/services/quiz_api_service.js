QuizApp.service('QuizAPI', function(){
	var _public = {};

	function parse_quiz(quiz){
		var open_questions = $.grep(quiz.items, function(item){ return item.item_type == 'open_question' });
		var parsed_open_questions = [];

		open_questions.forEach(function(q){
			parsed_open_questions.push({
				question: q.question,
				itemOrder: q.item_order
			});
		});

		var quiz = {
			title: quiz.title,
			openQuestions: parsed_open_questions
		}

		return quiz;
	}
	
	_public.get_quizes = function(options){
		$.get('/quiz')
		.done(function(data){
			options.done(data)
		});
	}

	_public.create_quiz = function(options){
		$.ajax({
			type: 'POST',
			url: '/quiz',
			contentType: 'application/json',
			data: JSON.stringify(parse_quiz(options.quiz))
		})
		.done(function(){
			options.done();
		})
		.fail(function(){
			options.fail();
		});
	}

	return _public;
});