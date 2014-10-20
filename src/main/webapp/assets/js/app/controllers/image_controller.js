QuizApp.controller('ImageController', ['$scope', '$routeParams', 'QuizAPI', function($scope, $routeParams, QuizAPI){
    
    $scope.send_image = function(){
        var file = $scope.image;
        console.log('file is ' + JSON.stringify(file));
        QuizAPI.upload_image({image:file});
    };
    
}])
