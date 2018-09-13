function OCWebHelp(){
  console.log("OpenCompany Web help:\n" +
              "OCWebPrintAppState(): print the whole app-state,\n" +
              "OCWebPrintOrgData(): to print the current org data,\n" +
              "OCWebPrintTeamData(): to print the current team data,\n" +
              "OCWebPrintTeamRoster(): to print the current team roster of users,\n" +
              "OCWebPrintChangeData(): to print the current change data,\n" +
              "OCWebPrintBoardData(): to print the current board data,\n" +
              "OCWebPrintActivityData(): to print the current activity data,\n" +
              "OCWebPrintSecureStoryData(): to print the current story in a secure link,\n" +
              "OCWebPrintReactionsData(): to print the current activity reactions data,\n" +
              "OCWebPrintCommentsData(): to print the current activity comments data,\n" +
              "OCWebPrintActivityCommentsData(): to print the current activity comments data,\n" +
              "OCWebPrintJWTContents(): to print the content of the JWT.\n" +
              "OCWebHelp(): print this help,\n" +
              "OCWebPrintAsciiArt(): print beautiful ASCII art,\n" +
              "OCWebConfigLogLevel(\"debug\"): to change log level.\n" +
              "OCWebPrintRouterPath(): to print the current router setup,\n" +
              "OCWebForceRefreshToken(): force a JWT token refresh with our auth server.")
}

function printArt(){
  console.group();
  console.log("%c\n" +
" ██████╗ ██████╗ ███████╗███╗   ██╗ ██████╗ ██████╗ ███╗   ███╗██████╗  █████╗ ███╗   ██╗██╗   ██╗\n" +
"██╔═══██╗██╔══██╗██╔════╝████╗  ██║██╔════╝██╔═══██╗████╗ ████║██╔══██╗██╔══██╗████╗  ██║╚██╗ ██╔╝\n" +
"██║   ██║██████╔╝█████╗  ██╔██╗ ██║██║     ██║   ██║██╔████╔██║██████╔╝███████║██╔██╗ ██║ ╚████╔╝ \n" +
"██║   ██║██╔═══╝ ██╔══╝  ██║╚██╗██║██║     ██║   ██║██║╚██╔╝██║██╔═══╝ ██╔══██║██║╚██╗██║  ╚██╔╝  \n" +
"╚██████╔╝██║     ███████╗██║ ╚████║╚██████╗╚██████╔╝██║ ╚═╝ ██║██║     ██║  ██║██║ ╚████║   ██║   \n" +
" ╚═════╝ ╚═╝     ╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝   "
, "font-family:monospace; color:#FA6452;");
  console.log("Say hi! hello@carrot.io");
  OCWebHelp();
  console.groupEnd();
}

window.OCWebPrintAsciiArt = printArt;
printArt();