goog.provide('oc.web.components.user_profile');
oc.web.components.user_profile.user_profile = rum.core.build_defc((function (user_data){
return React.createElement("div",({"className": "user-profile-container"}),React.createElement("div",({"className": "user-profile"}),React.createElement("div",({"className": "user-profile-header"}),(function (){var attrs39968 = (function (){var G__39969 = user_data;
var G__39970 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"preferred-avatar-size","preferred-avatar-size",498036456),(512)], null);
return (oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$2(G__39969,G__39970) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,G__39969,G__39970));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs39968))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-profile-header-avatar"], null)], null),attrs39968], 0))):({"className": "user-profile-header-avatar"})),((cljs.core.map_QMARK_(attrs39968))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs39968)], null)));
})(),React.createElement("div",({"className": "user-profile-header-info"}),React.createElement("div",({"className": "user-profile-header-info-name"}),(function (){var attrs39971 = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user_data);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"span",((cljs.core.map_QMARK_(attrs39971))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["name"], null)], null),attrs39971], 0))):({"className": "name"})),((cljs.core.map_QMARK_(attrs39971))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs39971)], null)));
})(),sablono.interpreter.interpret(((cuerdas.core.blank_QMARK_(new cljs.core.Keyword(null,"role-string","role-string",82910575).cljs$core$IFn$_invoke$arity$1(user_data)))?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.role","span.role",-1203287128),["(",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cuerdas.core.capital(new cljs.core.Keyword(null,"role-string","role-string",82910575).cljs$core$IFn$_invoke$arity$1(user_data))),")"].join('')], null))),sablono.interpreter.interpret((cljs.core.truth_(new cljs.core.Keyword(null,"self?","self?",-701815921).cljs$core$IFn$_invoke$arity$1(user_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.edit-profile-bt","button.mlb-reset.edit-profile-bt",-2030110074),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.nav_sidebar.show_user_settings(new cljs.core.Keyword(null,"profile","profile",-545963874));
})], null),"Edit profile"], null):null))),sablono.interpreter.interpret(((cljs.core.seq(new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(user_data)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-profile-header-info-title","div.user-profile-header-info-title",-1583380759),new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(user_data)], null):null)),sablono.interpreter.interpret(((cljs.core.seq(new cljs.core.Keyword(null,"blurb","blurb",-769928228).cljs$core$IFn$_invoke$arity$1(user_data)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-profile-header-info-blurb","div.user-profile-header-info-blurb",1638207481),new cljs.core.Keyword(null,"blurb","blurb",-769928228).cljs$core$IFn$_invoke$arity$1(user_data)], null):null)),(function (){var lts = oc.web.utils.user.location_timezone_string.cljs$core$IFn$_invoke$arity$variadic(user_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
return sablono.interpreter.interpret(((cljs.core.seq(lts))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-profile-header-info-locale","div.user-profile-header-info-locale",1671362845),lts], null):null));
})())),(function (){var has_profile_links_QMARK_ = cljs.core.seq(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.seq,cljs.core.vals(new cljs.core.Keyword(null,"profiles","profiles",507634713).cljs$core$IFn$_invoke$arity$1(user_data))));
return sablono.interpreter.interpret((cljs.core.truth_((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return ((cljs.core.seq(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(user_data))) || (has_profile_links_QMARK_));
}
})())?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-profile-footer","div.user-profile-footer",1546049099),(cljs.core.truth_(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.profile-link.email","a.profile-link.email",1191135934),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),["mailto:",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data))].join(''),new cljs.core.Keyword(null,"target","target",253001721),"_blank"], null),new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data)], null):null),(cljs.core.truth_(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(user_data))?(function (){var iter__4529__auto__ = (function oc$web$components$user_profile$iter__39974(s__39975){
return (new cljs.core.LazySeq(null,(function (){
var s__39975__$1 = s__39975;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__39975__$1);
if(temp__5735__auto__){
var s__39975__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__39975__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__39975__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__39977 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__39976 = (0);
while(true){
if((i__39976 < size__4528__auto__)){
var slack_user = cljs.core._nth(c__4527__auto__,i__39976);
if(((cljs.core.seq(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"-")))){
cljs.core.chunk_append(b__39977,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.profile-link.slack","a.profile-link.slack",-386790124),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"key","key",-1516042587),["profile-link-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_user)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(slack_user))].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__39976,s__39975__$1,slack_user,c__4527__auto__,size__4528__auto__,b__39977,s__39975__$2,temp__5735__auto__,has_profile_links_QMARK_){
return (function (p1__39967_SHARP_){
return oc.web.lib.utils.event_stop(p1__39967_SHARP_);
});})(i__39976,s__39975__$1,slack_user,c__4527__auto__,size__4528__auto__,b__39977,s__39975__$2,temp__5735__auto__,has_profile_links_QMARK_))
,new cljs.core.Keyword(null,"href","href",-793805698),"."], null),((cuerdas.core.starts_with_QMARK_(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"@"))?new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user):["@",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))].join(''))], null));

