goog.provide('oc.web.lib.utils');
oc.web.lib.utils.oc_animation_duration = (300);
/**
 * true if seq contains elm
 */
oc.web.lib.utils.in_QMARK_ = (function oc$web$lib$utils$in_QMARK_(coll,elm){
return cljs.core.some((function (p1__41062_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(elm,p1__41062_SHARP_);
}),coll);
});
oc.web.lib.utils.vec_dissoc = (function oc$web$lib$utils$vec_dissoc(coll,elem){
return cljs.core.filterv((function (p1__41063_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(elem,p1__41063_SHARP_);
}),coll);
});
oc.web.lib.utils.full_month_string = (function oc$web$lib$utils$full_month_string(month){
var G__41064 = month;
switch (G__41064) {
case (1):
return "January";

break;
case (2):
return "February";

break;
case (3):
return "March";

break;
case (4):
return "April";

break;
case (5):
return "May";

break;
case (6):
return "June";

break;
case (7):
return "July";

break;
case (8):
return "August";

break;
case (9):
return "September";

break;
case (10):
return "October";

break;
case (11):
return "November";

break;
case (12):
return "December";

break;
default:
return "";

}
});
/**
 * Return the name of the month given the integer month number. Accept flags to transform the string:
 *   - :short Return only the first 3 letters of the month name: JAN
 *   - :capitalize Return the capitalized name: January
 *   - :uppercase Return the uppercase: JANUARY
 */
oc.web.lib.utils.month_string_int = (function oc$web$lib$utils$month_string_int(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41222 = arguments.length;
var i__4737__auto___41223 = (0);
while(true){
if((i__4737__auto___41223 < len__4736__auto___41222)){
args__4742__auto__.push((arguments[i__4737__auto___41223]));

var G__41224 = (i__4737__auto___41223 + (1));
i__4737__auto___41223 = G__41224;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.month_string_int.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.month_string_int.cljs$core$IFn$_invoke$arity$variadic = (function (month,p__41067){
var vec__41068 = p__41067;
var flags = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41068,(0),null);
var short_month = oc.web.lib.utils.in_QMARK_(flags,new cljs.core.Keyword(null,"short","short",1928760516));
var capitalize = oc.web.lib.utils.in_QMARK_(flags,new cljs.core.Keyword(null,"capitalize","capitalize",511160605));
var uppercase = oc.web.lib.utils.in_QMARK_(flags,new cljs.core.Keyword(null,"uppercase","uppercase",2080890922));
var month_string = oc.web.lib.utils.full_month_string(month);
var short_string = (cljs.core.truth_(short_month)?cljs.core.subs.cljs$core$IFn$_invoke$arity$3(month_string,(0),(3)):month_string);
var capitalized_string = (cljs.core.truth_(capitalize)?cuerdas.core.capital(short_string):short_string);
var uppercase_string = (cljs.core.truth_(uppercase)?cuerdas.core.upper(capitalized_string):capitalized_string);
return uppercase_string;
}));

(oc.web.lib.utils.month_string_int.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.month_string_int.cljs$lang$applyTo = (function (seq41065){
var G__41066 = cljs.core.first(seq41065);
var seq41065__$1 = cljs.core.next(seq41065);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41066,seq41065__$1);
}));

oc.web.lib.utils.month_string = (function oc$web$lib$utils$month_string(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41225 = arguments.length;
var i__4737__auto___41226 = (0);
while(true){
if((i__4737__auto___41226 < len__4736__auto___41225)){
args__4742__auto__.push((arguments[i__4737__auto___41226]));

var G__41227 = (i__4737__auto___41226 + (1));
i__4737__auto___41226 = G__41227;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.month_string.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.month_string.cljs$core$IFn$_invoke$arity$variadic = (function (month,p__41075){
var vec__41076 = p__41075;
var flags = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41076,(0),null);
return oc.web.lib.utils.month_string_int.cljs$core$IFn$_invoke$arity$variadic(window.parseInt(month,(10)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([flags], 0));
}));

(oc.web.lib.utils.month_string.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.month_string.cljs$lang$applyTo = (function (seq41071){
var G__41072 = cljs.core.first(seq41071);
var seq41071__$1 = cljs.core.next(seq41071);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41072,seq41071__$1);
}));

oc.web.lib.utils.camel_case_str = (function oc$web$lib$utils$camel_case_str(value){
if(cljs.core.truth_(value)){
var upper_value = clojure.string.replace(value,/^(\w)/,(function (p1__41079_SHARP_){
return clojure.string.upper_case(cljs.core.first(p1__41079_SHARP_));
}));
return clojure.string.replace(upper_value,/-(\w)/,(function (p1__41080_SHARP_){
return [" ",clojure.string.upper_case(cljs.core.second(p1__41080_SHARP_))].join('');
}));
} else {
return null;
}
});
oc.web.lib.utils.get_week_day = (function oc$web$lib$utils$get_week_day(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41236 = arguments.length;
var i__4737__auto___41237 = (0);
while(true){
if((i__4737__auto___41237 < len__4736__auto___41236)){
args__4742__auto__.push((arguments[i__4737__auto___41237]));

var G__41238 = (i__4737__auto___41237 + (1));
i__4737__auto___41237 = G__41238;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.get_week_day.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.get_week_day.cljs$core$IFn$_invoke$arity$variadic = (function (day,p__41089){
var vec__41090 = p__41089;
var short_day = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41090,(0),null);
var G__41093 = day;
switch (G__41093) {
case (1):
if(cljs.core.truth_(short_day)){
return "Mon";
} else {
return "Monday";
}

break;
case (2):
if(cljs.core.truth_(short_day)){
return "Tue";
} else {
return "Tuesday";
}

break;
case (3):
if(cljs.core.truth_(short_day)){
return "Wed";
} else {
return "Wednesday";
}

break;
case (4):
if(cljs.core.truth_(short_day)){
return "Thu";
} else {
return "Thursday";
}

break;
case (5):
if(cljs.core.truth_(short_day)){
return "Fri";
} else {
return "Friday";
}

break;
case (6):
if(cljs.core.truth_(short_day)){
return "Sat";
} else {
return "Saturday";
}

break;
default:
if(cljs.core.truth_(short_day)){
return "Sun";
} else {
return "Sunday";
}

}
}));

(oc.web.lib.utils.get_week_day.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.get_week_day.cljs$lang$applyTo = (function (seq41083){
var G__41084 = cljs.core.first(seq41083);
var seq41083__$1 = cljs.core.next(seq41083);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41084,seq41083__$1);
}));

oc.web.lib.utils.js_date = (function oc$web$lib$utils$js_date(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41242 = arguments.length;
var i__4737__auto___41243 = (0);
while(true){
if((i__4737__auto___41243 < len__4736__auto___41242)){
args__4742__auto__.push((arguments[i__4737__auto___41243]));

var G__41244 = (i__4737__auto___41243 + (1));
i__4737__auto___41243 = G__41244;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic = (function (p__41099){
var vec__41100 = p__41099;
var date_str = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41100,(0),null);
if(cljs.core.truth_(date_str)){
return (new Date(date_str));
} else {
return (new Date());
}
}));

(oc.web.lib.utils.js_date.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.lib.utils.js_date.cljs$lang$applyTo = (function (seq41096){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq41096));
}));

oc.web.lib.utils.add_zero = (function oc$web$lib$utils$add_zero(v){
return [(((v < (10)))?"0":null),cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)].join('');
});
oc.web.lib.utils.date_string = (function oc$web$lib$utils$date_string(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41247 = arguments.length;
var i__4737__auto___41248 = (0);
while(true){
if((i__4737__auto___41248 < len__4736__auto___41247)){
args__4742__auto__.push((arguments[i__4737__auto___41248]));

var G__41249 = (i__4737__auto___41248 + (1));
i__4737__auto___41248 = G__41249;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.date_string.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.date_string.cljs$core$IFn$_invoke$arity$variadic = (function (js_date,p__41113){
var vec__41114 = p__41113;
var flags = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41114,(0),null);
var month = oc.web.lib.utils.month_string.cljs$core$IFn$_invoke$arity$variadic(oc.web.lib.utils.add_zero((js_date.getMonth() + (1))),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(cljs.core.truth_((function (){var or__4126__auto__ = oc.web.lib.utils.in_QMARK_(flags,new cljs.core.Keyword(null,"short-month","short-month",863577052));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.lib.utils.in_QMARK_(flags,new cljs.core.Keyword(null,"short","short",1928760516));
}
})())?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"short","short",1928760516)], null):null)], 0));
var day = js_date.getDate();
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(month)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(day),(cljs.core.truth_(oc.web.lib.utils.in_QMARK_(flags,new cljs.core.Keyword(null,"year","year",335913393)))?[", ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_date.getFullYear())].join(''):null)].join('');
}));

(oc.web.lib.utils.date_string.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.date_string.cljs$lang$applyTo = (function (seq41103){
var G__41104 = cljs.core.first(seq41103);
var seq41103__$1 = cljs.core.next(seq41103);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41104,seq41103__$1);
}));

