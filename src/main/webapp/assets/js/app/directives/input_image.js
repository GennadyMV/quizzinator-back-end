QuizApp.directive('imageInput', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.imageInput);
            
            element.bind('change', function(){
                scope.$apply(function(){
                    model.assign(scope, element[0].files[0]);
                });
            });
        }
    };
}])