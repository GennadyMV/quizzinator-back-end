QuizApp.service('QuizAPI', ['$http', 'AnswerFormatter', function ($http) {
        var _public = {};

        _public.get_quizes = function (options) {
            $http({
                method: 'GET',
                url: '/quiz'
            })
                    .success(function (quizes) {
                        quizes.forEach(function (quiz) {
                            quiz.items = angular.fromJson(quiz.items);
                        });

                        options.success(quizes)
                    });
        }

        _public.remove_answer = function (options) {
            $http({
                method: 'DELETE',
                url: 'quiz/' + options.quiz_id + '/answer/' + options.answer_id,
            })
                    .success(function () {
                        options.success();
                    })
                    .error(function () {
                        options.error();
                    });
        }

        _public.vote_review = function(options){
          $http({
            method: 'POST',
            url: '/quiz/' + options.quiz_id + '/answer/' + options.answer_id + '/review/' + options.review_id + '/rate',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            data: $.param({ userhash: options.userhash, rating: options.rating })
          }).
          success(function(){
            options.success();
          })
          .error(function(){
            options.error();
          });
        }

        _public.get_answers = function(options){
          $http({
            method: 'GET',
            url: '/quiz/' + options.quiz_id + '/answer'
          })
          .success(function(answers){
            answers.forEach(function (answer) {
                answer.answer = angular.fromJson(answer.answer);
            });

            options.success(answers);
          })
          .error(function(){
            options.error();
          })
        }

        _public.get_quiz = function (options) {
            $http({
                method: 'GET',
                url: 'quiz/' + options.id
            })
                    .success(function (quiz) {
                        quiz.items = angular.fromJson(quiz.items);
                        options.success(quiz);
                    })
                    .error(function () {
                        options.error();
                    });
        }

        _public.edit_quiz = function (options) {
            var quiz = jQuery.extend({}, options.quiz);
            quiz.items = angular.toJson(options.quiz.items);

            $http({
                method: 'POST',
                url: 'quiz/' + options.quiz.id,
                headers: {
                    'Content-Type': 'application/json'
                },
                data: angular.toJson(quiz)
            })
                    .success(function () {
                        options.success();
                    })
                    .error(function () {
                        options.error();
                    });
        }

        _public.create_quiz = function (options) {
            var quiz = jQuery.extend({}, options.quiz);
            quiz.items = angular.toJson(options.quiz.items);

            $http({
                method: 'POST',
                url: '/quiz',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: angular.toJson(quiz)
            })
                    .success(function (data, status, headers, config) {
                        options.success(data);
                    })
                    .error(function () {
                        options.error();
                    });
        }

        _public.get_reviews = function (options) {
            $http({
                method: 'GET',
                url: 'reviews/' + options.user_hash
            })
                    .success(function (reviews) {
                        reviews.forEach(function (review) {
                            review.yourAnswer.answer = angular.fromJson(review.yourAnswer.answer);
                        });

                        options.success(reviews);
                    })
                    .error(function () {
                        options.error();
                    });
        }


        _public.create_default_answer = function (options) {
            $http({
                method: 'POST',
                url: 'quiz/' + options.id + '/placeholder',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: angular.toJson(options.answers)
            })
                    .success(function () {
                        options.success();
                    })
                    .error(function () {
                        options.error();
                    });
        }

        _public.clone_quiz = function (options) {

            $http({
                method: 'POST',
                url: 'quiz/' + options.id + '/clone',
            })
                    .success(function (data) {
                        options.success(data);
                    })
                    .error(function () {
                        options.error();
                    });

        }

        _public.upload_image = function(options) {
            var data = new FormData();
            data.append('image', options.image);


            $http.post('images', data, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
            .success(function (data) {
                options.success(data);
            })
            .error(function () {
                //console.log("fails")
            });
        }

        _public.save_usernames = function(options) {
            $http({
                method: 'POST',
                url: 'preferredUsers/',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: options.usernames
            })
            .success(function(){

            })
        }

        _public.delete_usernames = function(options) {
            $http({
                method: 'DELETE',
                url: 'preferredUsers/'
            })
            .success(function(){

            })
        }

        _public.get_usernames = function(options) {
            $http({
                metod: 'GET',
                url: 'preferredUsers/'
            })
            .success(function(){

            })
        }

        _public.get_statistics = function(options){
          $http.get('quiz/' + options.quiz_id + '/userData').success(function(data){ options.success(data); });
        }


        return _public;
    }]);
