module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    uglify: {
      build: {
        src: ['js/vendor/dependencies/jquery.min.js', 'js/vendor/dependencies/jquery-ui.min.js' ,'js/vendor/dependencies/angular.min.js','js/vendor/dependencies/*.js', 'js/app/app.js', 'js/app/services/*.js', 'js/app/directives/*.js', 'js/app/controllers/*.js'],
        dest: 'js/quiznator.min.js'
      }
    },
    less: {
      development: {
        options: {
          compress: true,
          yuicompress: true,
          optimization: 2
        },
        files: {
          "css/site.min.css": "css/less/site.less"
        }
      }
    },
    watch: {
      files: ['js/app.js', 'js/services/*.js', 'js/directives/*.js', 'js/controllers/*.js', 'css/quiz.less'],
      tasks: ['uglify', 'less', 'cssmin']
    },
    cssmin: {
    combine: {
      files: {
        'css/site.min.css': ['css/site.min.css', 'css/vendor/*.css']
      }
    }
  }
  });

  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-uglify');

  // Default task(s).
  grunt.registerTask('default', ['uglify', 'less', 'cssmin', 'watch']);

};