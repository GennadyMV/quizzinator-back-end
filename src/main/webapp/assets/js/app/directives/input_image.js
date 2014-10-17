QuizApp.directive('input_image', function(){
	return {
		restrict: 'A',
		link: function(scope,elm,attrs){
			elm.bind('change', function(){
				$parse(attrs,fileInput).assign(scope,elm[0].image)
				scope.$apply();
			})
		}
	}
})