oc.web.lib.utils.pluralize = (function oc$web$lib$utils$pluralize(string,n){
if((n > (1))){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(string),"s"].join('');
} else {
return string;
}
});
/**
 * Get a string representing the elapsed time from a date in the past
 */
oc.web.lib.utils.time_since = (function oc$web$lib$utils$time_since(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41254 = arguments.length;
var i__4737__auto___41255 = (0);
while(true){
if((i__4737__auto___41255 < len__4736__auto___41254)){
args__4742__auto__.push((arguments[i__4737__auto___41255]));

var G__41256 = (i__4737__auto___41255 + (1));
i__4737__auto___41255 = G__41256;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.time_since.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.time_since.cljs$core$IFn$_invoke$arity$variadic = (function (past_date,p__41119){
var vec__41120 = p__41119;
var flags = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41120,(0),null);
var past_js_date = oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([past_date], 0));
var past = past_js_date.getTime();
var now_date = oc.web.lib.utils.js_date();
var now = now_date.getTime();
var seconds = Math.floor(((now - past) / (1000)));
var years_interval = Math.floor((seconds / (31536000)));
var months_interval = Math.floor((seconds / (2592000)));
var days_interval = Math.floor((seconds / (86400)));
var hours_interval = Math.floor((seconds / (3600)));
var minutes_interval = Math.floor((seconds / (60)));
var short_QMARK_ = oc.web.lib.utils.in_QMARK_(flags,new cljs.core.Keyword(null,"short","short",1928760516));
var date_prefix = oc.web.lib.utils.in_QMARK_(flags,new cljs.core.Keyword(null,"date-prefix","date-prefix",1109458669));
var lower_case = oc.web.lib.utils.in_QMARK_(flags,new cljs.core.Keyword(null,"lower-case","lower-case",-212358583));
if((years_interval > (0))){
return [(cljs.core.truth_(date_prefix)?" on ":null),oc.web.lib.utils.date_string.cljs$core$IFn$_invoke$arity$variadic(past_js_date,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.concat.cljs$core$IFn$_invoke$arity$2(flags,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"year","year",335913393)], null))], 0))].join('');
} else {
if((months_interval > (0))){
return [(cljs.core.truth_(date_prefix)?" on ":null),oc.web.lib.utils.date_string.cljs$core$IFn$_invoke$arity$variadic(past_js_date,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([flags], 0))].join('');
} else {
if((days_interval > (0))){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(days_interval),(cljs.core.truth_(short_QMARK_)?"d":[" ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.pluralize("day",days_interval))," ago"].join(''))].join('');
} else {
if((hours_interval > (0))){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(hours_interval),(cljs.core.truth_(short_QMARK_)?"h":[" ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.pluralize("hour",hours_interval))," ago"].join(''))].join('');
} else {
if((minutes_interval > (0))){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(minutes_interval),(cljs.core.truth_(short_QMARK_)?"m":[" ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.pluralize("minute",minutes_interval))," ago"].join(''))].join('');
} else {
if(cljs.core.truth_(short_QMARK_)){
return "now";
} else {
if(cljs.core.truth_(lower_case)){
return "just now";
} else {
return "Just now";
}
}

}
}
}
}
}
}));

(oc.web.lib.utils.time_since.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.time_since.cljs$lang$applyTo = (function (seq41117){
var G__41118 = cljs.core.first(seq41117);
var seq41117__$1 = cljs.core.next(seq41117);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41118,seq41117__$1);
}));

oc.web.lib.utils.time_without_leading_zeros = (function oc$web$lib$utils$time_without_leading_zeros(time_string){
return time_string.replace((new RegExp("^0([0-9])*","ig")),"$1");
});
oc.web.lib.utils.local_date_time = (function oc$web$lib$utils$local_date_time(past_date){
var time_string = past_date.toLocaleTimeString(window.navigator.language,({"hour": "2-digit", "minute": "2-digit", "format": "hour:minute"}));
var without_leading_zeros = oc.web.lib.utils.time_without_leading_zeros(time_string);
return cuerdas.core.upper(without_leading_zeros);
});
oc.web.lib.utils.foc_date_time = (function oc$web$lib$utils$foc_date_time(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41269 = arguments.length;
var i__4737__auto___41270 = (0);
while(true){
if((i__4737__auto___41270 < len__4736__auto___41269)){
args__4742__auto__.push((arguments[i__4737__auto___41270]));

var G__41271 = (i__4737__auto___41270 + (1));
i__4737__auto___41270 = G__41271;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.foc_date_time.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.foc_date_time.cljs$core$IFn$_invoke$arity$variadic = (function (past_date,p__41141){
var vec__41142 = p__41141;
var flags = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41142,(0),null);
var past_js_date = oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([past_date], 0));
var past = past_js_date.getTime();
var now_date = oc.web.lib.utils.js_date();
var now = now_date.getTime();
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(past_js_date.getFullYear(),now_date.getFullYear())) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(past_js_date.getMonth(),now_date.getMonth())) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(past_js_date.getDate(),now_date.getDate())))){
return oc.web.lib.utils.local_date_time(past_js_date);
} else {
return oc.web.lib.utils.time_since.cljs$core$IFn$_invoke$arity$variadic(past_date,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.concat.cljs$core$IFn$_invoke$arity$2(flags,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"short","short",1928760516)], null))], 0));
}
}));

