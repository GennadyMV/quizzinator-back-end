describe('QuizDefaultAnswersController', function(){
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
			create_default_answer: function(options){
				options.success();
			}
		}
	})();

	beforeEach(inject(function($controller, $rootScope) {
	  	scope = $rootScope.$new();

	  	ctrl = $controller('QuizDefaultAnswersController', {
	    		$scope: scope,
	    		QuizAPI: QuizAPIMock,
	    		$routeParams: { quizId: 1 }
	  	});
 	}));

 	it('should be able to create default answer', function(){
 		expect(scope.quiz).toBeDefined();

 		scope.create_default_answer();

 		expect(scope.message.type).toBe('success');
 	})
})