
$.fn.extend({
    quiz: function(options){
        return this.each(function(){
            var _this = this;            
            var quiz_id = $(_this).attr('data-quizId');

            $(_this).addClass('panel panel-default');
            
            API.get_quiz({
                id: quiz_id,
                done: function(quiz){
                    $(_this).html(TEMPLATE.render_quiz(quiz));
                }
            });
        });
    }
});