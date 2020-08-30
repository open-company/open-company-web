goog.provide('oc.web.router');
oc.web.router.get_token = (function oc$web$router$get_token(){
if(((cljs.core.not(window.location.pathname)) || (cljs.core.not(window.location.search)))){
oc.web.lib.sentry.capture_message_BANG_(["Window.location problem:"," windown.location.pathname:",cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.pathname)," window.location.search:",cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.search)," return:",[cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.pathname),cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.search)].join('')].join(''));
} else {
}

return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.pathname),cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.search)].join('');
});
/**
 * Custom transformer is needed to replace query parameters, rather
 *   than adding to them.
 *   See: https://gist.github.com/pleasetrythisathome/d1d9b1d74705b6771c20
 */
oc.web.router.build_transformer = (function oc$web$router$build_transformer(){
var transformer = (new goog.history.Html5History.TokenTransformer());
(transformer.retrieveToken = (function (path_prefix,location){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(location.pathname),cljs.core.str.cljs$core$IFn$_invoke$arity$1(location.search)].join('');
}));

(transformer.createUrl = (function (token,path_prefix,location){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_prefix),cljs.core.str.cljs$core$IFn$_invoke$arity$1(token)].join('');
}));

return transformer;
});
oc.web.router.make_history = (function oc$web$router$make_history(){
var G__41140 = (new goog.history.Html5History(window,oc.web.router.build_transformer()));
G__41140.setPathPrefix([cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.protocol),"//",cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.host)].join(''));

G__41140.setUseFragment(false);

return G__41140;
});
oc.web.router.history = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
oc.web.router.nav_BANG_ = (function oc$web$router$nav_BANG_(token){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.router",null,52,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["nav!",token], null);
}),null)),null,1548568627);

taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.router",null,53,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["history:",cljs.core.deref(oc.web.router.history)], null);
}),null)),null,1244407879);

return cljs.core.deref(oc.web.router.history).setToken(token);
});
oc.web.router.rewrite_org_uuid_as_slug = (function oc$web$router$rewrite_org_uuid_as_slug(org_uuid,org_slug){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.router",null,58,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Navigate from org",org_uuid,"to slug:",org_slug], null);
}),null)),null,133148481);

return oc.web.router.nav_BANG_(clojure.string.replace(oc.web.router.get_token(),cljs.core.re_pattern(org_uuid),org_slug));
});
oc.web.router.rewrite_board_uuid_as_slug = (function oc$web$router$rewrite_board_uuid_as_slug(board_uuid,board_slug){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.router",null,63,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Rewrite URL from board",board_uuid,"to slug:",board_slug], null);
}),null)),null,139053958);

var new_path = clojure.string.replace(oc.web.router.get_token(),cljs.core.re_pattern(board_uuid),board_slug);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("route","rewrite","route/rewrite",711205411),new cljs.core.Keyword(null,"board","board",-1907017633),board_slug], null));

return window.history.replaceState(({}),window.title,new_path);
});
oc.web.router.redirect_BANG_ = (function oc$web$router$redirect_BANG_(loc){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.router",null,70,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["redirect!",loc], null);
}),null)),null,1099719572);

return (window.location = loc);
});
oc.web.router.redirect_404_BANG_ = (function oc$web$router$redirect_404_BANG_(){
var win_location = window.location;
var pathname = win_location.pathname;
var search = win_location.search;
var hash_string = win_location.hash;
var encoded_url = encodeURIComponent([cljs.core.str.cljs$core$IFn$_invoke$arity$1(pathname),cljs.core.str.cljs$core$IFn$_invoke$arity$1(search),cljs.core.str.cljs$core$IFn$_invoke$arity$1(hash_string)].join(''));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.router",null,79,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["redirect-404!",encoded_url], null);
}),null)),null,1220062495);

return window.location.replace(["/404?path=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(encoded_url)].join(''));
});
oc.web.router.redirect_500_BANG_ = (function oc$web$router$redirect_500_BANG_(){
var win_location = window.location;
var pathname = win_location.pathname;
var search = win_location.search;
var hash_string = win_location.hash;
var encoded_url = encodeURIComponent([cljs.core.str.cljs$core$IFn$_invoke$arity$1(pathname),cljs.core.str.cljs$core$IFn$_invoke$arity$1(search),cljs.core.str.cljs$core$IFn$_invoke$arity$1(hash_string)].join(''));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.router",null,89,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["redirect-500!",encoded_url], null);
}),null)),null,1805632791);

