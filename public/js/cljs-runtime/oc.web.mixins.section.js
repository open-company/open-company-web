goog.provide('oc.web.mixins.section');
oc.web.mixins.section.container_nav_in = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.ws.change_client.container_watch();

return s;
})], null);
oc.web.mixins.section.focus_reload = (function oc$web$mixins$section$focus_reload(){
var temp__5735__auto__ = (function (){var or__4126__auto__ = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0();
}
})();
if(cljs.core.truth_(temp__5735__auto__)){
var slug = temp__5735__auto__;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.mixins.section",null,18,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Reloading data for:",slug], null);
}),null)),null,1913733760);

return oc.web.actions.activity.refresh_board_data(slug);
} else {
return null;
}
});
oc.web.mixins.section.window_focus_auto_loader = (function (){var throttled_refresh = (new goog.async.Throttle(oc.web.mixins.section.focus_reload,(5000)));
var fire_BANG_ = (function (){
return throttled_refresh.fire();
});
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
window.addEventListener("focus",fire_BANG_);

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
window.removeEventListener("focus",fire_BANG_);

throttled_refresh.stop();

throttled_refresh.dispose();

return s;
})], null);
})();
oc.web.mixins.section.default_get_comments_delay = (0);
oc.web.mixins.section.load_entry_comments = (function oc$web$mixins$section$load_entry_comments(var_args){
var G__40032 = arguments.length;
switch (G__40032) {
case 1:
return oc.web.mixins.section.load_entry_comments.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.mixins.section.load_entry_comments.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.mixins.section.load_entry_comments.cljs$core$IFn$_invoke$arity$1 = (function (container_data_get){
return oc.web.mixins.section.load_entry_comments.cljs$core$IFn$_invoke$arity$2(container_data_get,oc.web.mixins.section.default_get_comments_delay);
}));

(oc.web.mixins.section.load_entry_comments.cljs$core$IFn$_invoke$arity$2 = (function (container_data_get,delay){
var loaded_uuids = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentHashSet.EMPTY);
var load_comments = (function (s){
var temp__5735__auto__ = (container_data_get.cljs$core$IFn$_invoke$arity$1 ? container_data_get.cljs$core$IFn$_invoke$arity$1(s) : container_data_get.call(null,s));
if(cljs.core.truth_(temp__5735__auto__)){
var container_data = temp__5735__auto__;
var seq__40033 = cljs.core.seq(new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(container_data));
var chunk__40036 = null;
var count__40037 = (0);
var i__40038 = (0);
while(true){
if((i__40038 < count__40037)){
var entry = chunk__40036.cljs$core$IIndexed$_nth$arity$2(null,i__40038);
if(cljs.core.truth_((function (){var and__4115__auto__ = oc.web.utils.activity.entry_QMARK_(entry);
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not((function (){var G__40052 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry);
var fexpr__40051 = cljs.core.deref(loaded_uuids);
return (fexpr__40051.cljs$core$IFn$_invoke$arity$1 ? fexpr__40051.cljs$core$IFn$_invoke$arity$1(G__40052) : fexpr__40051.call(null,G__40052));
})())) && (cljs.core.not(new cljs.core.Keyword(null,"comments-loaded?","comments-loaded?",-595034611).cljs$core$IFn$_invoke$arity$1(entry))));
} else {
return and__4115__auto__;
}
})())){
var full_entry_40058 = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(loaded_uuids,cljs.core.conj,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry));

oc.web.lib.utils.after(delay,((function (seq__40033,chunk__40036,count__40037,i__40038,full_entry_40058,entry,container_data,temp__5735__auto__,loaded_uuids){
return (function (){
return oc.web.utils.activity.get_comments_if_needed.cljs$core$IFn$_invoke$arity$1(full_entry_40058);
});})(seq__40033,chunk__40036,count__40037,i__40038,full_entry_40058,entry,container_data,temp__5735__auto__,loaded_uuids))
);


var G__40059 = seq__40033;
var G__40060 = chunk__40036;
var G__40061 = count__40037;
var G__40062 = (i__40038 + (1));
seq__40033 = G__40059;
chunk__40036 = G__40060;
count__40037 = G__40061;
i__40038 = G__40062;
continue;
} else {
var G__40063 = seq__40033;
var G__40064 = chunk__40036;
var G__40065 = count__40037;
var G__40066 = (i__40038 + (1));
seq__40033 = G__40063;
chunk__40036 = G__40064;
count__40037 = G__40065;
i__40038 = G__40066;
continue;
}
} else {
var temp__5735__auto____$1 = cljs.core.seq(seq__40033);
if(temp__5735__auto____$1){
var seq__40033__$1 = temp__5735__auto____$1;
if(cljs.core.chunked_seq_QMARK_(seq__40033__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__40033__$1);
var G__40067 = cljs.core.chunk_rest(seq__40033__$1);
var G__40068 = c__4556__auto__;
var G__40069 = cljs.core.count(c__4556__auto__);
var G__40070 = (0);
seq__40033 = G__40067;
chunk__40036 = G__40068;
count__40037 = G__40069;
i__40038 = G__40070;
continue;
} else {
var entry = cljs.core.first(seq__40033__$1);
if(cljs.core.truth_((function (){var and__4115__auto__ = oc.web.utils.activity.entry_QMARK_(entry);
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not((function (){var G__40056 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry);
var fexpr__40055 = cljs.core.deref(loaded_uuids);
return (fexpr__40055.cljs$core$IFn$_invoke$arity$1 ? fexpr__40055.cljs$core$IFn$_invoke$arity$1(G__40056) : fexpr__40055.call(null,G__40056));
})())) && (cljs.core.not(new cljs.core.Keyword(null,"comments-loaded?","comments-loaded?",-595034611).cljs$core$IFn$_invoke$arity$1(entry))));
} else {
return and__4115__auto__;
}
})())){
var full_entry_40071 = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(loaded_uuids,cljs.core.conj,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry));

oc.web.lib.utils.after(delay,((function (seq__40033,chunk__40036,count__40037,i__40038,full_entry_40071,entry,seq__40033__$1,temp__5735__auto____$1,container_data,temp__5735__auto__,loaded_uuids){
return (function (){
return oc.web.utils.activity.get_comments_if_needed.cljs$core$IFn$_invoke$arity$1(full_entry_40071);
});})(seq__40033,chunk__40036,count__40037,i__40038,full_entry_40071,entry,seq__40033__$1,temp__5735__auto____$1,container_data,temp__5735__auto__,loaded_uuids))
);


var G__40072 = cljs.core.next(seq__40033__$1);
var G__40073 = null;
var G__40074 = (0);
var G__40075 = (0);
seq__40033 = G__40072;
chunk__40036 = G__40073;
count__40037 = G__40074;
i__40038 = G__40075;
continue;
} else {
var G__40076 = cljs.core.next(seq__40033__$1);
var G__40077 = null;
var G__40078 = (0);
var G__40079 = (0);
seq__40033 = G__40076;
chunk__40036 = G__40077;
count__40037 = G__40078;
i__40038 = G__40079;
continue;
}
}
} else {
return null;
}
}
break;
}
} else {
return null;
}
});
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
load_comments(s);

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
load_comments(s);

return s;
})], null);
}));

(oc.web.mixins.section.load_entry_comments.cljs$lang$maxFixedArity = 2);


//# sourceMappingURL=oc.web.mixins.section.js.map
