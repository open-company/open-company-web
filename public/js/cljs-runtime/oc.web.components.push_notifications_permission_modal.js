goog.provide('oc.web.components.push_notifications_permission_modal');
oc.web.components.push_notifications_permission_modal.push_notifications_permission_modal = rum.core.build_defcs((function (s){
return React.createElement("div",({"className": "push-notifications-permission-modal"}),React.createElement("div",({"className": "modal-header"}),React.createElement("button",({"onClick": (function (){
return oc.web.actions.user.deny_push_notification_permission();
}), "className": "modal-close-bt"})),React.createElement("div",({"className": "modal-title"}),"Notifications")),React.createElement("div",({"className": "modal-body"}),React.createElement("div",({"className": "carrot-icon"}),React.createElement("div",({"className": "notification-bubble"}),"3")),React.createElement("p",({"className": "modal-body-text"}),"Get notified when your team shares on Wut"),React.createElement("button",({"onClick": (function (){
return oc.web.expo.bridge_request_push_notification_permission_BANG_();
}), "className": "mlb-reset enable-notifications-bt"}),"Enable notifications"),React.createElement("button",({"onClick": (function (){
return oc.web.actions.user.deny_push_notification_permission();
}), "className": "mlb-reset no-thanks-btn"}),"No thanks, skip for now")));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.mixins.ui.no_scroll_mixin], null),"push-notifications-permission-modal");

//# sourceMappingURL=oc.web.components.push_notifications_permission_modal.js.map
