QuizApp.controller('ImageController', ['$scope', '$routeParams', 'QuizAPI', function($scope, $routeParams, QuizAPI){

  /**
  * Sends image to the server
  */
  $scope.send_image = function(){
    var file = $scope.image;
    QuizAPI.upload_image({
      image: file,
      success: function(data) {
        $scope.item.imageId = data.imageId;
      }
    });
  };

}])
