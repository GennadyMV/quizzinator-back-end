describe('QuizAnswersController', function () {
    beforeEach(module('QuizApp'));
    
    var ctrl, scope;

    var QuizAPIMock = (function () {
        return {
            remove_answer: function (options) {
                options.success(
                        {
                            
                        }
                );
            },
            get_answers: function(options){
                return [];
            }
        }
    })();

    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        

        ctrl = $controller('QuizAnswersController', {
            $scope: scope,
            QuizAPI: QuizAPIMock
        });
    }));
    
    it('should be able to remove an existing quiz', function () {
        scope.remove_answer(1);
        expect(scope.message.type).toBe('success');
    });
    
}); 