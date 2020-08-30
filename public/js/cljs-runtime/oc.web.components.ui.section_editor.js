goog.provide('oc.web.components.ui.section_editor');
/**
 * Given a user and a query string, return true if first-name, last-name or email contains it.
 */
oc.web.components.ui.section_editor.filter_user_by_query = (function oc$web$components$ui$section_editor$filter_user_by_query(user,query){
var lower_query = cuerdas.core.lower(query);
var first_name = new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user);
var last_name = new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user);
var lower_name = cuerdas.core.lower([cljs.core.str.cljs$core$IFn$_invoke$arity$1(first_name)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(last_name)].join(''));
var lower_email = cuerdas.core.lower(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user));
return (((lower_name.search(lower_query) >= (0))) || ((lower_email.search(lower_query) >= (0))));
});
oc.web.components.ui.section_editor.compare_users = (function oc$web$components$ui$section_editor$compare_users(user_1,user_2){
return cljs.core.compare(cuerdas.core.lower(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user_1)),cuerdas.core.lower(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user_2)));
});
/**
 * Given a list of users and a query string, return those that match the given query.
 */
oc.web.components.ui.section_editor.filter_users = (function oc$web$components$ui$section_editor$filter_users(users_list,query){
var filtered_users = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46299_SHARP_){
return oc.web.components.ui.section_editor.filter_user_by_query(p1__46299_SHARP_,query);
}),users_list);
return cljs.core.sort.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.section_editor.compare_users,filtered_users);
});
/**
 * Filter users that are not arleady viewer or author users.
 */
oc.web.components.ui.section_editor.get_addable_users = (function oc$web$components$ui$section_editor$get_addable_users(section_editing,users){
var already_in_ids = cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"viewers","viewers",-415894011).cljs$core$IFn$_invoke$arity$1(section_editing),new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_editing));
var without_self = cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__46300_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46300_SHARP_),oc.web.lib.jwt.user_id());
}),users);
return cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__46301_SHARP_){
return cljs.core.some(cljs.core.PersistentHashSet.createAsIfByAssoc([new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46301_SHARP_)]),already_in_ids);
}),without_self);
});
oc.web.components.ui.section_editor.private_access = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.access-item.private-access","div.access-item.private-access",-724142610),"Team members you invite"], null);
oc.web.components.ui.section_editor.team_access = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.access-item.team-access","div.access-item.team-access",1163168615),"Anyone on the team"], null);
oc.web.components.ui.section_editor.public_access = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.access-item.public-access","div.access-item.public-access",-1291764274),"Open for the public"], null);
oc.web.components.ui.section_editor.section_for_editing = (function oc$web$components$ui$section_editor$section_for_editing(section_data){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(section_data,new cljs.core.Keyword(null,"authors","authors",2063018172),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__46302_SHARP_){
if(cljs.core.map_QMARK_(p1__46302_SHARP_)){
return new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46302_SHARP_);
} else {
return p1__46302_SHARP_;
}
}),new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_data))),new cljs.core.Keyword(null,"viewers","viewers",-415894011),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__46303_SHARP_){
if(cljs.core.map_QMARK_(p1__46303_SHARP_)){
return new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46303_SHARP_);
} else {
return p1__46303_SHARP_;
}
}),new cljs.core.Keyword(null,"viewers","viewers",-415894011).cljs$core$IFn$_invoke$arity$1(section_data)));
});
oc.web.components.ui.section_editor.check_section_name_error = (function oc$web$components$ui$section_editor$check_section_name_error(s){
var section_editing = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)));
var org_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321)));
var boards = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46304_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__46304_SHARP_),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section_editing));
}),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data));
var sec_name = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","section-name","oc.web.components.ui.section-editor/section-name",1698485784).cljs$core$IFn$_invoke$arity$1(s));
var equal_names = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46305_SHARP_){
return sec_name.match((new RegExp(["^",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(p1__46305_SHARP_)),"$"].join(''),"ig")));
}),boards);
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","pre-flight-check","oc.web.components.ui.section-editor/pre-flight-check",-1531999935).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","pre-flight-ok","oc.web.components.ui.section-editor/pre-flight-ok",-1735583943).cljs$core$IFn$_invoke$arity$1(s),false);

if((cljs.core.count(equal_names) > (0))){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], null),(function (p1__46306_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__46306_SHARP_,new cljs.core.Keyword(null,"section-name-error","section-name-error",-581249210),oc.web.lib.utils.section_name_exists_error),new cljs.core.Keyword(null,"loading","loading",-737050189));
})], null));
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"section-name-error","section-name-error",-581249210).cljs$core$IFn$_invoke$arity$1(section_editing))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167),new cljs.core.Keyword(null,"section-name-error","section-name-error",-581249210)], null),null], null));
} else {
}

