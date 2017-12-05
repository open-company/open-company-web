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
    $("#site-header-signup-item").hide();
    // Move the red guy up
    $("div.home-page div.balloon.small-red-face").addClass("no-get-started-button");
    // Remove the get started centered button if the user is signed out
    $("#get-started-centred-bt").css({"display": "none"});
    // Hide the try it box at the bottom of the homepage
    $("section.fourth-section").css({"display": "none"});
    // Remove login button from the site mobile menu
    $("button#site-mobile-menu-login").css({"display": "none"});
    // Change Get started button to Your boards on site mobile menu
    var siteMobileMenuGetStarted = $("button#site-mobile-menu-getstarted");
    siteMobileMenuGetStarted.text( "Your Boards" );
    siteMobileMenuGetStarted.addClass("your-boards");
    // Top right corner became Your Boards
    var loginButton = $("#site-header-login-item");
    loginButton.text( "Your Boards" );
    loginButton.addClass("your-boards");
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
    loginButton.attr("href", your_board_url);
    $("div.footer-small-links.static").html("<a href=\"" + your_board_url + "\">Your Boards</a>")
    // Set the action of the site mobile menu's Get started button
    siteMobileMenuGetStarted.attr("onClick", "window.location = \"" + your_board_url + "\"");
    // If in 404 page show error message for logged in users
    $("div.error-page.not-found-page p.not-logged-in").hide();

  }else{ // No logged in user
    // link all get started button to signup with Slack
    $(".get-started-button").attr("onClick", "window.location = \"/login?slack\"");
    // Top right corner signup button
    $("#site-header-signup-item").attr("href", "/login?slack");
    // Top right corner login button
    $("#site-header-login-item").attr("href", "/login");
    // If in 404 page show error message for not logged in users
    $("div.error-page.not-found-page p.logged-in").show();
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

function isFireFox(){
  var ua = navigator.userAgent.toLowerCase();
  if (ua.match(/Firefox/) && !ua.match(/Seamonkey/)) {
      return true;
  } else {
      return false;
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

function siteMobileMenuToggle(){
  var menuClass = "mobile-menu-expanded";
  var body = document.body;
  if (body.classList.contains(menuClass)) {
    body.querySelector("div.site-mobile-menu").classList.add("hidden");
    body.classList.remove(menuClass);
  } else {
    body.querySelector("div.site-mobile-menu").classList.remove("hidden");
    body.classList.add(menuClass);
  }
}