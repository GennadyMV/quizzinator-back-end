QuizApp.directive('imageInput', ['$parse', function($parse){
	return {
		restrict: 'A',
		link: function(scope, elm, attrs){
			elm.bind('change', function(){
                            console.log('directive called!!');
                            console.log(elm);
                            console.log(elm[0].files);
                            $parse(attrs.imageInput).assign(scope, elm[0].files);
                            scope.$apply();
			})
		}
	}
}])