if((cljs.core.count(sec_name) >= oc.web.actions.section.min_section_name_length)){
oc.web.actions.section.pre_flight_check((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","editing-existing-section","oc.web.components.ui.section-editor/editing-existing-section",2018495799).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section_editing):null),sec_name);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","pre-flight-check","oc.web.components.ui.section-editor/pre-flight-check",-1531999935).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","pre-flight-check","oc.web.components.ui.section-editor/pre-flight-check",-1531999935).cljs$core$IFn$_invoke$arity$1(s),false);
}
}
});
oc.web.components.ui.section_editor.section_editor = rum.core.build_defcs((function (s,initial_section_data,on_change,from_section_picker){
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var no_drafts_boards = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46307_SHARP_){
return ((cljs.core.not(new cljs.core.Keyword(null,"draft","draft",1421831058).cljs$core$IFn$_invoke$arity$1(p1__46307_SHARP_))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__46307_SHARP_),oc.web.lib.utils.default_drafts_board_slug)));
}),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data));
var section_editing = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167));
var section_data = ((cljs.core.seq(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section_editing)))?org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"board-data","board-data",1372958925)):section_editing);
var team_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"team-data","team-data",-732020079));
var slack_teams = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"team-channels","team-channels",321697292));
var show_slack_channels_QMARK_ = (cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core._PLUS_,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__46308_SHARP_){
return cljs.core.count(new cljs.core.Keyword(null,"channels","channels",1132759174).cljs$core$IFn$_invoke$arity$1(p1__46308_SHARP_));
}),slack_teams)) > (0));
var channel_name = (cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","editing-existing-section","oc.web.components.ui.section-editor/editing-existing-section",2018495799).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.Keyword(null,"channel-name","channel-name",-188505362).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-mirror","slack-mirror",-506844872).cljs$core$IFn$_invoke$arity$1(section_data)):null);
var roster = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"team-roster","team-roster",-1945092859));
var all_users_data = (cljs.core.truth_(team_data)?new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(team_data):new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(roster));
var slack_orgs = new cljs.core.Keyword(null,"slack-orgs","slack-orgs",-1806634042).cljs$core$IFn$_invoke$arity$1(team_data);
var cur_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var slack_users = new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(cur_user_data);
var current_user_id = oc.web.lib.jwt.user_id();
var can_change = (function (){var or__4126__auto__ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section_editing),oc.web.lib.utils.default_section_slug);
if(or__4126__auto__){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = cljs.core.some(cljs.core.PersistentHashSet.createAsIfByAssoc([current_user_id]),new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_editing));
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return oc.web.lib.jwt.is_admin_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data));
}
}
})();
var last_section_standing = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.count(no_drafts_boards),(1));
var disallow_public_board_QMARK_ = (function (){var and__4115__auto__ = new cljs.core.Keyword(null,"content-visibility","content-visibility",550828155).cljs$core$IFn$_invoke$arity$1(org_data);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"disallow-public-board","disallow-public-board",-716419746).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"content-visibility","content-visibility",550828155).cljs$core$IFn$_invoke$arity$1(org_data));
} else {
return and__4115__auto__;
}
})();
var wrapped_on_change = (function (exit_cb){
if(cljs.core.truth_(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)))))){
return oc.web.components.ui.alert_modal.show_alert(new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"section-settings-unsaved-edits",new cljs.core.Keyword(null,"message","message",-406056002),"Leave without saving your changes?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"Stay",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Lose changes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.components.ui.alert_modal.hide_alert();

return (on_change.cljs$core$IFn$_invoke$arity$3 ? on_change.cljs$core$IFn$_invoke$arity$3(null,null,exit_cb) : on_change.call(null,null,null,exit_cb));
})], null));
} else {
return (on_change.cljs$core$IFn$_invoke$arity$3 ? on_change.cljs$core$IFn$_invoke$arity$3(null,null,exit_cb) : on_change.call(null,null,null,exit_cb));
}
});
return React.createElement("div",({"onClick": (function (p1__46309_SHARP_){
if(cljs.core.truth_(oc.web.lib.utils.event_inside_QMARK_(p1__46309_SHARP_,rum.core.ref_node(s,new cljs.core.Keyword(null,"section-editor","section-editor",436383962))))){
return null;
} else {
oc.web.lib.utils.event_stop(p1__46309_SHARP_);

return wrapped_on_change(oc.web.actions.nav_sidebar.close_all_panels);
}
}), "className": "section-editor-container"}),React.createElement("button",({"onClick": (function (){
return wrapped_on_change(oc.web.actions.nav_sidebar.close_all_panels);
}), "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"ref": "section-editor", "onClick": (function (e){
if(cljs.core.truth_(oc.web.lib.utils.event_inside_QMARK_(e,rum.core.ref_node(s,"section-editor-add-access-list")))){
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-access-list","oc.web.components.ui.section-editor/show-access-list",840836864).cljs$core$IFn$_invoke$arity$1(s),false);
}

if(cljs.core.truth_(oc.web.lib.utils.event_inside_QMARK_(e,rum.core.ref_node(s,"private-users-search")))){
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-search-results","oc.web.components.ui.section-editor/show-search-results",-1563787540).cljs$core$IFn$_invoke$arity$1(s),false);
}

if(cljs.core.truth_(oc.web.lib.utils.event_inside_QMARK_(e,rum.core.ref_node(s,"section-editor-add-private-users")))){
return null;
} else {
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-dropdown","oc.web.components.ui.section-editor/show-edit-user-dropdown",1636255352).cljs$core$IFn$_invoke$arity$1(s),null);
}
}), "className": "section-editor group"}),React.createElement("div",({"className": "section-editor-header"}),React.createElement("div",({"dangerouslySetInnerHTML": oc.web.lib.utils.emojify((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","editing-existing-section","oc.web.components.ui.section-editor/editing-existing-section",2018495799).cljs$core$IFn$_invoke$arity$1(s)))?"Team settings":"Create topic")), "className": "section-editor-header-title"})),(function (){var disable_bt = (function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","saving","oc.web.components.ui.section-editor/saving",1891466044).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = (cljs.core.count(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","section-name","oc.web.components.ui.section-editor/section-name",1698485784).cljs$core$IFn$_invoke$arity$1(s))) < oc.web.actions.section.min_section_name_length);
if(or__4126__auto____$1){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","pre-flight-check","oc.web.components.ui.section-editor/pre-flight-check",-1531999935).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
var or__4126__auto____$3 = new cljs.core.Keyword(null,"pre-flight-loading","pre-flight-loading",-1550718393).cljs$core$IFn$_invoke$arity$1(section_editing);
if(cljs.core.truth_(or__4126__auto____$3)){
return or__4126__auto____$3;
} else {
return cljs.core.seq(new cljs.core.Keyword(null,"section-name-error","section-name-error",-581249210).cljs$core$IFn$_invoke$arity$1(section_editing));
}
}
}
}
})();
return React.createElement("button",({"onClick": (function (_){
if(((cljs.core.not(disable_bt)) && (cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","saving","oc.web.components.ui.section-editor/saving",1891466044).cljs$core$IFn$_invoke$arity$1(s),false,true)))){
var section_node = rum.core.ref_node(s,"section-name");
var section_name = clojure.string.trim(section_node.value);
var personal_note_node = rum.core.ref_node(s,"personal-note");
var personal_note = (cljs.core.truth_(personal_note_node)?personal_note_node.innerText:null);
var success_cb = (function (p1__46310_SHARP_){
if(cljs.core.fn_QMARK_(on_change)){
return (on_change.cljs$core$IFn$_invoke$arity$3 ? on_change.cljs$core$IFn$_invoke$arity$3(p1__46310_SHARP_,personal_note,oc.web.actions.nav_sidebar.hide_section_editor) : on_change.call(null,p1__46310_SHARP_,personal_note,oc.web.actions.nav_sidebar.hide_section_editor));
} else {
return null;
}
});
return oc.web.actions.section.section_save_create(section_editing,section_name,success_cb);
} else {
return null;
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","save-bt",(cljs.core.truth_(disable_bt)?"disabled":null)], null))}),"Save");
})(),React.createElement("button",({"onClick": (function (){
return wrapped_on_change(oc.web.actions.nav_sidebar.hide_section_editor);
}), "className": "mlb-reset cancel-bt"}),"Back")),React.createElement("div",({"className": "section-editor-add"}),React.createElement("div",({"className": "section-editor-add-label"}),React.createElement("span",({"className": "section-name"}),"Topic name")),sablono.interpreter.create_element("input",({"value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","section-name","oc.web.components.ui.section-editor/section-name",1698485784).cljs$core$IFn$_invoke$arity$1(s)), "placeholder": "Topic name", "ref": "section-name", "maxLength": (50), "onChange": (function (e){
var next_section_name = e.target.value;
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","section-name","oc.web.components.ui.section-editor/section-name",1698485784).cljs$core$IFn$_invoke$arity$1(s)),next_section_name)){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","section-name","oc.web.components.ui.section-editor/section-name",1698485784).cljs$core$IFn$_invoke$arity$1(s),next_section_name);

if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","section-name-check-timeout","oc.web.components.ui.section-editor/section-name-check-timeout",1302312762).cljs$core$IFn$_invoke$arity$1(s)))){
window.clearTimeout(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","section-name-check-timeout","oc.web.components.ui.section-editor/section-name-check-timeout",1302312762).cljs$core$IFn$_invoke$arity$1(s)));
} else {
}

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","section-name-check-timeout","oc.web.components.ui.section-editor/section-name-check-timeout",1302312762).cljs$core$IFn$_invoke$arity$1(s),oc.web.lib.utils.after((500),(function (){
return oc.web.components.ui.section_editor.check_section_name_error(s);
})));
} else {
return null;
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["section-editor-add-name","oc-input",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"preflight-ok","preflight-ok",-1889129655),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","pre-flight-ok","oc.web.components.ui.section-editor/pre-flight-ok",-1735583943).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"preflight-error","preflight-error",1631595763),new cljs.core.Keyword(null,"section-name-error","section-name-error",-581249210).cljs$core$IFn$_invoke$arity$1(section_editing)], null))], null))})),React.createElement("div",({"className": "section-editor-add-label"}),React.createElement("span",({"className": "section-description"}),"Description")),sablono.interpreter.create_element("textarea",({"value": (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"description","description",-1428560544).cljs$core$IFn$_invoke$arity$1(section_editing);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(), "ref": "section-description", "placeholder": "Topic description", "columns": (2), "maxLength": (256), "onChange": (function (e){
oc.web.lib.utils.event_stop(e);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], null),(function (p1__46311_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__46311_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"description","description",-1428560544),e.target.value,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true], null)], 0));
})], null));
}), "className": "section-editor-description oc-input"})),React.createElement("div",({"className": "section-editor-add-label"}),"Topic security"),React.createElement("div",({"onClick": (function (p1__46312_SHARP_){
oc.web.lib.utils.event_stop(p1__46312_SHARP_);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-access-list","oc.web.components.ui.section-editor/show-access-list",840836864).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-access-list","oc.web.components.ui.section-editor/show-access-list",840836864).cljs$core$IFn$_invoke$arity$1(s))));
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["section-editor-add-access","oc-input",(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-access-list","oc.web.components.ui.section-editor/show-access-list",840836864).cljs$core$IFn$_invoke$arity$1(s)))?"active":null)], null))}),sablono.interpreter.interpret((function (){var G__46334 = new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(section_editing);
switch (G__46334) {
case "private":
return oc.web.components.ui.section_editor.private_access;

break;
case "public":
return oc.web.components.ui.section_editor.public_access;

break;
default:
return oc.web.components.ui.section_editor.team_access;

}
})())),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-access-list","oc.web.components.ui.section-editor/show-access-list",840836864).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-add-access-list","div.section-editor-add-access-list",1633961799),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),"section-editor-add-access-list"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.access-list-row","div.access-list-row",-686008417),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
oc.web.lib.utils.event_stop(e);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-access-list","oc.web.components.ui.section-editor/show-access-list",840836864).cljs$core$IFn$_invoke$arity$1(s),false);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], null),(function (p1__46313_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__46313_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"access","access",2027349272),"team",new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true], null)], 0));
})], null));
})], null),oc.web.components.ui.section_editor.team_access], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.access-list-row","div.access-list-row",-686008417),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
oc.web.lib.utils.event_stop(e);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-access-list","oc.web.components.ui.section-editor/show-access-list",840836864).cljs$core$IFn$_invoke$arity$1(s),false);

