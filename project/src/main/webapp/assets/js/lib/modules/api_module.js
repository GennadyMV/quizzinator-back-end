var API = (function(){
	var _public = {};

	_public.get_quiz = function(options){
			$.get('/quiz/' + options.id)
			.done(function(data){
				var items = [];
				data.openQuestions.forEach(function(q){
					q.itemType = 'open_question'
				});

				var quiz = {
					data.title,
					items: items
				}

				options.done(quiz);
 			}).fail(function(){
 				options.fail();
 			});
		};
	
	return _public;
})();
