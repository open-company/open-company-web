function OCWebHelp(){
  console.log(`
OpenCompany Web help:
OCWebHelp(): print this help,
OCWebPrintAppState(): print the whole app-state,
OCWebPrintOrgData(): to print the current org data,
OCWebPrintUpdatesListData(): to print the current loaded list of updates,
OCWebPrintUpdateData(): to print the current update data,
OCWebPrintBoardData(): to print the current board data,
OCWebPrintEntriesData(): to print all the entries loaded,
OCWebPrintJWTContents(): to print the content of the JWT.
OCWebConfigLogLevel("debug"): to change log level.`)
}

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
  console.log("We are hiring: hello@opencompany.com");
  OCWebHelp();
  console.groupEnd();
}

window.OCWebPrintAsciiArt = printArt;
printArt();