QuizApp.controller('QuizAnswersController', ['$scope', '$routeParams', 'QuizAPI', function($scope, $routeParams, QuizAPI){
  $scope.quiz_id = $routeParams.quizId;

  QuizAPI.get_answers({
    quiz_id: $routeParams.quizId,
    success: function(answers){
      $scope.answers = answers;
    },
    error: function(){}
  });

  $scope.get_answer_template = function(item){
    return '/assets/js/app/views/quiz/answer_formats/' + item.item_type + '.html';
  }
}]);
