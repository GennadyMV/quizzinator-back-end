QuizApp.controller('ReviewsController', ['$scope', '$location', '$routeParams', 'QuizAPI', function($scope, $location, $routeParams, QuizAPI){
	QuizAPI.get_reviews({
		user_hash: $routeParams.userHash,
		success: function(quizes){
			$scope.username = quizes[0].yourAnswer.user;
			$scope.quizes = quizes;
		},
		error: function(){
		}
	});

	$scope.upvote_review = function(review){

	}

	$scope.downvote_review = function(review){
		
	}

	$scope.get_answer_template = function(item){
		return '/assets/js/app/views/quiz/answer_formats/' + item.item_type + '.html';
	}
}]);
