QuizApp.controller('ImageController', ['$scope', '$routeParams', 'QuizAPI', function($scope, $routeParams, QuizAPI){
	$scope.send_image = function(elm){
		console.log(elm);
		console.log(elm.image);
		$scope.image = elm.image;
		$scope.$apply();
		QuizAPI.upload_image({
			image: $scope.image,
			success: function(){
				$scope.message = {
					content: 'The image has been sent!',
					type: 'success'
				};
			},
			error: function(){
				$scope.message = {
					content: 'Error in sending the image!',
					type: 'danger'
				};
			}
		})
	}
}])