## Technical Design

Carrot follows the Facebook Flux pattern:

<img src="./flux-diagram-white-background.png" style="width: 100%;" />
_image source: the Flux repo_

It's important to note, Flux isn't a particular software library, instead it's an architectural pattern that's been implemented now in countless React apps and in many React web frameworks.

The key idea in Flux is unidirectional data flow.

Here is some background information on Flux:

- https://github.com/facebook/flux - The Flux repo (documentation of the pattern, as well as a JavaScript Dispatcher implemntation)
- https://github.com/facebook/flux/tree/master/examples/flux-concepts - High-level Flux concepts
- https://facebook.github.io/flux/docs/in-depth-overview.html - Flux in depth
- https://github.com/kgann/cljs-flux - An example implementation of the Flux pattern in ClojureScript

### Action Creators

An action creator is called by a user interaction or an API request finishing.

These action creators are located in `/src/oc/web/actions/`. They are losely
organized based the data they will act on. So `/src/oc/web/actions/user.cljs`
contains actions for a user to sign up, login, and change user profile
information.


### Simplified Data Model

- Org
  - has many Teams
  - has many Sections

- Team
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

Our action creators will dispatch data and an event using our dispatcher.
We subscribe stores to particular events by creating a function with an arity
matching the event and payload. Stores with interest in the same event/payload
will have a separate register to the dispatcher.

When parts of the app state are changed a derivative is changed and used to
render the UI. Also UI components can read from a store to retrieve more information.

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

TODO: Update to actual components used.

![Company Dashboard Diagram](https://cdn.rawgit.com/open-company/open-company-web/mainline/docs/dashboard-viewing-component-tree.svg)

