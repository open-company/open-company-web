goog.provide('oc.web.api');
oc.web.api.web_endpoint = oc.web.local_settings.web_server_domain;
oc.web.api.storage_endpoint = oc.web.local_settings.storage_server_domain;
oc.web.api.auth_endpoint = oc.web.local_settings.auth_server_domain;
oc.web.api.pay_endpoint = oc.web.local_settings.pay_server_domain;
oc.web.api.interaction_endpoint = oc.web.local_settings.interaction_server_domain;
oc.web.api.change_endpoint = oc.web.local_settings.change_server_domain;
oc.web.api.search_endpoint = oc.web.local_settings.search_server_domain;
oc.web.api.reminders_endpoint = oc.web.local_settings.reminder_server_domain;
/**
 * Given a link map or a link string return the relative href.
 * @param {...*} var_args
 */
oc.web.api.relative_href = (function() { 
var oc$web$api$relative_href__delegate = function (args__33705__auto__){
var ocr_41240 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41240)) && ((cljs.core.count(ocr_41240) === 1)))){
try{var ocr_41240_0__41246 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41240,(0));
if(cljs.core.map_QMARK_(ocr_41240_0__41246)){
var link_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41240,(0));
var G__41267 = new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(link_map);
return (oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$1 ? oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$1(G__41267) : oc.web.api.relative_href.call(null,G__41267));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41252){if((e41252 instanceof Error)){
var e__32662__auto__ = e41252;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_41240_0__41246 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41240,(0));
if(typeof ocr_41240_0__41246 === 'string'){
var href = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41240,(0));
var parsed_uri = goog.Uri.parse(href);
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(parsed_uri.getPath()),(cljs.core.truth_(parsed_uri.hasQuery())?["?",cljs.core.str.cljs$core$IFn$_invoke$arity$1(parsed_uri.getEncodedQuery())].join(''):null),(cljs.core.truth_(parsed_uri.hasFragment())?["#",cljs.core.str.cljs$core$IFn$_invoke$arity$1(parsed_uri.getFragment())].join(''):null)].join('');
} else {
throw cljs.core.match.backtrack;

}
}catch (e41253){if((e41253 instanceof Error)){
var e__32662__auto____$1 = e41253;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$1;
}
} else {
throw e41253;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41252;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41250){if((e41250 instanceof Error)){
var e__32662__auto__ = e41250;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41240)].join('')));
} else {
throw e__32662__auto__;
}
} else {
throw e41250;

}
}};
var oc$web$api$relative_href = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__41616__i = 0, G__41616__a = new Array(arguments.length -  0);
while (G__41616__i < G__41616__a.length) {G__41616__a[G__41616__i] = arguments[G__41616__i + 0]; ++G__41616__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__41616__a,0,null);
} 
return oc$web$api$relative_href__delegate.call(this,args__33705__auto__);};
oc$web$api$relative_href.cljs$lang$maxFixedArity = 0;
oc$web$api$relative_href.cljs$lang$applyTo = (function (arglist__41627){
var args__33705__auto__ = cljs.core.seq(arglist__41627);
return oc$web$api$relative_href__delegate(args__33705__auto__);
});
oc$web$api$relative_href.cljs$core$IFn$_invoke$arity$variadic = oc$web$api$relative_href__delegate;
return oc$web$api$relative_href;
})()
;
oc.web.api.content_type = (function oc$web$api$content_type(type){
return ["application/vnd.open-company.",cljs.core.str.cljs$core$IFn$_invoke$arity$1(type),".v1+json;charset=UTF-8"].join('');
});
oc.web.api.config_request = (function oc$web$api$config_request(jwt_refresh_hn,jwt_error_handler,network_error_hn){
oc.web.api.jwt_refresh_handler = jwt_refresh_hn;

oc.web.api.jwt_refresh_error_hn = jwt_error_handler;

return (
oc.web.api.network_error_handler = network_error_hn)
;
});
oc.web.api.complete_params = (function oc$web$api$complete_params(params){
var change_client_id = cljs.core.deref(oc.web.utils.ws_client_ids.change_client_id);
var interaction_client_id = cljs.core.deref(oc.web.utils.ws_client_ids.interaction_client_id);
var notify_client_id = cljs.core.deref(oc.web.utils.ws_client_ids.notify_client_id);
var with_client_ids = (function (){var G__41268 = params;
var G__41268__$1 = (cljs.core.truth_(change_client_id)?cljs.core.assoc_in(G__41268,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"headers","headers",-835030129),"OC-Change-Client-ID"], null),change_client_id):G__41268);
var G__41268__$2 = (cljs.core.truth_(interaction_client_id)?cljs.core.assoc_in(G__41268__$1,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"headers","headers",-835030129),"OC-Interaction-Client-ID"], null),interaction_client_id):G__41268__$1);
if(cljs.core.truth_(notify_client_id)){
return cljs.core.assoc_in(G__41268__$2,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"headers","headers",-835030129),"OC-Notify-Client-ID"], null),notify_client_id);
} else {
return G__41268__$2;
}
})();
var jwt_or_id_token = (function (){var or__4126__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.lib.jwt.id_token();
}
})();
if(cljs.core.truth_(jwt_or_id_token)){
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"with-credentials?","with-credentials?",-1773202222),false], null),with_client_ids], 0)),new cljs.core.Keyword(null,"headers","headers",-835030129),cljs.core.merge,new cljs.core.PersistentArrayMap(null, 1, ["Authorization",["Bearer ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(jwt_or_id_token)].join('')], null));
} else {
return with_client_ids;

}
});
oc.web.api.headers_for_link = (function oc$web$api$headers_for_link(link){
var acah_headers = ((((cljs.core.contains_QMARK_(link,new cljs.core.Keyword(null,"access-control-allow-headers","access-control-allow-headers",260618047))) && ((new cljs.core.Keyword(null,"access-control-allow-headers","access-control-allow-headers",260618047).cljs$core$IFn$_invoke$arity$1(link) == null))))?cljs.core.PersistentArrayMap.EMPTY:(cljs.core.truth_(((cljs.core.contains_QMARK_(link,new cljs.core.Keyword(null,"access-control-allow-headers","access-control-allow-headers",260618047)))?new cljs.core.Keyword(null,"access-control-allow-headers","access-control-allow-headers",260618047).cljs$core$IFn$_invoke$arity$1(link):false))?new cljs.core.PersistentArrayMap(null, 1, ["Access-Control-Allow-Headers",new cljs.core.Keyword(null,"access-control-allow-headers","access-control-allow-headers",260618047).cljs$core$IFn$_invoke$arity$1(link)], null):new cljs.core.PersistentArrayMap(null, 1, ["Access-Control-Allow-Headers","Content-Type, Authorization"], null)
));
var with_content_type = (cljs.core.truth_(new cljs.core.Keyword(null,"content-type","content-type",-508222634).cljs$core$IFn$_invoke$arity$1(link))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(acah_headers,"content-type",new cljs.core.Keyword(null,"content-type","content-type",-508222634).cljs$core$IFn$_invoke$arity$1(link)):acah_headers);
var with_accept = (cljs.core.truth_(new cljs.core.Keyword(null,"accept","accept",1874130431).cljs$core$IFn$_invoke$arity$1(link))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_content_type,"accept",new cljs.core.Keyword(null,"accept","accept",1874130431).cljs$core$IFn$_invoke$arity$1(link)):with_content_type);
return with_accept;
});
oc.web.api.method_for_link = (function oc$web$api$method_for_link(link){
var G__41275 = new cljs.core.Keyword(null,"method","method",55703592).cljs$core$IFn$_invoke$arity$1(link);
switch (G__41275) {
case "POST":
return cljs_http.client.post;

break;
case "PUT":
return cljs_http.client.put;

break;
case "PATCH":
return cljs_http.client.patch;

break;
case "DELETE":
return cljs_http.client.delete$;

break;
default:
return cljs_http.client.get;

}
});
oc.web.api.refresh_jwt = (function oc$web$api$refresh_jwt(refresh_link){
var refresh_url = ((cljs.core.map_QMARK_(refresh_link))?[oc.web.local_settings.auth_server_domain,cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([refresh_link], 0)))].join(''):refresh_link);
var headers = ((cljs.core.map_QMARK_(refresh_link))?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(refresh_link)], null):cljs.core.PersistentArrayMap.EMPTY);
var method = ((cljs.core.map_QMARK_(refresh_link))?oc.web.api.method_for_link(refresh_link):cljs_http.client.get);
var G__41277 = refresh_url;
var G__41278 = oc.web.api.complete_params(headers);
return (method.cljs$core$IFn$_invoke$arity$2 ? method.cljs$core$IFn$_invoke$arity$2(G__41277,G__41278) : method.call(null,G__41277,G__41278));
});
oc.web.api.jwt_refresh = (function oc$web$api$jwt_refresh(success_cb,error_cb){
var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_41305){
var state_val_41306 = (state_41305[(1)]);
if((state_val_41306 === (1))){
var inst_41281 = (state_41305[(7)]);
var inst_41281__$1 = oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"refresh-url","refresh-url",-1631098110));
var state_41305__$1 = (function (){var statearr_41307 = state_41305;
(statearr_41307[(7)] = inst_41281__$1);

return statearr_41307;
})();
if(cljs.core.truth_(inst_41281__$1)){
var statearr_41308_41725 = state_41305__$1;
(statearr_41308_41725[(1)] = (2));

} else {
var statearr_41311_41726 = state_41305__$1;
(statearr_41311_41726[(1)] = (3));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41306 === (2))){
var inst_41281 = (state_41305[(7)]);
var inst_41283 = oc.web.api.refresh_jwt(inst_41281);
var state_41305__$1 = state_41305;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_41305__$1,(5),inst_41283);
} else {
if((state_val_41306 === (3))){
var inst_41301 = (error_cb.cljs$core$IFn$_invoke$arity$0 ? error_cb.cljs$core$IFn$_invoke$arity$0() : error_cb.call(null));
var state_41305__$1 = state_41305;
var statearr_41313_41727 = state_41305__$1;
(statearr_41313_41727[(2)] = inst_41301);

(statearr_41313_41727[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41306 === (4))){
var inst_41303 = (state_41305[(2)]);
var state_41305__$1 = state_41305;
return cljs.core.async.impl.ioc_helpers.return_chan(state_41305__$1,inst_41303);
} else {
if((state_val_41306 === (5))){
var inst_41281 = (state_41305[(7)]);
var inst_41285 = (state_41305[(8)]);
var inst_41285__$1 = (state_41305[(2)]);
var inst_41287 = (function (){var temp__5733__auto__ = inst_41281;
var refresh_url = inst_41281;
var res = inst_41285__$1;
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["jwt-refresh",res], null);
});
})();
var inst_41288 = (new cljs.core.Delay(inst_41287,null));
var inst_41289 = taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.api",null,133,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),inst_41288,null,51956727);
var inst_41291 = new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(inst_41285__$1);
var state_41305__$1 = (function (){var statearr_41314 = state_41305;
(statearr_41314[(8)] = inst_41285__$1);

(statearr_41314[(9)] = inst_41289);

return statearr_41314;
})();
if(cljs.core.truth_(inst_41291)){
var statearr_41317_41728 = state_41305__$1;
(statearr_41317_41728[(1)] = (6));

} else {
var statearr_41318_41729 = state_41305__$1;
(statearr_41318_41729[(1)] = (7));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41306 === (6))){
var inst_41285 = (state_41305[(8)]);
var inst_41293 = new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(inst_41285);
var inst_41294 = (success_cb.cljs$core$IFn$_invoke$arity$1 ? success_cb.cljs$core$IFn$_invoke$arity$1(inst_41293) : success_cb.call(null,inst_41293));
var state_41305__$1 = state_41305;
var statearr_41320_41730 = state_41305__$1;
(statearr_41320_41730[(2)] = inst_41294);

(statearr_41320_41730[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41306 === (7))){
var inst_41296 = (error_cb.cljs$core$IFn$_invoke$arity$0 ? error_cb.cljs$core$IFn$_invoke$arity$0() : error_cb.call(null));
var state_41305__$1 = state_41305;
var statearr_41321_41731 = state_41305__$1;
(statearr_41321_41731[(2)] = inst_41296);

(statearr_41321_41731[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41306 === (8))){
var inst_41298 = (state_41305[(2)]);
var state_41305__$1 = state_41305;
var statearr_41322_41732 = state_41305__$1;
(statearr_41322_41732[(2)] = inst_41298);

(statearr_41322_41732[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
});
return (function() {
var oc$web$api$jwt_refresh_$_state_machine__27076__auto__ = null;
var oc$web$api$jwt_refresh_$_state_machine__27076__auto____0 = (function (){
var statearr_41323 = [null,null,null,null,null,null,null,null,null,null];
(statearr_41323[(0)] = oc$web$api$jwt_refresh_$_state_machine__27076__auto__);

(statearr_41323[(1)] = (1));

return statearr_41323;
});
var oc$web$api$jwt_refresh_$_state_machine__27076__auto____1 = (function (state_41305){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_41305);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e41324){var ex__27079__auto__ = e41324;
var statearr_41325_41733 = state_41305;
(statearr_41325_41733[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_41305[(4)]))){
var statearr_41326_41734 = state_41305;
(statearr_41326_41734[(1)] = cljs.core.first((state_41305[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__41735 = state_41305;
state_41305 = G__41735;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$api$jwt_refresh_$_state_machine__27076__auto__ = function(state_41305){
switch(arguments.length){
case 0:
return oc$web$api$jwt_refresh_$_state_machine__27076__auto____0.call(this);
case 1:
return oc$web$api$jwt_refresh_$_state_machine__27076__auto____1.call(this,state_41305);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$api$jwt_refresh_$_state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$api$jwt_refresh_$_state_machine__27076__auto____0;
oc$web$api$jwt_refresh_$_state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$api$jwt_refresh_$_state_machine__27076__auto____1;
return oc$web$api$jwt_refresh_$_state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_41327 = f__27168__auto__();
(statearr_41327[(6)] = c__27167__auto__);

return statearr_41327;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
});
oc.web.api.method_name = (function oc$web$api$method_name(method){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(method,cljs_http.client.delete$)){
return "DELETE";
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(method,cljs_http.client.get)){
return "GET";
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(method,cljs_http.client.head)){
return "HEAD";
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(method,cljs_http.client.jsonp)){
return "JSONP";
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(method,cljs_http.client.move)){
return "MOVE";
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(method,cljs_http.client.options)){
return "OPTIONS";
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(method,cljs_http.client.patch)){
return "PATCH";
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(method,cljs_http.client.post)){
return "POST";
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(method,cljs_http.client.put)){
return "PUT";
} else {
return null;
}
}
}
}
}
}
}
}
}
});
oc.web.api.req = (function oc$web$api$req(endpoint,method,path,params,on_complete){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.api",null,161,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Req:",oc.web.api.method_name(method),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(endpoint),cljs.core.str.cljs$core$IFn$_invoke$arity$1(path)].join('')], null);
}),null)),null,1666775328);

