QuizApp.controller('QuizListController', ['$scope', 'QuizAPI', function($scope, QuizAPI){
	QuizAPI.get_quizes({
		success: function(quizes){
			$scope.quizes = quizes;
		}
	});
}]);