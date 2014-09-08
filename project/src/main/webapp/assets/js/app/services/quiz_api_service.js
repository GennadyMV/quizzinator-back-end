QuizApp.service('QuizAPI', function(){
	var _public = {};
	
	_public.create_quiz = function(options){
		$.post('/quiz', options.quiz)
		.done(function(){
			options.done();
		})
		.fail(function(){
			options.fail();
		});
	}

	return _public;
});