QuizApp.controller('ReviewsController', ['$scope', '$location', '$routeParams', 'QuizAPI', function($scope, $location, $routeParams, QuizAPI){
	/*QuizAPI.get_reviews({
		user_hash: $routeParams.userHash,
		success: function(quizes){
			$scope.quizes = quizes;
		},
		error: function(){
		}
	});*/

	$scope.quizes = [
		{
			title: 'What is love?',
			reviews: [
				{
					reviewer: 'Aaro',
					review: 'Hyvä Kalle!'
				},
				{
					reviewer: 'Toni',
					review: 'Jeaa!!!'
				}
			]
		},
		{
			title: 'Vlatislaav?',
			reviews: [
				{
					reviewer: 'Niko',
					review: 'Jees.'
				},
				{
					reviewer: 'Aaro',
					review: 'Hyvä Kalle!'
				},
				{
					reviewer: 'Toni',
					review: 'Jeaa!!!'
				}
			]
		}
	];
}]);