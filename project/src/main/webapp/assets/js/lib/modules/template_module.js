var TEMPLATE = (function(){
	var _public = {};

	var templates = {
		quiz_body: '<div class="panel-heading"><h3 class="panel-title">{{title}}</h3></div><div class="panel-body">{{{body}}}<div class="form-group"><button class="btn btn-primary submit-quiz">Send</button></div></div>',
		open_question: '<div class="form-group quiz-item" data-type="open_question" data-itemId="1"><label>{{title}}</label><textarea class="form-control"></textarea></div>'
	}

	_public.render_quiz = function(data){
		var quiz_template = Handlebars.compile(templates.quiz_body);
		var quiz_body = "";

		data.items.forEach(function(item){
			var item_template = Handlebars.compile(templates[item.type]);
			quiz_body += item_template(item);
		});

		return quiz_template({ title: data.title, body: quiz_body });
	}

	return _public;
})();
