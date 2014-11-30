QuizApp.directive('sharePeerReview', function () {
    return {
        scope: {
            quiz_id: '=ngModel'
        },
        link: function (scope, elem, attrs) {
            $(elem).html('<textarea class="form-control" rows="1"><div ng-controller="PeerReviewController" ng-init="init({ \'id\': ' + scope.quiz_id + ' })" quiz ng-hide="hidden"><div ng-include="view"></div></div></textarea><button style="margin-top: 15px" class="btn btn-default select-quiz-share-code">Select all</button>');

            $(elem).find('.select-quiz-share-code').on('click', function () {
                $(elem).find('textarea').select();
            });
        }
    };
});
