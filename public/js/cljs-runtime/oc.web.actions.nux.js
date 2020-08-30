goog.provide('oc.web.actions.nux');
/**
 * Read the cookie from the document only if the nux-cookie-value atom is nil.
 *   In all the other cases return the read value in the atom.
 */
oc.web.actions.nux.get_nux_cookie = (function oc$web$actions$nux$get_nux_cookie(){
var cookie_name = oc.web.router.nux_cookie(oc.web.lib.jwt.user_id());
var cookie_value = oc.web.lib.cookies.get_cookie(cookie_name);
if(cljs.core.truth_(cookie_value)){
return oc.web.lib.json.json__GT_cljs(cookie_value);
} else {
return null;
}
});
/**
 * Create a map for the new user cookie and save it. Also update the value of
 *   the nux-cookie-value atom.
 */
oc.web.actions.nux.set_nux_cookie = (function oc$web$actions$nux$set_nux_cookie(user_type,value_map){
var old_nux_cookie = oc.web.actions.nux.get_nux_cookie();
var value_map__$1 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"user-type","user-type",738868936),user_type], null),old_nux_cookie,value_map], 0));
var json_map = oc.web.lib.json.cljs__GT_json(value_map__$1);
var json_string = JSON.stringify(json_map);
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.nux_cookie(oc.web.lib.jwt.user_id()),json_string,((((60) * (60)) * (24)) * (7)));
});
oc.web.actions.nux.new_user_registered = (function oc$web$actions$nux$new_user_registered(user_type){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.first_ever_landing_cookie(oc.web.lib.jwt.user_id()),true,((((60) * (60)) * (24)) * (7)));

oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$2(oc.web.router.show_invite_box_cookie(oc.web.lib.jwt.user_id()),true);

return oc.web.actions.nux.set_nux_cookie(user_type,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"show-add-post-tooltip","show-add-post-tooltip",1769173942),true,new cljs.core.Keyword(null,"show-post-added-tooltip","show-post-added-tooltip",-1747287376),false,new cljs.core.Keyword(null,"show-edit-tooltip","show-edit-tooltip",2074919569),true], null));
});
/**
 * NUX completed for the current user, remove the cookie and update the nux-cookie-value.
 */
oc.web.actions.nux.nux_end = (function oc$web$actions$nux$nux_end(){
return oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(oc.web.router.nux_cookie(oc.web.lib.jwt.user_id()));
});
oc.web.actions.nux.default_tooltip_done = "done";
oc.web.actions.nux.parse_nux_cookie_value = (function oc$web$actions$nux$parse_nux_cookie_value(v){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(v,oc.web.actions.nux.default_tooltip_done)){
return false;
} else {
return v;
}
});
oc.web.actions.nux.mark_nux_step_done = (function oc$web$actions$nux$mark_nux_step_done(nux_step_key){
var temp__5735__auto__ = oc.web.actions.nux.get_nux_cookie();
if(cljs.core.truth_(temp__5735__auto__)){
var nux_cookie = temp__5735__auto__;
return oc.web.actions.nux.set_nux_cookie(new cljs.core.Keyword(null,"user-type","user-type",738868936).cljs$core$IFn$_invoke$arity$1(nux_cookie),cljs.core.PersistentArrayMap.createAsIfByAssoc([nux_step_key,oc.web.actions.nux.default_tooltip_done]));
} else {
return null;
}
});
oc.web.actions.nux.check_nux = (function oc$web$actions$nux$check_nux(){
var temp__33774__auto__ = oc.web.actions.nux.get_nux_cookie();
if(cljs.core.truth_(temp__33774__auto__)){
var nv = temp__33774__auto__;
var temp__33774__auto____$1 = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__33774__auto____$1)){
var org_data = temp__33774__auto____$1;
var team_data = oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$0();
var is_admin_QMARK_ = oc.web.lib.jwt.is_admin_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data));
var add_post_tooltip = new cljs.core.Keyword(null,"show-add-post-tooltip","show-add-post-tooltip",1769173942).cljs$core$IFn$_invoke$arity$1(nv);
var post_added_tooltip = new cljs.core.Keyword(null,"show-post-added-tooltip","show-post-added-tooltip",-1747287376).cljs$core$IFn$_invoke$arity$1(nv);
var fixed_post_added_tooltip = oc.web.actions.nux.parse_nux_cookie_value(post_added_tooltip);
var edit_tooltip = new cljs.core.Keyword(null,"show-edit-tooltip","show-edit-tooltip",2074919569).cljs$core$IFn$_invoke$arity$1(nv);
var user_type = new cljs.core.Keyword(null,"user-type","user-type",738868936).cljs$core$IFn$_invoke$arity$1(nv);
var team_has_bot_QMARK_ = oc.web.lib.jwt.team_has_bot_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data));
var fixed_add_post_tooltip = ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(add_post_tooltip,oc.web.actions.nux.default_tooltip_done)) && ((!(post_added_tooltip === true))));
if((((!(fixed_add_post_tooltip))) && (post_added_tooltip === true))){
oc.web.actions.nux.mark_nux_step_done(new cljs.core.Keyword(null,"show-add-post-tooltip","show-add-post-tooltip",1769173942));
} else {
}

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"nux","nux",-865367307)], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"show-add-post-tooltip","show-add-post-tooltip",1769173942),((fixed_add_post_tooltip)?(cljs.core.truth_(new cljs.core.Keyword(null,"author?","author?",-1083349935).cljs$core$IFn$_invoke$arity$1(org_data))?true:new cljs.core.Keyword(null,"is-second-user","is-second-user",-1078224089)):false),new cljs.core.Keyword(null,"show-post-added-tooltip","show-post-added-tooltip",-1747287376),fixed_post_added_tooltip,new cljs.core.Keyword(null,"user-type","user-type",738868936),user_type], null)], null));

