goog.provide('oc.web.dispatcher');
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.dispatcher !== 'undefined') && (typeof oc.web.dispatcher.app_state !== 'undefined')){
} else {
oc.web.dispatcher.app_state = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"loading","loading",-737050189),false,new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411),false], null));
goog.exportSymbol('oc.web.dispatcher.app_state', oc.web.dispatcher.app_state);
}
oc.web.dispatcher.recent_activity_sort = new cljs.core.Keyword(null,"recent-activity","recent-activity",1283224310);
oc.web.dispatcher.recently_posted_sort = new cljs.core.Keyword(null,"recently-posted","recently-posted",376126712);
oc.web.dispatcher.default_foc_layout = new cljs.core.Keyword(null,"expanded","expanded",-3020742);
oc.web.dispatcher.other_foc_layout = new cljs.core.Keyword(null,"collapsed","collapsed",-628494523);
oc.web.dispatcher.router_key = new cljs.core.Keyword(null,"router-path","router-path",-1742780249);
oc.web.dispatcher.checkout_result_key = new cljs.core.Keyword(null,"checkout-success-result","checkout-success-result",-1897249798);
oc.web.dispatcher.checkout_update_plan_key = new cljs.core.Keyword(null,"checkout-update-plan","checkout-update-plan",-659906914);
oc.web.dispatcher.expo_key = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"expo","expo",-610384546)], null);
oc.web.dispatcher.expo_deep_link_origin_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.expo_key,new cljs.core.Keyword(null,"deep-link-origin","deep-link-origin",-1098802749)));
oc.web.dispatcher.expo_app_version_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.expo_key,new cljs.core.Keyword(null,"app-version","app-version",361554836)));
oc.web.dispatcher.expo_push_token_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.expo_key,new cljs.core.Keyword(null,"push-token","push-token",-1283347571)));
oc.web.dispatcher.show_invite_box_key = new cljs.core.Keyword(null,"show-invite-box","show-invite-box",1572056533);
oc.web.dispatcher.api_entry_point_key = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"api-entry-point","api-entry-point",-920250357)], null);
oc.web.dispatcher.auth_settings_key = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219)], null);
oc.web.dispatcher.notifications_key = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"notifications-data","notifications-data",260044272)], null);
oc.web.dispatcher.show_login_overlay_key = new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411);
oc.web.dispatcher.orgs_key = new cljs.core.Keyword(null,"orgs","orgs",155776628);
oc.web.dispatcher.org_key = (function oc$web$dispatcher$org_key(org_slug){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(org_slug)], null);
});
oc.web.dispatcher.org_data_key = (function oc$web$dispatcher$org_data_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"org-data","org-data",96720321)));
});
oc.web.dispatcher.boards_key = (function oc$web$dispatcher$boards_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"boards","boards",1912049694)));
});
oc.web.dispatcher.payments_key = (function oc$web$dispatcher$payments_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"payments","payments",-1324138047)));
});
oc.web.dispatcher.posts_data_key = (function oc$web$dispatcher$posts_data_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"posts","posts",760043164)));
});
oc.web.dispatcher.board_key = (function oc$web$dispatcher$board_key(var_args){
var G__41440 = arguments.length;
switch (G__41440) {
case 3:
return oc.web.dispatcher.board_key.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 2:
return oc.web.dispatcher.board_key.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.board_key.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,board_slug,sort_type){
if(cljs.core.truth_(sort_type)){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.boards_key(org_slug),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(board_slug),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(sort_type)], null)));
} else {
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.boards_key(org_slug),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(board_slug)], null)));
}
}));

(oc.web.dispatcher.board_key.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,board_slug){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.boards_key(org_slug),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(board_slug),oc.web.dispatcher.recently_posted_sort], null)));
}));

(oc.web.dispatcher.board_key.cljs$lang$maxFixedArity = 3);

oc.web.dispatcher.board_data_key = (function oc$web$dispatcher$board_data_key(var_args){
var G__41444 = arguments.length;
switch (G__41444) {
case 2:
return oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,board_slug){
return oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,board_slug,oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,board_slug,sort_type){
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.board_key.cljs$core$IFn$_invoke$arity$3(org_slug,board_slug,sort_type),new cljs.core.Keyword(null,"board-data","board-data",1372958925));
}));

(oc.web.dispatcher.board_data_key.cljs$lang$maxFixedArity = 3);

oc.web.dispatcher.contributions_list_key = (function oc$web$dispatcher$contributions_list_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"contribs","contribs",-271924849)));
});
oc.web.dispatcher.contributions_key = (function oc$web$dispatcher$contributions_key(var_args){
var G__41446 = arguments.length;
switch (G__41446) {
case 2:
return oc.web.dispatcher.contributions_key.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.contributions_key.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.contributions_key.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,author_uuid){
return oc.web.dispatcher.contributions_key.cljs$core$IFn$_invoke$arity$3(org_slug,author_uuid,oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.dispatcher.contributions_key.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,author_uuid,sort_type){
if(cljs.core.truth_(sort_type)){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.contributions_list_key(org_slug),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(author_uuid),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(sort_type)], null)));
} else {
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.contributions_list_key(org_slug),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(author_uuid)));
}
}));

(oc.web.dispatcher.contributions_key.cljs$lang$maxFixedArity = 3);

oc.web.dispatcher.contributions_data_key = (function oc$web$dispatcher$contributions_data_key(var_args){
var G__41451 = arguments.length;
switch (G__41451) {
case 3:
return oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 2:
return oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,slug_or_uuid,sort_type){
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.contributions_key.cljs$core$IFn$_invoke$arity$3(org_slug,slug_or_uuid,sort_type),new cljs.core.Keyword(null,"contrib-data","contrib-data",1870572220));
}));

(oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,slug_or_uuid){
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.contributions_key.cljs$core$IFn$_invoke$arity$2(org_slug,slug_or_uuid),new cljs.core.Keyword(null,"contrib-data","contrib-data",1870572220));
}));

(oc.web.dispatcher.contributions_data_key.cljs$lang$maxFixedArity = 3);

oc.web.dispatcher.containers_key = (function oc$web$dispatcher$containers_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"container-data","container-data",-53681130)));
});
oc.web.dispatcher.container_key = (function oc$web$dispatcher$container_key(var_args){
var G__41456 = arguments.length;
switch (G__41456) {
case 2:
return oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,items_filter){
return oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,items_filter,oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,items_filter,sort_type){
if(cljs.core.truth_(sort_type)){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$variadic(oc.web.dispatcher.containers_key(org_slug),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(items_filter),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(sort_type)], 0)));
} else {
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.containers_key(org_slug),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(items_filter)));

}
}));

(oc.web.dispatcher.container_key.cljs$lang$maxFixedArity = 3);

oc.web.dispatcher.badges_key = (function oc$web$dispatcher$badges_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"badges","badges",-288609067)));
});
oc.web.dispatcher.replies_badge_key = (function oc$web$dispatcher$replies_badge_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.badges_key(org_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974)));
});
oc.web.dispatcher.following_badge_key = (function oc$web$dispatcher$following_badge_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.badges_key(org_slug),new cljs.core.Keyword(null,"following","following",-2049193617)));
});
oc.web.dispatcher.secure_activity_key = (function oc$web$dispatcher$secure_activity_key(org_slug,secure_id){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"secure-activities","secure-activities",1125018638),secure_id], null)));
});
oc.web.dispatcher.activity_key = (function oc$web$dispatcher$activity_key(org_slug,activity_uuid){
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(posts_key,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [activity_uuid], null)));
});
oc.web.dispatcher.activity_last_read_at_key = (function oc$web$dispatcher$activity_last_read_at_key(org_slug,activity_uuid){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.activity_key(org_slug,activity_uuid),new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930)));
});
oc.web.dispatcher.add_comment_key = (function oc$web$dispatcher$add_comment_key(org_slug){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"add-comment-data","add-comment-data",-1679568046)], null)));
});
oc.web.dispatcher.add_comment_string_key = (function oc$web$dispatcher$add_comment_string_key(var_args){
var G__41464 = arguments.length;
switch (G__41464) {
case 1:
return oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$1 = (function (activity_uuid){
return oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$3(activity_uuid,null,null);
}));

(oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$2 = (function (activity_uuid,parent_comment_uuid){
return oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$3(activity_uuid,parent_comment_uuid,null);
}));

(oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$3 = (function (activity_uuid,parent_comment_uuid,comment_uuid){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(activity_uuid),(cljs.core.truth_(parent_comment_uuid)?["-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(parent_comment_uuid)].join(''):null),(cljs.core.truth_(comment_uuid)?["-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(comment_uuid)].join(''):null)].join('');
}));

(oc.web.dispatcher.add_comment_string_key.cljs$lang$maxFixedArity = 3);

oc.web.dispatcher.add_comment_force_update_root_key = new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794);
oc.web.dispatcher.add_comment_force_update_key = (function oc$web$dispatcher$add_comment_force_update_key(add_comment_string_key){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.add_comment_force_update_root_key], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [add_comment_string_key], null)));
});
oc.web.dispatcher.add_comment_activity_key = (function oc$web$dispatcher$add_comment_activity_key(org_slug,activity_uuid){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.add_comment_key(org_slug),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [activity_uuid], null)));
});
oc.web.dispatcher.comment_reply_to_key = (function oc$web$dispatcher$comment_reply_to_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"comment-reply-to-key","comment-reply-to-key",-790632876)));
});
oc.web.dispatcher.comments_key = (function oc$web$dispatcher$comments_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"comments","comments",-293346423)));
});
oc.web.dispatcher.activity_comments_key = (function oc$web$dispatcher$activity_comments_key(org_slug,activity_uuid){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.comments_key(org_slug),activity_uuid));
});
oc.web.dispatcher.sorted_comments_key = new cljs.core.Keyword(null,"sorted-comments","sorted-comments",1988882718);
oc.web.dispatcher.activity_sorted_comments_key = (function oc$web$dispatcher$activity_sorted_comments_key(org_slug,activity_uuid){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.comments_key(org_slug),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [activity_uuid,oc.web.dispatcher.sorted_comments_key], null)));
});
oc.web.dispatcher.teams_data_key = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"teams-data","teams-data",-808450077),new cljs.core.Keyword(null,"teams","teams",1677714510)], null);
oc.web.dispatcher.team_data_key = (function oc$web$dispatcher$team_data_key(team_id){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"teams-data","teams-data",-808450077),team_id,new cljs.core.Keyword(null,"data","data",-232669377)], null);
});
oc.web.dispatcher.team_roster_key = (function oc$web$dispatcher$team_roster_key(team_id){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"teams-data","teams-data",-808450077),team_id,new cljs.core.Keyword(null,"roster","roster",-2092272532)], null);
});
oc.web.dispatcher.team_channels_key = (function oc$web$dispatcher$team_channels_key(team_id){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"teams-data","teams-data",-808450077),team_id,new cljs.core.Keyword(null,"channels","channels",1132759174)], null);
});
oc.web.dispatcher.active_users_key = (function oc$web$dispatcher$active_users_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"active-users","active-users",-329555191)));
});
oc.web.dispatcher.follow_list_key = (function oc$web$dispatcher$follow_list_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"follow-list","follow-list",-537326492)));
});
oc.web.dispatcher.follow_list_last_added_key = (function oc$web$dispatcher$follow_list_last_added_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"follow-list-last-added","follow-list-last-added",782919652)));
});
oc.web.dispatcher.follow_publishers_list_key = (function oc$web$dispatcher$follow_publishers_list_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.follow_list_key(org_slug),new cljs.core.Keyword(null,"publisher-uuids","publisher-uuids",1855461704)));
});
oc.web.dispatcher.follow_boards_list_key = (function oc$web$dispatcher$follow_boards_list_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.follow_list_key(org_slug),new cljs.core.Keyword(null,"follow-boards-list","follow-boards-list",-461166530)));
});
oc.web.dispatcher.unfollow_board_uuids_key = (function oc$web$dispatcher$unfollow_board_uuids_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.follow_list_key(org_slug),new cljs.core.Keyword(null,"unfollow-board-uuids","unfollow-board-uuids",-201121143)));
});
oc.web.dispatcher.followers_count_key = (function oc$web$dispatcher$followers_count_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"followers-count","followers-count",448882417)));
});
oc.web.dispatcher.followers_publishers_count_key = (function oc$web$dispatcher$followers_publishers_count_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.followers_count_key(org_slug),new cljs.core.Keyword(null,"publishers","publishers",1474752298)));
});
oc.web.dispatcher.followers_boards_count_key = (function oc$web$dispatcher$followers_boards_count_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.followers_count_key(org_slug),new cljs.core.Keyword(null,"boards","boards",1912049694)));
});
oc.web.dispatcher.mention_users_key = (function oc$web$dispatcher$mention_users_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"mention-users","mention-users",-525519758)));
});
oc.web.dispatcher.users_info_hover_key = (function oc$web$dispatcher$users_info_hover_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570)));
});
oc.web.dispatcher.uploading_video_key = (function oc$web$dispatcher$uploading_video_key(org_slug,video_id){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"uploading-videos","uploading-videos",-851649346),video_id], null)));
});
/**
 * Find the board key for db based on the current path.
 */
oc.web.dispatcher.current_board_key = (function oc$web$dispatcher$current_board_key(){
var org_slug = (oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0 ? oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0() : oc.web.dispatcher.current_org_slug.call(null));
var board_slug = (oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0 ? oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0() : oc.web.dispatcher.current_board_slug.call(null));
return oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,board_slug);
});
oc.web.dispatcher.can_compose_key = new cljs.core.Keyword(null,"can-copmose?","can-copmose?",1050091788);
/**
 * Key for a boolean value: true if the user has at least one board
 * he can publish updates in.
 */
