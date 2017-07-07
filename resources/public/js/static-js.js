function mailchipApiSubmit(e, emailValue, success, fail){
  e.preventDefault();
  $.POST(form.action,)
  $.ajax({
    type: "POST",
    data: {email: emailValue},
    success: function(a, b){
      console.log("SUCCESS", a, b);
      if (typeof success === "function") {
        success();
      }
    },
    error: function(a, b){
      console.log("ERROR", a, b);
      if (typeof fail === "function") {
        fail();
      }
    }
  });
}

$(document).ready(function(){
  var topThankyou = getParameterByName("tyt");
  var bottomThankyou = getParameterByName("tyb");
  window.history.pushState({}, document.title, rewriteUrl);
  if (topThankyou) {
    $(".carrot-box-thanks-top").show();
  }
  if (bottomThankyou) {
    $(".carrot-box-thanks-bottom").show();
  }
  var rewriteUrl = window.location.pathname + window.location.hash;

  var form = $("#mailchimp-api-subscribe-form");
  form.onSubmit = function(e){
    
  };
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