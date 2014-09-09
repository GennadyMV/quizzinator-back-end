'use strict'

describe('Quiz plugin', function(){
	it('should render title corretly', function(){
		var test_quiz = {
			title: 'My awesome quiz',
			items: []
		}

		var rendered_quiz = TEMPLATE.render_quiz(test_quiz);

		expect(rendered_quiz).toContain('My awesome quiz');
	});

	it('should render fields correctly when one field', function(){
		var test_quiz = {
			title: 'My awesome quiz',
			items: [
				{
					question: 'My awesome question',
					item_type: 'open_question',
					id: 1
				}
			]
		}

		var rendered_quiz = TEMPLATE.render_quiz(test_quiz);

		expect(rendered_quiz).toContain('My awesome quiz');
		expect(rendered_quiz).toContain('My awesome question');
	});

	it('should render fields correctly when multiple fields', function(){
		var test_quiz = {
			title: 'My awesome quiz',
			items: [
				{
					question: 'First awesome question',
					item_type: 'open_question',
					id: 1
				},
				{
					question: 'Second awesome question',
					item_type: 'open_question',
					id: 2
				}
			]
		}

		var rendered_quiz = TEMPLATE.render_quiz(test_quiz);

		expect(rendered_quiz).toContain('My awesome quiz');
		expect(rendered_quiz).toContain('First awesome question');
		expect(rendered_quiz).toContain('Second awesome question');
	});
});