if(show_slack_channels_QMARK_){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","slack-enabled","oc.web.components.ui.section-editor/slack-enabled",1764073527).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], null),(function (p1__46314_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__46314_SHARP_,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"access","access",2027349272),"private",new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true,new cljs.core.Keyword(null,"authors","authors",2063018172),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.set(new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_editing)),current_user_id),new cljs.core.Keyword(null,"slack-mirror","slack-mirror",-506844872),((show_slack_channels_QMARK_)?null:new cljs.core.Keyword(null,"slack-mirror","slack-mirror",-506844872).cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.section_editor.section_editor))], null)], 0));
})], null));
})], null),oc.web.components.ui.section_editor.private_access], null),(cljs.core.truth_(disallow_public_board_QMARK_)?null:new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.access-list-row","div.access-list-row",-686008417),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
oc.web.lib.utils.event_stop(e);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-access-list","oc.web.components.ui.section-editor/show-access-list",840836864).cljs$core$IFn$_invoke$arity$1(s),false);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], null),(function (p1__46315_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__46315_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"access","access",2027349272),"public",new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true], null)], 0));
})], null));
})], null),oc.web.components.ui.section_editor.public_access], null))], null):null)),sablono.interpreter.interpret(((show_slack_channels_QMARK_)?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-add-label.top-separator","div.section-editor-add-label.top-separator",-618323082),"Auto-share to Slack",((show_slack_channels_QMARK_)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.info","span.info",-1211216201)], null):null),((show_slack_channels_QMARK_)?(function (){var G__46335 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"selected","selected",574897764),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","slack-enabled","oc.web.components.ui.section-editor/slack-enabled",1764073527).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"did-change-cb","did-change-cb",116554135),(function (v){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","slack-enabled","oc.web.components.ui.section-editor/slack-enabled",1764073527).cljs$core$IFn$_invoke$arity$1(s),v);

