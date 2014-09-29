QuizApp.service('QuizAPI', ['$http', function($http){
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
			quiz = angular.fromJson(quiz);
			quiz.items = angular.fromJson(quiz.items)
			options.success(angular.fromJson(quiz));
		})
		.error(function(){
			options.error();
		});
	}

	_public.edit_quiz = function(options){
		options.quiz.items = angular.fromJson = (options.quiz.items);
		options.quiz.items = angular.toJson(options.quiz.items);
		console.log(options.quiz.items);

		$http({
			method: 'POST',
			url: 'quiz/' + options.quiz.id,
			headers: {
				'Content-Type': 'application/json'
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

	_public.create_quiz = function(options){
		options.quiz.items = angular.toJson(options.quiz.items);

		$http({
			method: 'POST',
			url: '/quiz',
			headers: {
		       'Content-Type': 'application/json'
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