goog.provide('oc.web.utils.user');
oc.web.utils.user.default_avatar = "/img/ML/happy_face_purple.svg";
oc.web.utils.user.other_default_avatars = new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/img/ML/happy_face_green.svg","/img/ML/happy_face_blue.svg","/img/ML/happy_face_purple.svg","/img/ML/happy_face_yellow.svg"], null);
oc.web.utils.user.default_avatar_QMARK_ = (function oc$web$utils$user$default_avatar_QMARK_(image_url){
var images_set = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.set(oc.web.utils.user.other_default_avatars),oc.web.utils.user.default_avatar);
return (images_set.cljs$core$IFn$_invoke$arity$1 ? images_set.cljs$core$IFn$_invoke$arity$1(image_url) : images_set.call(null,image_url));
});
oc.web.utils.user.random_avatar = (function oc$web$utils$user$random_avatar(){
return cljs.core.first(cljs.core.shuffle(cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.utils.user.other_default_avatars,oc.web.utils.user.default_avatar))));
});
oc.web.utils.user.publisher_board_slug_prefix = "publisher-board-";
oc.web.utils.user.user_avatar_filestack_config = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"accept","accept",1874130431),"image/*",new cljs.core.Keyword(null,"fromSources","fromSources",-481915603),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["local_file_system"], null),new cljs.core.Keyword(null,"transformations","transformations",-1807703897),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"crop","crop",793731643),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"aspectRatio","aspectRatio",-218867702),(1)], null)], null)], null);
oc.web.utils.user.user_name_max_lenth = (64);
/**
 * Check if the current user has an associated Slack user under a team that has the bot.
 */