if(cljs.core.truth_(v)){
return null;
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], null),(function (p1__46316_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__46316_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"slack-mirror","slack-mirror",-506844872),null,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true], null)], 0));
})], null));
}
})], null);
return (oc.web.components.ui.carrot_switch.carrot_switch.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.carrot_switch.carrot_switch.cljs$core$IFn$_invoke$arity$1(G__46335) : oc.web.components.ui.carrot_switch.carrot_switch.call(null,G__46335));
})():null)], null):null)),((show_slack_channels_QMARK_)?React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["section-editor-add-slack-channel","group",(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","slack-enabled","oc.web.components.ui.section-editor/slack-enabled",1764073527).cljs$core$IFn$_invoke$arity$1(s)))?null:"disabled")], null))}),sablono.interpreter.interpret((function (){var G__46336 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"initial-value","initial-value",470619381),(cljs.core.truth_(channel_name)?["#",cljs.core.str.cljs$core$IFn$_invoke$arity$1(channel_name)].join(''):null),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (team,channel){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], null),(function (p1__46317_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__46317_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"slack-mirror","slack-mirror",-506844872),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"channel-id","channel-id",138191095),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(channel),new cljs.core.Keyword(null,"channel-name","channel-name",-188505362),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(channel),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(team)], null),new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true], null)], 0));
})], null));
})], null);
return (oc.web.components.ui.slack_channels_dropdown.slack_channels_dropdown.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.slack_channels_dropdown.slack_channels_dropdown.cljs$core$IFn$_invoke$arity$1(G__46336) : oc.web.components.ui.slack_channels_dropdown.slack_channels_dropdown.call(null,G__46336));
})())):sablono.interpreter.interpret(((((cljs.core.not(oc.web.lib.jwt.team_has_bot_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(team_data)))) && ((cljs.core.count(slack_users) > (0))) && ((cljs.core.count(slack_orgs) > (0)))))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-enable-slack-bot.group","div.section-editor-enable-slack-bot.group",1387837522),"Automatically share updates to Slack? ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.enable-slack-bot-bt","button.mlb-reset.enable-slack-bot-bt",1669373491),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (_){
return oc.web.actions.org.bot_auth.cljs$core$IFn$_invoke$arity$variadic(team_data,cur_user_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.router.get_token()], 0));
})], null),"Add Wut bot"], null)], null):null))),sablono.interpreter.interpret(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(section_editing),"public"))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-access-public-description","div.section-editor-access-public-description",-459078315),"Public topics are visible to the world, including search engines."], null):null)),sablono.interpreter.interpret(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(section_editing),"private"))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-add-label.top-separator","div.section-editor-add-label.top-separator",-618323082),"Add members to this private topic"], null):null)),sablono.interpreter.interpret(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(section_editing),"private"))?(function (){var query = new cljs.core.Keyword("oc.web.components.ui.section-editor","query","oc.web.components.ui.section-editor/query",1438702739).cljs$core$IFn$_invoke$arity$1(s);
var available_users = oc.web.utils.user.filter_active_users(all_users_data);
var addable_users = oc.web.components.ui.section_editor.get_addable_users(section_editing,available_users);
var filtered_users = oc.web.components.ui.section_editor.filter_users(addable_users,cljs.core.deref(query));
if(cljs.core.truth_(can_change)){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-private-users-search","div.section-editor-private-users-search",1542768128),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),"private-users-search"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.oc-input","input.oc-input",-1802295716),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.hide_class,new cljs.core.Keyword(null,"value","value",305978217),cljs.core.deref(query),new cljs.core.Keyword(null,"type","type",1174270348),"text",new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),"Select a member...",new cljs.core.Keyword(null,"on-focus","on-focus",-13737624),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-search-results","oc.web.components.ui.section-editor/show-search-results",-1563787540).cljs$core$IFn$_invoke$arity$1(s),true);
}),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__46318_SHARP_){
var q = p1__46318_SHARP_.target.value;
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-search-results","oc.web.components.ui.section-editor/show-search-results",-1563787540).cljs$core$IFn$_invoke$arity$1(s),cljs.core.seq(q));

return cljs.core.reset_BANG_(query,q);
})], null)], null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-search-results","oc.web.components.ui.section-editor/show-search-results",-1563787540).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-private-users-results","div.section-editor-private-users-results",-784657713),(((cljs.core.count(filtered_users) > (0)))?(function (){var iter__4529__auto__ = (function oc$web$components$ui$section_editor$iter__46337(s__46338){
return (new cljs.core.LazySeq(null,(function (){
var s__46338__$1 = s__46338;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46338__$1);
if(temp__5735__auto__){
var s__46338__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46338__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46338__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46340 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46339 = (0);
while(true){
if((i__46339 < size__4528__auto__)){
var u = cljs.core._nth(c__4527__auto__,i__46339);
var team_user = cljs.core.some(((function (i__46339,u,c__4527__auto__,size__4528__auto__,b__46340,s__46338__$2,temp__5735__auto__,query,available_users,addable_users,filtered_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change){
return (function (p1__46319_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46319_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u))){
return p1__46319_SHARP_;
} else {
return null;
}
});})(i__46339,u,c__4527__auto__,size__4528__auto__,b__46340,s__46338__$2,temp__5735__auto__,query,available_users,addable_users,filtered_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change))
,new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(roster));
var user = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([u,team_user], 0));
var user_type = oc.web.utils.user.get_user_type.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user,org_data,section_editing], 0));
cljs.core.chunk_append(b__46340,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-private-users-result","div.section-editor-private-users-result",1570022789),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.hide_class,new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__46339,team_user,user,user_type,u,c__4527__auto__,size__4528__auto__,b__46340,s__46338__$2,temp__5735__auto__,query,available_users,addable_users,filtered_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change){
return (function (){
cljs.core.reset_BANG_(query,"");

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-search-results","oc.web.components.ui.section-editor/show-search-results",-1563787540).cljs$core$IFn$_invoke$arity$1(s),false);

return oc.web.actions.section.private_section_user_add(user,user_type);
});})(i__46339,team_user,user,user_type,u,c__4527__auto__,size__4528__auto__,b__46340,s__46338__$2,temp__5735__auto__,query,available_users,addable_users,filtered_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change))
,new cljs.core.Keyword(null,"ref","ref",1289896967),["add-user-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user))].join('')], null),(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(user) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,user)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.name","div.name",1027675228),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user)], null)], null));

