QuizApp.service('QuizAPI', function(){
	var _public = {};

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
			data: JSON.stringify(options.quiz)
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