oc.web.dispatcher.org_can_compose_key = (function oc$web$dispatcher$org_can_compose_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data_key(org_slug),oc.web.dispatcher.can_compose_key));
});
oc.web.dispatcher.user_notifications_key = (function oc$web$dispatcher$user_notifications_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"user-notifications","user-notifications",-2046716914)));
});
oc.web.dispatcher.grouped_user_notifications_key = (function oc$web$dispatcher$grouped_user_notifications_key(org_slug){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-notifications","user-notifications",-2046716914),new cljs.core.Keyword(null,"grouped","grouped",15624546)], null)));
});
oc.web.dispatcher.sorted_user_notifications_key = (function oc$web$dispatcher$sorted_user_notifications_key(org_slug){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-notifications","user-notifications",-2046716914),new cljs.core.Keyword(null,"sorted","sorted",-896746253)], null)));
});
oc.web.dispatcher.reminders_key = (function oc$web$dispatcher$reminders_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"reminders","reminders",-2135532712)));
});
oc.web.dispatcher.reminders_data_key = (function oc$web$dispatcher$reminders_data_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.reminders_key(org_slug),new cljs.core.Keyword(null,"reminders-list","reminders-list",-1194258379)));
});
oc.web.dispatcher.reminders_roster_key = (function oc$web$dispatcher$reminders_roster_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.reminders_key(org_slug),new cljs.core.Keyword(null,"reminders-roster","reminders-roster",507031882)));
});
oc.web.dispatcher.reminder_edit_key = (function oc$web$dispatcher$reminder_edit_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.reminders_key(org_slug),new cljs.core.Keyword(null,"reminder-edit","reminder-edit",1168054794)));
});
oc.web.dispatcher.change_data_key = (function oc$web$dispatcher$change_data_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"change-data","change-data",2068475383)));
});
oc.web.dispatcher.activities_read_key = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activities-read","activities-read",2125722631)], null);
oc.web.dispatcher.org_seens_key = (function oc$web$dispatcher$org_seens_key(org_slug){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_key(org_slug),new cljs.core.Keyword(null,"container-seen","container-seen",1269698583)));
});
oc.web.dispatcher.cmail_state_key = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321)], null);
oc.web.dispatcher.cmail_data_key = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261)], null);
oc.web.dispatcher.get_posts_for_board = (function oc$web$dispatcher$get_posts_for_board(posts_data,board_slug){
var posts_list = cljs.core.vals(posts_data);
var filter_fn = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,oc.web.utils.drafts.default_drafts_board_slug))?(function (p1__41486_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(p1__41486_SHARP_),"published");
}):(function (p1__41487_SHARP_){
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(p1__41487_SHARP_),board_slug)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(p1__41487_SHARP_),"published")));
}));
return cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(filter_fn,cljs.core.last),posts_data);
});
oc.web.dispatcher.is_container_QMARK_ = (function oc$web$dispatcher$is_container_QMARK_(container_slug){
var G__41490 = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(container_slug);
var fexpr__41489 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"inbox","inbox",1888669443),null,new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533),null,new cljs.core.Keyword(null,"following","following",-2049193617),null,new cljs.core.Keyword(null,"activity","activity",-1179221455),null,new cljs.core.Keyword(null,"replies","replies",-1389888974),null,new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283),null,new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830),null], null), null);
return (fexpr__41489.cljs$core$IFn$_invoke$arity$1 ? fexpr__41489.cljs$core$IFn$_invoke$arity$1(G__41490) : fexpr__41489.call(null,G__41490));
});
oc.web.dispatcher.is_container_with_sort_QMARK_ = (function oc$web$dispatcher$is_container_with_sort_QMARK_(container_slug){
var fexpr__41491 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 3, ["all-posts",null,"following",null,"unfollowing",null], null), null);
return (fexpr__41491.cljs$core$IFn$_invoke$arity$1 ? fexpr__41491.cljs$core$IFn$_invoke$arity$1(container_slug) : fexpr__41491.call(null,container_slug));
});
oc.web.dispatcher.is_recent_activity_QMARK_ = (function oc$web$dispatcher$is_recent_activity_QMARK_(container_slug){
var temp__5735__auto__ = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(container_slug);
if(cljs.core.truth_(temp__5735__auto__)){
var container_slug_kw = temp__5735__auto__;
var fexpr__41492 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"replies","replies",-1389888974),null], null), null);
return (fexpr__41492.cljs$core$IFn$_invoke$arity$1 ? fexpr__41492.cljs$core$IFn$_invoke$arity$1(container_slug_kw) : fexpr__41492.call(null,container_slug_kw));
} else {
return null;
}
});
oc.web.dispatcher.get_container_posts = (function oc$web$dispatcher$get_container_posts(base,posts_data,org_slug,container_slug,sort_type,items_key){
var cnt_key = (cljs.core.truth_(oc.web.dispatcher.is_container_QMARK_(container_slug))?oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,container_slug,sort_type):((cljs.core.seq((oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0 ? oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0() : oc.web.dispatcher.current_contributions_id.call(null))))?oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,container_slug):oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,container_slug)
));
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,cnt_key);
var posts_list = cljs.core.get.cljs$core$IFn$_invoke$arity$2(container_data,items_key);
var container_posts = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (entry){
if(((cljs.core.map_QMARK_(entry)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(entry),new cljs.core.Keyword(null,"entry","entry",505168823))))){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.get.cljs$core$IFn$_invoke$arity$2(posts_data,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry)),entry], 0));
} else {
return entry;
}
}),posts_list);
var items = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(container_slug,oc.web.utils.drafts.default_drafts_board_slug))?cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.not,new cljs.core.Keyword(null,"published?","published?",-1603043839)),container_posts):container_posts);
return cljs.core.vec(items);
});
oc.web.dispatcher.ui_theme_key = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ui-theme","ui-theme",1992064701)], null);
oc.web.dispatcher.drv_spec = (function oc$web$dispatcher$drv_spec(db){
return cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"login-with-email-error","login-with-email-error",-373631840),new cljs.core.Keyword(null,"activity-share","activity-share",-127339936),new cljs.core.Keyword(null,"contributions-id","contributions-id",-67679488),new cljs.core.Keyword(null,"payments","payments",-1324138047),new cljs.core.Keyword(null,"org-data","org-data",96720321),new cljs.core.Keyword(null,"edit-user-profile-avatar","edit-user-profile-avatar",303025729),new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167),new cljs.core.Keyword(null,"sorted-user-notifications","sorted-user-notifications",2145044225),new cljs.core.Keyword(null,"invite-add-slack-checked","invite-add-slack-checked",285177890),new cljs.core.Keyword(null,"add-comment-focus","add-comment-focus",-452934461),new cljs.core.Keyword(null,"wrt-activity-data","wrt-activity-data",-1368150621),new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411),new cljs.core.Keyword(null,"teams-data","teams-data",-808450077),new cljs.core.Keyword(null,"follow-list","follow-list",-537326492),new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700),new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004),new cljs.core.Keyword(null,"expand-image-src","expand-image-src",-899766588),new cljs.core.Keyword(null,"org-settings-team-management","org-settings-team-management",-2085361788),new cljs.core.Keyword(null,"follow-list-last-added","follow-list-last-added",782919652),new cljs.core.Keyword(null,"followers-boards-count","followers-boards-count",334424133),new cljs.core.Keyword(null,"mobile-swipe-menu","mobile-swipe-menu",897943653),new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),new cljs.core.Keyword(null,"team-roster","team-roster",-1945092859),new cljs.core.Keyword(null,"replies-badge","replies-badge",-112768699),new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219),new cljs.core.Keyword(null,"confirm-invitation","confirm-invitation",1250144934),new cljs.core.Keyword(null,"activities-read","activities-read",2125722631),new cljs.core.Keyword(null,"wrt-show","wrt-show",2081238599),new cljs.core.Keyword(null,"orgs-dropdown-visible","orgs-dropdown-visible",801323944),oc.web.dispatcher.checkout_result_key,new cljs.core.Keyword(null,"contributions-user-data","contributions-user-data",799006441),new cljs.core.Keyword(null,"active-users","active-users",-329555191),new cljs.core.Keyword(null,"site-menu-open","site-menu-open",-1324487927),new cljs.core.Keyword(null,"reminders-roster","reminders-roster",507031882),new cljs.core.Keyword(null,"search-active","search-active",913672682),new cljs.core.Keyword(null,"reminder-edit","reminder-edit",1168054794),new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261),new cljs.core.Keyword(null,"posts-data","posts-data",-788590901),new cljs.core.Keyword(null,"collect-password","collect-password",690937675),new cljs.core.Keyword(null,"unfollowing-data","unfollowing-data",-252860501),new cljs.core.Keyword(null,"unread-notifications-count","unread-notifications-count",2134919083),new cljs.core.Keyword(null,"entry-editing","entry-editing",-1938994964),new cljs.core.Keyword(null,"team-channels","team-channels",321697292),new cljs.core.Keyword(null,"grouped-user-notifications","grouped-user-notifications",-1098209203),new cljs.core.Keyword(null,"board-data","board-data",1372958925),new cljs.core.Keyword(null,"unread-notifications","unread-notifications",560844173),new cljs.core.Keyword(null,"route","route",329891309),new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915),new cljs.core.Keyword(null,"user-notifications","user-notifications",-2046716914),new cljs.core.Keyword(null,"password-reset","password-reset",1971592302),new cljs.core.Keyword(null,"id-token","id-token",-339268306),new cljs.core.Keyword(null,"user-responded-to-push-permission?","user-responded-to-push-permission?",1261192686),oc.web.dispatcher.checkout_update_plan_key,new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"mobile-navigation-sidebar","mobile-navigation-sidebar",-1723544081),new cljs.core.Keyword(null,"following-data","following-data",252543695),new cljs.core.Keyword(null,"entry-save-on-exit","entry-save-on-exit",-1377879888),new cljs.core.Keyword(null,"activity-shared-data","activity-shared-data",-674728784),new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504),new cljs.core.Keyword(null,"notifications-data","notifications-data",260044272),new cljs.core.Keyword(null,"edit-reminder","edit-reminder",1674513040),new cljs.core.Keyword(null,"show-post-added-tooltip","show-post-added-tooltip",-1747287376),new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864),new cljs.core.Keyword(null,"filtered-posts","filtered-posts",-1789524144),new cljs.core.Keyword(null,"secure-activity-data","secure-activity-data",223405040),new cljs.core.Keyword(null,"jwt","jwt",1504015441),new cljs.core.Keyword(null,"comments-data","comments-data",1871210833),new cljs.core.Keyword(null,"show-edit-tooltip","show-edit-tooltip",2074919569),new cljs.core.Keyword(null,"team-data","team-data",-732020079),new cljs.core.Keyword(null,"followers-count","followers-count",448882417),new cljs.core.Keyword(null,"mention-users","mention-users",-525519758),new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794),new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658),new cljs.core.Keyword(null,"alert-modal","alert-modal",-1208187214),new cljs.core.Keyword(null,"navbar-data","navbar-data",2074703570),new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118),new cljs.core.Keyword(null,"add-comment-data","add-comment-data",-1679568046),new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762),new cljs.core.Keyword(null,"loading","loading",-737050189),new cljs.core.Keyword(null,"expo-app-version","expo-app-version",-1636851181),new cljs.core.Keyword(null,"entry-board-slug","entry-board-slug",-2062350797),new cljs.core.Keyword(null,"signup-with-email","signup-with-email",-22609037),new cljs.core.Keyword(null,"orgs","orgs",155776628),new cljs.core.Keyword(null,"reminders-data","reminders-data",-1331370092),new cljs.core.Keyword(null,"show-invite-box","show-invite-box",1572056533),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),new cljs.core.Keyword(null,"nux","nux",-865367307),new cljs.core.Keyword(null,"following-badge","following-badge",1108067285),new cljs.core.Keyword(null,"container-data","container-data",-53681130),new cljs.core.Keyword(null,"secure-id","secure-id",-626735882),new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570),new cljs.core.Keyword(null,"team-invite","team-invite",-340365962),new cljs.core.Keyword(null,"expo-deep-link-origin","expo-deep-link-origin",404078070),new cljs.core.Keyword(null,"query-params","query-params",900640534),new cljs.core.Keyword(null,"show-add-post-tooltip","show-add-post-tooltip",1769173942),new cljs.core.Keyword(null,"bookmarks-data","bookmarks-data",-638909193),new cljs.core.Keyword(null,"change-data","change-data",2068475383),new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321),new cljs.core.Keyword(null,"user-info-data","user-info-data",-1081830984),new cljs.core.Keyword(null,"org-avatar-editing","org-avatar-editing",-1933353352),new cljs.core.Keyword(null,"media-input","media-input",107658136),new cljs.core.Keyword(null,"email-verification","email-verification",-2006200871),new cljs.core.Keyword(null,"wrt-read-data","wrt-read-data",-1241718247),new cljs.core.Keyword(null,"invite-users","invite-users",107417337),new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342),new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"search-results","search-results",306464634),new cljs.core.Keyword(null,"contributions-data","contributions-data",242246811),new cljs.core.Keyword(null,"nux-user-type","nux-user-type",414380283),new cljs.core.Keyword(null,"org-dashboard-data","org-dashboard-data",-593195621),new cljs.core.Keyword(null,"drafts-data","drafts-data",-1247967685),new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965),new cljs.core.Keyword(null,"ap-loading","ap-loading",1563043900),new cljs.core.Keyword(null,"show-invite-people-tooltip","show-invite-people-tooltip",307724796),new cljs.core.Keyword(null,"current-panel","current-panel",1482744861),new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579),new cljs.core.Keyword(null,"activity-share-medium","activity-share-medium",866045149),new cljs.core.Keyword(null,"ui-theme","ui-theme",1992064701),new cljs.core.Keyword(null,"comment-reply-to","comment-reply-to",1200575357),new cljs.core.Keyword(null,"follow-boards-list","follow-boards-list",-461166530),new cljs.core.Keyword(null,"invite-data","invite-data",-758838050),new cljs.core.Keyword(null,"can-compose","can-compose",-1144210210),new cljs.core.Keyword(null,"expo","expo",-610384546),new cljs.core.Keyword(null,"activity-share-container","activity-share-container",-1384168706)],[new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"login-with-email-error","login-with-email-error",-373631840).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"activity-share","activity-share",-127339936).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route","route",329891309)], null),(function (route){
return new cljs.core.Keyword(null,"contributions","contributions",-1280485964).cljs$core$IFn$_invoke$arity$1(route);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.payments_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
if(cljs.core.truth_(org_slug)){
return (oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2 ? oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(base,org_slug) : oc.web.dispatcher.org_data.call(null,base,org_slug));
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"edit-user-profile-avatar","edit-user-profile-avatar",303025729).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
if(cljs.core.truth_((function (){var and__4115__auto__ = base;
if(cljs.core.truth_(and__4115__auto__)){
return org_slug;
} else {
return and__4115__auto__;
}
})())){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.sorted_user_notifications_key(org_slug));
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"invite-add-slack-checked","invite-add-slack-checked",285177890).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"add-comment-focus","add-comment-focus",-452934461).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004)], null),(function (base,org_slug,panel_stack){
if(cljs.core.truth_((function (){var and__4115__auto__ = panel_stack;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__41496_SHARP_){
return clojure.string.starts_with_QMARK_(cljs.core.name(p1__41496_SHARP_),"wrt-");
}),panel_stack));
} else {
return and__4115__auto__;
}
})())){
var temp__41407__auto__ = cljs.core.name(cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__41497_SHARP_){
return clojure.string.starts_with_QMARK_(cljs.core.name(p1__41497_SHARP_),"wrt-");
}),panel_stack)));
if(cljs.core.truth_(temp__41407__auto__)){
var wrt_panel = temp__41407__auto__;
var temp__41407__auto____$1 = cljs.core.subs.cljs$core$IFn$_invoke$arity$3(wrt_panel,(4),((wrt_panel).length));
if(cljs.core.truth_(temp__41407__auto____$1)){
var wrt_uuid = temp__41407__auto____$1;
return (oc.web.dispatcher.activity_data_get.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.activity_data_get.cljs$core$IFn$_invoke$arity$3(org_slug,wrt_uuid,base) : oc.web.dispatcher.activity_data_get.call(null,org_slug,wrt_uuid,base));
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.teams_data_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.follow_list_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"expand-image-src","expand-image-src",-899766588).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"query-params","query-params",900640534),new cljs.core.Keyword(null,"org-data","org-data",96720321),new cljs.core.Keyword(null,"team-data","team-data",-732020079),new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219)], null),(function (base,query_params,org_data,team_data){
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"um-domain-invite","um-domain-invite",-1217004114),new cljs.core.Keyword(null,"um-domain-invite","um-domain-invite",-1217004114).cljs$core$IFn$_invoke$arity$1(base),new cljs.core.Keyword(null,"add-email-domain-team-error","add-email-domain-team-error",-1479228464),new cljs.core.Keyword(null,"add-email-domain-team-error","add-email-domain-team-error",-1479228464).cljs$core$IFn$_invoke$arity$1(base),new cljs.core.Keyword(null,"team-data","team-data",-732020079),team_data,new cljs.core.Keyword(null,"query-params","query-params",900640534),query_params], null);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.follow_list_last_added_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.followers_boards_count_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"mobile-swipe-menu","mobile-swipe-menu",897943653).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
if(cljs.core.truth_(((cljs.core.not(new cljs.core.Keyword(null,"jwt","jwt",1504015441).cljs$core$IFn$_invoke$arity$1(base)))?(function (){var and__4115__auto__ = new cljs.core.Keyword(null,"id-token","id-token",-339268306).cljs$core$IFn$_invoke$arity$1(base);
if(cljs.core.truth_(and__4115__auto__)){
return (oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$1 ? oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$1(base) : oc.web.dispatcher.current_secure_activity_id.call(null,base));
} else {
return and__4115__auto__;
}
})():false))){
return cljs.core.select_keys(new cljs.core.Keyword(null,"id-token","id-token",-339268306).cljs$core$IFn$_invoke$arity$1(base),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),new cljs.core.Keyword(null,"first-name","first-name",-1559982131),new cljs.core.Keyword(null,"last-name","last-name",-1695738974),new cljs.core.Keyword(null,"name","name",1843675177)], null));
} else {
return new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915).cljs$core$IFn$_invoke$arity$1(base);
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-data","org-data",96720321)], null),(function (base,org_data){
if(cljs.core.truth_(org_data)){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.team_roster_key(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data)));
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.replies_badge_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.auth_settings_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"route","route",329891309),new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219),new cljs.core.Keyword(null,"jwt","jwt",1504015441)], null),(function (base,route,auth_settings,jwt){
return new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"invitation-confirmed","invitation-confirmed",-298226477),new cljs.core.Keyword(null,"email-confirmed","email-confirmed",-2125677510).cljs$core$IFn$_invoke$arity$1(base),new cljs.core.Keyword(null,"invitation-error","invitation-error",1846843525),((cljs.core.contains_QMARK_(base,new cljs.core.Keyword(null,"email-confirmed","email-confirmed",-2125677510))) && (cljs.core.not(new cljs.core.Keyword(null,"email-confirmed","email-confirmed",-2125677510).cljs$core$IFn$_invoke$arity$1(base)))),new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219),auth_settings,new cljs.core.Keyword(null,"token","token",-1211463215),new cljs.core.Keyword(null,"token","token",-1211463215).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(route)),new cljs.core.Keyword(null,"jwt","jwt",1504015441),jwt], null);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.activities_read_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"wrt-show","wrt-show",2081238599).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"orgs-dropdown-visible","orgs-dropdown-visible",801323944).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.checkout_result_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"active-users","active-users",-329555191),new cljs.core.Keyword(null,"contributions-id","contributions-id",-67679488)], null),(function (base,active_users,contributions_id){
if(cljs.core.truth_((function (){var and__4115__auto__ = active_users;
if(cljs.core.truth_(and__4115__auto__)){
return contributions_id;
} else {
return and__4115__auto__;
}
})())){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(active_users,contributions_id);
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.active_users_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"site-menu-open","site-menu-open",-1324487927).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.reminders_roster_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"search-active","search-active",913672682).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.reminder_edit_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.cmail_data_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
if(cljs.core.truth_((function (){var and__4115__auto__ = base;
if(cljs.core.truth_(and__4115__auto__)){
return org_slug;
} else {
return and__4115__auto__;
}
})())){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.posts_data_key(org_slug));
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"jwt","jwt",1504015441)], null),(function (base,jwt){
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874),new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874).cljs$core$IFn$_invoke$arity$1(base),new cljs.core.Keyword(null,"collect-pswd-error","collect-pswd-error",-2005825999),new cljs.core.Keyword(null,"collect-password-error","collect-password-error",-1137727549).cljs$core$IFn$_invoke$arity$1(base),new cljs.core.Keyword(null,"jwt","jwt",1504015441),jwt], null);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830)));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"unread-notifications","unread-notifications",560844173)], null),(function (notifications){
var ncount = cljs.core.count(notifications);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.dispatcher",null,584,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Unread notification count updated: ",ncount], null);
}),null)),null,346934542);

