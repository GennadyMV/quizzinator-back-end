QuizApp.controller('ReviewPreferencesController', ['$scope', '$routeParams', 'QuizAPI', function($scope, $routeParams, QuizAPI){

	$scope.quiz_id = $routeParams.quizId;

	$scope.init = function() {
		$scope.get_usernames();
	}

	$scope.save_usernames = function() {
		$scope.usernames = $scope.username_field.split('\n');
		QuizAPI.save_usernames({
			usernames: $scope.usernames,
			success: function() {

			}
		});
	}

	$scope.delete_usernames = function() {
		$scope.usernames = $scope.username_field.split('\n');
		QuizAPI.delete_usernames({
			usernames: $scope.usernames,
			success: function() {

			}
		});
	}

	$scope.get_usernames = function() {
		QuizAPI.get_usernames({
			success: function(preferred_users) {
				$scope.current_usernames = preferred_users;
			}
		});
	}
}]);
