goog.provide('oc.web.lib.image_upload');
oc.web.lib.image_upload._fs = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
oc.web.lib.image_upload.init_filestack = (function oc$web$lib$image_upload$init_filestack(){
var or__4126__auto__ = cljs.core.deref(oc.web.lib.image_upload._fs);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var new_fs = filestack.init(oc.web.local_settings.filestack_key);
cljs.core.reset_BANG_(oc.web.lib.image_upload._fs,new_fs);

return new_fs;
}
});
oc.web.lib.image_upload.store_to = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"container","container",-1736937707),oc.web.local_settings.attachments_bucket,new cljs.core.Keyword(null,"region","region",270415120),"us-east-1",new cljs.core.Keyword(null,"location","location",1815599388),"s3"], null);
oc.web.lib.image_upload.upload_BANG_ = (function oc$web$lib$image_upload$upload_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38226 = arguments.length;
var i__4737__auto___38227 = (0);
while(true){
if((i__4737__auto___38227 < len__4736__auto___38226)){
args__4742__auto__.push((arguments[i__4737__auto___38227]));

var G__38228 = (i__4737__auto___38227 + (1));
i__4737__auto___38227 = G__38228;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((4) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((4)),(0),null)):null);
return oc.web.lib.image_upload.upload_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),argseq__4743__auto__);
});

(oc.web.lib.image_upload.upload_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (config,success_cb,progress_cb,error_cb,p__38204){
var vec__38205 = p__38204;
var close_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38205,(0),null);
var finished_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38205,(1),null);
var selected_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38205,(2),null);
var started_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38205,(3),null);
var from_sources = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"accept","accept",1874130431).cljs$core$IFn$_invoke$arity$1(config),"image/*"))?new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, ["local_file_system","imagesearch","googledrive","dropbox","onedrive","box"], null):new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, ["local_file_system","googledrive","dropbox","onedrive","box"], null));
var base_config = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"onFileUploadFailed","onFileUploadFailed",-932792540),new cljs.core.Keyword(null,"onFileUploadStarted","onFileUploadStarted",400148997),new cljs.core.Keyword(null,"transformations","transformations",-1807703897),new cljs.core.Keyword(null,"onClose","onClose",1513531338),new cljs.core.Keyword(null,"storeTo","storeTo",1625604716),new cljs.core.Keyword(null,"fromSources","fromSources",-481915603),new cljs.core.Keyword(null,"onFileUploadProgress","onFileUploadProgress",-1096058062),new cljs.core.Keyword(null,"onFileSelected","onFileSelected",629263415),new cljs.core.Keyword(null,"maxFiles","maxFiles",-1897204198),new cljs.core.Keyword(null,"maxSize","maxSize",-1434884613),new cljs.core.Keyword(null,"onFileUploadFinished","onFileUploadFinished",-593292611)],[error_cb,(function (res){
if(cljs.core.fn_QMARK_(started_cb)){
return (started_cb.cljs$core$IFn$_invoke$arity$1 ? started_cb.cljs$core$IFn$_invoke$arity$1(res) : started_cb.call(null,res));
} else {
return null;
}
}),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"crop","crop",793731643),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"circle","circle",1903212362),true], null),new cljs.core.Keyword(null,"rotate","rotate",152705015),true], null),(function (){
if(cljs.core.fn_QMARK_(close_cb)){
return (close_cb.cljs$core$IFn$_invoke$arity$0 ? close_cb.cljs$core$IFn$_invoke$arity$0() : close_cb.call(null));
} else {
return null;
}
}),oc.web.lib.image_upload.store_to,from_sources,(function (res,progress){
if(cljs.core.fn_QMARK_(progress_cb)){
return (progress_cb.cljs$core$IFn$_invoke$arity$2 ? progress_cb.cljs$core$IFn$_invoke$arity$2(res,progress) : progress_cb.call(null,res,progress));
} else {
return null;
}
}),(function (res){
if(cljs.core.fn_QMARK_(selected_cb)){
return (selected_cb.cljs$core$IFn$_invoke$arity$1 ? selected_cb.cljs$core$IFn$_invoke$arity$1(res) : selected_cb.call(null,res));
} else {
return null;
}
}),(1),oc.web.local_settings.file_upload_size,(function (res){
if(cljs.core.fn_QMARK_(finished_cb)){
return (finished_cb.cljs$core$IFn$_invoke$arity$1 ? finished_cb.cljs$core$IFn$_invoke$arity$1(res) : finished_cb.call(null,res));
} else {
return null;
}
})]);
var config__$1 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([base_config,config], 0));
var fs = oc.web.lib.image_upload.init_filestack();
return fs.pick(cljs.core.clj__GT_js(config__$1)).then((function (res){
var files_uploaded = goog.object.get(res,"filesUploaded");
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.count(files_uploaded),(1))){
var G__38208 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(files_uploaded,(0));
return (success_cb.cljs$core$IFn$_invoke$arity$1 ? success_cb.cljs$core$IFn$_invoke$arity$1(G__38208) : success_cb.call(null,G__38208));
} else {
return null;
}
}));
}));

