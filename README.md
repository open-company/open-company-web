# OpenCompany.io Web Application

## Overview


## Setup

To get an interactive development environment run:

```console
lein figwheel
```

Open your browser to [http://localhost:3449/](http://localhost:3449/).

This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to test it is:

```clojure
(js/alert "Am I connected?")
```

You should see an alert in the browser window.

To clean all compiled files:

```console
lein clean
```

To create a production build run:

```console
lein cljsbuild once min
```

Open your browser in `resources/public/index.html`. You will not
get live reloading nor a REPL. 

## License

Distributed under the [Mozilla Public License v2.0](http://www.mozilla.org/MPL/2.0/).

Copyright © 2015 Transparency, LLC