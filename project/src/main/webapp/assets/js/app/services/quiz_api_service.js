QuizApp.service('QuizAPI', function(){
	var _public = {};
	
	_public.create_quiz = function(options){
		$.post('/quiz', options.quiz)
		.done(function(){
			options.done(data);
		})
		.fail(function(){
			options.fail(data);
		});
	}

	return _public;
});