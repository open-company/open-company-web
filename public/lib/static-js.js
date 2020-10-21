
/***********************************************************************************************
 * When updating this library please update also the jwt-decode npm package.
 ***********************************************************************************************/
!function a(b,c,d){function e(g,h){if(!c[g]){if(!b[g]){var i="function"==typeof require&&require;if(!h&&i)return i(g,!0);if(f)return f(g,!0);var j=new Error("Cannot find module '"+g+"'");throw j.code="MODULE_NOT_FOUND",j}var k=c[g]={exports:{}};b[g][0].call(k.exports,function(a){var c=b[g][1][a];return e(c?c:a)},k,k.exports,a,b,c,d)}return c[g].exports}for(var f="function"==typeof require&&require,g=0;g<d.length;g++)e(d[g]);return e}({1:[function(a,b,c){function d(a){this.message=a}function e(a){var b=String(a).replace(/=+$/,"");if(b.length%4==1)throw new d("'atob' failed: The string to be decoded is not correctly encoded.");for(var c,e,g=0,h=0,i="";e=b.charAt(h++);~e&&(c=g%4?64*c+e:e,g++%4)?i+=String.fromCharCode(255&c>>(-2*g&6)):0)e=f.indexOf(e);return i}var f="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";d.prototype=new Error,d.prototype.name="InvalidCharacterError",b.exports="undefined"!=typeof window&&window.atob&&window.atob.bind(window)||e},{}],2:[function(a,b,c){function d(a){return decodeURIComponent(e(a).replace(/(.)/g,function(a,b){var c=b.charCodeAt(0).toString(16).toUpperCase();return c.length<2&&(c="0"+c),"%"+c}))}var e=a("./atob");b.exports=function(a){var b=a.replace(/-/g,"+").replace(/_/g,"/");switch(b.length%4){case 0:break;case 2:b+="==";break;case 3:b+="=";break;default:throw"Illegal base64url string!"}try{return d(b)}catch(c){return e(b)}}},{"./atob":1}],3:[function(a,b,c){"use strict";function d(a){this.message=a}var e=a("./base64_url_decode");d.prototype=new Error,d.prototype.name="InvalidTokenError",b.exports=function(a,b){if("string"!=typeof a)throw new d("Invalid token specified");b=b||{};var c=b.header===!0?0:1;try{return JSON.parse(e(a.split(".")[c]))}catch(f){throw new d("Invalid token specified: "+f.message)}},b.exports.InvalidTokenError=d},{"./base64_url_decode":2}],4:[function(a,b,c){(function(b){var c=a("./lib/index");"function"==typeof b.window.define&&b.window.define.amd?b.window.define("jwt_decode",function(){return c}):b.window&&(b.window.jwt_decode=c)}).call(this,"undefined"!=typeof global?global:"undefined"!=typeof self?self:"undefined"!=typeof window?window:{})},{"./lib/index":3}]},{},[4]);

function OCStaticGetCookie(name) {
  // Give a cookie name return its value
  var value = "; " + document.cookie;
  var parts = value.split("; " + name + "=");
  if (parts.length == 2)
    return decodeURI(parts.pop().split(";").shift());
}

function OCStaticCookieName(name){
  var domainParts = window.location.hostname.split("."),
      h = domainParts[0],
      prefix = "";
  switch(h) {
    case "192":
    case "localhost":
      prefix = "localhost-";
      break;
    case "staging":
      if (domainParts[1] === "wuts") {
        prefix = "staging-wut-";
      }else{
        prefix = "staging-";
      }
      break;
  }
  return prefix + name;
}

function OCStaticDeleteCookie(name) {
  document.cookie = OCStaticCookieName(name) + "=;expires=Thu Jan 01 1970 01:00:00 UTC;path=/;";
}

function OCStaticGetDecodedJWT(jwt) {
  if (jwt && typeof jwt_decode === "function") {
    try {
      return jwt_decode(jwt);
    } catch(error) {
      return null;
    }
  }
  return null;
}

function OCStaticGetYourBoardsUrl (jwt_data) {
  var url = "/login";
  if ( jwt_data ) {
    var user_id,
        org_slug,
        board_slug;
    if (jwt_data) {
      user_id = jwt_data["user-id"];
      if ( user_id ) {
        org_slug = OCStaticGetCookie(OCStaticCookieName("last-org-" + user_id));
        if ( org_slug ) {
          board_slug = "home";
          // Replace all-posts above withe the following to go back to the last visited board
          // OCStaticGetCookie(OCStaticCookieName("last-board-" + user_id + "-" + org_slug));
          if ( board_slug ){
            url = "/" + org_slug + "/" + board_slug;
          } else {
            url = "/" + org_slug;
          }
        }
      }
    }
  }
  return url;
}

