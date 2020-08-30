goog.provide('oc.web.components.ui.invite_email');
oc.web.components.ui.invite_email.default_row_num = (1);
oc.web.components.ui.invite_email.default_user = "";
oc.web.components.ui.invite_email.default_user_role = new cljs.core.Keyword(null,"author","author",2111686192);
oc.web.components.ui.invite_email.default_user_row = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"temp-user","temp-user",1357534508),oc.web.components.ui.invite_email.default_user,new cljs.core.Keyword(null,"user","user",1532431356),oc.web.components.ui.invite_email.default_user,new cljs.core.Keyword(null,"role","role",-736691072),oc.web.components.ui.invite_email.default_user_role], null);
oc.web.components.ui.invite_email.new_user_row = (function oc$web$components$ui$invite_email$new_user_row(s){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.components.ui.invite_email.default_user_row,new cljs.core.Keyword(null,"type","type",1174270348),"email");
});
oc.web.components.ui.invite_email.valid_user_QMARK_ = (function oc$web$components$ui$invite_email$valid_user_QMARK_(user_map){
return oc.web.lib.utils.valid_email_QMARK_(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user_map));
});
oc.web.components.ui.invite_email.has_valid_user_QMARK_ = (function oc$web$components$ui$invite_email$has_valid_user_QMARK_(users_list){
return cljs.core.some(oc.web.components.ui.invite_email.valid_user_QMARK_,users_list);
});
oc.web.components.ui.invite_email.setup_initial_rows = (function oc$web$components$ui$invite_email$setup_initial_rows(s){
var invite_users = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-users","invite-users",107417337)));
if((cljs.core.count(invite_users) === (0))){
var new_row = oc.web.components.ui.invite_email.new_user_row(s);
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),cljs.core.vec(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"rows-num","rows-num",-1183466189).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.components.ui.invite_email.default_row_num;
}
})(),new_row))], null));
} else {
return null;
}
});
oc.web.components.ui.invite_email.save_button_cta = (function oc$web$components$ui$invite_email$save_button_cta(s){
var or__4126__auto__ = new cljs.core.Keyword(null,"save-title","save-title",-2115503227).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "Send email invitations";
}
});
oc.web.components.ui.invite_email.saving_button_cta = (function oc$web$components$ui$invite_email$saving_button_cta(s){
var or__4126__auto__ = new cljs.core.Keyword(null,"saving-title","saving-title",-465556280).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "Sending email invitations";
}
});
oc.web.components.ui.invite_email.saved_button_cta = (function oc$web$components$ui$invite_email$saved_button_cta(s){
var or__4126__auto__ = new cljs.core.Keyword(null,"saved-title","saved-title",-1142856092).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "Email invitations sent!";
}
});
oc.web.components.ui.invite_email.invite_email = rum.core.build_defcs((function (s,p__44514){
var map__44515 = p__44514;
var map__44515__$1 = (((((!((map__44515 == null))))?(((((map__44515.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__44515.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__44515):map__44515);
var rows_num = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44515__$1,new cljs.core.Keyword(null,"rows-num","rows-num",-1183466189));
var hide_user_role = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44515__$1,new cljs.core.Keyword(null,"hide-user-role","hide-user-role",2103996678));
var invite_users = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"invite-users","invite-users",107417337));
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var is_admin_QMARK_ = oc.web.lib.jwt.is_admin_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data));
return React.createElement("div",({"className": "invite-email-container"}),React.createElement("div",({"key": "org-settings-invite-table", "className": "invite-email"}),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$ui$invite_email$iter__44517(s__44518){
return (new cljs.core.LazySeq(null,(function (){
var s__44518__$1 = s__44518;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__44518__$1);
if(temp__5735__auto__){
var s__44518__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__44518__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__44518__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__44520 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__44519 = (0);
while(true){
if((i__44519 < size__4528__auto__)){
var i = cljs.core._nth(c__4527__auto__,i__44519);
var user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(invite_users,i);
var key_string = ["invite-users-tabe-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(i)].join('');
cljs.core.chunk_append(b__44520,React.createElement("div",({"key": key_string, "className": "invite-email-item-outer"}),React.createElement("div",({"className": "invite-email-item group"}),React.createElement("div",({"className": "invite-email-item-left"}),sablono.interpreter.create_element("input",({"type": "text", "pattern": "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", "placeholder": "name@domain.com", "onChange": ((function (i__44519,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44520,s__44518__$2,temp__5735__auto__,invite_users,org_data,is_admin_QMARK_,map__44515,map__44515__$1,rows_num,hide_user_role){
return (function (p1__44510_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(invite_users,i,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"error","error",-978969032),null,new cljs.core.Keyword(null,"user","user",1532431356),p1__44510_SHARP_.target.value], null)], 0)))], null));
});})(i__44519,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44520,s__44518__$2,temp__5735__auto__,invite_users,org_data,is_admin_QMARK_,map__44515,map__44515__$1,rows_num,hide_user_role))
, "value": (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["org-settings-field","email-field","oc-input",(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(user_data))?"error":null)], null))})),sablono.interpreter.interpret((cljs.core.truth_(hide_user_role)?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-type-dropdown","div.user-type-dropdown",-1576674007),(function (){var G__44521 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),oc.web.lib.utils.guid(),new cljs.core.Keyword(null,"user-type","user-type",738868936),new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user_data),new cljs.core.Keyword(null,"hide-admin","hide-admin",-823852536),cljs.core.not(is_admin_QMARK_),new cljs.core.Keyword(null,"on-change","on-change",-732046149),((function (i__44519,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44520,s__44518__$2,temp__5735__auto__,invite_users,org_data,is_admin_QMARK_,map__44515,map__44515__$1,rows_num,hide_user_role){
return (function (p1__44511_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(invite_users,i,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"role","role",-736691072),p1__44511_SHARP_,new cljs.core.Keyword(null,"error","error",-978969032),null], null)], 0)))], null));
});})(i__44519,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44520,s__44518__$2,temp__5735__auto__,invite_users,org_data,is_admin_QMARK_,map__44515,map__44515__$1,rows_num,hide_user_role))
], null);
return (oc.web.components.ui.user_type_dropdown.user_type_dropdown.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_type_dropdown.user_type_dropdown.cljs$core$IFn$_invoke$arity$1(G__44521) : oc.web.components.ui.user_type_dropdown.user_type_dropdown.call(null,G__44521));
})()], null)))),sablono.interpreter.interpret((cljs.core.truth_(hide_user_role)?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.remove-user","button.mlb-reset.remove-user",-325901667),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__44519,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44520,s__44518__$2,temp__5735__auto__,invite_users,org_data,is_admin_QMARK_,map__44515,map__44515__$1,rows_num,hide_user_role){
return (function (){
var before = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(invite_users,(0),i);
var after = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(invite_users,(i + (1)),cljs.core.count(invite_users));
var next_invite_users = cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(before,after));
var fixed_next_invite_users = (((cljs.core.count(next_invite_users) === (0)))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.components.ui.invite_email.default_user_row,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(user_data))], null):next_invite_users);
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),fixed_next_invite_users], null));
});})(i__44519,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44520,s__44518__$2,temp__5735__auto__,invite_users,org_data,is_admin_QMARK_,map__44515,map__44515__$1,rows_num,hide_user_role))
], null)], null))))));

