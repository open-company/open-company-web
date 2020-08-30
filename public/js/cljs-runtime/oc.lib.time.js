goog.provide('oc.lib.time');
oc.lib.time.timestamp_format = (cljs_time.format.formatters.cljs$core$IFn$_invoke$arity$1 ? cljs_time.format.formatters.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"date-time","date-time",177938180)) : cljs_time.format.formatters.call(null,new cljs.core.Keyword(null,"date-time","date-time",177938180)));
/**
 * ISO 8601 string timestamp for the current time.
 */
oc.lib.time.current_timestamp = (function oc$lib$time$current_timestamp(){
return cljs_time.format.unparse(oc.lib.time.timestamp_format,cljs_time.core.now());
});
oc.lib.time.millis = (function oc$lib$time$millis(t){
return cljs_time.coerce.to_long(t);
});
oc.lib.time.now_ts = (function oc$lib$time$now_ts(){
return oc.lib.time.millis(cljs_time.core.now());
});

//# sourceMappingURL=oc.lib.time.js.map
