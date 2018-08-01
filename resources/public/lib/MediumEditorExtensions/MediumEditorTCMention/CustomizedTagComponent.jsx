export function CustomizedTagComponent (props) {
  const trigger = props.currentMentionText.substring(0, 1);

  var users = [{"name": "Iacopo Carraro",
                "slack-username": "iacopo",
                "user-id": "IACO-1234-1234"},
               {"name": "Sean Johnson",
                "slack-username": "sean",
                "user-id": "SEAN-1234-1234"},
               {"name": "Stuart Levinson",
                "slack-username": "stuart",
                "user-id": "STUA-1234-1234"},
               {"name": "Nathan Zorn",
                "slack-username": "nathan"}];

  return (
    <div class="oc-mention-options">
      {users.map(function(user, i){
        <div class="oc-mention-option" data-name={user["name"]} data-user-id={user["user-id"]} data-slack-username={user["slack-username"]}>
          {user["name"]}
        </div>})
      }
    </div>
  );
}