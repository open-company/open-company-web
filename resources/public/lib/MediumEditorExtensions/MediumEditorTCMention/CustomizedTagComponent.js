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
  if (user["selectedKey"] === "slack-username" && user["slack-username"]) {
    return user["slack-username"];
  }else if (user["name"]) {
    return user["name"];
  }else if (user["first-name"] && user["last-name"]) {
    return user["first-name"] + " " + user["last-name"];
  }else if (user["first-name"]) {
    return user["first-name"];
  }else if (user["last-name"]) {
    return user["last-name"];
  }else {
    return user["email"];
  }
}

function getSlackUsername(user) {
  return (user["selectedKey"] === "slack-username")? user["slack-username"] : user["slack-usernames"][0];
}

function ListItem(props) {
  let user = props.user;
  let avatarStyle = { "backgroundImage": "url(" + user["avatar-url"] + ")" };
  let displayName = getUserDisplayName(user);
  let slackUsername = getSlackUsername(user);

  return React.createElement(
    "div",
    { "key": "user-" + (user["user-id"] + "-" + user["email"]),
      "className": "oc-mention-option" +
                   (props.selectedIndex === props.index? " active" : "") +
                   (user["avatar-url"] && user["avatar-url"].length > 0? " has-avatar " : ""),
      "data-name": displayName,
      "data-user-id": user["user-id"],
      "data-slack-username": slackUsername,
      onMouseEnter: function(e){
        props.hoverItem(e, props.index);
      },
      onClick: function() {
        props.clickCb(user);
      }},
    React.createElement("div", { "className": "oc-mention-option-avatar",
                                 style: avatarStyle }),
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
    if (value && value.length) {
      let values = value.toLowerCase().split(/\s/g);
      for (let i = 0; i < values.length; i++) {
        if (values[i] && values[i].indexOf(searchValue.toLowerCase()) === 0){
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

    function checkSlackUsernames(user) {
      for(var i = 0; i < user["slack-usernames"].length; i++) {
        if (that.checkStringValue(user["slack-usernames"][i], currentText)) {
          return Object.assign(user, { "selectedKey": "slack-username",
                                       "slack-username": user["slack-usernames"][i] });
        }
      }
      return null;
    }

    let mappedUsers = props.users.map(function (user, i) {
      let filteredSlackUsernames = [];
      if (that.checkStringValue(user["name"], currentText)){
        return Object.assign(user, { "selectedKey": "name" });
      }
      else if (that.checkStringValue(user["first-name"], currentText)){
        return Object.assign(user, { "selectedKey": "first-name" });
      }
      else if (that.checkStringValue(user["last-name"], currentText)){
        return Object.assign(user, { "selectedKey": "last-name" });
      }
      else if (user["slack-usernames"].length > 0 && checkSlackUsernames(user)){
        return checkSlackUsernames(user);
      }
      else if (that.checkStringValue(user["email"], currentText)){
        return Object.assign(user, { "selectedKey": "email" });
      }
      else{
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
