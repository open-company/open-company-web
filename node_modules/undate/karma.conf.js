// Karma configuration
// Generated on Wed Jun 28 2017 10:30:27 GMT+0900 (JST)

const SUPPORTED_BROWSERS = [
  'Chrome',
  'Edge',
  'Firefox',
  'IE',
  'Opera',
  'Safari',
];

module.exports = function(config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '',


    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['mocha', 'detectBrowsers'],


    // list of files / patterns to load in the browser
    files: [
      'test/update.spec.js',
      'test/wrapCursor.spec.js',
    ],


    // list of files to exclude
    exclude: [
    ],


    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
      'test/update.spec.js': 'webpack',
      'test/wrapCursor.spec.js': 'webpack',
    },


    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress'],


    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: [],


    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: false,

    // Concurrency level
    // how many browser should be started simultaneous
    concurrency: Infinity,

    webpack: {
      module: {
        rules: [
          {
            test: /\.js(\.flow)?$/,
            exclude: /node_modules/,
            use: [
              {
                loader: 'babel-loader',
                options: {
                  presets: [
                    'flow',
                    'power-assert',
                  ]
                }
              }
            ]
          }
        ]
      }
    },

    detectBrowsers: {
      usePhantomJS: false,
      postDetection: (browsers) => {
        if (process.env.TRAVIS) {
          // TODO: Test with Chrome
          return ['Firefox'];
        } else {
          return browsers.filter(browser => SUPPORTED_BROWSERS.indexOf(browser) !== -1);
        }
      }
    },
  })
}
