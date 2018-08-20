function mod(num, tot) {
  return ((num%tot)+tot)%tot;
}

function isHidden(el) {
  let style = window.getComputedStyle(el);
  return (style.display === 'none' || style.visibility === 'hidden' || el.offsetParent === null);
}

function addEvent(element, eventName, callback) {
  if (element.addEventListener) {
    element.addEventListener(eventName, callback, false);
  } else if (element.attachEvent) {
    element.attachEvent("on" + eventName, callback);
  } else {
    element["on" + eventName] = callback;
  }
}

function removeEvent(element, eventName, callback) {
  if (element.addEventListener) {
    element.removeEventListener(eventName, callback, false);
  } else if (element.attachEvent) {
    element.detachEvent("on" + eventName, callback);
  } else {
    delete element["on" + eventName];
  }
}

function getUserDisplayName(user) {
  return (user["name"] && user["name"].length > 0) ? user["name"] : (user["first-name"] + " " + user["last-name"]);
}

function getUserSelectedDisplayValue(user) {
  return (user["selectedKey"] === "slack-username")? user["slack-username"] : user["slack-display-name"] || user["slack-usernames"][0];
}

function ListItem(props) {
  let user = props.user;
  let avatarStyle = { "backgroundImage": "url(" + user["avatar-url"] + ")" };
  let displayName = getUserDisplayName(user);
  let slackUsername = getUserSelectedDisplayValue(user);

  return React.createElement(
    "div",
    { "key": "user-" + (user["user-id"] + "-" + user["email"]),
      "className": "oc-mention-option " + (props.selectedIndex === props.index? "active" : ""),
      "data-name": displayName,
      "data-user-id": user["user-id"],
      "data-slack-username": slackUsername,
      onMouseEnter: function(e){
        props.hoverItem(e, props.index);
      },
      onClick: function() {
        props.clickCb(user);
      }},
    React.createElement("div", { "className": "oc-mention-option-avatar", style: avatarStyle }),
    React.createElement(
      "div",
      { "className": "oc-mention-option-title" },
      displayName
    ),
    React.createElement(
      "div",
      { "className": "oc-mention-option-subline" + (slackUsername && slackUsername.length? " slack-icon" : "") },
      slackUsername || user["email"]
    )
  );
};


// Using PureComponent as suggested here:
// https://reactjs.org/blog/2018/06/07/you-probably-dont-need-derived-state.html#what-about-memoization
class CustomizedTagComponent extends React.PureComponent {
  constructor(props) {
    super(props);
    this.state = {selectedIndex: 0};
  }

  keyPress(e) {
    e = e || window.event;
    let node = ReactDOM.findDOMNode(this);
    let options = $(node).find(".oc-mention-option");
    if (!isHidden(node) && options.length) {
      switch(e.keyCode){
        // Right and Down arrows
        case 39:
        case 40:
          if (this.state.selectedIndex !== null) {
            this.setState({selectedIndex: mod(this.state.selectedIndex + 1, options.length)})
          }else{
            this.setState({selectedIndex: 0});
          }
          e.preventDefault();
          e.stopPropagation();
          break;
        // Left and Up arrows
        case 37:
        case 38:
          if (this.state.selectedIndex !== null) {
            this.setState({selectedIndex: mod(this.state.selectedIndex - 1, options.length)})
          }else{
            this.setState({selectedIndex: options.length - 1});
          }
          e.preventDefault();
          e.stopPropagation();
          break;
        // Enter or tab
        case 13:
        case 9:
          if (this.state.selectedIndex !== null) {
            let user = this.filterUsers(this.props)[this.state.selectedIndex];
            this.selectItem(user);
          }
          e.preventDefault();
          e.stopPropagation();
          break;
      }
    }
  }

  componentDidMount() {
    // Add keyboard event listener
    addEvent(window, "keydown", this.keyPress.bind(this));
  }

  componentWillUnmount() {
    // Remove keyboard event listener
    removeEvent(window, "keydown", this.keyPress.bind(this));
  }

