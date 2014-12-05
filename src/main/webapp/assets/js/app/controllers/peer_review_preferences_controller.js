QuizApp.controller('ReviewPreferencesController', ['$scope', '$routeParams', 'QuizAPI', function($scope, $routeParams, QuizAPI){
	
	$scope.init = function() {
		console.log('toimii');
		$scope.get_usernames();
		console.log('vieläkin toimii');
	}

	$scope.save_usernames = function() {
		console.log($scope.usernames);
		QuizAPI.save_usernames({
			usernames: $scope.usernames,
			success: function() {

			}
		});
	}

	$scope.delete_usernames = function() {
		QuizAPI.delete_usernames({
			success: function() {

			}
		});
	}

	$scope.get_usernames = function() {
		QuizAPI.get_usernames({
			success: function(usernames) {
				$scope.current_usernames = usernames;
			}
		});
	}
}]);