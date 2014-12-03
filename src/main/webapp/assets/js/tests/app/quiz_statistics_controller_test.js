describe('QuizStatisticsController', function(){
  beforeEach(module('QuizApp'));

  var ctrl, scope;

  var QuizAPIMock = (function(){
    return {
      get_statistics: function(options){
        options.success([
          {
            username: 'kalle',
            numberOfAnswers: 2,
            hasImproved: true
          },
          {
            username: 'marko',
            numberOfAnswers: 1,
            hasImproved: false
          }
        ]);
      }
    }
  })();

  beforeEach(inject(function($controller, $rootScope){
    scope = $rootScope.$new();

    ctrl = $controller('QuizStatisticsController', {
      $scope: scope,
      $routeParams: { quizId: 1 },
      QuizAPI: QuizAPIMock
    });
  }));

  it('should be able to display quiz statistics', function(){
    expect(scope.statistics.length).toBe(2);
    expect(scope.statistics[0].username).toBe('kalle');
  });

});