var jwt = oc.web.lib.jwt.jwt();
var expired_QMARK_ = oc.web.lib.jwt.expired_QMARK_();
var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_41457){
var state_val_41458 = (state_41457[(1)]);
if((state_val_41458 === (7))){
var inst_41359 = (state_41457[(2)]);
var inst_41361 = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(endpoint),cljs.core.str.cljs$core$IFn$_invoke$arity$1(path)].join('');
var inst_41362 = oc.web.api.complete_params(params);
var inst_41363 = (method.cljs$core$IFn$_invoke$arity$2 ? method.cljs$core$IFn$_invoke$arity$2(inst_41361,inst_41362) : method.call(null,inst_41361,inst_41362));
var state_41457__$1 = (function (){var statearr_41459 = state_41457;
(statearr_41459[(7)] = inst_41359);

return statearr_41459;
})();
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_41457__$1,(15),inst_41363);
} else {
if((state_val_41458 === (20))){
var state_41457__$1 = state_41457;
var statearr_41460_41751 = state_41457__$1;
(statearr_41460_41751[(2)] = false);

(statearr_41460_41751[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (27))){
var inst_41397 = (state_41457[(2)]);
var state_41457__$1 = state_41457;
if(cljs.core.truth_(inst_41397)){
var statearr_41461_41754 = state_41457__$1;
(statearr_41461_41754[(1)] = (28));

} else {
var statearr_41462_41755 = state_41457__$1;
(statearr_41462_41755[(1)] = (29));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (1))){
var inst_41328 = (state_41457[(8)]);
var inst_41328__$1 = jwt;
var state_41457__$1 = (function (){var statearr_41463 = state_41457;
(statearr_41463[(8)] = inst_41328__$1);

return statearr_41463;
})();
if(cljs.core.truth_(inst_41328__$1)){
var statearr_41464_41759 = state_41457__$1;
(statearr_41464_41759[(1)] = (2));

} else {
var statearr_41465_41760 = state_41457__$1;
(statearr_41465_41760[(1)] = (3));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (24))){
var inst_41387 = (state_41457[(9)]);
var inst_41386 = (state_41457[(10)]);
var inst_41392 = (state_41457[(11)]);
var inst_41388 = (state_41457[(12)]);
var inst_41386__$1 = (state_41457[(2)]);
var inst_41387__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_41386__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var inst_41388__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_41386__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var inst_41389 = (function (){var map__41360 = inst_41386__$1;
var response = inst_41386__$1;
var status = inst_41387__$1;
var body = inst_41388__$1;
return (function (){
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Resp:",oc.web.api.method_name(method),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(endpoint),cljs.core.str.cljs$core$IFn$_invoke$arity$1(path)].join(''),status,response], null);
});
})();
var inst_41390 = (new cljs.core.Delay(inst_41389,null));
var inst_41391 = taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.api",null,177,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),inst_41390,null,-1656075712);
var inst_41392__$1 = jwt;
var state_41457__$1 = (function (){var statearr_41467 = state_41457;
(statearr_41467[(9)] = inst_41387__$1);

(statearr_41467[(10)] = inst_41386__$1);

(statearr_41467[(11)] = inst_41392__$1);

(statearr_41467[(13)] = inst_41391);

(statearr_41467[(12)] = inst_41388__$1);

return statearr_41467;
})();
if(cljs.core.truth_(inst_41392__$1)){
var statearr_41468_41765 = state_41457__$1;
(statearr_41468_41765[(1)] = (25));

} else {
var statearr_41469_41767 = state_41457__$1;
(statearr_41469_41767[(1)] = (26));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (4))){
var inst_41332 = (state_41457[(2)]);
var state_41457__$1 = state_41457;
if(cljs.core.truth_(inst_41332)){
var statearr_41470_41769 = state_41457__$1;
(statearr_41470_41769[(1)] = (5));

} else {
var statearr_41471_41770 = state_41457__$1;
(statearr_41471_41770[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (15))){
var inst_41365 = (state_41457[(14)]);
var inst_41365__$1 = (state_41457[(2)]);
var inst_41367 = (inst_41365__$1 == null);
var inst_41368 = cljs.core.not(inst_41367);
var state_41457__$1 = (function (){var statearr_41472 = state_41457;
(statearr_41472[(14)] = inst_41365__$1);

return statearr_41472;
})();
if(inst_41368){
var statearr_41473_41772 = state_41457__$1;
(statearr_41473_41772[(1)] = (16));

} else {
var statearr_41474_41773 = state_41457__$1;
(statearr_41474_41773[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (21))){
var inst_41378 = (state_41457[(2)]);
var state_41457__$1 = state_41457;
var statearr_41475_41774 = state_41457__$1;
(statearr_41475_41774[(2)] = inst_41378);

(statearr_41475_41774[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (31))){
var inst_41409 = (oc.web.api.network_error_handler.cljs$core$IFn$_invoke$arity$0 ? oc.web.api.network_error_handler.cljs$core$IFn$_invoke$arity$0() : oc.web.api.network_error_handler.call(null));
var state_41457__$1 = state_41457;
var statearr_41476_41775 = state_41457__$1;
(statearr_41476_41775[(2)] = inst_41409);

(statearr_41476_41775[(1)] = (33));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (32))){
var state_41457__$1 = state_41457;
var statearr_41477_41778 = state_41457__$1;
(statearr_41477_41778[(2)] = null);

(statearr_41477_41778[(1)] = (33));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (33))){
var inst_41387 = (state_41457[(9)]);
var inst_41412 = (state_41457[(2)]);
var inst_41413 = ((500) <= inst_41387);
var inst_41414 = (inst_41387 <= (599));
var inst_41415 = ((inst_41413) && (inst_41414));
var inst_41416 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(inst_41387,(400));
var inst_41417 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(inst_41387,(422));
var inst_41418 = ((inst_41415) || (inst_41416) || (inst_41417));
var state_41457__$1 = (function (){var statearr_41478 = state_41457;
(statearr_41478[(15)] = inst_41412);

return statearr_41478;
})();
if(cljs.core.truth_(inst_41418)){
var statearr_41479_41780 = state_41457__$1;
(statearr_41479_41780[(1)] = (34));

} else {
var statearr_41480_41781 = state_41457__$1;
(statearr_41480_41781[(1)] = (35));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (13))){
var inst_41350 = (oc.web.api.jwt_refresh_error_hn.cljs$core$IFn$_invoke$arity$0 ? oc.web.api.jwt_refresh_error_hn.cljs$core$IFn$_invoke$arity$0() : oc.web.api.jwt_refresh_error_hn.call(null));
var state_41457__$1 = state_41457;
var statearr_41481_41783 = state_41457__$1;
(statearr_41481_41783[(2)] = inst_41350);

(statearr_41481_41783[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (22))){
var inst_41365 = (state_41457[(14)]);
var inst_41383 = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,inst_41365);
var state_41457__$1 = state_41457;
var statearr_41482_41784 = state_41457__$1;
(statearr_41482_41784[(2)] = inst_41383);

(statearr_41482_41784[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (36))){
var inst_41386 = (state_41457[(10)]);
var inst_41437 = (state_41457[(2)]);
var inst_41438 = (on_complete.cljs$core$IFn$_invoke$arity$1 ? on_complete.cljs$core$IFn$_invoke$arity$1(inst_41386) : on_complete.call(null,inst_41386));
var state_41457__$1 = (function (){var statearr_41483 = state_41457;
(statearr_41483[(16)] = inst_41437);

return statearr_41483;
})();
return cljs.core.async.impl.ioc_helpers.return_chan(state_41457__$1,inst_41438);
} else {
if((state_val_41458 === (29))){
var state_41457__$1 = state_41457;
var statearr_41484_41787 = state_41457__$1;
(statearr_41484_41787[(2)] = null);

(statearr_41484_41787[(1)] = (30));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (6))){
var state_41457__$1 = state_41457;
var statearr_41485_41790 = state_41457__$1;
(statearr_41485_41790[(2)] = null);

(statearr_41485_41790[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (28))){
var inst_41399 = oc.web.router.redirect_BANG_(oc.web.urls.logout);
var state_41457__$1 = state_41457;
var statearr_41486_41791 = state_41457__$1;
(statearr_41486_41791[(2)] = inst_41399);

(statearr_41486_41791[(1)] = (30));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (25))){
var inst_41387 = (state_41457[(9)]);
var inst_41394 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(inst_41387,(401));
var state_41457__$1 = state_41457;
var statearr_41487_41793 = state_41457__$1;
(statearr_41487_41793[(2)] = inst_41394);

(statearr_41487_41793[(1)] = (27));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (34))){
var inst_41387 = (state_41457[(9)]);
var inst_41386 = (state_41457[(10)]);
var inst_41388 = (state_41457[(12)]);
var inst_41420 = [new cljs.core.Keyword(null,"response","response",-1068424192),new cljs.core.Keyword(null,"path","path",-188191168),new cljs.core.Keyword(null,"method","method",55703592),new cljs.core.Keyword(null,"jwt","jwt",1504015441),new cljs.core.Keyword(null,"params","params",710516235),new cljs.core.Keyword(null,"sessionURL","sessionURL",-2099350313)];
var inst_41421 = oc.web.api.method_name(method);
var inst_41422 = oc.web.lib.jwt.jwt();
var inst_41423 = oc.web.lib.fullstory.session_url();
var inst_41424 = [inst_41386,path,inst_41421,inst_41422,params,inst_41423];
var inst_41425 = cljs.core.PersistentHashMap.fromArrays(inst_41420,inst_41424);
var inst_41426 = (function (){var map__41360 = inst_41386;
var response = inst_41386;
var status = inst_41387;
var body = inst_41388;
var report = inst_41425;
return (function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, ["xhr response error:",oc.web.api.method_name(method),":",[cljs.core.str.cljs$core$IFn$_invoke$arity$1(endpoint),cljs.core.str.cljs$core$IFn$_invoke$arity$1(path)].join('')," -> ",status], null);
});
})();
var inst_41427 = (new cljs.core.Delay(inst_41426,null));
var inst_41428 = taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"error","error",-978969032),"oc.web.api",null,198,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),inst_41427,null,-24235311);
var inst_41429 = ["xhr response error:",cljs.core.str.cljs$core$IFn$_invoke$arity$1(inst_41387)].join('');
var inst_41434 = oc.web.lib.sentry.capture_error_with_extra_context_BANG_(inst_41425,inst_41429);
var state_41457__$1 = (function (){var statearr_41488 = state_41457;
(statearr_41488[(17)] = inst_41428);

return statearr_41488;
})();
var statearr_41489_41799 = state_41457__$1;
(statearr_41489_41799[(2)] = inst_41434);

(statearr_41489_41799[(1)] = (36));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (17))){
var state_41457__$1 = state_41457;
var statearr_41490_41800 = state_41457__$1;
(statearr_41490_41800[(2)] = false);

(statearr_41490_41800[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (3))){
var inst_41328 = (state_41457[(8)]);
var state_41457__$1 = state_41457;
var statearr_41491_41801 = state_41457__$1;
(statearr_41491_41801[(2)] = inst_41328);

(statearr_41491_41801[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (12))){
var inst_41338 = (state_41457[(18)]);
var inst_41347 = new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(inst_41338);
var inst_41348 = (oc.web.api.jwt_refresh_handler.cljs$core$IFn$_invoke$arity$1 ? oc.web.api.jwt_refresh_handler.cljs$core$IFn$_invoke$arity$1(inst_41347) : oc.web.api.jwt_refresh_handler.call(null,inst_41347));
var state_41457__$1 = state_41457;
var statearr_41492_41802 = state_41457__$1;
(statearr_41492_41802[(2)] = inst_41348);

(statearr_41492_41802[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (2))){
var state_41457__$1 = state_41457;
var statearr_41493_41803 = state_41457__$1;
(statearr_41493_41803[(2)] = expired_QMARK_);

(statearr_41493_41803[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (23))){
var inst_41365 = (state_41457[(14)]);
var state_41457__$1 = state_41457;
var statearr_41494_41804 = state_41457__$1;
(statearr_41494_41804[(2)] = inst_41365);

(statearr_41494_41804[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (35))){
var state_41457__$1 = state_41457;
var statearr_41497_41805 = state_41457__$1;
(statearr_41497_41805[(2)] = null);

(statearr_41497_41805[(1)] = (36));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (19))){
var state_41457__$1 = state_41457;
var statearr_41498_41806 = state_41457__$1;
(statearr_41498_41806[(2)] = true);

(statearr_41498_41806[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (11))){
var inst_41338 = (state_41457[(18)]);
var inst_41334 = (state_41457[(19)]);
var inst_41338__$1 = (state_41457[(2)]);
var inst_41341 = (function (){var temp__5733__auto__ = inst_41334;
var refresh_url = inst_41334;
var res = inst_41338__$1;
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["jwt-refresh",res], null);
});
})();
var inst_41342 = (new cljs.core.Delay(inst_41341,null));
var inst_41343 = taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.api",null,169,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),inst_41342,null,-44808900);
var inst_41345 = new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(inst_41338__$1);
var state_41457__$1 = (function (){var statearr_41499 = state_41457;
(statearr_41499[(18)] = inst_41338__$1);

(statearr_41499[(20)] = inst_41343);

return statearr_41499;
})();
if(cljs.core.truth_(inst_41345)){
var statearr_41500_41807 = state_41457__$1;
(statearr_41500_41807[(1)] = (12));

} else {
var statearr_41501_41808 = state_41457__$1;
(statearr_41501_41808[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (9))){
var inst_41354 = (oc.web.api.jwt_refresh_error_hn.cljs$core$IFn$_invoke$arity$0 ? oc.web.api.jwt_refresh_error_hn.cljs$core$IFn$_invoke$arity$0() : oc.web.api.jwt_refresh_error_hn.call(null));
var state_41457__$1 = state_41457;
var statearr_41505_41809 = state_41457__$1;
(statearr_41505_41809[(2)] = inst_41354);

(statearr_41505_41809[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (5))){
var inst_41334 = (state_41457[(19)]);
var inst_41334__$1 = oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"refresh-url","refresh-url",-1631098110));
var state_41457__$1 = (function (){var statearr_41506 = state_41457;
(statearr_41506[(19)] = inst_41334__$1);

return statearr_41506;
})();
if(cljs.core.truth_(inst_41334__$1)){
var statearr_41507_41810 = state_41457__$1;
(statearr_41507_41810[(1)] = (8));

} else {
var statearr_41508_41811 = state_41457__$1;
(statearr_41508_41811[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (14))){
var inst_41352 = (state_41457[(2)]);
var state_41457__$1 = state_41457;
var statearr_41509_41812 = state_41457__$1;
(statearr_41509_41812[(2)] = inst_41352);

(statearr_41509_41812[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (26))){
var inst_41392 = (state_41457[(11)]);
var state_41457__$1 = state_41457;
var statearr_41510_41813 = state_41457__$1;
(statearr_41510_41813[(2)] = inst_41392);

(statearr_41510_41813[(1)] = (27));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (16))){
var inst_41365 = (state_41457[(14)]);
var inst_41370 = inst_41365.cljs$lang$protocol_mask$partition0$;
var inst_41371 = (inst_41370 & (64));
var inst_41372 = inst_41365.cljs$core$ISeq$;
var inst_41373 = (cljs.core.PROTOCOL_SENTINEL === inst_41372);
var inst_41374 = ((inst_41371) || (inst_41373));
var state_41457__$1 = state_41457;
if(cljs.core.truth_(inst_41374)){
var statearr_41511_41814 = state_41457__$1;
(statearr_41511_41814[(1)] = (19));

} else {
var statearr_41512_41815 = state_41457__$1;
(statearr_41512_41815[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (30))){
var inst_41387 = (state_41457[(9)]);
var inst_41402 = (state_41457[(2)]);
var inst_41403 = (inst_41387 === (0));
var inst_41404 = ((500) <= inst_41387);
var inst_41405 = (inst_41387 <= (599));
var inst_41406 = ((inst_41404) && (inst_41405));
var inst_41407 = ((inst_41403) || (inst_41406));
var state_41457__$1 = (function (){var statearr_41513 = state_41457;
(statearr_41513[(21)] = inst_41402);

return statearr_41513;
})();
if(cljs.core.truth_(inst_41407)){
var statearr_41514_41816 = state_41457__$1;
(statearr_41514_41816[(1)] = (31));

} else {
var statearr_41515_41817 = state_41457__$1;
(statearr_41515_41817[(1)] = (32));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (10))){
var inst_41356 = (state_41457[(2)]);
var state_41457__$1 = state_41457;
var statearr_41516_41818 = state_41457__$1;
(statearr_41516_41818[(2)] = inst_41356);

(statearr_41516_41818[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (18))){
var inst_41381 = (state_41457[(2)]);
var state_41457__$1 = state_41457;
if(cljs.core.truth_(inst_41381)){
var statearr_41520_41819 = state_41457__$1;
(statearr_41520_41819[(1)] = (22));

} else {
var statearr_41521_41820 = state_41457__$1;
(statearr_41521_41820[(1)] = (23));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_41458 === (8))){
var inst_41334 = (state_41457[(19)]);
var inst_41336 = oc.web.api.refresh_jwt(inst_41334);
var state_41457__$1 = state_41457;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_41457__$1,(11),inst_41336);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var oc$web$api$req_$_state_machine__27076__auto__ = null;
var oc$web$api$req_$_state_machine__27076__auto____0 = (function (){
var statearr_41522 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_41522[(0)] = oc$web$api$req_$_state_machine__27076__auto__);

(statearr_41522[(1)] = (1));

return statearr_41522;
});
var oc$web$api$req_$_state_machine__27076__auto____1 = (function (state_41457){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_41457);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e41523){var ex__27079__auto__ = e41523;
var statearr_41524_41834 = state_41457;
(statearr_41524_41834[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_41457[(4)]))){
var statearr_41525_41835 = state_41457;
(statearr_41525_41835[(1)] = cljs.core.first((state_41457[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__41836 = state_41457;
state_41457 = G__41836;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$api$req_$_state_machine__27076__auto__ = function(state_41457){
switch(arguments.length){
case 0:
return oc$web$api$req_$_state_machine__27076__auto____0.call(this);
case 1:
return oc$web$api$req_$_state_machine__27076__auto____1.call(this,state_41457);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$api$req_$_state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$api$req_$_state_machine__27076__auto____0;
oc$web$api$req_$_state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$api$req_$_state_machine__27076__auto____1;
return oc$web$api$req_$_state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_41529 = f__27168__auto__();
(statearr_41529[(6)] = c__27167__auto__);

return statearr_41529;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
});
oc.web.api.web_http = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.api.req,oc.web.api.web_endpoint);
oc.web.api.storage_http = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.api.req,oc.web.api.storage_endpoint);
oc.web.api.auth_http = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.api.req,oc.web.api.auth_endpoint);
oc.web.api.pay_http = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.api.req,oc.web.api.pay_endpoint);
oc.web.api.interaction_http = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.api.req,oc.web.api.interaction_endpoint);
oc.web.api.change_http = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.api.req,oc.web.api.change_endpoint);
oc.web.api.search_http = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.api.req,oc.web.api.search_endpoint);
oc.web.api.reminders_http = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.api.req,oc.web.api.reminders_endpoint);
oc.web.api.handle_missing_link = (function oc$web$api$handle_missing_link(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41837 = arguments.length;
var i__4737__auto___41838 = (0);
while(true){
if((i__4737__auto___41838 < len__4736__auto___41837)){
args__4742__auto__.push((arguments[i__4737__auto___41838]));

var G__41839 = (i__4737__auto___41838 + (1));
i__4737__auto___41838 = G__41839;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((3) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((3)),(0),null)):null);
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),argseq__4743__auto__);
});

(oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic = (function (callee_name,link,callback,p__41534){
var vec__41535 = p__41534;
var parameters = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41535,(0),null);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"error","error",-978969032),"oc.web.api",null,221,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Handling missing link:",callee_name,":",link], null);
}),null)),null,1786995426);