(oc.web.lib.image_upload.upload_BANG_.cljs$lang$maxFixedArity = (4));

/** @this {Function} */
(oc.web.lib.image_upload.upload_BANG_.cljs$lang$applyTo = (function (seq38199){
var G__38200 = cljs.core.first(seq38199);
var seq38199__$1 = cljs.core.next(seq38199);
var G__38201 = cljs.core.first(seq38199__$1);
var seq38199__$2 = cljs.core.next(seq38199__$1);
var G__38202 = cljs.core.first(seq38199__$2);
var seq38199__$3 = cljs.core.next(seq38199__$2);
var G__38203 = cljs.core.first(seq38199__$3);
var seq38199__$4 = cljs.core.next(seq38199__$3);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38200,G__38201,G__38202,G__38203,seq38199__$4);
}));

oc.web.lib.image_upload.upload_file_BANG_ = (function oc$web$lib$image_upload$upload_file_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38229 = arguments.length;
var i__4737__auto___38230 = (0);
while(true){
if((i__4737__auto___38230 < len__4736__auto___38229)){
args__4742__auto__.push((arguments[i__4737__auto___38230]));

var G__38231 = (i__4737__auto___38230 + (1));
i__4737__auto___38230 = G__38231;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.lib.image_upload.upload_file_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.lib.image_upload.upload_file_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (file,success_cb,p__38213){
var vec__38214 = p__38213;
var error_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38214,(0),null);
var progress_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38214,(1),null);
try{var fs_client = oc.web.lib.image_upload.init_filestack();
return fs_client.upload(file,({"onProgress": (function (p1__38209_SHARP_){
if(cljs.core.fn_QMARK_(progress_cb)){
var G__38218 = p1__38209_SHARP_.totalPercent;
return (progress_cb.cljs$core$IFn$_invoke$arity$1 ? progress_cb.cljs$core$IFn$_invoke$arity$1(G__38218) : progress_cb.call(null,G__38218));
} else {
return null;
}
})})).then((function (res){
var url = goog.object.get(res,"url");
if(cljs.core.fn_QMARK_(success_cb)){
return (success_cb.cljs$core$IFn$_invoke$arity$1 ? success_cb.cljs$core$IFn$_invoke$arity$1(url) : success_cb.call(null,url));
} else {
return null;
}
}));
}catch (e38217){var e = e38217;
oc.web.lib.sentry.capture_error_BANG_.cljs$core$IFn$_invoke$arity$1(e);

if(cljs.core.fn_QMARK_(error_cb)){
return (error_cb.cljs$core$IFn$_invoke$arity$1 ? error_cb.cljs$core$IFn$_invoke$arity$1(e) : error_cb.call(null,e));
} else {
return null;
}
}}));

