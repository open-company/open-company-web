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

To view the current app state at any time, run this in the JavaScript console:

```
open_company_web.dispatcher.print_app_state()
```

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

Copyright © 2015-2017 OpenCompany, LLC.

## Actions

|  **Action** | **Caller** | **Description** |
|  ------ | ------ | ------ |
|  :email-domain-team-add | UI | Add an email domain to the user team. |
|  :email-domain-team-add/finish | API | Request to add an email domain to the team succeeded. |
|  :slack-team-add | UI | Start the request to add a Slack team to the current team. |
|  :topic-add | UI | Give a topic and its data add the topic to the current board. |
|  :topic-archive | UI | Start the archive topic action. Call the API function to archive. |
|  :topic-archive/success | API | Archive succesfully done, navigate to the board to reload the data without the topic. |
|  :bot-auth | UI | Start the bot add for a give slack team. |
|  :auth-settings | API | Read the auth-settings response from the auth server and save the data in the app-state. Usually called together with :entry-point. |
|  :auth-with-token | UI | Given a topic (email reset, invitation etc..) start the token exchange to login the user. |
|  :auth-with-token/failed | API | Token exchange failed, cleanup the app-state of the token exchange stuff. |
|  :auth-with-token/success | API | Token exchange succeeded. Show the collect name and password overlay if it was an invitation token, load the orgs associated with the user and redirect. |
|  :board | API | Read and save the board data from APi. Call the :load-other-boards action if necessary |
|  :name-pswd-collect | UI | Start the request to save the user name and password. |
|  :name-pswd-collect/finish | API | Collect name and password request finished. |
|  :pswd-collect | UI | Start the request to save the new user password on password request. |
|  :pswd-collect/finish | API | Collect password request finished. |
|  :board-create | UI | Start the request to create a new board for the current org. |
|  :org-create | UI | Start the request to create a new org. |
|  :dashboard-select-all | UI | Given a board select all the topics for the share. |
|  :dashboard-select-topic | UI | When in sharing mode toggle a topic in the selected set. |
|  :dashboard-share-mode | UI | Toggle the sharing mode. |
|  :default | - | Default action, never used, it's only a fallback |
|  :board-delete | UI | Delete a board of the current org. |
|  :entry-delete | UI | Start the delete entry action. Call the proper API function. It also takes care of removing the topic from the board if it's the last entry or to replace it with the previous entry if it was the latest. |
|  :entry-delete/success | API | Entry succesfully deleted, navigate to the board to reload the data without the entry. |
|  :entries-loaded | API | The request to load the entries of a certain topic is finished. |
|  :entry-point | API | Read the Api entry point and save the data in the app state. |
|  :channels-enumerate | UI | Start the request to load the Slack channels give a Slack team. |
|  :channels-enumerate/success | API | Slack channels loaded, it saves them in the proper place of the app-state. |
|  :foce-input | UI | Save a new data for the current edited entry. |
|  :foce-save | UI | Call the proper API function to save the topic data collected during FoCE. |
|  :auth-settings-get | UI | Start the request to load the auth-settings from the auth server. |
|  :teams-get | UI | Start the request to load the list of teams. |
|  :udpates-list-get | UI | Start the request to load the list of the prior updates for a certain org. |
|  :welcome-screen-hide | UI | Remove the welcome screen shown the first time the first org user open the dashboard. |
|  :input | UI | Generic input action, it's used passing in a path and the value. The value is saved at the specified path of the app-state. |
|  :invitation-confirmed | API | Confirm invitation request succeeded. |
|  :invite-by-email | UI | Start the request to invite a user, check if the email is already present and use the resend link if possible. No-op if the user is already an active user of the team. |
|  :invite-by-email/failed | API | Invitation request failed, add the proper error in the app-state. |
|  :invite-by-email/success | API | Reload the team data to show the new invited user, reset the app-state for invitation data. |
|  :jwt | UI | Given the JWT decoded data, save them in the app-state. |
|  :boards-load-other | UI | Start the request to load the data of all the rest of the org boards, not the one currently shown. |
|  :login-with-email | UI | Show the login with email overlay. |
|  :login-with-email/failed | API | Login via email failed, add the proper error message to the app-state. |
|  :login-with-email/success | API | Login via email succeeded. Save the returned JWT in the cookie and load the entry-point data. |
|  :login-with-slack | UI | Show the login with slack overlay. |
|  :logout | UI | Logout action: remove the JWT from the app-state and the jwt cookie. Redirect to /. |
|  :mobile-menu-toggle | UI | Toggle the menu on mobile device. |
|  :new-topics-load/finish | API | Read the available new topics and save them in the app-state for later use. |
|  :org | API | Read and save the org data in the app-state. Redirect the UI to the last seen or the last created board or to the board creation if none is present.  |
|  :password-reset | UI | Start the request to reset the user password. |
|  :password-reset/finish | API | Password reset request finished. |
|  :private-board-action | UI | Given a private board, a user of this board and an action perform the action: change role or remove the user. |
|  :private-board-add | UI | Add a user to a private board with a specific role. |
|  :slack-token-refresh | UI | Refresh the data of the user signed in with Slack. |
|  :user-profile-reset | UI | Reset the user profile data edited by the user but not yet saved. |
|  :add-topic-rollback | UI | Rollback the add topic to the board. User canceled action. |
|  :user-profile-save | UI | Save the edited user data. |
|  :set-board-cache! | UI | Save some data of the current board, used for example by the growth topic to remember the last focused metric in the UI. |
|  :add-topic-show | UI | Show the add topic view in the dashboard. |
|  :error-banner-show | UI | Given an error message and a time, show the specified error for that time, if the time is 0 stick the message. |
|  :login-overlay-show | UI | Set the login overlay type in the app-state to show login, signup, password reset or collect name and password views. |
|  :top-menu-show | UI | Toggle the dropdown menu of the topic in the dashboard. |
|  :signup-with-email | UI | Show the signup with email overlay. |
|  :signup-with-email/failed | API | Signup with email failed. Add the proper error message to the app-state. |
|  :signup-with-email/success | API | Signup with email succeeded. Save the JWT received in the cookie and remove the signup overlay. Load the entry point to redirect the user to the proper org/place. |
|  :foce-start | UI | Setup the app-state for FoCE initializing the data with the given entry data (empty if new entry). |
|  :foce-data-editing-start | UI | Start the data edit for growth and finances topics. |
|  :su-edit | API | When a new update is created it saves the link and the data of the update in the app-state. |
|  :subscription | API | Save the new subscription data in the app-state. |
|  :team-loaded | API | Save the team loaded data in the app-state. |
|  :team-roster-loaded | API | Save the roster in the team data. |
|  :teams-loaded | API | Read and save the list of teams. Start the request to load the team data or the roster if the link is not present for each team returned. |
|  :topic | API | Read and save the content of a topic in the app-state. Async start the load of the list of entries. |
|  :topic-enty | API | Read and save a topic entry in the proper place of the app-state. |
|  :update-loaded | API | Read and save the data of certain update in the app-state |
|  :updates-list | API | Read and save the list of the prior updates in the app-state given an org. |
|  :user-action | UI | Start a user action: given a team-id, a user data object, the action and a method make a request to complete the action. Can pass optional payload (if it's not a GET request). |
|  :user-action/complete | API | Refresh the team data to show the completed user action. |
|  :user-data | API | Current user data loaded, save them in the app-state. |
|  :user-profile-update/failed | API | User profile update request failed. |