var G__44525 = (i__44519 + (1));
i__44519 = G__44525;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__44520),oc$web$components$ui$invite_email$iter__44517(cljs.core.chunk_rest(s__44518__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__44520),null);
}
} else {
var i = cljs.core.first(s__44518__$2);
var user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(invite_users,i);
var key_string = ["invite-users-tabe-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(i)].join('');
return cljs.core.cons(React.createElement("div",({"key": key_string, "className": "invite-email-item-outer"}),React.createElement("div",({"className": "invite-email-item group"}),React.createElement("div",({"className": "invite-email-item-left"}),sablono.interpreter.create_element("input",({"type": "text", "pattern": "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", "placeholder": "name@domain.com", "onChange": ((function (user_data,key_string,i,s__44518__$2,temp__5735__auto__,invite_users,org_data,is_admin_QMARK_,map__44515,map__44515__$1,rows_num,hide_user_role){
return (function (p1__44510_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(invite_users,i,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"error","error",-978969032),null,new cljs.core.Keyword(null,"user","user",1532431356),p1__44510_SHARP_.target.value], null)], 0)))], null));
});})(user_data,key_string,i,s__44518__$2,temp__5735__auto__,invite_users,org_data,is_admin_QMARK_,map__44515,map__44515__$1,rows_num,hide_user_role))
, "value": (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["org-settings-field","email-field","oc-input",(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(user_data))?"error":null)], null))})),sablono.interpreter.interpret((cljs.core.truth_(hide_user_role)?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-type-dropdown","div.user-type-dropdown",-1576674007),(function (){var G__44522 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),oc.web.lib.utils.guid(),new cljs.core.Keyword(null,"user-type","user-type",738868936),new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user_data),new cljs.core.Keyword(null,"hide-admin","hide-admin",-823852536),cljs.core.not(is_admin_QMARK_),new cljs.core.Keyword(null,"on-change","on-change",-732046149),((function (user_data,key_string,i,s__44518__$2,temp__5735__auto__,invite_users,org_data,is_admin_QMARK_,map__44515,map__44515__$1,rows_num,hide_user_role){
return (function (p1__44511_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(invite_users,i,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"role","role",-736691072),p1__44511_SHARP_,new cljs.core.Keyword(null,"error","error",-978969032),null], null)], 0)))], null));
});})(user_data,key_string,i,s__44518__$2,temp__5735__auto__,invite_users,org_data,is_admin_QMARK_,map__44515,map__44515__$1,rows_num,hide_user_role))
], null);
return (oc.web.components.ui.user_type_dropdown.user_type_dropdown.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_type_dropdown.user_type_dropdown.cljs$core$IFn$_invoke$arity$1(G__44522) : oc.web.components.ui.user_type_dropdown.user_type_dropdown.call(null,G__44522));
})()], null)))),sablono.interpreter.interpret((cljs.core.truth_(hide_user_role)?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.remove-user","button.mlb-reset.remove-user",-325901667),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (user_data,key_string,i,s__44518__$2,temp__5735__auto__,invite_users,org_data,is_admin_QMARK_,map__44515,map__44515__$1,rows_num,hide_user_role){
return (function (){
var before = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(invite_users,(0),i);
var after = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(invite_users,(i + (1)),cljs.core.count(invite_users));
var next_invite_users = cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(before,after));
var fixed_next_invite_users = (((cljs.core.count(next_invite_users) === (0)))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.components.ui.invite_email.default_user_row,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(user_data))], null):next_invite_users);
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),fixed_next_invite_users], null));
});})(user_data,key_string,i,s__44518__$2,temp__5735__auto__,invite_users,org_data,is_admin_QMARK_,map__44515,map__44515__$1,rows_num,hide_user_role))
], null)], null))))),oc$web$components$ui$invite_email$iter__44517(cljs.core.rest(s__44518__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(invite_users)));
})())),React.createElement("button",({"onClick": (function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(invite_users,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.components.ui.invite_email.default_user_row,new cljs.core.Keyword(null,"type","type",1174270348),"email"))], null));
}), "className": "mlb-reset add-button"}),React.createElement("div",({"className": "add-button-plus"})),"Add another"),React.createElement("button",({"onClick": (function (){
var valid_count = cljs.core.count(cljs.core.filterv(oc.web.components.ui.invite_email.valid_user_QMARK_,invite_users));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.invite-email","sending","oc.web.components.ui.invite-email/sending",-306308368).cljs$core$IFn$_invoke$arity$1(s),valid_count);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.invite-email","initial-sending","oc.web.components.ui.invite-email/initial-sending",35933390).cljs$core$IFn$_invoke$arity$1(s),valid_count);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.invite-email","send-bt-cta","oc.web.components.ui.invite-email/send-bt-cta",-218881637).cljs$core$IFn$_invoke$arity$1(s),oc.web.components.ui.invite_email.saving_button_cta(s));

return oc.web.actions.team.invite_users(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-users","invite-users",107417337))));
}), "disabled": ((cljs.core.not(oc.web.components.ui.invite_email.has_valid_user_QMARK_(invite_users))) || ((cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.invite-email","sending","oc.web.components.ui.invite-email/sending",-306308368).cljs$core$IFn$_invoke$arity$1(s)) > (0)))), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","save-bt",((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.invite_email.saved_button_cta(s),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.invite-email","send-bt-cta","oc.web.components.ui.invite-email/send-bt-cta",-218881637).cljs$core$IFn$_invoke$arity$1(s))))?"no-disable":null)], null))}),sablono.interpreter.interpret(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.invite-email","send-bt-cta","oc.web.components.ui.invite-email/send-bt-cta",-218881637).cljs$core$IFn$_invoke$arity$1(s)))));
}),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.invite-email","send-bt-cta","oc.web.components.ui.invite-email/send-bt-cta",-218881637)),rum.core.local.cljs$core$IFn$_invoke$arity$2((0),new cljs.core.Keyword("oc.web.components.ui.invite-email","sending","oc.web.components.ui.invite-email/sending",-306308368)),rum.core.local.cljs$core$IFn$_invoke$arity$2((0),new cljs.core.Keyword("oc.web.components.ui.invite-email","initial-sending","oc.web.components.ui.invite-email/initial-sending",35933390)),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.components.ui.invite_email.setup_initial_rows(s);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.invite-email","send-bt-cta","oc.web.components.ui.invite-email/send-bt-cta",-218881637).cljs$core$IFn$_invoke$arity$1(s),oc.web.components.ui.invite_email.save_button_cta(s));

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
var sending_44531 = new cljs.core.Keyword("oc.web.components.ui.invite-email","sending","oc.web.components.ui.invite-email/sending",-306308368).cljs$core$IFn$_invoke$arity$1(s);
var initial_sending_44532 = new cljs.core.Keyword("oc.web.components.ui.invite-email","initial-sending","oc.web.components.ui.invite-email/initial-sending",35933390).cljs$core$IFn$_invoke$arity$1(s);
if((cljs.core.deref(sending_44531) > (0))){
var invite_users_44533 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-users","invite-users",107417337)));
var no_error_invites_44534 = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__44509_SHARP_){
return cljs.core.not(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(p1__44509_SHARP_));
}),invite_users_44533);
var error_invites_44535 = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"error","error",-978969032),invite_users_44533);
var hold_initial_sending_44536 = cljs.core.deref(initial_sending_44532);
cljs.core.reset_BANG_(sending_44531,cljs.core.count(no_error_invites_44534));

