goog.provide('oc.web.lib.fullstory');
oc.web.lib.fullstory.identify = (function oc$web$lib$fullstory$identify(){
if(cljs.core.truth_((((typeof FS !== 'undefined'))?(function (){var or__4126__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.lib.jwt.id_token();
}
})():false))){
var is_id_token_QMARK_ = oc.web.lib.jwt.id_token();
var user_data = (cljs.core.truth_(is_id_token_QMARK_)?oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$0():oc.web.lib.jwt.get_contents());
return FS.identify(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data),cljs.core.clj__GT_js(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"displayName","displayName",-809144601),(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user_data))," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user_data))].join('');
}
})(),new cljs.core.Keyword(null,"securePostId","securePostId",1473632247),cljs.core.boolean$(is_id_token_QMARK_),new cljs.core.Keyword(null,"email","email",1415816706),new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data)], null)));
} else {
return null;
}
});
oc.web.lib.fullstory.track_org = (function oc$web$lib$fullstory$track_org(org_data){
if((((typeof FS !== 'undefined')) && (cljs.core.map_QMARK_(org_data)))){
return FS.setUserVars(cljs.core.clj__GT_js(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"org","org",1495985),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.Keyword(null,"org_slug","org_slug",-322631770),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.Keyword(null,"role","role",-736691072),"-"], null)));
} else {
return null;
}
});
oc.web.lib.fullstory.session_url = (function oc$web$lib$fullstory$session_url(){
if(cljs.core.truth_((((typeof FS !== 'undefined'))?FS.getCurrentSessionURL:false))){
return FS.getCurrentSessionURL();
} else {
return null;
}
});

//# sourceMappingURL=oc.web.lib.fullstory.js.map
