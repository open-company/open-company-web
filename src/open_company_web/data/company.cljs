(ns open-company-web.data.company)

(def company {
  :name "Buffer"
  :currency "USD"
  :slug "buffer"

  :categories ["progress", "company"]
  :sections {:progress ["finances" "challenges" "growth"] :company ["values"]}

  :challenges {
    :title "Key Challenges"
    :icon "tactic"
    :body "<h3>Recruiting</h3><p>We're continuing our fast pace of growing the team, and we'd love your help to spread the word about all our <a>current open positions</a>, <a href=\"http://twitter.com\">Twitter</a> or <a href=\"http://buffer.com\">Buffer</a>.</p>
<h3>Spead & Reliability</h3><p>We've made some key speed and reliability <a href=\"/improvements\">improvements</a> to <a href=\"http://buffer.com/b2b\">Buffer for business</a>, but there is still a ways to go. Everyone please drop everything and support the devops team in anyway you can when we have an outage situation.</p>
<h3>Onbarding</h3><p>We're in the <a href=\"/early-stages\">early stages</a> of experimenting with a progress indicator in the onboarding improvement ideas to <a href=\"/gretchen\">Gretchen</a>."
    :updated-at "2015-09-14T20:49:19.000Z"
    :author {
      :name "Stuart Levinson"
      :user-id "U06SQLDFT"
      :image "https://avatars.slack-edge.com/2015-10-16/12647678369_79b4fbf15439d29d5457_192.jpg"
    }
  }

  :values {
    :updated-at "2015-11-30T18:17:55.073Z"
    :title "Values"
    :icon "scale"
    :author {
      :image "https://secure.gravatar.com/avatar/98b5456ea1c562024f41501ffd7bc3c6.jpg?s=192&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F7fa9%2Fimg%2Favatars%2Fava_0022-192.png"
      :name "Iacopo Carraro"
      :user-id "123456"
    }
    :revisions [
      {
        :href "/companies/buffer/values?as-of=2015-11-30T18:17:55.073Z"
        :method "GET"
        :rel "revision"
        :type "application/vnd.open-company.section.v1+json"
        :updated-at "2015-11-30T18:17:55.073Z"
      }
      {
        :href "/companies/buffer/values?as-of=2015-06-08T12:35:19.000Z"
        :method "GET"
        :rel "revision"
        :type "application/vnd.open-company.section.v1+json"
        :updated-at "2015-06-08T12:35:19.000Z"
      }
    ]
    :body "<h3>1. Choose positivity</h3> <ul> <li>You strive to approach things in a positive and optimistic way</li> <li>You avoid criticizing or condemning team members or users</li> <li>You avoid complaining</li> <li>You let the other person save face, even if they are clearly wrong</li> <li>You are deliberate about giving genuine appreciation</li> </ul> <h3>2. Default to transparency</h3> <ul> <li>You take pride in opportunities to share our beliefs, failures, strengths and decisions</li> <li>You use transparency as a tool to help others</li> <li>You always state your thoughts immediately and with honesty</li> <li>You share early in the decision process, to avoid \"big revelations\"</li> </ul> <h3>3. Focus on self-improvement</h3> <ul> <li>You are conscious of your current level of productivity and happiness, and make continual change to grow</li> <li>You have a higher expectation of yourself, than Buffer does of you</li> <li>You regularly and deliberately do things that make you feel uncomfortable</li> <li>You practice activities and develop habits that improve your mind and your body</li> </ul> <h3>4. Be a \"no-ego\" Doer</h3> <ul> <li>You don't attach your personal self to ideas</li> <li>You let others have your best ideas</li> <li>You approach new ideas thinking \"what can we do right now?\"</li> <li>You are humble</li> <li>You always ship code the moment it is better than what is live on our site - no matter what</li> </ul> <h3>5. Listen First, Then Listen More</h3> <ul> <li>You seek first to understand, then to be understood</li> <li>You focus on listening rather than responding</li> <li>You take the approach that everything is a hypothesis and you could be wrong</li> <li>You are suggestive rather than instructive, replacing phrases such as 'certainly','undoubtedly', etc with 'perhaps', 'I think', 'my intuition right now'</li> </ul> <h3>6. Have a Bias Towards Clarity</h3> <ul> <li>You talk, code, design and write in a clear way instead of being clever</li> <li>You over-communicate, repeating things more times than you would intuitively</li> <li>You use more words to explain, even if it feels obvious already</li> <li>You don't make assumptions, you instead ask that extra question to have the full picture</li> </ul> <h3>7. Make Time to Reflect</h3> <ul> <li>You deliberately find time for reflection, because that's where your life-changing adjustments come from</li> <li>You have a calm approach to discussions and ponder points in your own time</li> <li>You find time to jump out of the trenches into the higher level thinking that will move the needle</li> <li>You understand the value of patience and treat it as a muscle which needs practice to grow</li> </ul> <h3>8. Live Smarter, Not Harder</h3> <ul> <li>You value waking up fresh over working that extra hour</li> <li>You always aim to be fully engaged in an activity, or resting</li> <li>You single task your way through the day</li> <li>You are at the top of your game, as you focus on expanding capacity of your mental, physical, emotional and spiritual energy</li> <li>You choose to be at the single place on Earth where you are the happiest, and most productive, and you are not afraid to find out where that is</li> </ul> <h3>9. Show Gratitude</h3> <ul> <li>You regularly stop and are grateful for your circumstances</li> <li>You are grateful for the work co-workers do to push the company forward and help you move faster</li> <li>You approach customer conversations with humility and the knowledge that it's a privilege to serve these people</li> <li>You have gratitude for platforms, tools and open source that laid the foundation for the possibility of the company: \"If I have seen further it is by standing on the shoulders of giants\" - Isaac Newton</li> </ul> <h3>10. Do the Right Thing</h3> <ul> <li>You choose what's best for customers and the company in the long-term</li> <li>You correct the mistake even when no one would notice</li> <li>You strive to provide the best solution, even if that means foregoing profit</li> <li>You get excited about opportunities to help others</li> </ul>"
    :links [
      {
        :rel "self"
        :method "GET"
        :href "/companies/buffer/values"
        :type "application/vnd.open-company.section.v1+json"
      }
      {
        :rel "update"
        :method "PUT"
        :href "/companies/buffer/values"
        :type "application/vnd.open-company.section.v1+json"
      }
      {
        :rel "partial-update"
        :method "PATCH"
        :href "/companies/buffer/values"
        :type "application/vnd.open-company.section.v1+json"
      }
    ]
  }

  :growth {
    :updated-at "2015-10-23T11:21:39.000Z"
    :title "Key metrics"
    :icon "chart-growth"
    :author {
      :image "https://secure.gravatar.com/avatar/46c1c756f36549c2dea0253e1e025053?s=96&d=mm&r=g"
      :name "Joel Gascoigne"
      :user-id "123456"
    },
    :notes {
      :author {
        :image "https://secure.gravatar.com/avatar/46c1c756f36549c2dea0253e1e025053?s=96&d=mm&r=g"
        :name "Joel Gascoigne"
        :user-id "123456"
      }
      :body "<p>We usually find that September is one of the months of lowest growth. We are seeing this here with 3.2%, which
          is lower than our recent months, however it's a number we're happy about for generally our lowest growth month of the year.</p>
          <p>Our current YoY MRR growth stands at around 80%, a number we'd like to grow.</p>"
      :updated-at "2015-10-23T11:21:39.000Z"
    }
    :revisions [
      {
        :rel "revision"
        :method "GET"
        :href "/companies/buffer/growth?as-of=2015-10-23T11:21:39.000Z"
        :type "application/vnd.open-company.section.v1+json"
        :updated-at "2015-10-23T11:21:39.000Z"
      }
      {
        :rel "revision"
        :method "GET"
        :href "/companies/buffer/growth?as-of=2015-09-18T18:33:42.000Z"
        :type "application/vnd.open-company.section.v1+json"
        :updated-at "2015-09-18T18:33:42.000Z"
      }
      ; {
      ;   :rel "revision"
      ;   :method "GET"
      ;   :href "/companies/buffer/growth?as-of=2015-08-13T16:26:42.000Z"
      ;   :type "application/vnd.open-company.section.v1+json"
      ;   :updated-at "2015-08-13T16:26:42.000Z"
      ; }
      ; {
      ;   :rel "revision"
      ;   :method "GET"
      ;   :href "/companies/buffer/growth?as-of=2015-07-13T12:59:29.000Z"
      ;   :type "application/vnd.open-company.section.v1+json"
      ;   :updated-at "2015-07-13T12:59:29.000Z"
      ; }
    ]
    :created-at "2015-10-23T11:21:39.000Z"
    :metrics [
      {
        :interval "monthly"
        :name "Total registered users"
        :slug "total-registered-users"
        :target "high"
        :unit "users"
      }
      {
        :interval "monthly"
        :name "Monthly active users"
        :slug "mau"
        :target "high"
        :unit "users"
      }
      {
        :interval "monthly"
        :name "Average daily active users"
        :slug "dau"
        :target "high"
        :unit "users"
      }
      {
        :interval "monthly"
        :name "Annual recurring revenue"
        :slug "arr"
        :target "high"
        :unit "USD"
      }
    ]
    :links [
      {
        :rel "self"
        :method "GET"
        :href "/companies/buffer/growth"
        :type "application/vnd.open-company.section.v1+json"
      }
      {
        :rel "update"
        :method "PUT"
        :href "/companies/buffer/growth"
        :type "application/vnd.open-company.section.v1+json"
      }
      {
        :rel "partial-update"
        :method "PATCH"
        :href "/companies/buffer/growth"
        :type "application/vnd.open-company.section.v1+json"
      }
    ],
    :data [
      {
        :period "2015-06"
        :slug "total-registered-users"
        :value 2511292
      }
      {
        :period "2015-07"
        :slug "total-registered-users"
        :value 2592194
      }
      {
        :period "2015-08"
        :slug "total-registered-users"
        :value 2673418
      }
      {
        :period "2015-09"
        :slug "total-registered-users"
        :value 2757998
      }
      {
        :period "2015-06"
        :slug "mau"
        :value 206464
      }
      {
        :period "2015-07"
        :slug "mau"
        :value 208667
      }
      {
        :period "2015-08"
        :slug "mau"
        :value 211520
      }
      {
        :period "2015-09"
        :slug "mau"
        :value 219744
      }
      {
        :period "2015-06"
        :slug "dau"
        :value 52229
      }
      {
        :period "2015-07"
        :slug "dau"
        :value 51491
      }
      {
        :period "2015-08"
        :slug "dau"
        :value 50489
      }
      {
        :period "2015-09"
        :slug "dau"
        :value 54357
      }
      {
        :period "2015-06"
        :slug "arr"
        :value 6610000
      }
      {
        :period "2015-07"
        :slug "arr"
        :value 6860000
      }
      {
        :period "2015-08"
        :slug "arr"
        :value 7490000
      }
      {
        :period "2015-09"
        :slug "arr"
        :value 7730000
      }
    ]
  }

  :finances {
    :updated-at "2015-10-23T11:21:40.000Z"
    :title "Finances"
    :icon "money-bag"
    :author {
      :image "https://secure.gravatar.com/avatar/46c1c756f36549c2dea0253e1e025053?s=96&d=mm&r=g"
      :name "Joel Gascoigne"
      :user-id "123456"
    }
    :revisions [
      {
        :rel "revision"
        :method "GET"
        :href "/companies/buffer/finances?as-of=2015-10-23T11:21:40.000Z"
        :type "application/vnd.open-company.section.v1+json"
        :updated-at "2015-10-23T11:21:40.000Z"
      }
      {
        :rel "revision"
        :method "GET"
        :href "/companies/buffer/finances?as-of=2015-09-18T18:33:41.000Z"
        :type "application/vnd.open-company.section.v1+json"
        :updated-at "2015-09-18T18:33:41.000Z"
      }
      ; {
      ;   :rel "revision"
      ;   :method "GET"
      ;   :href "/companies/buffer/finances?as-of=2015-08-13T16:26:41.000Z"
      ;   :type "application/vnd.open-company.section.v1+json"
      ;   :updated-at "2015-08-13T16:26:41.000Z"
      ; }
      ; {
      ;   :rel "revision"
      ;   :method "GET"
      ;   :href "/companies/buffer/finances?as-of=2015-07-13T12:59:28.000Z"
      ;   :type "application/vnd.open-company.section.v1+json"
      ;   :updated-at "2015-07-13T12:59:28.000Z"
      ; }
    ]
    :created-at "2015-10-23T11:21:40.000Z"
    :links [
      {
        :rel "self"
        :method "GET"
        :href "/companies/buffer/finances"
        :type "application/vnd.open-company.section.v1+json"
      }
      {
        :rel "update"
        :method "PUT"
        :href "/companies/buffer/finances"
        :type "application/vnd.open-company.section.v1+json"
      }
      {
        :rel "partial-update"
        :method "PATCH"
        :href "/companies/buffer/finances"
        :type "application/vnd.open-company.section.v1+json"
      }
    ]
    :data [
      {
        :cash 2578881
        :costs 238715
        :period "2015-06"
        :revenue 550529
      }
      {
        :cash 2655505,
        :costs 494907,
        :period "2015-07",
        :revenue 571532
      }
      {
        :cash 2789388,
        :costs 490313,
        :period "2015-08",
        :revenue 624196
      }
      {
        :cash 2757998,
        :costs 675382,
        :period "2015-09",
        :revenue 643992
      }
    ]
  }
  
  :revisions [
    {
      :rel "revision"
      :method "GET"
      :href "/companies/buffer?as-of=2015-09-11T22:14:24Z"
      :type "application/vnd.open-company.v1+json"
      :updated-at "2015-09-11T22:14:24.000Z"
      :author {
        :name "Iacopo Carraro"
        :user-id "U06STCKLN"
        :image "https://secure.gravatar.com/avatar/98b5456ea1c562024f41501ffd7bc3c6.jpg?s=192&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0022.png"
      }
    }
    {
      :rel "revision"
      :method "GET"
      :href "/companies/buffer?as-of=2015-09-10T22:14:24Z"
      :type "application/vnd.open-company.v1+json"
      :updated-at "2015-09-10T22:14:24.000Z"
      :author {
        :name "Stuart Levinson"
        :user-id "U06SQLDFT"
        :image "https://avatars.slack-edge.com/2015-10-16/12647678369_79b4fbf15439d29d5457_192.jpg"
      }
    }
    {
      :rel "revision"
      :method "GET"
      :href "/companies/buffer?as-of=2015-09-09T22:14:24Z"
      :type "application/vnd.open-company.v1+json"
      :updated-at "2015-09-09T22:14:24.000Z"
      :author {
        :name "Stuart Levinson"
        :user-id "U06SQLDFT"
        :image "https://avatars.slack-edge.com/2015-10-16/12647678369_79b4fbf15439d29d5457_192.jpg"
      }
    }
    {
      :rel "revision"
      :method "GET"
      :href "/companies/buffer?as-of=2015-09-08T22:14:24Z"
      :type "application/vnd.open-company.v1+json"
      :updated-at "2015-09-08T22:14:24.000Z"
      :author {
        :name "Stuart Levinson"
        :user-id "U06SQLDFT"
        :image "https://avatars.slack-edge.com/2015-10-16/12647678369_79b4fbf15439d29d5457_192.jpg"
      }
    }
    {
      :rel "revision"
      :method "GET"
      :href "/companies/buffer?as-of=2015-09-07T22:14:24Z"
      :type "application/vnd.open-company.v1+json"
      :updated-at "2015-09-07T22:14:24.000Z"
      :author {
        :name "Stuart Levinson"
        :user-id "U06SQLDFT"
        :image "https://avatars.slack-edge.com/2015-10-16/12647678369_79b4fbf15439d29d5457_192.jpg"
      }
    }
  ]

  :links [
    {
      :rel "self"
      :method "GET"
      :href "/companies/buffer"
      :type "application/vnd.open-company.v1+json"
    }
    {
      :rel "update"
      :method "PUT"
      :href "/companies/buffer"
      :type "application/vnd.open-company.v1+json"
    }
    {
      :rel "partial-update"
      :method "PATCH"
      :href "/companies/buffer"
      :type "application/vnd.open-company.v1+json"
    }
    {
      :rel "delete"
      :method "DELETE"
      :href "/companies/buffer"
    }
  ]
})



