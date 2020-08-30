goog.provide('oc.web.urls');
oc.web.urls.params__GT_query_string = (function oc$web$urls$params__GT_query_string(m){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2("&",(function (){var iter__4529__auto__ = (function oc$web$urls$params__GT_query_string_$_iter__41910(s__41911){
return (new cljs.core.LazySeq(null,(function (){
var s__41911__$1 = s__41911;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__41911__$1);
if(temp__5735__auto__){
var s__41911__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__41911__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__41911__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__41913 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__41912 = (0);
while(true){
if((i__41912 < size__4528__auto__)){
var vec__41915 = cljs.core._nth(c__4527__auto__,i__41912);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41915,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41915,(1),null);
cljs.core.chunk_append(b__41913,[cljs.core.name(k),"=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)].join(''));

var G__42029 = (i__41912 + (1));
i__41912 = G__42029;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__41913),oc$web$urls$params__GT_query_string_$_iter__41910(cljs.core.chunk_rest(s__41911__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__41913),null);
}
} else {
var vec__41919 = cljs.core.first(s__41911__$2);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41919,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41919,(1),null);
return cljs.core.cons([cljs.core.name(k),"=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)].join(''),oc$web$urls$params__GT_query_string_$_iter__41910(cljs.core.rest(s__41911__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(m);
})());
});
oc.web.urls.home = "/";
oc.web.urls.home_no_redirect = [oc.web.urls.home,"?no_redirect=1"].join('');
oc.web.urls.about = "/about";
oc.web.urls.slack = "/slack";
oc.web.urls.domain = "wuts.io";
oc.web.urls.blog = ["https://blog.",oc.web.urls.domain].join('');
oc.web.urls.contact = "/contact";
oc.web.urls.press_kit = "/press-kit";
oc.web.urls.help = ["http://help.",oc.web.urls.domain,"/"].join('');
oc.web.urls.what_s_new = "https://wuts.news/";
oc.web.urls.home_try_it_focus = [oc.web.urls.home,"?tif"].join('');
oc.web.urls.contact_email = ["hello@",oc.web.urls.domain].join('');
oc.web.urls.contact_mail_to = ["mailto:",oc.web.urls.contact_email].join('');
oc.web.urls.login = "/login";
oc.web.urls.native_login = "/login/desktop";
oc.web.urls.sign_up = "/sign-up";
oc.web.urls.sign_up_slack = "/sign-up/slack";
oc.web.urls.sign_up_profile = "/sign-up/profile";
oc.web.urls.sign_up_team = "/sign-up/team";
oc.web.urls.sign_up_update_team = (function oc$web$urls$sign_up_update_team(var_args){
var G__41930 = arguments.length;
switch (G__41930) {
case 0:
return oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [oc.web.urls.sign_up,"/",cljs.core.name(org_slug),"/team"].join('');
}));

(oc.web.urls.sign_up_update_team.cljs$lang$maxFixedArity = 1);

oc.web.urls.sign_up_invite = (function oc$web$urls$sign_up_invite(var_args){
var G__41934 = arguments.length;
switch (G__41934) {
case 0:
return oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [oc.web.urls.sign_up,"/",cljs.core.name(org_slug),"/invite"].join('');
}));

(oc.web.urls.sign_up_invite.cljs$lang$maxFixedArity = 1);

oc.web.urls.slack_lander_check = "/slack-lander/check";
oc.web.urls.google_lander_check = "/google/lander";
oc.web.urls.logout = "/logout";
oc.web.urls.pricing = "/pricing";
oc.web.urls.terms = "/terms";
oc.web.urls.privacy = "/privacy";
oc.web.urls.not_found = (function oc$web$urls$not_found(var_args){
var args__4742__auto__ = [];
var len__4736__auto___42033 = arguments.length;
var i__4737__auto___42034 = (0);
while(true){
if((i__4737__auto___42034 < len__4736__auto___42033)){
args__4742__auto__.push((arguments[i__4737__auto___42034]));

var G__42036 = (i__4737__auto___42034 + (1));
i__4737__auto___42034 = G__42036;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.urls.not_found.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.urls.not_found.cljs$core$IFn$_invoke$arity$variadic = (function (p__41943){
var vec__41946 = p__41943;
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41946,(0),null);
return ["/404",(cljs.core.truth_(params)?["?",oc.web.urls.params__GT_query_string(params)].join(''):null)].join('');
}));

(oc.web.urls.not_found.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.urls.not_found.cljs$lang$applyTo = (function (seq41941){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq41941));
}));

oc.web.urls.email_confirmation = "/verify";
oc.web.urls.confirm_invitation = "/invite";
oc.web.urls.confirm_invitation_password = "/invite/password";
oc.web.urls.confirm_invitation_profile = "/invite/profile";
oc.web.urls.password_reset = "/reset";
oc.web.urls.email_wall = "/email-required";
oc.web.urls.login_wall = "/login-wall";
oc.web.urls.apps_detect = "/apps/detect";
/**
 * Org url
 */
oc.web.urls.org = (function oc$web$urls$org(var_args){
var G__41957 = arguments.length;
switch (G__41957) {
case 0:
return oc.web.urls.org.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.org.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.org.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.org.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return ["/",cljs.core.name(org_slug)].join('');
}));

(oc.web.urls.org.cljs$lang$maxFixedArity = 1);

/**
 * Org inbox url
 */
oc.web.urls.inbox = (function oc$web$urls$inbox(var_args){
var G__41960 = arguments.length;
switch (G__41960) {
case 0:
return oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/inbox"].join('');
}));

(oc.web.urls.inbox.cljs$lang$maxFixedArity = 1);

/**
 * Org all posts url
 */
oc.web.urls.all_posts = (function oc$web$urls$all_posts(var_args){
var G__41962 = arguments.length;
switch (G__41962) {
case 0:
return oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/all-posts"].join('');
}));

(oc.web.urls.all_posts.cljs$lang$maxFixedArity = 1);

/**
 * Org following url
 */
oc.web.urls.following = (function oc$web$urls$following(var_args){
var G__41967 = arguments.length;
switch (G__41967) {
case 0:
return oc.web.urls.following.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.following.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.following.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.following.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.following.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/home"].join('');
}));

(oc.web.urls.following.cljs$lang$maxFixedArity = 1);

/**
 * Org unfollowing url
 */
oc.web.urls.unfollowing = (function oc$web$urls$unfollowing(var_args){
var G__41969 = arguments.length;
switch (G__41969) {
case 0:
return oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/unfollowing"].join('');
}));

(oc.web.urls.unfollowing.cljs$lang$maxFixedArity = 1);

/**
 * Org follow-ups url
 */
oc.web.urls.follow_ups = (function oc$web$urls$follow_ups(var_args){
var G__41972 = arguments.length;
switch (G__41972) {
case 0:
return oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/follow-ups"].join('');
}));

(oc.web.urls.follow_ups.cljs$lang$maxFixedArity = 1);

/**
 * Org bookmarks url
 */
oc.web.urls.bookmarks = (function oc$web$urls$bookmarks(var_args){
var G__41978 = arguments.length;
switch (G__41978) {
case 0:
return oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/bookmarks"].join('');
}));

(oc.web.urls.bookmarks.cljs$lang$maxFixedArity = 1);

/**
 * Org must see url
 */
oc.web.urls.must_see = (function oc$web$urls$must_see(var_args){
var G__41982 = arguments.length;
switch (G__41982) {
case 0:
return oc.web.urls.must_see.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.must_see.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.must_see.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.must_see.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.must_see.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/must-see"].join('');
}));

(oc.web.urls.must_see.cljs$lang$maxFixedArity = 1);

/**
 * Org replies url
 */
oc.web.urls.replies = (function oc$web$urls$replies(var_args){
var G__41988 = arguments.length;
switch (G__41988) {
case 0:
return oc.web.urls.replies.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.replies.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.replies.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.replies.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.replies.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/for-you"].join('');
}));

(oc.web.urls.replies.cljs$lang$maxFixedArity = 1);

/**
 * Org topics url
 */
oc.web.urls.topics = (function oc$web$urls$topics(var_args){
var G__41991 = arguments.length;
switch (G__41991) {
case 0:
return oc.web.urls.topics.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.topics.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.topics.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.topics.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.topics.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/topics"].join('');
}));

(oc.web.urls.topics.cljs$lang$maxFixedArity = 1);

/**
 * Board url
 */
oc.web.urls.board = (function oc$web$urls$board(var_args){
var G__41999 = arguments.length;
switch (G__41999) {
case 0:
return oc.web.urls.board.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.board.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.urls.board.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.board.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.board.cljs$core$IFn$_invoke$arity$1 = (function (board_slug){
return oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),board_slug);
}));

(oc.web.urls.board.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,board_slug){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/",cljs.core.name(board_slug)].join('');
}));

(oc.web.urls.board.cljs$lang$maxFixedArity = 2);

oc.web.urls.default_url_fn = oc.web.urls.following;
oc.web.urls.default_board_slug = "following";
oc.web.urls.default_landing = (function oc$web$urls$default_landing(var_args){
var G__42001 = arguments.length;
switch (G__42001) {
case 0:
return oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
if(cljs.core.fn_QMARK_(oc.web.urls.default_url_fn)){
return (oc.web.urls.default_url_fn.cljs$core$IFn$_invoke$arity$1 ? oc.web.urls.default_url_fn.cljs$core$IFn$_invoke$arity$1(org_slug) : oc.web.urls.default_url_fn.call(null,org_slug));
} else {
return oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1(org_slug);
}
}));

(oc.web.urls.default_landing.cljs$lang$maxFixedArity = 1);

/**
 * Org all posts url for the first ever land
 */
oc.web.urls.first_ever_landing = (function oc$web$urls$first_ever_landing(var_args){
var G__42003 = arguments.length;
switch (G__42003) {
case 0:
return oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(org_slug)),"/hello"].join('');
}));

(oc.web.urls.first_ever_landing.cljs$lang$maxFixedArity = 1);

oc.web.urls.drafts = (function oc$web$urls$drafts(var_args){
var G__42005 = arguments.length;
switch (G__42005) {
case 0:
return oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$1 = (function (org_slug){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/drafts"].join('');
}));

(oc.web.urls.drafts.cljs$lang$maxFixedArity = 1);

/**
 * Entry url
 */
oc.web.urls.entry = (function oc$web$urls$entry(var_args){
var G__42007 = arguments.length;
switch (G__42007) {
case 0:
return oc.web.urls.entry.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.entry.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.urls.entry.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.entry.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.entry.cljs$core$IFn$_invoke$arity$1 = (function (entry_uuid){
return oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),entry_uuid);
}));

(oc.web.urls.entry.cljs$core$IFn$_invoke$arity$2 = (function (board_slug,entry_uuid){
return oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),board_slug,entry_uuid);
}));

(oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3 = (function (org_slug,board_slug,entry_uuid){
return [oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(org_slug,board_slug),"/post/",cljs.core.name(entry_uuid)].join('');
}));

(oc.web.urls.entry.cljs$lang$maxFixedArity = 3);

/**
 * Comment url
 */
oc.web.urls.comment_url = (function oc$web$urls$comment_url(var_args){
var G__42015 = arguments.length;
switch (G__42015) {
case 1:
return oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$1 = (function (comment_uuid){
return oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$2 = (function (entry_uuid,comment_uuid){
return oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),entry_uuid,comment_uuid);
}));

(oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$3 = (function (board_slug,entry_uuid,comment_uuid){
return oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),board_slug,entry_uuid,comment_uuid);
}));

(oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4 = (function (org_slug,board_slug,entry_uuid,comment_uuid){
return [oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(org_slug,board_slug,entry_uuid),"/comment/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(comment_uuid)].join('');
}));

(oc.web.urls.comment_url.cljs$lang$maxFixedArity = 4);

/**
 * Secure url for activity to show read only view.
 */
oc.web.urls.secure_activity = (function oc$web$urls$secure_activity(var_args){
var G__42023 = arguments.length;
switch (G__42023) {
case 0:
return oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$1 = (function (secure_id){
return oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),secure_id);
}));

(oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,secure_id){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/post/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(secure_id)].join('');
}));

(oc.web.urls.secure_activity.cljs$lang$maxFixedArity = 2);

/**
 * contributions url
 */
oc.web.urls.contributions = (function oc$web$urls$contributions(var_args){
var G__42025 = arguments.length;
switch (G__42025) {
case 0:
return oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1 = (function (author_uuid){
return oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),author_uuid);
}));

(oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$2 = (function (org_slug,author_uuid){
return [oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(org_slug),"/u/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(author_uuid)].join('');
}));

(oc.web.urls.contributions.cljs$lang$maxFixedArity = 2);

oc.web.urls.your_digest_url = (function oc$web$urls$your_digest_url(){
var temp__5733__auto__ = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__5733__auto__)){
var org_slug = temp__5733__auto__;
return oc.web.urls.following.cljs$core$IFn$_invoke$arity$1(org_slug);
} else {
return oc.web.urls.login;
}
});

//# sourceMappingURL=oc.web.urls.js.map