if(oc.shared.useragent.desktop_app_QMARK_){
window.OCCarrotDesktop.setBadgeCount(ncount);
} else {
}

return ncount;
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"entry-editing","entry-editing",-1938994964).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-data","org-data",96720321)], null),(function (base,org_data){
if(cljs.core.truth_(org_data)){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.team_channels_key(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data)));
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
if(cljs.core.truth_((function (){var and__4115__auto__ = base;
if(cljs.core.truth_(and__4115__auto__)){
return org_slug;
} else {
return and__4115__auto__;
}
})())){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.grouped_user_notifications_key(org_slug));
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),new cljs.core.Keyword(null,"board-slug","board-slug",99003663)], null),(function (base,org_slug,board_slug){
return (oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$3(base,org_slug,board_slug) : oc.web.dispatcher.board_data.call(null,base,org_slug,board_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-notifications","user-notifications",-2046716914)], null),(function (notifications){
return cljs.core.filter.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"unread","unread",-1950424572),notifications);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.router_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
if(cljs.core.truth_((function (){var and__4115__auto__ = base;
if(cljs.core.truth_(and__4115__auto__)){
return org_slug;
} else {
return and__4115__auto__;
}
})())){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.user_notifications_key(org_slug));
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219)], null),(function (base,auth_settings){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219),auth_settings,new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"collect-pswd-error","collect-pswd-error",-2005825999).cljs$core$IFn$_invoke$arity$1(base)], null);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"id-token","id-token",-339268306).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.boolean$(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.expo_push_token_key));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.checkout_update_plan_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route","route",329891309)], null),(function (route){
return new cljs.core.Keyword(null,"activity","activity",-1179221455).cljs$core$IFn$_invoke$arity$1(route);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route","route",329891309)], null),(function (route){
return new cljs.core.Keyword(null,"board","board",-1907017633).cljs$core$IFn$_invoke$arity$1(route);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"mobile-navigation-sidebar","mobile-navigation-sidebar",-1723544081).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"following","following",-2049193617)));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"entry-save-on-exit","entry-save-on-exit",-1377879888).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"activity-shared-data","activity-shared-data",-674728784).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778)], null),(function (base,org_slug,activity_uuid){
return (oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3(org_slug,activity_uuid,base) : oc.web.dispatcher.activity_data.call(null,org_slug,activity_uuid,base));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route","route",329891309)], null),(function (route){
return new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504).cljs$core$IFn$_invoke$arity$1(route);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.notifications_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"edit-reminder","edit-reminder",1674513040).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"nux","nux",-865367307)], null),(function (nux){
return new cljs.core.Keyword(null,"show-post-added-tooltip","show-post-added-tooltip",-1747287376).cljs$core$IFn$_invoke$arity$1(nux);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-data","org-data",96720321),new cljs.core.Keyword(null,"posts-data","posts-data",-788590901),new cljs.core.Keyword(null,"route","route",329891309)], null),(function (base,org_data,posts_data,route){
var container_slug = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"contributions","contributions",-1280485964).cljs$core$IFn$_invoke$arity$1(route);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"board","board",-1907017633).cljs$core$IFn$_invoke$arity$1(route);
}
})();
if(cljs.core.truth_((function (){var and__4115__auto__ = base;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = org_data;
if(cljs.core.truth_(and__4115__auto____$1)){
var and__4115__auto____$2 = posts_data;
if(cljs.core.truth_(and__4115__auto____$2)){
var and__4115__auto____$3 = route;
if(cljs.core.truth_(and__4115__auto____$3)){
return container_slug;
} else {
return and__4115__auto____$3;
}
} else {
return and__4115__auto____$2;
}
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
return oc.web.dispatcher.get_container_posts(base,posts_data,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),container_slug,new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504).cljs$core$IFn$_invoke$arity$1(route),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),new cljs.core.Keyword(null,"secure-id","secure-id",-626735882)], null),(function (base,org_slug,secure_id){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),(oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$3(org_slug,secure_id,base) : oc.web.dispatcher.secure_activity_data.call(null,org_slug,secure_id,base)),new cljs.core.Keyword(null,"is-showing-alert","is-showing-alert",1899000552),cljs.core.boolean$(new cljs.core.Keyword(null,"alert-modal","alert-modal",-1208187214).cljs$core$IFn$_invoke$arity$1(base))], null);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"jwt","jwt",1504015441).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.comments_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"nux","nux",-865367307)], null),(function (nux){
return new cljs.core.Keyword(null,"show-edit-tooltip","show-edit-tooltip",2074919569).cljs$core$IFn$_invoke$arity$1(nux);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-data","org-data",96720321)], null),(function (base,org_data){
if(cljs.core.truth_(org_data)){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.team_data_key(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data)));
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.followers_count_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.mention_users_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.add_comment_force_update_root_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return (oc.web.dispatcher.editable_boards_data.cljs$core$IFn$_invoke$arity$2 ? oc.web.dispatcher.editable_boards_data.cljs$core$IFn$_invoke$arity$2(base,org_slug) : oc.web.dispatcher.editable_boards_data.call(null,base,org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"alert-modal","alert-modal",-1208187214).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-data","org-data",96720321),new cljs.core.Keyword(null,"board-data","board-data",1372958925),new cljs.core.Keyword(null,"contributions-user-data","contributions-user-data",799006441),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"contributions-id","contributions-id",-67679488),new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778),new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], null),(function (base,org_data,board_data,contributions_user_data,org_slug,board_slug,contributions_id,activity_uuid,current_user_data){
var navbar_data = cljs.core.select_keys(base,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411),new cljs.core.Keyword(null,"orgs-dropdown-visible","orgs-dropdown-visible",801323944),new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004),new cljs.core.Keyword(null,"search-active","search-active",913672682),new cljs.core.Keyword(null,"show-whats-new-green-dot","show-whats-new-green-dot",-206531957)], null));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(navbar_data,new cljs.core.Keyword(null,"org-data","org-data",96720321),org_data),new cljs.core.Keyword(null,"board-data","board-data",1372958925),board_data),new cljs.core.Keyword(null,"contributions-user-data","contributions-user-data",799006441),contributions_user_data),new cljs.core.Keyword(null,"current-org-slug","current-org-slug",1185011374),org_slug),new cljs.core.Keyword(null,"current-board-slug","current-board-slug",1670379364),board_slug),new cljs.core.Keyword(null,"current-contributions-id","current-contributions-id",-1702467529),contributions_id),new cljs.core.Keyword(null,"current-activity-id","current-activity-id",-930108529),activity_uuid),new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),current_user_data);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118).cljs$core$IFn$_invoke$arity$1(base),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"edit-user-profile-failed","edit-user-profile-failed",418884834).cljs$core$IFn$_invoke$arity$1(base)], null);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.add_comment_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-data","org-data",96720321),new cljs.core.Keyword(null,"posts-data","posts-data",-788590901),new cljs.core.Keyword(null,"route","route",329891309)], null),(function (base,org_data,posts_data,route){
var container_slug = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"contributions","contributions",-1280485964).cljs$core$IFn$_invoke$arity$1(route);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"board","board",-1907017633).cljs$core$IFn$_invoke$arity$1(route);
}
})();
if(cljs.core.truth_((function (){var and__4115__auto__ = base;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = org_data;
if(cljs.core.truth_(and__4115__auto____$1)){
var and__4115__auto____$2 = container_slug;
if(cljs.core.truth_(and__4115__auto____$2)){
return posts_data;
} else {
return and__4115__auto____$2;
}
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
return oc.web.dispatcher.get_container_posts(base,posts_data,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),container_slug,new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504).cljs$core$IFn$_invoke$arity$1(route),new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762));
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"loading","loading",-737050189).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.expo_app_version_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route","route",329891309)], null),(function (route){
return new cljs.core.Keyword(null,"entry-board","entry-board",1825593318).cljs$core$IFn$_invoke$arity$1(route);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"signup-with-email","signup-with-email",-22609037).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.orgs_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.reminders_data_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.show_invite_box_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route","route",329891309)], null),(function (route){
return new cljs.core.Keyword(null,"org","org",1495985).cljs$core$IFn$_invoke$arity$1(route);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"nux","nux",-865367307).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.following_badge_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"contributions-id","contributions-id",-67679488),new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778),new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504)], null),(function (base,org_slug,board_slug,contributions_id,activity_uuid,sort_type){
if(cljs.core.truth_((function (){var and__4115__auto__ = org_slug;
if(cljs.core.truth_(and__4115__auto__)){
var or__4126__auto__ = board_slug;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return contributions_id;
}
} else {
return and__4115__auto__;
}
})())){
var is_contributions_QMARK_ = cljs.core.seq(contributions_id);
var cnt_key = ((is_contributions_QMARK_)?oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,contributions_id):(cljs.core.truth_(oc.web.dispatcher.is_container_QMARK_(board_slug))?oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,board_slug,sort_type):oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,board_slug)
));
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,cnt_key);
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route","route",329891309)], null),(function (route){
return new cljs.core.Keyword(null,"secure-id","secure-id",-626735882).cljs$core$IFn$_invoke$arity$1(route);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.users_info_hover_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"route","route",329891309),new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219),new cljs.core.Keyword(null,"jwt","jwt",1504015441)], null),(function (base,route,auth_settings,jwt){
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219),auth_settings,new cljs.core.Keyword(null,"invite-token","invite-token",610560078),new cljs.core.Keyword(null,"invite-token","invite-token",610560078).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(route)),new cljs.core.Keyword(null,"jwt","jwt",1504015441),jwt], null);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.expo_deep_link_origin_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route","route",329891309)], null),(function (route){
return new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(route);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"nux","nux",-865367307)], null),(function (nux){
return new cljs.core.Keyword(null,"show-add-post-tooltip","show-add-post-tooltip",1769173942).cljs$core$IFn$_invoke$arity$1(nux);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283)));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
if(cljs.core.truth_((function (){var and__4115__auto__ = base;
if(cljs.core.truth_(and__4115__auto__)){
return org_slug;
} else {
return and__4115__auto__;
}
})())){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.change_data_key(org_slug));
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.cmail_state_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"active-users","active-users",-329555191),new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004)], null),(function (base,active_users,panel_stack){
if(cljs.core.truth_((function (){var and__4115__auto__ = panel_stack;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__41498_SHARP_){
return clojure.string.starts_with_QMARK_(cljs.core.name(p1__41498_SHARP_),"user-info-");
}),panel_stack));
} else {
return and__4115__auto__;
}
})())){
var temp__41407__auto__ = cljs.core.name(cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__41499_SHARP_){
return clojure.string.starts_with_QMARK_(cljs.core.name(p1__41499_SHARP_),"user-info-");
}),panel_stack)));
if(cljs.core.truth_(temp__41407__auto__)){
var user_info_panel = temp__41407__auto__;
var temp__41407__auto____$1 = cljs.core.subs.cljs$core$IFn$_invoke$arity$3(user_info_panel,(("user-info-").length),((user_info_panel).length));
if(cljs.core.truth_(temp__41407__auto____$1)){
var user_id = temp__41407__auto____$1;
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(active_users,user_id);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"org-avatar-editing","org-avatar-editing",-1933353352).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"media-input","media-input",107658136).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219)], null),(function (base,auth_settings){
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219),auth_settings,new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"email-verification-error","email-verification-error",358093817).cljs$core$IFn$_invoke$arity$1(base),new cljs.core.Keyword(null,"success","success",1890645906),new cljs.core.Keyword(null,"email-verification-success","email-verification-success",-1955344779).cljs$core$IFn$_invoke$arity$1(base)], null);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004)], null),(function (base,panel_stack){
if(cljs.core.truth_((function (){var and__4115__auto__ = panel_stack;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__41494_SHARP_){
return clojure.string.starts_with_QMARK_(cljs.core.name(p1__41494_SHARP_),"wrt-");
}),panel_stack));
} else {
return and__4115__auto__;
}
})())){
var temp__41407__auto__ = cljs.core.name(cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__41495_SHARP_){
return clojure.string.starts_with_QMARK_(cljs.core.name(p1__41495_SHARP_),"wrt-");
}),panel_stack)));
if(cljs.core.truth_(temp__41407__auto__)){
var wrt_panel = temp__41407__auto__;
var temp__41407__auto____$1 = cljs.core.subs.cljs$core$IFn$_invoke$arity$3(wrt_panel,(4),((wrt_panel).length));
if(cljs.core.truth_(temp__41407__auto____$1)){
var wrt_uuid = temp__41407__auto____$1;
return (oc.web.dispatcher.activity_read_data.cljs$core$IFn$_invoke$arity$2 ? oc.web.dispatcher.activity_read_data.cljs$core$IFn$_invoke$arity$2(wrt_uuid,base) : oc.web.dispatcher.activity_read_data.call(null,wrt_uuid,base));
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.follow_publishers_list_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.PersistentVector.EMPTY,db], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"search-results","search-results",306464634).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),new cljs.core.Keyword(null,"contributions-id","contributions-id",-67679488)], null),(function (base,org_slug,contributions_id){
if(cljs.core.truth_((function (){var and__4115__auto__ = org_slug;
if(cljs.core.truth_(and__4115__auto__)){
return contributions_id;
} else {
return and__4115__auto__;
}
})())){
return (oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$2 ? oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$2(org_slug,contributions_id) : oc.web.dispatcher.contributions_data.call(null,org_slug,contributions_id));
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"nux","nux",-865367307)], null),(function (nux){
return new cljs.core.Keyword(null,"user-type","user-type",738868936).cljs$core$IFn$_invoke$arity$1(nux);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 22, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"orgs","orgs",155776628),new cljs.core.Keyword(null,"org-data","org-data",96720321),new cljs.core.Keyword(null,"contributions-data","contributions-data",242246811),new cljs.core.Keyword(null,"container-data","container-data",-53681130),new cljs.core.Keyword(null,"posts-data","posts-data",-788590901),new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),new cljs.core.Keyword(null,"entry-editing","entry-editing",-1938994964),new cljs.core.Keyword(null,"jwt","jwt",1504015441),new cljs.core.Keyword(null,"wrt-show","wrt-show",2081238599),new cljs.core.Keyword(null,"loading","loading",-737050189),new cljs.core.Keyword(null,"payments","payments",-1324138047),new cljs.core.Keyword(null,"search-active","search-active",913672682),new cljs.core.Keyword(null,"user-info-data","user-info-data",-1081830984),new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),new cljs.core.Keyword(null,"active-users","active-users",-329555191),new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342),new cljs.core.Keyword(null,"follow-boards-list","follow-boards-list",-461166530),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"contributions-id","contributions-id",-67679488),new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778)], null),(function (base,orgs,org_data,contributions_data,container_data,posts_data,activity_data,entry_editing,jwt,wrt_show,loading,payments,search_active,user_info_data,current_user_data,active_users,follow_publishers_list,follow_boards_list,org_slug,board_slug,contributions_id,activity_uuid){
return cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"org-data","org-data",96720321),new cljs.core.Keyword(null,"app-loading","app-loading",248815106),new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004),new cljs.core.Keyword(null,"force-login-wall","force-login-wall",1907021508),new cljs.core.Keyword(null,"current-board-slug","current-board-slug",1670379364),new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),new cljs.core.Keyword(null,"is-showing-alert","is-showing-alert",1899000552),new cljs.core.Keyword(null,"active-users","active-users",-329555191),new cljs.core.Keyword(null,"search-active","search-active",913672682),new cljs.core.Keyword(null,"posts-data","posts-data",-788590901),new cljs.core.Keyword(null,"jwt-data","jwt-data",1687811404),new cljs.core.Keyword(null,"entry-edit-dissmissing","entry-edit-dissmissing",-1058375764),new cljs.core.Keyword(null,"payments-data","payments-data",1676265421),new cljs.core.Keyword(null,"current-org-slug","current-org-slug",1185011374),new cljs.core.Keyword(null,"current-activity-id","current-activity-id",-930108529),new cljs.core.Keyword(null,"is-sharing-activity","is-sharing-activity",-1623884847),new cljs.core.Keyword(null,"orgs","orgs",155776628),new cljs.core.Keyword(null,"initial-section-editing","initial-section-editing",1587721238),new cljs.core.Keyword(null,"container-data","container-data",-53681130),new cljs.core.Keyword(null,"current-contributions-id","current-contributions-id",-1702467529),new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321),new cljs.core.Keyword(null,"show-section-add-cb","show-section-add-cb",2030550136),new cljs.core.Keyword(null,"user-info-data","user-info-data",-1081830984),new cljs.core.Keyword(null,"media-input","media-input",107658136),new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342),new cljs.core.Keyword(null,"contributions-data","contributions-data",242246811),new cljs.core.Keyword(null,"follow-boards-list","follow-boards-list",-461166530),new cljs.core.Keyword(null,"entry-editing-board-slug","entry-editing-board-slug",1272355038),new cljs.core.Keyword(null,"activity-share-container","activity-share-container",-1384168706)],[org_data,loading,new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004).cljs$core$IFn$_invoke$arity$1(base),new cljs.core.Keyword(null,"force-login-wall","force-login-wall",1907021508).cljs$core$IFn$_invoke$arity$1(base),board_slug,current_user_data,cljs.core.boolean$(new cljs.core.Keyword(null,"alert-modal","alert-modal",-1208187214).cljs$core$IFn$_invoke$arity$1(base)),active_users,search_active,posts_data,jwt,new cljs.core.Keyword(null,"entry-edit-dissmissing","entry-edit-dissmissing",-1058375764).cljs$core$IFn$_invoke$arity$1(base),payments,org_slug,activity_uuid,cljs.core.boolean$(new cljs.core.Keyword(null,"activity-share","activity-share",-127339936).cljs$core$IFn$_invoke$arity$1(base)),orgs,new cljs.core.Keyword(null,"initial-section-editing","initial-section-editing",1587721238).cljs$core$IFn$_invoke$arity$1(base),container_data,contributions_id,cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.cmail_state_key),new cljs.core.Keyword(null,"show-section-add-cb","show-section-add-cb",2030550136).cljs$core$IFn$_invoke$arity$1(base),user_info_data,new cljs.core.Keyword(null,"media-input","media-input",107658136).cljs$core$IFn$_invoke$arity$1(base),follow_publishers_list,contributions_data,follow_boards_list,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(entry_editing),new cljs.core.Keyword(null,"activity-share-container","activity-share-container",-1384168706).cljs$core$IFn$_invoke$arity$1(base)]);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"drafts","drafts",1523624562)));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"ap-loading","ap-loading",1563043900).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"nux","nux",-865367307)], null),(function (nux){
return new cljs.core.Keyword(null,"show-invite-people-tooltip","show-invite-people-tooltip",307724796).cljs$core$IFn$_invoke$arity$1(nux);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004)], null),(function (panel_stack){
return cljs.core.last(panel_stack);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.followers_publishers_count_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"activity-share-medium","activity-share-medium",866045149).cljs$core$IFn$_invoke$arity$1(base);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.ui_theme_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.comment_reply_to_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], null),(function (base,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.follow_boards_list_key(org_slug));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"team-data","team-data",-732020079),new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),new cljs.core.Keyword(null,"team-roster","team-roster",-1945092859),new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),(function (base,team_data,current_user_data,team_roster,invite_users){
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"team-data","team-data",-732020079),team_data,new cljs.core.Keyword(null,"invite-users","invite-users",107417337),invite_users,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),current_user_data,new cljs.core.Keyword(null,"team-roster","team-roster",-1945092859),team_roster], null);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-data","org-data",96720321)], null),(function (org_data){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(org_data,oc.web.dispatcher.can_compose_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(base,oc.web.dispatcher.expo_key);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"base","base",185279322)], null),(function (base){
return new cljs.core.Keyword(null,"activity-share-container","activity-share-container",-1384168706).cljs$core$IFn$_invoke$arity$1(base);
})], null)]);
});
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.dispatcher !== 'undefined') && (typeof oc.web.dispatcher.action !== 'undefined')){
} else {
oc.web.dispatcher.action = (function (){var method_table__4619__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__4620__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__4621__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__4622__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__4623__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__41603 = cljs.core.get_global_hierarchy;
return (fexpr__41603.cljs$core$IFn$_invoke$arity$0 ? fexpr__41603.cljs$core$IFn$_invoke$arity$0() : fexpr__41603.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("oc.web.dispatcher","action"),(function (db,p__41604){
var vec__41605 = p__41604;
var seq__41606 = cljs.core.seq(vec__41605);
var first__41607 = cljs.core.first(seq__41606);
var seq__41606__$1 = cljs.core.next(seq__41606);
var action_type = first__41607;
var _ = seq__41606__$1;
if(((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(action_type,new cljs.core.Keyword(null,"input","input",556931961))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(action_type,new cljs.core.Keyword(null,"update","update",1045576396))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(action_type,new cljs.core.Keyword(null,"entry-toggle-save-on-exit","entry-toggle-save-on-exit",-963399746))))){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.dispatcher",null,685,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Dispatching action:",action_type], null);
}),null)),null,612687876);
} else {
}

