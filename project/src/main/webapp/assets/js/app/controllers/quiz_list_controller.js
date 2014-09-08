QuizApp.controller('QuizListController', ['$scope', 'QuizAPI', function($scope, QuizAPI){
	QuizAPI.get_quizes({
		done: function(quizes){
			$scope.quizes = quizes;
			$scope.$apply();
		}
	});
}]);