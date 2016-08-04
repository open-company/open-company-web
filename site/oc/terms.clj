(ns oc.terms)

(defn terms [{contact-mail-to :contact-mail-to contact-email :contact-email}]
  [:div.container.outer.section.content
    [:div.row
     [:div.col-md-12
      [:h1 "OpenCompany Terms of Service"]
      [:p "Effective: August 1, 2016"]

      [:ol

        [:li [:strong "Acceptance of Terms."]
          [:ol
            [:li "This Terms of Service document, "
              [:strong "INCLUDING THE BINDING ARBITRATION PROVISION CONTAINED IN SECTION 10 "]
              "(the “"
              [:strong "TOS"]
              "), is an agreement you must accept in order to use OpenCompany’s Service (as defined below). It is applicable to (a) “"
              [:strong "Owners"]
              "” -- those who initially set up the Service (b) Contributors – those who add and edit information, and that can share it with others outside of the company; (“"
              [:strong "Contributors"]
              "), and to c) Users – users who are invited to join an existing team that has already been created in the Service by an Owner or Contributor (“"
              [:strong "Users"]
              "”). The terms “"
              [:strong "you"]
              "” and “"
              [:strong "users"]
              "” encompass all users, including both Contributors and Users. This document describes both your rights and your obligations as part of using the Service. It is important that you read it carefully because you will be legally bound to these terms. OpenCompany, LLC (“"
              [:strong "OpenCompany"]
              "” “"
              [:strong "we"]
              "” “"
              [:strong "us"]
              "”) only provides its Service (as defined below) to you subject to this TOS. By accepting this TOS or by accessing or using the Service, you agree to be bound by this TOS (including the Privacy Policy, which is incorporated here by reference)."]
            [:li "If you are entering into this TOS on behalf of a company or other legal entity, you represent that you have the authority to bind such entity, its Contributors, its Users, and its affiliates to this TOS. In that case, the terms “you” or “your” shall also refer to such entity, its Contributors, its Users, and its affiliates, as applicable. If you do not have such authority, or if you do not agree with this TOS, you may not use the Service. You acknowledge that this TOS is a contract between you and OpenCompany, even though it is electronic and is not physically signed by you and OpenCompany, and it governs your use of the Service."]
            [:li "As our business evolves, OpenCompany may change this TOS. If we make a material change to the TOS, we may provide you with reasonable notice prior to the changes either by emailing the email address associated with your account or by posting a notice on the Site. You can review the most current version of the TOS at any time by visiting this page. The revised terms and conditions will become effective on the date set forth in our notice, and if you use the Service after that date, your use will constitute acceptance of the revised terms and conditions. If any change to this TOS is not acceptable to you, your only remedy is to cancel your account and stop using the Services through the process provided in the Service."]
            [:li "You are solely responsible for informing users of the applicable company policies, obtaining any legally required member consent to such policies, and for ensuring that all uses of the Services comply with applicable federal, state and/or international privacy laws, including but not limited to, the Electronic Communications Privacy Act, 18 U.S.C. § 2510 et seq.  Please also see the Privacy Policy for more information on these topics. "]
            [:li "By accessing or using the Services, you affirm that you are at least 18 years of age (or have reached the age of majority if that is not 18 years of age where you live). You represent that you are fully able and competent to enter into and comply with the terms and conditions in this TOS. The Service is not directed to children under 13, so if you are under 13 years of age, you are not permitted to access or use the Services. If we become aware that you are using the Service even though you are under 13, we will deactivate your account."]]]

        [:li [:strong "Description of Service. "]
          "The “"
          [:strong "Service"]
          "” means (a) OpenCompany’s dashboard and shared updates and related systems and technologies, as well as the website "
          [:a {:href "https://opencompany.com"} "opencompany.com"]
          ", and (b) all software (including the Software, as defined below), applications, data, reports, text, images, and other content made available by or on behalf of OpenCompany through any of the foregoing. The “Service” does not include Your Data (as defined below) or any software application or service that is provided by you or a third party, which you use in connection with the Service, whether or not OpenCompany designates them as “official integrations” (each a “"
          [:strong "Non-OpenCompany Product"]
          "”). Any modifications and new features added to the Service are also subject to this TOS. OpenCompany reserves the right to modify or discontinue the Service (or any Service plan) or any feature or functionality thereof at any time without notice to you. All rights, title and interest in and to the Service and its components (including all intellectual property rights) will remain with and belong exclusively to OpenCompany."]

        [:li [:strong "Access and Use of the Service."]
          [:ol
            [:li "You may access and use the Service only for lawful, authorized purposes and you shall not misuse the Service in any manner (as determined by OpenCompany, LLC in its sole discretion). See Section 6 for specific provisions outlining prohibited uses of the Service. You shall comply with any codes of conduct, policies, storage limitations, or other notices OpenCompany provides you or publishes in connection with the Service from time to time, but if any of those policies materially change the TOS, we will provide you with reasonable notice as provided in Section 1.3 above. You shall promptly notify OpenCompany if you learn of a security breach related to the Service."]
            [:li "OpenCompany is an Open Source Software (OSS) project, but may contain proprietary and confidential information that is protected by applicable intellectual property and other laws. Subject to the terms and conditions of this TOS, OpenCompany only grants you a personal, non-sublicensable and non-exclusive license to use the object code of any non OSS solely in connection with the Service. Any rights not expressly granted herein are reserved."]
            [:li "OpenCompany reserves the right to use your name as a reference for marketing or promotional purposes on the Site and in other communication with existing or potential OpenCompany customers. For example, we might list your company on one of our webpages under lists of OpenCompany customers. We don’t want to list customers who don’t want to be listed, so you may send an email to "
              [:a {:href contact-mail-to} contact-email]
              " stating that you do not wish to be used as a reference."]
            [:li "OpenCompany may make available, in its sole discretion, the OpenCompany Application Programming Interface and related documentation, data, code, and other materials provided with the API (collectively “"
              [:strong "API"]
              "”). You undertake use of the API at your own risk, and such use of this API will be governed by this TOS. You hereby agree to be bound by those terms, and acknowledge that violation of OpenCompany’s TOS is grounds for termination of Your Account."]]]

        [:li [:strong "Your Data Rights and Related Responsibilities."]
          [:ol
            [:li "“"
              [:strong "Your Data"]
              "” means any data and content you upload, post, transmit or otherwise made available via the Services (which may include data you elect to import from Non-OpenCompany Products you use). “"
              [:strong "Your Data"]
              "” includes anything you enter or upload into the Service. OpenCompany will make commercially reasonable efforts to ensure that all facilities used to store and process Your Data meet a high standard for security."]
            [:li "In order for us to provide the Service to you, we require that you grant us certain rights with respect to Your Data. For example, we need to be able to transmit, store and copy Your Data in order to display it to you and your teammates, to index it so you are able to search it, to make backups to prevent data loss, and so on. Your acceptance of this TOS gives us the permission to do so and grants us any such rights necessary to provide the service to you, only for the purpose of providing the service (and for no other purpose). This permission includes allowing us to use third-party service providers (such as Amazon Web Services) in the operation and administration of the Service and the rights granted to us are extended to these third parties to the degree necessary in order for the Service to be provided."]
            [:li "If any users send us any feedback or suggestions regarding the Service, you grant OpenCompany an unlimited, irrevocable, perpetual, free license to use any such feedback or suggestions for any purpose without any obligation to you."]
            [:li "You are solely responsible for your conduct (including by and between all users), the content of Your Data, and all communications with others while using the Services. We may choose to review Public Content (as defined below) for compliance with our policies and guidelines, but you acknowledge that OpenCompany has no obligation to monitor any information on the Services. However, OpenCompany may remove or disable any Public Content at any time for any reason or for no reason at all. We are not responsible for the accuracy, appropriateness, or legality of Your Data or any other information you and your users may be able to access using the Services. The Services provide features that allow you and your users to share Your Data and other materials with others or to make it public. Please consider carefully what you allow to be shared or made public."]]]

        [:li [:strong "Payment."]
          [:ol
            [:li "To the extent you use a Service plan that is made available for a fee, you will be required to select a payment plan and provide accurate information regarding your credit card or other payment instrument. You will promptly update your account information with any changes in your payment information. You agree to pay OpenCompany in accordance with the terms set forth on the Site (currently, "
              [:a {:href "https://opencompany.com/pricing"} "opencompany.com/pricing"] 
              " and related pages) and this TOS, and you authorize OpenCompany or its third-party payment processors to bill your payment instrument in advance on a periodic basis in accordance with such terms."]
            [:li "If you dispute any charges you must let OpenCompany know within sixty (60) days after the date that OpenCompany invoices you. All amounts paid are non-refundable and we reserve the right to change our prices in the future. If we increase our prices for your Service plan, we will provide notice of the change on the Site and in email to you at least 30 days before the change is to take effect. Your continued use of the Service after the price change goes into effect constitutes your agreement to pay the changed amount. OpenCompany may choose to bill you through an invoice, in which case, full payment for invoices issued must be received by the date specified in the invoice. Past due fees are subject to a finance charge of 1.5% per month on any outstanding balance, or the maximum permitted by law, whichever is lower, plus all expenses of collection. You shall be responsible for all taxes associated with Services other than U.S. taxes based on OpenCompany’s net income."]]]

        [:li [:strong "Representations and Warranties. "]
          "You represent and warrant to OpenCompany that (i) you have full power and authority to enter into this TOS; (ii) you own all Your Data or have obtained all permissions, releases, rights or licenses required to engage in your activities (and allow OpenCompany to perform its obligations) in connection with the Services without obtaining any further releases or consents; and (iii) Your Data and your other activities in connection with the Service, and OpenCompany’s exercise of all rights and license granted by you herein, do not and will not violate, infringe, or misappropriate any third party’s copyright, trademark, right of privacy or publicity, or other personal or proprietary right, nor does Your Data contain any matter that is unlawful or illegal."]

        [:li [:strong "You also agree not to:"]
          [:ol
            [:li "upload, post, transmit, or otherwise make available any of Your Data that is unlawful or illegal, including without limitation Data that is libelous, or invasive of another's privacy;"]
            [:li "use the Service to harm minors in any way;"]
            [:li "impersonate any person or entity, including, but not limited to, a OpenCompany employee, or other user, or falsely state or otherwise misrepresent your affiliation with a person or entity;"]
            [:li "manipulate identifiers in order to disguise the origin of any of Your Data;"]
            [:li "upload, post, transmit, or otherwise make available any of Your Data that you do not have a right to make available under any law or under contractual or fiduciary relationships (such as inside information, proprietary and confidential information learned or disclosed as part of employment relationships or under nondisclosure agreements);"]
            [:li "upload, post, transmit or otherwise make available any of Your Data in a manner that infringes any patent, trademark, trade secret, copyright or other proprietary rights of any party;"]
            [:li "sublicense, resell, rent, lease, transfer or assign (except as permitted in Section 16) the Service or its use, or offer the Service on a time share basis to any third party;"]
            [:li "use the Service to upload, post, transmit, or otherwise make available any unsolicited or unauthorized advertising, promotional materials, \"junk mail,\" \"spam,\" \"chain letters,\" \"pyramid schemes,\" or any other form of solicitation;"]
            [:li "use the Service to upload, post, transmit, or otherwise make available any software viruses or any other computer code, files or programs designed to interrupt, destroy or limit the functionality of any computer software or hardware;"]
            [:li "interfere with or disrupt the Service or servers or networks connected to the Service, or disobey any requirements, procedures, policies or regulations of networks connected to the Service, including using any device or software;"]
            [:li "modify, adapt, or hack the Service, including by using any non-public OpenCompany APIs, or otherwise attempt to gain unauthorized access to the Service or its related systems or networks."]
            [:li "intentionally or unintentionally violate any applicable local, state, national or international law in connection with your use of the Service, including, but not limited to, any data, privacy, or export control laws, or regulations promulgated by the U.S. Securities and Exchange Commission, any rules of any national or other securities exchange, including, without limitation, the New York Stock Exchange, the American Stock Exchange, or the NASDAQ, and any regulations having the force of law;"]
            [:li "use the Service to provide material support or resources (or to conceal or disguise the nature, location, source, or ownership of material support or resources) to any organization(s) designated by the United States government as a foreign terrorist organization pursuant to section 219 of the Immigration and Nationality Act;"]
            [:li "use the Service to engage in any unlawful or illegal activities; and/or"]
            [:li "collect or store personal data about other users in connection with any of the prohibited conduct and activities set forth above."]]]

        [:li "You acknowledge that OpenCompany and its designees shall have the right (but not the obligation) in their sole discretion to pre-screen, refuse, or remove any of Your Data that is available via the Service. For example, we may choose to review publically visible content (“"
          [:strong "Public Content"]
          "”) posted for compliance with our policies and guidelines. If, for instance, you upload files that do not belong to you and make these files available publicly, we can delete those files. We may also review Your Data transmitted through non-public mechanisms where we deem appropriate, including for violations of this TOS or in response to a user complaint. Without limiting the foregoing, OpenCompany and its designees shall have the right (but not the obligation) to remove any of Your Data that violates the TOS or is otherwise objectionable. You must evaluate, and bear all risks associated with, the use of Your Data, including any reliance on the accuracy, completeness, or usefulness of Your Data."]

        [:li "You acknowledge, consent and agree that OpenCompany may access, preserve and disclose your account information and Your Data if required to do so by law or in a good faith belief that such access preservation or disclosure is reasonably necessary to: (i) comply with legal process; (ii) enforce the TOS; (iii) respond to claims that any of Your Data violates the rights of third parties; (iv) respond to your requests for customer service; or (v) protect the rights, property or personal safety of OpenCompany, its users and the public."]

        [:li [:strong "Term; Termination."]
          [:ol
            [:li "This TOS will continue in full effect unless and until your account or this TOS is terminated as described herein. Service plans that are paid monthly will automatically renew for additional months, and Service plans that are paid annually will automatically renew for additional years. You have the right to deactivate your account at any time by using the account deactivation interface in company Settings."]
            [:li "We reserve the right to deactivate and delete your account (or the access privileges of any Member) and terminate this TOS at any time for any reason, or no reason, with or without notice. Without limiting the foregoing, OpenCompany may, in its sole discretion, publish policies whereby we delete your account for prolonged inactivity. Upon any termination of this TOS, we will have no obligation to maintain or provide Your Data. If your team’s account is deleted, we will delete or destroy all copies of Your Data in our possession or control, in a reasonably expedient way, unless legally prohibited."]
            [:li "Please see our Privacy Policy at "
              [:a {:href "https://opencompany.com/privacy"} "opencompany.com/privacy"]
              " for more information about the choices you have regarding Your Data."]
            [:li "All accrued rights to payment and the terms of Section 5 and Sections 8 through 20 shall survive termination of this TOS."]]]

        [:li [:strong "Disclaimer of Warranties."]
          [:ol
            [:li "The Services may be temporarily unavailable for scheduled maintenance or for unscheduled emergency maintenance, or because of other causes beyond our reasonable control, but OpenCompany shall use reasonable efforts to provide advance notice of any material scheduled service disruption. Further, you understand that Your Data may be transmitted or handled in an unencrypted manner if you choose to use unencrypted gateways to connect to the Service. Additionally, while OpenCompany takes steps to ensure that information provided to its third party vendors and hosting partners is transmitted using reasonable security measures, it does not guarantee that these transmissions will be encrypted. Accordingly, you acknowledge that you bear sole responsibility for adequate security, protection and backup of Your Data. OpenCompany will have no liability to you for any unauthorized access or use of any of Your Data, or any corruption, deletion, destruction or loss of any of Your Data."]
            [:li "THE SERVICE AND ALL RELATED COMPONENTS AND INFORMATION ARE PROVIDED ON AN “AS IS” AND “AS AVAILABLE” BASIS WITHOUT ANY WARRANTIES OF ANY KIND, AND OPENCOMPANY EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES, WHETHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, TITLE, FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT. YOU ACKNOWLEDGE THAT OpenCompany DOES NOT WARRANT THAT THE SERVICE WILL BE UNINTERRUPTED, TIMELY, SECURE, ERROR-FREE OR VIRUS-FREE, NOR DOES IT MAKE ANY WARRANTY AS TO THE RESULTS THAT MAY BE OBTAINED FROM USE OF THE SERVICES, AND NO INFORMATION, ADVICE OR SERVICES OBTAINED BY YOU FROM OpenCompany OR THROUGH THE SERVICE SHALL CREATE ANY WARRANTY NOT EXPRESSLY STATED IN THIS TOS."]]]

        [:li [:strong "Limitation of Liability."]
          [:ol
            [:li "UNDER NO CIRCUMSTANCES AND UNDER NO LEGAL THEORY (WHETHER IN CONTRACT, TORT, OR OTHERWISE) SHALL OPENCOMPANY BE LIABLE TO YOU OR ANY THIRD PARTY FOR (A) ANY INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, CONSEQUENTIAL OR PUNITIVE DAMAGES, INCLUDING LOST PROFITS, LOST SALES OR BUSINESS, LOST DATA, OR (B) FOR ANY AMOUNT IN THE AGGREGATE IN EXCESS OF THE FEES ACTUALLY PAID BY YOU IN THE SIX (6) MONTHS PRECEDING THE EVENT GIVING RISE TO YOUR CLAIM OR, IF NO FEES APPLY, FIVE HUNDRED ($500) U.S. DOLLARS, OR (C) ANY MATTER BEYOND OUR REASONABLE CONTROL. THE PROVISIONS OF THIS SECTION ALLOCATE THE RISKS UNDER THIS TOS BETWEEN THE PARTIES, AND THE PARTIES HAVE RELIED ON THESE LIMITATIONS IN DETERMINING WHETHER TO ENTER INTO THIS TOS."]
            [:li "Some jurisdictions do not allow the exclusion of implied warranties or limitation of liability for incidental or consequential damages, which means that some of the above limitations may not apply to you. IN THESE JURISDICTIONS, OPENCOMPANY’S LIABILITY WILL BE LIMITED TO THE GREATEST EXTENT PERMITTED BY LAW."]]]

        [:li [:strong "Dispute Resolution/Arbitration."]
          [:ol
            [:li [:strong "PLEASE READ THIS SECTION CAREFULLY – IT MAY SIGNIFICANTLY AFFECT YOUR LEGAL RIGHTS, INCLUDING YOUR RIGHT TO FILE A LAWSUIT IN COURT AND TO HAVE A JURY HEAR YOUR CLAIMS. "]
              "In order to expedite and control the cost of disputes, you and we agree that any legal or equitable claim arising out of or relating in any way to your use of the Services or these TOS, and the formation, validity, enforceability, scope, or applicability of this TOS, including this Section 10 (referred to as a “"
              [:strong "Claim"]
              "”) will be resolved as follows:"]
            [:li [:strong "Informal Resolution. "]
              "We will first try to resolve any Claim informally. Accordingly, neither of us may start a formal proceeding (except for Claims described in Section 10.4 below) for at least 30 days after one of us notifies the other of a Claim in writing. Notice of the Claim will include a brief written statement that sets forth the name, address, and contact information of the party giving it, the facts giving rise to the dispute, claim, or controversy and the relief requested. You will send your notice by email to "
              [:a {:href contact-mail-to} contact-email]
              "AND to the address listed directly below."
              [:div.container
                [:div.row
                  [:div.col-md-12 "OpenCompany LLC"]]
                [:div.row
                  [:div.col-md-12 "8 Lincoln Ln"]]
                [:div.row
                  [:div.col-md-12 "Cambridge, MA 02138"]]]]
            [:li [:strong "Formal Resolution. "]
              "Except as provided in Section 10.4, if we cannot resolve a Claim informally, any Claim either of us asserts will be resolved "
              [:strong "only by binding arbitration and not in courts of general jurisdiction. "]
              "The arbitration will be conducted under the rules of JAMS that are in effect at the time the arbitration is initiated (referred to as the “"
              [:strong "JAMS Rules"]
              "”) and under the rules set forth in this TOS. If there is a conflict between JAMS Rules and the rules set forth in this TOS, the rules set forth in this TOS will govern. "
              [:strong "ARBITRATION MEANS THAT YOU WAIVE YOUR RIGHT TO A JURY TRIAL. "]
              "You may, in arbitration, seek any and all remedies otherwise available to you pursuant to your state’s law."]
            [:li "(a) Human Users. If you are an individual user and you decide to initiate arbitration on your own behalf as a living person, we agree to reimburse your arbitration initiation fee, and any additional deposit required by JAMS to initiate your arbitration. We also agree to pay the costs of the arbitration proceeding. Other fees, such as attorney’s fees and expenses of travel to the arbitration, will be paid in accordance with JAMS Rules. The arbitration will be telephonic arbitration. To start an arbitration, you or we must do the following things:"
              [:p "(1) Write a Demand for Arbitration. The demand must include a description of the Claim and the amount of damages sought to be recovered. You can find a copy of a Demand for Arbitration at "
                [:a {:href "http://www.jamsadr.com/"} "www.jamsadr.com"]]
              [:p "(2) Send three copies of the Demand for Arbitration, plus the appropriate filing fee, to:"]
              [:div.container
                [:div.row
                  [:div.col-md-12 [:strong "JAMS"]]]
                [:div.row
                  [:div.col-md-12 [:strong "500 North State College Blvd., Suite 600"]]]
                [:div.row
                  [:div.col-md-12 [:strong "Orange, CA 92868"]]]
                [:div.row
                  [:div.col-md-12 [:strong "1-800-352-5267"]]]]
              [:p "(3) Send one copy of the demand for arbitration to us at the U.S. mailing address noted above."]]
            [:li "(b) Legal Entities. If you decide to initiate arbitration on behalf of the company or legal entity you represent, you will be required to pay the arbitration initiation fee as well as any additional deposit required by JAMS to initiate your arbitration. You also agree to pay the costs of the arbitration proceeding. Other fees, such as attorney’s fees and expenses of travel to the arbitration, will be paid in accordance with JAMS Rules. The arbitration will be telephonic arbitration. To start an arbitration, you or we must do the following things:"
              [:p "(1) Write a Demand for Arbitration. The demand must include a description of the Claim and the amount of damages sought to be recovered. You can find a copy of a Demand for Arbitration at "
                [:a {:href "http://www.jamsadr.com/"} "www.jamsadr.com"]]
              [:p "(2) Send three copies of the Demand for Arbitration, plus the appropriate filing fee, to:"]
              [:div.container
                [:div.row
                  [:div.col-md-12 [:strong "JAMS"]]]
                [:div.row
                  [:div.col-md-12 [:strong "500 North State College Blvd., Suite 600"]]]
                [:div.row
                  [:div.col-md-12 [:strong "Orange, CA 92868"]]]
                [:div.row
                  [:div.col-md-12 [:strong "1-800-352-5267"]]]]
              [:p "(3) Send one copy of the demand for arbitration to us at the U.S. mailing address noted above."]]
            [:li [:strong "Special Rules. "]
              "In the arbitration proceeding, the arbitrator has no authority to make errors of law, and any award may be challenged if the arbitrator does so. Otherwise, the arbitrator’s decision is final and binding on all parties and may be enforced in any federal or state court that has jurisdiction. "
              [:strong "Neither you nor we shall be entitled to join or consolidate claims in arbitration by or against other individuals or entities, or arbitrate any claim as a representative member of a class or in a private attorney general capacity."]
              "Accordingly, you and we agree that the JAMS Class Action Procedures do not apply to our arbitration. A court may sever any portion of Section 10 that it finds to be unenforceable, except for the prohibition on class, representative and private attorney general arbitration."]
            [:li [:strong "Exceptions. "]
              "Notwithstanding the foregoing, the notice and 30-day negotiation period required by this paragraph shall not apply, however, to disputes, claims, or controversies concerning patents, copyrights, moral rights, trademarks, and trade secrets and claims of piracy or unauthorized use of the Service, including disputes involving a violation of the Communications Act of 1934, 47 U.S.C. § 605, or the Digital Millennium Copyright Act, 17 U.S.C. § 1201, or the Electronic Communications Privacy Act, 18 U.S.C. §§ 2510-2521, or any other statement or law governing theft of service, may be decided only by a court of competent jurisdiction. You may also assert an individual action in small claims court in lieu of arbitration."]
            [:li [:strong "Member Right to Opt Out. "]
              "If you are a Member, you have the right to opt-out and not be bound by the binding arbitration requirement by sending written notice of your decision to opt-out to the Email address "
              [:a {:href contact-mail-to} contact-email]
              " AND by U.S. Mail to OpenCompany LLC, 8 Lincoln Ln, Cambridge, MA 02138. The notice must be sent within the later of 30 days of August 1, 2016 or your first use of the Services, whichever is later. If you opt-out of the binding arbitration requirement, OpenCompany also will not be bound by the requirement. Administrative Users may not opt-out of the binding arbitration requirements."]
            [:li [:strong "Changes to this Section. "]
              "OpenCompany will provide 30 days’ notice of any changes to this section. Changes will become effective on the 30th day, and will apply prospectively only to any claims arising after the 30th day."]]]

        [:li [:strong "STATUTE OF LIMITATIONS. "]
          "Regardless of any statute or law to the contrary or the applicable dispute resolution process, an informal complaint pertaining to any Claim or cause of action arising out of or related to use of the Service or under the TOS must be filed with OpenCompany within one (1) year after such Claim or cause of action arose or be forever barred. For Claims pursuant to the exceptions identified in Section 10.4, these Claims must be filed with the appropriate court within three (3) years after such claim or cause of action arose or be forever barred."]

        [:li [:strong "NOTICE AND PROCEDURE FOR MAKING CLAIMS OF COPYRIGHT OR INTELLECTUAL PROPERTY INFRINGEMENT."]
          "We respect the intellectual property of others, and we ask our users to do the same. We may, in appropriate circumstances and at its discretion, disable and/or terminate the accounts of users who may be repeat infringers. If you believe that your work has been copied in a way that constitutes copyright infringement, or your intellectual property rights have been otherwise violated, please provide OpenCompany's Copyright Agent the following information:"
          [:ol
            [:li "an electronic or physical signature of the person authorized to act on behalf of the owner of the copyright or other intellectual property interest;"]
            [:li "a description of the copyrighted work or other intellectual property that you claim has been infringed;"]
            [:li "a description of where the material that you claim is infringing is located on the site;"]
            [:li "your address, telephone number, and email address;"]
            [:li "a statement by you that you have a good faith belief that the disputed use is not authorized by the copyright owner, its agent, or the law;"]
            [:li "a statement by you, made under penalty of perjury, that the above information in your notice is accurate and that you are the copyright or intellectual property owner or authorized to act on the copyright or intellectual property owner's behalf."]]
          [:p "OpenCompany's Agent for Notice of claims of copyright or other intellectual property infringement can be reached as follows:"]
          [:div.container
            [:div.row
              [:div.col-md-12 "By mail:"]]
            [:div.row
              [:div.col-md-12 "Copyright Agent"]]
            [:div.row
              [:div.col-md-12 "c/o OpenCompany LLC"]]
            [:div.row
              [:div.col-md-12 "8 Lincoln Ln"]]
            [:div.row
              [:div.col-md-12 "Cambridge, MA 02138"]]]]

        [:li [:strong "Indemnification. "]
          "You shall defend, indemnify, and hold harmless OpenCompany from and against any claims, actions or demands, including without limitation reasonable legal and accounting fees, arising or resulting from your breach of this TOS, any of Your Data, or your (and your Members’) use or misuse of the Service. OpenCompany shall provide notice to you of any such claim, suit or demand. OpenCompany reserves the right to conduct the exclusive defense and control of any matter that is subject to indemnification under this section. In such case, you agree to cooperate with any reasonable requests assisting OpenCompany’s defense of such matters."]

        [:li [:strong "Enforceability. "]
          "If any provision of this TOS is found to be unenforceable or invalid, that provision will be limited or eliminated to the minimum extent necessary so that this TOS will otherwise remain in full force and effect and enforceable."]

        [:li [:strong "Integration, Modification, and Authority. "]
          "This TOS is the complete and exclusive statement of the mutual understanding of the parties and supersedes and cancels all previous written and oral agreements, communications and other understandings relating to the subject matter of this TOS. All waivers and modifications to this TOS must be in a writing signed by both parties that expressly by its terms modifies or waives a provision of this TOS, except as otherwise provided herein. No agency, partnership, joint venture, or employment is created as a result of this TOS and you do not have any authority of any kind to bind OpenCompany in any respect whatsoever."]

        [:li [:strong "Assignment. "]
          "You may not assign this TOS without the prior written consent of OpenCompany, except, if you are a company or other legal entity, you may assign this TOS in connection with a merger, re-organization or acquisition of all or a substantial portion of your assets by another company, but only upon 30-days prior notice to OpenCompany. OpenCompany may assign or transfer this TOS, in whole or in part, without restriction."]

        [:li [:strong "Notices. "]
          "Except as otherwise set forth herein, all notices under this TOS will be in writing and will be deemed to have been duly given when received, if personally delivered; when receipt is electronically confirmed, if transmitted by facsimile or email; the day after it is sent, if sent for next day delivery by recognized overnight delivery service; and upon receipt, if sent by certified or registered mail, return receipt requested."]

        [:li [:strong "Choice of Law and Forum."]
          "The TOS and the relationship between the parties shall be governed by the laws of the State of Massachusetts without regard to its conflict of law."]

        [:li [:strong "Waiver and Severability of Terms. "]
          "The failure of OpenCompany to exercise or enforce any right or provision of the TOS shall not constitute a waiver of such right or provision."]

        [:li [:strong "No Right of Survivorship and Non-Transferability."]
          "If you are a living person, you agree that your account is non-transferable and your rights to the content within your account terminate upon your death, however the content may thereafter be available and accessible by Administrative Users."]

        [:li [:strong "Government Users. "]
          "Nothing herein makes OpenCompany a government contractor for any federal, state, local, or foreign government."]]]]])