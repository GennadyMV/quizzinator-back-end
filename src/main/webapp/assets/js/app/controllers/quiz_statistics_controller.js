QuizApp.controller('QuizStatisticsController', ['$scope', '$routeParams', 'QuizAPI', function($scope, $routeParams, QuizAPI){
	$scope.quiz_id = $routeParams.quizId;

	QuizAPI.get_statistics({
		quiz_id: $scope.quiz_id,
		success: function(data){
			$scope.statistics = data;
		}
	});
}]);
