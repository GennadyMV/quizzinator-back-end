describe('QuizListController', function () {
    var ctrl, scope;

    var QuizAPIMock = (function () {
        return {
            clone_quiz: function (options) {
                options.success([
                    {
                        id: 2,
                        title: 'This is a quiz'
                    }
                ]);
            }
        }
    })();

    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();

        ctrl = $controller('QuizListController', {
            $scope: scope,
            QuizAPI: QuizAPIMock
        });
    }));
    
    it('should be able to clone an existing quiz', function () {
        var quiz =  {
                        id: 1,
                        title: 'This is a quiz'
                    }
        scope.clone_quiz(quiz);
        expect(scope.message.type).toBe('success');
    });
});