(oc.web.lib.utils.foc_date_time.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.foc_date_time.cljs$lang$applyTo = (function (seq41138){
var G__41139 = cljs.core.first(seq41138);
var seq41138__$1 = cljs.core.next(seq41138);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41139,seq41138__$1);
}));

oc.web.lib.utils.explore_date_time = (function oc$web$lib$utils$explore_date_time(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41272 = arguments.length;
var i__4737__auto___41273 = (0);
while(true){
if((i__4737__auto___41273 < len__4736__auto___41272)){
args__4742__auto__.push((arguments[i__4737__auto___41273]));

var G__41274 = (i__4737__auto___41273 + (1));
i__4737__auto___41273 = G__41274;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.explore_date_time.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.explore_date_time.cljs$core$IFn$_invoke$arity$variadic = (function (past_date,p__41153){
var vec__41155 = p__41153;
var flags = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41155,(0),null);
var past_js_date = oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([past_date], 0));
var past = past_js_date.getTime();
var now_date = oc.web.lib.utils.js_date();
var now = now_date.getTime();
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(past_js_date.getFullYear(),now_date.getFullYear())) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(past_js_date.getMonth(),now_date.getMonth())) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(past_js_date.getDate(),now_date.getDate())))){
return [" at ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.local_date_time(past_js_date))].join('');
} else {
return oc.web.lib.utils.time_since.cljs$core$IFn$_invoke$arity$variadic(past_date,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.concat.cljs$core$IFn$_invoke$arity$2(flags,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"date-prefix","date-prefix",1109458669),new cljs.core.Keyword(null,"lower-case","lower-case",-212358583)], null))], 0));
}
}));

(oc.web.lib.utils.explore_date_time.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.explore_date_time.cljs$lang$applyTo = (function (seq41149){
var G__41150 = cljs.core.first(seq41149);
var seq41149__$1 = cljs.core.next(seq41149);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41150,seq41149__$1);
}));

/**
 * Given a map of class names as keys return a string of the those classes that evaulates as true
 */
