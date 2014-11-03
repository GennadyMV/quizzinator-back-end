QuizApp.directive('highlight', function(){
	return {
		scope: {
			code: '=ngModel',
			language: '@',
			content: '@'
		},
		link: function(scope, elem, attrs){
			var language = scope.language || 'java';

			if(!scope.content){
				$(elem).html('<pre class=' + language + '>' + scope.code + '</pre>')
			}

			hljs.highlightBlock($(elem).children('pre')[0]);
		}
	}
});
