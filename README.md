# [OpenCompany](https://github.com/open-company) Web Application

[![License: CC BY-NC-SA 4.0](https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-lightgrey.svg)](https://creativecommons.org/licenses/by-nc-sa/4.0/)
[![Build Status](https://travis-ci.org/open-company/open-company-web.svg?branch=master)](https://travis-ci.org/open-company/open-company-web)

## Background

> Transparency, honesty, kindness, good stewardship, even humor, work in businesses at all times.

> -- [John Gerzema](http://www.johngerzema.com/)

Teams struggle to keep everyone on the same page. People are hyper-connected in the moment with chat and email, but it gets noisy as teams grow, and people miss key information. Everyone needs clear and consistent leadership, and the solution is surprisingly simple and effective - **great leadership updates that build transparency and alignment**.

With that in mind we designed [Carrot](https://carrot.io/), a software-as-a-service application powered by the open source [OpenCompany platform](https://github.com/open-company) and this source-available [web UI](https://github.com/open-company/open-company-web).

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

Most of the dependencies are internal, meaning [Shadow-cljs](https://github.com/thheller/shadow-cljs) will handle getting them for you. There are a few exceptions:

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) - a Java 8 JRE is needed to run Clojure
* [Leiningen](https://github.com/technomancy/leiningen) 2.9.1+ - Clojure's build and dependency management tool
* [Node LTE](https://nodejs.org/en/)
* [NPM](https://npmjs.com) - A command line tool and package manager for Javascript

#### Java

Chances are your system already has Java 8+ installed. You can verify this with:

```console
java -version
```

If you do not have Java 8+ [download it](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and follow the installation instructions.

An option we recommend is [OpenJDK](https://openjdk.java.net/). There are [instructions for Linux](https://openjdk.java.net/install/index.html) and [Homebrew](https://brew.sh/) can be used to install OpenJDK on a Mac with:

```
brew tap AdoptOpenJDK/openjdk
brew update
brew cask install adoptopenjdk8
```

#### Leiningen

Installing Leiningen is easy, for the most up to date instructions, check out the [Leiningen installation](https://github.com/technomancy/leiningen#installation) section of the README.
You can verify your install with:

```console
lein -v
```


## Usage

Prospective users of [Carrot](https://carrot.io/) should get started by going to [Carrot.io](https://carrot.io/). The following usage is **for developers** wanting to work on the Web application.

Local usage of the web application requires you to run 3 services, the [Storage service](https://github.com/open-company/open-company-storage), the [Auth service](https://github.com/open-company/open-company-auth), and the [Interaction service](https://github.com/open-company/open-company-interaction). All can be started in their respective repositiories with:

```console
lein start
```

Plus the following optional services are usually required to get a clean working app: [Change service](https://github.com/open-company/open-company-change) and [Notify service](https://github.com/open-company/open-company-notify)

To get an interactive web **development** environment, start the iterative compilation process in this repository:

```console
lein start
```

If you've done it right, you'll now have 4 terminal sessions running: Storage, Auth, Interaction and Web, optionally another 2 running Change and Notify services.

Open your browser to [http://localhost:3559/](http://localhost:3559/).

If you want to use your local IP address instead of localhost to tests on a different device you just need to create a file named `resources/public/lib/local-env.js` with the following content:

```
var OCEnv = {
  "web-hostname": "192.168.1.2",
  "port": "3559"
}
```

also set that same IP address in your environment:

```console
export LOCAL_DEV_HOST=192.168.1.2
export LOCAL_DEV_PORT=3559
```

Then you can connect to [http://192.168.1.2:3559](http://192.168.1.2:3559). This file, and any of its keys, is optional.

NB: If you want to change the port you have to change it also in the [shadow.cljs](/shadow.cljs) and [oc.web.local-settings](/src/main/oc/web/local_settings.cljs) files.

To create a **production** build run:

```console
lein prod-build
```

To compile assets for production use you need the Google Closure compiler. Download the google closure compiler from this link

https://developers.google.com/closure/compiler/

then unzip and place it in a known location:

```console
mkdir ~/closure_compiler
mv <download location>/compiler_latest.zip ~/closure_compiler/
cd ~/closure_compiler
unzip compiler_latest.zip
```
now you can build it:

```console
script/compile_assets.sh <compiler version> ~/path/to/open-company-web $LOCAL_DEV_HOST:$LOCAL_DEV_PORT
cp ~/path/to/open-company-web/target/public/oc_assets.js* ~/path/to/open-company-way/resources/public/
```

### Project REPL

To have a ClojureScript REPL connected to the browser, first start the dev task:

```console
shadow-cljs browser-repl

# or, if shadow-cljs is installed locally

npx shadow-cljs browser-repl
```

Try running:

```console
(js/alert "This is the REPL evaluate window")
```

This will open a new browser window loading http://localhost:9630/repl-js/browser-repl and that will evaluate all the code entered there.

To inject code in your webapp directly now you need to run:

```console
shadow-cljs cljs-repl webapp

# or, if shadow-cljs is installed locally

npx shadow-cljs cljs-repl webapp
```

This will open a REPL and will move to oc.web.core namespace.

Now refresh the webapp window to make sure the REPL is connected to it and then run:

```console
(.log js/console "This is the webapp window, current url is:" (.. js/window -location -pathname))
```

Next open a browser window: [http://localhost:3559/](http://localhost:3559/)

If the REPL doesn't connect to the browser window, refresh the page in the browser.

At this point, you should be able to use the project's namespaces:

```console
cljs.user=> (require '[oc.web.lib.utils :as utils])
nil
cljs.user=> (utils/vec-dissoc [:a :b :c] :a)
[:b :c]
```

#### Cider

@FIXME: i don't use cider so i am not sure how to fix this atm

For cider users, the steps are just slightly different. Within emacs:

```console
M-x cider-jack-in-cljs
```

When prompted to select a ClojureScript REPL type, choose `custom` (we will
start our own). Wait for the REPL to connect, and again it will prompt you to
enter the custom form to start the ClojureScript REPL. Just press enter. You now
have a running Clojure REPL with all the required middleware.

From here on out, the process is very similar to starting things up from the terminal.
At the REPL, we're going to start the `boot dev` task as a future process:

```console
user=> (in-ns 'boot.user)
#namespace[boot.user]
boot.user=> (def p (future (boot (dev))))
```

Once that's finished compiling, open your browser window: [http://localhost:3559/](http://localhost:3559/)

Now you're ready to start the ClojureScript REPL:

```console
boot.user=> (start-repl)
```

Wait for compilation to finish, and you should be dropped into the `cljs.user` namespace. If not, try
refreshing the browser. Sometimes it needs a gentle nudge.

Let's test it out:

```console
cljs.user=> (js/alert "Hello, browser!")
```

If all's well, you should see an alert box appear in the browser.

## Technical Design

Documentation of the technical design is [here](./docs/TECHNICAL_DESIGN.md).


## Testing

TBD

## Desktop Application

Desktop app wrapper has moved to its own repo at [github.com/open-company/open-company-desktop](https://github.com/open-company/open-company-desktop)

## Participation

Please note that this project is released with a [Contributor Code of Conduct](https://github.com/open-company/open-company-web/blob/mainline/CODE-OF-CONDUCT.md). By participating in this project you agree to abide by its terms.


## License

Copyright © 2015-2020 OpenCompany, LLC.

This code is licensed under the [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International Public License](https://creativecommons.org/licenses/by-nc-sa/4.0/) (CC BY-NC-SA 4.0).

This means the code is source available to you, but is not open source, due to the following terms of the CC BY-NC-SA 4.0 license:

**Attribution** — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.

**NonCommercial** — You may not use the material for commercial purposes.

**ShareAlike** — If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original. 

This code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.