oc.web.lib.sentry.capture_message_with_extra_context_BANG_(cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"callee","callee",-156445786),callee_name,new cljs.core.Keyword(null,"link","link",-1769163468),link,new cljs.core.Keyword(null,"sessionURL","sessionURL",-2099350313),oc.web.lib.fullstory.session_url()], null),parameters], 0)),["Client API error on: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(callee_name)].join(''));

oc.web.actions.notifications.show_notification(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.lib.utils.internal_error,new cljs.core.Keyword(null,"expire","expire",-70657108),(5)));

if(cljs.core.fn_QMARK_(callback)){
var G__41538 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"success","success",1890645906),false,new cljs.core.Keyword(null,"status","status",-1997798413),(0)], null);
return (callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(G__41538) : callback.call(null,G__41538));
} else {
return null;
}
}));

(oc.web.api.handle_missing_link.cljs$lang$maxFixedArity = (3));

/** @this {Function} */
(oc.web.api.handle_missing_link.cljs$lang$applyTo = (function (seq41530){
var G__41531 = cljs.core.first(seq41530);
var seq41530__$1 = cljs.core.next(seq41530);
var G__41532 = cljs.core.first(seq41530__$1);
var seq41530__$2 = cljs.core.next(seq41530__$1);
var G__41533 = cljs.core.first(seq41530__$2);
var seq41530__$3 = cljs.core.next(seq41530__$2);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41531,G__41532,G__41533,seq41530__$3);
}));

