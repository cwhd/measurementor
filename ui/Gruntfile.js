/*jslint node: true */
'use strict';

var pkg = require('./package.json');

//Using exclusion patterns slows down Grunt significantly
//instead of creating a set of patterns like '**/*.js' and '!**/node_modules/**'
//this method is used to create a set of inclusive patterns for all subdirectories
//skipping node_modules, bower_components, dist, and any .dirs
//This enables users to create any directory structure they desire.
var createFolderGlobs = function(fileTypePatterns) {
    fileTypePatterns = Array.isArray(fileTypePatterns) ? fileTypePatterns : [fileTypePatterns];
    var ignore = ['node_modules', 'bower_components', 'dist', 'temp'];
    var fs = require('fs');
    return fs.readdirSync(process.cwd())
        .map(function(file) {
            if (ignore.indexOf(file) !== -1 ||
                file.indexOf('.') === 0 ||
                !fs.lstatSync(file).isDirectory()) {
                return null;
            } else {
                return fileTypePatterns.map(function(pattern) {
                    return file + '/**/' + pattern;
                });
            }
        })
        .filter(function(patterns) {
            return patterns;
        })
        .concat(fileTypePatterns);
};

module.exports = function(grunt) {

    // load all grunt tasks
    require('load-grunt-tasks')(grunt);

    // Project configuration.
    grunt.initConfig({
        connect: {
            main: {
                options: {
                    port: 9001
                }
            }
        },
        watch: {
            main: {
                options: {
                    livereload: true,
                    livereloadOnError: false,
                    spawn: false
                },
                files: [createFolderGlobs(['*.js', '*.less', '*.html', '*.ts']), '!_SpecRunner.html', '!.grunt'],
                tasks: [] //all the tasks are run dynamically during the watch event handler
            }
        },
        jshint: {
            main: {
                options: {
                    jshintrc: '.jshintrc'
                },
                src: ['build/*.js', "build/app/generalLayout/*.js", 'build/app/generalLayout/partial/*/*js', 'build/app/generalLayout/service/*js', 'build/app/generalLayout/filter/*js', 'build/app/generalLayout/filter/*js', 'build/app/jobs-config/partial/*/*js',
                    'build/app/jobs-config/service/*js', 'build/app/user-management/partial/*/*js', 'build/app/user-management/service/*js'
                ]
            }
        },
        clean: {
            before: {
                src: ['dist', 'temp']
            },
            after: {
                src: ['temp']
            }
        },
        less: {
            production: {
                options: {},
                files: {
                    'temp/app.css': 'app.less'
                }
            }
        },
        ngtemplates: {
            main: {
                options: {
                    module: pkg.name,
                    htmlmin: '<%= htmlmin.main.options %>'
                },
                src: [createFolderGlobs('*.html'), '!index.html', '!_SpecRunner.html'],
                dest: 'temp/templates.js'
            }
        },
        copy: {
            main: {
                files: [{
                    src: ['images/**'],
                    dest: 'dist/'
                }, {
                    src: ['bower_components/font-awesome/fonts/**'],
                    dest: 'dist/',
                    filter: 'isFile',
                    expand: true
                }, {
                    src: ['bower_components/bootstrap/fonts/**'],
                    dest: 'dist/',
                    filter: 'isFile',
                    expand: true
                }]
            }
        },
        dom_munger: {
            read: {
                options: {
                    read: [{
                        selector: 'script[data-concat!="false"]',
                        attribute: 'src',
                        writeto: 'appjs'
                    }, {
                        selector: 'link[rel="stylesheet"][data-concat!="false"]',
                        attribute: 'href',
                        writeto: 'appcss'
                    }]
                },
                src: 'index.html'
            },
            update: {
                options: {
                    remove: ['script[data-remove!="false"]', 'link[data-remove!="false"]'],
                    append: [{
                        selector: 'body',
                        html: '<script src="app.full.min.js"></script>'
                    }, {
                        selector: 'head',
                        html: '<link rel="stylesheet" href="app.full.min.css">'
                    }]
                },
                src: 'index.html',
                dest: 'dist/index.html'
            }
        },
        cssmin: {
            main: {
                src: ['temp/app.css', '<%= dom_munger.data.appcss %>'],
                dest: 'dist/app.full.min.css'
            }
        },
        concat: {
            main: {
                src: ['<%= dom_munger.data.appjs %>', '<%= ngtemplates.main.dest %>'],
                dest: 'temp/app.full.js'
            }
        },
        ngAnnotate: {
            main: {
                src: 'temp/app.full.js',
                dest: 'temp/app.full.js'
            }
        },
        uglify: {
            main: {
                src: 'temp/app.full.js',
                dest: 'dist/app.full.min.js'
            }
        },
        htmlmin: {
            main: {
                options: {
                    collapseBooleanAttributes: true,
                    collapseWhitespace: true,
                    removeAttributeQuotes: true,
                    removeComments: true,
                    removeEmptyAttributes: true,
                    removeScriptTypeAttributes: true,
                    removeStyleLinkTypeAttributes: true
                },
                files: {
                    'dist/index.html': 'dist/index.html'
                }
            }
        },
        //Imagemin has issues on Windows.  
        //To enable imagemin:
        // - "npm install grunt-contrib-imagemin"
        // - Comment in this section
        // - Add the "imagemin" task after the "htmlmin" task in the build task alias
        // imagemin: {
        //   main:{
        //     files: [{
        //       expand: true, cwd:'dist/',
        //       src:['**/{*.png,*.jpg}'],
        //       dest: 'dist/'
        //     }]
        //   }
        typescript: {
            base: {
                src: ['app/*/partial/*/*.ts', 'app/*/service/*.ts', 'app/*/filter/*.ts', 'app/*/*.ts', '*.ts', 'protractor/*.ts'],
                dest: 'build',
                options: {
                    module: 'amd', //or commonjs 
                    target: 'es5', //or es3 
                    basePath: '',
                    sourceMap: false,
                    declaration: false,
                    keepDirectoryHierarchy: true
                }
            },
            during_watch: {
                src: ['app/*/partial/*/*.ts', 'app/*/service/*.ts', 'app/*/filter/*.ts', 'app/*/*.ts', '*.ts', 'protractor/*.ts'],
                dest: '../public/build',
                options: {
                    module: 'amd', //or commonjs 
                    target: 'es5', //or es3 
                    basePath: '',
                    sourceMap: false,
                    declaration: false,
                    keepDirectoryHierarchy: true
                }
            },
            //  during_watch
        },

        // less: {
        //     during_watch: {
        //         options: {
        //             paths: ["assets/css"]
        //         },
        //         files: {
        //             "path/to/result.css": "path/to/source.less"
        //         }
        //     },
        // },

        karma: {
            options: {
                frameworks: ['jasmine'],
                files: [ //this files data is also updated in the watch handler, if updated change there too
                    '<%= dom_munger.data.appjs %>',
                    'bower_components/angular-mocks/angular-mocks.js',
                    createFolderGlobs('*-spec.js')
                ],
                logLevel: 'LOG_DEBUG',
                autoWatch: false, //watching is handled by grunt-contrib-watch
                singleRun: true,

                preprocessors: {
                    'build/app/general-layout/partial/*/!(*spec*)': 'coverage',
                    'build/app/general-layout/service/!(*spec*)': 'coverage',
                    'build/app/general-layout/filter/!(*spec*)': 'coverage',
                    'build/app/jobs-config/partial/*/!(*spec*)': 'coverage',
                    'build/app/jobs-config/service/!(*spec*)': 'coverage',
                    'build/app/user-management/partial/*/!(*spec*)': 'coverage',
                    'build/app/user-management/service/!(*spec*)': 'coverage'
                },
                reporters: ['dots', 'junit', 'mocha', 'coverage'],
                junitReporter: {
                    outputFile: 'test-results.xml'
                },
                coverageReporter: {
                    type: 'html',
                    dir: 'coverage/'
                }
            },
            all_tests: {
                browsers: ['PhantomJS']
            },
            during_watch: {
                browsers: ['PhantomJS']
            },
        },
    });

    //grunt.registerTask('build',['jshint','clean:before','less','dom_munger','ngtemplates','cssmin','concat','ngAnnotate','uglify','copy','htmlmin','clean:after']);
    grunt.registerTask('build', ['typescript:base', 'jshint', 'clean:before', 'less', 'dom_munger', 'ngtemplates', 'cssmin', 'concat', 'ngAnnotate', 'uglify', 'copy', 'htmlmin', 'clean:after']);
    grunt.registerTask('serve', ['dom_munger:read', 'jshint', 'connect', 'typescript:during_watch', 'watch']);
    grunt.registerTask('test', ['dom_munger:read', 'karma:all_tests']);

    grunt.registerTask('ts', ['typescript:during_watch']);

    grunt.event.on('watch', function(action, filepath) {
        //https://github.com/gruntjs/grunt-contrib-watch/issues/156

        var tasksToRun = [];

        if (filepath.lastIndexOf('.ts') !== -1) {
            grunt.config('typescript.base.src', filepath);
            grunt.config('typescript.options.keepDirectoryHierarchy', true);

            grunt.config('typescript.base.dest', '../public/build');
            tasksToRun.push('ts');

            //find the appropriate unit test for the changed file
            var spec = filepath;
            if (filepath.lastIndexOf('-spec.ts') === -1) {
                spec = "../public/build/" + filepath.substring(0, filepath.length - 3) + '-spec.js';
            }

            //if the spec exists then lets run it
            if (grunt.file.exists(spec)) {
                var files = [].concat(grunt.config('dom_munger.data.appjs'));
                files.push('bower_components/angular-mocks/angular-mocks.js');
                files.push(spec);
                grunt.config('karma.options.files', files);
                tasksToRun.push('karma:during_watch');
            }
        }

        if (filepath.lastIndexOf('starter-template.less') !== -1) {
            //var destPathForLessCompiler = "../public/test.css"; // + filepath.substring(0, filepath.length - 4) + "css";
            grunt.config('less.during_watch.files', {
                "../public/assets/css/starter-template.css": filepath
            });
            grunt.config('less.during_watch.verbose', true);
            tasksToRun.push('less:during_watch');
        }

        //if index.html changed, we need to reread the <script> tags so our next run of karma
        //will have the correct environment
        if (filepath === 'index.html') {
            tasksToRun.push('dom_munger:read');
        }

        grunt.config('copy.during_watch.files', [{
            src: filepath,
            dest: '../public/' + filepath
        }]);
        grunt.config('copy.during_watch.verbose', true);
        tasksToRun.push('copy:during_watch');

        grunt.config('watch.main.tasks', tasksToRun);
    });
};