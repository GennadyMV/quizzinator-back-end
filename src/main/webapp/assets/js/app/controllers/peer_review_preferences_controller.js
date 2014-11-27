QuizApp.controller('ImageController', ['$scope', '$routeParams', 'QuizAPI', function($scope, $routeParams, QuizAPI){
	$scope.save_username = function(username) {
		QuizAPI.save_username({
			username: username,
			success: function() {

			}
		})
	}

	$scope.delete_username = function(username) {
		QuizAPI.delete_username({
			username: username,
			success: function() {

			}
		})
	}
}