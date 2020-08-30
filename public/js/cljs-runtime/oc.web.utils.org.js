goog.provide('oc.web.utils.org');
oc.web.utils.org.org_avatar_filestack_config = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"accept","accept",1874130431),"image/*",new cljs.core.Keyword(null,"fromSources","fromSources",-481915603),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["local_file_system"], null)], null);
oc.web.utils.org.org_name_max_length = (50);
oc.web.utils.org.clean_email_domain = (function oc$web$utils$org$clean_email_domain(email_domain){
if(cljs.core.truth_(email_domain)){
var lower_case_domain = clojure.string.lower_case(email_domain);
var trailing_slash_QMARK_ = lower_case_domain.endsWith("/");
var no_trailing_slash = (cljs.core.truth_(trailing_slash_QMARK_)?cljs.core.subs.cljs$core$IFn$_invoke$arity$3(email_domain,(0),(cljs.core.count(email_domain) - (1))):email_domain);
var no_beginning_at = (cljs.core.truth_(no_trailing_slash.startsWith("@"))?cljs.core.subs.cljs$core$IFn$_invoke$arity$2(no_trailing_slash,(1)):no_trailing_slash);
var no_beginning_http = (cljs.core.truth_(no_beginning_at.startsWith("http://"))?cljs.core.subs.cljs$core$IFn$_invoke$arity$2(no_beginning_at,(7)):no_beginning_at);
var no_beginning_https = (cljs.core.truth_(no_beginning_http.startsWith("https://"))?cljs.core.subs.cljs$core$IFn$_invoke$arity$2(no_beginning_http,(8)):no_beginning_http);
if(cljs.core.truth_(no_beginning_https.startsWith("www."))){
return cljs.core.subs.cljs$core$IFn$_invoke$arity$2(no_beginning_https,(4));
} else {
return no_beginning_https;
}
} else {
return null;
}
});
oc.web.utils.org.disappearing_count_value = (function oc$web$utils$org$disappearing_count_value(previous_val,next_val){
if(((cljs.core.integer_QMARK_(next_val)) && ((next_val > (0))))){
return next_val;
} else {
return previous_val;
}
});

//# sourceMappingURL=oc.web.utils.org.js.map
