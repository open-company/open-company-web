function OCWebHelp(){
  console.log("OpenCompany Web console functions:\n\n" +
              "OCWebConfigLogLevel(\"debug\"): to change log level\n" +
              "OCWebForceRefreshToken(): force a JWT token refresh with our auth server\n" +
              "OCWebPrintActivityCommentsData(): to print the current activity comments data\n" +
              "OCWebPrintActivityData(): to print the current activity data\n" +
              "OCWebPrintActivityReadData()\n" +
              "OCWebPrintAppState(): print the whole app-state\n" +
              "OCWebPrintAsciiArt(): print beautiful ASCII art\n" +
              "OCWebPrintBoardData(): to print the current section's data\n" +
              "OCWebPrintChangeData(): to print the current change data\n" +
              "OCWebPrintCommentsData(): to print the current activity comments data\n" +
              "OCWebPrintContainerData()\n" +
              "OCWebPrintEntryEditingData()\n" +
              "OCWebPrintFilteredPostsData()\n" +
              "OCWebPrintJWTContents(): to print the content of the JWT\n" +
              "OCWebPrintOrgData(): to print the current org data\n" +
              "OCWebPrintPanelStack(): to print the right side panel state\n" +
              "OCWebPrintPostsData()\n"+
              "OCWebPrintReactionsData(): to print the current activity reactiosn data\n" +
              "OCWebPrintRemindersData()\n" +
              "OCWebPrintRemindersEditData()\n" +
              "OCWebPrintRouterPath(): to print the current router setup\n" +
              "OCWebPrintSecureActivityData()\n" +
              "OCWebPrintTeamData(): to print the current team data\n" +
              "OCWebPrintTeamRoster(): to print the current team roster of users\n" +
              "OCWebPrintUserNotifications(): to print the notifications\n" +
              "OCWebPrintPaymentsData(): to print the payments data\n" +
              "OCWebHelp(): print this help\n")
}

function printArt(){
  console.group();
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
  console.log("Say hi! hello@carrot.io");
  OCWebHelp();
  console.groupEnd();
}

window.OCWebPrintAsciiArt = printArt;
printArt();