goog.provide('oc.web.local_settings');
oc.web.local_settings.env = (function oc$web$local_settings$env(key){
var G__39134 = window;
var G__39134__$1 = (((G__39134 == null))?null:G__39134.OCEnv);
var G__39134__$2 = (((G__39134__$1 == null))?null:cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$1(G__39134__$1));
var G__39134__$3 = (((G__39134__$2 == null))?null:clojure.walk.keywordize_keys(G__39134__$2));
if((G__39134__$3 == null)){
return null;
} else {
return (key.cljs$core$IFn$_invoke$arity$1 ? key.cljs$core$IFn$_invoke$arity$1(G__39134__$3) : key.call(null,G__39134__$3));
}
});
oc.web.local_settings.local_dsn = "https://747713ae92c246d1a64bbce9aab3da34@app.getsentry.com/73174";
oc.web.local_settings.local_whitelist_array = cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["localhost","127.0.0.1",oc.web.local_settings.env(new cljs.core.Keyword(null,"web-hostname","web-hostname",1611064379))], null));
oc.web.local_settings.web_hostname = (function (){var or__4126__auto__ = oc.web.local_settings.env(new cljs.core.Keyword(null,"web-hostname","web-hostname",1611064379));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "localhost";
}
})();
oc.web.local_settings.web_port = (function (){var or__4126__auto__ = oc.web.local_settings.env(new cljs.core.Keyword(null,"web-port","web-port",1875931715));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "3559";
}
})();
oc.web.local_settings.web_server_domain = ["http://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.local_settings.web_hostname),":",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.local_settings.web_port)].join('');
oc.web.local_settings.storage_server_domain = ["http://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.local_settings.web_hostname),":3001"].join('');
oc.web.local_settings.auth_server_domain = ["http://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.local_settings.web_hostname),":3003"].join('');
oc.web.local_settings.pay_server_domain = ["http://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.local_settings.web_hostname),":3004"].join('');
oc.web.local_settings.interaction_server_domain = ["http://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.local_settings.web_hostname),":3002"].join('');
oc.web.local_settings.change_server_domain = ["http://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.local_settings.web_hostname),":3006"].join('');
oc.web.local_settings.search_server_domain = ["http://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.local_settings.web_hostname),":3007"].join('');
oc.web.local_settings.search_enabled_QMARK_ = true;
oc.web.local_settings.reminders_enabled_QMARK_ = false;
oc.web.local_settings.reminder_server_domain = ["http://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.local_settings.web_hostname),":3011"].join('');
oc.web.local_settings.web_server = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.local_settings.web_hostname),":",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.local_settings.web_port)].join('');
oc.web.local_settings.jwt_cookie_domain = oc.web.local_settings.web_hostname;
oc.web.local_settings.jwt_cookie_secure = false;
oc.web.local_settings.deploy_key = "";
oc.web.local_settings.filestack_key = "Aoay0qXUSOyVIcDvls4Egz";
oc.web.local_settings.cookie_name_prefix = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.local_settings.web_hostname),"-"].join('');
oc.web.local_settings.log_level = "debug";
oc.web.local_settings.cdn_url = "";
oc.web.local_settings.attachments_bucket = "open-company-attachments-non-prod";
oc.web.local_settings.oc_seen_ttl = (30);
oc.web.local_settings.payments_enabled = true;
oc.web.local_settings.stripe_api_key = "pk_test_srP6wqbAalvBWYxcdAi4NlX0";
oc.web.local_settings.ws_monitor_interval = (30);
oc.web.local_settings.giphy_api_key = "M2FfNXledXWbpa7FZkg2vvUD8kHMTQVF";
oc.web.local_settings.file_upload_size = (((20) * (1024)) * (1024));
oc.web.local_settings.mac_app_url = "https://github.com/open-company/open-company-web/releases/download/untagged-a060f76d2ed11d47ff35/Carrot.dmg";
oc.web.local_settings.win_app_url = "https://github.com/open-company/open-company-web/releases/download/untagged-a060f76d2ed11d47ff35/Carrot.exe";
oc.web.local_settings.ios_app_url = "https://apps.apple.com/us/app/carrot-mobile/id1473028573";
oc.web.local_settings.android_app_url = "https://play.google.com/apps/testing/io.carrot.mobile";
oc.web.local_settings.poll_can_add_reply = false;
oc.web.local_settings.wut_QMARK_ = true;
oc.web.local_settings.publisher_board_enabled_QMARK_ = false;
oc.web.local_settings.seen_home_container_id = "1111-1111-1111";
oc.web.local_settings.seen_replies_container_id = "2222-2222-2222";
oc.web.local_settings.digest_times = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"700","700",-54226475),null,new cljs.core.Keyword(null,"1700","1700",-1665066246),null,new cljs.core.Keyword(null,"1200","1200",975793790),null], null), null);

//# sourceMappingURL=oc.web.local_settings.js.map
