describe('ReviewsController', function(){
  beforeEach(module('QuizApp'));

  var ctrl, scope;

  var QuizAPIMock = (function(){
    return {
      get_reviews: function(options){
        options.success([
            {
              quizId: 1,
              title: 'This is a quiz',
              reviews: [
                {
                  id: 1,
                  reviewer: 'Pekka',
                  review: 'Nice!',
                  rating: 0
                }
              ],
              yourAnswer: {
                id: 1,
                user: 'Kalle'
              }
            }
        ]);
      },
      vote_review: function(options){
        options.success();
      }
    }
  })();

  beforeEach(inject(function($controller, $rootScope) {
      scope = $rootScope.$new();

      ctrl = $controller('ReviewsController', {
          $scope: scope,
          QuizAPI: QuizAPIMock,
          $routeParams: { userHash: 'abc' }
      });
   }));

   it('should be able to display users reviews', function(){
      expect(scope.quizes.length).toBe(1);
      expect(scope.quizes[0].title).toBe('This is a quiz');
      expect(scope.quizes[0].reviews.length).toBe(1);
      expect(scope.username).toBe('Kalle');
   });

   it('should be able to upvote a review', function(){
     expect(scope.quizes[0].reviews[0].rating).toBe(0);

     scope.upvote_review(scope.quizes[0], scope.quizes[0].yourAnswer, scope.quizes[0].reviews[0]);

     expect(scope.quizes[0].reviews[0].rating).toBe(1);
   });

   it('should be able to downvote a review', function(){
     expect(scope.quizes[0].reviews[0].rating).toBe(0);

     scope.downvote_review(scope.quizes[0], scope.quizes[0].yourAnswer, scope.quizes[0].reviews[0]);

     expect(scope.quizes[0].reviews[0].rating).toBe(-1);
   });
});
