# [OpenCompany](https://opencompany.com/) Web Application

[![MPL License](http://img.shields.io/badge/license-MPL-blue.svg?style=flat)](https://www.mozilla.org/MPL/2.0/)
[![Build Status](https://travis-ci.org/open-company/open-company-web.svg?branch=master)](https://travis-ci.org/open-company/open-company-web)
[![Roadmap on Trello](http://img.shields.io/badge/roadmap-trello-blue.svg?style=flat)](https://trello.com/b/3naVWHgZ/open-company-development)


## Background

> Transparency, honesty, kindness, good stewardship, even humor, work in businesses at all times.

> -- [John Gerzema](http://www.johngerzema.com/)

Employees and investors, co-founders and execs, they all want more transparency from their startups, but there's no consensus about what it means to be transparent. OpenCompany is a platform that simplifies how key business information is shared with stakeholders.

When information about growth, finances, ownership and challenges is shared transparently, it inspires trust, new ideas and new levels of stakeholder engagement. OpenCompany makes it easy for founders to engage with employees and investors, creating a sense of ownership and urgency for everyone.

[OpenCompany](https://opencompany.com/) is GitHub for the rest of your company.

To maintain transparency, OpenCompany information is always accessible and easy to find. Being able to search or flip through prior updates empowers everyone. Historical context brings new employees and investors up to speed, refreshes memories, and shows how the company is evolving over time.

Transparency expectations are changing. Startups need to change as well if they are going to attract and retain savvy employees and investors. Just as open source changed the way we build software, transparency changes how we build successful startups with information that is open, interactive, and always accessible. The OpenCompany platform turns transparency into a competitive advantage.

Like the open companies we promote and support, the [OpenCompany](https://opencompany.com/) platform is completely transparent. The company supporting this effort, OpenCompany, LLC, is an open company. The [platform](https://github.com/open-company/open-company-web) is open source software, and open company data is [open data](https://en.wikipedia.org/wiki/Open_data) accessible through the [platform API](https://github.com/open-company/open-company-api).

To get started, head to: [OpenCompany](https://opencompany.com/)


## Overview

The OpenCompany Web Application provides a Web UI for creating and consuming open company content and data.

![OpenCompany Screenshot](https://open-company-assets.s3.amazonaws.com/oc-screenshot.png)

### Browser Support

Latest Chrome, Firefox, Safari and IE Edge.


## Local Setup

Users of the [OpenCompany](https://opencompany.com/) platform should get started by going to [OpenCompany](https://opencompany.com/). The following local setup is for developers wanting to work on the platform's Web application software.

Most of the dependencies are internal, meaning [Boot](https://github.com/boot-clj/boot) will handle getting them for you. There are a few exceptions:

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) - a Java 8 JRE is needed to run Clojure
* [Boot](https://github.com/boot-clj/boot) - A Clojure build and dependency management tool

#### Java

Chances are your system already has Java 8+ installed. You can verify this with:

```console
java -version
```

If you do not have Java 8+ [download it](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and follow the installation instructions.

#### Boot

Installing Boot is easy, for the most up to date instructions, check out the [Boot README](https://github.com/boot-clj/boot#install).
You can verify your install with:

```console
boot -V
```


## Usage

Users of the [OpenCompany](https://opencompany.com/) platform should get started by going to [OpenCompany](https://opencompany.com/). The following usage is for developers wanting to work on the platform's Web application software.

Local usage of the web application requires you to run 2 services, the [API service](https://github.com/open-company/open-company-api) and the [Auth service](https://github.com/open-company/open-company-auth). Both can be started in their respective repositiories with:

```console
lein start
```

To get an interactive web **development** environment, start the iterative compilation process in this repository:

```console
boot dev
```

If you've done it right, you'll now have 3 terminal sessions running: API, Auth, Web.

Open your browser to [http://localhost:3559/](http://localhost:3559/).

To create a **production** build run:

```console
boot prod-build
```

Open `target/public/app-shell.html` in your browser. Using production rather than dev, you will not get the live reloading nor a REPL.

### Project REPL

To have a ClojureScript REPL connected to the browser, first start the dev task:

```console
$ boot dev
```

If you get an error like this:

```console
clojure.lang.Compiler$CompilerException: java.lang.RuntimeException: No such var: string/index-of, compiling:(cljs/source_map.clj:260:52)
             java.lang.RuntimeException: No such var: string/index-of
```

Make sure you have [clojure 1.7+](https://github.com/adzerk-oss/boot-cljs-repl#boot-cljs-repl) in build.boot, in the project boot settings [boot.properties](boot.properties) and in your local setup: `~/.boot/boot.properties`.

Next open a browser window: [http://localhost:3559/](http://localhost:3559/)

Then in another terminal window start a REPL with boot:

```console
$ boot repl -c
```

At this point, you can start a weasel server to connect to the browser window:

```console
cljs.user=> (start-repl)
```

If the REPL doesn't connect to the browser window, refresh the page in the browser.

At this point, you should be able to use the project's namespaces:

```console
cljs.user=> (require '[open-company-web.lib.utils :as utils])
nil
cljs.user=> (utils/vec-dissoc [:a :b :c] :a)
[:b :c]
```

## Techincal Design

### App State Design

The app-state has the following structure:

```clojure
{
  :buffer {
     :company-data {
       :name "Buffer"
       :slug "buffer"
       ;; ... other data form /companies/buffer ....
     }
     :su-list {
        :collection {
          ;; .. data from /companies/buffer/updates...
        }
     }
     :buffer-update-march-2016 {
       ;; ... data from /companies/buffer/buffer-update-march-2016 ...
     }
}
```

Never access the app-state directly but we should always use the proper functions in the `dispatcher`.
This way it will be very simple to change the structure of the app-state in the future since we just need
to change it in one place.

### Component Tree Design

The OpenCompany web app is made up of trees of Om/Rum components that start from a few top level components (company-dashboard, company-settings, user-settings, company-list, login...) and include other child components in a tree, sometimes just a few, and sometimes many.

One of the most important component trees is the company dashboard. This provides the main UI for viewing and editing a company's topics. Here's a diagram of the read-only aspects of the company dashboard component tree:

![Company Dashboard Diagram](https://cdn.rawgit.com/open-company/open-company-web/mainline/docs/dashboard-viewing-component-tree.svg)


## Testing

Install [PhantomJS](https://http://phantomjs.org/) downloading the latest 2.x binary from [here](https://github.com/eugene1g/phantomjs/releases), the one from their site is currently broken.

Then move the `phantomjs` binary somewhere reachable by your `PATH` so you can run:

```console
phantomjs -v
```

Then run:

```console
boot test!
```

For more info on testing:

- React simulate wrapper: [bensu/cljs-react-test](https://github.com/bensu/cljs-react-test)


## Participation

Please note that this project is released with a [Contributor Code of Conduct](https://github.com/open-company/open-company-web/blob/mainline/CODE-OF-CONDUCT.md). By participating in this project you agree to abide by its terms.


## License

Distributed under the [Mozilla Public License v2.0](http://www.mozilla.org/MPL/2.0/).

Copyright © 2015-2016 OpenCompany, LLC.