oc.web.api.org_allowed_keys = new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032),new cljs.core.Keyword(null,"logo-width","logo-width",-4247589),new cljs.core.Keyword(null,"logo-height","logo-height",-2066989379),new cljs.core.Keyword(null,"content-visibility","content-visibility",550828155),new cljs.core.Keyword(null,"why-carrot","why-carrot",-60713555),new cljs.core.Keyword(null,"utm-data","utm-data",-1997478583)], null);
oc.web.api.entry_allowed_keys = new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"headline","headline",-157157727),new cljs.core.Keyword(null,"body","body",-2049205669),new cljs.core.Keyword(null,"attachments","attachments",-1535547830),new cljs.core.Keyword(null,"video-id","video-id",2132630536),new cljs.core.Keyword(null,"video-error","video-error",331887081),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"status","status",-1997798413),new cljs.core.Keyword(null,"must-see","must-see",-2009706697),new cljs.core.Keyword(null,"polls","polls",-580623582),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803)], null);
oc.web.api.board_allowed_keys = new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"access","access",2027349272),new cljs.core.Keyword(null,"slack-mirror","slack-mirror",-506844872),new cljs.core.Keyword(null,"viewers","viewers",-415894011),new cljs.core.Keyword(null,"authors","authors",2063018172),new cljs.core.Keyword(null,"private-notifications","private-notifications",2119502043),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"description","description",-1428560544)], null);
oc.web.api.user_allowed_keys = new cljs.core.PersistentVector(null, 14, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"first-name","first-name",-1559982131),new cljs.core.Keyword(null,"last-name","last-name",-1695738974),new cljs.core.Keyword(null,"password","password",417022471),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),new cljs.core.Keyword(null,"timezone","timezone",1831928099),new cljs.core.Keyword(null,"digest-medium","digest-medium",1562900172),new cljs.core.Keyword(null,"notification-medium","notification-medium",195200470),new cljs.core.Keyword(null,"reminder-medium","reminder-medium",679614122),new cljs.core.Keyword(null,"qsg-checklist","qsg-checklist",525807503),new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"location","location",1815599388),new cljs.core.Keyword(null,"blurb","blurb",-769928228),new cljs.core.Keyword(null,"profiles","profiles",507634713),new cljs.core.Keyword(null,"digest-delivery","digest-delivery",-1355287749)], null);
oc.web.api.reminder_allowed_keys = new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-uuid","org-uuid",1539092089),new cljs.core.Keyword(null,"headline","headline",-157157727),new cljs.core.Keyword(null,"assignee","assignee",-1242457026),new cljs.core.Keyword(null,"frequency","frequency",-1408891382),new cljs.core.Keyword(null,"period-occurrence","period-occurrence",-953948637),new cljs.core.Keyword(null,"week-occurrence","week-occurrence",-1563004993)], null);
oc.web.api.web_app_version_check = (function oc$web$api$web_app_version_check(callback){
return oc.web.api.web_http(cljs_http.client.get,["/version/version",oc.web.local_settings.deploy_key,".json"].join(''),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"heades","heades",2016709995),new cljs.core.PersistentArrayMap(null, 2, ["Access-Control-Allow-Headers","Content-Type","content-type","application/json"], null)], null),(function (p1__41540_SHARP_){
if(cljs.core.fn_QMARK_(callback)){
return (callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(p1__41540_SHARP_) : callback.call(null,p1__41540_SHARP_));
} else {
return null;
}
}));
});
oc.web.api.store_500_test = (function oc$web$api$store_500_test(with_response){
return oc.web.api.storage_http(cljs_http.client.get,(cljs.core.truth_(with_response)?"/---error-test---":"/---500-test---"),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 2, ["Access-Control-Allow-Headers","Content-Type","content-type","text/plain"], null)], null),(function (_){
return null;
}));
});
oc.web.api.auth_500_test = (function oc$web$api$auth_500_test(with_response){
return oc.web.api.auth_http(cljs_http.client.get,(cljs.core.truth_(with_response)?"/---error-test---":"/---500-test---"),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"content-type","content-type",-508222634),"text/plain"], null))], null),(function (_){
return null;
}));
});
oc.web.api.get_entry_point = (function oc$web$api$get_entry_point(requested_org,callback){
return oc.web.api.storage_http(cljs_http.client.get,"/",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"query-params","query-params",900640534),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"requested","requested",1992266651),requested_org], null)], null),(function (p__41543){
var map__41544 = p__41543;
var map__41544__$1 = (((((!((map__41544 == null))))?(((((map__41544.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41544.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41544):map__41544);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41544__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41544__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var fixed_body = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
return (callback.cljs$core$IFn$_invoke$arity$2 ? callback.cljs$core$IFn$_invoke$arity$2(success,fixed_body) : callback.call(null,success,fixed_body));
}));
});
oc.web.api.get_auth_settings = (function oc$web$api$get_auth_settings(callback){
var invite_token = oc.web.dispatcher.invite_token.cljs$core$IFn$_invoke$arity$0();
var default_headers = oc.web.api.headers_for_link(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"access-control-allow-headers","access-control-allow-headers",260618047),null,new cljs.core.Keyword(null,"content-type","content-type",-508222634),"application/json"], null));
var with_auth_token = (cljs.core.truth_(invite_token)?cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([default_headers,new cljs.core.PersistentArrayMap(null, 1, ["Authorization",["Bearer ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(invite_token)].join('')], null)], 0)):default_headers);
var header_options = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),with_auth_token], null);
return oc.web.api.auth_http(cljs_http.client.get,"/",header_options,(function (p__41548){
var map__41549 = p__41548;
var map__41549__$1 = (((((!((map__41549 == null))))?(((((map__41549.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41549.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41549):map__41549);
var response = map__41549__$1;
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41549__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41549__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41549__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var body__$1 = (cljs.core.truth_(success)?body:null);
return (callback.cljs$core$IFn$_invoke$arity$2 ? callback.cljs$core$IFn$_invoke$arity$2(body__$1,status) : callback.call(null,body__$1,status));
}));
});
oc.web.api.get_payments = (function oc$web$api$get_payments(payments_link,callback){
if(cljs.core.truth_(payments_link)){
return oc.web.api.auth_http(oc.web.api.method_for_link(payments_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([payments_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(payments_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("get-payments",payments_link,callback);
}
});
/**
 * Used for PUT, PATCH and DELETE of subscriptions. Adds json-params with {:plan-id plan-id}
 * only if a plan-id is passed.
 */
oc.web.api.update_plan_subscription = (function oc$web$api$update_plan_subscription(update_link,plan_id,callback){
if(cljs.core.truth_(update_link)){
var update_subscription_body = oc.web.lib.json.cljs__GT_json(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"plan-id","plan-id",-1166088535),plan_id], null));
var options_STAR_ = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(update_link)], null);
var options = (cljs.core.truth_(plan_id)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(options_STAR_,new cljs.core.Keyword(null,"json-params","json-params",-1112693596),update_subscription_body):options_STAR_);
return oc.web.api.auth_http(oc.web.api.method_for_link(update_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([update_link], 0)),options,callback);
} else {
return oc.web.api.handle_missing_link("update-plan-subscription",update_link,callback);
}
});
oc.web.api.get_checkout_session_id = (function oc$web$api$get_checkout_session_id(checkout_link,success_url,cancel_url,callback){
if(cljs.core.truth_(checkout_link)){
return oc.web.api.auth_http(oc.web.api.method_for_link(checkout_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([checkout_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(checkout_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"success-url","success-url",-133575939),success_url,new cljs.core.Keyword(null,"cancel-url","cancel-url",-430057382),cancel_url], null))], null),callback);
} else {
return oc.web.api.handle_missing_link("get-checkout-session-id",checkout_link,callback);
}
});
oc.web.api.get_org = (function oc$web$api$get_org(org_link,callback){
if(cljs.core.truth_(org_link)){
var params = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(org_link)], null);
return oc.web.api.storage_http(oc.web.api.method_for_link(org_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_link], 0)),params,callback);
} else {
return oc.web.api.handle_missing_link("get-org",org_link,callback);
}
});
oc.web.api.patch_org = (function oc$web$api$patch_org(org_patch_link,data,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = org_patch_link;
if(cljs.core.truth_(and__4115__auto__)){
return data;
} else {
return and__4115__auto__;
}
})())){
var org_data = cljs.core.select_keys(data,oc.web.api.org_allowed_keys);
var json_data = oc.web.lib.json.cljs__GT_json(org_data);
return oc.web.api.storage_http(oc.web.api.method_for_link(org_patch_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_patch_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"json-params","json-params",-1112693596),json_data,new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(org_patch_link)], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("patch-org",org_patch_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"data","data",-232669377),data], null)], 0));
}
});
oc.web.api.add_email_domain = (function oc$web$api$add_email_domain(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41875 = arguments.length;
var i__4737__auto___41876 = (0);
while(true){
if((i__4737__auto___41876 < len__4736__auto___41875)){
args__4742__auto__.push((arguments[i__4737__auto___41876]));

var G__41877 = (i__4737__auto___41876 + (1));
i__4737__auto___41876 = G__41877;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((4) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((4)),(0),null)):null);
return oc.web.api.add_email_domain.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),argseq__4743__auto__);
});

