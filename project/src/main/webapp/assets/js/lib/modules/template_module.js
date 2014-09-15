var TEMPLATE = (function(){
	var _public = {};

	var templates = {
		loading: 'Loading the quiz...',
		quiz_body: '<div class="panel-heading"><h3 class="panel-title">{{title}}</h3></div><div class="panel-body"><form>{{{body}}}<div class="form-group"><button class="btn btn-primary submit-quiz" data-targetQuiz="{{id}}"><span class="glyphicon glyphicon-ok"></span> Send</button></form></div></div>',
		open_question: '<div class="form-group quiz-item" data-type="open_question" data-itemId="{{id}}"><label>{{question}}</label><textarea class="form-control open-question-value" required></textarea></div>'
	}

	_public.render_loading = function(){
		return templates.loading;
	}

	_public.render_quiz = function(data){
		var quiz_template = Handlebars.compile(templates.quiz_body);
		var quiz_body = "";

		data.items.forEach(function(item){
			var item_template = Handlebars.compile(templates[item.item_type]);
			quiz_body += item_template(item);
		});

		return quiz_template({ id: data.id, title: data.title, body: quiz_body });
	}

	return _public;
})();
