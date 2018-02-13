Our analytics can be seen at https://analytics.google.com/

Following this article https://philipwalton.com/articles/the-google-analytics-setup-i-use-on-every-site-i-build/

Only a few of the custom dimensions and metrics in the article were chosen. These should help us use google analytics.

Each of the custom dimensions and metrics have been created in google analytics and can be seen in the admin section.

#### Dimensions

* TRACKING_VERSION: is the deploy version and can be used to generate reports for only a certain code release.
* CLIENT_ID: Google Analytics uses a client ID to associate individual hits with a particular user. 
* WINDOW_ID: our code generates a uuid for each open window.
* HIT_ID: The hit stats can be used to track individual interactions by a user.
* HIT_TIME
* HIT_TYPE
* HIT_SOURCE
* VISIBILITY_STATE: tracks if the page was visible by the user. this stat is from the browser.
* URL_QUERY_PARAMS: The query params with a request.

#### Metrics

* RESPONSE_END_TIME: the point when the server finishes delivering the response to the browser.
* DOM_LOAD_TIME: when all the page’s content is processed and loaded in the DOM, and the browser can start rendering the page.
* WINDOW_LOAD_TIME: when all the page’s initial resources are loaded.
* PAGE_VISIBLE: how often the page was visible to the user.
* MAX_SCROLL_PERCENTAGE: How far did a user scroll on the page.
* PAGE_LOADS: How often did a page load.

#### Code
[CarrogGA code](../resources/public/lib/autotrack/google-analytics.js)

```javascript
var CarrotGA = {

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
```
