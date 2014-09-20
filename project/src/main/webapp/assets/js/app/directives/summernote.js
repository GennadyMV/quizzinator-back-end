QuizApp.directive('summernote', ['$sce', function($sce){
	return {
		scope: {
			content: '=ngModel'
		},
		link: function(scope, elem, attrs){
			$(elem).summernote({
				height: 300,
				onkeyup: function(){
					scope.$apply(function(){
						scope.content = $(elem).code();
						console.log(scope.content);
					});
				}
			});

			$(elem).code(scope.content);
			$('.note-editor').bind('click', function(){
				$('.quiz-item').sortable('cancel');
			});
		}
	}
}]);