(def revisions {
  :buffer {
    :values {
      "2015-06-08T12:35:19.000Z" {
        :updated-at "2015-06-08T12:35:19.000Z"
        :title "Values"
        :author {
          :image "https://secure.gravatar.com/avatar/46c1c756f36549c2dea0253e1e025053?s=96&d=mm&r=g"
          :name "Joel Gascoigne"
          :user-id "123456"
        }
        :revisions [
          {
            :href "/companies/buffer/values?as-of=2015-11-30T18:17:55.073Z"
            :method "GET"
            :rel "revision"
            :type "application/vnd.open-company.section.v1+json"
            :updated-at "2015-11-30T18:17:55.073Z"
          }
          {
            :href "/companies/buffer/values?as-of=2015-06-08T12:35:19.000Z"
            :method "GET"
            :rel "revision"
            :type "application/vnd.open-company.section.v1+json"
            :updated-at "2015-06-08T12:35:19.000Z"
          }
        ]
        :body "<h3>1. Choose positivity</h3>
            <ul>
              <li>You strive to approach things in a positive and optimistic way</li>
              <li>You avoid criticizing or condemning team members or users</li>
              <li>You avoid complaining</li>
              <li>You let the other person save face, even if they are clearly wrong</li>
              <li>You are deliberate about giving genuine appreciation</li>
            </ul>
            <h3>2. Default to transparency</h3>
            <ul>
              <li>You take pride in opportunities to share our beliefs, failures, strengths and decisions</li>
              <li>You use transparency as a tool to help others</li>
              <li>You always state your thoughts immediately and with honesty</li>
              <li>You share early in the decision process, to avoid \"big revelations\"</li>
            </ul>
            <h3>3. Focus on self-improvement</h3>
            <ul>
              <li>You are conscious of your current level of productivity and happiness, and make continual change to grow</li>
              <li>You have a higher expectation of yourself, than Buffer does of you</li>
              <li>You regularly and deliberately do things that make you feel uncomfortable</li>
              <li>You practice activities and develop habits that improve your mind and your body</li>
            </ul>
            <h3>4. Be a \"no-ego\" Doer</h3>
            <ul>
              <li>You don't attach your personal self to ideas</li>
              <li>You let others have your best ideas</li>
              <li>You approach new ideas thinking \"what can we do right now?\"</li>
              <li>You are humble</li>
              <li>You always ship code the moment it is better than what is live on our site - no matter what</li>
            </ul>"
        :created-at "2015-06-08T12:35:19.000Z"
        :links [
          {
            :rel "self"
            :method "GET"
            :href "/companies/buffer/values"
            :type "application/vnd.open-company.section.v1+json"
          }
          {
            :rel "update"
            :method "PUT"
            :href "/companies/buffer/values"
            :type "application/vnd.open-company.section.v1+json"
          }
          {
            :rel "partial-update"
            :method "PATCH"
            :href "/companies/buffer/values"
            :type "application/vnd.open-company.section.v1+json"
          }
        ]
      }
    }
    :finances {
      "2015-09-18T18:33:41.000Z" {
        :updated-at "2015-09-18T18:33:41.000Z"
        :title "Finances"
        :author {
          :image "https://secure.gravatar.com/avatar/46c1c756f36549c2dea0253e1e025053?s=96&d=mm&r=g"
          :name "Joel Gascoigne"
          :user-id "123456"
        }
        :notes {
          :author {
            :image "https://secure.gravatar.com/avatar/46c1c756f36549c2dea0253e1e025053?s=96&d=mm&r=g"
            :name "Joel Gascoigne"
            :user-id "123456"
          }
          :body "<p>We're delighted to hit almost $7.5M in annual recurring revenue and be fast moving
            towards the big $10M ARR milestone.</p>
            <p>As a company, we're starting to feel that some of our recent team growth and structure adjustments
            in the last few months are starting to take effect.</p>"
          :updated-at "2015-09-18T18:33:41.000Z"
        }
        :revisions [
          {
            :rel "revision"
            :method "GET"
            :href "/companies/buffer/finances?as-of=2015-10-23T11:21:40.000Z"
            :type "application/vnd.open-company.section.v1+json"
            :updated-at "2015-10-23T11:21:40.000Z"
          }
          {
            :rel "revision"
            :method "GET"
            :href "/companies/buffer/finances?as-of=2015-09-18T18:33:41.000Z"
            :type "application/vnd.open-company.section.v1+json"
            :updated-at "2015-09-18T18:33:41.000Z"
          }
          ; {
          ;   :rel "revision"
          ;   :method "GET"
          ;   :href "/companies/buffer/finances?as-of=2015-08-13T16:26:41.000Z"
          ;   :type "application/vnd.open-company.section.v1+json"
          ;   :updated-at "2015-08-13T16:26:41.000Z"
          ; }
          ; {
          ;   :rel "revision"
          ;   :method "GET"
          ;   :href "/companies/buffer/finances?as-of=2015-07-13T12:59:28.000Z"
          ;   :type "application/vnd.open-company.section.v1+json"
          ;   :updated-at "2015-07-13T12:59:28.000Z"
          ; }
        ]
        :created-at "2015-09-18T18:33:41.000Z"
        :links [
          {
            :rel "self"
            :method "GET"
            :href "/companies/buffer/finances"
            :type "application/vnd.open-company.section.v1+json"
          }
          {
            :rel "update"
            :method "PUT"
            :href "/companies/buffer/finances"
            :type "application/vnd.open-company.section.v1+json"
          }
          {
            :rel "partial-update"
            :method "PATCH"
            :href "/companies/buffer/finances"
            :type "application/vnd.open-company.section.v1+json"
          }
        ]
        :data [
          {
            :cash 2578881
            :costs 238715
            :period "2015-06"
            :revenue 550529
          }
          {
            :cash 2655505
            :costs 494907
            :period "2015-07"
            :revenue 571532
          }
          {
            :cash 2789388
            :costs 490313
            :period "2015-08"
            :revenue 624196
          }
        ]
      }
    }
    :growth {
      "2015-09-18T18:33:42.000Z" {
        :updated-at "2015-09-18T18:33:42.000Z"
        :title "Key metrics"
        :author {
          :image "https://secure.gravatar.com/avatar/46c1c756f36549c2dea0253e1e025053?s=96&d=mm&r=g"
          :name "Joel Gascoigne"
          :user-id "123456"
        }
        :notes {
          :author {
            :image "https://secure.gravatar.com/avatar/46c1c756f36549c2dea0253e1e025053?s=96&d=mm&r=g"
            :name "Joel Gascoigne"
            :user-id "123456"
          }
          :body "<p>As a company, we're starting to feel that some of our recent team growth
            and structure adjustments in the last few months are starting to take effect.</p>"
          :updated-at "2015-09-18T18:33:42.000Z"
        }
        :revisions [
          {
            :rel "revision"
            :method "GET"
            :href "/companies/buffer/growth?as-of=2015-10-23T11:21:39.000Z"
            :type "application/vnd.open-company.section.v1+json"
            :updated-at "2015-10-23T11:21:39.000Z"
          }
          {
            :rel "revision"
            :method "GET"
            :href "/companies/buffer/growth?as-of=2015-09-18T18:33:42.000Z"
            :type "application/vnd.open-company.section.v1+json"
            :updated-at "2015-09-18T18:33:42.000Z"
          }
          ; {
          ;   :rel "revision"
          ;   :method "GET"
          ;   :href "/companies/buffer/growth?as-of=2015-08-13T16:26:42.000Z"
          ;   :type "application/vnd.open-company.section.v1+json"
          ;   :updated-at "2015-08-13T16:26:42.000Z"
          ; }
          ; {
          ;   :rel "revision"
          ;   :method "GET"
          ;   :href "/companies/buffer/growth?as-of=2015-07-13T12:59:29.000Z"
          ;   :type "application/vnd.open-company.section.v1+json"
          ;   :updated-at "2015-07-13T12:59:29.000Z"
          ; }
        ]
        :created-at "2015-09-18T18:33:42.000Z"
        :metrics [
          {
            :interval "monthly"
            :name "Total registered users"
            :slug "total-registered-users"
            :target "high"
            :unit "users"
          }
          {
            :interval "monthly"
            :name "Monthly active users"
            :slug "mau"
            :target "high"
            :unit "users"
          }
          {
            :interval "monthly"
            :name "Average daily active users"
            :slug "dau"
            :target "high"
            :unit "users"
          }
          {
            :interval "monthly"
            :name "Annual recurring revenue"
            :slug "arr"
            :target "high"
            :unit "USD"
          }
        ]
        :links [
          {
            :rel "self"
            :method "GET"
            :href "/companies/buffer/growth"
            :type "application/vnd.open-company.section.v1+json"
          }
          {
            :rel "update"
            :method "PUT"
            :href "/companies/buffer/growth"
            :type "application/vnd.open-company.section.v1+json"
          }
          {
            :rel "partial-update"
            :method "PATCH"
            :href "/companies/buffer/growth"
            :type "application/vnd.open-company.section.v1+json"
          }
        ]
        :data [
          {
            :period "2015-06"
            :slug "total-registered-users"
            :value 2511292
          }
          {
            :period "2015-07"
            :slug "total-registered-users"
            :value 2592194
          }
          {
            :period "2015-08"
            :slug "total-registered-users"
            :value 2673418
          }
          {
            :period "2015-06"
            :slug "mau"
            :value 206464
          }
          {
            :period "2015-07"
            :slug "mau"
            :value 208667
          }
          {
            :period "2015-08"
            :slug "mau"
            :value 211520
          }
          {
            :period "2015-06"
            :slug "dau"
            :value 52229
          }
          {
            :period "2015-07"
            :slug "dau"
            :value 51491
          }
          {
            :period "2015-08"
            :slug "dau"
            :value 50489
          }
          {
            :period "2015-06"
            :slug "arr"
            :value 6610000
          }
          {
            :period "2015-07"
            :slug "arr"
            :value 6860000
          }
          {
            :period "2015-08"
            :slug "arr"
            :value 7490000
          }
        ]
      }
    }
  }
})