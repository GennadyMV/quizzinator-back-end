QuizApp.directive('datepicker', function(){
  return {
    scope: {
      date: '=ngModel'
    },
    link: function(scope, elem, attrs){
      $(elem).datepicker({
          format: 'yyyy-mm-dd'
      });
    }
  };
});
