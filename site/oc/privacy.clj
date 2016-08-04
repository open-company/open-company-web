(ns oc.privacy)

(defn privacy [{contact-mail-to :contact-mail-to contact-email :contact-email}]
  [:div.container.outer.section.content
    [:div.row
     [:div.col-md-12

      [:h1 "Our Privacy Principles"]
      [:p "If you read nothing else, please read this:"]
      [:ul
        [:li "By default, anything you post to OpenCompany is private to your team. That is, viewing your information requires authentication as a member of that team unless the information has been actively shared with someone outside the team, such as investors, advisors, and customers."]
        [:li "Companies can choose to use OpenCompany for free, but the free version does not maintain a private dashboard. The free version is for a public dashboard, which means your information can be viewed by anyone that comes to your URL, for example, https://opencompany.com/your-company-name."]
        [:li "We try to make our product easy to use, with settings and options that are easy to find and understand. This is good for privacy, good for the product, and good for OpenCompany customers and users."]]

      [:h1 "OpenCompany Privacy Policy"]
      [:p "Updated: August 1, 2016"]
      [:p "This privacy policy is here to help you understand what information we collect at OpenCompany, how we use it, and what choices you have. When we talk about OpenCompany in this policy, we are talking about OpenCompany, LLC, the company and the OpenCompany application and website at "
        [:a {:href "https://opencompany.com"} "https://opencompany.com"]
        ". OpenCompany is available for use via a web browser on your desktop or mobile device."]
      [:p "This policy describes how OpenCompany treats your information, not how other organizations treat your information. If you are using OpenCompany in a workplace or on a device or account issued to you by your employer or another organization, that company or organization likely has its own policies regarding storage, access, modification, deletion, and retention of communications and content which may apply to your use of OpenCompany. Please check with your employer about the policies it has in place regarding your communications and related content on OpenCompany."]

      [:h2 "Information we collect and receive"]
      [:p "We collect different kinds of information. Some of it is personally identifiable and some is non-identifying or aggregated. Here are the types of information we collect or receive:"]
      [:ul
        [:li 
          [:strong "Team information. "]
          "When you create a team on OpenCompany, we collect your email address, your company name, OpenCompany domain (ex: opencompany.com/your-team-name), your Slack user name, team name, and personal and team avatars. "]
        [:li
          [:strong "Account and profile information. "]
          "The only information we require to create your OpenCompany account is a Slack authentication and company name."]
        [:li
          [:strong "Billing information. "]
          "If you purchase a subscription for OpenCompany, our third party payment processors will collect and store your billing address and credit card information."]
        [:li
          [:strong "Log data. "]
          "When you use OpenCompany, our servers automatically record information, including information that your browser sends whenever you visit a website when you’re using it. This log data may include your Internet Protocol address, the address of the web page you visited before coming to OpenCompany, your browser type and settings, the date and time of your request, information about your browser configuration and plug-ins, language preferences, and cookie data."]
        [:li
          [:strong "Device information "]
          "In addition to log data, we may also collect information about the device you’re using OpenCompany on, including what type of device it is, what operating system you’re using, device settings, unique device identifiers, and crash data. Whether we collect some or all of this information often depends on what type of device you’re using and its settings."]
        [:li
          [:strong "Geo-location information. "]
          "WiFi and IP addresses received from your browser or device may be used to determine approximate location."]
        [:li
          [:strong "OpenCompany usage information. "]
          "This is information about features, content, and links you interact with within OpenCompany and what integrations with related services you use."]
        [:li
          [:strong "Content that you add and share within OpenCompany. "]
          "This includes:"
          [:ul
            [:li "Text, images, and other types of files."]
            [:li "When text, images or files were added and by whom, when or if they were seen by you, and how you received them."]]]
        [:li
          [:strong "Information from partners or other 3rd parties. "]
          "OpenCompany may receive information from partners or others that we could use to make our own information better or more useful. This might be aggregate level information about which IP addresses go with which zip codes or it might be more specific information about how well an online marketing or email campaign performed."]]

      [:h2 "Our cookie policy"]
      [:p "OpenCompany uses cookies, or similar technologies like single-pixel gifs and web beacons, to record log data. We use both session-based and persistent cookies."]
      [:p "Cookies are small text files sent by us to your computer and from your computer to us, each time you visit our website. They are unique to your OpenCompany account or your browser. Session-based cookies last only while your browser is open and are automatically deleted when you close your browser. Persistent cookies last until you or your browser delete them or until they expire."]
      [:p "Some cookies are associated with your OpenCompany account and personal information in order to remember that you are logged in and which teams you are logged into. Other cookies are not tied to your OpenCompany account but are unique and allow us to do site analytics and customization, among other similar things. If you access OpenCompany through your browser, you can manage your cookie settings there but if you disable all cookies you may not be able to use OpenCompany."]
      [:p "OpenCompany sets and accesses our own cookies on our company-owned domains. In addition, we use 3rd parties for website analytics. You may opt-out of third party cookies on their respective websites. We do not currently recognize or respond to browser-initiated Do Not Track signals as there is no consistent industry standard for compliance."]

      [:h2 "How we use your information"]
      [:p "We use your information for the following:"]
      [:ul
        [:li
          [:strong "Providing the OpenCompany service. "]
          "We use information you provide to authenticate you and deliver content to you and from you."]
        [:li
          [:strong "Understanding and improving our products. "]
          "To make the product better we have to understand how users are using it. We have a fair bit of data about usage and we intend to use it many different ways to improve our products, including research. This policy is not intended to place any limits on what we do with usage data that is aggregated or de-identified so it is no longer tied to a OpenCompany user."]
        [:li
          [:strong "Investigating and preventing bad stuff from happening. "]
          "We work hard to keep OpenCompany secure and to prevent abuse and fraud."]
        [:li
          [:strong "Communicating with you. "]
          [:ul
            [:li
              [:strong "Solving your problems and responding to your requests. "]
              "If you contact us with a problem or question, we will use your information to respond to that request and address your problems or concerns."]
            [:li
              [:strong "In-product communications. "]
              "We may use the information you provide to contact you through OpenCompany or other in-product messaging tools."]
            [:li
              [:strong "Email messages. "]
              "We may send you service and administrative emails. For example, to inform you about changes in our services, our service offerings and important service related notices, such as changes to this policy or security and fraud notices. These messages are considered part of the service and you may not opt-out of them. In addition, we sometimes send emails to OpenCompany users about new product features or other news about OpenCompany. You can opt-out of these at any time."]]]]

      [:h2 "Your choices"]
      [:ul
        [:li "If you are a member of a free, public dashboard, the content and images you include can be seen by anyone visiting your OpenCompany URL. You can choose to make your information private by choosing that option in Company Settings."]
        [:li "If you are a member of a paid, private dashboard, you can choose with whom to share your information. You can share the information via email, via Slack, or via a URL link."]
        [:li "The browser you use may provide you with the ability to control cookies or other types of local data storage."]
        [:li "Your mobile device may provide you with choices around how and whether location or other data is shared with us."]
        [:li "OpenCompany does not control these choices, or default settings, which are offered by makers of your browser or mobile device."]]

      [:h2 "Sharing and disclosure"]
      [:p "There are times when communications and related content and other user information may be shared by OpenCompany. This section discusses only how OpenCompany may share user information. Organizations that use OpenCompany may have their own policies for sharing and disclosure of information they can access through OpenCompany. OpenCompany may share information:"]
      [:ul
        [:li
          [:strong "With consent, to comply with legal process, or to protect OpenCompany and our users. "]
          "When we have your consent or if we believe that disclosure is reasonably necessary to comply with a law, regulation or legal request; to protect the safety, rights, or property of the public, any person, or OpenCompany; or to detect, prevent, or otherwise address fraud, security or technical issues. If we receive a law enforcement or other third party request for information we will provide prior notice to the subject of the request where we are legally permitted to do so."]
        [:li
          [:strong "With third party service providers and agents. "]
          "We may employ third party companies or individuals to process personal information on our behalf based on our instructions and in compliance with this Privacy Policy. For example, we may share data with a security consultant to help us get better at preventing unauthorized access or with an email vendor to send messages on our behalf. We may also share data with hosting providers, payment processors, marketing vendors, and other consultants who work on our behalf."]
        [:li "If we engage in a merger, acquisition, bankruptcy, dissolution, reorganization, sale of some or all of OpenCompany's assets financing, acquisition of all or a portion of our business, or similar transaction or proceeding that involves the transfer of the information described in this Privacy Policy."]
        [:li
          [:strong "For Business and Research Purposes. "]
          "We may also share aggregated or de-identified information with our partners or others for business or research purposes. For example, we may tell a prospective OpenCompany customer the average number of topics updated within an OpenCompany dashboard in a month or may partner with research firm or academics to explore interesting questions about workplace communications. Again, this policy is not intended to prohibit the disclosure and use of aggregated or de-identified data."]]

      [:h2 "Security"]
      [:p "OpenCompany takes reasonable steps to protect information you provide to us as part of your use of the OpenCompany service from loss, misuse, and unauthorized access or disclosure. These steps take into account the sensitivity of the information we collect, process and store and the current state of technology. When you enter sensitive information (such as sign-in credentials) we encrypt the transmission of that information using secure socket layer technology (SSL). We follow generally accepted standards to protect the personal data submitted to us, both during transmission and once we receive it. However, no electronic or email transmission or digital storage mechanism is ever fully secure or error free."]

      [:h2 "Children's Information"]
      [:p "OpenCompany is not directed to children under 13. If you learn that a minor child has provided us with personal information without your consent, please "
        [:a {:href contact-mail-to} "contact us."]]

      [:h2 "Changes to this Privacy Policy"]
      [:p "We may change this policy from time to time, and if we do we’ll post any changes on this page. If you continue to use OpenCompany after those changes are in effect, you agree to the revised policy. If the changes are material, we may provide more prominent notice or seek your consent to the new policy."]

      [:h2 "Contacting OpenCompany"]
      [:p "Please also feel free to contact us if you have any questions about OpenCompany’s Privacy Policy or practices. You may contact us at "
        [:a {:href contact-mail-to} contact-email]]]]])