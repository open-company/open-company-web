# [OpenCompany.io](https://opencompany.io) Web Application

[![MPL License](http://img.shields.io/badge/license-MPL-blue.svg?style=flat)](https://www.mozilla.org/MPL/2.0/)
[![Roadmap on Trello](http://img.shields.io/badge/roadmap-trello-blue.svg?style=flat)](https://trello.com/b/3naVWHgZ/open-company-development)
[![Build Status](https://travis-ci.org/open-company/open-company-web.svg?branch=master)](https://travis-ci.org/open-company/open-company-web)

## Overview

> Transparency, honesty, kindness, good stewardship, even humor, work in businesses at all times.

> -- [John Gerzema](http://www.johngerzema.com/)

Build your company in the open with transparency for your co-founders, your team, and your investors. Or share your company's journey with everyone, your customers and the rest of the startup community too.

[OpenCompany.io](https://opencompany.io) is GitHub for the rest of your company:

* **Dashboard** - An easy tool for founders to provide transparency to their teams and beyond.
* **Founders' Guide** - Tools, best practices and insights from open company founders and their companies.
* **Open Company Directory** - Founders sharing with their teams and beyond.
* **Community** - Spread the word and knowledge and inspire more founders to open up.

Like the open companies we promote and support, the [OpenCompany.io](https://opencompany.io) platform is completely transparent. The company supporting this effort, Transparency, LLC, is an open company. The platform is open source software, and open company data is [open data](https://en.wikipedia.org/wiki/Open_data) accessible through the [platform API](https://github.com/open-company/open-company-api).

To get started, head to [OpenCompany.io](https://opencompany.io).

## Local Setup

Users of the [OpenCompany.io](https://opencompany.io) platform should get started by going to [OpenCompany.io](https://opencompany.io). The following local setup is for developers wanting to work on the platform's Web application software.

Most of the dependencies are internal, meaning [Leiningen](https://github.com/technomancy/leiningen) will handle getting them for you. There are a few exceptions:

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) - a Java 8 JRE is needed to run Clojure
* [Leiningen](https://github.com/technomancy/leiningen) - Clojure's build and dependency management tool

Chances are your system already has Java 8 installed. You can verify this with:

```console
java -version
```

If you do not have Java 8 [download it](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and follow the installation instructions.

Leiningen is easy to install:

1. Download the latest [lein script from the stable branch](https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein).
1. Place it somewhere that's on your $PATH (`env | grep PATH`). `/usr/local/bin` is a good choice if it is on your PATH.
1. Set it to be executable. `chmod 755 /usr/local/bin/lein`
1. Run it: `lein` This will finish the installation.

Then let Leiningen install the rest of the dependencies:

```console
git clone https://github.com/open-company/open-company-web.git
cd open-company-web
lein deps
```

## Usage

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

Open your browser in `resources/public/index.html`. You will not get live reloading nor a REPL.

## Tests

Install [PhantomJS](https://http://phantomjs.org/) downloading the latest binary [here](https://github.com/eugene1g/phantomjs/releases), the one from their site is currently broken.
Then move the binary somewhere reachable by your `PATH` so you can run:

```console
phantomjs -v
```

Then run:

```console
lein test!
```

For more info on testing:

- Plugin: [bensu/doo](https://github.com/bensu/doo)
- React simulate wrapper: [bensu/cljs-react-test](https://github.com/bensu/doo)

## License

Distributed under the [Mozilla Public License v2.0](http://www.mozilla.org/MPL/2.0/).

Copyright © 2015 Transparency, LLC