// Get the jwt cookie to know if the user is logged in
var jwt = OCStaticGetCookie(OCStaticCookieName("jwt"));
if (jwt) {
  var decoded_jwt = OCStaticGetDecodedJWT(jwt),
      your_board_url = OCStaticGetYourBoardsUrl(decoded_jwt);
  if (window.location.pathname === "/" && !(OCStaticGetParameterByName("no_redirect"))) {
    window.location = your_board_url;
  }
}

function OCWebSetupStaticPagesJS(){
  var switchFn = function() {
    $("button.keep-aligned-section-next-bt").toggleClass("active");
    var $switchContainer = $("div.slack-email-switch-container");
    $switchContainer.toggleClass("show-slack");
    $switchContainer.toggleClass("show-email");
    $("img.keep-aligned-section-screenshot.screenshot-1").toggleClass("carion-1");
    $("img.keep-aligned-section-screenshot.screenshot-1").toggleClass("carion-1-alt");
  };
  $("button.slack-email-switch-bt").on("click", function(){
    var $switchContainer = $("div.slack-email-switch-container");
    if ($(this).hasClass("email-bt") && $switchContainer.hasClass("show-email")) {
      return;
    }
    if ($(this).hasClass("slack-bt") && $switchContainer.hasClass("show-slack")) {
      return;
    }
    switchFn();
  })
  $("button.keep-aligned-section-next-bt").on("click", switchFn);
}

document.addEventListener("DOMContentLoaded", function(_) {

  if(!document.body.classList.contains("covid-banner") && OCStaticGetParameterByName("ref") === "producthunt"){
    document.body.classList.add("ph-banner")
  }

  // Initialize tooltips
  $('[data-toggle="tooltip"]').tooltip();
  // Sticky header for marketing site
  if ( $("nav.site-navbar").length > 0) {
    $(window).on("scroll", function(){
      if ($(window).scrollTop() < 54) {
        $("nav.site-navbar").removeClass("sticky");
      }else{
        $("nav.site-navbar").addClass("sticky");
      }
    });
  }

  OCWebSetupStaticPagesJS();

  $(window).on("click", function(e){
    $target = $(e.target);
    if (!$target.hasClass("tear-price-select") && !$target.parents(".tear-price-select").length) {
      $("div.tear-price-select-container").removeClass("open");
    }
  });

  if (jwt) {
    $("div.site-navbar-container, div.site-mobile-menu-footer").removeClass("anonymous").addClass("your-digest");
    $(".your-digest-after").attr("href", your_board_url);
    $(".login-signup-or").hide();
    // Remove the get started centered button if the user is signed in
    $("#get-started-centred-bt").hide();

    // Hide get started and login buttons in the footer
    $("div.footer-small-links.static").hide();
    // If in 404 page show error message for logged in users
    $("div.error-page.not-found-page p.not-logged-in").hide();

  }else{ // No logged in user
    $("div.site-navbar-container, div.site-mobile-menu-footer").removeClass("your-digest").addClass("anonymous");
    // If in 404 page show error message for not logged in users
    $("div.error-page.not-found-page p.logged-in").show();
  }

});

