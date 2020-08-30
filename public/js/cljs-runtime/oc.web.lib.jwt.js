goog.provide('oc.web.lib.jwt');
var module$node_modules$jwt_decode$lib$index=shadow.js.require("module$node_modules$jwt_decode$lib$index", {});
oc.web.lib.jwt.decode = (function oc$web$lib$jwt$decode(encoded_jwt){
try{return module$node_modules$jwt_decode$lib$index(encoded_jwt);
}catch (e41052){if((e41052 instanceof Object)){
var e = e41052;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"warn","warn",-436710552),"oc.web.lib.jwt",null,14,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Failed attempt to decode JWT:",encoded_jwt], null);
}),null)),null,-534781095);

return null;
} else {
throw e41052;

}
}});
oc.web.lib.jwt.id_token = (function oc$web$lib$jwt$id_token(){
return oc.web.lib.cookies.get_cookie(new cljs.core.Keyword(null,"id-token","id-token",-339268306));
});
oc.web.lib.jwt.get_id_token_contents = (function oc$web$lib$jwt$get_id_token_contents(var_args){
var G__41054 = arguments.length;
switch (G__41054) {
case 0:
return oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.id_token());
}));

(oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$1 = (function (token){
var G__41055 = token;
var G__41055__$1 = (((G__41055 == null))?null:oc.web.lib.jwt.decode(G__41055));
if((G__41055__$1 == null)){
return null;
} else {
return cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$variadic(G__41055__$1,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"keywordize-keys","keywordize-keys",1310784252),true], 0));
}
}));

(oc.web.lib.jwt.get_id_token_contents.cljs$lang$maxFixedArity = 1);

oc.web.lib.jwt.jwt = (function oc$web$lib$jwt$jwt(){
return oc.web.lib.cookies.get_cookie(new cljs.core.Keyword(null,"jwt","jwt",1504015441));
});
oc.web.lib.jwt.get_contents = (function oc$web$lib$jwt$get_contents(){
var G__41056 = oc.web.lib.jwt.jwt();
var G__41056__$1 = (((G__41056 == null))?null:oc.web.lib.jwt.decode(G__41056));
if((G__41056__$1 == null)){
return null;
} else {
return cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$variadic(G__41056__$1,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"keywordize-keys","keywordize-keys",1310784252),true], 0));
}
});
oc.web.lib.jwt.get_key = (function oc$web$lib$jwt$get_key(k){
var contents = (cljs.core.truth_(oc.web.lib.jwt.jwt())?oc.web.lib.jwt.get_contents():oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$0());
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(contents,k);
});
oc.web.lib.jwt.expired_QMARK_ = (function oc$web$lib$jwt$expired_QMARK_(){
var expire = goog.date.DateTime.fromTimestamp(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"expire","expire",-70657108)));
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(expire,goog.date.min((new Date()),expire));
});
oc.web.lib.jwt.is_slack_org_QMARK_ = (function oc$web$lib$jwt$is_slack_org_QMARK_(){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"auth-source","auth-source",1912135250)),"slack");
});
oc.web.lib.jwt.team_id = (function oc$web$lib$jwt$team_id(){
return cljs.core.first(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"teams","teams",1677714510)));
});
oc.web.lib.jwt.user_id = (function oc$web$lib$jwt$user_id(){
return oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"user-id","user-id",-206822291));
});
oc.web.lib.jwt.is_admin_QMARK_ = (function oc$web$lib$jwt$is_admin_QMARK_(team_id){
var admins = oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"admin","admin",-1239101627));
return cljs.core.some(cljs.core.PersistentHashSet.createAsIfByAssoc([team_id]),admins);
});
/**
 * Keys of slack-bots are strings converted to keywords, since JSON adds the double quotes
 * when it gets keywordized on this side it becomes :"team-id".
 */
oc.web.lib.jwt.slack_bots_team_key = (function oc$web$lib$jwt$slack_bots_team_key(team_id){
return cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(["\"",cljs.core.str.cljs$core$IFn$_invoke$arity$1(team_id),"\""].join(''));
});
oc.web.lib.jwt.user_is_part_of_the_team = (function oc$web$lib$jwt$user_is_part_of_the_team(team_id){
var teams = oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"teams","teams",1677714510));
return cljs.core.some(cljs.core.PersistentHashSet.createAsIfByAssoc([team_id]),teams);
});
oc.web.lib.jwt.team_has_bot_QMARK_ = (function oc$web$lib$jwt$team_has_bot_QMARK_(team_id){
var slack_bots = oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"slack-bots","slack-bots",648744031));
return cljs.core.some((function (p__41057){
var vec__41058 = p__41057;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41058,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41058,(1),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.lib.jwt.slack_bots_team_key(team_id),k)){
return v;
} else {
return null;
}
}),slack_bots);
});
(window.OCWebPrintJWTContents = oc.web.lib.jwt.get_contents);

//# sourceMappingURL=oc.web.lib.jwt.js.map