oc.web.utils.user.user_has_slack_with_bot_QMARK_ = (function oc$web$utils$user$user_has_slack_with_bot_QMARK_(current_user_data,bots_data,team_roster){
var slack_orgs_with_bot = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561),bots_data);
var slack_users = new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__37303_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__37303_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data));
}),new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(team_roster))));
return cljs.core.some((function (p1__37304_SHARP_){
return cljs.core.contains_QMARK_(slack_users,cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(p1__37304_SHARP_));
}),slack_orgs_with_bot);
});
oc.web.utils.user.user_has_push_token_QMARK_ = (function oc$web$utils$user$user_has_push_token_QMARK_(current_user_data,push_token){
var current_push_tokens = cljs.core.set(new cljs.core.Keyword(null,"expo-push-tokens","expo-push-tokens",-1639730197).cljs$core$IFn$_invoke$arity$1(current_user_data));
return (current_push_tokens.cljs$core$IFn$_invoke$arity$1 ? current_push_tokens.cljs$core$IFn$_invoke$arity$1(push_token) : current_push_tokens.call(null,push_token));
});
oc.web.utils.user.auth_link_with_state = (function oc$web$utils$user$auth_link_with_state(original_url,p__37312){
var map__37315 = p__37312;
var map__37315__$1 = (((((!((map__37315 == null))))?(((((map__37315.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37315.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37315):map__37315);
var state = map__37315__$1;
var user_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37315__$1,new cljs.core.Keyword(null,"user-id","user-id",-206822291));
var team_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37315__$1,new cljs.core.Keyword(null,"team-id","team-id",-14505725));
var redirect = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37315__$1,new cljs.core.Keyword(null,"redirect","redirect",-1975673286));
var redirect_origin = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37315__$1,new cljs.core.Keyword(null,"redirect-origin","redirect-origin",1138035106));
var parsed_url = (new URL(original_url));
var old_state_string = parsed_url.searchParams.get("state");
var decoded_state = oc.lib.oauth.decode_state_string(old_state_string);
var combined_state = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([decoded_state,state], 0));
var new_state_string = oc.lib.oauth.encode_state_string(combined_state);
parsed_url.searchParams.set("state",new_state_string);

return cljs.core.str.cljs$core$IFn$_invoke$arity$1(parsed_url);
});
oc.web.utils.user.localized_time = (function oc$web$utils$user$localized_time(var_args){
var G__37331 = arguments.length;
switch (G__37331) {
case 1:
return oc.web.utils.user.localized_time.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.utils.user.localized_time.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.utils.user.localized_time.cljs$core$IFn$_invoke$arity$1 = (function (tz){
return oc.web.utils.user.localized_time.cljs$core$IFn$_invoke$arity$2(tz,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.web.utils.user.localized_time.cljs$core$IFn$_invoke$arity$2 = (function (tz,p__37332){
var map__37333 = p__37332;
var map__37333__$1 = (((((!((map__37333 == null))))?(((((map__37333.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37333.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37333):map__37333);
var opts = map__37333__$1;
var suffix = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37333__$1,new cljs.core.Keyword(null,"suffix","suffix",367373057));
try{return [cljs.core.str.cljs$core$IFn$_invoke$arity$1((new Date()).toLocaleTimeString(window.navigator.language,({"hour": "2-digit", "minute": "2-digit", "format": "hour:minute", "timeZone": tz}))),((cljs.core.seq(suffix))?[" ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cuerdas.core.trim.cljs$core$IFn$_invoke$arity$1(suffix))].join(''):null)].join('');
}catch (e37335){var e = e37335;
return null;
}}));

(oc.web.utils.user.localized_time.cljs$lang$maxFixedArity = 2);

oc.web.utils.user.readable_tz = (function oc$web$utils$user$readable_tz(tz){
return cuerdas.core.collapse_whitespace(cuerdas.core.replace(tz,/(_|-)/," "));
});
oc.web.utils.user.time_with_timezone = (function oc$web$utils$user$time_with_timezone(var_args){
var G__37342 = arguments.length;
switch (G__37342) {
case 1:
return oc.web.utils.user.time_with_timezone.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.utils.user.time_with_timezone.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.utils.user.time_with_timezone.cljs$core$IFn$_invoke$arity$1 = (function (timezone){
return oc.web.utils.user.time_with_timezone.cljs$core$IFn$_invoke$arity$2(timezone,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.web.utils.user.time_with_timezone.cljs$core$IFn$_invoke$arity$2 = (function (timezone,opts){
var temp__5735__auto__ = oc.web.utils.user.localized_time.cljs$core$IFn$_invoke$arity$2(timezone,opts);
if(cljs.core.truth_(temp__5735__auto__)){
var lt = temp__5735__auto__;
return oc.web.lib.utils.time_without_leading_zeros(lt);
} else {
return null;
}
}));

(oc.web.utils.user.time_with_timezone.cljs$lang$maxFixedArity = 2);

oc.web.utils.user.timezone_location_string = (function oc$web$utils$user$timezone_location_string(var_args){
var args__4742__auto__ = [];
var len__4736__auto___37479 = arguments.length;
var i__4737__auto___37480 = (0);
while(true){
if((i__4737__auto___37480 < len__4736__auto___37479)){
args__4742__auto__.push((arguments[i__4737__auto___37480]));

var G__37481 = (i__4737__auto___37480 + (1));
i__4737__auto___37480 = G__37481;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.utils.user.timezone_location_string.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.utils.user.timezone_location_string.cljs$core$IFn$_invoke$arity$variadic = (function (user_data,p__37366){
var vec__37367 = p__37366;
var local_time_string_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37367,(0),null);
var twt = oc.web.utils.user.time_with_timezone.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"timezone","timezone",1831928099).cljs$core$IFn$_invoke$arity$1(user_data),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"suffix","suffix",367373057),(cljs.core.truth_(local_time_string_QMARK_)?"local time":null)], null));
var tz = oc.web.utils.user.readable_tz(new cljs.core.Keyword(null,"timezone","timezone",1831928099).cljs$core$IFn$_invoke$arity$1(user_data));
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(twt),cljs.core.str.cljs$core$IFn$_invoke$arity$1(((cljs.core.seq(new cljs.core.Keyword(null,"location","location",1815599388).cljs$core$IFn$_invoke$arity$1(user_data)))?((cljs.core.seq(twt))?[" (",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"location","location",1815599388).cljs$core$IFn$_invoke$arity$1(user_data)),")"].join(''):new cljs.core.Keyword(null,"location","location",1815599388).cljs$core$IFn$_invoke$arity$1(user_data)):((cljs.core.seq(tz))?((cljs.core.seq(twt))?[" (",cljs.core.str.cljs$core$IFn$_invoke$arity$1(tz),")"].join(''):tz):null)))].join('');
}));

(oc.web.utils.user.timezone_location_string.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.utils.user.timezone_location_string.cljs$lang$applyTo = (function (seq37356){
var G__37357 = cljs.core.first(seq37356);
var seq37356__$1 = cljs.core.next(seq37356);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__37357,seq37356__$1);
}));

oc.web.utils.user.location_timezone_string = (function oc$web$utils$user$location_timezone_string(var_args){
var args__4742__auto__ = [];
var len__4736__auto___37487 = arguments.length;
var i__4737__auto___37488 = (0);
while(true){
if((i__4737__auto___37488 < len__4736__auto___37487)){
args__4742__auto__.push((arguments[i__4737__auto___37488]));

var G__37489 = (i__4737__auto___37488 + (1));
i__4737__auto___37488 = G__37489;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.utils.user.location_timezone_string.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.utils.user.location_timezone_string.cljs$core$IFn$_invoke$arity$variadic = (function (user_data,p__37385){
var vec__37386 = p__37385;
var local_time_string_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37386,(0),null);
var twt = oc.web.utils.user.time_with_timezone.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"timezone","timezone",1831928099).cljs$core$IFn$_invoke$arity$1(user_data),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"suffix","suffix",367373057),(cljs.core.truth_(local_time_string_QMARK_)?"local time":null)], null));
var tz = oc.web.utils.user.readable_tz(new cljs.core.Keyword(null,"timezone","timezone",1831928099).cljs$core$IFn$_invoke$arity$1(user_data));
return cljs.core.str.cljs$core$IFn$_invoke$arity$1(((cljs.core.seq(new cljs.core.Keyword(null,"location","location",1815599388).cljs$core$IFn$_invoke$arity$1(user_data)))?((cljs.core.seq(twt))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"location","location",1815599388).cljs$core$IFn$_invoke$arity$1(user_data))," (",cljs.core.str.cljs$core$IFn$_invoke$arity$1(twt),")"].join(''):new cljs.core.Keyword(null,"location","location",1815599388).cljs$core$IFn$_invoke$arity$1(user_data)):((cljs.core.seq(tz))?((cljs.core.seq(twt))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(twt)," (",cljs.core.str.cljs$core$IFn$_invoke$arity$1(tz),")"].join(''):tz):null)));
}));

(oc.web.utils.user.location_timezone_string.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.utils.user.location_timezone_string.cljs$lang$applyTo = (function (seq37383){
var G__37384 = cljs.core.first(seq37383);
var seq37383__$1 = cljs.core.next(seq37383);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__37384,seq37383__$1);
}));

/**
 * @param {...*} var_args
 */
oc.web.utils.user.active_QMARK_ = (function() { 
var oc$web$utils$user$active_QMARK___delegate = function (args__33708__auto__){
var ocr_37394 = cljs.core.vec(args__33708__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_37394)) && ((cljs.core.count(ocr_37394) === 1)))){
try{var ocr_37394_0__37397 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37394,(0));
if(cljs.core.map_QMARK_(ocr_37394_0__37397)){
var user = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37394,(0));
var G__37415 = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(user);
return (oc.web.utils.user.active_QMARK_.cljs$core$IFn$_invoke$arity$1 ? oc.web.utils.user.active_QMARK_.cljs$core$IFn$_invoke$arity$1(G__37415) : oc.web.utils.user.active_QMARK_.call(null,G__37415));
} else {
throw cljs.core.match.backtrack;

}
}catch (e37406){if((e37406 instanceof Error)){
var e__32662__auto__ = e37406;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_37394_0__37397 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37394,(0));
if(cljs.core.not(ocr_37394_0__37397)){
var _user_status = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37394,(0));
return false;
} else {
throw cljs.core.match.backtrack;

}
}catch (e37407){if((e37407 instanceof Error)){
var e__32662__auto____$1 = e37407;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_37394_0__37397 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37394,(0));
if(typeof ocr_37394_0__37397 === 'string'){
var user_status = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37394,(0));
var fexpr__37414 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["unverified",null,"active",null], null), null);
return (fexpr__37414.cljs$core$IFn$_invoke$arity$1 ? fexpr__37414.cljs$core$IFn$_invoke$arity$1(user_status) : fexpr__37414.call(null,user_status));
} else {
throw cljs.core.match.backtrack;

}
}catch (e37408){if((e37408 instanceof Error)){
var e__32662__auto____$2 = e37408;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e37408;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e37407;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e37406;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e37405){if((e37405 instanceof Error)){
var e__32662__auto__ = e37405;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_37394)].join('')));
} else {
throw e__32662__auto__;
}
} else {
throw e37405;

}
}};
var oc$web$utils$user$active_QMARK_ = function (var_args){
var args__33708__auto__ = null;
if (arguments.length > 0) {
var G__37497__i = 0, G__37497__a = new Array(arguments.length -  0);
while (G__37497__i < G__37497__a.length) {G__37497__a[G__37497__i] = arguments[G__37497__i + 0]; ++G__37497__i;}
  args__33708__auto__ = new cljs.core.IndexedSeq(G__37497__a,0,null);
} 
return oc$web$utils$user$active_QMARK___delegate.call(this,args__33708__auto__);};
oc$web$utils$user$active_QMARK_.cljs$lang$maxFixedArity = 0;
oc$web$utils$user$active_QMARK_.cljs$lang$applyTo = (function (arglist__37499){
var args__33708__auto__ = cljs.core.seq(arglist__37499);
return oc$web$utils$user$active_QMARK___delegate(args__33708__auto__);
});
oc$web$utils$user$active_QMARK_.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$user$active_QMARK___delegate;
return oc$web$utils$user$active_QMARK_;
})()
;
oc.web.utils.user.filter_active_users = (function oc$web$utils$user$filter_active_users(users_list){
return cljs.core.filter.cljs$core$IFn$_invoke$arity$2(oc.web.utils.user.active_QMARK_,users_list);
});
oc.web.utils.user.user_role_string = (function oc$web$utils$user$user_role_string(role_kw){
var G__37416 = role_kw;
var G__37416__$1 = (((G__37416 instanceof cljs.core.Keyword))?G__37416.fqn:null);
switch (G__37416__$1) {
case "admin":
return "admin";

break;
case "author":
return "contributor";

break;
default:
return "viewer";

}
});
/**
 * Get the author data from the org list of authors
 */
