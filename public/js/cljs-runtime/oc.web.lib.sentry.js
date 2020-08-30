goog.provide('oc.web.lib.sentry');
var module$node_modules$$sentry$browser$dist$index=shadow.js.require("module$node_modules$$sentry$browser$dist$index", {});
oc.web.lib.sentry.init_parameters = (function oc$web$lib$sentry$init_parameters(dsn){
return new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"whitelistUrls","whitelistUrls",-645686980),oc.web.local_settings.local_whitelist_array,new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"isMobile","isMobile",-2043133877),oc.web.lib.responsive.is_mobile_size_QMARK_(),new cljs.core.Keyword(null,"hasJWT","hasJWT",-1851423344),(!(cljs.core.not(oc.web.lib.jwt.jwt())))], null),new cljs.core.Keyword(null,"sourceRoot","sourceRoot",-1175172830),oc.web.local_settings.web_server,new cljs.core.Keyword(null,"release","release",-1534371381),oc.web.local_settings.deploy_key,new cljs.core.Keyword(null,"debug","debug",-1608172596),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.local_settings.log_level,"debug"),new cljs.core.Keyword(null,"dsn","dsn",1561266567),dsn], null);
});
oc.web.lib.sentry.sentry_setup = (function oc$web$lib$sentry$sentry_setup(){
if(cljs.core.truth_((((typeof js !== 'undefined') && (typeof js.module$node_modules$$sentry$browser$dist$index !== 'undefined'))?oc.web.local_settings.local_dsn:false))){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.lib.sentry",null,19,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Setup Sentry"], null);
}),null)),null,987835486);

var sentry_params = oc.web.lib.sentry.init_parameters(oc.web.local_settings.local_dsn);
module$node_modules$$sentry$browser$dist$index.init(cljs.core.clj__GT_js(sentry_params));

taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.lib.sentry",null,22,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sentry params:",sentry_params], null);
}),null)),null,-1023117733);

return module$node_modules$$sentry$browser$dist$index.configureScope((function (scope){
scope.setTag("isMobile",oc.web.lib.responsive.is_mobile_size_QMARK_());

scope.setTag("hasJWT",(!(cljs.core.not(oc.web.lib.jwt.jwt()))));

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.lib.sentry",null,27,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Set Sentry user:",oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"user-id","user-id",-206822291))], null);
}),null)),null,1901948007);

return scope.setUser(cljs.core.clj__GT_js(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"user-id","user-id",-206822291)),new cljs.core.Keyword(null,"id","id",-1388402092),oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"user-id","user-id",-206822291)),new cljs.core.Keyword(null,"first-name","first-name",-1559982131),oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"first-name","first-name",-1559982131)),new cljs.core.Keyword(null,"last-name","last-name",-1695738974),oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"last-name","last-name",-1695738974))], null)));
} else {
return null;
}
}));
} else {
return null;
}
});
oc.web.lib.sentry.custom_error = (function oc$web$lib$sentry$custom_error(error_name,error_message){
var err = (new Error(error_message));
(err.name = error_name);

return err;
});
oc.web.lib.sentry.capture_error_BANG_ = (function oc$web$lib$sentry$capture_error_BANG_(var_args){
var G__41074 = arguments.length;
switch (G__41074) {
case 1:
return oc.web.lib.sentry.capture_error_BANG_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.lib.sentry.capture_error_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.lib.sentry.capture_error_BANG_.cljs$core$IFn$_invoke$arity$1 = (function (e){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.lib.sentry",null,40,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Capture error:",e], null);
}),null)),null,1628431933);

return module$node_modules$$sentry$browser$dist$index.captureException(e);
}));

(oc.web.lib.sentry.capture_error_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (e,error_info){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.lib.sentry",null,43,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Capture error:",e,"extra:",error_info], null);
}),null)),null,1496179757);

return module$node_modules$$sentry$browser$dist$index.captureException(e,({"extra": error_info}));
}));

(oc.web.lib.sentry.capture_error_BANG_.cljs$lang$maxFixedArity = 2);

oc.web.lib.sentry.capture_message_BANG_ = (function oc$web$lib$sentry$capture_message_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41146 = arguments.length;
var i__4737__auto___41147 = (0);
while(true){
if((i__4737__auto___41147 < len__4736__auto___41146)){
args__4742__auto__.push((arguments[i__4737__auto___41147]));

var G__41148 = (i__4737__auto___41147 + (1));
i__4737__auto___41147 = G__41148;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.sentry.capture_message_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.sentry.capture_message_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (msg,p__41085){
var vec__41086 = p__41085;
var log_level = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41086,(0),null);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.lib.sentry",null,47,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Capture message:",msg], null);
}),null)),null,2090708160);

