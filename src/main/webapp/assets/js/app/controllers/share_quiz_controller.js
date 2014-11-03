QuizApp.controller('ShareQuizController', ['$scope', '$routeParams', '$location', function($scope, $routeParams, $location){
	$scope.quiz_id = $routeParams.quizId;
        $scope.apiurl = $location.absUrl().split('#')[0];
}]);