(oc.web.api.add_email_domain.cljs$core$IFn$_invoke$arity$variadic = (function (add_email_domain_link,domain,callback,team_data,p__41559){
var vec__41560 = p__41559;
var pre_flight = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41560,(0),null);
if(cljs.core.truth_((function (){var and__4115__auto__ = add_email_domain_link;
if(cljs.core.truth_(and__4115__auto__)){
return domain;
} else {
return and__4115__auto__;
}
})())){
var email_domain_payload = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"email-domain","email-domain",-768613335),domain], null);
var with_preflight = (cljs.core.truth_(pre_flight)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(email_domain_payload,new cljs.core.Keyword(null,"pre-flight","pre-flight",-531914038),true):email_domain_payload);
var json_data = oc.web.lib.json.cljs__GT_json(with_preflight);
return oc.web.api.auth_http(oc.web.api.method_for_link(add_email_domain_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([add_email_domain_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(add_email_domain_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),json_data], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("add-email-domain",add_email_domain_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"domain","domain",1847214937),domain,new cljs.core.Keyword(null,"team-data","team-data",-732020079),team_data,new cljs.core.Keyword(null,"pre-flight","pre-flight",-531914038),pre_flight], null)], 0));
}
}));

(oc.web.api.add_email_domain.cljs$lang$maxFixedArity = (4));

/** @this {Function} */
(oc.web.api.add_email_domain.cljs$lang$applyTo = (function (seq41554){
var G__41555 = cljs.core.first(seq41554);
var seq41554__$1 = cljs.core.next(seq41554);
var G__41556 = cljs.core.first(seq41554__$1);
var seq41554__$2 = cljs.core.next(seq41554__$1);
var G__41557 = cljs.core.first(seq41554__$2);
var seq41554__$3 = cljs.core.next(seq41554__$2);
var G__41558 = cljs.core.first(seq41554__$3);
var seq41554__$4 = cljs.core.next(seq41554__$3);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41555,G__41556,G__41557,G__41558,seq41554__$4);
}));

