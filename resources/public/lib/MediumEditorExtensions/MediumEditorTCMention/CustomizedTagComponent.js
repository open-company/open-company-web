function mod(num, tot) {
  return ((num%tot)+tot)%tot;
}

function isHidden(el) {
  var style = window.getComputedStyle(el);
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
  return (user["selectedKey"] === "slack-username")? user["slack-username"] : getUserDisplayName(user);
}

function ListItem(props) {
  var user = props.user;
  var avatarStyle = { "backgroundImage": "url(" + user["avatar-url"] + ")" };
  var displayName = getUserDisplayName(user);
  var selectedValue = getUserSelectedDisplayValue(user);

  return React.createElement(
    "div",
    { "key": "user-" + (user["user-id"] + "-" + user["email"]),
      "className": "oc-mention-option " + (props.selectedIndex === props.index? "active" : ""),
      "data-name": displayName,
      "data-user-id": user["user-id"],
      "data-slack-username": user["slack-username"],
      onMouseEnter: function(e){
        props.hoverItem(e, props.index);
      },
      onMouseLeave: function(e){
        props.blurItem(e, props.index);
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
      { "className": "oc-mention-option-subline" },
      user["email"] || user["slack-username"]
    )
  );
};



// function CustomizedTagComponent(props) {
class CustomizedTagComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {selectedIndex: null};
  }

  keyPress(e) {
    e = e || window.event;
    var node = ReactDOM.findDOMNode(this);
    var options = $(node).find(".oc-mention-option");
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
        // Enter
        case 13:
          if (this.state.selectedIndex !== null) {
            var user = this.filteredUsers()[this.state.selectedIndex];
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
    this.setState({selectedIndex: null});
  }

  selectItem(user){
    var selectedValue = getUserSelectedDisplayValue(user);
    this.props.selectMentionCallback("@" + selectedValue, user);
  }

  filteredUsers(){
    const trigger = this.props.currentMentionText.substring(0, 1);
    const currentText = this.props.currentMentionText.substring(1, this.props.currentMentionText.length).toLowerCase();

    var mappedUsers = this.props.users.map(function (user, i) {
      var activeUser = user["status"] === "active",
          filteredSlackUsernames = [];
      if (activeUser && user["slack-users"] && Object.values(user["slack-users"]).length > 0) {
        Object.values(user["slack-users"]).map(function(slackUser){
          if (slackUser["display-name"] && slackUser["display-name"].toLowerCase().indexOf(currentText) !== -1)
            filteredSlackUsernames.push(slackUser);
        });
      }
      if (user["name"] && user["name"].toLowerCase().indexOf(currentText) !== -1)
        return Object.assign(user, { "selectedKey": "name" });
      else if (user["first-name"] && user["first-name"].toLowerCase().indexOf(currentText) !== -1)
        return Object.assign(user, { "selectedKey": "first-name" });
      else if (user["last-name"] && user["last-name"].toLowerCase().indexOf(currentText) !== -1)
        return Object.assign(user, { "selectedKey": "last-name" });
      else if (activeUser && filteredSlackUsernames.length > 0)
        return Object.assign(user, { "selectedKey": "slack-username",
                                     "slack-username": filteredSlackUsernames[0]["display-name"] });
      else if (!activeUser && user["slack-display-name"] && user["slack-display-name"].toLowerCase().indexOf(currentText) !== -1)
        return Object.assign(user, { "selectedKey": "slack-display-name" });
      else if (user["email"] && user["email"].toLowerCase().indexOf(currentText) !== -1)
        return Object.assign(user, { "selectedKey": "email" });
      else return user;
    });
    return mappedUsers.filter(function (user) {
      return !!user["selectedKey"];
    });
  }

  render() {
    var that = this;
    return React.createElement(
      "div",
      { "className": "oc-mention-options",
        "contentEditable": false },
      React.createElement(
        "div",
        { "className": "oc-mention-options-list" },
        this.filteredUsers().map(function (user, i) {
          return ListItem({user: user,
                           index: i,
                           selectedIndex: that.state.selectedIndex,
                           clickCb: that.selectItem.bind(that),
                           hoverItem: that.hoverItem.bind(that),
                           blurItem: that.blurItem.bind(that)})
        })));
  }
}