return action_type;
}),new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__4623__auto__,method_table__4619__auto__,prefer_table__4620__auto__,method_cache__4621__auto__,cached_hierarchy__4622__auto__));
})();
}
oc.web.dispatcher.actions = cljs_flux.dispatcher.dispatcher();
oc.web.dispatcher.actions_dispatch = oc.web.dispatcher.actions.cljs_flux$dispatcher$IDispatcher$register$arity$2(null,(function (payload){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.app_state,oc.web.dispatcher.action,payload);
}));
oc.web.dispatcher.dispatch_BANG_ = (function oc$web$dispatcher$dispatch_BANG_(payload){
return oc.web.dispatcher.actions.cljs_flux$dispatcher$IDispatcher$dispatch$arity$2(null,payload);
});
oc.web.dispatcher.route = (function oc$web$dispatcher$route(var_args){
var G__41613 = arguments.length;
switch (G__41613) {
case 0:
return oc.web.dispatcher.route.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.route.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.route.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.route.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.route.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key], null));
}));

(oc.web.dispatcher.route.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.current_org_slug = (function oc$web$dispatcher$current_org_slug(var_args){
var G__41617 = arguments.length;
switch (G__41617) {
case 0:
return oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key,new cljs.core.Keyword(null,"org","org",1495985)], null));
}));

(oc.web.dispatcher.current_org_slug.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.current_board_slug = (function oc$web$dispatcher$current_board_slug(var_args){
var G__41622 = arguments.length;
switch (G__41622) {
case 0:
return oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key,new cljs.core.Keyword(null,"board","board",-1907017633)], null));
}));

(oc.web.dispatcher.current_board_slug.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.current_contributions_id = (function oc$web$dispatcher$current_contributions_id(var_args){
var G__41625 = arguments.length;
switch (G__41625) {
case 0:
return oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key,new cljs.core.Keyword(null,"contributions","contributions",-1280485964)], null));
}));

(oc.web.dispatcher.current_contributions_id.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.current_sort_type = (function oc$web$dispatcher$current_sort_type(var_args){
var G__41629 = arguments.length;
switch (G__41629) {
case 0:
return oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key,new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504)], null));
}));

(oc.web.dispatcher.current_sort_type.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.current_activity_id = (function oc$web$dispatcher$current_activity_id(var_args){
var G__41633 = arguments.length;
switch (G__41633) {
case 0:
return oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key,new cljs.core.Keyword(null,"activity","activity",-1179221455)], null));
}));

(oc.web.dispatcher.current_activity_id.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.current_entry_board_slug = (function oc$web$dispatcher$current_entry_board_slug(var_args){
var G__41637 = arguments.length;
switch (G__41637) {
case 0:
return oc.web.dispatcher.current_entry_board_slug.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.current_entry_board_slug.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.current_entry_board_slug.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.current_entry_board_slug.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.current_entry_board_slug.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key,new cljs.core.Keyword(null,"entry-board","entry-board",1825593318)], null));
}));

(oc.web.dispatcher.current_entry_board_slug.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.current_secure_activity_id = (function oc$web$dispatcher$current_secure_activity_id(var_args){
var G__41640 = arguments.length;
switch (G__41640) {
case 0:
return oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key,new cljs.core.Keyword(null,"secure-id","secure-id",-626735882)], null));
}));

(oc.web.dispatcher.current_secure_activity_id.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.current_comment_id = (function oc$web$dispatcher$current_comment_id(var_args){
var G__41645 = arguments.length;
switch (G__41645) {
case 0:
return oc.web.dispatcher.current_comment_id.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.current_comment_id.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.current_comment_id.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.current_comment_id.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.current_comment_id.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key,new cljs.core.Keyword(null,"comment","comment",532206069)], null));
}));

(oc.web.dispatcher.current_comment_id.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.query_params = (function oc$web$dispatcher$query_params(var_args){
var G__41648 = arguments.length;
switch (G__41648) {
case 0:
return oc.web.dispatcher.query_params.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.query_params.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.query_params.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.query_params.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.query_params.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key,new cljs.core.Keyword(null,"query-params","query-params",900640534)], null));
}));

(oc.web.dispatcher.query_params.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.query_param = (function oc$web$dispatcher$query_param(var_args){
var G__41651 = arguments.length;
switch (G__41651) {
case 1:
return oc.web.dispatcher.query_param.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.query_param.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.query_param.cljs$core$IFn$_invoke$arity$1 = (function (k){
return oc.web.dispatcher.query_param.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),k);
}));

(oc.web.dispatcher.query_param.cljs$core$IFn$_invoke$arity$2 = (function (data,k){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key,new cljs.core.Keyword(null,"query-params","query-params",900640534),k], null));
}));

(oc.web.dispatcher.query_param.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.route_param = (function oc$web$dispatcher$route_param(var_args){
var G__41655 = arguments.length;
switch (G__41655) {
case 1:
return oc.web.dispatcher.route_param.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.route_param.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.route_param.cljs$core$IFn$_invoke$arity$1 = (function (k){
return oc.web.dispatcher.route_param.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),k);
}));

(oc.web.dispatcher.route_param.cljs$core$IFn$_invoke$arity$2 = (function (data,k){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key,k], null));
}));