oc.web.lib.utils.class_set = (function oc$web$lib$utils$class_set(classes){
return clojure.string.join.cljs$core$IFn$_invoke$arity$1(cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__41159_SHARP_){
return [" ",cljs.core.name(p1__41159_SHARP_)].join('');
}),cljs.core.keys(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__41160_SHARP_){
var and__4115__auto__ = cljs.core.first(p1__41160_SHARP_);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.second(p1__41160_SHARP_);
} else {
return and__4115__auto__;
}
}),classes))));
});
oc.web.lib.utils.link_for = (function oc$web$lib$utils$link_for(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41276 = arguments.length;
var i__4737__auto___41279 = (0);
while(true){
if((i__4737__auto___41279 < len__4736__auto___41276)){
args__4742__auto__.push((arguments[i__4737__auto___41279]));

var G__41280 = (i__4737__auto___41279 + (1));
i__4737__auto___41279 = G__41280;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic = (function (args){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(oc.lib.hateoas.link_for,args);
}));

(oc.web.lib.utils.link_for.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.lib.utils.link_for.cljs$lang$applyTo = (function (seq41161){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq41161));
}));

oc.web.lib.utils.as_of_now = (function oc$web$lib$utils$as_of_now(){
var date = oc.web.lib.utils.js_date();
return date.toISOString();
});
oc.web.lib.utils.css_color = (function oc$web$lib$utils$css_color(color){
var colors = cljs.core.subvec.cljs$core$IFn$_invoke$arity$2(clojure.string.split.cljs$core$IFn$_invoke$arity$2(color,(new RegExp(""))),(2));
var red = cljs.core.take.cljs$core$IFn$_invoke$arity$2((2),colors);
var green = cljs.core.take.cljs$core$IFn$_invoke$arity$2((2),cljs.core.drop.cljs$core$IFn$_invoke$arity$2((2),colors));
var blue = cljs.core.take.cljs$core$IFn$_invoke$arity$2((2),cljs.core.drop.cljs$core$IFn$_invoke$arity$2((4),colors));
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__41162_SHARP_){
return cljs.reader.read_string.cljs$core$IFn$_invoke$arity$1(clojure.string.join.cljs$core$IFn$_invoke$arity$1(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(p1__41162_SHARP_,"0x")));
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [red,green,blue], null));
});
oc.web.lib.utils.scroll_to_y = (function oc$web$lib$utils$scroll_to_y(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41286 = arguments.length;
var i__4737__auto___41290 = (0);
while(true){
if((i__4737__auto___41290 < len__4736__auto___41286)){
args__4742__auto__.push((arguments[i__4737__auto___41290]));

var G__41299 = (i__4737__auto___41290 + (1));
i__4737__auto___41290 = G__41299;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.scroll_to_y.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.scroll_to_y.cljs$core$IFn$_invoke$arity$variadic = (function (scroll_y,p__41183){
var vec__41184 = p__41183;
var duration = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41184,(0),null);
if(cljs.core.truth_((function (){var and__4115__auto__ = duration;
if(cljs.core.truth_(and__4115__auto__)){
return (duration === (0));
} else {
return and__4115__auto__;
}
})())){
if(cljs.core.truth_(oc.shared.useragent.edge_QMARK_)){
return (document.scrollingElement.scrollTop = scroll_y);
} else {
return document.scrollingElement.scrollTo((0),scroll_y);
}
} else {
return (new goog.fx.dom.Scroll(document.scrollingElement,[(0),window.scrollY],[(0),scroll_y],((cljs.core.integer_QMARK_(duration))?duration:oc.web.lib.utils.oc_animation_duration))).play();
}
}));

(oc.web.lib.utils.scroll_to_y.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.scroll_to_y.cljs$lang$applyTo = (function (seq41181){
var G__41182 = cljs.core.first(seq41181);
var seq41181__$1 = cljs.core.next(seq41181);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41182,seq41181__$1);
}));

oc.web.lib.utils.scroll_to_bottom = (function oc$web$lib$utils$scroll_to_bottom(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41309 = arguments.length;
var i__4737__auto___41310 = (0);
while(true){
if((i__4737__auto___41310 < len__4736__auto___41309)){
args__4742__auto__.push((arguments[i__4737__auto___41310]));

var G__41312 = (i__4737__auto___41310 + (1));
i__4737__auto___41310 = G__41312;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.scroll_to_bottom.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.scroll_to_bottom.cljs$core$IFn$_invoke$arity$variadic = (function (elem,p__41189){
var vec__41190 = p__41189;
var animated = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41190,(0),null);
var elem_scroll_top = elem.scrollHeight;
return (new goog.fx.dom.Scroll(elem,[(0),elem.scrollTop],[(0),elem_scroll_top],(cljs.core.truth_(animated)?(320):oc.web.lib.utils.oc_animation_duration))).play();
}));

(oc.web.lib.utils.scroll_to_bottom.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.scroll_to_bottom.cljs$lang$applyTo = (function (seq41187){
var G__41188 = cljs.core.first(seq41187);
var seq41187__$1 = cljs.core.next(seq41187);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41188,seq41187__$1);
}));

oc.web.lib.utils.scroll_to_element = (function oc$web$lib$utils$scroll_to_element(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41315 = arguments.length;
var i__4737__auto___41316 = (0);
while(true){
if((i__4737__auto___41316 < len__4736__auto___41315)){
args__4742__auto__.push((arguments[i__4737__auto___41316]));

var G__41319 = (i__4737__auto___41316 + (1));
i__4737__auto___41316 = G__41319;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.scroll_to_element.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.scroll_to_element.cljs$core$IFn$_invoke$arity$variadic = (function (elem,p__41195){
var vec__41196 = p__41195;
var offset = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41196,(0),null);
var duration = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41196,(1),null);
var elem_scroll_top = (elem.offsetTop + (function (){var or__4126__auto__ = offset;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (0);
}
})());
return oc.web.lib.utils.scroll_to_y.cljs$core$IFn$_invoke$arity$variadic(elem_scroll_top,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([duration], 0));
}));

(oc.web.lib.utils.scroll_to_element.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.scroll_to_element.cljs$lang$applyTo = (function (seq41193){
var G__41194 = cljs.core.first(seq41193);
var seq41193__$1 = cljs.core.next(seq41193);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41194,seq41193__$1);
}));

oc.web.lib.utils.is_test_env_QMARK_ = (function oc$web$lib$utils$is_test_env_QMARK_(){
return (!(cljs.core.not(window._phantom)));
});
oc.web.lib.utils.after = (function oc$web$lib$utils$after(ms,fn){
return setTimeout(fn,ms);
});
oc.web.lib.utils.maybe_after = (function oc$web$lib$utils$maybe_after(ms,fn){
if((ms === (0))){
return (fn.cljs$core$IFn$_invoke$arity$0 ? fn.cljs$core$IFn$_invoke$arity$0() : fn.call(null));
} else {
return setTimeout(fn,ms);
}
});
oc.web.lib.utils.every = (function oc$web$lib$utils$every(ms,fn){
return setInterval(fn,ms);
});
/**
 * Take a string containing either Unicode emoji (mobile device keyboard), short code emoji (web app),
 *   or ASCII emoji (old skool) and convert it to HTML string ready to be added to the DOM (dangerously)
 *   with emoji image tags via the Emoji One lib and resources.
 */
oc.web.lib.utils.emojify = (function oc$web$lib$utils$emojify(text){
return ({"__html": text});
});
oc.web.lib.utils.strip_HTML_tags = (function oc$web$lib$utils$strip_HTML_tags(text){
if(cljs.core.truth_(text)){
var reg = (new RegExp("</?[^>]+(>|$)","ig"));
return text.replace(reg,"");
} else {
return null;
}
});
oc.web.lib.utils.strip_img_tags = (function oc$web$lib$utils$strip_img_tags(text){
if(cljs.core.truth_(text)){
var reg = (new RegExp("<img/?[^>]+(>|$)","ig"));
return text.replace(reg,"");
} else {
return null;
}
});
oc.web.lib.utils.strip_br_tags = (function oc$web$lib$utils$strip_br_tags(text){
if(cljs.core.truth_(text)){
var reg = (new RegExp("<br[ ]{0,}/?>","ig"));
return text.replace(reg,"");
} else {
return null;
}
});
oc.web.lib.utils.strip_empty_tags = (function oc$web$lib$utils$strip_empty_tags(text){
if(cljs.core.truth_(text)){
var reg = (new RegExp("<[a-zA-Z]{1,}[ ]{0,}(class)?[ ]{0,}([0-9a-zA-Z-]{0,}=\"[a-zA-Z\\s]{0,}\")?>[ ]{0,}</[a-zA-Z]{1,}[ ]{0,}>","ig"));
return text.replace(reg,"");
} else {
return null;
}
});
/**
 * Generate a 4 char UUID
 */
oc.web.lib.utils.my_uuid = (function oc$web$lib$utils$my_uuid(){
return Math.floor(((Math.random() + (1)) * (65536))).toString((16)).substring((1));
});
/**
 * Generate v4 GUID based on this http://stackoverflow.com/a/2117523
 */
oc.web.lib.utils.guid = (function oc$web$lib$utils$guid(){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.my_uuid()),cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.my_uuid()),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.my_uuid()),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.my_uuid()),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.my_uuid()),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.my_uuid()),cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.my_uuid()),cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.my_uuid())].join('');
});
/**
 * Generate a UUID for an entry
 */