var G__39996 = (i__39976 + (1));
i__39976 = G__39996;
continue;
} else {
var G__39997 = (i__39976 + (1));
i__39976 = G__39997;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__39977),oc$web$components$user_profile$iter__39974(cljs.core.chunk_rest(s__39975__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__39977),null);
}
} else {
var slack_user = cljs.core.first(s__39975__$2);
if(((cljs.core.seq(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"-")))){
return cljs.core.cons(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.profile-link.slack","a.profile-link.slack",-386790124),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"key","key",-1516042587),["profile-link-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_user)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(slack_user))].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (s__39975__$1,slack_user,s__39975__$2,temp__5735__auto__,has_profile_links_QMARK_){
return (function (p1__39967_SHARP_){
return oc.web.lib.utils.event_stop(p1__39967_SHARP_);
});})(s__39975__$1,slack_user,s__39975__$2,temp__5735__auto__,has_profile_links_QMARK_))
,new cljs.core.Keyword(null,"href","href",-793805698),"."], null),((cuerdas.core.starts_with_QMARK_(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"@"))?new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user):["@",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))].join(''))], null),oc$web$components$user_profile$iter__39974(cljs.core.rest(s__39975__$2)));
} else {
var G__39998 = cljs.core.rest(s__39975__$2);
s__39975__$1 = G__39998;
continue;
}
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.vals(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(user_data)));
})():null),((has_profile_links_QMARK_)?(function (){var iter__4529__auto__ = (function oc$web$components$user_profile$iter__39978(s__39979){
return (new cljs.core.LazySeq(null,(function (){
var s__39979__$1 = s__39979;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__39979__$1);
if(temp__5735__auto__){
var s__39979__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__39979__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__39979__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__39981 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__39980 = (0);
while(true){
if((i__39980 < size__4528__auto__)){
var vec__39987 = cljs.core._nth(c__4527__auto__,i__39980);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__39987,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__39987,(1),null);
if((!(cuerdas.core.blank_QMARK_(v)))){
var un = cljs.core.last(cuerdas.core.split.cljs$core$IFn$_invoke$arity$2(v,/\//));
cljs.core.chunk_append(b__39981,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.profile-link","a.profile-link",-1627839971),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"class","class",-2030961996),cljs.core.name(k),new cljs.core.Keyword(null,"key","key",-1516042587),["profile-",cljs.core.name(k)].join(''),new cljs.core.Keyword(null,"target","target",253001721),"_blank",new cljs.core.Keyword(null,"href","href",-793805698),((((cuerdas.core.starts_with_QMARK_(v,"http")) || (cuerdas.core.starts_with_QMARK_(v,"//"))))?v:["https://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)].join(''))], null),un], null));

var G__39999 = (i__39980 + (1));
i__39980 = G__39999;
continue;
} else {
var G__40000 = (i__39980 + (1));
i__39980 = G__40000;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__39981),oc$web$components$user_profile$iter__39978(cljs.core.chunk_rest(s__39979__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__39981),null);
}
} else {
var vec__39993 = cljs.core.first(s__39979__$2);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__39993,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__39993,(1),null);
if((!(cuerdas.core.blank_QMARK_(v)))){
var un = cljs.core.last(cuerdas.core.split.cljs$core$IFn$_invoke$arity$2(v,/\//));
return cljs.core.cons(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.profile-link","a.profile-link",-1627839971),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"class","class",-2030961996),cljs.core.name(k),new cljs.core.Keyword(null,"key","key",-1516042587),["profile-",cljs.core.name(k)].join(''),new cljs.core.Keyword(null,"target","target",253001721),"_blank",new cljs.core.Keyword(null,"href","href",-793805698),((((cuerdas.core.starts_with_QMARK_(v,"http")) || (cuerdas.core.starts_with_QMARK_(v,"//"))))?v:["https://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)].join(''))], null),un], null),oc$web$components$user_profile$iter__39978(cljs.core.rest(s__39979__$2)));
} else {
var G__40001 = cljs.core.rest(s__39979__$2);
s__39979__$1 = G__40001;
continue;
}
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(new cljs.core.Keyword(null,"profiles","profiles",507634713).cljs$core$IFn$_invoke$arity$1(user_data));
})():null)], null):null));
})()));
}),null,"user-profile");

//# sourceMappingURL=oc.web.components.user_profile.js.map