var G__46355 = (i__46339 + (1));
i__46339 = G__46355;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46340),oc$web$components$ui$section_editor$iter__46337(cljs.core.chunk_rest(s__46338__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46340),null);
}
} else {
var u = cljs.core.first(s__46338__$2);
var team_user = cljs.core.some(((function (u,s__46338__$2,temp__5735__auto__,query,available_users,addable_users,filtered_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change){
return (function (p1__46319_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46319_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u))){
return p1__46319_SHARP_;
} else {
return null;
}
});})(u,s__46338__$2,temp__5735__auto__,query,available_users,addable_users,filtered_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change))
,new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(roster));
var user = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([u,team_user], 0));
var user_type = oc.web.utils.user.get_user_type.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user,org_data,section_editing], 0));
return cljs.core.cons(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-private-users-result","div.section-editor-private-users-result",1570022789),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.hide_class,new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (team_user,user,user_type,u,s__46338__$2,temp__5735__auto__,query,available_users,addable_users,filtered_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change){
return (function (){
cljs.core.reset_BANG_(query,"");

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-search-results","oc.web.components.ui.section-editor/show-search-results",-1563787540).cljs$core$IFn$_invoke$arity$1(s),false);

return oc.web.actions.section.private_section_user_add(user,user_type);
});})(team_user,user,user_type,u,s__46338__$2,temp__5735__auto__,query,available_users,addable_users,filtered_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change))
,new cljs.core.Keyword(null,"ref","ref",1289896967),["add-user-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user))].join('')], null),(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(user) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,user)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.name","div.name",1027675228),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user)], null)], null),oc$web$components$ui$section_editor$iter__46337(cljs.core.rest(s__46338__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(filtered_users);
})():new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-private-users-result.no-more-invites","div.section-editor-private-users-result.no-more-invites",408414173),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.name","div.name",1027675228),["Looks like you'll need to invite more people before you can add them. ","You can do that in "].join(''),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"invite","invite",126355381));
})], null),"Wut topic settings"], null),"."], null)], null))], null):null)], null);
} else {
return null;
}
})():null)),sablono.interpreter.interpret(((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(section_editing),"private")) && (((cljs.core.count(new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_editing)) + cljs.core.count(new cljs.core.Keyword(null,"viewers","viewers",-415894011).cljs$core$IFn$_invoke$arity$1(section_editing))) > (0)))))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-add-label.group","div.section-editor-add-label.group",341431598),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.main-label","span.main-label",-1914795380),"Team members"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.role-header","span.role-header",-1243032027),"Access"], null)], null):null)),sablono.interpreter.interpret(((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(section_editing),"private")) && (((cljs.core.count(new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_editing)) + cljs.core.count(new cljs.core.Keyword(null,"viewers","viewers",-415894011).cljs$core$IFn$_invoke$arity$1(section_editing))) > (0)))))?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-add-private-users","div.section-editor-add-private-users",-680633767),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),"section-editor-add-private-users"], null),(function (){var user_id = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-dropdown","oc.web.components.ui.section-editor/show-edit-user-dropdown",1636255352).cljs$core$IFn$_invoke$arity$1(s));
var user_type = (cljs.core.truth_(oc.web.lib.utils.in_QMARK_(new cljs.core.Keyword(null,"viewers","viewers",-415894011).cljs$core$IFn$_invoke$arity$1(section_editing),user_id))?new cljs.core.Keyword(null,"viewer","viewer",-783949853):new cljs.core.Keyword(null,"author","author",2111686192));
var team_user = cljs.core.some((function (p1__46320_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46320_SHARP_),user_id)){
return p1__46320_SHARP_;
} else {
return null;
}
}),all_users_data);
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-add-private-users-dropdown-container","div.section-editor-add-private-users-dropdown-container",1430610373),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"top","top",-1856271961),[cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-top","oc.web.components.ui.section-editor/show-edit-user-top",-2005930239).cljs$core$IFn$_invoke$arity$1(s)) + (-114))),"px"].join(''),new cljs.core.Keyword(null,"display","display",242065432),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-dropdown","oc.web.components.ui.section-editor/show-edit-user-dropdown",1636255352).cljs$core$IFn$_invoke$arity$1(s)))?"block":"none")], null)], null),(function (){var G__46341 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"items","items",1031954938),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"viewer","viewer",-783949853),new cljs.core.Keyword(null,"label","label",1718410804),"View"], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"author","author",2111686192),new cljs.core.Keyword(null,"label","label",1718410804),"Edit"], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"remove","remove",-131428414),new cljs.core.Keyword(null,"label","label",1718410804),"Remove"], null)], null),new cljs.core.Keyword(null,"value","value",305978217),user_type,new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (item){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-dropdown","oc.web.components.ui.section-editor/show-edit-user-dropdown",1636255352).cljs$core$IFn$_invoke$arity$1(s),null);

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item),new cljs.core.Keyword(null,"remove","remove",-131428414))){
return oc.web.actions.section.private_section_user_remove(team_user);
} else {
return oc.web.actions.section.private_section_user_add(team_user,new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item));
}
})], null);
return (oc.web.components.ui.dropdown_list.dropdown_list.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.dropdown_list.dropdown_list.cljs$core$IFn$_invoke$arity$1(G__46341) : oc.web.components.ui.dropdown_list.dropdown_list.call(null,G__46341));
})()], null);
})(),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-add-private-users-list.group","div.section-editor-add-private-users-list.group",594101875),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.hide_class,new cljs.core.Keyword(null,"on-scroll","on-scroll",1590848677),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-dropdown","oc.web.components.ui.section-editor/show-edit-user-dropdown",1636255352).cljs$core$IFn$_invoke$arity$1(s),null);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-top","oc.web.components.ui.section-editor/show-edit-user-top",-2005930239).cljs$core$IFn$_invoke$arity$1(s),null);
}),new cljs.core.Keyword(null,"ref","ref",1289896967),"edit-users-scroll"], null),(function (){var author_ids = cljs.core.set(new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_editing));
var viewer_ids = cljs.core.set(new cljs.core.Keyword(null,"viewers","viewers",-415894011).cljs$core$IFn$_invoke$arity$1(section_editing));
var authors = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(author_ids,new cljs.core.Keyword(null,"user-id","user-id",-206822291)),all_users_data);
var viewers = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(viewer_ids,new cljs.core.Keyword(null,"user-id","user-id",-206822291)),all_users_data);
var complete_authors = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__46321_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__46321_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"author","author",2111686192),new cljs.core.Keyword(null,"display-name","display-name",694513143),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(p1__46321_SHARP_)], null)], 0));
}),authors);
var complete_viewers = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__46322_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__46322_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"viewer","viewer",-783949853),new cljs.core.Keyword(null,"display-name","display-name",694513143),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(p1__46322_SHARP_)], null)], 0));
}),viewers);
var all_users = cljs.core.concat.cljs$core$IFn$_invoke$arity$2(complete_authors,complete_viewers);
var self_user = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46323_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46323_SHARP_),current_user_id);
}),all_users));
var rest_users = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46324_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46324_SHARP_),current_user_id);
}),all_users);
var sorted_users = cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [self_user], null),cljs.core.sort.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.section_editor.compare_users,rest_users));
var iter__4529__auto__ = (function oc$web$components$ui$section_editor$iter__46342(s__46343){
return (new cljs.core.LazySeq(null,(function (){
var s__46343__$1 = s__46343;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46343__$1);
if(temp__5735__auto__){
var s__46343__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46343__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46343__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46345 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46344 = (0);
while(true){
if((i__46344 < size__4528__auto__)){
var user = cljs.core._nth(c__4527__auto__,i__46344);
var user_type = new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(user);
var self = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),current_user_id);
var showing_dropdown = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-dropdown","oc.web.components.ui.section-editor/show-edit-user-dropdown",1636255352).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user));
cljs.core.chunk_append(b__46345,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-add-private-user.group","div.section-editor-add-private-user.group",-1084690364),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ref","ref",1289896967),["edit-user-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user))].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__46344,user_type,self,showing_dropdown,user,c__4527__auto__,size__4528__auto__,b__46345,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change){
return (function (){
if(cljs.core.truth_((function (){var and__4115__auto__ = can_change;
if(cljs.core.truth_(and__4115__auto__)){
return (!(self));
} else {
return and__4115__auto__;
}
})())){
var user_node = rum.core.ref_node(s,["edit-user-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user))].join(''));
var top = (user_node.offsetTop - user_node.parentElement.scrollTop);
var next_user_id = ((showing_dropdown)?null:new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-top","oc.web.components.ui.section-editor/show-edit-user-top",-2005930239).cljs$core$IFn$_invoke$arity$1(s),top);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-dropdown","oc.web.components.ui.section-editor/show-edit-user-dropdown",1636255352).cljs$core$IFn$_invoke$arity$1(s),next_user_id);
} else {
return null;
}
});})(i__46344,user_type,self,showing_dropdown,user,c__4527__auto__,size__4528__auto__,b__46345,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change))
], null),(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(user) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,user)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.name","div.name",1027675228),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),current_user_id))?" (you)":null)], null),((self)?((((cljs.core.seq(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section_editing))) && ((cljs.core.count(new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_data)) > (1)))))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-type.remove-link","div.user-type.remove-link",1022670786),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__46344,user_type,self,showing_dropdown,user,c__4527__auto__,size__4528__auto__,b__46345,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change){
return (function (){
var authors__$1 = new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_data);
var self_data = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(((function (i__46344,authors__$1,user_type,self,showing_dropdown,user,c__4527__auto__,size__4528__auto__,b__46345,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change){
return (function (p1__46325_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46325_SHARP_),current_user_id);
});})(i__46344,authors__$1,user_type,self,showing_dropdown,user,c__4527__auto__,size__4528__auto__,b__46345,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change))
,authors__$1));
return oc.web.components.ui.alert_modal.show_alert(new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/error_icon.png",new cljs.core.Keyword(null,"action","action",-811238024),"remove-self-user-from-private-section",new cljs.core.Keyword(null,"message","message",-406056002),"Are you sure you want to leave this topic?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"No",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),((function (i__46344,authors__$1,self_data,user_type,self,showing_dropdown,user,c__4527__auto__,size__4528__auto__,b__46345,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change){
return (function (){
return oc.web.components.ui.alert_modal.hide_alert();
});})(i__46344,authors__$1,self_data,user_type,self,showing_dropdown,user,c__4527__auto__,size__4528__auto__,b__46345,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change))
,new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),((function (i__46344,authors__$1,self_data,user_type,self,showing_dropdown,user,c__4527__auto__,size__4528__auto__,b__46345,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change){
return (function (){
oc.web.actions.section.private_section_kick_out_self(self_data);

return oc.web.components.ui.alert_modal.hide_alert();
});})(i__46344,authors__$1,self_data,user_type,self,showing_dropdown,user,c__4527__auto__,size__4528__auto__,b__46345,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change))
], null));
});})(i__46344,user_type,self,showing_dropdown,user,c__4527__auto__,size__4528__auto__,b__46345,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change))
], null),"Leave topic"], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-type.no-dropdown","div.user-type.no-dropdown",1348401635),"Edit"], null)):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-type","div.user-type",-1602613736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"no-dropdown","no-dropdown",1298423035),cljs.core.not(can_change),new cljs.core.Keyword(null,"active","active",1895962068),showing_dropdown], null))], null),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_type,new cljs.core.Keyword(null,"author","author",2111686192)))?"Edit":"View")], null))], null));

