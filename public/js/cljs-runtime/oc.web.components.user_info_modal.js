goog.provide('oc.web.components.user_info_modal');
oc.web.components.user_info_modal.user_info_modal = rum.core.build_defcs((function (s,p__44528){
var map__44529 = p__44528;
var map__44529__$1 = (((((!((map__44529 == null))))?(((((map__44529.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__44529.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__44529):map__44529);
var user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44529__$1,new cljs.core.Keyword(null,"user-data","user-data",2143823568));
var org_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44529__$1,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var my_profile_QMARK_ = (function (){var and__4115__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"self?","self?",-701815921).cljs$core$IFn$_invoke$arity$1(user_data);
} else {
return and__4115__auto__;
}
})();
var member_QMARK_ = new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(org_data);
var team_role = (cljs.core.truth_(member_QMARK_)?oc.web.utils.user.get_user_type.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,org_data], 0)):null);
var panel_stack = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004));
var followers_publishers_count = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579));
var followers_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(followers_publishers_count,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data));
return React.createElement("div",({"onClick": (function (p1__44526_SHARP_){
if(cljs.core.truth_(oc.web.lib.utils.event_inside_QMARK_(p1__44526_SHARP_,rum.core.ref_node(s,new cljs.core.Keyword(null,"user-info","user-info",-1061909920))))){
return null;
} else {
return oc.web.actions.nav_sidebar.close_all_panels();
}
}), "className": "user-info-modal"}),React.createElement("button",({"onClick": oc.web.actions.nav_sidebar.close_all_panels, "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"ref": "user-info", "className": "user-info"}),React.createElement("div",({"className": "user-info-header"}),(function (){var attrs44540 = (cljs.core.truth_(my_profile_QMARK_)?"My profile":"Profile");
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44540))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-info-header-title"], null)], null),attrs44540], 0))):({"className": "user-info-header-title"})),((cljs.core.map_QMARK_(attrs44540))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44540)], null)));
})(),sablono.interpreter.interpret((((cljs.core.count(panel_stack) > (1)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.cancel-bt","button.mlb-reset.cancel-bt",617197847),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.nav_sidebar.show_user_settings(null);
})], null),"Back"], null):null)),sablono.interpreter.interpret((cljs.core.truth_(my_profile_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.save-bt","button.mlb-reset.save-bt",1152501214),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.nav_sidebar.show_user_settings(new cljs.core.Keyword(null,"profile","profile",-545963874));
})], null),"Edit profile"], null):null))),(function (){var attrs44537 = (function (){var G__44541 = user_data;
var G__44542 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"preferred-avatar-size","preferred-avatar-size",498036456),(240)], null);
return (oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$2(G__44541,G__44542) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,G__44541,G__44542));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44537))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-info-body"], null)], null),attrs44537], 0))):({"className": "user-info-body"})),((cljs.core.map_QMARK_(attrs44537))?new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var attrs44538 = oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data], 0));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44538))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-info-name"], null)], null),attrs44538], 0))):({"className": "user-info-name"})),((cljs.core.map_QMARK_(attrs44538))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44538)], null)));
})(),sablono.interpreter.interpret((cljs.core.truth_(new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(user_data))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-title","div.user-info-title",108404818),new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(user_data)], null):null)),sablono.interpreter.interpret((((followers_count > (0)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-followers-count","div.user-info-followers-count",-250697555),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(followers_count)," follower",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(followers_count,(1)))?"s":null)].join('')], null):null)),sablono.interpreter.interpret((cljs.core.truth_(member_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.view-posts-bt","button.mlb-reset.view-posts-bt",339730880),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__44527_SHARP_){
oc.web.actions.nav_sidebar.close_all_panels();

return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(p1__44527_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data),oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data)));
})], null),(cljs.core.truth_(my_profile_QMARK_)?"View my posts":"View posts")], null):null)),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.some(cljs.core.seq,cljs.core.vals(cljs.core.select_keys(user_data,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"location","location",1815599388),new cljs.core.Keyword(null,"timezone","timezone",1831928099),new cljs.core.Keyword(null,"email","email",1415816706),new cljs.core.Keyword(null,"profiles","profiles",507634713),new cljs.core.Keyword(null,"slack-users","slack-users",-434149941)], null)))))?new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-about","div.user-info-about",-1590302455),(cljs.core.truth_(new cljs.core.Keyword(null,"blurb","blurb",-769928228).cljs$core$IFn$_invoke$arity$1(user_data))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p.user-info-about-blurb","p.user-info-about-blurb",619844319),new cljs.core.Keyword(null,"blurb","blurb",-769928228).cljs$core$IFn$_invoke$arity$1(user_data)], null):null),(function (){var temp__5735__auto__ = oc.web.utils.user.timezone_location_string.cljs$core$IFn$_invoke$arity$variadic(user_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var timezone_location_string = temp__5735__auto__;
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-about-location","div.user-info-about-location",-1025358573),timezone_location_string], null);
} else {
return null;
}
})(),(cljs.core.truth_(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-about-email","div.user-info-about-email",1297154482),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),["mailto:",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data))].join(''),new cljs.core.Keyword(null,"target","target",253001721),"_blank"], null),new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data)], null)], null):null),(cljs.core.truth_(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(user_data))?(function (){var iter__4529__auto__ = (function oc$web$components$user_info_modal$iter__44543(s__44544){
return (new cljs.core.LazySeq(null,(function (){
var s__44544__$1 = s__44544;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__44544__$1);
if(temp__5735__auto__){
var s__44544__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__44544__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__44544__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__44546 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__44545 = (0);
while(true){
if((i__44545 < size__4528__auto__)){
var slack_user = cljs.core._nth(c__4527__auto__,i__44545);
if(((cljs.core.seq(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"-")))){
cljs.core.chunk_append(b__44546,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-about-slack","div.user-info-about-slack",-2053281985),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),["slack-user-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_user)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(slack_user))].join('')], null),((clojure.string.starts_with_QMARK_(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"@"))?new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user):["@",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))].join(''))], null));

