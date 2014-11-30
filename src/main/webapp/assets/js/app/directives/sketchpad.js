QuizApp.directive('sketchpad', function(){
  var self = {};

  var _raphael = null;

  var _initialize_sketch = function(sketchpad, sketch){
    creators = {
      text: function(el){
        var t = sketchpad.add_text(el.text);
        t.attr({ 'font-size': el['font-size'], 'font-family': 'Arial, Helvetica, sans-serif', 'fill': el['fill'], 'text-anchor': 'start', 'x': el['x'], 'y': el['y'] });
        t.translate(0, t.getBBox().height/2);
      },
      path: function(el){
        console.log('path:' + el['path']);
        var p = _raphael.path(el['path']);
        p.attr({ 'stroke-width': el['stroke-width'], 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke': el['stroke'] });
      },
      rectangle: function(el){
        var r = sketchpad.add_rectangle();
        r.attr({ 'fill': 'white', 'fill-opacity': 0, 'stroke': el['stroke'], 'stroke-width': el['stroke-width'], 'x': el['x'], 'y': el['y'], 'width': el['width'], 'height': el['height'] });

      }
    };

    var elements = angular.fromJson(sketch).paper;

    elements.forEach(function(el){
      if(typeof creators[el.type] === 'function'){
        creators[el.type](el);
      }
    });
  }

  var _initialize_toolbar = function(sketchpad, elem){
    var toolbar = $(elem).siblings('.sketchpad-toolbar')[0];

    $(toolbar).find('.palette-color').on('click', function(){
      var palette_colors = $(this).parent('.palette-container').children('.palette-color');

      $(palette_colors).removeClass('chosen');
      $(this).addClass('chosen');

      sketchpad.pen().color($(this).css('background-color'));
    });

    $(toolbar).find('.stroke-width-delimeter').on('click', function(){
      var stroke_width_delimeters = $(this).parent('.stroke-width-container').children('.stroke-width-delimeter');
      var stroke_width = parseInt($(this).attr('data-width'));

      $(stroke_width_delimeters).removeClass('chosen')
      $(this).addClass('chosen');

      sketchpad.pen().width(stroke_width);
    });

    $(toolbar).find('.add-rectangle-to-sketchpad button').on('click', function(){
      var stroke_width = parseInt($('.stroke-width-delimeter.chosen').attr('data-width'));

      sketchpad.add_rectangle(stroke_width);
    });

    $(toolbar).find('.sketchpad-clear').on('click', function(){
      sketchpad.clear();
    });

    $(toolbar).find('.add-text-to-sketchpad button').on('click', function(){
      var textarea = $(this).parent('.add-text-to-sketchpad').find('textarea');
      var stroke_width = parseInt($('.stroke-width-delimeter.chosen').attr('data-width'));
      var font_size = 16;

      if(stroke_width == 1){
        font_size = 10;
      }else if(stroke_width == 9){
        font_size = 22;
      }

      sketchpad.add_text($(textarea).val(), font_size);
      $(textarea).val('');
    });
  }

  self.scope = { sketch: '=ngModel' };

  self.link = function(scope, elem, attrs){
    _raphael = Raphael($(elem)[0], 450, 450);

    var sketchpad = Raphael.sketchpad(_raphael, {
      width: 450,
      height: 450,
      editing: true
    });

    $(elem).find('svg').css({
      border: '1px solid rgb(220,220,220)'
    });

    _initialize_toolbar(sketchpad, elem);

    if(scope.sketch){
      _initialize_sketch(sketchpad, scope.sketch);
    }

    sketchpad.change(function() {
      console.log('change')
      scope.$apply(function(){
        var elements = sketchpad.elements();
        scope.sketch = elements;
      });
    });
  }

  return self;
});
