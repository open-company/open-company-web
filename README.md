# [OpenCompany](https://github.com/open-company) Web Application

[![MPL License](http://img.shields.io/badge/license-MPL-blue.svg?style=flat)](https://www.mozilla.org/MPL/2.0/)
[![Build Status](https://travis-ci.org/open-company/open-company-web.svg?branch=master)](https://travis-ci.org/open-company/open-company-web)

## Background

> Transparency, honesty, kindness, good stewardship, even humor, work in businesses at all times.

> -- [John Gerzema](http://www.johngerzema.com/)

Companies struggle to keep everyone on the same page. People are hyper-connected in the moment but still don’t know what’s happening across the company. Employees and investors, co-founders and execs, customers and community, they all want more transparency. The solution is surprisingly simple and effective - great company updates that build transparency and alignment.

With that in mind we designed the [Carrot](https://carrot.io/) software-as-a-service application, powered by the open source [OpenCompany platform](https://github.com/open-company). The product design is based on three principles:

1. It has to be easy or no one will play.
2. The "big picture" should always be visible.
3. Alignment is valuable beyond the team, too.

Carrot simplifies how key business information is shared with stakeholders to create alignment. When information about growth, finances, ownership and challenges is shared transparently, it inspires trust, new ideas and new levels of stakeholder engagement. Carrot makes it easy for founders to engage with employees and investors, creating alignment for everyone.

[Carrot](https://carrot.io/) is GitHub for the rest of your company.

Transparency expectations are changing. Organizations need to change as well if they are going to attract and retain savvy employees and investors. Just as open source changed the way we build software, transparency changes how we build successful companies with information that is open, interactive, and always accessible. Carrot turns transparency into a competitive advantage.

To get started, head to: [Carrot](https://carrot.io/)


## Overview

The OpenCompany Web Application provides a Web UI for creating and consuming open company content and data.

![OpenCompany Screenshot](https://open-company-assets.s3.amazonaws.com/animation.webm)

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

### Actions

The OpenCompany web app dispatches **actions** from the UI, browser events, and API request results. An action often
results in a new asynchronous API request or in app state changes that trigger a new render cycle of the UI components
listening to those parts of the app state.

The list of these actions, the caller that initiates the action, and a description of when the action is used is
provided in the table below:

|  **Action** | **Caller** | **Description** |
|  ------ | ------ | ------ |
|  :activity-delete | UI | Delete the selected entry. |
|  :activity-delete/finish | API | Entry delete request finished, refresh the board data. |
|  :activity-get/finish | API | Activity (secure or not) loaded from API. |
|  :activity-modal-edit | UI | Activate editing in modal. |
|  :activity-move | UI | Move an activity from a board to another. |
|  :activity-share-hide | UI | Hide the share modal. |
|  :activity-share-reset | UI | Reset the shared data to start a new share. |
|  :activity-share-show | UI | Show the share modal. |
|  :activity-share | UI | Start share request with the passed medium and data. |
|  :activity-share/finish | API | Share request finished. |
|  :alert-modal-hide | UI | Hide the alert modal view. |
|  :alert-modal-show | UI | Show the alert modal view. |
|  :alert-modal-hide-done | UI | Alert modal view hide finished. |
|  :all-activity-calendar | UI | Load activities from a calendar link. |
|  :all-activity-get | UI | Start loading all activity. |
|  :all-activity-get/finish | API | All activity data loaded, dispatch them. |
|  :all-activity-more | UI | Load more activities automatically when the user scrolls. |
|  :all-activity-more/finish | API | More activities loaded, concatenate the new results with the old ones. |
|  :auth-settings | API | Read the auth-settings response from the auth server and save the data in the app-state. Usually called together with :entry-point. |
|  :auth-settings-get | UI | Start the request to load the auth-settings from the auth server. |
|  :auth-with-token | UI | Given a topic (email reset, invitation etc..) start the token exchange to login the user. |
|  :auth-with-token/failed | API | Token exchange failed, cleanup the app-state of the token exchange stuff. |
|  :auth-with-token/success | API | Token exchange succeeded. Show the collect name and password overlay if it was an invitation token, load the orgs associated with the user and redirect. |
|  :board | API | Read and save the board data from API. Call the :load-other-boards action if necessary |
|  :board-get | UI | Load a board passing in directly the board link. |
|  :board-create | UI | Start the request to create a new board for the current org. |
|  :board-delete | UI | Delete a board of the current org. |
|  :board-edit | UI | Start the board edit for existing board and new board. |
|  :board-edit-save | UI | Save the board edited data. |
|  :board-edit-save/finish | API | Board created or updated. |
|  :board-edit/dismiss | UI | Starts the dismiss process of the board editing modal. |
|  :board-nav-away | UI | Updates local change state when a user leaves a board. |
|  :board-seen | UI | Updates local change state and the change service when a user sees a board. |
|  :boards-load-other | UI | Start the request to load the data of all the rest of the org boards, not the one currently shown. |
|  :bot-auth | UI | Start the bot add for a give slack team. |
|  :calendar-get | UI | Load the calendar data to show the AA sidebar. |
|  :calendar-get/finish | API | Calendar data finished loading. |
|  :channels-enumerate | UI | Start the request to load the Slack channels give a Slack team. |
|  :channels-enumerate/success | API | Slack channels loaded, it saves them in the proper place of the app-state. |
|  :comment-add | UI | Add a comment to an entry. |
|  :comment-add/finish | API | Request to add a comment finished, can be it failed. In all cases reloads the comments with :comments-get. |
|  :comment-save | UI | Edit and save a comment to an entry. |
|  :comment-save/finish | API | Request to save a comment finished, can be it failed. |
|  :comment-delete | UI | Delete a comment to an entry. |
|  :comment-delete/finish | API | Request to delete a comment finished, can be it failed. In all cases reloads the comments with :comments-get. |
|  :comments-get | UI | Starts the request to load the comments given an entry UUID. |
|  :comments-get/finish | API | Request to load the comments for an entry finished, could be it failed though. |
|  :container/change | WS | Notice from change service with change info for a board |
|  :container/status | WS | Response from change service watch request with timestamp info for boards |
|  :default | - | Default action, never used, it's only a fallback |
|  :first-forced-post-start | UI | Dismiss the first onboarded nux screen and open the new forced post modal. |
|  :email-domain-team-add | UI | Add an email domain to the user team. |
|  :email-domain-team-add/finish | API | Request to add an email domain to the team succeeded. |
|  :entries-loaded | API | The request to load the entries of a certain topic is finished. |
|  :entry-edit | UI | Start editing of an existing or a new entry. |
|  :entry-edit/dismiss | UI | Dismiss the entry editing. |
|  :entry-modal-save | UI | Save editing from modal. |
|  :entry-point | API | Read the Api entry point and save the data in the app state. |
|  :entry-point-get | UI | Load the storage entry point passing some flags to add to the app-state. |
|  :entry-publish | UI | Publish the edited entry. |
|  :entry-publish/failed | API | Publish entry reuqest finish but failed. |
|  :entry-publish/finish | API | Publish entry reuqest finish, refresh the board data. |
|  :entry-save | UI | Save the edited entry. |
|  :entry-save-on-exit | UI | Save the current edits to the local cache to avoid losing them. |
|  :entry-save/failed | API | Save entry reuqest finish but failed. |
|  :entry-save/finish | API | Save entry reuqest finish, refresh the board data. |
|  :entry-toggle-save-on-exit | UI | Toggle save on exit to avoid losing edits on page unload or edit exit. |
|  :error-banner-show | UI | Given an error message and a time, show the specified error for that time, if the time is 0 stick the message. |
|  :input | UI | Generic input action, it's used passing in a path and the value. The value is saved at the specified path of the app-state. |
|  :invitation-confirmed | API | Confirm invitation request succeeded. |
|  :invite-by-email | UI | Start the request to invite a user, check if the email is already present and use the resend link if possible. No-op if the user is already an active user of the team. |
|  :invite-by-email/failed | API | Invitation request failed, add the proper error in the app-state. |
|  :invite-by-email/success | API | Reload the team data to show the new invited user, reset the app-state for invitation data. |
|  :invite-users | UI | Invite the users in the list. |
|  :invite-user/success | API | The user was successfully invited. |
|  :invite-user/failed | API | The user was not invited due to an error. |
|  :jwt | UI | Given the JWT decoded data, save them in the app-state. |
|  :login-overlay-show | UI | Set the login overlay type in the app-state to show login, signup, password reset or collect name and password views. |
|  :login-with-email | UI | Show the login with email overlay. |
|  :login-with-email/failed | API | Login via email failed, add the proper error message to the app-state. |
|  :login-with-email/success | API | Login via email succeeded. Save the returned JWT in the cookie and load the entry-point data. |
|  :login-with-slack | UI | Show the login with slack overlay. |
|  :logout | UI | Logout action: remove the JWT from the app-state and the jwt cookie. Redirect to /. |
|  :mobile-menu-toggle | UI | Toggle the menu on mobile device. |
|  :name-pswd-collect | UI | Start the request to save the user name and password. |
|  :name-pswd-collect/finish | API | Collect name and password request finished. |
|  :nux-end | UI | Finish the NUX. |
|  :org | API | Read and save the org data in the app-state. Redirect the UI to the last seen or the last created board or to the board creation if none is present.  |
|  :org-create | UI | Start the request to create a new org. |
|  :org-edit | UI | Setup the data to start the organization settings editing. |
|  :org-edit-save | UI | Save the organization data edited from settings. |
|  :org-redirect | UI | Redirect to the newly created org after the setup screen have been shown. |
|  :password-reset | UI | Start the request to reset the user password. |
|  :password-reset/finish | API | Password reset request finished. |
|  :private-board-action | UI | Given a private board, a user of this board and an action perform the action: change role or remove the user. |
|  :private-board-add | UI | Add a user to a private board with a specific role. |
|  :pswd-collect | UI | Start the request to save the new user password on password request. |
|  :pswd-collect/finish | API | Collect password request finished. |
|  :reaction-toggle | UI | Toggle a reaction, temporarily change it in the local state then starts the request to save it server side. |
|  :react-from-picker | UI | Add a reaction selected from the picker. |
|  :react-from-picker/finish | API | Reaction add request finished. |
|  :secure-activity-get | UI | Load an activity using the secure uuid. |
|  :signup-with-email | UI | Show the signup with email overlay. |
|  :signup-with-email/failed | API | Signup with email failed. Add the proper error message to the app-state. |
|  :signup-with-email/success | API | Signup with email succeeded. Save the JWT received in the cookie and remove the signup overlay. Load the entry point to redirect the user to the proper org/place. |
|  :site-menu-toggle | UI | Collapse and expand the site menu for mobile. |
|  :slack-team-add | UI | Start the request to add a Slack team to the current team. |
|  :slack-token-refresh | UI | Refresh the data of the user signed in with Slack. |
|  :story-get | UI | Load a story. |
|  :story-get/finish | API | Story loaded. |
|  :subscription | API | Save the new subscription data in the app-state. |
|  :team-loaded | API | Save the team loaded data in the app-state. |
|  :team-roster-loaded | API | Save the roster in the team data. |
|  :teams-get | UI | Start the request to load the list of teams. |
|  :teams-loaded | API | Read and save the list of teams. Start the request to load the team data or the roster if the link is not present for each team returned. |
|  :topic | API | Read and save the content of a topic in the app-state. Async start the load of the list of entries. |
|  :topic-add | UI | Add a new topic to the topics list of the current board. |
|  :topic-enty | API | Read and save a topic entry in the proper place of the app-state. |
|  :trend-bar-status | UI | Change the trend bar status, it accepts: :hidden, :collapsed, :expanded and :trending as in oc.web.components.trend-bar. |
|  :update-loaded | API | Read and save the data of certain update in the app-state |
|  :user-action | UI | Start a user action: given a team-id, a user data object, the action and a method make a request to complete the action. Can pass optional payload (if it's not a GET request). |
|  :user-action/complete | API | Refresh the team data to show the completed user action. |
|  :user-data | API | Current user data loaded, save them in the app-state. |
|  :user-profile-reset | UI | Reset the user profile data edited by the user but not yet saved. |
|  :user-profile-save | UI | Save the edited user data. |
|  :user-profile-update/failed | API | User profile update request failed. |
|  :whats-new-modal-hide | UI | Hide the What's New modal. |
|  :whats-new-modal-show | UI | Show the What's New modal. |
|  :ws-interaction/comment-add | WS | Websocket message for an added comment. |
|  :ws-interaction/comment-delete | WS | Websocket message for a deleted comment. |
|  :ws-interaction/comment-update | WS | Websocket message for an updated comment. |
|  :ws-interaction/reaction-add | WS | Websocket message for an added reaction. |
|  :ws-interaction/reaction-delete | WS | Websocket message for a removed reaction. |

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
