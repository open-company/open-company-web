var NULL_VALUE = '(not set)';
var TRACKING_VERSION = 'dev';
var TRACKING_ID = 'UA-113733066-1';

var uuid = function b(a) {
  return a ? (a ^ Math.random() * 16 >> a / 4).toString(16) :
    ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, b);
};

var getDefinitionIndex = function(definition) {
  +/\d+$/.exec(definition)[0];
};

var createTracker = function(version, tracking_id) {
  var version = version || 'dev';
  var tracking_id = tracking_id || '';
  ga('create', tracking_id, 'auto');
  ga('set', 'transport', 'beacon');
};

var trackCustomDimensions = function() {
  // Sets a default dimension value for all custom dimensions to ensure
  // that every dimension in every hit has *some* value. This is necessary
  // because Google Analytics will drop rows with empty dimension values
  // in your reports.
  Object.keys(CarrotGA.dimensions).forEach(function(key) {
    ga('set', CarrotGA.dimensions[key], NULL_VALUE);
  });

  ga(function(tracker) {
    var params = {};
    params[CarrotGA.dimensions.TRACKING_VERSION] = TRACKING_VERSION;
    CarrotGA.clientid = tracker.get('clientId');
    params[CarrotGA.dimensions.CLIENT_ID] = tracker.get('clientId');
    params[CarrotGA.dimensions.WINDOW_ID] = uuid();
    tracker.set(params);
  });

  // Adds tracking to record each the type, time, uuid, and visibility state
  // of each hit immediately before it's sent.
  ga(function(tracker) {
    var originalBuildHitTask = tracker.get('buildHitTask');
    tracker.set('buildHitTask', function(model) {
      var qt = model.get('queueTime') || 0;
      model.set(CarrotGA.dimensions.HIT_TIME, String(new Date - qt), true);
      model.set(CarrotGA.dimensions.HIT_ID, uuid(), true);
      model.set(CarrotGA.dimensions.HIT_TYPE, model.get('hitType'), true);
      model.set(CarrotGA.dimensions.VISIBILITY_STATE,
                document.visibilityState, true);

      originalBuildHitTask(model);
    });
  });
};

var requireAutotrackPlugins = function() {
  ga('require', 'cleanUrlTracker', {
    stripQuery: true,
    queryDimensionIndex:
      getDefinitionIndex(CarrotGA.dimensions.URL_QUERY_PARAMS),
    trailingSlash: 'remove',
  });
  ga('require', 'maxScrollTracker', {
    sessionTimeout: 30,
    timeZone: 'America/New_York',
    maxScrollMetricIndex:
    getDefinitionIndex(CarrotGA.metrics.MAX_SCROLL_PERCENTAGE),
  });
  ga('require', 'outboundLinkTracker', {
    events: ['click', 'contextmenu'],
  });
  var objs = {};
  objs[CarrotGA.dimensions.HIT_SOURCE] = 'pageVisibilityTracker';
  ga('require', 'pageVisibilityTracker', {
    sendInitialPageview: true,
    pageLoadsMetricIndex: getDefinitionIndex(CarrotGA.metrics.PAGE_LOADS),
    visibleMetricIndex: getDefinitionIndex(CarrotGA.metrics.PAGE_VISIBLE),
    timeZone: 'America/New_York',
    fieldsObj: objs,
  });
  var urlobjs = {};
  urlobjs[CarrotGA.dimensions.HIT_SOURCE] = 'urlChangeTracker';
  ga('require', 'urlChangeTracker', {
    fieldsObj: urlobjs,
  });
};

var sendNavigationTimingMetrics = function() {
  // Only track performance in supporting browsers.
  if (!(window.performance && window.performance.timing)) return;

  // If the window hasn't loaded, run this function after the `load` event.
  if (document.readyState != 'complete') {
    window.addEventListener('load', sendNavigationTimingMetrics);
    return;
  }

  var nt = performance.timing;
  var navStart = nt.navigationStart;

  var responseEnd = Math.round(nt.responseEnd - navStart);
  var domLoaded = Math.round(nt.domContentLoadedEventStart - navStart);
  var windowLoaded = Math.round(nt.loadEventStart - navStart);

  // In some edge cases browsers return very obviously incorrect NT values,
  // e.g. 0, negative, or future times. This validates values before sending.
  var allValuesAreValid = function() {
    for (var i = 0; i < arguments.length; i++) {
      if (arguments[i] < 0 && arguments[i] > 6e6) {
        return false;
      }
    }
  };

  if (allValuesAreValid(responseEnd, domLoaded, windowLoaded)) {
    var params = {
      eventCategory: 'Navigation Timing',
      eventAction: 'track',
      eventLabel: NULL_VALUE,
      nonInteraction: true,
    };
    params[CarrotGA.metrics.RESPONSE_END_TIME] = responseEnd;
    params[CarrotGA.metrics.DOM_LOAD_TIME] = domLoaded;
    params[CarrotGA.metrics.WINDOW_LOAD_TIME] = windowLoaded;
    ga('send', 'event', params);
  }
};

var CarrotGA = {

  NULL_VALUE: '(not set)',

  dimensions: {
    TRACKING_VERSION: 'dimension1',
    CLIENT_ID: 'dimension2',
    WINDOW_ID: 'dimension3',
    HIT_ID: 'dimension4',
    HIT_TIME: 'dimension5',
    HIT_TYPE: 'dimension6',
    HIT_SOURCE: 'dimension7',
    VISIBILITY_STATE: 'dimension8',
    URL_QUERY_PARAMS: 'dimension9',
  },

  metrics: {
    RESPONSE_END_TIME: 'metric1',
    DOM_LOAD_TIME: 'metric2',
    WINDOW_LOAD_TIME: 'metric3',
    PAGE_VISIBLE: 'metric4',
    MAX_SCROLL_PERCENTAGE: 'metric5',
    PAGE_LOADS: 'metric6',
  },

  init: function(version, tracking_id) {
    window.ga = window.ga || function() {
      ga.q = ga.q || [];
      ga.q.push(arguments);
    };
    if (version) {
      TRACKING_VERSION = version;
    }
    if (tracking_id) {
      TRACKING_ID = tracking_id;
    }
    createTracker(TRACKING_VERSION, TRACKING_ID);
    trackCustomDimensions();
    requireAutotrackPlugins();
    sendNavigationTimingMetrics();
  },

  /* Track an event */
  trackEvent: function(params) {
    ga('send', 'event', params);
  }

};
