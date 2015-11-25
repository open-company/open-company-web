(ns open-company-web.data.company)

(def company {
  :name "Buffer"
  :currency "USD"
  :slug "buffer"
  
  :categories ["progress", "company"]
  :sections {:progress ["finances" "challenges" "growth"] :company []}
  
  :challenges {
    :title "Key Challenges"
    :body "<h3>Recruiting</h3><p>We're continuing our fast pace of growing the team, and we'd love your help to spread the word about all our <a>current open positions</a>, <a href=\"http://twitter.com\">Twitter</a> or <a href=\"http://buffer.com\">Buffer</a>.</p>
<h3>Spead & Reliability</h3><p>We've made some key speed and reliability <a href=\"/improvements\">improvements</a> to <a href=\"http://buffer.com/b2b\">Buffer for business</a>, but there is still a ways to go. Everyone please drop everything and support the devops team in anyway you can when we have an outage situation.</p>
<h3>Onbarding</h3><p>We're in the <a href=\"/early-stages\">early stages</a> of experimenting with a progress indicator in the onboarding improvement ideas to <a href=\"/gretchen\">Gretchen</a>."
    :updated-at "2015-09-14T20:49:19Z"
    :author {
      :name "Stuart Levinson"
      :user-id "U06SQLDFT"
      :image "https://avatars.slack-edge.com/2015-10-16/12647678369_79b4fbf15439d29d5457_192.jpg"
    }
  }
  
  :growth {
    :title "Key metrics"
    :metrics [
      {
        :slug "test-metric"
        :name "Test metric"
        :interval "monthly"
        :unit "USD"
        :target "high"
      }]
    :data [
      {
        :period "2015-10"
        :slug "test-metric"
        :value 12345
      }
      {
        :period "2015-11"
        :slug "test-metric"
        :value 13456
      }]
    :author {
      :user-id "123456"
      :image "http://www.emoticonswallpapers.com/avatar/cartoons/Wiley-Coyote-Dazed.jpg"
      :name "Wile E. Coyote"
    }
    :notes {
      :body "This is what I have to say about that."
      :updated-at "2015-09-14T20:49:19Z"
      :author {
        :user-id "123456"
        :image "http://www.emoticonswallpapers.com/avatar/cartoons/Wiley-Coyote-Dazed.jpg"
        :name "Wile E. Coyote"
      }
      :links [
        {
          :rel "self"
          :method "GET"
          :href "/companies/buffer/growth/notes"
          :type "application/vnd.open-company.notes.v1+json"
        }
        {
          :rel "update"
          :method "POST"
          :href "/companies/buffer/growth/notes"
          :type "application/vnd.open-company.notes.v1+json"
        }
      ]
    }
    :updated-at "2015-09-14T20:49:19Z"
    :links [
      {
        :rel "self"
        :method "GET"
        :href "/companies/buffer/growth"
        :type "application/vnd.open-company.section.v1+json"
      }
      {
        :rel "update"
        :method "POST"
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
  }

  :finances {
    :data [
      {
        :period "2015-08"
        :cash 1209133
        :revenue 977
        :costs 27155
      }
      {
        :period "2015-07"
        :cash 1235311
        :revenue 512
        :costs 26412
      }
      {
        :period "2015-06"
        :cash 1261376
        :revenue 286
        :costs 26577
      }
      {
        :period "2015-05"
        :cash 1287667
        :revenue 0
        :costs 44960
      }
      {
        :period "2015-04"
        :cash 82627
        :revenue 0
        :costs 27861
      }
      {
        :period "2015-03"
        :cash 109746
        :revenue 0
        :costs 27119
      }
      {
        :period "2015-02"
        :cash 136865
        :revenue 0
        :costs 22345
      }
      {
        :period "2015-01"
        :cash 159210
        :revenue 0
        :costs 20633
      }
      {
        :period "2014-12"
        :cash 179843
        :revenue 0
        :costs 38762
      }
      {
        :period "2014-11"
        :cash 218605
        :revenue 0
        :costs 18814
      }
      {
        :period "2014-10"
        :cash 237419
        :revenue 0
        :costs 19546
      }      
    ]
    :title "Finances report"
    :updated-at "2015-09-17T12:29:30Z"
    :author {
      :name "Iacopo Carraro"
      :user-id "U06STCKLN"
      :image "https://secure.gravatar.com/avatar/98b5456ea1c562024f41501ffd7bc3c6.jpg?s=192&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0022.png"
    }
    :notes {
      :body "<h3>Title of notes</h3><p>This is the body of notes with an external <a href=\"http://www.google.it\">link to google</a>.</p>"
      :updated-at "2015-09-14T20:49:19Z"
      :author {
        :name "Stuart Levinson"
        :user-id "U06SQLDFT"
        :image "https://avatars.slack-edge.com/2015-10-16/12647678369_79b4fbf15439d29d5457_192.jpg"
      }
      :links [
        {
          :rel "self"
          :method "GET"
          :href "/companies/buffer/finances/notes"
          :type "application/vnd.open-company.notes.v1+json"
        }
        {
          :rel "update"
          :method "POST"
          :href "/companies/buffer/finances/notes"
          :type "application/vnd.open-company.notes.v1+json"
        }
      ]
    }
    :links [
      {
        :rel "self"
        :method "GET"
        :href "/companies/buffer/finances"
        :type "application/vnd.open-company.section.v1+json"
      }
      {
        :rel "update"
        :method "POST"
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
  }
  
  :revisions [
    {
      :rel "revision"
      :method "GET"
      :href "/companies/buffer?as-of=2015-09-11T22:14:24Z"
      :type "application/vnd.open-company.v1+json"
      :updated-at "2015-09-11T22:14:24Z"
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
      :updated-at "2015-09-10T22:14:24Z"
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
      :updated-at "2015-09-09T22:14:24Z"
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
      :updated-at "2015-09-08T22:14:24Z"
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
      :updated-at "2015-09-07T22:14:24Z"
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