(oc.web.dispatcher.route_param.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.route_set = (function oc$web$dispatcher$route_set(var_args){
var G__41659 = arguments.length;
switch (G__41659) {
case 0:
return oc.web.dispatcher.route_set.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.route_set.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.route_set.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.route_set.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.route_set.cljs$core$IFn$_invoke$arity$1 = (function (data){
return oc.web.dispatcher.route_param.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.Keyword(null,"route","route",329891309));
}));

(oc.web.dispatcher.route_set.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.invite_token = (function oc$web$dispatcher$invite_token(var_args){
var G__41661 = arguments.length;
switch (G__41661) {
case 0:
return oc.web.dispatcher.invite_token.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.invite_token.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.invite_token.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.invite_token.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.invite_token.cljs$core$IFn$_invoke$arity$1 = (function (data){
return oc.web.dispatcher.query_param.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.Keyword(null,"invite-token","invite-token",610560078));
}));

(oc.web.dispatcher.invite_token.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.in_route_QMARK_ = (function oc$web$dispatcher$in_route_QMARK_(var_args){
var G__41663 = arguments.length;
switch (G__41663) {
case 1:
return oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1 = (function (route_name){
return oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.route_set.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state)),route_name);
}));

(oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$2 = (function (routes,route_name){
if(cljs.core.truth_(route_name)){
var G__41665 = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(route_name);
return (routes.cljs$core$IFn$_invoke$arity$1 ? routes.cljs$core$IFn$_invoke$arity$1(G__41665) : routes.call(null,G__41665));
} else {
return null;
}
}));

(oc.web.dispatcher.in_route_QMARK_.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.payments_data = (function oc$web$dispatcher$payments_data(var_args){
var G__41667 = arguments.length;
switch (G__41667) {
case 0:
return oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),org_slug);
}));

(oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$2 = (function (data,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.payments_key(org_slug));
}));

(oc.web.dispatcher.payments_data.cljs$lang$maxFixedArity = 2);

/**
 * 
 */
oc.web.dispatcher.bot_access = (function oc$web$dispatcher$bot_access(var_args){
var G__41670 = arguments.length;
switch (G__41670) {
case 0:
return oc.web.dispatcher.bot_access.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.bot_access.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.bot_access.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.bot_access.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.bot_access.cljs$core$IFn$_invoke$arity$1 = (function (data){
return new cljs.core.Keyword(null,"bot-access","bot-access",1631196357).cljs$core$IFn$_invoke$arity$1(data);
}));

(oc.web.dispatcher.bot_access.cljs$lang$maxFixedArity = 1);

/**
 * 
 */
oc.web.dispatcher.notifications_data = (function oc$web$dispatcher$notifications_data(var_args){
var G__41674 = arguments.length;
switch (G__41674) {
case 0:
return oc.web.dispatcher.notifications_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.notifications_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.notifications_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.notifications_data.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.notifications_data.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.notifications_key);
}));

(oc.web.dispatcher.notifications_data.cljs$lang$maxFixedArity = 1);

/**
 * 
 */
oc.web.dispatcher.teams_data_requested = (function oc$web$dispatcher$teams_data_requested(var_args){
var G__41678 = arguments.length;
switch (G__41678) {
case 0:
return oc.web.dispatcher.teams_data_requested.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.teams_data_requested.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.teams_data_requested.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.teams_data_requested.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.teams_data_requested.cljs$core$IFn$_invoke$arity$1 = (function (data){
return new cljs.core.Keyword(null,"teams-data-requested","teams-data-requested",-1880742826).cljs$core$IFn$_invoke$arity$1(data);
}));

(oc.web.dispatcher.teams_data_requested.cljs$lang$maxFixedArity = 1);

/**
 * Get the Auth settings data
 */
oc.web.dispatcher.auth_settings = (function oc$web$dispatcher$auth_settings(var_args){
var G__41681 = arguments.length;
switch (G__41681) {
case 0:
return oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.auth_settings_key);
}));

(oc.web.dispatcher.auth_settings.cljs$lang$maxFixedArity = 1);

/**
 * Get the API entry point.
 */
oc.web.dispatcher.api_entry_point = (function oc$web$dispatcher$api_entry_point(var_args){
var G__41685 = arguments.length;
switch (G__41685) {
case 0:
return oc.web.dispatcher.api_entry_point.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.api_entry_point.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.api_entry_point.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.api_entry_point.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.api_entry_point.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.api_entry_point_key);
}));

(oc.web.dispatcher.api_entry_point.cljs$lang$maxFixedArity = 1);

/**
 * Get the current logged in user info.
 */
oc.web.dispatcher.current_user_data = (function oc$web$dispatcher$current_user_data(var_args){
var G__41689 = arguments.length;
switch (G__41689) {
case 0:
return oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], null));
}));

(oc.web.dispatcher.current_user_data.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.orgs_data = (function oc$web$dispatcher$orgs_data(var_args){
var G__41692 = arguments.length;
switch (G__41692) {
case 0:
return oc.web.dispatcher.orgs_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.orgs_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.orgs_data', oc.web.dispatcher.orgs_data);

(oc.web.dispatcher.orgs_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.orgs_data.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.orgs_data.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.orgs_key);
}));

(oc.web.dispatcher.orgs_data.cljs$lang$maxFixedArity = 1);

/**
 * Get org data.
 */
oc.web.dispatcher.org_data = (function oc$web$dispatcher$org_data(var_args){
var G__41694 = arguments.length;
switch (G__41694) {
case 0:
return oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.org_data', oc.web.dispatcher.org_data);

(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$1 = (function (data){
return oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2 = (function (data,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.org_data_key(org_slug));
}));

(oc.web.dispatcher.org_data.cljs$lang$maxFixedArity = 2);

/**
 * Get org all posts data.
 */
oc.web.dispatcher.posts_data = (function oc$web$dispatcher$posts_data(var_args){
var G__41696 = arguments.length;
switch (G__41696) {
case 0:
return oc.web.dispatcher.posts_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.posts_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.posts_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.posts_data', oc.web.dispatcher.posts_data);

(oc.web.dispatcher.posts_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.posts_data.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.posts_data.cljs$core$IFn$_invoke$arity$1 = (function (data){
return oc.web.dispatcher.posts_data.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1(data));
}));

(oc.web.dispatcher.posts_data.cljs$core$IFn$_invoke$arity$2 = (function (data,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.posts_data_key(org_slug));
}));

(oc.web.dispatcher.posts_data.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.s_or_k_QMARK_ = (function oc$web$dispatcher$s_or_k_QMARK_(x){
return (((x instanceof cljs.core.Keyword)) || (typeof x === 'string'));
});
/**
 * Get board data from org data map: mostly used to edit the board infos.
 * @param {...*} var_args
 */
oc.web.dispatcher.org_board_data = (function() { 
var oc$web$dispatcher$org_board_data__delegate = function (args__41272__auto__){
var ocr_41702 = cljs.core.vec(args__41272__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41702)) && ((cljs.core.count(ocr_41702) === 0)))){
var G__41729 = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var G__41730 = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
return (oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$2 ? oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$2(G__41729,G__41730) : oc.web.dispatcher.org_board_data.call(null,G__41729,G__41730));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41710){if((e41710 instanceof Error)){
var e__40179__auto__ = e41710;
if((e__40179__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41702)) && ((cljs.core.count(ocr_41702) === 1)))){
try{var ocr_41702_0__41704 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(0));
if(oc.web.dispatcher.s_or_k_QMARK_(ocr_41702_0__41704)){
var board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(0));
var G__41727 = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var G__41728 = board_slug;
return (oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$2 ? oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$2(G__41727,G__41728) : oc.web.dispatcher.org_board_data.call(null,G__41727,G__41728));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41726){if((e41726 instanceof Error)){
var e__40179__auto____$1 = e41726;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$1;
}
} else {
throw e41726;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41711){if((e41711 instanceof Error)){
var e__40179__auto____$1 = e41711;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41702)) && ((cljs.core.count(ocr_41702) === 2)))){
try{var ocr_41702_0__41705 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(0));
if((function (p1__41697_SHARP_){
return (((p1__41697_SHARP_ instanceof cljs.core.Keyword)) || (typeof p1__41697_SHARP_ === 'string'));
})(ocr_41702_0__41705)){
try{var ocr_41702_1__41706 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(1));
if((function (p1__41698_SHARP_){
return (((p1__41698_SHARP_ instanceof cljs.core.Keyword)) || (typeof p1__41698_SHARP_ === 'string'));
})(ocr_41702_1__41706)){
var board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(1));
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(0));
var G__41724 = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),org_slug);
var G__41725 = board_slug;
return (oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$2 ? oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$2(G__41724,G__41725) : oc.web.dispatcher.org_board_data.call(null,G__41724,G__41725));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41723){if((e41723 instanceof Error)){
var e__40179__auto____$2 = e41723;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41723;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41720){if((e41720 instanceof Error)){
var e__40179__auto____$2 = e41720;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
try{var ocr_41702_0__41705 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(0));
if(cljs.core.truth_((function (p1__41699_SHARP_){
if(cljs.core.map_QMARK_(p1__41699_SHARP_)){
var and__4115__auto__ = new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(p1__41699_SHARP_);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(p1__41699_SHARP_);
} else {
return and__4115__auto__;
}
} else {
return false;
}
})(ocr_41702_0__41705))){
try{var ocr_41702_1__41706 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(1));
if((function (p1__41700_SHARP_){
return (((p1__41700_SHARP_ instanceof cljs.core.Keyword)) || (typeof p1__41700_SHARP_ === 'string'));
})(ocr_41702_1__41706)){
var board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(1));
var org_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(0));
var board_slug_kw = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(board_slug);
return cljs.core.some((function (p1__41701_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__41701_SHARP_)),board_slug_kw)){
return p1__41701_SHARP_;
} else {
return null;
}
}),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41722){if((e41722 instanceof Error)){
var e__40179__auto____$3 = e41722;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$3;
}
} else {
throw e41722;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41721){if((e41721 instanceof Error)){
var e__40179__auto____$3 = e41721;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$3;
}
} else {
throw e41721;

}
}} else {
throw e__40179__auto____$2;
}
} else {
throw e41720;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41712){if((e41712 instanceof Error)){
var e__40179__auto____$2 = e41712;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41702)) && ((cljs.core.count(ocr_41702) === 3)))){
try{var ocr_41702_0__41707 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(0));
if(cljs.core.map_QMARK_(ocr_41702_0__41707)){
try{var ocr_41702_2__41709 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(2));
if(cljs.core.keyword_identical_QMARK_(ocr_41702_2__41709,new cljs.core.Keyword(null,"guard","guard",-873147811))){
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(0));
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(1));
var G__41718 = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(data,org_slug);
var G__41719 = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1(data);
return (oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$2 ? oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$2(G__41718,G__41719) : oc.web.dispatcher.org_board_data.call(null,G__41718,G__41719));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41715){if((e41715 instanceof Error)){
var e__40179__auto____$3 = e41715;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
var board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(2));
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(0));
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41702,(1));
var G__41716 = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(data,org_slug);
var G__41717 = board_slug;
return (oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$2 ? oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$2(G__41716,G__41717) : oc.web.dispatcher.org_board_data.call(null,G__41716,G__41717));
} else {
throw e__40179__auto____$3;
}
} else {
throw e41715;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41714){if((e41714 instanceof Error)){
var e__40179__auto____$3 = e41714;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$3;
}
} else {
throw e41714;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41713){if((e41713 instanceof Error)){
var e__40179__auto____$3 = e41713;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41702)].join('')));
} else {
throw e__40179__auto____$3;
}
} else {
throw e41713;

}
}} else {
throw e__40179__auto____$2;
}
} else {
throw e41712;

}
}} else {
throw e__40179__auto____$1;
}
} else {
throw e41711;

}
}} else {
throw e__40179__auto__;
}
} else {
throw e41710;

}
}};
var oc$web$dispatcher$org_board_data = function (var_args){
var args__41272__auto__ = null;
if (arguments.length > 0) {
var G__42305__i = 0, G__42305__a = new Array(arguments.length -  0);
while (G__42305__i < G__42305__a.length) {G__42305__a[G__42305__i] = arguments[G__42305__i + 0]; ++G__42305__i;}
  args__41272__auto__ = new cljs.core.IndexedSeq(G__42305__a,0,null);
} 
return oc$web$dispatcher$org_board_data__delegate.call(this,args__41272__auto__);};
oc$web$dispatcher$org_board_data.cljs$lang$maxFixedArity = 0;
oc$web$dispatcher$org_board_data.cljs$lang$applyTo = (function (arglist__42308){
var args__41272__auto__ = cljs.core.seq(arglist__42308);
return oc$web$dispatcher$org_board_data__delegate(args__41272__auto__);
});
oc$web$dispatcher$org_board_data.cljs$core$IFn$_invoke$arity$variadic = oc$web$dispatcher$org_board_data__delegate;
return oc$web$dispatcher$org_board_data;
})()
;
/**
 * Get board data.
 * @param {...*} var_args
 */
