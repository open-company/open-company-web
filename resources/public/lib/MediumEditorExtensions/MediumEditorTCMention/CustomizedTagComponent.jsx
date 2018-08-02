// This is used to create CustomizedTagComponent.js using a tool like: http://infoheap.com/online-react-jsx-to-javascript/
// to switch from
export function CustomizedTagComponent(props) {
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

  return (
    <div class="oc-mention-options">
      <label class="oc-mention-options-title">@mention to tag someone</label>
      <div class="oc-mention-options-list">
        {selectedUsers.map(function(user, i){
          var avatarStyle = {"backgroundImage": user["avatar-url"]};
          return (
            <div class="oc-mention-option"
              data-name={user["name"] || user["first-name"] + " " + user["last-name"]}
              data-user-id={user["user-id"]}
              data-slack-username={user["slack-username"]}
              onClick={props.selectMentionCallback("@" + user[user["selectedKey"]], user)}>
              <div class="oc-mention-option-avatar" style={avatarStyle}></div>
              <div class="oc-mention-option-title">{user["name"]}</div>
              <div class="oc-mention-option-subline">{user["email"] || user["slack-username"]}</div>
            </div>)
        })}
      </div>
    </div>
  );
}