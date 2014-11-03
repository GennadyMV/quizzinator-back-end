QuizApp.directive('shareLibs', function () {
    return {
        scope: {
            apiurl: '=ngModel'
        },
        link: function (scope, elem, attrs) {
            $(elem).html('Add this stylesheet tag\
      <pre>&lt;link rel=&quot;stylesheet&quot; href=&quot;' + scope.apiurl + 'assets/css/quiz-plugin.min.css&quot;&gt;</pre>\
\
      and this script tag\
      <pre>&lt;script src=&quot;' + scope.apiurl + 'assets/js/quiz-plugin.min.js&quot;&gt;&lt;/script&gt;</pre>\
        \
        under the head-element of your web page.');

            $(elem).find('.select-quiz-share-code').on('click', function () {
                $(elem).find('textarea').select();
            })
        }
    }
});