oc.web.lib.utils.activity_uuid = (function oc$web$lib$utils$activity_uuid(){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.my_uuid()),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.my_uuid()),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.my_uuid())].join('');
});
oc.web.lib.utils.event_stop = (function oc$web$lib$utils$event_stop(e){
e.preventDefault();

return e.stopPropagation();
});
oc.web.lib.utils.event_inside_QMARK_ = (function oc$web$lib$utils$event_inside_QMARK_(e,el){
if(cljs.core.truth_(e)){
var element = e.target;
while(true){
if(cljs.core.truth_(element)){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(element,el)){
return true;
} else {
var G__41466 = element.parentElement;
element = G__41466;
continue;
}
} else {
return false;
}
break;
}
} else {
return null;
}
});
oc.web.lib.utils.to_end_of_content_editable = (function oc$web$lib$utils$to_end_of_content_editable(content_editable_element){
if(cljs.core.truth_(document.createRange)){
var rg = document.createRange();
rg.selectNodeContents(content_editable_element);

rg.collapse(false);

var selection = window.getSelection();
selection.removeAllRanges();

return selection.addRange(rg);
} else {
var rg = document.body.createTextRange();
rg.moveToElementText(content_editable_element);

rg.collapse(false);

return rg.select();
}
});
oc.web.lib.utils.valid_email_pattern = "[+a-zA-Z0-9_.!#$%&'*\\/=?^`{|}~-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,63}";
oc.web.lib.utils.valid_email_re = cljs.core.re_pattern(["^",oc.web.lib.utils.valid_email_pattern,"$"].join(''));
oc.web.lib.utils.valid_email_QMARK_ = (function oc$web$lib$utils$valid_email_QMARK_(addr){
if(cljs.core.truth_(addr)){
return goog.format.EmailAddress.isValidAddress(addr);
} else {
return null;
}
});
oc.web.lib.utils.valid_domain_pattern = "@?(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,63}";
oc.web.lib.utils.valid_domain_re = cljs.core.re_pattern(["^",oc.web.lib.utils.valid_domain_pattern,"$"].join(''));
oc.web.lib.utils.valid_domain_QMARK_ = (function oc$web$lib$utils$valid_domain_QMARK_(domain){
if(typeof domain === 'string'){
return cljs.core.re_matches(oc.web.lib.utils.valid_domain_re,domain);
} else {
return null;
}
});
oc.web.lib.utils.remove_tooltips = (function oc$web$lib$utils$remove_tooltips(){
return $("div.tooltip").remove();
});
oc.web.lib.utils.parse_input_email = (function oc$web$lib$utils$parse_input_email(email_address){
var parsed_email = goog.format.EmailAddress.parse(email_address);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"name","name",1843675177),parsed_email.getName(),new cljs.core.Keyword(null,"address","address",559499426),parsed_email.getAddress()], null);
});
/**
 * Given a collection and a function return the index that make the function truely.
 */
oc.web.lib.utils.index_of = (function oc$web$lib$utils$index_of(s,f){
var idx = (0);
var items = s;
while(true){
if(cljs.core.empty_QMARK_(items)){
return null;
} else {
if(cljs.core.truth_((function (){var G__41199 = cljs.core.first(items);
return (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(G__41199) : f.call(null,G__41199));
})())){
return idx;
} else {
var G__41495 = (idx + (1));
var G__41496 = cljs.core.rest(items);
idx = G__41495;
items = G__41496;
continue;

}
}
break;
}
});
oc.web.lib.utils.network_error = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),"Network request",new cljs.core.Keyword(null,"description","description",-1428560544),((oc.shared.useragent.pseudo_native_QMARK_)?"Probably just a temporary issue. Please try again later if this persists.":"Probably just a temporary issue. Please refresh if this persists."),new cljs.core.Keyword(null,"server-error","server-error",-426815993),true,new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"generic-network-error","generic-network-error",1483843484),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true], null);
oc.web.lib.utils.internal_error = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),"Internal error occurred",new cljs.core.Keyword(null,"description","description",-1428560544),["An internal error occurrent, we have been informed of the ","problem and we will be working on it as soon as possible. ","Thanks for understanding."].join(''),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"internal-error","internal-error",-2033790199),new cljs.core.Keyword(null,"server-error","server-error",-426815993),true,new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true], null);
oc.web.lib.utils.clean_google_chart_url = (function oc$web$lib$utils$clean_google_chart_url(gchart_url){
if(typeof gchart_url === 'string'){
return gchart_url.replace(/\/u\/\d+/i,"");
} else {
return "";
}
});
oc.web.lib.utils.valid_google_chart_url_QMARK_ = (function oc$web$lib$utils$valid_google_chart_url_QMARK_(url){
if(typeof url === 'string'){
var cleaned_url = oc.web.lib.utils.clean_google_chart_url(url);
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cleaned_url.indexOf("://docs.google.com/spreadsheets/d/"),(-1));
} else {
return null;
}
});
oc.web.lib.utils.cdn = (function oc$web$lib$utils$cdn(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41502 = arguments.length;
var i__4737__auto___41503 = (0);
while(true){
if((i__4737__auto___41503 < len__4736__auto___41502)){
args__4742__auto__.push((arguments[i__4737__auto___41503]));

var G__41504 = (i__4737__auto___41503 + (1));
i__4737__auto___41503 = G__41504;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.cdn.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.cdn.cljs$core$IFn$_invoke$arity$variadic = (function (img_src,p__41202){
var vec__41203 = p__41202;
var no_deploy_folder = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41203,(0),null);
var use_cdn_QMARK_ = cljs.core.seq(oc.web.local_settings.cdn_url);
var cdn = ((use_cdn_QMARK_)?oc.web.local_settings.cdn_url:"");
var deploy_key = ((cljs.core.empty_QMARK_(oc.web.local_settings.deploy_key))?"":["/",oc.web.local_settings.deploy_key].join(''));
var with_deploy_folder = (cljs.core.truth_(no_deploy_folder)?cdn:[cdn,deploy_key].join(''));
return [((use_cdn_QMARK_)?with_deploy_folder:null),cljs.core.str.cljs$core$IFn$_invoke$arity$1(img_src)].join('');
}));

(oc.web.lib.utils.cdn.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.cdn.cljs$lang$applyTo = (function (seq41200){
var G__41201 = cljs.core.first(seq41200);
var seq41200__$1 = cljs.core.next(seq41200);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41201,seq41200__$1);
}));

oc.web.lib.utils.rgb_with_opacity = (function oc$web$lib$utils$rgb_with_opacity(rgb,opacity){
return ["rgba(",clojure.string.join.cljs$core$IFn$_invoke$arity$2(",",cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.vec(oc.web.lib.utils.css_color(rgb)),opacity)),")"].join('');
});
oc.web.lib.utils.get_24h_time = (function oc$web$lib$utils$get_24h_time(js_date){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_date.getHours()),":",oc.web.lib.utils.add_zero(js_date.getMinutes())].join('');
});
oc.web.lib.utils.get_ampm_time = (function oc$web$lib$utils$get_ampm_time(js_date){
var hours = js_date.getHours();
var minutes = oc.web.lib.utils.add_zero(js_date.getMinutes());
var ampm = (((hours >= (12)))?" PM":" AM");
var hours__$1 = cljs.core.mod(hours,(12));
var hours__$2 = (((hours__$1 === (0)))?(12):hours__$1);
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(hours__$2),":",minutes,ampm].join('');
});
oc.web.lib.utils.format_time_string = (function oc$web$lib$utils$format_time_string(js_date){
var r = RegExp("am|pm","i");
var h12 = (function (){var or__4126__auto__ = js_date.toLocaleTimeString().match(r);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_date).match(r);
}
})();
if(cljs.core.truth_(h12)){
return oc.web.lib.utils.get_ampm_time(js_date);
} else {
return oc.web.lib.utils.get_24h_time(js_date);
}
});
oc.web.lib.utils.activity_date_string = (function oc$web$lib$utils$activity_date_string(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41517 = arguments.length;
var i__4737__auto___41518 = (0);
while(true){
if((i__4737__auto___41518 < len__4736__auto___41517)){
args__4742__auto__.push((arguments[i__4737__auto___41518]));

var G__41519 = (i__4737__auto___41518 + (1));
i__4737__auto___41518 = G__41519;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.activity_date_string.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.activity_date_string.cljs$core$IFn$_invoke$arity$variadic = (function (js_date,p__41208){
var vec__41209 = p__41208;
var hide_time = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41209,(0),null);
var hide_year = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41209,(1),null);
var time_string = oc.web.lib.utils.format_time_string(js_date);
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.full_month_string((js_date.getMonth() + (1))))," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_date.getDate()),(cljs.core.truth_(hide_year)?null:", "),cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(hide_year)?null:js_date.getFullYear())),(cljs.core.truth_(hide_time)?null:[" at ",time_string].join(''))].join('');
}));

