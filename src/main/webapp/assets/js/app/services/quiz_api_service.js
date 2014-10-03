QuizApp.service('QuizAPI', ['$http', 'AnswerFormatter', function($http){
	var _public = {};

	_public.get_quizes = function(options){
		$http({
			method: 'GET',
			url: '/quiz'
		})
		.success(function(quizes){
			quizes.forEach(function(quiz){
				quiz.items = angular.fromJson(quiz.items);
			});

			options.success(quizes)
		});
	}

	_public.get_quiz = function(options){
		$http({
			method: 'GET',
			url: 'quiz/' + options.id
		})
		.success(function(quiz){
			quiz.items = angular.fromJson(quiz.items);
			options.success(quiz);
		})
		.error(function(){
			options.error();
		});
	}

	_public.edit_quiz = function(options){
		var quiz = jQuery.extend({}, options.quiz);
		quiz.items = angular.toJson(options.quiz.items);

		$http({
			method: 'POST',
			url: 'quiz/' + options.quiz.id,
			headers: {
				'Content-Type': 'application/json'
			},
			data: angular.toJson(quiz)
		})
		.success(function(){
			options.success();
		})
		.error(function(){
			options.error();
		});
	}

	_public.create_quiz = function(options){
		var quiz = jQuery.extend({}, options.quiz);
		quiz.items = angular.toJson(options.quiz.items);

		$http({
			method: 'POST',
			url: '/quiz',
			headers: {
		       'Content-Type': 'application/json'
		    },
			data: angular.toJson(quiz)
		})
		.success(function(data, status, headers, config){
			options.success(data);
		})
		.error(function(){
			options.error();
		});
	}

	_public.get_reviews = function(options){
		$http({
			method: 'GET',
			url: 'reviews/' + options.user_hash
		})
		.success(function(reviews){
			options.success(reviews);
		})
		.error(function(){
			options.error();
		});
	}


	_public.create_default_answer = function(options) {
		$http({
			method: 'POST',
			url: 'quiz/' + options.id + '/placeholder',
			headers: {
				'Content-Type': 'application/json'
			},
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