goog.provide('oc.web.components.ui.stream_attachments');
oc.web.components.ui.stream_attachments.stream_attachments = rum.core.build_defcs((function (s,attachments,expand_cb,remove_cb){
var atc_num = cljs.core.count(attachments);
var editable_QMARK_ = cljs.core.fn_QMARK_(remove_cb);
var show_all_QMARK_ = (!(cljs.core.fn_QMARK_(expand_cb)));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
var attachments_num = ((show_all_QMARK_)?cljs.core.count(attachments):((is_mobile_QMARK_)?(1):(2)));
var attachments_list = ((show_all_QMARK_)?attachments:cljs.core.take.cljs$core$IFn$_invoke$arity$2(attachments_num,attachments));
var should_show_expand_QMARK_ = (cljs.core.count(attachments) > cljs.core.count(attachments_list));
var attachment_uploading = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864));
return sablono.interpreter.interpret((cljs.core.truth_((function (){var or__4126__auto__ = (atc_num > (0));
if(or__4126__auto__){
return or__4126__auto__;
} else {
return attachment_uploading;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-attachments","div.stream-attachments",-1000552245),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-attachments-content.group","div.stream-attachments-content.group",1904948627),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),((show_all_QMARK_)?null:"collapsed")], null),(cljs.core.truth_(attachment_uploading)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-attachments-item.group","div.stream-attachments-item.group",-2012811858),(oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.attachment-uploading","span.attachment-uploading",1954262115),["Uploading ",cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"progress","progress",244323547).cljs$core$IFn$_invoke$arity$1(attachment_uploading);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (0);
}
})()),"%..."].join('')], null)], null):null),(function (){var iter__4529__auto__ = (function oc$web$components$ui$stream_attachments$iter__39445(s__39446){
return (new cljs.core.LazySeq(null,(function (){
var s__39446__$1 = s__39446;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__39446__$1);
if(temp__5735__auto__){
var s__39446__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__39446__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__39446__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__39449 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__39447 = (0);
while(true){
if((i__39447 < size__4528__auto__)){
var idx = cljs.core._nth(c__4527__auto__,i__39447);
var atch = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(attachments_list,idx);
var atch_key = ["attachment-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"file-url","file-url",-863738963).cljs$core$IFn$_invoke$arity$1(atch))].join('');
var created_at = new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(atch);
var file_name = new cljs.core.Keyword(null,"file-name","file-name",-1654217259).cljs$core$IFn$_invoke$arity$1(atch);
var size = new cljs.core.Keyword(null,"file-size","file-size",-1900760755).cljs$core$IFn$_invoke$arity$1(atch);
var subtitle = (cljs.core.truth_(size)?clojure.string.lower_case(["(",clojure.contrib.humanize.filesize.cljs$core$IFn$_invoke$arity$variadic(size,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"binary","binary",-1802232288),false,new cljs.core.Keyword(null,"format","format",-1306924766),"%.2f"], 0)),")"].join('')):null);
cljs.core.chunk_append(b__39449,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-attachments-item.group","div.stream-attachments-item.group",-2012811858),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),atch_key], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.group","a.group",1741459406),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),new cljs.core.Keyword(null,"file-url","file-url",-863738963).cljs$core$IFn$_invoke$arity$1(atch),new cljs.core.Keyword(null,"target","target",253001721),"_blank"], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.attachment-info.group","div.attachment-info.group",1437571602),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),((editable_QMARK_)?"editable":null)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.attachment-icon","div.attachment-icon",1764419953)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.attachment-labels","div.attachment-labels",154450793),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),((editable_QMARK_)?"edit":null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.attachment-name","span.attachment-name",1142286372),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.hide_class], null),file_name], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.attachment-description","span.attachment-description",1542423977),subtitle], null)], null),((editable_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.remove-attachment-bt","button.mlb-reset.remove-attachment-bt",-1678681164),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),((is_mobile_QMARK_)?null:(function (){

return "tooltip";
})()
),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"data-delay","data-delay",1974747786),"{\"show\":\"500\", \"hide\":\"0\"}",new cljs.core.Keyword(null,"title","title",636505583),"Remove attachment",new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__39447,atch,atch_key,created_at,file_name,size,subtitle,idx,c__4527__auto__,size__4528__auto__,b__39449,s__39446__$2,temp__5735__auto__,atc_num,editable_QMARK_,show_all_QMARK_,is_mobile_QMARK_,attachments_num,attachments_list,should_show_expand_QMARK_,attachment_uploading){
return (function (p1__39443_SHARP_){
oc.web.lib.utils.event_stop(p1__39443_SHARP_);

oc.web.lib.utils.remove_tooltips();

if(cljs.core.fn_QMARK_(remove_cb)){
return (remove_cb.cljs$core$IFn$_invoke$arity$1 ? remove_cb.cljs$core$IFn$_invoke$arity$1(atch) : remove_cb.call(null,atch));
} else {
return null;
}
});})(i__39447,atch,atch_key,created_at,file_name,size,subtitle,idx,c__4527__auto__,size__4528__auto__,b__39449,s__39446__$2,temp__5735__auto__,atc_num,editable_QMARK_,show_all_QMARK_,is_mobile_QMARK_,attachments_num,attachments_list,should_show_expand_QMARK_,attachment_uploading))
], null)], null):null)], null)], null)], null));

