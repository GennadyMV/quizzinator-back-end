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
		QuizAPI.delete_usernames({
			usernames: $($scope.current_usernames).not($scope.usernames).get(),
			success: function() {
				$scope.current_usernames = $scope.usernames;
			}
		});
	}


	$scope.get_usernames = function() {
		QuizAPI.get_usernames({
			success: function(preferred_users) {
				$scope.current_usernames = preferred_users;
				$scope.username_field = preferred_users.join('\n');
			}
		});
	}
}]);