(oc.web.lib.utils.activity_date_string.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.activity_date_string.cljs$lang$applyTo = (function (seq41206){
var G__41207 = cljs.core.first(seq41206);
var seq41206__$1 = cljs.core.next(seq41206);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41207,seq41206__$1);
}));

oc.web.lib.utils.tooltip_date = (function oc$web$lib$utils$tooltip_date(past_date){
var past_js_date = oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([past_date], 0));
var now_date = oc.web.lib.utils.js_date();
var hide_time = ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(past_js_date.getFullYear(),now_date.getFullYear())) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(past_js_date.getMonth(),now_date.getMonth())) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(past_js_date.getDate(),now_date.getDate())));
return oc.web.lib.utils.activity_date_string.cljs$core$IFn$_invoke$arity$variadic(past_js_date,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([hide_time,false], 0));
});
/**
 * Get a string representing the elapsed time from a date in the past
 */
oc.web.lib.utils.activity_date = (function oc$web$lib$utils$activity_date(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41526 = arguments.length;
var i__4737__auto___41527 = (0);
while(true){
if((i__4737__auto___41527 < len__4736__auto___41526)){
args__4742__auto__.push((arguments[i__4737__auto___41527]));

var G__41528 = (i__4737__auto___41527 + (1));
i__4737__auto___41527 = G__41528;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.lib.utils.activity_date.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.lib.utils.activity_date.cljs$core$IFn$_invoke$arity$variadic = (function (past_js_date,p__41214){
var vec__41215 = p__41214;
var hide_time = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41215,(0),null);
if(cljs.core.truth_(past_js_date)){
var past = past_js_date.getTime();
var now = oc.web.lib.utils.js_date().getTime();
var seconds = Math.floor(((now - past) / (1000)));
var years_interval = Math.floor((seconds / (31536000)));
var months_interval = Math.floor((seconds / (2592000)));
var days_interval = Math.floor((seconds / (86400)));
var hours_interval = Math.floor((seconds / (3600)));
var minutes_interval = Math.floor((seconds / (60)));
if((years_interval > (0))){
return ["on ",oc.web.lib.utils.activity_date_string.cljs$core$IFn$_invoke$arity$variadic(past_js_date,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([hide_time], 0))].join('');
} else {
if((((months_interval > (0))) || ((days_interval > (7))))){
return ["on ",oc.web.lib.utils.activity_date_string.cljs$core$IFn$_invoke$arity$variadic(past_js_date,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([hide_time], 0))].join('');
} else {
if((days_interval > (0))){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(days_interval)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.pluralize("day",days_interval))," ago"].join('');
} else {
if((hours_interval > (0))){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(hours_interval)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.pluralize("hour",hours_interval))," ago"].join('');
} else {
if((minutes_interval > (0))){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(minutes_interval)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.pluralize("min",minutes_interval))," ago"].join('');
} else {
return "Just now";

}
}
}
}
}
} else {
return null;
}
}));

(oc.web.lib.utils.activity_date.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.lib.utils.activity_date.cljs$lang$applyTo = (function (seq41212){
var G__41213 = cljs.core.first(seq41212);
var seq41212__$1 = cljs.core.next(seq41212);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41213,seq41212__$1);
}));

