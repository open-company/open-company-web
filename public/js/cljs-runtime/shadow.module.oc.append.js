
shadow.cljs.devtools.client.env.module_loaded('oc');

try { oc.web.core.init(); } catch (e) { console.error("An error occurred when calling (oc.web.core/init)"); throw(e); }