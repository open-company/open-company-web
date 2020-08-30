goog.provide('oc.web.components.ui.org_avatar');
oc.web.components.ui.org_avatar.default_max_logo_height = (96);
oc.web.components.ui.org_avatar.internal_org_avatar = (function oc$web$components$ui$org_avatar$internal_org_avatar(s,org_data,show_org_avatar_QMARK_,show_org_name_QMARK_){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.org-avatar-container.group","div.org-avatar-container.group",1980692941),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"no-avatar","no-avatar",2004989286),cljs.core.not(show_org_avatar_QMARK_)], null)),new cljs.core.Keyword(null,"data-first-letter","data-first-letter",-1963319651),cljs.core.first(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_data))], null),(cljs.core.truth_(show_org_avatar_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img.org-avatar-img","img.org-avatar-img",1526551896),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"src","src",-1651076051),oc.web.images.optimize_org_avatar_url(new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.components.ui.org_avatar.default_max_logo_height),new cljs.core.Keyword(null,"on-error","on-error",1728533530),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.org-avatar","img-load-failed","oc.web.components.ui.org-avatar/img-load-failed",161421832).cljs$core$IFn$_invoke$arity$1(s),true);
})], null)], null):null),(cljs.core.truth_(show_org_name_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.org-name","span.org-name",-1128111694),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"dangerouslySetInnerHTML","dangerouslySetInnerHTML",-554971138),oc.web.lib.utils.emojify(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_data))], null)], null):null)], null);
});
/**
 * Org avatar component, params:
 * - should-show-link: add anchor tag around the avatar linked to the company page
 * - show-org-name: possible values:
 *     * :always
 *     * :never
 *     * :auto (default)
 *    auto means that it's shown if the org logo is empty.
 */
oc.web.components.ui.org_avatar.org_avatar = rum.core.build_defcs((function() { 
var G__39572__delegate = function (s,org_data,should_show_link,p__39567){
var vec__39568 = p__39567;
var show_org_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__39568,(0),null);
var org_logo = new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(org_data);
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["org-avatar",((clojure.string.blank_QMARK_(org_logo))?"missing-logo":null)], null))}),sablono.interpreter.interpret((cljs.core.truth_(org_data)?(function (){var org_slug = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data);
var has_name = cljs.core.seq(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_data));
var org_name = ((has_name)?new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_data):oc.web.lib.utils.camel_case_str(org_slug));
var img_load_failed = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.org-avatar","img-load-failed","oc.web.components.ui.org-avatar/img-load-failed",161421832).cljs$core$IFn$_invoke$arity$1(s));
var show_org_avatar_QMARK_ = ((cljs.core.not(img_load_failed)) && ((!(clojure.string.blank_QMARK_(org_logo)))));
var show_org_name_QMARK_ = (function (){var G__39571 = show_org_name;
var G__39571__$1 = (((G__39571 instanceof cljs.core.Keyword))?G__39571.fqn:null);
switch (G__39571__$1) {
case "always":
return true;

break;
case "never":
return false;

break;
default:
return (!(show_org_avatar_QMARK_));

}
})();
var avatar_link = (cljs.core.truth_(should_show_link)?oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(org_slug):null);
if(cljs.core.truth_(should_show_link)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.org-link","a.org-link",1208801200),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"href","href",-793805698),avatar_link], null),oc.web.components.ui.org_avatar.internal_org_avatar(s,org_data,show_org_avatar_QMARK_,show_org_name_QMARK_)], null);
} else {
return oc.web.components.ui.org_avatar.internal_org_avatar(s,org_data,show_org_avatar_QMARK_,show_org_name_QMARK_);
}
})():null)));
};
var G__39572 = function (s,org_data,should_show_link,var_args){
var p__39567 = null;
if (arguments.length > 3) {
var G__39574__i = 0, G__39574__a = new Array(arguments.length -  3);
while (G__39574__i < G__39574__a.length) {G__39574__a[G__39574__i] = arguments[G__39574__i + 3]; ++G__39574__i;}
  p__39567 = new cljs.core.IndexedSeq(G__39574__a,0,null);
} 
return G__39572__delegate.call(this,s,org_data,should_show_link,p__39567);};
G__39572.cljs$lang$maxFixedArity = 3;
G__39572.cljs$lang$applyTo = (function (arglist__39575){
var s = cljs.core.first(arglist__39575);
arglist__39575 = cljs.core.next(arglist__39575);
var org_data = cljs.core.first(arglist__39575);
arglist__39575 = cljs.core.next(arglist__39575);
var should_show_link = cljs.core.first(arglist__39575);
var p__39567 = cljs.core.rest(arglist__39575);
return G__39572__delegate(s,org_data,should_show_link,p__39567);
});
G__39572.cljs$core$IFn$_invoke$arity$variadic = G__39572__delegate;
return G__39572;
})()
,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.org-avatar","img-load-failed","oc.web.components.ui.org-avatar/img-load-failed",161421832))], null),"org-avatar");

//# sourceMappingURL=oc.web.components.ui.org_avatar.js.map
