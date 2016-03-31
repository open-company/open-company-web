# [OpenCompany](https://opencompany.io) Web Application

[![MPL License](http://img.shields.io/badge/license-MPL-blue.svg?style=flat)](https://www.mozilla.org/MPL/2.0/)
[![Build Status](https://travis-ci.org/open-company/open-company-web.svg?branch=master)](https://travis-ci.org/open-company/open-company-web)
[![Dependencies Status](https://jarkeeper.com/open-company/open-company-web/status.svg)](https://jarkeeper.com/open-company/open-company-web)
[![Roadmap on Trello](http://img.shields.io/badge/roadmap-trello-blue.svg?style=flat)](https://trello.com/b/3naVWHgZ/open-company-development)


## Overview

> Transparency, honesty, kindness, good stewardship, even humor, work in businesses at all times.

> -- [John Gerzema](http://www.johngerzema.com/)

Employees and investors, co-founders and execs, they all want more transparency from their startups, but there's no consensus about what it means to be transparent. OpenCompany is a platform that simplifies how key business information is shared with stakeholders.

When information about growth, finances, ownership and challenges is shared transparently, it inspires trust, new ideas and new levels of stakeholder engagement. OpenCompany makes it easy for founders to engage with employees and investors, creating a sense of ownership and urgency for everyone.

[OpenCompany](https://opencompany.io) is GitHub for the rest of your company.

To maintain transparency, OpenCompany information is always accessible and easy to find. Being able to search or flip through prior updates empowers everyone. Historical context brings new employees and investors up to speed, refreshes memories, and shows how the company is evolving over time.

Transparency expectations are changing. Startups need to change as well if they are going to attract and retain savvy employees and investors. Just as open source changed the way we build software, transparency changes how we build successful startups with information that is open, interactive, and always accessible. The OpenCompany platform turns transparency into a competitive advantage.

Like the open companies we promote and support, the [OpenCompany](https://opencompany.io) platform is completely transparent. The company supporting this effort, OpenCompany, Inc., is an open company. The [platform](https://github.com/open-company/open-company-web) is open source software, and open company data is [open data](https://en.wikipedia.org/wiki/Open_data) accessible through the [platform API](https://github.com/open-company/open-company-api).

To get started, head to: [OpenCompany](https://opencompany.io)


## Local Setup

Users of the [OpenCompany](https://opencompany.io) platform should get started by going to [OpenCompany](https://opencompany.io). The following local setup is for developers wanting to work on the platform's Web application software.

Most of the dependencies are internal, meaning [Boot](https://github.com/boot-clj/boot) will handle getting them for you. There are a few exceptions:

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) - a Java 8 JRE is needed to run Clojure
* [Boot](https://github.com/boot-clj/boot) - A Clojure build and dependency management tool

Chances are your system already has Java 8 installed. You can verify this with:

```console
java -version
```

If you do not have Java 8 [download it](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and follow the installation instructions.

Installing Boot is easy, for the most up to date instructions, check out the [Boot README](https://github.com/boot-clj/boot#install)

## Usage

Users of the [OpenCompany](https://opencompany.io) platform should get started by going to [OpenCompany](https://opencompany.io). The following usage is for developers wanting to work on the platform's Web application software.

Local usage requires you to run 2 services, the [API service](https://github.com/open-company/open-company-api) and the [Auth service](https://github.com/open-company/open-company-auth). Both can be started in their respective repositiories with:

```console
lein start
```

**Development**: To get an interactive web development environment, start the iterative compilation process in this repository:

```console
boot dev
```

If you've done it right, you'll have 3 terminal sessions running: API, Auth, Web.

Open your browser to [http://localhost:3559/](http://localhost:3559/).

**Production**: To create a production build run:

```console
boot prod-build
```

Open `target/public/app-shell.html` in your browser. You will not get live reloading nor a REPL.


## Tests

Install [PhantomJS](https://http://phantomjs.org/) downloading the latest binary [here](https://github.com/eugene1g/phantomjs/releases), the one from their site is currently broken.

Then move the `phantomjs` binary somewhere reachable by your `PATH` so you can run:

```console
phantomjs -v
```

Then run:

```console
boot test!
```

For more info on testing:

- Plugin: [bensu/doo](https://github.com/bensu/doo)
- React simulate wrapper: [bensu/cljs-react-test](https://github.com/bensu/cljs-react-test)


## Participation

Please note that this project is released with a [Contributor Code of Conduct](https://github.com/open-company/open-company-web/blob/mainline/CODE-OF-CONDUCT.md). By participating in this project you agree to abide by its terms.


## License

Distributed under the [Mozilla Public License v2.0](http://www.mozilla.org/MPL/2.0/).

Copyright © 2015-2016 OpenCompany, Inc.
