(ns open-company-web.lib.iso4217)

; The currencies db comes from https://github.com/zwacky/isoCurrency

(def iso4217 {
	:AFN {
	  :text "Afghani",
	  :fraction 2,
	  :symbol "؋",
	  :code "AF"
	}
	:EUR {
	  :text "Euro",
	  :fraction 2,
	  :symbol "€",
	  :code "EUR"
	}
	:ALL {
	  :text "Lek",
	  :fraction 2,
	  :symbol "Lek",
	  :code "ALL"
	}
	:DZD {
	  :text "Algerian Dinar",
	  :fraction 2,
	  :symbol false,
	  :code "DZD"
	}
	:USD {
	  :text "US Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "USD"
	}
	:AOA {
	  :text "Kwanza",
	  :fraction 2,
	  :symbol false,
	  :code "AOA"
	}
	:XCD {
	  :text "East Caribbean Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "XCD"
	}
	:ARS {
	  :text "Argentine Peso",
	  :fraction 2,
	  :symbol "$",
	  :code "ARS"
	}
	:AMD {
	  :text "Armenian Dram",
	  :fraction 2,
	  :symbol false,
	  :code "AMD"
	}
	:AWG {
	  :text "Aruban Florin",
	  :fraction 2,
	  :symbol "ƒ",
	  :code "AWG"
	}
	:AUD {
	  :text "Australian Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "AUD"
	}
	:AZN {
	  :text "Azerbaijanian Manat",
	  :fraction 2,
	  :symbol "ман",
	  :code "AZ"
	}
	:BSD {
	  :text "Bahamian Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "BSD"
	}
	:BHD {
	  :text "Bahraini Dinar",
	  :fraction 3,
	  :symbol false,
	  :code "BHD"
	}
	:BDT {
	  :text "Taka",
	  :fraction 2,
	  :symbol false,
	  :code "BDT"
	}
	:BBD {
	  :text "Barbados Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "BBD"
	}
	:BYR {
	  :text "Belarussian Ruble",
	  :fraction 0,
	  :symbol "p.",
	  :code "BYR"
	}
	:BZD {
	  :text "Belize Dollar",
	  :fraction 2,
	  :symbol "BZ$",
	  :code "BZD"
	}
	:XOF {
	  :text "CF Franc BCEAO",
	  :fraction 0,
	  :symbol false,
	  :code "XOF"
	}
	:BMD {
	  :text "Bermudian Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "BMD"
	}
	:BTN {
	  :text "Ngultrum",
	  :fraction 2,
	  :symbol false,
	  :code "BT"
	}
	:INR {
	  :text "Indian Rupee",
	  :fraction 2,
	  :symbol "",
	  :code "INR"
	}
	:BOB {
	  :text "Boliviano",
	  :fraction 2,
	  :symbol "$b",
	  :code "BOB"
	}
	:BOV {
	  :text "Mvdol",
	  :fraction 2,
	  :symbol false,
	  :code "BOV"
	}
	:BAM {
	  :text "Convertible Mark",
	  :fraction 2,
	  :symbol "KM",
	  :code "BAM"
	}
	:BWP {
	  :text "Pula",
	  :fraction 2,
	  :symbol "P",
	  :code "BWP"
	}
	:NOK {
	  :text "Norwegian Krone",
	  :fraction 2,
	  :symbol "kr",
	  :code "NOK"
	}
	:BRL {
	  :text "Brazilian Real",
	  :fraction 2,
	  :symbol "R$",
	  :code "BRL"
	}
	:BND {
	  :text "Brunei Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "BND"
	}
	:BGN {
	  :text "Bulgarian Lev",
	  :fraction 2,
	  :symbol "лв",
	  :code "BG"
	}
	:BIF {
	  :text "Burundi Franc",
	  :fraction 0,
	  :symbol false,
	  :code "BIF"
	}
	:KHR {
	  :text "Riel",
	  :fraction 2,
	  :symbol "៛",
	  :code "KHR"
	}
	:XAF {
	  :text "CF Franc BEAC",
	  :fraction 0,
	  :symbol false,
	  :code "XAF"
	}
	:CAD {
	  :text "Canadian Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "CAD"
	}
	:CVE {
	  :text "Cabo Verde Escudo",
	  :fraction 2,
	  :symbol false,
	  :code "CVE"
	}
	:KYD {
	  :text "Cayman Islands Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "KYD"
	}
	:CLF {
	  :text "Unidad de Fomento",
	  :fraction 4,
	  :symbol false,
	  :code "CLF"
	}
	:CLP {
	  :text "Chilean Peso",
	  :fraction 0,
	  :symbol "$",
	  :code "CLP"
	}
	:CNY {
	  :text "Yuan Renminbi",
	  :fraction 2,
	  :symbol "¥",
	  :code "CNY"
	}
	:COP {
	  :text "Colombian Peso",
	  :fraction 2,
	  :symbol "$",
	  :code "COP"
	}
	:COU {
	  :text "Unidad de Valor Real",
	  :fraction 2,
	  :symbol false,
	  :code "COU"
	}
	:KMF {
	  :text "Comoro Franc",
	  :fraction 0,
	  :symbol false,
	  :code "KMF"
	}
	:CDF {
	  :text "Congolese Franc",
	  :fraction 2,
	  :symbol false,
	  :code "CDF"
	}
	:NZD {
	  :text "New Zealand Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "NZD"
	}
	:CRC {
	  :text "Cost Rican Colon",
	  :fraction 2,
	  :symbol "₡",
	  :code "CRC"
	}
	:HRK {
	  :text "Croatian Kuna",
	  :fraction 2,
	  :symbol "kn",
	  :code "HRK"
	}
	:CUC {
	  :text "Peso Convertible",
	  :fraction 2,
	  :symbol false,
	  :code "CUC"
	}
	:CUP {
	  :text "Cuban Peso",
	  :fraction 2,
	  :symbol "₱",
	  :code "CUP"
	}
	:ANG {
	  :text "Netherlands Antillean Guilder",
	  :fraction 2,
	  :symbol "ƒ",
	  :code "ANG"
	}
	:CZK {
	  :text "Czech Koruna",
	  :fraction 2,
	  :symbol "Kč",
	  :code "CZK"
	}
	:DKK {
	  :text "Danish Krone",
	  :fraction 2,
	  :symbol "kr",
	  :code "DKK"
	}
	:DJF {
	  :text "Djibouti Franc",
	  :fraction 0,
	  :symbol false,
	  :code "DJF"
	}
	:DOP {
	  :text "Dominican Peso",
	  :fraction 2,
	  :symbol "RD$",
	  :code "DOP"
	}
	:EGP {
	  :text "Egyptian Pound",
	  :fraction 2,
	  :symbol "£",
	  :code "EGP"
	}
	:SVC {
	  :text "El Salvador Colon",
	  :fraction 2,
	  :symbol "$",
	  :code "SVC"
	}
	:ERN {
	  :text "Nakfa",
	  :fraction 2,
	  :symbol false,
	  :code "ER"
	}
	:ETB {
	  :text "Ethiopian Birr",
	  :fraction 2,
	  :symbol false,
	  :code "ETB"
	}
	:FKP {
	  :text "Falkland Islands Pound",
	  :fraction 2,
	  :symbol "£",
	  :code "FKP"
	}
	:FJD {
	  :text "Fiji Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "FJD"
	}
	:XPF {
	  :text "CFP Franc",
	  :fraction 0,
	  :symbol false,
	  :code "XPF"
	}
	:GMD {
	  :text "Dalasi",
	  :fraction 2,
	  :symbol false,
	  :code "GMD"
	}
	:GEL {
	  :text "Lari",
	  :fraction 2,
	  :symbol false,
	  :code "GEL"
	}
	:GHS {
	  :text "Ghan Cedi",
	  :fraction 2,
	  :symbol false,
	  :code "GHS"
	}
	:GIP {
	  :text "Gibraltar Pound",
	  :fraction 2,
	  :symbol "£",
	  :code "GIP"
	}
	:GTQ {
	  :text "Quetzal",
	  :fraction 2,
	  :symbol "Q",
	  :code "GTQ"
	}
	:GBP {
	  :text "Pound Sterling",
	  :fraction 2,
	  :symbol "£",
	  :code "GBP"
	}
	:GNF {
	  :text "Guine Franc",
	  :fraction 0,
	  :symbol false,
	  :code "GNF"
	}
	:GYD {
	  :text "Guyan Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "GYD"
	}
	:HTG {
	  :text "Gourde",
	  :fraction 2,
	  :symbol false,
	  :code "HTG"
	}
	:HNL {
	  :text "Lempira",
	  :fraction 2,
	  :symbol "L",
	  :code "HNL"
	}
	:HKD {
	  :text "Hong Kong Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "HKD"
	}
	:HUF {
	  :text "Forint",
	  :fraction 2,
	  :symbol "Ft",
	  :code "HUF"
	}
	:ISK {
	  :text "Iceland Krona",
	  :fraction 0,
	  :symbol "kr",
	  :code "ISK"
	}
	:IDR {
	  :text "Rupiah",
	  :fraction 2,
	  :symbol "Rp",
	  :code "IDR"
	}
	:XDR {
	  :text "SDR (Special Drawing Right)",
	  :fraction 0,
	  :symbol false,
	  :code "XDR"
	}
	:IRR {
	  :text "Iranian Rial",
	  :fraction 2,
	  :symbol "﷼",
	  :code "IRR"
	}
	:IQD {
	  :text "Iraqi Dinar",
	  :fraction 3,
	  :symbol false,
	  :code "IQD"
	}
	:ILS {
	  :text "New Israeli Sheqel",
	  :fraction 2,
	  :symbol "₪",
	  :code "ILS"
	}
	:JMD {
	  :text "Jamaican Dollar",
	  :fraction 2,
	  :symbol "J$",
	  :code "JMD"
	}
	:JPY {
	  :text "Yen",
	  :fraction 0,
	  :symbol "¥",
	  :code "JPY"
	}
	:JOD {
	  :text "Jordanian Dinar",
	  :fraction 3,
	  :symbol false,
	  :code "JOD"
	}
	:KZT {
	  :text "Tenge",
	  :fraction 2,
	  :symbol "лв",
	  :code "KZT"
	}
	:KES {
	  :text "Kenyan Shilling",
	  :fraction 2,
	  :symbol false,
	  :code "KES"
	}
	:KPW {
	  :text "North Korean Won",
	  :fraction 2,
	  :symbol "₩",
	  :code "KPW"
	}
	:KRW {
	  :text "Won",
	  :fraction 0,
	  :symbol "₩",
	  :code "KRW"
	}
	:KWD {
	  :text "Kuwaiti Dinar",
	  :fraction 3,
	  :symbol false,
	  :code "KWD"
	}
	:KGS {
	  :text "Som",
	  :fraction 2,
	  :symbol "лв",
	  :code "KGS"
	}
	:LAK {
	  :text "Kip",
	  :fraction 2,
	  :symbol "₭",
	  :code "LAK"
	}
	:LBP {
	  :text "Lebanese Pound",
	  :fraction 2,
	  :symbol "£",
	  :code "LBP"
	}
	:LSL {
	  :text "Loti",
	  :fraction 2,
	  :symbol false,
	  :code "LSL"
	}
	:ZAR {
	  :text "Rand",
	  :fraction 2,
	  :symbol "R",
	  :code "ZAR"
	}
	:LRD {
	  :text "Liberian Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "LRD"
	}
	:LYD {
	  :text "Libyan Dinar",
	  :fraction 3,
	  :symbol false,
	  :code "LYD"
	}
	:CHF {
	  :text "Swiss Franc",
	  :fraction 2,
	  :symbol "CHF",
	  :code "CHF"
	}
	:LTL {
	  :text "Lithuanian Litas",
	  :fraction 2,
	  :symbol "Lt",
	  :code "LTL"
	}
	:MOP {
	  :text "Pataca",
	  :fraction 2,
	  :symbol false,
	  :code "MOP"
	}
	:MKD {
	  :text "Denar",
	  :fraction 2,
	  :symbol "ден",
	  :code "MKD"
	}
	:MGA {
	  :text "Malagasy riary",
	  :fraction 2,
	  :symbol false,
	  :code "MGA"
	}
	:MWK {
	  :text "Kwacha",
	  :fraction 2,
	  :symbol false,
	  :code "MWK"
	}
	:MYR {
	  :text "Malaysian Ringgit",
	  :fraction 2,
	  :symbol "RM",
	  :code "MYR"
	}
	:MVR {
	  :text "Rufiyaa",
	  :fraction 2,
	  :symbol false,
	  :code "MVR"
	}
	:MRO {
	  :text "Ouguiya",
	  :fraction 2,
	  :symbol false,
	  :code "MRO"
	}
	:MUR {
	  :text "Mauritius Rupee",
	  :fraction 2,
	  :symbol "₨",
	  :code "MUR"
	}
	:XUA {
	  :text "ADB Unit of ccount",
	  :fraction 0,
	  :symbol false,
	  :code "XUA"
	}
	:MXN {
	  :text "Mexican Peso",
	  :fraction 2,
	  :symbol "$",
	  :code "MX"
	}
	:MXV {
	  :text "Mexican Unidad de Inversion (UDI)",
	  :fraction 2,
	  :symbol false,
	  :code "MXV"
	}
	:MDL {
	  :text "Moldovan Leu",
	  :fraction 2,
	  :symbol false,
	  :code "MDL"
	}
	:MNT {
	  :text "Tugrik",
	  :fraction 2,
	  :symbol "₮",
	  :code "MNT"
	}
	:MAD {
	  :text "Moroccan Dirham",
	  :fraction 2,
	  :symbol false,
	  :code "MAD"
	}
	:MZN {
	  :text "Mozambique Metical",
	  :fraction 2,
	  :symbol "MT",
	  :code "MZ"
	}
	:MMK {
	  :text "Kyat",
	  :fraction 2,
	  :symbol false,
	  :code "MMK"
	}
	:NAD {
	  :text "Namibi Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "NAD"
	}
	:NPR {
	  :text "Nepalese Rupee",
	  :fraction 2,
	  :symbol "₨",
	  :code "NPR"
	}
	:NIO {
	  :text "Cordob Oro",
	  :fraction 2,
	  :symbol "C$",
	  :code "NIO"
	}
	:NGN {
	  :text "Naira",
	  :fraction 2,
	  :symbol "₦",
	  :code "NG"
	}
	:OMR {
	  :text "Rial Omani",
	  :fraction 3,
	  :symbol "﷼",
	  :code "OMR"
	}
	:PKR {
	  :text "Pakistan Rupee",
	  :fraction 2,
	  :symbol "₨",
	  :code "PKR"
	}
	:PAB {
	  :text "Balboa",
	  :fraction 2,
	  :symbol "B/.",
	  :code "PAB"
	}
	:PGK {
	  :text "Kina",
	  :fraction 2,
	  :symbol false,
	  :code "PGK"
	}
	:PYG {
	  :text "Guarani",
	  :fraction 0,
	  :symbol "Gs",
	  :code "PYG"
	}
	:PEN {
	  :text "Nuevo Sol",
	  :fraction 2,
	  :symbol "S/.",
	  :code "PE"
	}
	:PHP {
	  :text "Philippine Peso",
	  :fraction 2,
	  :symbol "₱",
	  :code "PHP"
	}
	:PLN {
	  :text "Zloty",
	  :fraction 2,
	  :symbol "zł",
	  :code "PL"
	}
	:QAR {
	  :text "Qatari Rial",
	  :fraction 2,
	  :symbol "﷼",
	  :code "QAR"
	}
	:RON {
	  :text "New Romanian Leu",
	  :fraction 2,
	  :symbol "lei",
	  :code "RO"
	}
	:RUB {
	  :text "Russian Ruble",
	  :fraction 2,
	  :symbol "руб",
	  :code "RUB"
	}
	:RWF {
	  :text "Rwand Franc",
	  :fraction 0,
	  :symbol false,
	  :code "RWF"
	}
	:SHP {
	  :text "Saint Helen Pound",
	  :fraction 2,
	  :symbol "£",
	  :code "SHP"
	}
	:WST {
	  :text "Tala",
	  :fraction 2,
	  :symbol false,
	  :code "WST"
	}
	:STD {
	  :text "Dobra",
	  :fraction 2,
	  :symbol false,
	  :code "STD"
	}
	:SAR {
	  :text "Saudi Riyal",
	  :fraction 2,
	  :symbol "﷼",
	  :code "SAR"
	}
	:RSD {
	  :text "Serbian Dinar",
	  :fraction 2,
	  :symbol "Дин.",
	  :code "RSD"
	}
	:SCR {
	  :text "Seychelles Rupee",
	  :fraction 2,
	  :symbol "₨",
	  :code "SCR"
	}
	:SLL {
	  :text "Leone",
	  :fraction 2,
	  :symbol false,
	  :code "SLL"
	}
	:SGD {
	  :text "Singapore Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "SGD"
	}
	:XSU {
	  :text "Sucre",
	  :fraction 0,
	  :symbol false,
	  :code "XSU"
	}
	:SBD {
	  :text "Solomon Islands Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "SBD"
	}
	:SOS {
	  :text "Somali Shilling",
	  :fraction 2,
	  :symbol "S",
	  :code "SOS"
	}
	:SSP {
	  :text "South Sudanese Pound",
	  :fraction 2,
	  :symbol false,
	  :code "SSP"
	}
	:LKR {
	  :text "Sri Lank Rupee",
	  :fraction 2,
	  :symbol "₨",
	  :code "LKR"
	}
	:SDG {
	  :text "Sudanese Pound",
	  :fraction 2,
	  :symbol false,
	  :code "SDG"
	}
	:SRD {
	  :text "Surinam Dollar",
	  :fraction 2,
	  :symbol "$",
	  :code "SRD"
	}
	:SZL {
	  :text "Lilangeni",
	  :fraction 2,
	  :symbol false,
	  :code "SZL"
	}
	:SEK {
	  :text "Swedish Krona",
	  :fraction 2,
	  :symbol "kr",
	  :code "SEK"
	}
	:CHE {
	  :text "WIR Euro",
	  :fraction 2,
	  :symbol false,
	  :code "CHE"
	}
	:CHW {
	  :text "WIR Franc",
	  :fraction 2,
	  :symbol false,
	  :code "CHW"
	}
	:SYP {
	  :text "Syrian Pound",
	  :fraction 2,
	  :symbol "£",
	  :code "SYP"
	}
	:TWD {
	  :text "New Taiwan Dollar",
	  :fraction 2,
	  :symbol "NT$",
	  :code "TWD"
	}
	:TJS {
	  :text "Somoni",
	  :fraction 2,
	  :symbol false,
	  :code "TJS"
	}
	:TZS {
	  :text "Tanzanian Shilling",
	  :fraction 2,
	  :symbol false,
	  :code "TZS"
	}
	:THB {
	  :text "Baht",
	  :fraction 2,
	  :symbol "฿",
	  :code "THB"
	}
	:TOP {
	  :text "Pa’anga",
	  :fraction 2,
	  :symbol false,
	  :code "TOP"
	}
	:TTD {
	  :text "Trinidad nd Tobago Dollar",
	  :fraction 2,
	  :symbol "TT$",
	  :code "TTD"
	}
	:TND {
	  :text "Tunisian Dinar",
	  :fraction 3,
	  :symbol false,
	  :code "TND"
	}
	:TRY {
	  :text "Turkish Lira",
	  :fraction 2,
	  :symbol "₺",
	  :code "TRY"
	}
	:TMT {
	  :text "Turkmenistan New Manat",
	  :fraction 2,
	  :symbol false,
	  :code "TMT"
	}
	:UGX {
	  :text "Ugand Shilling",
	  :fraction 0,
	  :symbol false,
	  :code "UGX"
	}
	:UAH {
	  :text "Hryvnia",
	  :fraction 2,
	  :symbol "₴",
	  :code "UAH"
	}
	:AED {
	  :text "UAE Dirham",
	  :fraction 2,
	  :symbol false,
	  :code "AED"
	}
	:USN {
	  :text "US Dollar (Next day)",
	  :fraction 2,
	  :symbol false,
	  :code "US"
	}
	:UYI {
	  :text "Uruguay Peso en Unidades Indexadas (URUIURUI)",
	  :fraction 0,
	  :symbol false,
	  :code "UYI"
	}
	:UYU {
	  :text "Peso Uruguayo",
	  :fraction 2,
	  :symbol "$U",
	  :code "UYU"
	}
	:UZS {
	  :text "Uzbekistan Sum",
	  :fraction 2,
	  :symbol "лв",
	  :code "UZS"
	}
	:VUV {
	  :text "Vatu",
	  :fraction 0,
	  :symbol false,
	  :code "VUV"
	}
	:VEF {
	  :text "Bolivar",
	  :fraction 2,
	  :symbol "Bs",
	  :code "VEF"
	}
	:VND {
	  :text "Dong",
	  :fraction 0,
	  :symbol "₫",
	  :code "VND"
	}
	:YER {
	  :text "Yemeni Rial",
	  :fraction 2,
	  :symbol "﷼",
	  :code "YER"
	}
	:ZMW {
	  :text "Zambian Kwacha",
	  :fraction 2,
	  :symbol false,
	  :code "ZMW"
	}
	:ZWL {
	  :text "Zimbabwe Dollar",
	  :fraction 2,
	  :symbol false,
	  :code "ZWL"
	}
})

(defn sorted-iso4217
  []
  (let [primary-currencies [:CAD :EUR :GBP :USD]
        main-currencies (vals (select-keys iso4217 primary-currencies))
        separator {:text "---------" :fraction 0 :symbol "" :code ""}
        with-separator (concat main-currencies [separator])
        all-the-rest (apply dissoc iso4217 primary-currencies)
        sorted-rest (sort-by :text (vals all-the-rest))]
    (concat with-separator sorted-rest)))