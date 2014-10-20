QuizApp.directive('imageInput', function(){
	return {
		restrict: 'A',
		link: function(scope, elm, attrs){
			elm.bind('change', function(){
				$parse(attrs,imageInput).assign(scope,elm[0].files);
				console.log(elm[0]files);
				scope.$apply();
			})
		}
	}
})