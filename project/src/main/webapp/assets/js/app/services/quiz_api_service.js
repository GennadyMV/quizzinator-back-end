QuizApp.service('QuizAPI', ['$http', function($http){
	var _public = {};

	_public.get_quizes = function(options){
		$.get('/quiz')
		.done(function(data){
			options.done(data)
		});
	}

	_public.create_quiz = function(options){
		options.quiz.items = angular.toJson(options.quiz.items);

		$http({
			method: 'POST',
			url: '/quiz',
			headers: {
		       "Content-Type": "application/json"
		    },
			data: angular.toJson(options.quiz)
		})
		.success(function(){
			options.success();
		})
		.error(function(){
			options.error();
		});
	}

	return _public;
}]);