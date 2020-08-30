goog.provide('oc.web.stores.jwt');
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"jwt","jwt",1504015441),(function (db,p__43280){
var vec__43281 = p__43280;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43281,(0),null);
var jwt_data = oc.web.lib.jwt.get_contents();
var next_db = (cljs.core.truth_(oc.web.lib.cookies.get_cookie(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411)))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,oc.web.dispatcher.show_login_overlay_key,cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411)))):db);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.stores.jwt",null,16,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [jwt_data], null);
}),null)),null,1561390938);

return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(next_db,new cljs.core.Keyword(null,"jwt","jwt",1504015441),jwt_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"id-token","id-token",-339268306),(function (db,p__43286){
var vec__43287 = p__43286;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43287,(0),null);
var jwt_data = oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$0();
var next_db = (cljs.core.truth_(oc.web.lib.cookies.get_cookie(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411)))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,oc.web.dispatcher.show_login_overlay_key,cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411)))):db);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.stores.jwt",null,26,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [jwt_data], null);
}),null)),null,230061522);

return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(next_db,new cljs.core.Keyword(null,"id-token","id-token",-339268306),jwt_data);
}));

//# sourceMappingURL=oc.web.stores.jwt.js.map
