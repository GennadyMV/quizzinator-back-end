$.fn.extend({
    quiz: function(options){
        return this.each(function(){
            var _this = this;            
            var quiz_id = $(_this).attr('data-quizId');

            $(_this).addClass('panel panel-default');
            $(_this).html(TEMPLATE.render_quiz({
                title: 'My awesome quiz',
                items: [
                {
                    type: 'open_question',
                    title: 'Lorem ipsum?'
                },
                {
                    type: 'open_question',
                    title: 'Lorem ipsum?'
                }
                ]
            }));
        });
    }
});