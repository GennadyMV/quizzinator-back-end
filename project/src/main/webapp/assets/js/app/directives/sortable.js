QuizApp.directive('sortable', function(){
	return {
		scope: {
			handle: '@',
			order: '=ngModel'
		},
		link: function(scope, elem, attrs){
			var handle_selector = '#' + scope.handle;

			elem.on('order_change', function(){
				scope.$apply(function(){
					scope.order = $(handle_selector + ' > li').index(elem);
				});
			});

			$(handle_selector).sortable({ 
				cursor: 'move', 
				opacity: 0.6,
				update: function(event, ui){
					$(handle_selector + ' > li').trigger('order_change');
				}
			});

			$(handle_selector + ' > li').trigger('order_change');
			$(handle_selector).disableSelection();

		}
	}
});