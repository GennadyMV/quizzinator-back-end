QuizApp.directive('viewer', function(){
  return {
    scope: {
      sketch: '=ngModel'
    },
    link: function(scope, elem, attrs){
      var viewer = Raphael($(elem)[0], 450, 450);

      creators = {
        text: function(el){
          var t = viewer.text(el.x, el.y, el.text);
          t.attr({ 'font-size': el['font-size'], 'font-family': 'Arial, Helvetica, sans-serif', 'fill': el['fill'], 'text-anchor': 'start' });
          t.translate(0, t.getBBox().height/2);
        },
        path: function(el){
          var p = viewer.path(el['path']);
          p.attr({ 'stroke-width': el['stroke-width'], 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke': el['stroke'] });
        },
        rectangle: function(el){
          var r = viewer.rect(el['x'], el['y'], el['width'], el['height']);
          r.attr({ 'fill': 'white', 'fill-opacity': 0, 'stroke': el['stroke'], 'stroke-width': el['stroke-width'] });
        }
      };

      var elements = angular.fromJson(scope.sketch).paper;

      elements.forEach(function(el){
        if(typeof creators[el.type] === 'function'){
          creators[el.type](el);
        }
      });
    }
  }
});