if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"show-add-post-tooltip","show-add-post-tooltip",1769173942).cljs$core$IFn$_invoke$arity$1(nv),oc.web.actions.nux.default_tooltip_done)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"show-post-added-tooltip","show-post-added-tooltip",-1747287376).cljs$core$IFn$_invoke$arity$1(nv),oc.web.actions.nux.default_tooltip_done)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"show-edit-tooltip","show-edit-tooltip",2074919569).cljs$core$IFn$_invoke$arity$1(nv),oc.web.actions.nux.default_tooltip_done)))){
return oc.web.actions.nux.nux_end();
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
});
oc.web.actions.nux.dismiss_add_post_tooltip = (function oc$web$actions$nux$dismiss_add_post_tooltip(){
oc.web.actions.nux.mark_nux_step_done(new cljs.core.Keyword(null,"show-add-post-tooltip","show-add-post-tooltip",1769173942));

return oc.web.actions.nux.check_nux();
});
oc.web.actions.nux.show_post_added_tooltip = (function oc$web$actions$nux$show_post_added_tooltip(post_uuid){
var temp__5735__auto___41597 = oc.web.actions.nux.get_nux_cookie();
if(cljs.core.truth_(temp__5735__auto___41597)){
var nux_cookie_41598 = temp__5735__auto___41597;
oc.web.actions.nux.set_nux_cookie(new cljs.core.Keyword(null,"user-type","user-type",738868936).cljs$core$IFn$_invoke$arity$1(nux_cookie_41598),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"show-add-post-tooltip","show-add-post-tooltip",1769173942),oc.web.actions.nux.default_tooltip_done,new cljs.core.Keyword(null,"show-post-added-tooltip","show-post-added-tooltip",-1747287376),(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"show-post-added-tooltip","show-post-added-tooltip",-1747287376).cljs$core$IFn$_invoke$arity$1(nux_cookie_41598);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return post_uuid;
}
})()], null));
} else {
}

return oc.web.actions.nux.check_nux();
});
oc.web.actions.nux.dismiss_post_added_tooltip = (function oc$web$actions$nux$dismiss_post_added_tooltip(){
oc.web.actions.nux.mark_nux_step_done(new cljs.core.Keyword(null,"show-post-added-tooltip","show-post-added-tooltip",-1747287376));

return oc.web.actions.nux.check_nux();
});
oc.web.actions.nux.dismiss_edit_tooltip = (function oc$web$actions$nux$dismiss_edit_tooltip(){
oc.web.actions.nux.mark_nux_step_done(new cljs.core.Keyword(null,"show-edit-tooltip","show-edit-tooltip",2074919569));

return oc.web.actions.nux.check_nux();
});

//# sourceMappingURL=oc.web.actions.nux.js.map
