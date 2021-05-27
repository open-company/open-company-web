const carrotContact = "hello@carrot.io";

function formatHelpLine(command, description) {
  return console.log("%c" + command + " " + "%c" + description,
                     "font-weight: normal;",
                     "font-style: italic;");
}

function OCWebHelp(){
  console.log("Debug app-state functions:\n")
  formatHelpLine("oc.web.dispatcher.db()", "Print the whole app-state");
  formatHelpLine("oc.web.lib.logging.config_log_level_BANG_(\"debug\")", "Change log level");
  formatHelpLine("oc.web.actions.user.force_jwt_refresh()", "Force a JWT token refresh");
  formatHelpLine("oc.web.dispatcher", "Namespace with more debug functions");
  formatHelpLine("OCWebHelp()", "Print this help");
}

function OCWebPrintAsciiArt(){
  console.group(carrotContact);
  console.log("%c\n" +
" ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄ \n" +
"▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌\n" +
"▐░█▀▀▀▀▀▀▀▀▀ ▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀█░▌ ▀▀▀▀█░█▀▀▀▀ \n" +
"▐░▌          ▐░▌       ▐░▌▐░▌       ▐░▌▐░▌       ▐░▌▐░▌       ▐░▌     ▐░▌     \n" +
"▐░▌          ▐░█▄▄▄▄▄▄▄█░▌▐░█▄▄▄▄▄▄▄█░▌▐░█▄▄▄▄▄▄▄█░▌▐░▌       ▐░▌     ▐░▌     \n" +
"▐░▌          ▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░▌       ▐░▌     ▐░▌     \n" +
"▐░▌          ▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀█░█▀▀ ▐░█▀▀▀▀█░█▀▀ ▐░▌       ▐░▌     ▐░▌     \n" +
"▐░▌          ▐░▌       ▐░▌▐░▌     ▐░▌  ▐░▌     ▐░▌  ▐░▌       ▐░▌     ▐░▌     \n" +
"▐░█▄▄▄▄▄▄▄▄▄ ▐░▌       ▐░▌▐░▌      ▐░▌ ▐░▌      ▐░▌ ▐░█▄▄▄▄▄▄▄█░▌     ▐░▌     \n" +
"▐░░░░░░░░░░░▌▐░▌       ▐░▌▐░▌       ▐░▌▐░▌       ▐░▌▐░░░░░░░░░░░▌     ▐░▌     \n" +
" ▀▀▀▀▀▀▀▀▀▀▀  ▀         ▀  ▀         ▀  ▀         ▀  ▀▀▀▀▀▀▀▀▀▀▀       ▀      "
                                                                              
, "font-family:monospace; color:#FA6452;");
  console.log(`Say hi! ${carrotContact}`);
  OCWebHelp();
  console.groupEnd(carrotContact);
}

(function() {
OCWebPrintAsciiArt();
})();