function CustomizedTagComponent(props) {
  const trigger = props.currentMentionText.substring(0, 1);
  const currentText = props.currentMentionText.substring(1, props.currentMentionText.length).toLowerCase();

  var mappedUsers = props.users.map(function(user, i) {
    if (user["name"] && user["name"].toLowerCase().indexOf(currentText) !== -1)
      return Object.assign(user, {"selectedKey": "name"});
    else if (user["first-name"] && user["first-name"].toLowerCase().indexOf(currentText) !== -1)
      return Object.assign(user, {"selectedKey": "first-name"});
    else if (user["last-name"] && user["last-name"].toLowerCase().indexOf(currentText) !== -1)
      return Object.assign(user, {"selectedKey": "last-name"});
    else if (user["slack-username"] && user["slack-username"].toLowerCase().indexOf(currentText) !== -1)
      return Object.assign(user, {"selectedKey": "slack-username"});
    else if (user["email"] && user["email"].toLowerCase().indexOf(currentText) !== -1)
      return Object.assign(user, {"selectedKey": "email"});
    else 
      return user;
  });
  var selectedUsers = mappedUsers.filter(function(user){
    return !!user["selectedKey"];
  });

  return React.createElement(
    "div",
    { "className": "oc-mention-options" },
    selectedUsers.map(function (user, i) {
      return React.createElement(
        "div",
        { "className": "oc-mention-option",
          "data-name": user["name"] || user["first-name"] + " " + user["last-name"],
          "data-user-id": user["user-id"],
          "data-slack-username": user["slack-username"],
          "key": "oc-mention-option-" + i,
          "onClick": function(a, b, c) {
            props.selectMentionCallback("@" + user[user["selectedKey"]], user)}},
        user[user["selectedKey"]]
      );
    })
  );
}
