goog.provide('oops.core');
oops.core.report_error_dynamically = (function oops$core$report_error_dynamically(msg,data){
if(oops.state.was_error_reported_QMARK_()){
return null;
} else {
oops.state.mark_error_reported_BANG_();

var G__43527 = oops.config.get_error_reporting();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"throw","throw",-1044625833),G__43527)){
throw oops.state.prepare_error_from_call_site(msg,oops.helpers.wrap_data_in_enveloper_if_possible(oops.config.use_envelope_QMARK_(),data));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"console","console",1228072057),G__43527)){
var G__43529 = (console["error"]);
var G__43530 = msg;
var G__43531 = oops.helpers.wrap_data_in_enveloper_if_possible(oops.config.use_envelope_QMARK_(),data);
var fexpr__43528 = oops.state.get_console_reporter();
return (fexpr__43528.cljs$core$IFn$_invoke$arity$3 ? fexpr__43528.cljs$core$IFn$_invoke$arity$3(G__43529,G__43530,G__43531) : fexpr__43528.call(null,G__43529,G__43530,G__43531));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(false,G__43527)){
return null;
} else {
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__43527)].join('')));

}
}
}
}
});
oops.core.report_warning_dynamically = (function oops$core$report_warning_dynamically(msg,data){
var G__43532 = oops.config.get_warning_reporting();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"throw","throw",-1044625833),G__43532)){
throw oops.state.prepare_error_from_call_site(msg,oops.helpers.wrap_data_in_enveloper_if_possible(oops.config.use_envelope_QMARK_(),data));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"console","console",1228072057),G__43532)){
var G__43534 = (console["warn"]);
var G__43535 = msg;
var G__43536 = oops.helpers.wrap_data_in_enveloper_if_possible(oops.config.use_envelope_QMARK_(),data);
var fexpr__43533 = oops.state.get_console_reporter();
return (fexpr__43533.cljs$core$IFn$_invoke$arity$3 ? fexpr__43533.cljs$core$IFn$_invoke$arity$3(G__43534,G__43535,G__43536) : fexpr__43533.call(null,G__43534,G__43535,G__43536));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(false,G__43532)){
return null;
} else {
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__43532)].join('')));

}
}
}
});
oops.core.report_if_needed_dynamically = (function oops$core$report_if_needed_dynamically(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43728 = arguments.length;
var i__4737__auto___43729 = (0);
while(true){
if((i__4737__auto___43729 < len__4736__auto___43728)){
args__4742__auto__.push((arguments[i__4737__auto___43729]));

var G__43730 = (i__4737__auto___43729 + (1));
i__4737__auto___43729 = G__43730;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oops.core.report_if_needed_dynamically.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oops.core.report_if_needed_dynamically.cljs$core$IFn$_invoke$arity$variadic = (function (msg_id,p__43539){
var vec__43540 = p__43539;
var info = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43540,(0),null);

if(cljs.core.contains_QMARK_(oops.config.get_suppress_reporting(),msg_id)){
} else {
var G__43543_43731 = oops.config.get_config_key(msg_id);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"warn","warn",-436710552),G__43543_43731)){
oops.core.report_warning_dynamically.call(null,oops.messages.runtime_message.cljs$core$IFn$_invoke$arity$2(msg_id,info),info);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"error","error",-978969032),G__43543_43731)){
oops.core.report_error_dynamically.call(null,oops.messages.runtime_message.cljs$core$IFn$_invoke$arity$2(msg_id,info),info);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(false,G__43543_43731)){
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(null,G__43543_43731)){
} else {
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__43543_43731)].join('')));

}
}
}
}
}

return null;
}));

(oops.core.report_if_needed_dynamically.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oops.core.report_if_needed_dynamically.cljs$lang$applyTo = (function (seq43537){
var G__43538 = cljs.core.first(seq43537);
var seq43537__$1 = cljs.core.next(seq43537);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__43538,seq43537__$1);
}));

oops.core.validate_object_access_dynamically = (function oops$core$validate_object_access_dynamically(obj,mode,key,push_QMARK_,check_key_read_QMARK_,check_key_write_QMARK_){
if(((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mode,(0))) && ((void 0 === obj))))?((cljs.core.contains_QMARK_(oops.config.get_suppress_reporting(),new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301)))?true:(function (){
oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"flavor","flavor",-1331636636),"undefined",new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));

