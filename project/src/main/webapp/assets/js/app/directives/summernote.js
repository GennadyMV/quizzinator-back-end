QuizApp.directive('summernote', function(){
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
					});
				}
			});

			$(elem).code(scope.content);
			$('.note-editor').bind('click', function(){
				$('.quiz-item').sortable('cancel');
			});
		}
	}
});