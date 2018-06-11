(ns oc.privacy)

(defn privacy [{contact-mail-to :contact-mail-to contact-email :contact-email}]
  [:div.container.privacy-policy-page
    [:div.legal-link "Click here to see OpenCompany " [:a {:href "/terms"} "Terms of Service"] "."]
    [:div.readable-content-container
      [:h1 "Privacy policy"]
      [:p "This Privacy Policy describes OpenCompany's policies and procedures on the collection, use and disclosure of your information. OpenCompany receives your information through our Carrot web site, APIs, services and third parties (\"Services\"). When using any of our Services you consent to the collection, transfer, manipulation, storage, disclosure and other uses of your information as described in this Privacy Policy. Irrespective of which country that you reside in or create information from, your information may be used by OpenCompany in the United States or any other country where OpenCompany operates."]
      [:h2 "Information Collection and Use"]
      [:p [:i "We collect and use your information to provide our Services and improve them over time."]]
      [:p "Information Collected Upon Registration: When you create or modify an OpenCompany account, you provide some personal information, such as your name, email address, and password. The Personal Information you provide is used for such purposes as allowing you to set up a user account and profile that can be used to interact with the Service, improving the content of the Service, customizing the content you see, and communicating with you about specials and new features."]
      [:p "Information Collected Automatically: When you use the Service, OpenCompany automatically receives and records information on our server logs from your browser or mobile platform, including your location, IP address, cookie information, and the page you requested. We treat this data as non-Identifying Information, except where we are required to do otherwise under applicable law. OpenCompany only uses this data in aggregate form. We may provide aggregate information to our partners about how OpenCompany Users, collectively, use our Service, so that our partners may also understand how often people use their services and our Service."]
      [:p "We may combine your Personal Information with Non-Identifying Information and aggregate it with information collected from other OpenCompany Users (defined below) to attempt to provide you with a better experience, to improve the quality and value of the Service and to analyze and understand how our Services are being used. We may also use the combined information without aggregating it to serve you specifically, for instance to deliver a product to you according to your preferences or restrictions."]
      [:p "We also use your Personal Information to contact you with OpenCompany newsletters, marketing materials, and other information that may be of interest to you. If you decide at any time that you no longer wish to receive such communications from us, please follow the unsubscribe instructions provided in any of the communications or update your \"user profile\" information. Please note that we may also use your Personal Information to contact you with information related to your use of the Service; you may not opt out of these notifications."]
      [:p "Log Data"]
      [:p "When you visit the Service, whether as a OpenCompany User or a non-registered user just browsing (any of these, a \"OpenCompany User\"), our servers automatically record information that your browser sends whenever you visit a website (\"Log Data\"). This Log Data may include information such as your computer's Internet Protocol (\"IP\") address, browser type or the webpage you were visiting before you came to our Service, pages of our Service that you visit, the time spent on those pages, information you search for on our Service, access times and dates, and other statistics. We use this information to monitor and analyze use of the Service and for the Service's technical administration, to increase our Service's functionality and user-friendliness, and to better tailor it to our visitors' needs. We also use this information to verify that visitors to the Service meet the criteria required to process their requests. We do not treat Log Data as Personal Information or use it in association with other Personal Information, though we may aggregate, analyze and evaluate such information for the same purposes as stated above regarding other Non-Identifying Information."]
      [:p "Cookies"]
      [:p "Like many websites, we use \"cookies\" to collect information. A cookie is a small data file that we transfer to your computer's hard disk for record-keeping purposes. We use cookies for two purposes. First, we utilize persistent cookies to save your registration ID and login password for future logins to the Service. Second, we utilize session ID cookies to enable certain features of the Service, to better understand how you interact with the Service and to monitor aggregate usage by OpenCompany Users and web traffic routing on the Service. Unlike persistent cookies, session cookies are deleted from your computer when you log off from the Service and then close your browser. Third party advertisers on the Service may also place or read cookies on your browser. You can instruct your browser, by changing its options, to stop accepting cookies or to prompt you before accepting a cookie from the websites you visit. If you do not accept cookies, however, you may not be able to use all portions of the Service."]
      [:p "Web Beacons"]
      [:p "Our Web pages may contain electronic images known as Web beacons (sometimes called single-pixel gifs) and are used along with cookies to compile aggregated statistics to analyze how our Service is used and may be used in some of our emails to let us know which emails and links have been opened by recipients. This allows us to gauge the effectiveness of our customer communications and marketing campaigns."]
      [:p "Information Sharing and Disclosure"]
      [:p [:i "We do not disclose your private information except in the limited circumstances described here."]]
      [:p "Information you enter into OpenCompany will be accessible by other users on your team, unless you mark information as Private or Public.  "]
      [:p "Information marked Private will be accessible by anyone you invite to view and edit your information. Information marked as Public is accessible by anyone, and could be included in search engines such as Google.  "]
      [:p "INTERNATIONAL TRANSFER"]
      [:p "YOUR INFORMATION MAY BE TRANSFERRED TO - AND MAINTAINED ON - COMPUTERS LOCATED OUTSIDE OF YOUR STATE, PROVINCE, COUNTRY OR OTHER GOVERNMENTAL JURISDICTION WHERE THE PRIVACY LAWS MAY NOT BE AS PROTECTIVE AS THOSE IN YOUR JURISDICTION. IF YOU ARE LOCATED OUTSIDE THE UNITED STATES AND CHOOSE TO PROVIDE INFORMATION TO US, TALKTO TRANSFERS PERSONAL INFORMATION TO THE UNITED STATES AND PROCESSES IT THERE. YOUR CONSENT TO THIS PRIVACY POLICY FOLLOWED BY YOUR SUBMISSION OF SUCH INFORMATION REPRESENTS YOUR AGREEMENT TO THAT TRANSFER."]
      [:p [:i "Phishing"]]
      [:p "Safeguarding information to help protect you from identity theft and phishing is a top priority. OpenCompany will not, at any time, request your credit card information, your account ID, login password, or national identification numbers in a non-secure or unsolicited e-mail or telephone communication. For more information about phishing, visit the Federal Trade Commission's website at http://www.ftc.gov."]
      [:p [:i "Aggregate Information and Non-Identifying Information"]]
      [:p "We may share aggregated information that does not include Personal Information and we may otherwise disclose Non-Identifying Information and Log Data with third parties for industry analysis, demographic profiling and other purposes. Any aggregated information shared in these contexts will not contain your Personal Information."]
      [:p [:i "Third Party Services"]]
      [:p "OpenCompany uses a variety of services hosted by third parties to help provide our Services, such as hosting our blog, and to help us understand the use of our Services, such as Google Analytics. These services may collect information sent by your browser as part of a web page request, such as cookies or your IP request."]
      [:p "OpenCompany will also share or disclose your information with your consent, such as when you use a third party client to access the OpenCompany Services."]
      [:p [:i "Law and Harm"]]
      [:p "We may disclose your information if we believe that it is reasonably necessary to comply with a law, regulation or legal request; to protect the safety of any person; to address fraud, security or technical issues; or to protect OpenCompany's rights or property."]
      [:p [:i "Business Transfers"]]
      [:p "In the event that OpenCompany is involved in a bankruptcy, merger, acquisition, reorganization or sale of assets, your information may be sold or transferred as part of that transaction. The promises in this privacy policy will apply to your information as transferred to the new entity."]
      [:p [:i "Modifying Your Personal Information"]]
      [:p "If you are a registered user of our Services, we provide you with tools to access or modify the personal information you provided to us and associated with your account. Even after you delete your account or profile, copies of that information may remain viewable elsewhere, to the extent it has been shared with others."]
      [:p [:i "Security"]]
      [:p "Your OpenCompany account is protected by a password for your privacy and security. You need to prevent unauthorized access to your account and Personal Information by selecting and protecting your password appropriately, and limiting access to your computer and browser by signing off after you have finished accessing your account."]
      [:p "OpenCompany cannot, however, guarantee the security of user account information. No method of transmission over the Internet, or method of electronic storage, is 100% secure; and, unauthorized entry or use, hardware or software failure, and other factors, may compromise the security of user information at any time."]
      [:p [:i "Links to Other Services"]]
      [:p "Our Service contains links to other websites. If you choose to visit an advertiser by \"clicking on\" a banner ad or other type of advertisement, or click on another third party link, you will be directed to that third party's website. The fact that we link to a website or present a banner ad or other type of advertisement is not an endorsement, authorization or representation of our affiliation with that third party, nor is it an endorsement of their privacy or information security policies or practices. We do not exercise control over third party websites. These other websites may place their own cookies or other files on your computer, collect data or solicit personal information from you. Other Services follow different rules regarding the use or disclosure of the personal information you submit to them."]
      [:p [:i "Logging In with Slack"]]
      [:p "You can log in to our Service using Slack. This service will authenticate your identity and provide you the option to share certain personal information with us such as your name, email address, and picture to pre-populate your profile."]
      [:p [:i "Our Policy Toward Children"]]
      [:p "OpenCompany does not knowingly collect personally identifiable information from children under 13. If a parent or guardian becomes aware that his or her child has provided us with Personal Information without their consent, he or she should contact us at support@OpenCompany.com. If we become aware that a child under 13 has provided us with Personal Information, we will take steps to delete such information and terminate the child’s account."]
      [:p [:i "Changes to this Policy"]]
      [:p "We may revise this Privacy Policy from time to time. The most current version of the policy will govern our use of your information and will always be at [:a {:href \"http://carrot.io/privacy\"} \"http://carrot.io/privacy\"]. If we make a change to this policy that, in our sole discretion, is material, we will notify you via the e-mail address associated with your account. By continuing to access or use the Services after those changes become effective, you agree to be bound by the revised Privacy Policy."]
      [:p [:i "This Privacy Policy is effective as of August 16, 2017"]]
      [:p "Questions about this Privacy Policy? Please let us know at " [:a {:href "mailto:hello@carrot.io"} "hello@carrot.io"] "."]
]])