oc.web.dispatcher.board_data = (function() { 
var oc$web$dispatcher$board_data__delegate = function (args__41272__auto__){
var ocr_41735 = cljs.core.vec(args__41272__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41735)) && ((cljs.core.count(ocr_41735) === 0)))){
var G__41765 = cljs.core.deref(oc.web.dispatcher.app_state);
return (oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$1 ? oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$1(G__41765) : oc.web.dispatcher.board_data.call(null,G__41765));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41743){if((e41743 instanceof Error)){
var e__40179__auto__ = e41743;
if((e__40179__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41735)) && ((cljs.core.count(ocr_41735) === 1)))){
try{var ocr_41735_0__41737 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(0));
if(cljs.core.map_QMARK_(ocr_41735_0__41737)){
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(0));
var G__41762 = data;
var G__41763 = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1(data);
var G__41764 = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1(data);
return (oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$3(G__41762,G__41763,G__41764) : oc.web.dispatcher.board_data.call(null,G__41762,G__41763,G__41764));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41757){if((e41757 instanceof Error)){
var e__40179__auto____$1 = e41757;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_41735_0__41737 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(0));
if((function (p1__41731_SHARP_){
return (((p1__41731_SHARP_ instanceof cljs.core.Keyword)) || (typeof p1__41731_SHARP_ === 'string'));
})(ocr_41735_0__41737)){
var board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(0));
var G__41759 = cljs.core.deref(oc.web.dispatcher.app_state);
var G__41760 = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var G__41761 = board_slug;
return (oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$3(G__41759,G__41760,G__41761) : oc.web.dispatcher.board_data.call(null,G__41759,G__41760,G__41761));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41758){if((e41758 instanceof Error)){
var e__40179__auto____$2 = e41758;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41758;

}
}} else {
throw e__40179__auto____$1;
}
} else {
throw e41757;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41744){if((e41744 instanceof Error)){
var e__40179__auto____$1 = e41744;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41735)) && ((cljs.core.count(ocr_41735) === 2)))){
try{var ocr_41735_0__41738 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(0));
if((function (p1__41732_SHARP_){
return (((p1__41732_SHARP_ instanceof cljs.core.Keyword)) || (typeof p1__41732_SHARP_ === 'string'));
})(ocr_41735_0__41738)){
try{var ocr_41735_1__41739 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(1));
if((function (p1__41733_SHARP_){
return (((p1__41733_SHARP_ instanceof cljs.core.Keyword)) || (typeof p1__41733_SHARP_ === 'string'));
})(ocr_41735_1__41739)){
var board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(1));
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(0));
var G__41754 = cljs.core.deref(oc.web.dispatcher.app_state);
var G__41755 = org_slug;
var G__41756 = board_slug;
return (oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$3(G__41754,G__41755,G__41756) : oc.web.dispatcher.board_data.call(null,G__41754,G__41755,G__41756));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41753){if((e41753 instanceof Error)){
var e__40179__auto____$2 = e41753;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41753;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41747){if((e41747 instanceof Error)){
var e__40179__auto____$2 = e41747;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
try{var ocr_41735_0__41738 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(0));
if(cljs.core.map_QMARK_(ocr_41735_0__41738)){
try{var ocr_41735_1__41739 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(1));
if((function (p1__41734_SHARP_){
return (((p1__41734_SHARP_ instanceof cljs.core.Keyword)) || (typeof p1__41734_SHARP_ === 'string'));
})(ocr_41735_1__41739)){
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(1));
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(0));
var G__41750 = cljs.core.deref(oc.web.dispatcher.app_state);
var G__41751 = org_slug;
var G__41752 = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1(data);
return (oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$3(G__41750,G__41751,G__41752) : oc.web.dispatcher.board_data.call(null,G__41750,G__41751,G__41752));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41749){if((e41749 instanceof Error)){
var e__40179__auto____$3 = e41749;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$3;
}
} else {
throw e41749;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41748){if((e41748 instanceof Error)){
var e__40179__auto____$3 = e41748;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$3;
}
} else {
throw e41748;

}
}} else {
throw e__40179__auto____$2;
}
} else {
throw e41747;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41745){if((e41745 instanceof Error)){
var e__40179__auto____$2 = e41745;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41735)) && ((cljs.core.count(ocr_41735) === 3)))){
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(0));
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(1));
var board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41735,(2));
if(cljs.core.truth_((function (){var and__4115__auto__ = org_slug;
if(cljs.core.truth_(and__4115__auto__)){
return board_slug;
} else {
return and__4115__auto__;
}
})())){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,board_slug));
} else {
return null;
}
} else {
throw cljs.core.match.backtrack;

}
}catch (e41746){if((e41746 instanceof Error)){
var e__40179__auto____$3 = e41746;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41735)].join('')));
} else {
throw e__40179__auto____$3;
}
} else {
throw e41746;

}
}} else {
throw e__40179__auto____$2;
}
} else {
throw e41745;

}
}} else {
throw e__40179__auto____$1;
}
} else {
throw e41744;

}
}} else {
throw e__40179__auto__;
}
} else {
throw e41743;

}
}};
var oc$web$dispatcher$board_data = function (var_args){
var args__41272__auto__ = null;
if (arguments.length > 0) {
var G__42332__i = 0, G__42332__a = new Array(arguments.length -  0);
while (G__42332__i < G__42332__a.length) {G__42332__a[G__42332__i] = arguments[G__42332__i + 0]; ++G__42332__i;}
  args__41272__auto__ = new cljs.core.IndexedSeq(G__42332__a,0,null);
} 
return oc$web$dispatcher$board_data__delegate.call(this,args__41272__auto__);};
oc$web$dispatcher$board_data.cljs$lang$maxFixedArity = 0;
oc$web$dispatcher$board_data.cljs$lang$applyTo = (function (arglist__42333){
var args__41272__auto__ = cljs.core.seq(arglist__42333);
return oc$web$dispatcher$board_data__delegate(args__41272__auto__);
});
oc$web$dispatcher$board_data.cljs$core$IFn$_invoke$arity$variadic = oc$web$dispatcher$board_data__delegate;
return oc$web$dispatcher$board_data;
})()
;
/**
 * Get contributions data
 * @param {...*} var_args
 */
oc.web.dispatcher.contributions_data = (function() { 
var oc$web$dispatcher$contributions_data__delegate = function (args__41272__auto__){
var ocr_41770 = cljs.core.vec(args__41272__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41770)) && ((cljs.core.count(ocr_41770) === 0)))){
var G__41802 = cljs.core.deref(oc.web.dispatcher.app_state);
return (oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$1 ? oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$1(G__41802) : oc.web.dispatcher.contributions_data.call(null,G__41802));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41778){if((e41778 instanceof Error)){
var e__40179__auto__ = e41778;
if((e__40179__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41770)) && ((cljs.core.count(ocr_41770) === 1)))){
try{var ocr_41770_0__41772 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(0));
if(cljs.core.map_QMARK_(ocr_41770_0__41772)){
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(0));
var G__41799 = data;
var G__41800 = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1(data);
var G__41801 = oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$1(data);
return (oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$3(G__41799,G__41800,G__41801) : oc.web.dispatcher.contributions_data.call(null,G__41799,G__41800,G__41801));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41794){if((e41794 instanceof Error)){
var e__40179__auto____$1 = e41794;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_41770_0__41772 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(0));
if((function (p1__41766_SHARP_){
return (((p1__41766_SHARP_ instanceof cljs.core.Keyword)) || (typeof p1__41766_SHARP_ === 'string'));
})(ocr_41770_0__41772)){
var contributions_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(0));
var G__41796 = cljs.core.deref(oc.web.dispatcher.app_state);
var G__41797 = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var G__41798 = contributions_id;
return (oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$3(G__41796,G__41797,G__41798) : oc.web.dispatcher.contributions_data.call(null,G__41796,G__41797,G__41798));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41795){if((e41795 instanceof Error)){
var e__40179__auto____$2 = e41795;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41795;

}
}} else {
throw e__40179__auto____$1;
}
} else {
throw e41794;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41779){if((e41779 instanceof Error)){
var e__40179__auto____$1 = e41779;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41770)) && ((cljs.core.count(ocr_41770) === 2)))){
try{var ocr_41770_0__41773 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(0));
if((function (p1__41767_SHARP_){
return (((p1__41767_SHARP_ instanceof cljs.core.Keyword)) || (typeof p1__41767_SHARP_ === 'string'));
})(ocr_41770_0__41773)){
try{var ocr_41770_1__41774 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(1));
if((function (p1__41768_SHARP_){
return (((p1__41768_SHARP_ instanceof cljs.core.Keyword)) || (typeof p1__41768_SHARP_ === 'string'));
})(ocr_41770_1__41774)){
var contributions_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(1));
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(0));
var G__41791 = cljs.core.deref(oc.web.dispatcher.app_state);
var G__41792 = org_slug;
var G__41793 = contributions_id;
return (oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$3(G__41791,G__41792,G__41793) : oc.web.dispatcher.contributions_data.call(null,G__41791,G__41792,G__41793));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41788){if((e41788 instanceof Error)){
var e__40179__auto____$2 = e41788;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41788;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41782){if((e41782 instanceof Error)){
var e__40179__auto____$2 = e41782;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
try{var ocr_41770_0__41773 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(0));
if(cljs.core.map_QMARK_(ocr_41770_0__41773)){
try{var ocr_41770_1__41774 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(1));
if((function (p1__41769_SHARP_){
return (((p1__41769_SHARP_ instanceof cljs.core.Keyword)) || (typeof p1__41769_SHARP_ === 'string'));
})(ocr_41770_1__41774)){
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(1));
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(0));
var G__41785 = cljs.core.deref(oc.web.dispatcher.app_state);
var G__41786 = org_slug;
var G__41787 = oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$1(data);
return (oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$3(G__41785,G__41786,G__41787) : oc.web.dispatcher.contributions_data.call(null,G__41785,G__41786,G__41787));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41784){if((e41784 instanceof Error)){
var e__40179__auto____$3 = e41784;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$3;
}
} else {
throw e41784;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41783){if((e41783 instanceof Error)){
var e__40179__auto____$3 = e41783;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$3;
}
} else {
throw e41783;

}
}} else {
throw e__40179__auto____$2;
}
} else {
throw e41782;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41780){if((e41780 instanceof Error)){
var e__40179__auto____$2 = e41780;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41770)) && ((cljs.core.count(ocr_41770) === 3)))){
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(0));
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(1));
var contributions_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41770,(2));
if(cljs.core.truth_((function (){var and__4115__auto__ = org_slug;
if(cljs.core.truth_(and__4115__auto__)){
return contributions_id;
} else {
return and__4115__auto__;
}
})())){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,contributions_id));
} else {
return null;
}
} else {
throw cljs.core.match.backtrack;

}
}catch (e41781){if((e41781 instanceof Error)){
var e__40179__auto____$3 = e41781;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41770)].join('')));
} else {
throw e__40179__auto____$3;
}
} else {
throw e41781;

}
}} else {
throw e__40179__auto____$2;
}
} else {
throw e41780;

}
}} else {
throw e__40179__auto____$1;
}
} else {
throw e41779;

}
}} else {
throw e__40179__auto__;
}
} else {
throw e41778;

}
}};
var oc$web$dispatcher$contributions_data = function (var_args){
var args__41272__auto__ = null;
if (arguments.length > 0) {
var G__42346__i = 0, G__42346__a = new Array(arguments.length -  0);
while (G__42346__i < G__42346__a.length) {G__42346__a[G__42346__i] = arguments[G__42346__i + 0]; ++G__42346__i;}
  args__41272__auto__ = new cljs.core.IndexedSeq(G__42346__a,0,null);
} 
return oc$web$dispatcher$contributions_data__delegate.call(this,args__41272__auto__);};
oc$web$dispatcher$contributions_data.cljs$lang$maxFixedArity = 0;
oc$web$dispatcher$contributions_data.cljs$lang$applyTo = (function (arglist__42347){
var args__41272__auto__ = cljs.core.seq(arglist__42347);
return oc$web$dispatcher$contributions_data__delegate(args__41272__auto__);
});
oc$web$dispatcher$contributions_data.cljs$core$IFn$_invoke$arity$variadic = oc$web$dispatcher$contributions_data__delegate;
return oc$web$dispatcher$contributions_data;
})()
;
goog.exportSymbol('oc.web.dispatcher.contributions_data', oc.web.dispatcher.contributions_data);
oc.web.dispatcher.editable_boards_data = (function oc$web$dispatcher$editable_boards_data(var_args){
var G__41805 = arguments.length;
switch (G__41805) {
case 0:
return oc.web.dispatcher.editable_boards_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.editable_boards_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.editable_boards_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.editable_boards_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.editable_boards_data.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.dispatcher.editable_boards_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.editable_boards_data.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),org_slug);
}));

(oc.web.dispatcher.editable_boards_data.cljs$core$IFn$_invoke$arity$2 = (function (data,org_slug){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(data,org_slug);
var filtered_boards = cljs.core.filterv((function (board){
return cljs.core.some((function (p1__41803_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"rel","rel",1378823488).cljs$core$IFn$_invoke$arity$1(p1__41803_SHARP_),"create")){
return p1__41803_SHARP_;
} else {
return null;
}
}),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(board));
}),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data));
return cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850),filtered_boards),filtered_boards);
}));

(oc.web.dispatcher.editable_boards_data.cljs$lang$maxFixedArity = 2);

/**
 * Get container data.
 */
oc.web.dispatcher.container_data = (function oc$web$dispatcher$container_data(var_args){
var G__41807 = arguments.length;
switch (G__41807) {
case 0:
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.container_data', oc.web.dispatcher.container_data);

(oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$1 = (function (data){
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(data,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1(data),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1(data),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$1(data));
}));

(oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$2 = (function (data,org_slug){
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(data,org_slug,oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1(data),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$1(data));
}));

(oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$3 = (function (data,org_slug,board_slug){
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(data,org_slug,board_slug,oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$1(data));
}));

(oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4 = (function (data,org_slug,board_slug,sort_type){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,board_slug,sort_type));
}));

(oc.web.dispatcher.container_data.cljs$lang$maxFixedArity = 4);

/**
 * Get all-posts container data.
 */
oc.web.dispatcher.all_posts_data = (function oc$web$dispatcher$all_posts_data(var_args){
var G__41809 = arguments.length;
switch (G__41809) {
case 0:
return oc.web.dispatcher.all_posts_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.all_posts_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.all_posts_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.all_posts_data.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.all_posts_data', oc.web.dispatcher.all_posts_data);

(oc.web.dispatcher.all_posts_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.all_posts_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.all_posts_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.all_posts_data.cljs$core$IFn$_invoke$arity$3(org_slug,oc.web.dispatcher.recently_posted_sort,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.all_posts_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return oc.web.dispatcher.all_posts_data.cljs$core$IFn$_invoke$arity$3(org_slug,oc.web.dispatcher.recently_posted_sort,data);
}));

(oc.web.dispatcher.all_posts_data.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,sort_type,data){
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(data,org_slug,new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533),sort_type);
}));

(oc.web.dispatcher.all_posts_data.cljs$lang$maxFixedArity = 3);

/**
 * Get replies container data.
 */
oc.web.dispatcher.replies_data = (function oc$web$dispatcher$replies_data(var_args){
var G__41811 = arguments.length;
switch (G__41811) {
case 0:
return oc.web.dispatcher.replies_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.replies_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.replies_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.replies_data', oc.web.dispatcher.replies_data);

(oc.web.dispatcher.replies_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.replies_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.replies_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.replies_data.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.replies_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(data,org_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974),oc.web.dispatcher.recent_activity_sort);
}));

(oc.web.dispatcher.replies_data.cljs$lang$maxFixedArity = 2);

/**
 * Get following container data.
 */