return module$node_modules$$sentry$browser$dist$index.captureMessage(msg,(function (){var or__4126__auto__ = log_level;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "info";
}
})());
}));

(oc.web.lib.sentry.capture_message_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.sentry.capture_message_BANG_.cljs$lang$applyTo = (function (seq41081){
var G__41082 = cljs.core.first(seq41081);
var seq41081__$1 = cljs.core.next(seq41081);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41082,seq41081__$1);
}));

oc.web.lib.sentry.test_sentry = (function oc$web$lib$sentry$test_sentry(){
setTimeout((function (){
return oc.web.lib.sentry.capture_message_BANG_("Message from clojure");
}),(1000));

try{return (new errorThrowingCode());
}catch (e41094){var e = e41094;
return oc.web.lib.sentry.capture_error_BANG_.cljs$core$IFn$_invoke$arity$1(e);
}});
goog.exportSymbol('oc.web.lib.sentry.test_sentry', oc.web.lib.sentry.test_sentry);
oc.web.lib.sentry.set_extra_context_BANG_ = (function oc$web$lib$sentry$set_extra_context_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41151 = arguments.length;
var i__4737__auto___41152 = (0);
while(true){
if((i__4737__auto___41152 < len__4736__auto___41151)){
args__4742__auto__.push((arguments[i__4737__auto___41152]));

var G__41154 = (i__4737__auto___41152 + (1));
i__4737__auto___41152 = G__41154;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.lib.sentry.set_extra_context_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.lib.sentry.set_extra_context_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (scope,ctx,p__41105){
var vec__41106 = p__41105;
var prefix = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41106,(0),null);
var seq__41109 = cljs.core.seq(cljs.core.keys(ctx));
var chunk__41110 = null;
var count__41111 = (0);
var i__41112 = (0);
while(true){
if((i__41112 < count__41111)){
var k = chunk__41110.cljs$core$IIndexed$_nth$arity$2(null,i__41112);
if(cljs.core.map_QMARK_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(ctx,k))){
oc.web.lib.sentry.set_extra_context_BANG_.cljs$core$IFn$_invoke$arity$variadic(scope,cljs.core.get.cljs$core$IFn$_invoke$arity$2(ctx,k),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([[cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix),((cljs.core.seq(prefix))?"|":null),cljs.core.name(k)].join('')], 0));
} else {
scope.setExtra([cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix),((cljs.core.seq(prefix))?"|":null),cljs.core.name(k)].join(''),cljs.core.get.cljs$core$IFn$_invoke$arity$2(ctx,k));
}


var G__41163 = seq__41109;
var G__41164 = chunk__41110;
var G__41165 = count__41111;
var G__41166 = (i__41112 + (1));
seq__41109 = G__41163;
chunk__41110 = G__41164;
count__41111 = G__41165;
i__41112 = G__41166;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__41109);
if(temp__5735__auto__){
var seq__41109__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__41109__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__41109__$1);
var G__41167 = cljs.core.chunk_rest(seq__41109__$1);
var G__41168 = c__4556__auto__;
var G__41169 = cljs.core.count(c__4556__auto__);
var G__41170 = (0);
seq__41109 = G__41167;
chunk__41110 = G__41168;
count__41111 = G__41169;
i__41112 = G__41170;
continue;
} else {
var k = cljs.core.first(seq__41109__$1);
if(cljs.core.map_QMARK_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(ctx,k))){
oc.web.lib.sentry.set_extra_context_BANG_.cljs$core$IFn$_invoke$arity$variadic(scope,cljs.core.get.cljs$core$IFn$_invoke$arity$2(ctx,k),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([[cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix),((cljs.core.seq(prefix))?"|":null),cljs.core.name(k)].join('')], 0));
} else {
scope.setExtra([cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix),((cljs.core.seq(prefix))?"|":null),cljs.core.name(k)].join(''),cljs.core.get.cljs$core$IFn$_invoke$arity$2(ctx,k));
}


var G__41171 = cljs.core.next(seq__41109__$1);
var G__41172 = null;
var G__41173 = (0);
var G__41174 = (0);
seq__41109 = G__41171;
chunk__41110 = G__41172;
count__41111 = G__41173;
i__41112 = G__41174;
continue;
}
} else {
return null;
}
}
break;
}
}));