oc.web.utils.user.get_author = (function oc$web$utils$user$get_author(user_id,authors){
if(((cljs.core.seq(authors)) && (cljs.core.seq(user_id)))){
var fexpr__37425 = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),authors));
return (fexpr__37425.cljs$core$IFn$_invoke$arity$1 ? fexpr__37425.cljs$core$IFn$_invoke$arity$1(user_id) : fexpr__37425.call(null,user_id));
} else {
return null;
}
});
/**
 * Calculate the user type, return admin if it's an admin,
 *   check if it's in the authors list if not admin
 *   return viewer else.
 * @param {...*} var_args
 */
oc.web.utils.user.get_user_type = (function() { 
var oc$web$utils$user$get_user_type__delegate = function (args__33708__auto__){
var ocr_37427 = cljs.core.vec(args__33708__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_37427)) && ((cljs.core.count(ocr_37427) === 2)))){
var user_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37427,(0));
var org_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37427,(1));
if(new cljs.core.Keyword(null,"admin?","admin?",-366877557).cljs$core$IFn$_invoke$arity$1(user_data) === true){
return new cljs.core.Keyword(null,"admin","admin",-1239101627);
} else {
if(cljs.core.truth_((function (){var G__37455 = new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data);
var fexpr__37454 = cljs.core.set(new cljs.core.Keyword(null,"admin","admin",-1239101627).cljs$core$IFn$_invoke$arity$1(user_data));
return (fexpr__37454.cljs$core$IFn$_invoke$arity$1 ? fexpr__37454.cljs$core$IFn$_invoke$arity$1(G__37455) : fexpr__37454.call(null,G__37455));
})())){
return new cljs.core.Keyword(null,"admin","admin",-1239101627);
} else {
if(cljs.core.truth_(oc.web.utils.user.get_author(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data),new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(org_data)))){
return new cljs.core.Keyword(null,"author","author",2111686192);
} else {
return new cljs.core.Keyword(null,"viewer","viewer",-783949853);

}
}
}
} else {
throw cljs.core.match.backtrack;

}
}catch (e37436){if((e37436 instanceof Error)){
var e__32662__auto__ = e37436;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_37427)) && ((cljs.core.count(ocr_37427) === 3)))){
try{var ocr_37427_2__37434 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37427,(2));
if(cljs.core.map_QMARK_(ocr_37427_2__37434)){
var board_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37427,(2));
var user_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37427,(0));
var org_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37427,(1));
if(cljs.core.truth_(oc.web.utils.user.get_author(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data),new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(board_data)))){
return new cljs.core.Keyword(null,"viewer","viewer",-783949853);
} else {
return null;
}
} else {
throw cljs.core.match.backtrack;

}
}catch (e37439){if((e37439 instanceof Error)){
var e__32662__auto____$1 = e37439;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$1;
}
} else {
throw e37439;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e37438){if((e37438 instanceof Error)){
var e__32662__auto____$1 = e37438;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_37427)].join('')));
} else {
throw e__32662__auto____$1;
}
} else {
throw e37438;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e37436;

}
}};
var oc$web$utils$user$get_user_type = function (var_args){
var args__33708__auto__ = null;
if (arguments.length > 0) {
var G__37510__i = 0, G__37510__a = new Array(arguments.length -  0);
while (G__37510__i < G__37510__a.length) {G__37510__a[G__37510__i] = arguments[G__37510__i + 0]; ++G__37510__i;}
  args__33708__auto__ = new cljs.core.IndexedSeq(G__37510__a,0,null);
} 
return oc$web$utils$user$get_user_type__delegate.call(this,args__33708__auto__);};
oc$web$utils$user$get_user_type.cljs$lang$maxFixedArity = 0;
oc$web$utils$user$get_user_type.cljs$lang$applyTo = (function (arglist__37511){
var args__33708__auto__ = cljs.core.seq(arglist__37511);
return oc$web$utils$user$get_user_type__delegate(args__33708__auto__);
});
oc$web$utils$user$get_user_type.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$user$get_user_type__delegate;
return oc$web$utils$user$get_user_type;
})()
;

//# sourceMappingURL=oc.web.utils.user.js.map
