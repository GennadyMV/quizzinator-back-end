describe('EditQuizController', function(){
	beforeEach(module('QuizApp'));

	var ctrl, scope;

	var QuizAPIMock = (function(){
		return {
			get_quiz: function(options){
				options.success({
					title: 'Lorem ipsum',
					items: [
						{
							item_type: 'open_question',
							question: 'Lorem ipsum?'
						}
					]
				});
			},
			edit_quiz: function(options){
				options.success();
			}
		}
	})();

	beforeEach(inject(function($controller, $rootScope) {
	  	$controller('ManageQuizController', {
	  		$scope: $rootScope
	  	});

	  	scope = $rootScope.$new();

	  	ctrl = $controller('EditQuizController', {
	    		$scope: scope,
	    		QuizAPI: QuizAPIMock,
	    		$routeParams: { quizId: 1 }
	  	});
 	}));

 	it('should be able to edit existing quiz', function(){
 		expect(scope.quiz).toBeDefined();

 		scope.quiz.items[0].question = 'Hahaa?';

 		scope.edit_quiz();
 		expect(scope.message.type).toBe('success');
 	});
});