var G__44571 = (i__44545 + (1));
i__44545 = G__44571;
continue;
} else {
var G__44572 = (i__44545 + (1));
i__44545 = G__44572;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__44546),oc$web$components$user_info_modal$iter__44543(cljs.core.chunk_rest(s__44544__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__44546),null);
}
} else {
var slack_user = cljs.core.first(s__44544__$2);
if(((cljs.core.seq(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"-")))){
return cljs.core.cons(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-about-slack","div.user-info-about-slack",-2053281985),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),["slack-user-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_user)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(slack_user))].join('')], null),((clojure.string.starts_with_QMARK_(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"@"))?new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user):["@",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))].join(''))], null),oc$web$components$user_info_modal$iter__44543(cljs.core.rest(s__44544__$2)));
} else {
var G__44573 = cljs.core.rest(s__44544__$2);
s__44544__$1 = G__44573;
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
})():null),((cljs.core.seq(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.seq,cljs.core.vals(new cljs.core.Keyword(null,"profiles","profiles",507634713).cljs$core$IFn$_invoke$arity$1(user_data)))))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-about-profiles","div.user-info-about-profiles",681470961),(function (){var iter__4529__auto__ = (function oc$web$components$user_info_modal$iter__44547(s__44548){
return (new cljs.core.LazySeq(null,(function (){
var s__44548__$1 = s__44548;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__44548__$1);
if(temp__5735__auto__){
var s__44548__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__44548__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__44548__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__44550 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__44549 = (0);
while(true){
if((i__44549 < size__4528__auto__)){
var vec__44551 = cljs.core._nth(c__4527__auto__,i__44549);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44551,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44551,(1),null);
if(cljs.core.seq(v)){
cljs.core.chunk_append(b__44550,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"class","class",-2030961996),cljs.core.name(k),new cljs.core.Keyword(null,"key","key",-1516042587),["profile-",cljs.core.name(k)].join(''),new cljs.core.Keyword(null,"target","target",253001721),"_blank",new cljs.core.Keyword(null,"href","href",-793805698),((((clojure.string.starts_with_QMARK_(v,"http")) || (clojure.string.starts_with_QMARK_(v,"//"))))?v:["https://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)].join(''))], null)], null));

var G__44574 = (i__44549 + (1));
i__44549 = G__44574;
continue;
} else {
var G__44575 = (i__44549 + (1));
i__44549 = G__44575;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__44550),oc$web$components$user_info_modal$iter__44547(cljs.core.chunk_rest(s__44548__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__44550),null);
}
} else {
var vec__44554 = cljs.core.first(s__44548__$2);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44554,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44554,(1),null);
if(cljs.core.seq(v)){
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"class","class",-2030961996),cljs.core.name(k),new cljs.core.Keyword(null,"key","key",-1516042587),["profile-",cljs.core.name(k)].join(''),new cljs.core.Keyword(null,"target","target",253001721),"_blank",new cljs.core.Keyword(null,"href","href",-793805698),((((clojure.string.starts_with_QMARK_(v,"http")) || (clojure.string.starts_with_QMARK_(v,"//"))))?v:["https://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)].join(''))], null)], null),oc$web$components$user_info_modal$iter__44547(cljs.core.rest(s__44548__$2)));
} else {
var G__44576 = cljs.core.rest(s__44548__$2);
s__44548__$1 = G__44576;
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
})()], null):null)], null):null))], null):new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44537),(function (){var attrs44539 = oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data], 0));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44539))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-info-name"], null)], null),attrs44539], 0))):({"className": "user-info-name"})),((cljs.core.map_QMARK_(attrs44539))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44539)], null)));
})(),sablono.interpreter.interpret((cljs.core.truth_(new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(user_data))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-title","div.user-info-title",108404818),new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(user_data)], null):null)),sablono.interpreter.interpret((((followers_count > (0)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-followers-count","div.user-info-followers-count",-250697555),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(followers_count)," follower",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(followers_count,(1)))?"s":null)].join('')], null):null)),sablono.interpreter.interpret((cljs.core.truth_(member_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.view-posts-bt","button.mlb-reset.view-posts-bt",339730880),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__44527_SHARP_){
oc.web.actions.nav_sidebar.close_all_panels();

return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(p1__44527_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data),oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data)));
})], null),(cljs.core.truth_(my_profile_QMARK_)?"View my posts":"View posts")], null):null)),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.some(cljs.core.seq,cljs.core.vals(cljs.core.select_keys(user_data,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"location","location",1815599388),new cljs.core.Keyword(null,"timezone","timezone",1831928099),new cljs.core.Keyword(null,"email","email",1415816706),new cljs.core.Keyword(null,"profiles","profiles",507634713),new cljs.core.Keyword(null,"slack-users","slack-users",-434149941)], null)))))?new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-about","div.user-info-about",-1590302455),(cljs.core.truth_(new cljs.core.Keyword(null,"blurb","blurb",-769928228).cljs$core$IFn$_invoke$arity$1(user_data))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p.user-info-about-blurb","p.user-info-about-blurb",619844319),new cljs.core.Keyword(null,"blurb","blurb",-769928228).cljs$core$IFn$_invoke$arity$1(user_data)], null):null),(function (){var temp__5735__auto__ = oc.web.utils.user.timezone_location_string.cljs$core$IFn$_invoke$arity$variadic(user_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var timezone_location_string = temp__5735__auto__;
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-about-location","div.user-info-about-location",-1025358573),timezone_location_string], null);
} else {
return null;
}
})(),(cljs.core.truth_(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-about-email","div.user-info-about-email",1297154482),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),["mailto:",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data))].join(''),new cljs.core.Keyword(null,"target","target",253001721),"_blank"], null),new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data)], null)], null):null),(cljs.core.truth_(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(user_data))?(function (){var iter__4529__auto__ = (function oc$web$components$user_info_modal$iter__44557(s__44558){
return (new cljs.core.LazySeq(null,(function (){
var s__44558__$1 = s__44558;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__44558__$1);
if(temp__5735__auto__){
var s__44558__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__44558__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__44558__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__44560 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__44559 = (0);
while(true){
if((i__44559 < size__4528__auto__)){
var slack_user = cljs.core._nth(c__4527__auto__,i__44559);
if(((cljs.core.seq(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"-")))){
cljs.core.chunk_append(b__44560,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-about-slack","div.user-info-about-slack",-2053281985),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),["slack-user-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_user)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(slack_user))].join('')], null),((clojure.string.starts_with_QMARK_(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"@"))?new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user):["@",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))].join(''))], null));