return false;
})()
):((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mode,(0))) && ((obj == null))))?((cljs.core.contains_QMARK_(oops.config.get_suppress_reporting(),new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301)))?true:(function (){
oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"flavor","flavor",-1331636636),"nil",new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));

return false;
})()
):(cljs.core.truth_(goog.isBoolean(obj))?((cljs.core.contains_QMARK_(oops.config.get_suppress_reporting(),new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301)))?true:(function (){
oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"flavor","flavor",-1331636636),"boolean",new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));

return false;
})()
):(cljs.core.truth_(goog.isNumber(obj))?((cljs.core.contains_QMARK_(oops.config.get_suppress_reporting(),new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301)))?true:(function (){
oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"flavor","flavor",-1331636636),"number",new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));

return false;
})()
):(cljs.core.truth_(goog.isString(obj))?((cljs.core.contains_QMARK_(oops.config.get_suppress_reporting(),new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301)))?true:(function (){
oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"flavor","flavor",-1331636636),"string",new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));

return false;
})()
):((cljs.core.not(goog.isObject(obj)))?((cljs.core.contains_QMARK_(oops.config.get_suppress_reporting(),new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301)))?true:(function (){
oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"flavor","flavor",-1331636636),"non-object",new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));

return false;
})()
):(cljs.core.truth_(goog.isDateLike(obj))?((cljs.core.contains_QMARK_(oops.config.get_suppress_reporting(),new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301)))?true:(function (){
oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"flavor","flavor",-1331636636),"date-like",new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));

return true;
})()
):(cljs.core.truth_(oops.helpers.cljs_type_QMARK_(obj))?((cljs.core.contains_QMARK_(oops.config.get_suppress_reporting(),new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301)))?true:(function (){
oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"flavor","flavor",-1331636636),"cljs type",new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));

return true;
})()
):(cljs.core.truth_(oops.helpers.cljs_instance_QMARK_(obj))?((cljs.core.contains_QMARK_(oops.config.get_suppress_reporting(),new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301)))?true:(function (){
oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"unexpected-object-value","unexpected-object-value",-1214439301),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"flavor","flavor",-1331636636),"cljs instance",new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));