oc.web.lib.utils.activity_date_tooltip = (function oc$web$lib$utils$activity_date_tooltip(entry_data){
var created_at = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(entry_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(entry_data);
}
})();
var last_edit = cljs.core.last(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(entry_data));
var updated_at = (cljs.core.truth_(new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336).cljs$core$IFn$_invoke$arity$1(last_edit))?new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336).cljs$core$IFn$_invoke$arity$1(last_edit):null);
var same_author_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(last_edit),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"publisher","publisher",-153364540).cljs$core$IFn$_invoke$arity$1(entry_data)));
var should_show_updated_at_QMARK_ = (((!(same_author_QMARK_))) || (((oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([updated_at], 0)).getTime() - oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([created_at], 0)).getTime()) > ((((1000) * (60)) * (60)) * (24)))));
var created_str = oc.web.lib.utils.tooltip_date(created_at);
var updated_str = oc.web.lib.utils.tooltip_date(updated_at);
var label_prefix = ((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(entry_data),"published")) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(entry_data),new cljs.core.Keyword(null,"entry","entry",505168823)))))?"Posted on ":"Created on ");
if((!(should_show_updated_at_QMARK_))){
return [label_prefix,created_str].join('');
} else {
if(same_author_QMARK_){
return [label_prefix,created_str,"\nEdited ",updated_str].join('');
} else {
return [label_prefix,created_str,"\nEdited ",updated_str," by ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(last_edit))].join('');
}
}
});
oc.web.lib.utils.ios_copy_to_clipboard = (function oc$web$lib$utils$ios_copy_to_clipboard(el){
var old_ce = el.contentEditable;
var old_ro = el.readOnly;
var rg = document.createRange();
(el.contentEditable = true);

(el.readOnly = false);

rg.selectNodeContents(el);

var s = window.getSelection();
s.removeAllRanges();

s.addRange(rg);

el.setSelectionRange((0),el.value.length);

(el.contentEditable = old_ce);

return (el.readOnly = old_ro);
});
oc.web.lib.utils.copy_to_clipboard = (function oc$web$lib$utils$copy_to_clipboard(el){
try{if(cljs.core.truth_(oc.shared.useragent.ios_QMARK_)){
oc.web.lib.utils.ios_copy_to_clipboard(el);
} else {
}

return document.execCommand("copy");
}catch (e41218){var e = e41218;
return false;
}});
oc.web.lib.utils.body_without_preview = (function oc$web$lib$utils$body_without_preview(body){
var body_without_tags = oc.web.lib.utils.strip_empty_tags(oc.web.lib.utils.strip_br_tags(oc.web.lib.utils.strip_img_tags(body)));
var hidden_class = ["activity-body-",cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.rand.cljs$core$IFn$_invoke$arity$1((10000)) | (0)))].join('');
var $body_content = $(["<div class=\"",hidden_class," hidden\">",cljs.core.str.cljs$core$IFn$_invoke$arity$1(body_without_tags),"</div>"].join(''));
var appened_body = $(document.body).append($body_content);
var _ = $([".",hidden_class," .carrot-no-preview"].join('')).each((function (){
var this$ = this;
var $this = $(this$);
return $this.remove();
}));
var $hidden_div = $([".",hidden_class].join(''));
var body_without_preview = $hidden_div.html();
var ___$1 = $hidden_div.remove();
return body_without_preview;
});
oc.web.lib.utils.remove_elements = (function oc$web$lib$utils$remove_elements($container,el_selector,re_check){
var $el = $container.find(el_selector);
while(true){
if(cljs.core.truth_(((($el.length > (0)))?$el.html().match(re_check):false))){
$el.remove();

var G__41539 = $container.find(el_selector);
$el = G__41539;
continue;
} else {
return null;
}
break;
}
});
oc.web.lib.utils.clean_body_html = (function oc$web$lib$utils$clean_body_html(inner_html){
var $container = $("<div class=\"hidden\"/>").html(inner_html);
var _ = $(".rangySelectionBoundary",$container).remove();
var ___$1 = $(".oc-mention-popup",$container).remove();
var ___$2 = $(".oc-poll-container",$container).remove();
var re_check = RegExp("^([\\s]*|[\\<br\\s*/?\\>]{0,1}|[\\s]*|[\\&nbsp;]*)*$","i");
var ___$3 = oc.web.lib.utils.remove_elements($container,"> p:last-child",re_check);
var ___$4 = oc.web.lib.utils.remove_elements($container,"> p:first-child",re_check);
return $container.html();
});
oc.web.lib.utils.url_org_slug = (function oc$web$lib$utils$url_org_slug(storage_url){
var parts = cuerdas.core.split.cljs$core$IFn$_invoke$arity$2(storage_url,"/");
if(cuerdas.core.starts_with_QMARK_(storage_url,"http")){
return cljs.core.nth.cljs$core$IFn$_invoke$arity$2(parts,(4));
} else {
return cljs.core.nth.cljs$core$IFn$_invoke$arity$2(parts,(2));
}
});
oc.web.lib.utils.storage_url_org_slug = (function oc$web$lib$utils$storage_url_org_slug(url){
return oc.web.lib.utils.url_org_slug(url);
});
oc.web.lib.utils.url_board_slug = (function oc$web$lib$utils$url_board_slug(url){
var parts = cuerdas.core.split.cljs$core$IFn$_invoke$arity$2(url,"/");
if(cuerdas.core.starts_with_QMARK_(url,"http")){
return cljs.core.nth.cljs$core$IFn$_invoke$arity$2(parts,(5));
} else {
return cljs.core.nth.cljs$core$IFn$_invoke$arity$2(parts,(3));
}
});
oc.web.lib.utils.section_org_slug = (function oc$web$lib$utils$section_org_slug(section_data){
return oc.web.lib.utils.url_org_slug(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(section_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0)));
});
oc.web.lib.utils.post_org_slug = (function oc$web$lib$utils$post_org_slug(post_data){
return oc.web.lib.utils.url_org_slug(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(post_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0)));
});
oc.web.lib.utils.default_body_placeholder = "What's happening";
oc.web.lib.utils.default_drafts_board_name = oc.web.utils.drafts.default_drafts_board_name;
oc.web.lib.utils.default_drafts_board_slug = oc.web.utils.drafts.default_drafts_board_slug;
oc.web.lib.utils.default_draft_status = oc.web.utils.drafts.default_draft_status;
oc.web.lib.utils.default_drafts_board = oc.web.utils.drafts.default_drafts_board;
oc.web.lib.utils.default_section_slug = "--default-section-slug";
oc.web.lib.utils.default_section_access = "team";
oc.web.lib.utils.default_section = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"name","name",1843675177),"",new cljs.core.Keyword(null,"access","access",2027349272),oc.web.lib.utils.default_section_access,new cljs.core.Keyword(null,"slug","slug",2029314850),oc.web.lib.utils.default_section_slug], null);
oc.web.lib.utils.retina_src = (function oc$web$lib$utils$retina_src(url){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"src","src",-1651076051),oc.web.lib.utils.cdn([cljs.core.str.cljs$core$IFn$_invoke$arity$1(url),".png"].join('')),new cljs.core.Keyword(null,"src-set","src-set",1389408880),[oc.web.lib.utils.cdn([cljs.core.str.cljs$core$IFn$_invoke$arity$1(url),"@2x.png"].join(''))," 2x"].join('')], null);
});
oc.web.lib.utils.trim = (function oc$web$lib$utils$trim(value){
if(typeof value === 'string'){
return cuerdas.core.trim.cljs$core$IFn$_invoke$arity$1(value);
} else {
return value;
}
});
oc.web.lib.utils.section_name_exists_error = "Team name already exists or isn't allowed";
oc.web.lib.utils.calc_video_height = (function oc$web$lib$utils$calc_video_height(width){
return ((width * ((3) / (4))) | (0));
});
oc.web.lib.utils.hide_class = "fs-hide";
oc.web.lib.utils.find_node = (function oc$web$lib$utils$find_node(e,fn){
var el = e.target;
while(true){
if(cljs.core.truth_((function (){var or__4126__auto__ = cljs.core.not(el);
if(or__4126__auto__){
return or__4126__auto__;
} else {
return (fn.cljs$core$IFn$_invoke$arity$1 ? fn.cljs$core$IFn$_invoke$arity$1(el) : fn.call(null,el));
}
})())){
return el;
} else {
var G__41541 = el.parentElement;
el = G__41541;
continue;
}
break;
}
});
oc.web.lib.utils.element_clicked_QMARK_ = (function oc$web$lib$utils$element_clicked_QMARK_(e,element_name){
return oc.web.lib.utils.find_node(e,(function (p1__41219_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(p1__41219_SHARP_.tagName,element_name);
}));
});
oc.web.lib.utils.button_clicked_QMARK_ = (function oc$web$lib$utils$button_clicked_QMARK_(e){
return oc.web.lib.utils.element_clicked_QMARK_(e,"BUTTON");
});
oc.web.lib.utils.input_clicked_QMARK_ = (function oc$web$lib$utils$input_clicked_QMARK_(e){
return oc.web.lib.utils.element_clicked_QMARK_(e,"INPUT");
});
oc.web.lib.utils.anchor_clicked_QMARK_ = (function oc$web$lib$utils$anchor_clicked_QMARK_(e){
return oc.web.lib.utils.element_clicked_QMARK_(e,"A");
});
oc.web.lib.utils.content_editable_clicked_QMARK_ = (function oc$web$lib$utils$content_editable_clicked_QMARK_(e){
return oc.web.lib.utils.find_node(e,(function (p1__41220_SHARP_){
return p1__41220_SHARP_.attributes.contenteditable;
}));
});
/**
 * Debounce function: give a function and a wait time call it immediately
 *   and avoid calling it again for the wait time.
 *   NB: if you call setState in the passed fn you need to check it's still mounted
 *   manually since this can be calling f after it has been unmounted.
 */
