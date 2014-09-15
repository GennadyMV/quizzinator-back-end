var API = (function(){
	var _public = {};

	_public.get_quiz = function(options){
			$.get('/quiz/' + options.id)
			.done(function(data){
				var items = [];

				data.openQuestions.forEach(function(q){
					q.item_type = 'open_question'
					items.push(q);
				});

				var quiz = {
					id: data.id,
					title: data.title,
					items: items.sort(function(a, b){ return a.itemOrder - b.itemOrder; })
				}

				options.done(quiz);
 			}).fail(function(){
 				options.fail();
 			});
		};
	
	return _public;
})();
