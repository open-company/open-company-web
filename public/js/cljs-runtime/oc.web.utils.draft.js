goog.provide('oc.web.utils.draft');
oc.web.utils.draft.delete_draft_clicked = (function oc$web$utils$draft$delete_draft_clicked(draft,e){
oc.web.lib.utils.event_stop(e);

var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"delete-entry",new cljs.core.Keyword(null,"message","message",-406056002),"Delete this draft?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"No",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes",new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.actions.activity.activity_delete(draft);

return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});

//# sourceMappingURL=oc.web.utils.draft.js.map
