goog.provide('oc.web.mixins.seen');
oc.web.mixins.seen.container_nav_mixin = (function oc$web$mixins$seen$container_nav_mixin(){
var container_slug = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var sort_type = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
cljs.core.reset_BANG_(container_slug,(cljs.core.truth_(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0())?cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0()):oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0()));

cljs.core.reset_BANG_(sort_type,oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0());

oc.web.actions.activity.container_nav_in(cljs.core.deref(container_slug),cljs.core.deref(sort_type));

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
oc.web.actions.activity.container_nav_in(cljs.core.deref(container_slug),cljs.core.deref(sort_type));

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
oc.web.actions.activity.container_nav_out(cljs.core.deref(container_slug),cljs.core.deref(sort_type));

return s;
})], null);
});

//# sourceMappingURL=oc.web.mixins.seen.js.map
