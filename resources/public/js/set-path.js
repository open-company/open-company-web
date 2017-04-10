function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)", "i"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

if (getParameterByName("path")) {
  history.pushState(null, "Page could not be found", getParameterByName("path"));
}

document.addEventListener("DOMContentLoaded", function(e){
  if (document.cookie.indexOf("jwt=") >= 0) {
    document.querySelector("#oc-signin-logout-btn").innerText = "LOGOUT";
    document.querySelector("#oc-signin-logout-btn").href = "/logout";
    document.querySelector("#oc-404-disclaimer").innerText = "You are accessing a page that doesn’t exist or requires different authentication.";
  } else {
    document.querySelector("#oc-signin-logout-btn").innerText = "SIGN IN / SIGN UP";
    document.querySelector("#oc-signin-logout-btn").href = "/login";
    document.querySelector("#oc-404-disclaimer").innerText = "You are accessing a page that doesn’t exist or requires authentication.";
  }
});