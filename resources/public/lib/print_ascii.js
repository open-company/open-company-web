function OCWebHelp(){
  console.log(`
OpenCompany Web help:
OCWebPrintAppState(): print the whole app-state,
OCWebPrintOrgData(): to print the current org data,
OCWebPrintTeamData(): to print the current team data,
OCWebPrintTeamRoster(): to print the current team roster of users,
OCWebPrintBoardData(): to print the current board data,
OCWebPrintActivitiesData(): to print all the activities loaded for the current board,
OCWebPrintActivityData(): to print the current activity data,
OCWebPrintSecureStoryData(): to print the current story in a secure link,
OCWebPrintReactionsData(): to print the current activity reactions data,
OCWebPrintCommentsData(): to print the current activity comments data,
OCWebPrintActivityCommentsData(): to print the current activity comments data,
OCWebPrintJWTContents(): to print the content of the JWT.
OCWebHelp(): print this help,
OCWebPrintAsciiArt(): print beautiful ASCII art,
OCWebConfigLogLevel("debug"): to change log level.
OCWebPrintRouterPath(): to print the current router setup,
OCWebForceRefreshToken(): force a JWT token refresh with our auth server.
`)}

function printArt(){
  console.group();
  console.log(`%c
 ██████╗ ██████╗ ███████╗███╗   ██╗ ██████╗ ██████╗ ███╗   ███╗██████╗  █████╗ ███╗   ██╗██╗   ██╗
██╔═══██╗██╔══██╗██╔════╝████╗  ██║██╔════╝██╔═══██╗████╗ ████║██╔══██╗██╔══██╗████╗  ██║╚██╗ ██╔╝
██║   ██║██████╔╝█████╗  ██╔██╗ ██║██║     ██║   ██║██╔████╔██║██████╔╝███████║██╔██╗ ██║ ╚████╔╝
██║   ██║██╔═══╝ ██╔══╝  ██║╚██╗██║██║     ██║   ██║██║╚██╔╝██║██╔═══╝ ██╔══██║██║╚██╗██║  ╚██╔╝
╚██████╔╝██║     ███████╗██║ ╚████║╚██████╗╚██████╔╝██║ ╚═╝ ██║██║     ██║  ██║██║ ╚████║   ██║
 ╚═════╝ ╚═╝     ╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝
`, "font-family:monospace; color:#f9d748;");
  console.log("Say hi! hello@carrot.io");
  OCWebHelp();
  console.groupEnd();
}

window.OCWebPrintAsciiArt = printArt;
printArt();