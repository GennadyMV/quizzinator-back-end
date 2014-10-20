describe('ManageQuizController', function(){
	beforeEach(module('QuizApp'));

	var ctrl, scope;

	beforeEach(inject(function($controller, $rootScope) {
  	scope = $rootScope.$new();
  	ctrl = $controller('ManageQuizController', {
    		$scope: scope
  	});
  }));

	it('should be initialized correctly', function(){
		expect(scope.quiz.title).toBe('');
		expect(scope.quiz.items.length).toBe(0);
	});

	it('should be able to add an open question item', function(){
    scope.widgets[0].creator();
    expect(scope.quiz.items[0].item_type).toBe('open_question');

		scope.quiz.items[0].question = 'Wazzup?';

		expect(scope.quiz.items.length).toBe(1);
		expect(scope.quiz.items[0].question).toBe('Wazzup?')
		expect(scope.quiz.items[0].item_type).toBe('open_question');
	});

  it('should be able to add an text container item', function(){
    scope.widgets[1].creator();
    expect(scope.quiz.items[0].item_type).toBe('text_container');

    scope.quiz.items[0].content = '<h1>This is my quiz!</h1>';

    expect(scope.quiz.items.length).toBe(1);
    expect(scope.quiz.items[0].content).toBe('<h1>This is my quiz!</h1>');
  });

  it('should be able to add an multiple choice question item', function(){
    scope.widgets[4].creator();
    expect(scope.quiz.items[0].item_type).toBe('multiple_choice_question');

    scope.quiz.items[0].question = 'Pick one!';

    expect(scope.quiz.items.length).toBe(1);
    expect(scope.quiz.items[0].question).toBe('Pick one!');
  });

  it('should be able to add an option to a multiple choice question', function(){
    scope.widgets[4].creator();
    scope.quiz.items[0].new_option = {
      title: 'Pick me!'
    }

    scope.add_option(scope.quiz.items[0]);

    expect(scope.quiz.items[0].options.length).toBe(1);
    expect(scope.quiz.items[0].options[0].title).toBe('Pick me!');
  });

  it('should be able to remove an option from a multiple choice question', function(){
    scope.widgets[4].creator();
    scope.quiz.items[0].new_option = {
      title: 'Pick me!'
    }

    scope.add_option(scope.quiz.items[0]);

    expect(scope.quiz.items[0].options.length).toBe(1);

    scope.remove_option(scope.quiz.items[0], 0);

    expect(scope.quiz.items[0].options.length).toBe(0);      
  });

  it('should be able to add scale questions', function(){
    scope.widgets[5].creator();

    expect(scope.quiz.items.length).toBe(1);
    expect(scope.quiz.items[0].item_type).toBe('scale_question');
  });

  it('should be able to add code sample', function(){
    scope.widgets[2].creator();
    scope.quiz.items[0].code = 'int luku = 4;';

    expect(scope.quiz.items.length).toBe(1);
    expect(scope.quiz.items[0].item_type).toBe('code_sample');
    expect(scope.quiz.items[0].code).toContain('int luku');
  });

  it('should be able to add a checkbox question', function(){
    scope.widgets[3].creator();
    scope.quiz.items[0].question = 'Choose one or more';

    expect(scope.quiz.items.length).toBe(1);
    expect(scope.quiz.items[0].question).toBe('Choose one or more');
  });

  it('should be able to add a slider question', function(){
    scope.widgets[6].creator();
    scope.quiz.items[0].question = 'Slider';

    expect(scope.quiz.items.length).toBe(1);
    expect(scope.quiz.items[0].question).toBe('Slider');
  });

  it('should be able to add checkboxes to a checkbox question and remove checkboxes from it', function(){
    scope.widgets[3].creator();
    scope.quiz.items[0].question = 'Choose one or more';
    scope.quiz.items[0].new_checkbox = {
      title: 'Choose this'
    };

    expect(scope.quiz.items[0].checkboxes.length).toBe(0);

    scope.add_checkbox(scope.quiz.items[0]);

    expect(scope.quiz.items[0].checkboxes.length).toBe(1);

    scope.remove_checkbox(scope.quiz.items[0], 0);

    expect(scope.quiz.items[0].checkboxes.length).toBe(0);
  });

  it('should be able to remove an item', function(){
    scope.widgets[1].creator();

    expect(scope.quiz.items.length).toBe(1);

    scope.widgets[0].creator();

    expect(scope.quiz.items.length).toBe(2);

    scope.remove_item(0);

    expect(scope.quiz.items.length).toBe(1);

    scope.remove_item(0);

    expect(scope.quiz.items.length).toBe(0);      
  });

	it('should have items ordered correctly when added', function(){
		scope.widgets[0].creator();
    scope.quiz.items[0].question = 'Wazzup?';

    scope.widgets[0].creator();
    scope.quiz.items[1].question = 'What is love?';

		expect(scope.quiz.items[0].question).toBe('Wazzup?');
    expect(scope.quiz.items[1].question).toBe('What is love?');
	});
});