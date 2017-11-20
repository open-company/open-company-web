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

function cookieName(name){
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
  return prefix + name;
}

$(document).ready(function(){
  // Get the jwt cookie to know if the user is logged in
  var jwt = getCookie(cookieName("jwt"));
  if (jwt) {
    $("#site-header-login-item").hide();
    // Hide the try it box at the bottom of the homepage
    $("div.try-it").css({"display": "none"});
    // Remove the label below it too
    $("#easy-setup-label").css({"display": "none"});
    // Top right corner became Your Boards
    $("#site-header-signup-item").text( "Your Boards" );
    var your_board_url = "/login",
        decoded_jwt;
    if ( typeof jwt_decode === "function" ) {
      var decoded_jwt = jwt_decode(jwt),
          user_id,
          org_slug,
          board_slug;
      if (jwt_decode &&  decoded_jwt) {
        user_id = decoded_jwt["user-id"];
        if ( user_id ) {
          org_slug = getCookie(cookieName("last-org-" + user_id))
          if ( org_slug ) {
            board_slug = getCookie(cookieName("last-board-" + user_id + "-" + org_slug))
            if ( board_slug ){
              if ( getCookie(cookieName("last-filter-" + user_id + "-" + board_slug + "-" + org_slug)) === "by-topic" ) {
                your_board_url = "/" + org_slug + "/" + board_slug + "/by-topic";
              } else {
                your_board_url = "/" + org_slug + "/" + board_slug;
              }
            } else {
              your_board_url = "/" + org_slug;
            }
          }
        }
      }
    }
    $("#site-header-signup-item").attr("onClick", "window.location = \"" + your_board_url + "\";");
    // If in 404 page show error message for logged in users
    $("div.error-page.not-found-page p.not-logged-in").hide();

  }else{ // No logged in user
    // link all get started button to signup with Slack
    $(".get-started-button").attr("onClick", "window.location = \"/login?slack\"");
    // Top right corner button
    $("#site-header-signup-item").text("Get Started");
    $("#site-header-signup-item").attr("onClick", "window.location = \"/login?slack\"");
    // If in 404 page show error message for not logged in users
    $("div.error-page.not-found-page p.logged-in").hide();
  }

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

function isEdge(){
  if (navigator.appName == 'Microsoft Internet Explorer' ||
      !!(navigator.userAgent.match(/Trident/) || navigator.userAgent.match(/rv:11/) || navigator.userAgent.match(/Edge\/\d+/)))
  {
    return true;
  }else{
    return false;
  }
}

function isIE(){
  if (navigator.appName == 'Microsoft Internet Explorer' ||
      !!(navigator.userAgent.match(/Trident/) ||
         navigator.userAgent.match(/rv:11/)))
  {
    return true;
  }else{
    return false;
  }
}