QuizApp.directive('shareLibs', function () {
    return {
        scope: {
            apiurl: '=ngModel'
        },
        link: function (scope, elem, attrs) {
            $(elem).html('<pre></pre>');

            var code = '<link rel="stylesheet" href="' + scope.apiurl + '/assets/css/quiz-plugin.min.css">\n' +
                        '<script src="' + scope.apiurl + '/assets/js/quiz-plugin.min.js"></script>';
                
            $(elem).children().first().text(code);
        }
    }
});
