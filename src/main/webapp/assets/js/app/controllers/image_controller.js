QuizApp.controller('ImageController', ['$scope', '$routeParams', 'QuizAPI', function($scope, $routeParams, QuizAPI){
    
    $scope.send_image = function(){
        var file = $scope.image;
        QuizAPI.upload_image({image:file});
    };
    
}])