oc.web.api.create_org = (function oc$web$api$create_org(create_org_link,org_data,callback){
if(cljs.core.truth_(create_org_link)){
var team_id = cljs.core.first(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"teams","teams",1677714510)));
var fixed_org_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.select_keys(org_data,oc.web.api.org_allowed_keys),new cljs.core.Keyword(null,"team-id","team-id",-14505725),team_id);
if(cljs.core.truth_((function (){var and__4115__auto__ = fixed_org_data;
if(cljs.core.truth_(and__4115__auto__)){
return create_org_link;
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.storage_http(oc.web.api.method_for_link(create_org_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([create_org_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(create_org_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(fixed_org_data)], null),callback);
} else {
return null;
}
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("create-org",create_org_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"org-data","org-data",96720321),org_data], null)], 0));
}
});
oc.web.api.delete_samples = (function oc$web$api$delete_samples(delete_samples_link,callback){
if(cljs.core.truth_(delete_samples_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(delete_samples_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([delete_samples_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(delete_samples_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("delete-samples",delete_samples_link,callback);
}
});
oc.web.api.get_board = (function oc$web$api$get_board(board_link,callback){
if(cljs.core.truth_(board_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(board_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([board_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(board_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("get-board",board_link,callback);
}
});
oc.web.api.patch_board = (function oc$web$api$patch_board(board_patch_link,data,note,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = board_patch_link;
if(cljs.core.truth_(and__4115__auto__)){
return data;
} else {
return and__4115__auto__;
}
})())){
var board_data = cljs.core.select_keys(data,new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"slug","slug",2029314850),new cljs.core.Keyword(null,"access","access",2027349272),new cljs.core.Keyword(null,"slack-mirror","slack-mirror",-506844872),new cljs.core.Keyword(null,"authors","authors",2063018172),new cljs.core.Keyword(null,"viewers","viewers",-415894011),new cljs.core.Keyword(null,"private-notifications","private-notifications",2119502043),new cljs.core.Keyword(null,"description","description",-1428560544)], null));
var with_personal_note = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(board_data,new cljs.core.Keyword(null,"note","note",1426297904),note);
var json_data = oc.web.lib.json.cljs__GT_json(with_personal_note);
return oc.web.api.storage_http(oc.web.api.method_for_link(board_patch_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([board_patch_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"json-params","json-params",-1112693596),json_data,new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(board_patch_link)], null),(function (p__41563){
var map__41564 = p__41563;
var map__41564__$1 = (((((!((map__41564 == null))))?(((((map__41564.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41564.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41564):map__41564);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41564__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41564__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41564__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
return (callback.cljs$core$IFn$_invoke$arity$3 ? callback.cljs$core$IFn$_invoke$arity$3(success,body,status) : callback.call(null,success,body,status));
}));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("patch-board",board_patch_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"note","note",1426297904),note,new cljs.core.Keyword(null,"data","data",-232669377),data], null)], 0));
}
});
oc.web.api.create_board = (function oc$web$api$create_board(create_board_link,board_data,note,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = create_board_link;
if(cljs.core.truth_(and__4115__auto__)){
return board_data;
} else {
return and__4115__auto__;
}
})())){
var fixed_board_data = cljs.core.select_keys(board_data,oc.web.api.board_allowed_keys);
var fixed_entries = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__41566_SHARP_){
return oc.web.utils.poll.clean_polls(cljs.core.select_keys(p1__41566_SHARP_,cljs.core.conj.cljs$core$IFn$_invoke$arity$variadic(oc.web.api.entry_allowed_keys,new cljs.core.Keyword(null,"uuid","uuid",-2145095719),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"secure-uuid","secure-uuid",-1972075067)], 0))));
}),new cljs.core.Keyword(null,"entries","entries",-86943161).cljs$core$IFn$_invoke$arity$1(board_data));
var with_entries = (((cljs.core.count(fixed_entries) > (0)))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(fixed_board_data,new cljs.core.Keyword(null,"entries","entries",-86943161),fixed_entries):fixed_board_data);
var with_personal_note = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_entries,new cljs.core.Keyword(null,"note","note",1426297904),note);
if(cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(fixed_board_data);
if(cljs.core.truth_(and__4115__auto__)){
return create_board_link;
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.storage_http(oc.web.api.method_for_link(create_board_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([create_board_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(create_board_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(with_personal_note)], null),callback);
} else {
return null;
}
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("create-board",create_board_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"board-data","board-data",1372958925),board_data,new cljs.core.Keyword(null,"note","note",1426297904),note], null)], 0));
}
});
oc.web.api.pre_flight_section_check = (function oc$web$api$pre_flight_section_check(pre_flight_link,section_slug,section_name,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = pre_flight_link;
if(cljs.core.truth_(and__4115__auto__)){
return section_name;
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.storage_http(oc.web.api.method_for_link(pre_flight_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([pre_flight_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(pre_flight_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"name","name",1843675177),section_name,new cljs.core.Keyword(null,"exclude","exclude",-1230250334),cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [section_slug], null))),new cljs.core.Keyword(null,"pre-flight","pre-flight",-531914038),true], null))], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("pre-flight-section-check",pre_flight_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"section-slug","section-slug",-364299439),section_slug,new cljs.core.Keyword(null,"section-name","section-name",-1093746339),section_name], null)], 0));
}
});
oc.web.api.delete_board = (function oc$web$api$delete_board(delete_board_link,board_slug,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = delete_board_link;
if(cljs.core.truth_(and__4115__auto__)){
return board_slug;
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.storage_http(oc.web.api.method_for_link(delete_board_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([delete_board_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(delete_board_link)], null),(function (p__41567){
var map__41568 = p__41567;
var map__41568__$1 = (((((!((map__41568 == null))))?(((((map__41568.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41568.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41568):map__41568);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41568__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41568__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41568__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return (callback.cljs$core$IFn$_invoke$arity$3 ? callback.cljs$core$IFn$_invoke$arity$3(status,success,body) : callback.call(null,status,success,body));
}));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("delete-board",delete_board_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"board-slug","board-slug",99003663),board_slug], null)], 0));
}
});
oc.web.api.remove_user_from_private_board = (function oc$web$api$remove_user_from_private_board(remove_user_link,callback){
if(cljs.core.truth_(remove_user_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(remove_user_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([remove_user_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(remove_user_link)], null),(function (p__41570){
var map__41571 = p__41570;
var map__41571__$1 = (((((!((map__41571 == null))))?(((((map__41571.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41571.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41571):map__41571);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41571__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41571__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41571__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return (callback.cljs$core$IFn$_invoke$arity$3 ? callback.cljs$core$IFn$_invoke$arity$3(status,success,body) : callback.call(null,status,success,body));
}));
} else {
return oc.web.api.handle_missing_link("remove-user-from-private-board",remove_user_link,callback);
}
});
oc.web.api.get_all_posts = (function oc$web$api$get_all_posts(activity_link,callback){
if(cljs.core.truth_(activity_link)){
var href = oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([activity_link], 0));
return oc.web.api.storage_http(oc.web.api.method_for_link(activity_link),href,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(activity_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("get-all-posts",activity_link,callback);
}
});
oc.web.api.load_more_items = (function oc$web$api$load_more_items(more_link,direction,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = more_link;
if(cljs.core.truth_(and__4115__auto__)){
return direction;
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.storage_http(oc.web.api.method_for_link(more_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([more_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(more_link)], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("load-more-items",more_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"direction","direction",-633359395),direction], null)], 0));
}
});
oc.web.api.auth_with_email = (function oc$web$api$auth_with_email(auth_link,email,pswd,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = auth_link;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = email;
if(cljs.core.truth_(and__4115__auto____$1)){
return pswd;
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.auth_http(oc.web.api.method_for_link(auth_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([auth_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"basic-auth","basic-auth",-673163332),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"username","username",1605666410),email,new cljs.core.Keyword(null,"password","password",417022471),pswd], null),new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(auth_link)], null),(function (p__41573){
var map__41574 = p__41573;
var map__41574__$1 = (((((!((map__41574 == null))))?(((((map__41574.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41574.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41574):map__41574);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41574__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41574__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41574__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
return (callback.cljs$core$IFn$_invoke$arity$3 ? callback.cljs$core$IFn$_invoke$arity$3(success,body,status) : callback.call(null,success,body,status));
}));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("auth-with-email",auth_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"email","email",1415816706),email,new cljs.core.Keyword(null,"pswd","pswd",278786885),cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(cljs.core.count(pswd),"*"))], null)], 0));
}
});
oc.web.api.auth_with_token = (function oc$web$api$auth_with_token(auth_link,token,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = auth_link;
if(cljs.core.truth_(and__4115__auto__)){
return token;
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.auth_http(oc.web.api.method_for_link(auth_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([auth_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.api.headers_for_link(auth_link),new cljs.core.PersistentArrayMap(null, 2, ["Access-Control-Allow-Headers","Content-Type, Authorization","Authorization",["Bearer ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(token)].join('')], null)], 0))], null),(function (p__41576){
var map__41577 = p__41576;
var map__41577__$1 = (((((!((map__41577 == null))))?(((((map__41577.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41577.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41577):map__41577);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41577__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41577__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41577__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
return (callback.cljs$core$IFn$_invoke$arity$3 ? callback.cljs$core$IFn$_invoke$arity$3(success,body,status) : callback.call(null,success,body,status));
}));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("auth-with-token",auth_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"token","token",-1211463215),token], null)], 0));
}
});
oc.web.api.confirm_invitation = (function oc$web$api$confirm_invitation(auth_link,token,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = auth_link;
if(cljs.core.truth_(and__4115__auto__)){
return token;
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.auth_http(oc.web.api.method_for_link(auth_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([auth_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.api.headers_for_link(auth_link),new cljs.core.PersistentArrayMap(null, 2, ["Access-Control-Allow-Headers","Content-Type, Authorization","Authorization",["Bearer ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(token)].join('')], null)], 0))], null),(function (p__41579){
var map__41580 = p__41579;
var map__41580__$1 = (((((!((map__41580 == null))))?(((((map__41580.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41580.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41580):map__41580);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41580__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41580__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41580__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return oc.web.lib.utils.after((100),(function (){
return (callback.cljs$core$IFn$_invoke$arity$3 ? callback.cljs$core$IFn$_invoke$arity$3(status,body,success) : callback.call(null,status,body,success));
}));
}));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("confirm-invitation",auth_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"token","token",-1211463215),token], null)], 0));
}
});
oc.web.api.signup_with_email = (function oc$web$api$signup_with_email(auth_link,first_name,last_name,email,pswd,timezone,callback){
var invite_token = oc.web.dispatcher.invite_token.cljs$core$IFn$_invoke$arity$0();
var default_headers = oc.web.api.headers_for_link(auth_link);
var with_auth_token = (cljs.core.truth_(invite_token)?cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([default_headers,new cljs.core.PersistentArrayMap(null, 1, ["Authorization",["Bearer ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(invite_token)].join('')], null)], 0)):default_headers);
var user_map = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"first-name","first-name",-1559982131),first_name,new cljs.core.Keyword(null,"last-name","last-name",-1695738974),last_name,new cljs.core.Keyword(null,"email","email",1415816706),email,new cljs.core.Keyword(null,"password","password",417022471),pswd,new cljs.core.Keyword(null,"timezone","timezone",1831928099),timezone], null);
if(cljs.core.truth_((function (){var and__4115__auto__ = auth_link;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = first_name;
if(cljs.core.truth_(and__4115__auto____$1)){
var and__4115__auto____$2 = last_name;
if(cljs.core.truth_(and__4115__auto____$2)){
var and__4115__auto____$3 = email;
if(cljs.core.truth_(and__4115__auto____$3)){
return pswd;
} else {
return and__4115__auto____$3;
}
} else {
return and__4115__auto____$2;
}
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.auth_http(oc.web.api.method_for_link(auth_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([auth_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(user_map),new cljs.core.Keyword(null,"headers","headers",-835030129),with_auth_token], null),(function (p__41582){
var map__41583 = p__41582;
var map__41583__$1 = (((((!((map__41583 == null))))?(((((map__41583.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41583.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41583):map__41583);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41583__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41583__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41583__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
return (callback.cljs$core$IFn$_invoke$arity$3 ? callback.cljs$core$IFn$_invoke$arity$3(success,body,status) : callback.call(null,success,body,status));
}));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("signup-with-email",auth_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(user_map,new cljs.core.Keyword(null,"password","password",417022471),cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(cljs.core.count(pswd),"*")))], 0));
}
});
oc.web.api.get_teams = (function oc$web$api$get_teams(enumerate_link,callback){
if(cljs.core.truth_(enumerate_link)){
return oc.web.api.auth_http(oc.web.api.method_for_link(enumerate_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([enumerate_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(enumerate_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("get-teams",enumerate_link,callback);
}
});
oc.web.api.get_team = (function oc$web$api$get_team(team_link,callback){
if(cljs.core.truth_(team_link)){
return oc.web.api.auth_http(oc.web.api.method_for_link(team_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([team_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(team_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("get-team",team_link,callback);
}
});
oc.web.api.enumerate_channels = (function oc$web$api$enumerate_channels(enumerate_link,callback){
if(cljs.core.truth_(enumerate_link)){
return oc.web.api.auth_http(oc.web.api.method_for_link(enumerate_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([enumerate_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(enumerate_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("enumerate-channels",enumerate_link,callback);
}
});
oc.web.api.patch_team = (function oc$web$api$patch_team(team_patch_link,team_id,new_team_data,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = team_patch_link;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = team_id;
if(cljs.core.truth_(and__4115__auto____$1)){
return new_team_data;
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.auth_http(oc.web.api.method_for_link(team_patch_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([team_patch_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(team_patch_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(new_team_data)], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("patch-team",team_patch_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"team-id","team-id",-14505725),team_id,new cljs.core.Keyword(null,"new-team-data","new-team-data",-3022948),new_team_data], null)], 0));
}
});
/**
 * Give a user email and type of user send an invitation to the team.
 * If the team has only one company, checked via API entry point links, send the company name of that.
 * Add the company's logo and its size if possible.
 */
oc.web.api.send_invitation = (function oc$web$api$send_invitation(invitation_link,invited_user,invite_from,user_type,first_name,last_name,note,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = invitation_link;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = invited_user;
if(cljs.core.truth_(and__4115__auto____$1)){
var and__4115__auto____$2 = invite_from;
if(cljs.core.truth_(and__4115__auto____$2)){
return user_type;
} else {
return and__4115__auto____$2;
}
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var json_params = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"first-name","first-name",-1559982131),first_name,new cljs.core.Keyword(null,"last-name","last-name",-1695738974),last_name,new cljs.core.Keyword(null,"note","note",1426297904),note,new cljs.core.Keyword(null,"admin","admin",-1239101627),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_type,new cljs.core.Keyword(null,"admin","admin",-1239101627))], null);
var with_invited_user = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(invite_from,"slack"))?cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([json_params,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"slack-id","slack-id",862141985),new cljs.core.Keyword(null,"slack-id","slack-id",862141985).cljs$core$IFn$_invoke$arity$1(invited_user),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(invited_user),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(invited_user),new cljs.core.Keyword(null,"email","email",1415816706),new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(invited_user)], null)], 0)):cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(json_params,new cljs.core.Keyword(null,"email","email",1415816706),invited_user));
var with_company_name = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([with_invited_user,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"org-name","org-name",53378517),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032),new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.Keyword(null,"logo-width","logo-width",-4247589),new cljs.core.Keyword(null,"logo-width","logo-width",-4247589).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.Keyword(null,"logo-height","logo-height",-2066989379),new cljs.core.Keyword(null,"logo-height","logo-height",-2066989379).cljs$core$IFn$_invoke$arity$1(org_data)], null)], 0));
return oc.web.api.auth_http(oc.web.api.method_for_link(invitation_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([invitation_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(with_company_name),new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(invitation_link)], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("send-invitation",invitation_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"invited-user","invited-user",1296906518),invited_user,new cljs.core.Keyword(null,"invite-from","invite-from",-73333708),invite_from,new cljs.core.Keyword(null,"user-type","user-type",738868936),user_type,new cljs.core.Keyword(null,"first-name","first-name",-1559982131),first_name,new cljs.core.Keyword(null,"last-name","last-name",-1695738974),last_name,new cljs.core.Keyword(null,"note","note",1426297904),note], null)], 0));
}
});
oc.web.api.add_admin = (function oc$web$api$add_admin(add_admin_link,user,callback){
if(cljs.core.truth_(add_admin_link)){
return oc.web.api.auth_http(oc.web.api.method_for_link(add_admin_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([add_admin_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(add_admin_link)], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("add-admin",add_admin_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"user","user",1532431356),user], null)], 0));
}
});
oc.web.api.remove_admin = (function oc$web$api$remove_admin(remove_admin_link,user,callback){
if(cljs.core.truth_(remove_admin_link)){
return oc.web.api.auth_http(oc.web.api.method_for_link(remove_admin_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([remove_admin_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(remove_admin_link)], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("remove-admin",remove_admin_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"user","user",1532431356),user], null)], 0));
}
});
oc.web.api.handle_invite_link = (function oc$web$api$handle_invite_link(invite_token_link,callback){
if(cljs.core.truth_(invite_token_link)){
return oc.web.api.auth_http(oc.web.api.method_for_link(invite_token_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([invite_token_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(invite_token_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("handle-invite-link",invite_token_link,callback);
}
});
oc.web.api.get_active_users = (function oc$web$api$get_active_users(active_users_link,callback){
if(cljs.core.truth_(active_users_link)){
return oc.web.api.auth_http(oc.web.api.method_for_link(active_users_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([active_users_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(active_users_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("get-active-users",active_users_link,callback);
}
});
oc.web.api.user_action = (function oc$web$api$user_action(action_link,payload,callback){
if(cljs.core.truth_(action_link)){
var headers = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(action_link)], null);
var with_payload = (cljs.core.truth_(payload)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(headers,new cljs.core.Keyword(null,"json-params","json-params",-1112693596),payload):headers);
return oc.web.api.auth_http(oc.web.api.method_for_link(action_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([action_link], 0)),with_payload,callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("user-action",action_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"payload","payload",-383036092),payload], null)], 0));
}
});
oc.web.api.collect_password = (function oc$web$api$collect_password(update_link,pswd,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = update_link;
if(cljs.core.truth_(and__4115__auto__)){
return pswd;
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.auth_http(oc.web.api.method_for_link(update_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([update_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"json-params","json-params",-1112693596),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"password","password",417022471),pswd], null),new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(update_link)], null),(function (p__41585){
var map__41586 = p__41585;
var map__41586__$1 = (((((!((map__41586 == null))))?(((((map__41586.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41586.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41586):map__41586);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41586__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41586__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41586__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return oc.web.lib.utils.after((100),(function (){
return (callback.cljs$core$IFn$_invoke$arity$3 ? callback.cljs$core$IFn$_invoke$arity$3(status,body,success) : callback.call(null,status,body,success));
}));
}));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("collect-password",update_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"pswd","pswd",278786885),cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(cljs.core.count(pswd),"*"))], null)], 0));
}
});
oc.web.api.get_user = (function oc$web$api$get_user(user_link,callback){
if(cljs.core.truth_(user_link)){
return oc.web.api.auth_http(oc.web.api.method_for_link(user_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(user_link)], null),(function (p__41588){
var map__41589 = p__41588;
var map__41589__$1 = (((((!((map__41589 == null))))?(((((map__41589.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41589.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41589):map__41589);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41589__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41589__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41589__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return (callback.cljs$core$IFn$_invoke$arity$2 ? callback.cljs$core$IFn$_invoke$arity$2(success,body) : callback.call(null,success,body));
}));
} else {
return oc.web.api.handle_missing_link("get-user",user_link,callback);
}
});
oc.web.api.patch_user = (function oc$web$api$patch_user(patch_user_link,new_user_data,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = patch_user_link;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.map_QMARK_(new_user_data);
} else {
return and__4115__auto__;
}
})())){
var without_email = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(new_user_data,new cljs.core.Keyword(null,"email","email",1415816706));
var safe_new_user_data = cljs.core.select_keys(without_email,oc.web.api.user_allowed_keys);
return oc.web.api.auth_http(oc.web.api.method_for_link(patch_user_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([patch_user_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(patch_user_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(safe_new_user_data)], null),(function (p__41591){
var map__41592 = p__41591;
var map__41592__$1 = (((((!((map__41592 == null))))?(((((map__41592.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41592.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41592):map__41592);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41592__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41592__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41592__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return (callback.cljs$core$IFn$_invoke$arity$3 ? callback.cljs$core$IFn$_invoke$arity$3(status,body,success) : callback.call(null,status,body,success));
}));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("patch-user",patch_user_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"new-user-data","new-user-data",-873997213),new_user_data], null)], 0));
}
});
oc.web.api.refresh_slack_user = (function oc$web$api$refresh_slack_user(refresh_link,callback){
if(cljs.core.truth_(refresh_link)){
return oc.web.api.auth_http(oc.web.api.method_for_link(refresh_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([refresh_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(refresh_link)], null),(function (p__41594){
var map__41595 = p__41594;
var map__41595__$1 = (((((!((map__41595 == null))))?(((((map__41595.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41595.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41595):map__41595);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41595__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41595__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41595__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return (callback.cljs$core$IFn$_invoke$arity$3 ? callback.cljs$core$IFn$_invoke$arity$3(status,body,success) : callback.call(null,status,body,success));
}));
} else {
return oc.web.api.handle_missing_link("refresh-slack-user",refresh_link,callback);
}
});
/**
 * Given a user-id add him as an author to the current org.
 *   Refresh the user list and the org-data when finished.
 */
oc.web.api.add_author = (function oc$web$api$add_author(add_author_link,user_id,callback){
if(cljs.core.truth_(add_author_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(add_author_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([add_author_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(add_author_link),new cljs.core.Keyword(null,"body","body",-2049205669),user_id], null),callback);
} else {
return oc.web.api.handle_missing_link("add-author",add_author_link,callback);
}
});
/**
 * Given a map containing :user-id and :links, remove the user as an author using the `remove` link.
 *   Refresh the org data when finished.
 */
oc.web.api.remove_author = (function oc$web$api$remove_author(remove_author_link,user_author,callback){
if(cljs.core.truth_(remove_author_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(remove_author_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([remove_author_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(remove_author_link)], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("remove-author",remove_author_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"user-author","user-author",1682605159),user_author], null)], 0));
}
});
oc.web.api.password_reset = (function oc$web$api$password_reset(reset_link,email,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = reset_link;
if(cljs.core.truth_(and__4115__auto__)){
return email;
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.auth_http(oc.web.api.method_for_link(reset_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([reset_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(reset_link),new cljs.core.Keyword(null,"body","body",-2049205669),email], null),(function (p__41599){
var map__41600 = p__41599;
var map__41600__$1 = (((((!((map__41600 == null))))?(((((map__41600.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41600.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41600):map__41600);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41600__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41600__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41600__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return (callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(status) : callback.call(null,status));
}));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("password-reset",reset_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"email","email",1415816706),email], null)], 0));
}
});
oc.web.api.resend_verification_email = (function oc$web$api$resend_verification_email(resend_link,callback){
if(cljs.core.truth_(resend_link)){
return oc.web.api.auth_http(oc.web.api.method_for_link(resend_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([resend_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(resend_link)], null),(function (p__41602){
var map__41603 = p__41602;
var map__41603__$1 = (((((!((map__41603 == null))))?(((((map__41603.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41603.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41603):map__41603);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41603__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41603__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41603__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return (callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(success) : callback.call(null,success));
}));
} else {
return null;
}
});
oc.web.api.add_expo_push_token = (function oc$web$api$add_expo_push_token(add_token_link,push_token,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = add_token_link;
if(cljs.core.truth_(and__4115__auto__)){
return push_token;
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.auth_http(oc.web.api.method_for_link(add_token_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([add_token_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(add_token_link),new cljs.core.Keyword(null,"body","body",-2049205669),push_token], null),(function (p__41605){
var map__41606 = p__41605;
var map__41606__$1 = (((((!((map__41606 == null))))?(((((map__41606.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41606.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41606):map__41606);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41606__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41606__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41606__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return (callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(success) : callback.call(null,success));
}));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("add-expo-push-token",add_token_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"push-token","push-token",-1283347571),push_token], null)], 0));
}
});
oc.web.api.get_comments = (function oc$web$api$get_comments(comments_link,callback){
if(cljs.core.truth_(comments_link)){
return oc.web.api.interaction_http(oc.web.api.method_for_link(comments_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([comments_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(comments_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("get-comments",comments_link,callback);
}
});
oc.web.api.add_comment = (function oc$web$api$add_comment(add_comment_link,comment_body,comment_uuid,parent_comment_uuid,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = add_comment_link;
if(cljs.core.truth_(and__4115__auto__)){
return comment_body;
} else {
return and__4115__auto__;
}
})())){
var json_data = oc.web.lib.json.cljs__GT_json(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"body","body",-2049205669),comment_body,new cljs.core.Keyword(null,"uuid","uuid",-2145095719),comment_uuid,new cljs.core.Keyword(null,"parent-uuid","parent-uuid",-2003485227),parent_comment_uuid], null));
return oc.web.api.interaction_http(oc.web.api.method_for_link(add_comment_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([add_comment_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(add_comment_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),json_data], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("add-comment",add_comment_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"comment-body","comment-body",-695514287),comment_body], null)], 0));
}
});
oc.web.api.delete_comment = (function oc$web$api$delete_comment(delete_comment_link,callback){
if(cljs.core.truth_(delete_comment_link)){
return oc.web.api.interaction_http(oc.web.api.method_for_link(delete_comment_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([delete_comment_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(delete_comment_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("delete-comment",delete_comment_link,callback);
}
});
oc.web.api.patch_comment = (function oc$web$api$patch_comment(patch_comment_link,new_comment_body,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = patch_comment_link;
if(cljs.core.truth_(and__4115__auto__)){
return new_comment_body;
} else {
return and__4115__auto__;
}
})())){
var json_data = oc.web.lib.json.cljs__GT_json(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"body","body",-2049205669),new_comment_body], null));
return oc.web.api.interaction_http(oc.web.api.method_for_link(patch_comment_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([patch_comment_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(patch_comment_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),json_data], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("patch-comment",patch_comment_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"new-comment-body","new-comment-body",-937499098),new_comment_body], null)], 0));
}
});
oc.web.api.toggle_reaction = (function oc$web$api$toggle_reaction(reaction_link,callback){
if(cljs.core.truth_(reaction_link)){
return oc.web.api.interaction_http(oc.web.api.method_for_link(reaction_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([reaction_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(reaction_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("toggle-reaction",reaction_link,callback);
}
});
/**
 * Given the link to react with an arbitrary emoji and the emoji, post it to the interaction service
 */
oc.web.api.react_from_picker = (function oc$web$api$react_from_picker(react_link,emoji,callback){
if(cljs.core.truth_(react_link)){
return oc.web.api.interaction_http(oc.web.api.method_for_link(react_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([react_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(react_link),new cljs.core.Keyword(null,"body","body",-2049205669),emoji], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("react-from-picker",react_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"emoji","emoji",1031230144),emoji], null)], 0));
}
});
oc.web.api.get_entry = (function oc$web$api$get_entry(entry_link,callback){
if(cljs.core.truth_(entry_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(entry_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([entry_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(entry_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("get-entry",entry_link,callback);
}
});
oc.web.api.create_entry = (function oc$web$api$create_entry(create_entry_link,entry_data,edit_key,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = create_entry_link;
if(cljs.core.truth_(and__4115__auto__)){
return entry_data;
} else {
return and__4115__auto__;
}
})())){
var cleaned_entry_data = oc.web.utils.poll.clean_polls(cljs.core.select_keys(entry_data,oc.web.api.entry_allowed_keys));
return oc.web.api.storage_http(oc.web.api.method_for_link(create_entry_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([create_entry_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(create_entry_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(cleaned_entry_data)], null),cljs.core.partial.cljs$core$IFn$_invoke$arity$3(callback,entry_data,edit_key));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("create-entry",create_entry_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),entry_data,new cljs.core.Keyword(null,"edit-key","edit-key",-1833788727),edit_key], null)], 0));
}
});
oc.web.api.publish_entry = (function oc$web$api$publish_entry(publish_entry_link,entry_data,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = entry_data;
if(cljs.core.truth_(and__4115__auto__)){
return publish_entry_link;
} else {
return and__4115__auto__;
}
})())){
var cleaned_entry_data = oc.web.utils.poll.clean_polls(cljs.core.select_keys(entry_data,oc.web.api.entry_allowed_keys));
return oc.web.api.storage_http(oc.web.api.method_for_link(publish_entry_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([publish_entry_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(publish_entry_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(cleaned_entry_data)], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("publish-entry",publish_entry_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),entry_data], null)], 0));
}
});
oc.web.api.patch_entry = (function oc$web$api$patch_entry(patch_entry_link,entry_data,edit_key,callback){
if(cljs.core.truth_(patch_entry_link)){
var cleaned_entry_data = oc.web.utils.poll.clean_polls(cljs.core.select_keys(entry_data,oc.web.api.entry_allowed_keys));
return oc.web.api.storage_http(oc.web.api.method_for_link(patch_entry_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([patch_entry_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(patch_entry_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(cleaned_entry_data)], null),cljs.core.partial.cljs$core$IFn$_invoke$arity$3(callback,entry_data,edit_key));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("patch-entry",patch_entry_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),entry_data,new cljs.core.Keyword(null,"edit-key","edit-key",-1833788727),edit_key], null)], 0));
}
});
oc.web.api.delete_entry = (function oc$web$api$delete_entry(delete_entry_link,callback){
if(cljs.core.truth_(delete_entry_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(delete_entry_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([delete_entry_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(delete_entry_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("delete-entry",delete_entry_link,callback);
}
});
oc.web.api.revert_entry = (function oc$web$api$revert_entry(revert_entry_link,entry_data,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = revert_entry_link;
if(cljs.core.truth_(and__4115__auto__)){
return entry_data;
} else {
return and__4115__auto__;
}
})())){
var cleaned_entry_data = cljs.core.select_keys(entry_data,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"revision-id","revision-id",1301980641)], null));
return oc.web.api.storage_http(oc.web.api.method_for_link(revert_entry_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([revert_entry_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(revert_entry_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(cleaned_entry_data)], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("revert-entry",revert_entry_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),entry_data], null)], 0));
}
});
oc.web.api.share_entry = (function oc$web$api$share_entry(share_link,share_data,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = share_link;
if(cljs.core.truth_(and__4115__auto__)){
return share_data;
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.storage_http(oc.web.api.method_for_link(share_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([share_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(share_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),oc.web.lib.json.cljs__GT_json(share_data)], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("share-entry",share_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"share-data","share-data",1810949431),share_data], null)], 0));
}
});
oc.web.api.get_secure_entry = (function oc$web$api$get_secure_entry(secure_link,callback){
if(cljs.core.truth_(secure_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(secure_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([secure_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(secure_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("get-secure-entry",secure_link,callback);
}
});
oc.web.api.get_entry_with_uuid = (function oc$web$api$get_entry_with_uuid(org_slug,board_slug,activity_uuid,callback){
var activity_link = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"href","href",-793805698),["/orgs/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(org_slug),"/boards/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(board_slug),"/entries/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(activity_uuid)].join(''),new cljs.core.Keyword(null,"method","method",55703592),"GET",new cljs.core.Keyword(null,"rel","rel",1378823488),"",new cljs.core.Keyword(null,"accept","accept",1874130431),"application/vnd.open-company.entry.v1+json"], null);
if(cljs.core.truth_((function (){var and__4115__auto__ = org_slug;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = board_slug;
if(cljs.core.truth_(and__4115__auto____$1)){
return activity_uuid;
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
return oc.web.api.storage_http(oc.web.api.method_for_link(activity_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([activity_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(activity_link)], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("get-entry-with-uuid",activity_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),org_slug,new cljs.core.Keyword(null,"board-slug","board-slug",99003663),board_slug,new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778),activity_uuid], null)], 0));
}
});
oc.web.api.query = (function oc$web$api$query(org_uuid,search_query,callback){
var search_link = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"href","href",-793805698),["/search/?q=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(search_query),"&org=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(org_uuid)].join(''),new cljs.core.Keyword(null,"content-type","content-type",-508222634),oc.web.api.content_type("search"),new cljs.core.Keyword(null,"method","method",55703592),"GET",new cljs.core.Keyword(null,"rel","rel",1378823488),""], null);
if(cljs.core.truth_(search_query)){
return oc.web.api.search_http(oc.web.api.method_for_link(search_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([search_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(search_link)], null),(function (p__41608){
var map__41609 = p__41608;
var map__41609__$1 = (((((!((map__41609 == null))))?(((((map__41609.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41609.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41609):map__41609);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41609__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41609__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41609__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var G__41611 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"query","query",-1288509510),search_query,new cljs.core.Keyword(null,"success","success",1890645906),success,new cljs.core.Keyword(null,"error","error",-978969032),(cljs.core.truth_(success)?null:body),new cljs.core.Keyword(null,"body","body",-2049205669),(cljs.core.truth_((function (){var and__4115__auto__ = success;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(body);
} else {
return and__4115__auto__;
}
})())?oc.web.lib.json.json__GT_cljs(body):null)], null);
return (callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(G__41611) : callback.call(null,G__41611));
}));
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("query",search_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"org-uuid","org-uuid",1539092089),org_uuid,new cljs.core.Keyword(null,"search-query","search-query",-1077556709),search_query], null)], 0));
}
});
oc.web.api.get_reminders = (function oc$web$api$get_reminders(reminders_link,callback){
if(cljs.core.truth_(reminders_link)){
return oc.web.api.reminders_http(oc.web.api.method_for_link(reminders_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([reminders_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(reminders_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("get-reminders",reminders_link,callback);
}
});
oc.web.api.add_reminder = (function oc$web$api$add_reminder(add_reminder_link,reminder_data,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = add_reminder_link;
if(cljs.core.truth_(and__4115__auto__)){
return reminder_data;
} else {
return and__4115__auto__;
}
})())){
var fixed_reminder_data = cljs.core.select_keys(reminder_data,oc.web.api.reminder_allowed_keys);
var json_data = oc.web.lib.json.cljs__GT_json(fixed_reminder_data);
return oc.web.api.reminders_http(oc.web.api.method_for_link(add_reminder_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([add_reminder_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(add_reminder_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),json_data], null),callback);
} else {
return oc.web.api.handle_missing_link("add-reminder",add_reminder_link,callback);
}
});
oc.web.api.update_reminder = (function oc$web$api$update_reminder(update_reminder_link,reminder_data,callback){
if(cljs.core.truth_((function (){var and__4115__auto__ = update_reminder_link;
if(cljs.core.truth_(and__4115__auto__)){
return reminder_data;
} else {
return and__4115__auto__;
}
})())){
var fixed_reminder_data = cljs.core.select_keys(reminder_data,oc.web.api.reminder_allowed_keys);
var json_data = oc.web.lib.json.cljs__GT_json(fixed_reminder_data);
return oc.web.api.reminders_http(oc.web.api.method_for_link(update_reminder_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([update_reminder_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(update_reminder_link),new cljs.core.Keyword(null,"json-params","json-params",-1112693596),json_data], null),callback);
} else {
return oc.web.api.handle_missing_link("update-reminder",update_reminder_link,callback);
}
});
oc.web.api.delete_reminder = (function oc$web$api$delete_reminder(delete_reminder_link,callback){
if(cljs.core.truth_(delete_reminder_link)){
return oc.web.api.reminders_http(oc.web.api.method_for_link(delete_reminder_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([delete_reminder_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(delete_reminder_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("delete-reminder",delete_reminder_link,callback);
}
});
oc.web.api.get_reminders_roster = (function oc$web$api$get_reminders_roster(roster_link,callback){
if(cljs.core.truth_(roster_link)){
return oc.web.api.reminders_http(oc.web.api.method_for_link(roster_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([roster_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(roster_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("get-reminders-roster",roster_link,callback);
}
});
oc.web.api.toggle_bookmark = (function oc$web$api$toggle_bookmark(bookmark_link,callback){
if(cljs.core.truth_(bookmark_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(bookmark_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([bookmark_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(bookmark_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("toggle-bookmark",bookmark_link,callback);
}
});
oc.web.api.inbox_dismiss = (function oc$web$api$inbox_dismiss(dismiss_link,dismiss_at,callback){
if(cljs.core.truth_(dismiss_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(dismiss_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([dismiss_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(dismiss_link),new cljs.core.Keyword(null,"body","body",-2049205669),dismiss_at], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("inbox-dismiss",dismiss_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"dismiss-at","dismiss-at",980214785),dismiss_at], null)], 0));
}
});
oc.web.api.inbox_unread = (function oc$web$api$inbox_unread(unread_link,callback){
if(cljs.core.truth_(unread_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(unread_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([unread_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(unread_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("inbox-unread",unread_link,callback);
}
});
oc.web.api.inbox_follow = (function oc$web$api$inbox_follow(follow_link,callback){
if(cljs.core.truth_(follow_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(follow_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([follow_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(follow_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("inbox-follow",follow_link,callback);
}
});
oc.web.api.inbox_unfollow = (function oc$web$api$inbox_unfollow(unfollow_link,callback){
if(cljs.core.truth_(unfollow_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(unfollow_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([unfollow_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(unfollow_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("inbox-unfollow",unfollow_link,callback);
}
});
oc.web.api.inbox_dismiss_all = (function oc$web$api$inbox_dismiss_all(dismiss_all_link,dismiss_at,callback){
if(cljs.core.truth_(dismiss_all_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(dismiss_all_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([dismiss_all_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(dismiss_all_link),new cljs.core.Keyword(null,"body","body",-2049205669),dismiss_at], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("inbox-dismiss-all",dismiss_all_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"dismiss-at","dismiss-at",980214785),dismiss_at], null)], 0));
}
});
oc.web.api.get_contributions = (function oc$web$api$get_contributions(contrib_link,callback){
if(cljs.core.truth_(contrib_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(contrib_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([contrib_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(contrib_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("get-contributions",contrib_link,callback);
}
});
oc.web.api.poll_vote = (function oc$web$api$poll_vote(vote_link,callback){
if(cljs.core.truth_(vote_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(vote_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([vote_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(vote_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("vote-link",vote_link,callback);
}
});
oc.web.api.poll_add_reply = (function oc$web$api$poll_add_reply(add_reply_link,reply_body,callback){
if(cljs.core.truth_(add_reply_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(add_reply_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([add_reply_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(add_reply_link),new cljs.core.Keyword(null,"body","body",-2049205669),reply_body], null),callback);
} else {
return oc.web.api.handle_missing_link.cljs$core$IFn$_invoke$arity$variadic("poll-add-reply",add_reply_link,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"reply-body","reply-body",-2100286863),reply_body], null)], 0));
}
});
oc.web.api.poll_delete_reply = (function oc$web$api$poll_delete_reply(delete_reply_link,callback){
if(cljs.core.truth_(delete_reply_link)){
return oc.web.api.storage_http(oc.web.api.method_for_link(delete_reply_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([delete_reply_link], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(delete_reply_link)], null),callback);
} else {
return oc.web.api.handle_missing_link("poll-delete-reply",delete_reply_link,callback);
}
});
oc.web.api.request_reads_data = (function oc$web$api$request_reads_data(item_id){
if(cljs.core.seq(item_id)){
return oc.web.ws.change_client.who_read(item_id);
} else {
return null;
}
});
oc.web.api.request_reads_count = (function oc$web$api$request_reads_count(item_ids){
if(cljs.core.seq(item_ids)){
return oc.web.ws.change_client.who_read_count(item_ids);
} else {
return null;
}
});
oc.web.api.mark_unread = (function oc$web$api$mark_unread(mark_unread_link,container_id,callback){
if(cljs.core.truth_(mark_unread_link)){
return oc.web.api.change_http(oc.web.api.method_for_link(mark_unread_link),oc.web.api.relative_href.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([mark_unread_link], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headers","headers",-835030129),oc.web.api.headers_for_link(mark_unread_link),new cljs.core.Keyword(null,"body","body",-2049205669),container_id], null),callback);
} else {
return oc.web.api.handle_missing_link("mark-unread",mark_unread_link,callback);
}
});

//# sourceMappingURL=oc.web.api.js.map