var G__46362 = (i__46344 + (1));
i__46344 = G__46362;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46345),oc$web$components$ui$section_editor$iter__46342(cljs.core.chunk_rest(s__46343__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46345),null);
}
} else {
var user = cljs.core.first(s__46343__$2);
var user_type = new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(user);
var self = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),current_user_id);
var showing_dropdown = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-dropdown","oc.web.components.ui.section-editor/show-edit-user-dropdown",1636255352).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user));
return cljs.core.cons(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-add-private-user.group","div.section-editor-add-private-user.group",-1084690364),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ref","ref",1289896967),["edit-user-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user))].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (user_type,self,showing_dropdown,user,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change){
return (function (){
if(cljs.core.truth_((function (){var and__4115__auto__ = can_change;
if(cljs.core.truth_(and__4115__auto__)){
return (!(self));
} else {
return and__4115__auto__;
}
})())){
var user_node = rum.core.ref_node(s,["edit-user-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user))].join(''));
var top = (user_node.offsetTop - user_node.parentElement.scrollTop);
var next_user_id = ((showing_dropdown)?null:new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-top","oc.web.components.ui.section-editor/show-edit-user-top",-2005930239).cljs$core$IFn$_invoke$arity$1(s),top);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-dropdown","oc.web.components.ui.section-editor/show-edit-user-dropdown",1636255352).cljs$core$IFn$_invoke$arity$1(s),next_user_id);
} else {
return null;
}
});})(user_type,self,showing_dropdown,user,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change))
], null),(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(user) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,user)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.name","div.name",1027675228),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),current_user_id))?" (you)":null)], null),((self)?((((cljs.core.seq(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section_editing))) && ((cljs.core.count(new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_data)) > (1)))))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-type.remove-link","div.user-type.remove-link",1022670786),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (user_type,self,showing_dropdown,user,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change){
return (function (){
var authors__$1 = new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_data);
var self_data = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46325_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46325_SHARP_),current_user_id);
}),authors__$1));
return oc.web.components.ui.alert_modal.show_alert(new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/error_icon.png",new cljs.core.Keyword(null,"action","action",-811238024),"remove-self-user-from-private-section",new cljs.core.Keyword(null,"message","message",-406056002),"Are you sure you want to leave this topic?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"No",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.actions.section.private_section_kick_out_self(self_data);

return oc.web.components.ui.alert_modal.hide_alert();
})], null));
});})(user_type,self,showing_dropdown,user,s__46343__$2,temp__5735__auto__,author_ids,viewer_ids,authors,viewers,complete_authors,complete_viewers,all_users,self_user,rest_users,sorted_users,org_data,no_drafts_boards,section_editing,section_data,team_data,slack_teams,show_slack_channels_QMARK_,channel_name,roster,all_users_data,slack_orgs,cur_user_data,slack_users,current_user_id,can_change,last_section_standing,disallow_public_board_QMARK_,wrapped_on_change))
], null),"Leave topic"], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-type.no-dropdown","div.user-type.no-dropdown",1348401635),"Edit"], null)):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-type","div.user-type",-1602613736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"no-dropdown","no-dropdown",1298423035),cljs.core.not(can_change),new cljs.core.Keyword(null,"active","active",1895962068),showing_dropdown], null))], null),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_type,new cljs.core.Keyword(null,"author","author",2111686192)))?"Edit":"View")], null))], null),oc$web$components$ui$section_editor$iter__46342(cljs.core.rest(s__46343__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(sorted_users);
})()], null)], null):null)),sablono.interpreter.interpret(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(section_editing),"private"))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-add-label","div.section-editor-add-label",1099900058),"Personal note"], null):null)),sablono.interpreter.interpret(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(section_editing),"private"))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-editor-add-personal-note.oc-input","div.section-editor-add-personal-note.oc-input",-1257229476),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.hide_class,new cljs.core.Keyword(null,"content-editable","content-editable",636764967),true,new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),"Add a personal note to your invitation...",new cljs.core.Keyword(null,"ref","ref",1289896967),"personal-note",new cljs.core.Keyword(null,"on-paste","on-paste",-50859856),(function (p1__46326_SHARP_){
return OnPaste_StripFormatting(rum.core.ref_node(s,"personal-note"),p1__46326_SHARP_);
}),new cljs.core.Keyword(null,"on-key-press","on-key-press",-399563677),(function (e){
if((((cljs.core.count(e.target.innerText) >= (500))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.key,"Enter")))){
return oc.web.lib.utils.event_stop(e);
} else {
return null;
}
}),new cljs.core.Keyword(null,"dangerouslySetInnerHTML","dangerouslySetInnerHTML",-554971138),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"__html","__html",674048345),""], null)], null)], null):null)),(function (){var attrs46333 = (cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","editing-existing-section","oc.web.components.ui.section-editor/editing-existing-section",2018495799).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(section_data),"delete"], 0));
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.delete-bt","button.mlb-reset.delete-bt",-168688558),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
if(last_section_standing){
return null;
} else {
return oc.web.components.ui.alert_modal.show_alert(new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"delete-section",new cljs.core.Keyword(null,"message","message",-406056002),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Are you sure?"], null),(((new cljs.core.Keyword(null,"entry-count","entry-count",-1800299381).cljs$core$IFn$_invoke$arity$1(section_data) > (0)))?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991)," This will delete the topic and ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"strong","strong",269529000),"all"], null)," its updates, too."], null):null)], null),new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"No",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes, I'm sure",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.actions.section.section_delete.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section_data),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"title","title",636505583),"Topic deleted",new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"section-deleted","section-deleted",877485224)], null))], 0));

