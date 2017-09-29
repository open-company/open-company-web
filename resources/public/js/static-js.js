function mailchimpApiSubmit(e, form, success, fail){
  e.preventDefault();
  $.ajax({
    type: "POST",
    url: form.action,
    contentType: "text/plain",
    data: $("input." + form.id + "-input").val(),
    success: function(a, b){
      if (typeof success === "function") {
        success();
      }
    },
    error: function(a, b){
      if (typeof fail === "function") {
        fail();
      }
    }
  });
}

function getCookie(name) {
    var dc = document.cookie;
    var prefix = name + "=";
    var begin = dc.indexOf("; " + prefix);
    if (begin == -1) {
        begin = dc.indexOf(prefix);
        if (begin != 0) return null;
    }
    else
    {
        begin += 2;
        var end = document.cookie.indexOf(";", begin);
        if (end == -1) {
        end = dc.length;
        }
    }
    // because unescape has been deprecated, replaced with decodeURI
    //return unescape(dc.substring(begin + prefix.length, end));
    return decodeURI(dc.substring(begin + prefix.length, end));
}

function cookieName(){
  var h = window.location.hostname.split(".")[0];
  var prefix = "";
  switch(h) {
    case "localhost":
      prefix = "localhost-";
      break;
    case "staging":
      prefix = "staging-";
      break;
  }
  return prefix + "jwt";
}

$(document).ready(function(){
  // var tif = getParameterByName("tif");
  // var confirm = getParameterByName("confirm");
  // var rewriteUrl = window.location.pathname + window.location.hash;
  // window.history.pushState({}, document.title, rewriteUrl);
  // if (tif) {
  //   $(".try-it-form-central-input").focus();
  // }

  // $("#try-it-form-central").submit( function(e){
  //   mailchimpApiSubmit(e, this, function(){
  //     $(".carrot-box-thanks-top").show();
  //     $(".try-it-combo-field-top").hide();
  //   }, function(){});
  //   return false;
  // });

  // $("#try-it-form-bottom").submit( function(e){
  //   mailchimpApiSubmit(e, this, function(){
  //     $(".carrot-box-thanks-bottom").show();
  //     $(".try-it-combo-field-bottom").hide();
  //   }, function(){});
  //   return false;
  // });

  // if (confirm) {
  //   $(".confirm-thanks").show();
  //   $(".carrot-box-thanks-top").hide();
  //   $("#try-it-form-central").hide();
  // }

  var jwt = getCookie(cookieName());
  if (jwt) {
    $("#site-header-login-item").hide();
  }
  $("#site-header-signup-item").text( jwt? "Your Newsboard" : "Sign Up" );
  $("#site-header-signup-item").attr("onClick", jwt? "window.location = \"/login\"" : "window.location = \"/sign-up\"");
});

function getParameterByName(name, url) {
  if (!url)
    url = window.location.href;
  name = name.replace(/[\[\]]/g, "\\$&");
  var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
      results = regex.exec(url);
  if (!results)
    return null;
  if (!results[2])
    return '';
  return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function isSafari(){
  var ua = navigator.userAgent.toLowerCase(); 
  if (ua.indexOf('safari') != -1) { 
    if (ua.indexOf('chrome') > -1) {
      return false;
    } else {
      return true;
    }
  }
}

function isIE(){
  if (navigator.appName == 'Microsoft Internet Explorer' ||
      !!(navigator.userAgent.match(/Trident/) || navigator.userAgent.match(/rv:11/) || navigator.userAgent.match(/Edge\/\d+/)) ||
      (typeof $.browser !== "undefined" && $.browser.msie == 1))
  {
    return true;
  }else{
    return false;
  }
}