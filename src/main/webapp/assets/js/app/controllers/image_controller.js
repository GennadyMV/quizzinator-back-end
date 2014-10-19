QuizApp.controller('ImageController', ['$scope', '$routeParams', 'QuizAPI', function($scope, $routeParams, QuizAPI){
	$scope.input_image = function(elm){
		QuizAPI.send_image({
			$scope.image = elm.image;
			$scope.$apply();
		})
	}
}])