return window.location.replace(["/500?path=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(encoded_url)].join(''));
});
oc.web.router.history_back_BANG_ = (function oc$web$router$history_back_BANG_(){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.router",null,94,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["history-back!"], null);
}),null)),null,1698549243);

return window.history.go((-1));
});
oc.web.router.setup_navigation_BANG_ = (function oc$web$router$setup_navigation_BANG_(cb_fn){
var h = (function (){var G__41158 = oc.web.router.make_history();
goog.events.listen(G__41158,goog.history.EventType.NAVIGATE,cb_fn);

G__41158.setEnabled(true);

return G__41158;
})();
return cljs.core.reset_BANG_(oc.web.router.history,h);
});
/**
 * Cookie to save the last accessed org
 */
oc.web.router.last_org_cookie = (function oc$web$router$last_org_cookie(){
return ["last-org-",cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(oc.web.lib.jwt.jwt())?oc.web.lib.jwt.user_id():null))].join('');
});
/**
 * Cookie to save the last accessed board
 */
oc.web.router.last_board_cookie = (function oc$web$router$last_board_cookie(org_slug){
return ["last-board-",(cljs.core.truth_(oc.web.lib.jwt.jwt())?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.user_id()),"-"].join(''):null),cljs.core.name(org_slug)].join('');
});
/**
 * Cookie to save the last view used: grid or stream
 */
oc.web.router.last_board_view_cookie = (function oc$web$router$last_board_view_cookie(org_slug){
return ["last-board-view-",(cljs.core.truth_(oc.web.lib.jwt.jwt())?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"user-id","user-id",-206822291))),"-"].join(''):null),cljs.core.name(org_slug)].join('');
});
/**
 * Cookie to save the last board slug used in a post creation
 */
oc.web.router.last_used_board_slug_cookie = (function oc$web$router$last_used_board_slug_cookie(org_slug){
return ["last-used-board-slug-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.user_id()),"-",cljs.core.name(org_slug)].join('');
});
/**
 * Cookie to save the last selection in home: Following or All updates
 */
oc.web.router.last_home_cookie = (function oc$web$router$last_home_cookie(org_slug){
return ["last-used-home-slug-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.user_id()),"-",cljs.core.name(org_slug)].join('');
});
/**
 * Cookie to save the last sort selected
 */
oc.web.router.last_sort_cookie = (function oc$web$router$last_sort_cookie(org_slug){
return ["last-sort-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.user_id()),"-",cljs.core.name(org_slug)].join('');
});
/**
 * Cookie to save the last FOC layout used
 */
oc.web.router.last_foc_layout_cookie = (function oc$web$router$last_foc_layout_cookie(org_slug){
return ["last-foc-layout-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.user_id()),"-",cljs.core.name(org_slug)].join('');
});
/**
 * Cookie to remember if the boards and journals tooltips where shown.
 */
oc.web.router.nux_cookie = (function oc$web$router$nux_cookie(user_id){
return ["nux-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(user_id)].join('');
});
/**
 * Cookie used to land the user to a special URL only the first time.
 */
oc.web.router.first_ever_landing_cookie = (function oc$web$router$first_ever_landing_cookie(user_id){
return ["first-ever-ap-land-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(user_id)].join('');
});
/**
 * Cookie to check if the add first post tooltip shuold be visible.
 */
oc.web.router.show_add_post_tooltip_cookie = (function oc$web$router$show_add_post_tooltip_cookie(){
return ["add-post-tooltip-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.user_id())].join('');
});
/**
 * Cookie to check if the invite people tooltip shuold be visible.
 */
oc.web.router.show_invite_people_tooltip_cookie = (function oc$web$router$show_invite_people_tooltip_cookie(){
return ["invite-people-tooltip-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.user_id())].join('');
});
/**
 * Cookie to check if the invite people tooltip shuold be visible.
 */
oc.web.router.show_invite_box_cookie = (function oc$web$router$show_invite_box_cookie(user_id){
return ["invite-box-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(user_id)].join('');
});
/**
 * Cookie used to remember if the sections list was collapsed or not.
 */
oc.web.router.collapse_boards_list_cookie = (function oc$web$router$collapse_boards_list_cookie(){
return ["collapse-sections-list-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.user_id())].join('');
});
/**
 * Cookie used to remember if the users list was collapsed or not.
 */
oc.web.router.collapse_users_list_cookie = (function oc$web$router$collapse_users_list_cookie(){
return ["collapse-users-list-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.user_id())].join('');
});
oc.web.router.login_redirect_cookie = "login-redirect";
oc.web.router.expo_push_token_cookie = "expo-push-token";

//# sourceMappingURL=oc.web.router.js.map