(oc.web.lib.image_upload.upload_file_BANG_.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.lib.image_upload.upload_file_BANG_.cljs$lang$applyTo = (function (seq38210){
var G__38211 = cljs.core.first(seq38210);
var seq38210__$1 = cljs.core.next(seq38210);
var G__38212 = cljs.core.first(seq38210__$1);
var seq38210__$2 = cljs.core.next(seq38210__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38211,G__38212,seq38210__$2);
}));

oc.web.lib.image_upload.thumbnail_BANG_ = (function oc$web$lib$image_upload$thumbnail_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38232 = arguments.length;
var i__4737__auto___38233 = (0);
while(true){
if((i__4737__auto___38233 < len__4736__auto___38232)){
args__4742__auto__.push((arguments[i__4737__auto___38233]));

var G__38234 = (i__4737__auto___38233 + (1));
i__4737__auto___38233 = G__38234;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.image_upload.thumbnail_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.image_upload.thumbnail_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (fs_url,p__38221){
var vec__38222 = p__38221;
var success_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38222,(0),null);
var progress_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38222,(1),null);
var error_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38222,(2),null);
var fs_client = oc.web.lib.image_upload.init_filestack();
var opts = cljs.core.clj__GT_js(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"resize","resize",297367086),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"fit","fit",869444807),"crop",new cljs.core.Keyword(null,"width","width",-384071477),(272),new cljs.core.Keyword(null,"height","height",1025178622),(204),new cljs.core.Keyword(null,"align","align",1964212802),"faces"], null)], null));
var transformed_url = fs_client.transform(fs_url,opts);
var fixed_success_cb = (function (res){
if(cljs.core.fn_QMARK_(success_cb)){
return (success_cb.cljs$core$IFn$_invoke$arity$1 ? success_cb.cljs$core$IFn$_invoke$arity$1(res) : success_cb.call(null,res));
} else {
return null;
}
});
var fixed_progress_cb = (function (res,progress){
if(cljs.core.fn_QMARK_(progress_cb)){
return (progress_cb.cljs$core$IFn$_invoke$arity$2 ? progress_cb.cljs$core$IFn$_invoke$arity$2(res,progress) : progress_cb.call(null,res,progress));
} else {
return null;
}
});
var fixed_error_cb = (function (res,err){
if(cljs.core.fn_QMARK_(error_cb)){
return (error_cb.cljs$core$IFn$_invoke$arity$2 ? error_cb.cljs$core$IFn$_invoke$arity$2(res,err) : error_cb.call(null,res,err));
} else {
return null;
}
});
var storing_task = fs_client.storeURL(transformed_url,cljs.core.clj__GT_js(oc.web.lib.image_upload.store_to),fixed_success_cb,fixed_error_cb,fixed_progress_cb);
try{return storing_task.then((function (res){
var url = goog.object.get(res,"url");
if(cljs.core.fn_QMARK_(success_cb)){
return (success_cb.cljs$core$IFn$_invoke$arity$1 ? success_cb.cljs$core$IFn$_invoke$arity$1(url) : success_cb.call(null,url));
} else {
return null;
}
}));
}catch (e38225){var e = e38225;
oc.web.lib.sentry.capture_error_BANG_.cljs$core$IFn$_invoke$arity$1(e);

if(cljs.core.fn_QMARK_(error_cb)){
return (error_cb.cljs$core$IFn$_invoke$arity$1 ? error_cb.cljs$core$IFn$_invoke$arity$1(e) : error_cb.call(null,e));
} else {
return null;
}
}}));

(oc.web.lib.image_upload.thumbnail_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.image_upload.thumbnail_BANG_.cljs$lang$applyTo = (function (seq38219){
var G__38220 = cljs.core.first(seq38219);
var seq38219__$1 = cljs.core.next(seq38219);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38220,seq38219__$1);
}));


//# sourceMappingURL=oc.web.lib.image_upload.js.map