var G__39450 = (i__39447 + (1));
i__39447 = G__39450;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__39449),oc$web$components$ui$stream_attachments$iter__39445(cljs.core.chunk_rest(s__39446__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__39449),null);
}
} else {
var idx = cljs.core.first(s__39446__$2);
var atch = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(attachments_list,idx);
var atch_key = ["attachment-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"file-url","file-url",-863738963).cljs$core$IFn$_invoke$arity$1(atch))].join('');
var created_at = new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(atch);
var file_name = new cljs.core.Keyword(null,"file-name","file-name",-1654217259).cljs$core$IFn$_invoke$arity$1(atch);
var size = new cljs.core.Keyword(null,"file-size","file-size",-1900760755).cljs$core$IFn$_invoke$arity$1(atch);
var subtitle = (cljs.core.truth_(size)?clojure.string.lower_case(["(",clojure.contrib.humanize.filesize.cljs$core$IFn$_invoke$arity$variadic(size,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"binary","binary",-1802232288),false,new cljs.core.Keyword(null,"format","format",-1306924766),"%.2f"], 0)),")"].join('')):null);
return cljs.core.cons(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-attachments-item.group","div.stream-attachments-item.group",-2012811858),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),atch_key], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.group","a.group",1741459406),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),new cljs.core.Keyword(null,"file-url","file-url",-863738963).cljs$core$IFn$_invoke$arity$1(atch),new cljs.core.Keyword(null,"target","target",253001721),"_blank"], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.attachment-info.group","div.attachment-info.group",1437571602),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),((editable_QMARK_)?"editable":null)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.attachment-icon","div.attachment-icon",1764419953)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.attachment-labels","div.attachment-labels",154450793),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),((editable_QMARK_)?"edit":null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.attachment-name","span.attachment-name",1142286372),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.hide_class], null),file_name], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.attachment-description","span.attachment-description",1542423977),subtitle], null)], null),((editable_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.remove-attachment-bt","button.mlb-reset.remove-attachment-bt",-1678681164),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),((is_mobile_QMARK_)?null:(function (){

return "tooltip";
})()
),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"data-delay","data-delay",1974747786),"{\"show\":\"500\", \"hide\":\"0\"}",new cljs.core.Keyword(null,"title","title",636505583),"Remove attachment",new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (atch,atch_key,created_at,file_name,size,subtitle,idx,s__39446__$2,temp__5735__auto__,atc_num,editable_QMARK_,show_all_QMARK_,is_mobile_QMARK_,attachments_num,attachments_list,should_show_expand_QMARK_,attachment_uploading){
return (function (p1__39443_SHARP_){
oc.web.lib.utils.event_stop(p1__39443_SHARP_);

oc.web.lib.utils.remove_tooltips();

if(cljs.core.fn_QMARK_(remove_cb)){
return (remove_cb.cljs$core$IFn$_invoke$arity$1 ? remove_cb.cljs$core$IFn$_invoke$arity$1(atch) : remove_cb.call(null,atch));
} else {
return null;
}
});})(atch,atch_key,created_at,file_name,size,subtitle,idx,s__39446__$2,temp__5735__auto__,atc_num,editable_QMARK_,show_all_QMARK_,is_mobile_QMARK_,attachments_num,attachments_list,should_show_expand_QMARK_,attachment_uploading))
], null)], null):null)], null)], null)], null),oc$web$components$ui$stream_attachments$iter__39445(cljs.core.rest(s__39446__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(attachments_list)));
})(),((should_show_expand_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-attachments-show-more","div.stream-attachments-show-more",1623513883),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),expand_cb], null),["+ ",cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.count(attachments) - cljs.core.count(attachments_list)))," more"].join('')], null):null)], null)], null):null));
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.stream-attachments","attachments-dropdown","oc.web.components.ui.stream-attachments/attachments-dropdown",1266372445)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864)], 0))], null),"stream-attachments");

//# sourceMappingURL=oc.web.components.ui.stream_attachments.js.map