oc.web.components.ui.alert_modal.hide_alert();

return oc.web.actions.nav_sidebar.hide_section_editor();
})], null));
}
}),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),"tooltip",new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),((last_section_standing)?"You cannot delete the last remaining topic.":"Delete this topic and all its updates."),new cljs.core.Keyword(null,"class","class",-2030961996),((last_section_standing)?"disabled":null)], null),"Delete topic"], null):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46333))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["section-editor-add-footer"], null)], null),attrs46333], 0))):({"className": "section-editor-add-footer"})),((cljs.core.map_QMARK_(attrs46333))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46333)], null)));
})())));
}),new cljs.core.PersistentVector(null, 24, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.mixins.ui.refresh_tooltips_mixin,rum.core.reactive,rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.section-editor","query","oc.web.components.ui.section-editor/query",1438702739)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.section-editor","show-access-list","oc.web.components.ui.section-editor/show-access-list",840836864)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.section-editor","show-search-results","oc.web.components.ui.section-editor/show-search-results",-1563787540)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-dropdown","oc.web.components.ui.section-editor/show-edit-user-dropdown",1636255352)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.section-editor","show-edit-user-top","oc.web.components.ui.section-editor/show-edit-user-top",-2005930239)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.section-editor","initial-section-name","oc.web.components.ui.section-editor/initial-section-name",507012030)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.section-editor","editing-existing-section","oc.web.components.ui.section-editor/editing-existing-section",2018495799)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.section-editor","slack-enabled","oc.web.components.ui.section-editor/slack-enabled",1764073527)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.section-editor","section-name","oc.web.components.ui.section-editor/section-name",1698485784)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.section-editor","pre-flight-check","oc.web.components.ui.section-editor/pre-flight-check",-1531999935)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.section-editor","pre-flight-ok","oc.web.components.ui.section-editor/pre-flight-ok",-1735583943)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.section-editor","section-name-check-timeout","oc.web.components.ui.section-editor/section-name-check-timeout",1302312762)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.section-editor","saving","oc.web.components.ui.section-editor/saving",1891466044)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"board-data","board-data",1372958925)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"team-data","team-data",-732020079)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"team-channels","team-channels",321697292)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"team-roster","team-roster",-1945092859)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),oc.web.mixins.ui.autoresize_textarea(new cljs.core.Keyword(null,"section-description","section-description",-1413838302)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.actions.team.teams_get();

