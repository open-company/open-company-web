function mailchipApiSubmit(e, form, success, fail){
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

$(document).ready(function(){
  var tif = getParameterByName("tif");
  var rewriteUrl = window.location.pathname + window.location.hash;
  window.history.pushState({}, document.title, rewriteUrl);
  if (tif) {
    $(".try-it-form-central-input").focus();
  }

  $(".mailchimp-api-subscribe-form").submit( function(e){
    mailchipApiSubmit(e, this, function(){$(".carrot-box-thanks-top").show(); $(".try-it-combo-field").hide();}, function(){});
    return false;
  });
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