oc.web.dispatcher.following_data = (function oc$web$dispatcher$following_data(var_args){
var G__41813 = arguments.length;
switch (G__41813) {
case 0:
return oc.web.dispatcher.following_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.following_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.following_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.following_data', oc.web.dispatcher.following_data);

(oc.web.dispatcher.following_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.following_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.following_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.following_data.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.following_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(data,org_slug,new cljs.core.Keyword(null,"following","following",-2049193617),oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.dispatcher.following_data.cljs$lang$maxFixedArity = 2);

/**
 * Get following container data.
 */
oc.web.dispatcher.unfollowing_data = (function oc$web$dispatcher$unfollowing_data(var_args){
var G__41815 = arguments.length;
switch (G__41815) {
case 0:
return oc.web.dispatcher.unfollowing_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.unfollowing_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.unfollowing_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.unfollowing_data', oc.web.dispatcher.unfollowing_data);

(oc.web.dispatcher.unfollowing_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.unfollowing_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.unfollowing_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.unfollowing_data.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.unfollowing_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(data,org_slug,new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830),oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.dispatcher.unfollowing_data.cljs$lang$maxFixedArity = 2);

/**
 * Get following container data.
 */
oc.web.dispatcher.bookmarks_data = (function oc$web$dispatcher$bookmarks_data(var_args){
var G__41817 = arguments.length;
switch (G__41817) {
case 0:
return oc.web.dispatcher.bookmarks_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.bookmarks_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.bookmarks_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.bookmarks_data', oc.web.dispatcher.bookmarks_data);

(oc.web.dispatcher.bookmarks_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.bookmarks_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.bookmarks_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.bookmarks_data.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.bookmarks_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(data,org_slug,new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283),oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.dispatcher.bookmarks_data.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.filtered_posts_data = (function oc$web$dispatcher$filtered_posts_data(var_args){
var G__41819 = arguments.length;
switch (G__41819) {
case 0:
return oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.filtered_posts_data', oc.web.dispatcher.filtered_posts_data);

(oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$4(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$1 = (function (data){
return oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$4(data,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1(data),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1(data),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$1(data));
}));

(oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$2 = (function (data,org_slug){
return oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$4(data,org_slug,oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1(data),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$1(data));
}));

(oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$3 = (function (data,org_slug,board_slug){
return oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$4(data,org_slug,board_slug,oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$1(data));
}));

(oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$4 = (function (data,org_slug,board_slug,sort_type){
var posts_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.posts_data_key(org_slug));
return oc.web.dispatcher.get_container_posts(data,posts_data,org_slug,board_slug,sort_type,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
}));

(oc.web.dispatcher.filtered_posts_data.cljs$lang$maxFixedArity = 4);

oc.web.dispatcher.items_to_render_data = (function oc$web$dispatcher$items_to_render_data(var_args){
var G__41821 = arguments.length;
switch (G__41821) {
case 0:
return oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.items_to_render_data', oc.web.dispatcher.items_to_render_data);

(oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$1 = (function (data){
return oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$4(data,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1(data),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1(data),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$1(data));
}));

(oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$2 = (function (data,org_slug){
return oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$4(data,org_slug,oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1(data),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$1(data));
}));

(oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$3 = (function (data,org_slug,board_slug){
return oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$4(data,org_slug,oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$1(data),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$1(data));
}));

(oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$4 = (function (data,org_slug,board_slug,sort_type){
var posts_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.posts_data_key(org_slug));
return oc.web.dispatcher.get_container_posts(data,posts_data,org_slug,board_slug,sort_type,new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762));
}));

(oc.web.dispatcher.items_to_render_data.cljs$lang$maxFixedArity = 4);

oc.web.dispatcher.draft_posts_data = (function oc$web$dispatcher$draft_posts_data(var_args){
var G__41823 = arguments.length;
switch (G__41823) {
case 0:
return oc.web.dispatcher.draft_posts_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.draft_posts_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.draft_posts_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.draft_posts_data', oc.web.dispatcher.draft_posts_data);

(oc.web.dispatcher.draft_posts_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.draft_posts_data.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.dispatcher.draft_posts_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.draft_posts_data.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),org_slug);
}));

(oc.web.dispatcher.draft_posts_data.cljs$core$IFn$_invoke$arity$2 = (function (data,org_slug){
return oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$3(data,org_slug,oc.web.utils.drafts.default_drafts_board_slug);
}));

(oc.web.dispatcher.draft_posts_data.cljs$lang$maxFixedArity = 2);

/**
 * Get activity data.
 */
oc.web.dispatcher.activity_data = (function oc$web$dispatcher$activity_data(var_args){
var G__41825 = arguments.length;
switch (G__41825) {
case 0:
return oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.activity_data', oc.web.dispatcher.activity_data);

(oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1 = (function (activity_id){
return oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_id,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,activity_id){
return oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3(org_slug,activity_id,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,activity_id,data){
var activity_key = oc.web.dispatcher.activity_key(org_slug,activity_id);
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,activity_key);
}));

(oc.web.dispatcher.activity_data.cljs$lang$maxFixedArity = 3);

oc.web.dispatcher.activity_data_get = oc.web.dispatcher.activity_data;
oc.web.dispatcher.entry_data = oc.web.dispatcher.activity_data;
/**
 * Get secure activity data.
 */
oc.web.dispatcher.secure_activity_data = (function oc$web$dispatcher$secure_activity_data(var_args){
var G__41827 = arguments.length;
switch (G__41827) {
case 0:
return oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.secure_activity_data', oc.web.dispatcher.secure_activity_data);

(oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$1 = (function (secure_id){
return oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),secure_id,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,secure_id){
return oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$3(org_slug,secure_id,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,secure_id,data){
var activity_key = oc.web.dispatcher.secure_activity_key(org_slug,secure_id);
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,activity_key);
}));

(oc.web.dispatcher.secure_activity_data.cljs$lang$maxFixedArity = 3);

oc.web.dispatcher.comments_data = (function oc$web$dispatcher$comments_data(var_args){
var G__41829 = arguments.length;
switch (G__41829) {
case 0:
return oc.web.dispatcher.comments_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.comments_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.comments_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.comments_data', oc.web.dispatcher.comments_data);

(oc.web.dispatcher.comments_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.comments_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.comments_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.comments_data.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.comments_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.comments_key(org_slug));
}));

(oc.web.dispatcher.comments_data.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.comment_data = (function oc$web$dispatcher$comment_data(var_args){
var G__41832 = arguments.length;
switch (G__41832) {
case 1:
return oc.web.dispatcher.comment_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.comment_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.comment_data.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.comment_data', oc.web.dispatcher.comment_data);

(oc.web.dispatcher.comment_data.cljs$core$IFn$_invoke$arity$1 = (function (comment_uuid){
return oc.web.dispatcher.comment_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),comment_uuid,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.comment_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,comment_uuid){
return oc.web.dispatcher.comment_data.cljs$core$IFn$_invoke$arity$3(org_slug,comment_uuid,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.comment_data.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,comment_uuid,data){
var all_entry_comments = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.comments_key(org_slug));
var all_comments = cljs.core.flatten(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sorted-comments","sorted-comments",1988882718),cljs.core.vals(all_entry_comments)));
return cljs.core.some((function (p1__41830_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__41830_SHARP_),comment_uuid)){
return p1__41830_SHARP_;
} else {
return null;
}
}),all_comments);
}));

(oc.web.dispatcher.comment_data.cljs$lang$maxFixedArity = 3);

oc.web.dispatcher.activity_comments_data = (function oc$web$dispatcher$activity_comments_data(var_args){
var G__41834 = arguments.length;
switch (G__41834) {
case 0:
return oc.web.dispatcher.activity_comments_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.activity_comments_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.activity_comments_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.activity_comments_data.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.activity_comments_data', oc.web.dispatcher.activity_comments_data);

(oc.web.dispatcher.activity_comments_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.activity_comments_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.activity_comments_data.cljs$core$IFn$_invoke$arity$1 = (function (activity_uuid){
return oc.web.dispatcher.activity_comments_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_uuid,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.activity_comments_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,activity_uuid){
return oc.web.dispatcher.activity_comments_data.cljs$core$IFn$_invoke$arity$3(org_slug,activity_uuid,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.activity_comments_data.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,activity_uuid,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.activity_comments_key(org_slug,activity_uuid));
}));

(oc.web.dispatcher.activity_comments_data.cljs$lang$maxFixedArity = 3);

oc.web.dispatcher.activity_sorted_comments_data = (function oc$web$dispatcher$activity_sorted_comments_data(var_args){
var G__41836 = arguments.length;
switch (G__41836) {
case 0:
return oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.activity_sorted_comments_data', oc.web.dispatcher.activity_sorted_comments_data);

(oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$1 = (function (activity_uuid){
return oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_uuid,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,activity_uuid){
return oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$3(org_slug,activity_uuid,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,activity_uuid,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.activity_sorted_comments_key(org_slug,activity_uuid));
}));

(oc.web.dispatcher.activity_sorted_comments_data.cljs$lang$maxFixedArity = 3);

oc.web.dispatcher.teams_data = (function oc$web$dispatcher$teams_data(var_args){
var G__41838 = arguments.length;
switch (G__41838) {
case 0:
return oc.web.dispatcher.teams_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.teams_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.teams_data', oc.web.dispatcher.teams_data);

(oc.web.dispatcher.teams_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.teams_data.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.teams_data.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.teams_data_key);
}));

(oc.web.dispatcher.teams_data.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.team_data = (function oc$web$dispatcher$team_data(var_args){
var G__41840 = arguments.length;
switch (G__41840) {
case 0:
return oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()));
}));

(oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$1 = (function (team_id){
return oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$2(team_id,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$2 = (function (team_id,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.team_data_key(team_id));
}));

(oc.web.dispatcher.team_data.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.team_roster = (function oc$web$dispatcher$team_roster(var_args){
var G__41842 = arguments.length;
switch (G__41842) {
case 0:
return oc.web.dispatcher.team_roster.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.team_roster.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.team_roster.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.team_roster.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.team_roster.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()));
}));

(oc.web.dispatcher.team_roster.cljs$core$IFn$_invoke$arity$1 = (function (team_id){
return oc.web.dispatcher.team_roster.cljs$core$IFn$_invoke$arity$2(team_id,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.team_roster.cljs$core$IFn$_invoke$arity$2 = (function (team_id,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.team_roster_key(team_id));
}));

(oc.web.dispatcher.team_roster.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.team_channels = (function oc$web$dispatcher$team_channels(var_args){
var G__41844 = arguments.length;
switch (G__41844) {
case 0:
return oc.web.dispatcher.team_channels.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.team_channels.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.team_channels.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.team_channels.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.team_channels.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()));
}));

(oc.web.dispatcher.team_channels.cljs$core$IFn$_invoke$arity$1 = (function (team_id){
return oc.web.dispatcher.team_channels.cljs$core$IFn$_invoke$arity$2(team_id,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.team_channels.cljs$core$IFn$_invoke$arity$2 = (function (team_id,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.team_channels_key(team_id));
}));

(oc.web.dispatcher.team_channels.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.active_users = (function oc$web$dispatcher$active_users(var_args){
var G__41846 = arguments.length;
switch (G__41846) {
case 0:
return oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.active_users', oc.web.dispatcher.active_users);

(oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.active_users_key(org_slug));
}));

(oc.web.dispatcher.active_users.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.follow_list = (function oc$web$dispatcher$follow_list(var_args){
var G__41848 = arguments.length;
switch (G__41848) {
case 0:
return oc.web.dispatcher.follow_list.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.follow_list.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.follow_list.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.follow_list', oc.web.dispatcher.follow_list);

(oc.web.dispatcher.follow_list.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.follow_list.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.follow_list.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.follow_list.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.follow_list.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.follow_list_key(org_slug));
}));

(oc.web.dispatcher.follow_list.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.followers_count = (function oc$web$dispatcher$followers_count(var_args){
var G__41850 = arguments.length;
switch (G__41850) {
case 0:
return oc.web.dispatcher.followers_count.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.followers_count.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.followers_count.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.followers_count', oc.web.dispatcher.followers_count);

(oc.web.dispatcher.followers_count.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.followers_count.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.followers_count.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.followers_count.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.followers_count.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.followers_count_key(org_slug));
}));

(oc.web.dispatcher.followers_count.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.followers_publishers_count = (function oc$web$dispatcher$followers_publishers_count(var_args){
var G__41852 = arguments.length;
switch (G__41852) {
case 0:
return oc.web.dispatcher.followers_publishers_count.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.followers_publishers_count.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.followers_publishers_count.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.followers_publishers_count', oc.web.dispatcher.followers_publishers_count);

(oc.web.dispatcher.followers_publishers_count.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.followers_publishers_count.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.followers_publishers_count.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.followers_publishers_count.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.followers_publishers_count.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.followers_publishers_count_key(org_slug));
}));

(oc.web.dispatcher.followers_publishers_count.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.followers_boards_count = (function oc$web$dispatcher$followers_boards_count(var_args){
var G__41854 = arguments.length;
switch (G__41854) {
case 0:
return oc.web.dispatcher.followers_boards_count.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.followers_boards_count.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.followers_boards_count.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.followers_boards_count', oc.web.dispatcher.followers_boards_count);

(oc.web.dispatcher.followers_boards_count.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.followers_boards_count.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.followers_boards_count.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.followers_boards_count.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.followers_boards_count.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.followers_boards_count_key(org_slug));
}));

(oc.web.dispatcher.followers_boards_count.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.follow_publishers_list = (function oc$web$dispatcher$follow_publishers_list(var_args){
var G__41856 = arguments.length;
switch (G__41856) {
case 0:
return oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.follow_publishers_list', oc.web.dispatcher.follow_publishers_list);

(oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.follow_publishers_list_key(org_slug));
}));

(oc.web.dispatcher.follow_publishers_list.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.follow_boards_list = (function oc$web$dispatcher$follow_boards_list(var_args){
var G__41858 = arguments.length;
switch (G__41858) {
case 0:
return oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.follow_boards_list', oc.web.dispatcher.follow_boards_list);

(oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.follow_boards_list_key(org_slug));
}));

(oc.web.dispatcher.follow_boards_list.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.unfollow_board_uuids = (function oc$web$dispatcher$unfollow_board_uuids(var_args){
var G__41860 = arguments.length;
switch (G__41860) {
case 0:
return oc.web.dispatcher.unfollow_board_uuids.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.unfollow_board_uuids.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.unfollow_board_uuids.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.unfollow_board_uuids', oc.web.dispatcher.unfollow_board_uuids);

(oc.web.dispatcher.unfollow_board_uuids.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.unfollow_board_uuids.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.unfollow_board_uuids.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.unfollow_board_uuids.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.unfollow_board_uuids.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.unfollow_board_uuids_key(org_slug));
}));

(oc.web.dispatcher.unfollow_board_uuids.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.uploading_video_data = (function oc$web$dispatcher$uploading_video_data(var_args){
var G__41862 = arguments.length;
switch (G__41862) {
case 1:
return oc.web.dispatcher.uploading_video_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.uploading_video_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.dispatcher.uploading_video_data.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.uploading_video_data.cljs$core$IFn$_invoke$arity$1 = (function (video_id){
return oc.web.dispatcher.uploading_video_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),video_id,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.uploading_video_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,video_id){
return oc.web.dispatcher.uploading_video_data.cljs$core$IFn$_invoke$arity$3(org_slug,video_id,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.uploading_video_data.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,video_id,data){
var uv_key = oc.web.dispatcher.uploading_video_key(org_slug,video_id);
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,uv_key);
}));

(oc.web.dispatcher.uploading_video_data.cljs$lang$maxFixedArity = 3);

/**
 * Get user notifications data
 */
oc.web.dispatcher.user_notifications_data = (function oc$web$dispatcher$user_notifications_data(var_args){
var G__41864 = arguments.length;
switch (G__41864) {
case 0:
return oc.web.dispatcher.user_notifications_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.user_notifications_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.user_notifications_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.user_notifications_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.user_notifications_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.user_notifications_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.user_notifications_data.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.user_notifications_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.user_notifications_key(org_slug));
}));

(oc.web.dispatcher.user_notifications_data.cljs$lang$maxFixedArity = 2);

/**
 * Get change data.
 */
oc.web.dispatcher.change_data = (function oc$web$dispatcher$change_data(var_args){
var G__41866 = arguments.length;
switch (G__41866) {
case 0:
return oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1 = (function (data){
return oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2 = (function (data,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.change_data_key(org_slug));
}));

(oc.web.dispatcher.change_data.cljs$lang$maxFixedArity = 2);

/**
 * Get the read counts of all the items.
 * @param {...*} var_args
 */
oc.web.dispatcher.activity_read_data = (function() { 
var oc$web$dispatcher$activity_read_data__delegate = function (args__41272__auto__){
var ocr_41867 = cljs.core.vec(args__41272__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41867)) && ((cljs.core.count(ocr_41867) === 0)))){
var G__41891 = cljs.core.deref(oc.web.dispatcher.app_state);
return (oc.web.dispatcher.activity_read_data.cljs$core$IFn$_invoke$arity$1 ? oc.web.dispatcher.activity_read_data.cljs$core$IFn$_invoke$arity$1(G__41891) : oc.web.dispatcher.activity_read_data.call(null,G__41891));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41875){if((e41875 instanceof Error)){
var e__40179__auto__ = e41875;
if((e__40179__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41867)) && ((cljs.core.count(ocr_41867) === 1)))){
try{var ocr_41867_0__41869 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(0));
if(cljs.core.map_QMARK_(ocr_41867_0__41869)){
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(0));
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.activities_read_key);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41887){if((e41887 instanceof Error)){
var e__40179__auto____$1 = e41887;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_41867_0__41869 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(0));
if(cljs.core.seq_QMARK_(ocr_41867_0__41869)){
var item_ids = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(0));
var G__41889 = item_ids;
var G__41890 = cljs.core.deref(oc.web.dispatcher.app_state);
return (oc.web.dispatcher.activity_read_data.cljs$core$IFn$_invoke$arity$2 ? oc.web.dispatcher.activity_read_data.cljs$core$IFn$_invoke$arity$2(G__41889,G__41890) : oc.web.dispatcher.activity_read_data.call(null,G__41889,G__41890));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41888){if((e41888 instanceof Error)){
var e__40179__auto____$2 = e41888;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41888;

}
}} else {
throw e__40179__auto____$1;
}
} else {
throw e41887;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41876){if((e41876 instanceof Error)){
var e__40179__auto____$1 = e41876;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41867)) && ((cljs.core.count(ocr_41867) === 2)))){
try{var ocr_41867_0__41870 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(0));
if(cljs.core.seq_QMARK_(ocr_41867_0__41870)){
try{var ocr_41867_1__41871 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(1));
if(cljs.core.map_QMARK_(ocr_41867_1__41871)){
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(1));
var item_ids = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(0));
var all_activities_read = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.activities_read_key);
return cljs.core.select_keys(all_activities_read,item_ids);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41886){if((e41886 instanceof Error)){
var e__40179__auto____$2 = e41886;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41886;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41885){if((e41885 instanceof Error)){
var e__40179__auto____$2 = e41885;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41885;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41877){if((e41877 instanceof Error)){
var e__40179__auto____$2 = e41877;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41867)) && ((cljs.core.count(ocr_41867) === 1)))){
try{var ocr_41867_0__41872 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(0));
if(typeof ocr_41867_0__41872 === 'string'){
var item_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(0));
var G__41883 = item_id;
var G__41884 = cljs.core.deref(oc.web.dispatcher.app_state);
return (oc.web.dispatcher.activity_read_data.cljs$core$IFn$_invoke$arity$2 ? oc.web.dispatcher.activity_read_data.cljs$core$IFn$_invoke$arity$2(G__41883,G__41884) : oc.web.dispatcher.activity_read_data.call(null,G__41883,G__41884));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41882){if((e41882 instanceof Error)){
var e__40179__auto____$3 = e41882;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$3;
}
} else {
throw e41882;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41878){if((e41878 instanceof Error)){
var e__40179__auto____$3 = e41878;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41867)) && ((cljs.core.count(ocr_41867) === 2)))){
try{var ocr_41867_0__41873 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(0));
if(typeof ocr_41867_0__41873 === 'string'){
try{var ocr_41867_1__41874 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(1));
if(cljs.core.map_QMARK_(ocr_41867_1__41874)){
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(1));
var item_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41867,(0));
var all_activities_read = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.activities_read_key);
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(all_activities_read,item_id);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41881){if((e41881 instanceof Error)){
var e__40179__auto____$4 = e41881;
if((e__40179__auto____$4 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$4;
}
} else {
throw e41881;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41880){if((e41880 instanceof Error)){
var e__40179__auto____$4 = e41880;
if((e__40179__auto____$4 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$4;
}
} else {
throw e41880;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41879){if((e41879 instanceof Error)){
var e__40179__auto____$4 = e41879;
if((e__40179__auto____$4 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41867)].join('')));
} else {
throw e__40179__auto____$4;
}
} else {
throw e41879;

}
}} else {
throw e__40179__auto____$3;
}
} else {
throw e41878;

}
}} else {
throw e__40179__auto____$2;
}
} else {
throw e41877;

}
}} else {
throw e__40179__auto____$1;
}
} else {
throw e41876;

}
}} else {
throw e__40179__auto__;
}
} else {
throw e41875;

}
}};
var oc$web$dispatcher$activity_read_data = function (var_args){
var args__41272__auto__ = null;
if (arguments.length > 0) {
var G__42485__i = 0, G__42485__a = new Array(arguments.length -  0);
while (G__42485__i < G__42485__a.length) {G__42485__a[G__42485__i] = arguments[G__42485__i + 0]; ++G__42485__i;}
  args__41272__auto__ = new cljs.core.IndexedSeq(G__42485__a,0,null);
} 
return oc$web$dispatcher$activity_read_data__delegate.call(this,args__41272__auto__);};
oc$web$dispatcher$activity_read_data.cljs$lang$maxFixedArity = 0;
oc$web$dispatcher$activity_read_data.cljs$lang$applyTo = (function (arglist__42486){
var args__41272__auto__ = cljs.core.seq(arglist__42486);
return oc$web$dispatcher$activity_read_data__delegate(args__41272__auto__);
});
oc$web$dispatcher$activity_read_data.cljs$core$IFn$_invoke$arity$variadic = oc$web$dispatcher$activity_read_data__delegate;
return oc$web$dispatcher$activity_read_data;
})()
;
oc.web.dispatcher.org_seens_data = (function oc$web$dispatcher$org_seens_data(var_args){
var G__41893 = arguments.length;
switch (G__41893) {
case 0:
return oc.web.dispatcher.org_seens_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.org_seens_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.org_seens_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.org_seens_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.org_seens_data.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.dispatcher.org_seens_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.org_seens_data.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.dispatcher.org_seens_data.cljs$core$IFn$_invoke$arity$2 = (function (data,org_slug){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.org_seens_key(org_slug));
}));

(oc.web.dispatcher.org_seens_data.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.cmail_data = (function oc$web$dispatcher$cmail_data(var_args){
var G__41895 = arguments.length;
switch (G__41895) {
case 0:
return oc.web.dispatcher.cmail_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.cmail_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.cmail_data', oc.web.dispatcher.cmail_data);

(oc.web.dispatcher.cmail_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.cmail_data.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.cmail_data.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.cmail_data_key);
}));

(oc.web.dispatcher.cmail_data.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.cmail_state = (function oc$web$dispatcher$cmail_state(var_args){
var G__41897 = arguments.length;
switch (G__41897) {
case 0:
return oc.web.dispatcher.cmail_state.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.cmail_state.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.dispatcher.cmail_state', oc.web.dispatcher.cmail_state);

(oc.web.dispatcher.cmail_state.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.cmail_state.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.cmail_state.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.cmail_state_key);
}));

(oc.web.dispatcher.cmail_state.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.reminders_data = (function oc$web$dispatcher$reminders_data(var_args){
var G__41899 = arguments.length;
switch (G__41899) {
case 0:
return oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.reminders_data_key(org_slug));
}));

(oc.web.dispatcher.reminders_data.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.reminders_roster_data = (function oc$web$dispatcher$reminders_roster_data(var_args){
var G__41901 = arguments.length;
switch (G__41901) {
case 0:
return oc.web.dispatcher.reminders_roster_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.reminders_roster_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.reminders_roster_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.reminders_roster_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.reminders_roster_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.reminders_roster_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.reminders_roster_data.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.reminders_roster_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.reminders_roster_key(org_slug));
}));

(oc.web.dispatcher.reminders_roster_data.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.reminder_edit_data = (function oc$web$dispatcher$reminder_edit_data(var_args){
var G__41903 = arguments.length;
switch (G__41903) {
case 0:
return oc.web.dispatcher.reminder_edit_data.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.reminder_edit_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.dispatcher.reminder_edit_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.reminder_edit_data.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.reminder_edit_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.reminder_edit_data.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return oc.web.dispatcher.reminder_edit_data.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.reminder_edit_data.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.reminder_edit_key(org_slug));
}));

(oc.web.dispatcher.reminder_edit_data.cljs$lang$maxFixedArity = 2);

oc.web.dispatcher.expo_deep_link_origin = (function oc$web$dispatcher$expo_deep_link_origin(var_args){
var G__41905 = arguments.length;
switch (G__41905) {
case 0:
return oc.web.dispatcher.expo_deep_link_origin.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.expo_deep_link_origin.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.expo_deep_link_origin.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.expo_deep_link_origin.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.expo_deep_link_origin.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.expo_deep_link_origin_key);
}));

(oc.web.dispatcher.expo_deep_link_origin.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.expo_app_version = (function oc$web$dispatcher$expo_app_version(var_args){
var G__41907 = arguments.length;
switch (G__41907) {
case 0:
return oc.web.dispatcher.expo_app_version.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.expo_app_version.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.expo_app_version.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.expo_app_version.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.expo_app_version.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.expo_app_version_key);
}));

(oc.web.dispatcher.expo_app_version.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.expo_push_token = (function oc$web$dispatcher$expo_push_token(var_args){
var G__41909 = arguments.length;
switch (G__41909) {
case 0:
return oc.web.dispatcher.expo_push_token.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.dispatcher.expo_push_token.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.dispatcher.expo_push_token.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.dispatcher.expo_push_token.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
}));

(oc.web.dispatcher.expo_push_token.cljs$core$IFn$_invoke$arity$1 = (function (data){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(data,oc.web.dispatcher.expo_push_token_key);
}));

(oc.web.dispatcher.expo_push_token.cljs$lang$maxFixedArity = 1);

oc.web.dispatcher.print_app_state = (function oc$web$dispatcher$print_app_state(){
return cljs.core.deref(oc.web.dispatcher.app_state);
});
oc.web.dispatcher.print_org_data = (function oc$web$dispatcher$print_org_data(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.org_data_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()));
});
oc.web.dispatcher.print_team_data = (function oc$web$dispatcher$print_team_data(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.team_data_key(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0())));
});
oc.web.dispatcher.print_team_roster = (function oc$web$dispatcher$print_team_roster(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.team_roster_key(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0())));
});
oc.web.dispatcher.print_change_data = (function oc$web$dispatcher$print_change_data(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.change_data_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()));
});
oc.web.dispatcher.print_activity_read_data = (function oc$web$dispatcher$print_activity_read_data(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.activities_read_key);
});
oc.web.dispatcher.print_board_data = (function oc$web$dispatcher$print_board_data(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0()));
});
oc.web.dispatcher.print_container_data = (function oc$web$dispatcher$print_container_data(){
if(cljs.core.truth_(oc.web.dispatcher.is_container_QMARK_(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0()))){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0()));
} else {
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0()));
}
});
oc.web.dispatcher.print_activity_data = (function oc$web$dispatcher$print_activity_data(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.activity_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0()));
});
oc.web.dispatcher.print_secure_activity_data = (function oc$web$dispatcher$print_secure_activity_data(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.secure_activity_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0()));
});
oc.web.dispatcher.print_reactions_data = (function oc$web$dispatcher$print_reactions_data(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.activity_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0()),new cljs.core.Keyword(null,"reactions","reactions",2029850654)));
});
oc.web.dispatcher.print_comments_data = (function oc$web$dispatcher$print_comments_data(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.comments_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()));
});
oc.web.dispatcher.print_activity_comments_data = (function oc$web$dispatcher$print_activity_comments_data(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.activity_comments_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0()));
});
oc.web.dispatcher.print_entry_editing_data = (function oc$web$dispatcher$print_entry_editing_data(){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),new cljs.core.Keyword(null,"entry-editing","entry-editing",-1938994964));
});
oc.web.dispatcher.print_posts_data = (function oc$web$dispatcher$print_posts_data(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.posts_data_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()));
});
oc.web.dispatcher.print_filtered_posts = (function oc$web$dispatcher$print_filtered_posts(){
return oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$3(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0());
});
oc.web.dispatcher.print_items_to_render = (function oc$web$dispatcher$print_items_to_render(){
return oc.web.dispatcher.items_to_render_data.cljs$core$IFn$_invoke$arity$3(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0());
});
oc.web.dispatcher.print_user_notifications = (function oc$web$dispatcher$print_user_notifications(){
return oc.web.dispatcher.user_notifications_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
});
oc.web.dispatcher.print_reminders_data = (function oc$web$dispatcher$print_reminders_data(){
return oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
});
oc.web.dispatcher.print_reminder_edit_data = (function oc$web$dispatcher$print_reminder_edit_data(){
return oc.web.dispatcher.reminder_edit_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.deref(oc.web.dispatcher.app_state));
});
oc.web.dispatcher.print_panel_stack = (function oc$web$dispatcher$print_panel_stack(){
return new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
});
oc.web.dispatcher.print_payments_data = (function oc$web$dispatcher$print_payments_data(){
return oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
});
oc.web.dispatcher.print_router_path = (function oc$web$dispatcher$print_router_path(){
return oc.web.dispatcher.route.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
});
(window.OCWebPrintAppState = oc.web.dispatcher.print_app_state);
(window.OCWebPrintOrgData = oc.web.dispatcher.print_org_data);
(window.OCWebPrintTeamData = oc.web.dispatcher.print_team_data);
(window.OCWebPrintTeamRoster = oc.web.dispatcher.print_team_roster);
(window.OCWebPrintActiveUsers = oc.web.dispatcher.active_users);
(window.OCWebPrintChangeData = oc.web.dispatcher.print_change_data);
(window.OCWebPrintActivityReadData = oc.web.dispatcher.print_activity_read_data);
(window.OCWebPrintBoardData = oc.web.dispatcher.print_board_data);
(window.OCWebPrintContainerData = oc.web.dispatcher.print_container_data);
(window.OCWebPrintActivityData = oc.web.dispatcher.print_activity_data);
(window.OCWebPrintSecureActivityData = oc.web.dispatcher.print_secure_activity_data);
(window.OCWebPrintReactionsData = oc.web.dispatcher.print_reactions_data);
(window.OCWebPrintCommentsData = oc.web.dispatcher.print_comments_data);
(window.OCWebPrintActivityCommentsData = oc.web.dispatcher.print_activity_comments_data);
(window.OCWebPrintEntryEditingData = oc.web.dispatcher.print_entry_editing_data);
(window.OCWebPrintFilteredPostsData = oc.web.dispatcher.print_filtered_posts);
(window.OCWebPrintItemsToRender = oc.web.dispatcher.print_items_to_render);
(window.OCWebPrintPostsData = oc.web.dispatcher.print_posts_data);
(window.OCWebPrintUserNotifications = oc.web.dispatcher.print_user_notifications);
(window.OCWebPrintRemindersData = oc.web.dispatcher.print_reminders_data);
(window.OCWebPrintReminderEditData = oc.web.dispatcher.print_reminder_edit_data);
(window.OCWebPrintPanelStack = oc.web.dispatcher.print_panel_stack);
(window.OCWebPrintPaymentsData = oc.web.dispatcher.print_payments_data);
(window.OCWebPrintRouterPath = oc.web.dispatcher.print_router_path);
(window.OCWebUtils = ({"deref": cljs.core.deref, "keyword": cljs.core.keyword, "count": cljs.core.count, "get": cljs.core.get, "filter": cljs.core.filter, "map": cljs.core.map, "clj__GT_js": cljs.core.clj__GT_js, "js__GT_clj": cljs.core.js__GT_clj}));

//# sourceMappingURL=oc.web.dispatcher.js.map
