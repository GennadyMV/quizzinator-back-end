QuizApp.directive('highlighter', function(){
	return {
		scope: {
			code: '=ngModel'
		},
		link: function(scope, elem, attrs){

			$(elem).prepend('<div class="code-sample"><pre class="java">' + scope.code + '</pre></div>');
			
			hljs.highlightBlock($(elem).find('.code-sample pre')[0]);
			
			$(elem).children('textarea').allowTabChar();
			$(elem).children('textarea').on('keyup', function(e){
				var _this = this;

				scope.$apply(function(){
					scope.code = $(_this).val();
				});
			});

			scope.$watch('code', function(new_val, old_val){
				$(elem).children('.code-sample').html('<pre class="java">' + scope.code + '</pre>');
				
				hljs.highlightBlock($(elem).find('.code-sample pre')[0]);
			});
		}
	}
});