if((cljs.core.count(no_error_invites_44534) === (0))){
oc.web.lib.utils.after((1000),(function (){
cljs.core.reset_BANG_(sending_44531,(0));

cljs.core.reset_BANG_(initial_sending_44532,(0));

if((cljs.core.count(error_invites_44535) === (0))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.invite-email","send-bt-cta","oc.web.components.ui.invite-email/send-bt-cta",-218881637).cljs$core$IFn$_invoke$arity$1(s),oc.web.components.ui.invite_email.saved_button_cta(s));

oc.web.lib.utils.after((2500),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.invite-email","send-bt-cta","oc.web.components.ui.invite-email/send-bt-cta",-218881637).cljs$core$IFn$_invoke$arity$1(s),oc.web.components.ui.invite_email.save_button_cta(s));
}));

oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"title","title",636505583),["Invite",(((hold_initial_sending_44536 > (1)))?"s":null)," sent."].join(''),new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"primary-bt-inline","primary-bt-inline",-796141614),true,new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"invites-sent","invites-sent",555070572)], null));

return oc.web.components.ui.invite_email.setup_initial_rows(s);
} else {
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.invite-email","send-bt-cta","oc.web.components.ui.invite-email/send-bt-cta",-218881637).cljs$core$IFn$_invoke$arity$1(s),oc.web.components.ui.invite_email.save_button_cta(s));
}
}));
} else {
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),null], null));

return s;
})], null)], null),"invite-email");

//# sourceMappingURL=oc.web.components.ui.invite_email.js.map