return true;
})()
):true
)))))))))){
if(cljs.core.truth_(push_QMARK_)){
oops.state.add_key_to_current_path_BANG_(key);

oops.state.set_last_access_modifier_BANG_(mode);
} else {
}

var and__4115__auto__ = (cljs.core.truth_(check_key_read_QMARK_)?((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mode,(0))) && (cljs.core.not(goog.object.containsKey(obj,key)))))?oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"missing-object-key","missing-object-key",-1300201731),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"key","key",-1516042587),key,new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null)):true):true);
if(cljs.core.truth_(and__4115__auto__)){
if(cljs.core.truth_(check_key_write_QMARK_)){
var temp__5737__auto__ = oops.helpers.get_property_descriptor(obj,key);
if((temp__5737__auto__ == null)){
if(cljs.core.truth_(oops.helpers.is_object_frozen_QMARK_(obj))){
return oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"object-is-frozen","object-is-frozen",-1391578096),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"key","key",-1516042587),key,new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));
} else {
if(cljs.core.truth_(oops.helpers.is_object_sealed_QMARK_(obj))){
return oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"object-is-sealed","object-is-sealed",-1791813926),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"key","key",-1516042587),key,new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));
} else {
return true;

}
}
} else {
var descriptor_43552 = temp__5737__auto__;
var temp__5737__auto____$1 = oops.helpers.determine_property_non_writable_reason(descriptor_43552);
if((temp__5737__auto____$1 == null)){
return true;
} else {
var reason_43553 = temp__5737__auto____$1;
return oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"object-key-not-writable","object-key-not-writable",206336031),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"key","key",-1516042587),key,new cljs.core.Keyword(null,"frozen?","frozen?",613726824),oops.helpers.is_object_frozen_QMARK_(obj),new cljs.core.Keyword(null,"reason","reason",-2070751759),reason_43553,new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));
}
}
} else {
return true;
}
} else {
return and__4115__auto__;
}
} else {
return null;
}
});
oops.core.validate_fn_call_dynamically = (function oops$core$validate_fn_call_dynamically(fn,mode){
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mode,(1))) && ((fn == null)))){
return true;
} else {
if(cljs.core.truth_(goog.isFunction(fn))){
return true;
} else {
return oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"expected-function-value","expected-function-value",-1399123630),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"path","path",-188191168),oops.state.get_key_path_str(),new cljs.core.Keyword(null,"soft?","soft?",-1339668477),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mode,(1)),new cljs.core.Keyword(null,"fn","fn",-1175266204),fn,new cljs.core.Keyword(null,"obj","obj",981763962),oops.state.get_target_object()], null));

}
}
});
oops.core.punch_key_dynamically_BANG_ = (function oops$core$punch_key_dynamically_BANG_(obj,key){
var child_factory_43569 = oops.config.get_child_factory();
var child_factory_43569__$1 = (function (){var G__43570 = child_factory_43569;
var G__43570__$1 = (((G__43570 instanceof cljs.core.Keyword))?G__43570.fqn:null);
switch (G__43570__$1) {
case "js-obj":
return (function (){
return ({});
});

break;
case "js-array":
return (function (){
return [];
});

break;
default:
return child_factory_43569;

}
})();

var child_obj_43568 = (child_factory_43569__$1.cljs$core$IFn$_invoke$arity$2 ? child_factory_43569__$1.cljs$core$IFn$_invoke$arity$2(obj,key) : child_factory_43569__$1.call(null,obj,key));
if(oops.core.validate_object_access_dynamically(obj,(2),key,false,true,true)){
(obj[key] = child_obj_43568);
} else {
}

return child_obj_43568;
});
oops.core.build_path_dynamically = (function oops$core$build_path_dynamically(selector){
if(((typeof selector === 'string') || ((selector instanceof cljs.core.Keyword)))){
var selector_path_43577 = [];
oops.schema.prepare_simple_path_BANG_(selector,selector_path_43577);

return selector_path_43577;
} else {
var selector_path_43578 = [];
oops.schema.prepare_path_BANG_(selector,selector_path_43578);

return selector_path_43578;

}
});
oops.core.check_path_dynamically = (function oops$core$check_path_dynamically(path,op){
var temp__5739__auto__ = oops.schema.check_dynamic_path_BANG_(path,op);
if((temp__5739__auto__ == null)){
return null;
} else {
var issue_43588 = temp__5739__auto__;
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(oops.core.report_if_needed_dynamically,issue_43588);
}
});
oops.core.get_key_dynamically = (function oops$core$get_key_dynamically(obj,key,mode){
if(oops.core.validate_object_access_dynamically(obj,mode,key,true,true,false)){
return (obj[key]);
} else {
return null;
}
});
oops.core.set_key_dynamically = (function oops$core$set_key_dynamically(obj,key,val,mode){
if(oops.core.validate_object_access_dynamically(obj,mode,key,true,true,true)){
return (obj[key] = val);
} else {
return null;
}
});
oops.core.get_selector_dynamically = (function oops$core$get_selector_dynamically(obj,selector){
if(cljs.core.truth_((((!(cljs.spec.alpha.valid_QMARK_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oops.sdefs","obj-selector","oops.sdefs/obj-selector",655346305),selector))))?(function (){var explanation_43606 = cljs.spec.alpha.explain_data(new cljs.core.Keyword("oops.sdefs","obj-selector","oops.sdefs/obj-selector",655346305),selector);
return oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"invalid-selector","invalid-selector",1262807990),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"explanation","explanation",-1426612608),explanation_43606,new cljs.core.Keyword(null,"selector","selector",762528866),selector], null));
})():true))){
var path_43599 = (function (){var path_43595 = oops.core.build_path_dynamically(selector);
oops.core.check_path_dynamically(path_43595,(0));

return path_43595;
})();
var len_43600 = path_43599.length;
var i_43601 = (0);
var obj_43602 = obj;
while(true){
if((i_43601 < len_43600)){
var mode_43603 = (path_43599[i_43601]);
var key_43604 = (path_43599[(i_43601 + (1))]);
var next_obj_43605 = oops.core.get_key_dynamically(obj_43602,key_43604,mode_43603);
var G__43632 = mode_43603;
switch (G__43632) {
case (0):
var G__43779 = (i_43601 + (2));
var G__43780 = next_obj_43605;
i_43601 = G__43779;
obj_43602 = G__43780;
continue;

break;
case (1):
if((!((next_obj_43605 == null)))){
var G__43781 = (i_43601 + (2));
var G__43782 = next_obj_43605;
i_43601 = G__43781;
obj_43602 = G__43782;
continue;
} else {
return null;
}

break;
case (2):
if((!((next_obj_43605 == null)))){
var G__43783 = (i_43601 + (2));
var G__43784 = next_obj_43605;
i_43601 = G__43783;
obj_43602 = G__43784;
continue;
} else {
var G__43785 = (i_43601 + (2));
var G__43786 = oops.core.punch_key_dynamically_BANG_.call(null,obj_43602,key_43604);
i_43601 = G__43785;
obj_43602 = G__43786;
continue;
}

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__43632)].join('')));

}
} else {
return obj_43602;
}
break;
}
} else {
return null;
}
});
oops.core.get_selector_call_info_dynamically = (function oops$core$get_selector_call_info_dynamically(obj,selector){
if(cljs.core.truth_((((!(cljs.spec.alpha.valid_QMARK_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oops.sdefs","obj-selector","oops.sdefs/obj-selector",655346305),selector))))?(function (){var explanation_43667 = cljs.spec.alpha.explain_data(new cljs.core.Keyword("oops.sdefs","obj-selector","oops.sdefs/obj-selector",655346305),selector);
return oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"invalid-selector","invalid-selector",1262807990),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"explanation","explanation",-1426612608),explanation_43667,new cljs.core.Keyword(null,"selector","selector",762528866),selector], null));
})():true))){
var path_43643 = (function (){var path_43642 = oops.core.build_path_dynamically(selector);
oops.core.check_path_dynamically(path_43642,(0));

return path_43642;
})();
var len_43644 = path_43643.length;
if((len_43644 < (4))){
return [obj,(function (){var path_43646 = path_43643;
var len_43647 = path_43646.length;
var i_43648 = (0);
var obj_43649 = obj;
while(true){
if((i_43648 < len_43647)){
var mode_43650 = (path_43646[i_43648]);
var key_43651 = (path_43646[(i_43648 + (1))]);
var next_obj_43652 = oops.core.get_key_dynamically(obj_43649,key_43651,mode_43650);
var G__43670 = mode_43650;
switch (G__43670) {
case (0):
var G__43789 = (i_43648 + (2));
var G__43790 = next_obj_43652;
i_43648 = G__43789;
obj_43649 = G__43790;
continue;

break;
case (1):
if((!((next_obj_43652 == null)))){
var G__43791 = (i_43648 + (2));
var G__43792 = next_obj_43652;
i_43648 = G__43791;
obj_43649 = G__43792;
continue;
} else {
return null;
}

break;
case (2):
if((!((next_obj_43652 == null)))){
var G__43793 = (i_43648 + (2));
var G__43794 = next_obj_43652;
i_43648 = G__43793;
obj_43649 = G__43794;
continue;
} else {
var G__43795 = (i_43648 + (2));
var G__43796 = oops.core.punch_key_dynamically_BANG_.call(null,obj_43649,key_43651);
i_43648 = G__43795;
obj_43649 = G__43796;
continue;
}

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__43670)].join('')));

}
} else {
return obj_43649;
}
break;
}
})()];
} else {
var target_obj_43645 = (function (){var path_43653 = path_43643.slice((0),(len_43644 - (2)));
var len_43654 = path_43653.length;
var i_43655 = (0);
var obj_43656 = obj;
while(true){
if((i_43655 < len_43654)){
var mode_43657 = (path_43653[i_43655]);
var key_43658 = (path_43653[(i_43655 + (1))]);
var next_obj_43659 = oops.core.get_key_dynamically(obj_43656,key_43658,mode_43657);
var G__43673 = mode_43657;
switch (G__43673) {
case (0):
var G__43802 = (i_43655 + (2));
var G__43803 = next_obj_43659;
i_43655 = G__43802;
obj_43656 = G__43803;
continue;

break;
case (1):
if((!((next_obj_43659 == null)))){
var G__43804 = (i_43655 + (2));
var G__43805 = next_obj_43659;
i_43655 = G__43804;
obj_43656 = G__43805;
continue;
} else {
return null;
}

break;
case (2):
if((!((next_obj_43659 == null)))){
var G__43806 = (i_43655 + (2));
var G__43807 = next_obj_43659;
i_43655 = G__43806;
obj_43656 = G__43807;
continue;
} else {
var G__43808 = (i_43655 + (2));
var G__43809 = oops.core.punch_key_dynamically_BANG_.call(null,obj_43656,key_43658);
i_43655 = G__43808;
obj_43656 = G__43809;
continue;
}

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__43673)].join('')));

}
} else {
return obj_43656;
}
break;
}
})();
return [target_obj_43645,(function (){var path_43660 = [(path_43643[(len_43644 - (2))]),(path_43643[(len_43644 - (1))])];
var len_43661 = path_43660.length;
var i_43662 = (0);
var obj_43663 = target_obj_43645;
while(true){
if((i_43662 < len_43661)){
var mode_43664 = (path_43660[i_43662]);
var key_43665 = (path_43660[(i_43662 + (1))]);
var next_obj_43666 = oops.core.get_key_dynamically(obj_43663,key_43665,mode_43664);
var G__43685 = mode_43664;
switch (G__43685) {
case (0):
var G__43813 = (i_43662 + (2));
var G__43814 = next_obj_43666;
i_43662 = G__43813;
obj_43663 = G__43814;
continue;

break;
case (1):
if((!((next_obj_43666 == null)))){
var G__43815 = (i_43662 + (2));
var G__43816 = next_obj_43666;
i_43662 = G__43815;
obj_43663 = G__43816;
continue;
} else {
return null;
}

break;
case (2):
if((!((next_obj_43666 == null)))){
var G__43817 = (i_43662 + (2));
var G__43818 = next_obj_43666;
i_43662 = G__43817;
obj_43663 = G__43818;
continue;
} else {
var G__43819 = (i_43662 + (2));
var G__43820 = oops.core.punch_key_dynamically_BANG_.call(null,obj_43663,key_43665);
i_43662 = G__43819;
obj_43663 = G__43820;
continue;
}

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__43685)].join('')));

}
} else {
return obj_43663;
}
break;
}
})()];
}
} else {
return null;
}
});
oops.core.set_selector_dynamically = (function oops$core$set_selector_dynamically(obj,selector,val){
if(cljs.core.truth_((((!(cljs.spec.alpha.valid_QMARK_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oops.sdefs","obj-selector","oops.sdefs/obj-selector",655346305),selector))))?(function (){var explanation_43715 = cljs.spec.alpha.explain_data(new cljs.core.Keyword("oops.sdefs","obj-selector","oops.sdefs/obj-selector",655346305),selector);
return oops.core.report_if_needed_dynamically.call(null,new cljs.core.Keyword(null,"invalid-selector","invalid-selector",1262807990),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"explanation","explanation",-1426612608),explanation_43715,new cljs.core.Keyword(null,"selector","selector",762528866),selector], null));
})():true))){
var path_43702 = (function (){var path_43701 = oops.core.build_path_dynamically(selector);
oops.core.check_path_dynamically(path_43701,(1));

return path_43701;
})();
var len_43705 = path_43702.length;
var parent_obj_path_43706 = path_43702.slice((0),(len_43705 - (2)));
var key_43703 = (path_43702[(len_43705 - (1))]);
var mode_43704 = (path_43702[(len_43705 - (2))]);
var parent_obj_43707 = (function (){var path_43708 = parent_obj_path_43706;
var len_43709 = path_43708.length;
var i_43710 = (0);
var obj_43711 = obj;
while(true){
if((i_43710 < len_43709)){
var mode_43712 = (path_43708[i_43710]);
var key_43713 = (path_43708[(i_43710 + (1))]);
var next_obj_43714 = oops.core.get_key_dynamically(obj_43711,key_43713,mode_43712);
var G__43718 = mode_43712;
switch (G__43718) {
case (0):
var G__43825 = (i_43710 + (2));
var G__43826 = next_obj_43714;
i_43710 = G__43825;
obj_43711 = G__43826;
continue;

break;
case (1):
if((!((next_obj_43714 == null)))){
var G__43827 = (i_43710 + (2));
var G__43828 = next_obj_43714;
i_43710 = G__43827;
obj_43711 = G__43828;
continue;
} else {
return null;
}

break;
case (2):
if((!((next_obj_43714 == null)))){
var G__43830 = (i_43710 + (2));
var G__43831 = next_obj_43714;
i_43710 = G__43830;
obj_43711 = G__43831;
continue;
} else {
var G__43832 = (i_43710 + (2));
var G__43833 = oops.core.punch_key_dynamically_BANG_.call(null,obj_43711,key_43713);
i_43710 = G__43832;
obj_43711 = G__43833;
continue;
}

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__43718)].join('')));

}
} else {
return obj_43711;
}
break;
}
})();
return oops.core.set_key_dynamically(parent_obj_43707,key_43703,val,mode_43704);
} else {
return null;
}
});

//# sourceMappingURL=oops.core.js.map
