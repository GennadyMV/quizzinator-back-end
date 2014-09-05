var API = (function(){
	var _public = {};

	_public.get_quiz = function(options){
			$.get('/quiz/' + options.id)
			.done(function(data){
				options.done(data);
 	
 			}).fail(function(data){
 				options.fail(data);

 			})
		};
	
	return _public;
})();
