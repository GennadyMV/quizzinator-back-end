QuizApp.directive('highlight', function(){
	return {
		scope: {
			code: '=ngModel'
		},
		link: function(scope, elem, attrs){
			$(elem).html('<pre class="java">' + scope.code + '</pre>')
			hljs.highlightBlock($(elem).children('pre')[0]);
		}
	}
});