(oc.web.lib.sentry.set_extra_context_BANG_.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.lib.sentry.set_extra_context_BANG_.cljs$lang$applyTo = (function (seq41095){
var G__41097 = cljs.core.first(seq41095);
var seq41095__$1 = cljs.core.next(seq41095);
var G__41098 = cljs.core.first(seq41095__$1);
var seq41095__$2 = cljs.core.next(seq41095__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41097,G__41098,seq41095__$2);
}));

oc.web.lib.sentry.capture_message_with_extra_context_BANG_ = (function oc$web$lib$sentry$capture_message_with_extra_context_BANG_(ctx,message){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.lib.sentry",null,64,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Capture message:",message,"with context:",ctx], null);
}),null)),null,12281533);

return module$node_modules$$sentry$browser$dist$index.withScope((function (scope){
oc.web.lib.sentry.set_extra_context_BANG_(scope,ctx);

return oc.web.lib.sentry.capture_message_BANG_(message);
}));
});
oc.web.lib.sentry.capture_error_with_extra_context_BANG_ = (function oc$web$lib$sentry$capture_error_with_extra_context_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41175 = arguments.length;
var i__4737__auto___41176 = (0);
while(true){
if((i__4737__auto___41176 < len__4736__auto___41175)){
args__4742__auto__.push((arguments[i__4737__auto___41176]));

var G__41177 = (i__4737__auto___41176 + (1));
i__4737__auto___41176 = G__41177;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.lib.sentry.capture_error_with_extra_context_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.lib.sentry.capture_error_with_extra_context_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (ctx,error_name,p__41126){
var vec__41127 = p__41126;
var error_message = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41127,(0),null);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.lib.sentry",null,70,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Capture error:",error_name,"message:",error_message,"with context:",ctx], null);
}),null)),null,323307501);

return module$node_modules$$sentry$browser$dist$index.withScope((function (scope){
oc.web.lib.sentry.set_extra_context_BANG_(scope,ctx);

try{throw oc.web.lib.sentry.custom_error((function (){var or__4126__auto__ = error_message;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return error_name;
}
})(),(cljs.core.truth_(error_message)?error_name:"Error"));
}catch (e41130){var e = e41130;
return oc.web.lib.sentry.capture_error_BANG_.cljs$core$IFn$_invoke$arity$1(e);
}}));
}));

(oc.web.lib.sentry.capture_error_with_extra_context_BANG_.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.lib.sentry.capture_error_with_extra_context_BANG_.cljs$lang$applyTo = (function (seq41123){
var G__41124 = cljs.core.first(seq41123);
var seq41123__$1 = cljs.core.next(seq41123);
var G__41125 = cljs.core.first(seq41123__$1);
var seq41123__$2 = cljs.core.next(seq41123__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41124,G__41125,seq41123__$2);
}));

oc.web.lib.sentry.capture_error_with_message = (function oc$web$lib$sentry$capture_error_with_message(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41178 = arguments.length;
var i__4737__auto___41179 = (0);
while(true){
if((i__4737__auto___41179 < len__4736__auto___41178)){
args__4742__auto__.push((arguments[i__4737__auto___41179]));

var G__41180 = (i__4737__auto___41179 + (1));
i__4737__auto___41179 = G__41180;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.sentry.capture_error_with_message.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});
goog.exportSymbol('oc.web.lib.sentry.capture_error_with_message', oc.web.lib.sentry.capture_error_with_message);

(oc.web.lib.sentry.capture_error_with_message.cljs$core$IFn$_invoke$arity$variadic = (function (error_name,p__41133){
var vec__41134 = p__41133;
var error_message = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41134,(0),null);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.lib.sentry",null,79,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Capture error:",error_name,"message:",error_message], null);
}),null)),null,1001745387);

try{throw oc.web.lib.sentry.custom_error((function (){var or__4126__auto__ = error_message;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return error_name;
}
})(),(cljs.core.truth_(error_message)?error_name:"Error"));
}catch (e41137){var e = e41137;
return oc.web.lib.sentry.capture_error_BANG_.cljs$core$IFn$_invoke$arity$1(e);
}}));

(oc.web.lib.sentry.capture_error_with_message.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.sentry.capture_error_with_message.cljs$lang$applyTo = (function (seq41131){
var G__41132 = cljs.core.first(seq41131);
var seq41131__$1 = cljs.core.next(seq41131);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41132,seq41131__$1);
}));


//# sourceMappingURL=oc.web.lib.sentry.js.map
