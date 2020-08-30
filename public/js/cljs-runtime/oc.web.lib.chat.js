goog.provide('oc.web.lib.chat');
oc.web.lib.chat.identify = (function oc$web$lib$chat$identify(){
if(cljs.core.truth_(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"email","email",1415816706)))){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.lib.chat",null,9,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Identify user to Intercom"], null);
}),null)),null,90951834);

return oc.web.lib.utils.after((100),(function (){
var user = oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0();
var user_id = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user);
var org__$1 = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
return window.Intercom("update",cljs.core.clj__GT_js(new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"user_id","user_id",993497112),user_id,new cljs.core.Keyword(null,"created_at","created_at",1484050750),new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"name","name",1843675177),oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"name","name",1843675177)),new cljs.core.Keyword(null,"email","email",1415816706),new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"org_author?","org_author?",98520264),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(org__$1)),user_id),new cljs.core.Keyword(null,"avatar","avatar",-1607499307),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"avatar","avatar",-1607499307),new cljs.core.Keyword(null,"image_url","image_url",-1356964050),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(user))], null),new cljs.core.Keyword(null,"company","company",-340475075),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org__$1),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org__$1),new cljs.core.Keyword(null,"created_at","created_at",1484050750),new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(org__$1)], null),new cljs.core.Keyword(null,"timezone","timezone",1831928099),new cljs.core.Keyword(null,"timezone","timezone",1831928099).cljs$core$IFn$_invoke$arity$1(user)], null)));
}));
} else {
return null;
}
});

//# sourceMappingURL=oc.web.lib.chat.js.map
