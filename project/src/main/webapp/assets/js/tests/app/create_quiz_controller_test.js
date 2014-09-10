describe('CreateQuizController', function(){
	beforeEach(module('QuizApp'));

  	var ctrl, scope;

  	var QuizAPIMock = (function(){
  		return {
  			create_quiz: function(options){
  				options.done();
  			}
  		}
  	})();

  	beforeEach(inject(function($controller, $rootScope) {
    	scope = $rootScope.$new();
    	ctrl = $controller('CreateQuizController', {
      		$scope: scope,
      		QuizAPI: QuizAPIMock
    	});
    }));

  	it('should be initialized correctly', function(){
  		expect(scope.quiz.title).toBe('');
  		expect(scope.quiz.items.length).toBe(0);
  	});

  	it('should be able to add an open question item', function(){
  		scope.new_item = {
  			open_question: {
  				question: 'Wazzup?'
  			}
  		}
  		
  		scope.add_open_question();

  		expect(scope.quiz.items.length).toBe(1);
  		expect(scope.quiz.items[0].question).toBe('Wazzup?')
  		expect(scope.quiz.items[0].item_type).toBe('open_question');
  	});

  	it('should have items ordered correctly when added', function(){
  		scope.new_item = {
  			open_question: {
  				question: 'What is love?'
  			}
  		}

  		scope.add_open_question();

  		expect(scope.quiz.items[0].item_order).toBe(0);
  	});

  	it('should be able to save a quiz and inform the user', function(){
  		scope.quiz = {
  			title: 'My awesome quiz',
  			items: []
  		}

  		scope.save_quiz();

  		expect(scope.message.type).toBe('success');
  		expect(scope.message.content).toBe('The quiz has been saved!');
  	});
});