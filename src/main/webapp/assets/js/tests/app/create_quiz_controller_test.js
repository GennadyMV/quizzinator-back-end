describe('CreateQuizController', function(){
	beforeEach(module('QuizApp'));

	var ctrl, scope;

	var QuizAPIMock = (function(){
		return {
			create_quiz: function(options){
				options.success({ id: 1 });
			}
		}
	})();

	beforeEach(inject(function($controller, $rootScope) {
	  	$controller('ManageQuizController', {
	  		$scope: $rootScope
	  	});

	  	scope = $rootScope.$new();

	  	ctrl = $controller('CreateQuizController', {
	    		$scope: scope,
	    		QuizAPI: QuizAPIMock
	  	});
 	}));

 	it('should have all widgets available', function(){
 		expect(scope.widgets.length).toBeDefined();
 	});

 	it('should be able to save a quiz', function(){
 		expect(scope.save_quiz).toBeDefined();
 		
 		scope.quiz = {
 			title: 'Lorem ipsum',
 			items: []
 		};

 		scope.save_quiz();

 		expect(scope.message.type).toBe('success');
 	});
});