var G__44577 = (i__44559 + (1));
i__44559 = G__44577;
continue;
} else {
var G__44578 = (i__44559 + (1));
i__44559 = G__44578;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__44560),oc$web$components$user_info_modal$iter__44557(cljs.core.chunk_rest(s__44558__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__44560),null);
}
} else {
var slack_user = cljs.core.first(s__44558__$2);
if(((cljs.core.seq(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"-")))){
return cljs.core.cons(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-about-slack","div.user-info-about-slack",-2053281985),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),["slack-user-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_user)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(slack_user))].join('')], null),((clojure.string.starts_with_QMARK_(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"@"))?new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user):["@",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))].join(''))], null),oc$web$components$user_info_modal$iter__44557(cljs.core.rest(s__44558__$2)));
} else {
var G__44579 = cljs.core.rest(s__44558__$2);
s__44558__$1 = G__44579;
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
})():null),((cljs.core.seq(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.seq,cljs.core.vals(new cljs.core.Keyword(null,"profiles","profiles",507634713).cljs$core$IFn$_invoke$arity$1(user_data)))))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-about-profiles","div.user-info-about-profiles",681470961),(function (){var iter__4529__auto__ = (function oc$web$components$user_info_modal$iter__44561(s__44562){
return (new cljs.core.LazySeq(null,(function (){
var s__44562__$1 = s__44562;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__44562__$1);
if(temp__5735__auto__){
var s__44562__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__44562__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__44562__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__44564 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__44563 = (0);
while(true){
if((i__44563 < size__4528__auto__)){
var vec__44565 = cljs.core._nth(c__4527__auto__,i__44563);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44565,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44565,(1),null);
if(cljs.core.seq(v)){
cljs.core.chunk_append(b__44564,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"class","class",-2030961996),cljs.core.name(k),new cljs.core.Keyword(null,"key","key",-1516042587),["profile-",cljs.core.name(k)].join(''),new cljs.core.Keyword(null,"target","target",253001721),"_blank",new cljs.core.Keyword(null,"href","href",-793805698),((((clojure.string.starts_with_QMARK_(v,"http")) || (clojure.string.starts_with_QMARK_(v,"//"))))?v:["https://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)].join(''))], null)], null));

var G__44583 = (i__44563 + (1));
i__44563 = G__44583;
continue;
} else {
var G__44584 = (i__44563 + (1));
i__44563 = G__44584;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__44564),oc$web$components$user_info_modal$iter__44561(cljs.core.chunk_rest(s__44562__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__44564),null);
}
} else {
var vec__44568 = cljs.core.first(s__44562__$2);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44568,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44568,(1),null);
if(cljs.core.seq(v)){
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"class","class",-2030961996),cljs.core.name(k),new cljs.core.Keyword(null,"key","key",-1516042587),["profile-",cljs.core.name(k)].join(''),new cljs.core.Keyword(null,"target","target",253001721),"_blank",new cljs.core.Keyword(null,"href","href",-793805698),((((clojure.string.starts_with_QMARK_(v,"http")) || (clojure.string.starts_with_QMARK_(v,"//"))))?v:["https://",cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)].join(''))], null)], null),oc$web$components$user_info_modal$iter__44561(cljs.core.rest(s__44562__$2)));
} else {
var G__44585 = cljs.core.rest(s__44562__$2);
s__44562__$1 = G__44585;
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
})()], null):null)], null):null))], null)));
})()));
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579)], 0))], null),"user-info-modal");

//# sourceMappingURL=oc.web.components.user_info_modal.js.map