function OCStaticGetParameterByName(name, url) {
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

function OCStaticSiteMobileMenuToggle(){
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


function OCStaticStartFixFixedPositioning(sel) {
  // Let's assume the fixed top navbar has id="navbar"
  // Cache the fixed element
  var $navbar = $(sel);

  var fixFixedPosition = function() {
    $navbar.css({
      position: 'absolute',
      top: document.body.scrollTop + 'px'
    });
  };
  var resetFixedPosition = function() {
    $navbar.css({
      position: 'fixed',
      top: ''
    });
    $(document).off('scroll', updateScrollTop);
  };
  var updateScrollTop = function() {
    $navbar.css('top', document.body.scrollTop + 'px');
  };

  $('input, textarea, [contenteditable=true]').on({
    focus: function() {
      // NOTE: The delay is required.
      setTimeout(fixFixedPosition, 100);
      // Keep the fixed element absolutely positioned at the top
      // when the keyboard is visible
      $(document).scroll(updateScrollTop);
    },
    blur: resetFixedPosition
  });
}


function OCStaticShowPHBanner(){
  OCStaticHideCovidBanner();
  document.body.classList.add("ph-banner")
}

function OCStaticHidePHBanner(){
  document.body.classList.remove("ph-banner")
}

function OCStaticShowCovidBanner(){
  OCStaticHidePHBanner();
  document.body.classList.add("covid-banner")
}

function OCStaticHideCovidBanner(){
  document.body.classList.remove("covid-banner")
}

function OCStaticTextareaSaveSelection() {
    if (window.getSelection) {
        var sel = window.getSelection();
        if (sel.getRangeAt && sel.rangeCount) {
            return sel.getRangeAt(0);
        }
    } else if (document.selection && document.selection.createRange) {
        return document.selection.createRange();
    }
    return null;
}

function OCStaticTextareaRestoreSelection(range) {
    if (range) {
        if (window.getSelection) {
            var sel = window.getSelection();
            sel.removeAllRanges();
            sel.addRange(range);
        } else if (document.selection && range.select) {
            range.select();
        }
    }
}

function isiPhoneWithoutPhysicalHomeBt(){
  // Really basic check for the ios platform
  // https://stackoverflow.com/questions/9038625/detect-if-device-is-ios
  var iOS = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;

  // Get the device pixel ratio
  var ratio = window.devicePixelRatio || 1;

  // Define the users device screen dimensions
  var screen = {
    width : window.screen.width * ratio,
    height : window.screen.height * ratio
  };

  // iPhone X and Xs Detection
  if (iOS && screen.width === 1125 && screen.height === 2436) {
    return true;
  }
  // iPhone Xr Detection
  if (iOS && screen.width === 828 && screen.height === 1792) {
    return true;
  }

  // iPhone Xs Max Detection
  if (iOS && screen.width === 1242 && screen.height === 2688) {
    return true;
  }
  return false;
}

var OCYTVideoAutoplay = false;

(function(){
  $(document).ready(function(){
    
    var $appsBt = $("button.apps-bt");
    if ($appsBt.length > 0) {
      $appsBt.click(function(event){
        event.stopPropagation();
        $("div.apps-container").toggleClass("dropdown-menu-visible");
      });
      $(window).click(function(event){
        $("div.apps-container").removeClass("dropdown-menu-visible");
      });
    }

    if (window.location.pathname == "/" && window.location.hash == "#oc-video") {
      OCYTVideoAutoplay = true;
      OCStaticShowYTVideo()
    }
  });
})();

var OCYTVideoPlayer = null,
OCYTVideoFinished = false;

function onYouTubeIframeAPIReady() {
  var winWidth = document.documentElement.clientWidth || window.innerWidth,
  winHeight = document.documentElement.clientHeight || window.innerHeight;
  OCYTVideoPlayer = new YT.Player('youtube-player', {
    height: Math.min(winHeight, 608).toString(),
    width: Math.min(winWidth, 1080).toString(),
    videoId: 'dMWpnHxQMP4',
    allowsInlineMediaPlayback: 'TRUE',
    allowfullscreen: 'true',
    playerVars: {
        showinfo: 0,
        rel: 0,
        playsinline: 1,
        autoplay: (OCYTVideoAutoplay? 1 : 0)
    },
    events: {
      'onReady': OCYTVideoOnPlayerReady,
      'onStateChange': OCYTVideoOnPlayerStateChange
    }
  });
  if (OCYTVideoAutoplay) {
    setTimeout(OCYTVideoPlay, 500);
  }
}

function OCYTVideoOnPlayerReady(event) {
}

function OCYTVideoPlay(restart) {
  if (OCYTVideoPlayer && OCYTVideoPlayer.playVideo) {
    if (OCYTVideoFinished || restart) {
      OCYTVideoPlayer.seekTo(0);
      OCYTVideoFinished = false;
    }
    OCYTVideoPlayer.playVideo();
  }
}

function OCYTVideoOnPlayerStateChange(event){
  if (event.data === 0) {
    OCYTVideoFinished = true;
    OCYTVideoPause();
  }
}

function OCYTVideoPause(e) {
  if (OCYTVideoPlayer) {
    OCYTVideoPlayer.pauseVideo();
    console.log("pause!");
    $(document.body).removeClass('show-animation-lightbox no-scroll');
    if (e) {
      e.preventDefault();
      e.stopPropagation();
    }
  }
}

function OCStaticShowYTVideo() {
  $("div.close-communigation-gaps-video").addClass("video-playing");
  OCYTVideoPlay();
}