oc.web.lib.utils.debounced_fn = (function oc$web$lib$utils$debounced_fn(f,w){
var timeout = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
return (function() { 
var G__41542__delegate = function (args){
var wait_QMARK_ = cljs.core.deref(timeout);
var later = (function (){
cljs.core.reset_BANG_(timeout,null);

if(cljs.core.truth_(wait_QMARK_)){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(f,args);
} else {
return null;
}
});
if(cljs.core.truth_(wait_QMARK_)){
clearTimeout(cljs.core.deref(timeout));
} else {
}

cljs.core.reset_BANG_(timeout,setTimeout(later,w));

if(cljs.core.truth_(wait_QMARK_)){
return null;
} else {
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(f,args);
}
};
var G__41542 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__41546__i = 0, G__41546__a = new Array(arguments.length -  0);
while (G__41546__i < G__41546__a.length) {G__41546__a[G__41546__i] = arguments[G__41546__i + 0]; ++G__41546__i;}
  args = new cljs.core.IndexedSeq(G__41546__a,0,null);
} 
return G__41542__delegate.call(this,args);};
G__41542.cljs$lang$maxFixedArity = 0;
G__41542.cljs$lang$applyTo = (function (arglist__41547){
var args = cljs.core.seq(arglist__41547);
return G__41542__delegate(args);
});
G__41542.cljs$core$IFn$_invoke$arity$variadic = G__41542__delegate;
return G__41542;
})()
;
});
oc.web.lib.utils.throttled_debounced_fn = (function oc$web$lib$utils$throttled_debounced_fn(f,w){
var last_call = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((0));
var timeout = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
return (function() { 
var G__41551__delegate = function (args){
var now = oc.web.lib.utils.js_date().getTime();
var later = (function (){
cljs.core.reset_BANG_(timeout,null);

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(f,args);
});
clearTimeout(cljs.core.deref(timeout));

if(((now - cljs.core.deref(last_call)) < w)){
return setTimeout(later,w);
} else {
cljs.core.reset_BANG_(timeout,null);

cljs.core.reset_BANG_(last_call,now);

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(f,args);
}
};
var G__41551 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__41552__i = 0, G__41552__a = new Array(arguments.length -  0);
while (G__41552__i < G__41552__a.length) {G__41552__a[G__41552__i] = arguments[G__41552__i + 0]; ++G__41552__i;}
  args = new cljs.core.IndexedSeq(G__41552__a,0,null);
} 
return G__41551__delegate.call(this,args);};
G__41551.cljs$lang$maxFixedArity = 0;
G__41551.cljs$lang$applyTo = (function (arglist__41553){
var args = cljs.core.seq(arglist__41553);
return G__41551__delegate(args);
});
G__41551.cljs$core$IFn$_invoke$arity$variadic = G__41551__delegate;
return G__41551;
})()
;
});
oc.web.lib.utils.observe = (function oc$web$lib$utils$observe(){
if(cljs.core.truth_(window.attachEvent)){
return (function (el,e,handler){
return el.attachEvent(["on",cljs.core.str.cljs$core$IFn$_invoke$arity$1(e)].join(''),handler);
});
} else {
return (function (el,e,handler){
return el.addEventListener(e,handler);
});
}
});
oc.web.lib.utils.page_scroll_top = (function oc$web$lib$utils$page_scroll_top(){
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var activity_id = oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(((cljs.core.not(activity_id))?(function (){var and__4115__auto__ = board_slug;
if(cljs.core.truth_(and__4115__auto__)){
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(board_slug),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.default_drafts_board_slug))){
return is_mobile_QMARK_;
} else {
return false;
}
} else {
return and__4115__auto__;
}
})():false))){
return (65);
} else {
return (0);
}
});

//# sourceMappingURL=oc.web.lib.utils.js.map
