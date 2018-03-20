## Technical Design

Carrot follows the Facebook Flux pattern:

<img src="./flux-diagram-white-background.png" style="width: 100%;" />

_Image source: the [Flux repo](https://github.com/facebook/flux)_

It's important to note, Flux isn't a particular software library, instead it's an architectural pattern that's been implemented now in countless React apps and in many React web frameworks.

The key idea in Flux is having a unidirectional data flow:

<img src="./flux-simple-f8-diagram-with-client-action-1300w.png" style="width: 100%;" />

_Image source: [Flux concepts](https://github.com/facebook/flux/tree/master/examples/flux-concepts)_

Here is some background information on Flux:

- https://github.com/facebook/flux - The Flux repo (documentation of the pattern, as well as a JavaScript Dispatcher implemntation)
- https://github.com/facebook/flux/tree/master/examples/flux-concepts - High-level Flux concepts
- https://facebook.github.io/flux/docs/in-depth-overview.html - Flux in depth
- https://github.com/kgann/cljs-flux - An example implementation of the Flux pattern in ClojureScript

### Action Creators

An action creator is called by a user interaction or an API request finishing.

These action creators are located in `/src/oc/web/actions/`. They are losely
organized based on the data they will act on. So `/src/oc/web/actions/user.cljs` contains actions for things like: user sign up, login, and change of user profile information.

An action creator will handle the following:

- Pass data and an action to the dispatcher.
- Make async API calls where the results call a "finished" action creator.

There are some things the action creators do now but that might belong in a store:

- Update cookie information
- Update url routes.

### Simplified Data Model

- Org
  - has many Sections

- Team
  - has many Orgs
  - has many Users
  - Authentication Settings
  - Slack bot

- User
  - Slack info

- Section
  - Slack info 
  - has many Posts
  - has many authors
  - has many viewers

- Post
  - has many Comments
  - has many Reactions
  - has many authors

- Reaction
  - has an author

- Comment
  - has many Reactions
  - has an author
  
### Stores (changing the app state)

Our action creators dispatch data in an action event using our dispatcher.

We subscribe stores to particular events by creating a function with an arity
matching the event and payload. Stores with interest in the same event/payload
will have a separate register to the dispatcher.

When parts of the app state are changed a derivative is changed and used to
render the UI. Also UI components can read from a store to retrieve more information.

In a traditional Plux architechure the components subscribe to a change event
in a store and then use the store to get data. With derivatives we have all that data in a single atom. 

The first parameter to a store's handler is the application state, an atom that
contains all the data used by the react components. Each handler modifies the
passed in app state and then returns it.

When parts of the app state are changed the value of a derivative is changed and used to render the UI. The use of derivaties reduce the amount of code needed
to listen for events and then read the current state from each store.

A store can process a single event with the following code:

```clojure
;; dispatcher/action is a multimethod. Additional action methods can be
;; defined to handle an "event" (this case it is :search-query/finish)
(defmethod dispatcher/action :search-query/finish
  [db [data]]
  (assoc db :something-changed data) ;; always return the application state
)
```

The second way is to register with the dispatcher to receive all events:

```clojure
(ns oc.web.stores.reaction
  (:require [cljs-flux.dispatcher :as flux]
            [oc.web.dispatcher :as dispatcher]))

;; Reducers used to watch for reaction dispatch data
(defmulti reducer (fn [db [action-type & _]]
                    (timbre/info "Dispatching reaction reducer:" action-type)
                    action-type))

(def reactions-dispatch
  (flux/register
   dispatcher/actions
   (fn [payload]
     (swap! dispatcher/app-state reducer payload))))

(defmethod reducer :default [db payload]
  ;; ignore state changes not specific to reactions
  db)

(defmethod reducer :all-posts-get/finish
  [db [_ {:keys [org year month from body]}]]
  db)
```

#### Org

Information about organization.

#### Team

Information abut teams.

#### User

Handles nux

Handles user profile settings.

Handles authentication.

#### Section

Handles Section information.

#### Activity (aka Post)

Handles post information.


#### Reaction

Information about reactions to posts and comments.

#### Comment

Information about comments.

#### Search

Handles search results.

### App State Design

The app-state has the following structure:

```clojure
{
  :key {
     :data-for-key {
       :name "Buffer"
       :slug "buffer"
       ;; ... other data form /companies/buffer ....
     }
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

The Carrot web app is made up of trees of Rum components that start from a few top level components and include other child components in a tree, sometimes just a few, and sometimes many.

TODO: Update copy.
One of the most important component trees is the company dashboard. This provides the main UI for viewing and editing a company's topics. Here's a diagram of the read-only aspects of the company dashboard component tree:

TODO: Update to actual components used.

![Company Dashboard Diagram](https://cdn.rawgit.com/open-company/open-company-web/mainline/docs/dashboard-viewing-component-tree.svg)