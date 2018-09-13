# [OpenCompany](https://github.com/open-company) Web Application

[![Build Status](https://travis-ci.org/open-company/open-company-web.svg?branch=master)](https://travis-ci.org/open-company/open-company-web)

## Background

> Transparency, honesty, kindness, good stewardship, even humor, work in businesses at all times.

> -- [John Gerzema](http://www.johngerzema.com/)

Teams struggle to keep everyone on the same page. People are hyper-connected in the moment with chat and email, but it gets noisy as teams grow, and people miss key information. Everyone needs clear and consistent leadership, and the solution is surprisingly simple and effective - **great leadership updates that build transparency and alignment**.

With that in mind we designed [Carrot](https://carrot.io/), a software-as-a-service application powered by the open source [OpenCompany platform](https://github.com/open-company) and a source-available [web UI](https://github.com/open-company/open-company-web).

With Carrot, important company updates, announcements, stories, and strategic plans create focused, topic-based conversations that keep everyone aligned without interruptions. When information is shared transparently, it inspires trust, new ideas and new levels of stakeholder engagement. Carrot makes it easy for leaders to engage with employees, investors, and customers, creating alignment for everyone.

Transparency expectations are changing. Organizations need to change as well if they are going to attract and retain savvy teams, investors and customers. Just as open source changed the way we build software, transparency changes how we build successful companies with information that is open, interactive, and always accessible. Carrot turns transparency into a competitive advantage.

To get started, head to: [Carrot](https://carrot.io/)


## Overview

The OpenCompany Web Application provides a Web UI for creating and consuming open company content and data.

![OpenCompany Screenshot](https://open-company-assets.s3.amazonaws.com/new_homepage_screenshot.png)

### Browser Support

Latest Chrome, Firefox, and Safari.


## Local Setup

Prospective users of [Carrot](https://carrot.io/) should get started by going to [Carrot.io](https://carrot.io/). The following local setup is **for developers** wanting to work on the Web application.

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

Prospective users of [Carrot](https://carrot.io/) should get started by going to [Carrot.io](https://carrot.io/). The following usage is **for developers** wanting to work on the Web application.

Local usage of the web application requires you to run 3 services, the [Storage service](https://github.com/open-company/open-company-storage), the [Auth service](https://github.com/open-company/open-company-auth), and the [Interaction service](https://github.com/open-company/open-company-interaction). All can be started in their respective repositiories with:

```console
lein start
```

To get an interactive web **development** environment, start the iterative compilation process in this repository:

```console
boot dev
```

If you've done it right, you'll now have 4 terminal sessions running: Storage, Auth, Interaction and Web.

Open your browser to [http://localhost:3559/](http://localhost:3559/).

To create a **production** build run:

```console
boot prod-build target
```

Open `target/public/app-shell.html` in your browser. Using production rather than dev, you will not get the live reloading nor a REPL.

To get a jetty server along with the production build:

Download the google closure compiler.
https://developers.google.com/closure/compiler/

```console
mkdir ~/closure_compiler
mv <download location>/compiler_latest.zip ~/closure_compiler/
cd ~/closure_compiler
unzip compiler_latest.zip
```

```console
boot dev-advanced target
```

```console
script/compile_assets.sh <compiler version> ~/path/to/open-company-web localhost:3559
cp ~/path/to/open-company-web/target/public/oc_assets.js* ~/path/to/open-company-way/resources/public/
```

#### TCMention development

The extention for MediumEditor called TCMention is based on [this](https://github.com/tomchentw/medium-editor-tc-mention). To make changes to the extention itself clone [our fork](https://github.com/open-company/medium-editor-tc-mention) of that repo, change what you need, then run `npm run build` and run:
`cp  lib/* ../open-company-web/resources/public/lib/MediumEditorExtensions/MediumEditorTCMention/`.
Commit the changes back to our fork to make sure we have the latest.

If you need to change the mentions popup panels you can write the JSX code in resources/public/lib/MediumEditorExtensions/MediumEditorTCMention/CustomizedTagComponent.jsx or the JS code in CustomizedTagComponent.js. Better if keep them in sync so we can use both.


#### Ziggeo media player and recorder development

To change the Ziggeo video player and recorder you need to change [our fork](https://github.com/open-company/betajs-media-components) of [betajs/betajs-media-components](https://github.com/betajs/betajs-media-components).
Once you changed everything you need you only need to run this `source script/local_ziggeo_dev.sh` to compile the ziggeo sdk and copy it here.
Do not forget to commit the changes made to betajs-media-compoentns and the changed files here in oc-web.


### Project REPL

To have a ClojureScript REPL connected to the browser, first start the dev task:

```console
boot dev
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
boot repl -c
```

At this point, you can start a weasel server to connect to the browser window:

```console
cljs.user=> (start-repl)
```

If the REPL doesn't connect to the browser window, refresh the page in the browser.

At this point, you should be able to use the project's namespaces:

```console
cljs.user=> (require '[oc.web.lib.utils :as utils])
nil
cljs.user=> (utils/vec-dissoc [:a :b :c] :a)
[:b :c]
```

## Technical Design

Documentation of the technical design is [here](./docs/TECHNICAL_DESIGN.md).


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

Distributed under the [Mozilla Public License v2.0](http://www.mozilla.org/MPL/2.0/) with the [Commons Clause](https://commonsclause.com/).

Copyright © 2015-2018 OpenCompany, LLC.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the [Commons Clause](https://commonsclause.com/) and the [Mozilla Public License v2.0](http://www.mozilla.org/MPL/2.0/) for more details.