  hoverItem(e, idx) {
    this.setState({selectedIndex: idx});
  }

  blurItem(e, idx) {
    this.setState({selectedIndex: 0});
  }

  selectItem(user){
    let selectedValue = getUserSelectedDisplayValue(user);
    this.props.selectMentionCallback("@" + selectedValue, user);
  }

  checkStringValue(value, searchValue){
    console.log("XXX         value", value, "searchValue", searchValue);
    if (value && value.length) {
      let values = value.toLowerCase().split(/\s/g);
      console.log("XXX            splittedValues", values);
      for (let i = 0; i < values.length; i++) {
        console.log("XXX              checking", values[i]);
        if (values[i] && values[i].indexOf(searchValue.toLowerCase()) === 0){
          console.log("XXX              found!");
          return true;
        }
      }
    }
    return false;
  }

  filterUsers(props){
    const trigger = props.currentMentionText.substring(0, 1);
    const currentText = props.currentMentionText.substring(1, props.currentMentionText.length).toLowerCase();
    const that = this;
    console.log("XXX filterUsers currentText:", currentText);

    function getSlackUsernames(user){
      let slackUsernames = [];
      if (user["slack-users"] && Object.values(user["slack-users"]).length > 0) {
        Object.values(user["slack-users"]).map(function(slackUser){
          slackUsernames.push(slackUser);
        });
      }
      return slackUsernames;
    }

    let mappedUsers = props.users.map(function (user, i) {
      let activeUser = user["status"] === "active" || user["status"] === "unverified",
          filteredSlackUsernames = [];
      user = Object.assign(user, { "slack-usernames": getSlackUsernames(user)});
      console.log("XXX     slack-users", user["slack-users"], "->", Object.values(user["slack-usernames"]));
      console.log("XXX     filteredSlackUsernames", filteredSlackUsernames);
      if (that.checkStringValue(user["name"], currentText)){
        console.log("XXX     found name!");
        return Object.assign(user, { "selectedKey": "name" });
      }
      else if (that.checkStringValue(user["first-name"], currentText)){
        console.log("XXX     found first-name!");
        return Object.assign(user, { "selectedKey": "first-name" });
      }
      else if (that.checkStringValue(user["last-name"], currentText)){
        console.log("XXX     found last-name!");
        return Object.assign(user, { "selectedKey": "last-name" });
      }
      else if (that.checkStringValue(user["slack-display-name"], currentText)){
        console.log("XXX     found slack-display-name!");
        return Object.assign(user, { "selectedKey": "slack-username",
                                     "slack-username": user["slack-usernames"] });
      }
      else if (user["slack-usernames"].length > 0){
        console.log("XXX       checking all", user["slack-usernames"]);
        for(var i = 0; i < user["slack-usernames"].length; i++) {
          console.log("XXX       checking: ", user["slack-usernames"][i]);
          if (that.checkStringValue(user["slack-usernames"][i], currentText)) {
            console.log("XXX     found slack-username!", i);
            return Object.assign(user, { "selectedKey": "slack-username",
                                         "slack-username": user["slack-usernames"][i] });
          }
        }
      }
      else if (that.checkStringValue(user["email"], currentText)){
        console.log("XXX     found email!");
        return Object.assign(user, { "selectedKey": "email" });
      }
      else{
        console.log("XXX     not found!");
        return user;
      }
    });
    return mappedUsers.filter(function (user) {
      return !!user["selectedKey"];
    });
  }

  render() {
    let that = this;
    let filteredUsers = this.filterUsers(this.props);
    console.log("XXX props", this.props);
    console.log("XXX filteredUsers", filteredUsers);
    return React.createElement(
      "div",
      { "className": "oc-mention-options",
        "contentEditable": false },
      React.createElement(
        "div",
        { "className": "oc-mention-options-list" },
        filteredUsers.map(function (user, i) {
          return ListItem({user: user,
                           index: i,
                           selectedIndex: that.state.selectedIndex,
                           clickCb: that.selectItem.bind(that),
                           hoverItem: that.hoverItem.bind(that)})
        })));
  }
}
