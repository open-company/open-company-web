// function CustomizedTagComponent(props) {
//   const trigger = props.currentMentionText.substring(0, 1);
//   const currentText = props.currentMentionText.substring(1, props.currentMentionText.length).toLowerCase();

function CustomizedTagComponent(props) {
  const trigger = props.currentMentionText.substring(0, 1);
  const currentText = props.currentMentionText.substring(1, props.currentMentionText.length).toLowerCase();

  var mappedUsers = props.users.map(function (user, i) {
    if (user["name"] && user["name"].toLowerCase().indexOf(currentText) !== -1)
      return Object.assign(user, { "selectedKey": "name" });
    else if (user["first-name"] && user["first-name"].toLowerCase().indexOf(currentText) !== -1)
      return Object.assign(user, { "selectedKey": "first-name" });
    else if (user["last-name"] && user["last-name"].toLowerCase().indexOf(currentText) !== -1)
      return Object.assign(user, { "selectedKey": "last-name" });
    else if (user["slack-username"] && user["slack-username"].toLowerCase().indexOf(currentText) !== -1)
      return Object.assign(user, { "selectedKey": "slack-username" });
    else if (user["email"] && user["email"].toLowerCase().indexOf(currentText) !== -1)
      return Object.assign(user, { "selectedKey": "email" });
    else return user;
  });
  var selectedUsers = mappedUsers.filter(function (user) {
    return !!user["selectedKey"];
  });

  return React.createElement(
    "div",
    { "className": "oc-mention-options" },
    React.createElement(
      "div",
      { "className": "oc-mention-options-list" },
      selectedUsers.map(function (user, i) {
        var avatarStyle = { "backgroundImage": "url(" + user["avatar-url"] + ")" };
        var displayName = (user["name"] && user["name"].length > 0) ? user["name"] : (user["first-name"] + " " + user["last-name"]);
        var selectedValue = (user["selectedKey"] === "slack-username")? user["slack-username"] : displayName;
        return React.createElement(
          "div",
          { "key": "user-" + (user["user-id"] + "-" + user["email"]),
            "className": "oc-mention-option",
            "data-name": displayName,
            "data-user-id": user["user-id"],
            "data-slack-username": user["slack-username"],
            onClick: function(){
              props.selectMentionCallback("@" + selectedValue);
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
      })
    )
  );
}