var initial_section_data_46378 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var new_section_46380 = (initial_section_data_46378 == null);
var fixed_section_data_46381 = ((new_section_46380)?oc.web.lib.utils.default_section:oc.web.components.ui.section_editor.section_for_editing(initial_section_data_46378));
if(typeof new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(fixed_section_data_46381) === 'string'){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","section-name","oc.web.components.ui.section-editor/section-name",1698485784).cljs$core$IFn$_invoke$arity$1(s),clojure.string.trim($(["<div>",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(fixed_section_data_46381)),"</div>"].join('')).text()));
} else {
}

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","editing-existing-section","oc.web.components.ui.section-editor/editing-existing-section",2018495799).cljs$core$IFn$_invoke$arity$1(s),(!(new_section_46380)));

if(cljs.core.seq(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(fixed_section_data_46381))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","initial-section-name","oc.web.components.ui.section-editor/initial-section-name",507012030).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(fixed_section_data_46381));
} else {
}

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], null),fixed_section_data_46381], null));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","slack-enabled","oc.web.components.ui.section-editor/slack-enabled",1764073527).cljs$core$IFn$_invoke$arity$1(s),cljs.core.seq(new cljs.core.Keyword(null,"channel-id","channel-id",138191095).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-mirror","slack-mirror",-506844872).cljs$core$IFn$_invoke$arity$1(fixed_section_data_46381))));

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
var section_editing_46386 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)));
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","pre-flight-check","oc.web.components.ui.section-editor/pre-flight-check",-1531999935).cljs$core$IFn$_invoke$arity$1(s)))){
if(cljs.core.truth_(new cljs.core.Keyword(null,"pre-flight-loading","pre-flight-loading",-1550718393).cljs$core$IFn$_invoke$arity$1(section_editing_46386))){
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","pre-flight-check","oc.web.components.ui.section-editor/pre-flight-check",-1531999935).cljs$core$IFn$_invoke$arity$1(s),false);

if(((cljs.core.not(new cljs.core.Keyword(null,"section-name-error","section-name-error",-581249210).cljs$core$IFn$_invoke$arity$1(section_editing_46386))) && (cljs.core.not(new cljs.core.Keyword(null,"section-error","section-error",738290699).cljs$core$IFn$_invoke$arity$1(section_editing_46386))))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","pre-flight-ok","oc.web.components.ui.section-editor/pre-flight-ok",-1735583943).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
}
}
} else {
}

if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.section-editor","saving","oc.web.components.ui.section-editor/saving",1891466044).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"loading","loading",-737050189).cljs$core$IFn$_invoke$arity$1(section_editing_46386));
} else {
return and__4115__auto__;
}
})())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.section-editor","saving","oc.web.components.ui.section-editor/saving",1891466044).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
}

return s;
})], null)], null),"section-editor");

//# sourceMappingURL=oc.web.components.ui.section_editor.js.map
