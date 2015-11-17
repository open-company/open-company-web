(ns open-company-web.data.new-section
  (:require [clojure.walk :refer (keywordize-keys stringify-keys)]))

(def new-section-plain {
  "categories" [

    {
      "name" "progress",
      "title" "Progress",
      "sections" [

        {
          "name" "update",
          "title" "Founder's Update",
          "description" "Company update",
          "body" "<p><em>Provide an overview of what's happened since the last update.</em></p>
          <p>Overall it's been a great week, we had our first customer advisor board meeting on Tuesday. We did
          miss our new customer signup target, mainly with cheese shops, so we are retrenching our target markets a
          bit.</p>
          <ul>
            <li>We made a shift toward adding more structure and making roles and commitments explicit,
            everyone got a title</li>
            <li>We have a company-wide push on customer validation focusing on produce and seafood vendors</li>
            <li>We're all meeting up for a all-hands hackathon the first week of next month in Tampa</li>
          </ul>
          <p>Please do everything you can to support Tim this week as he is taking the lead on customer validation.</p>",
          "core" true
        },

        {
          "name" "highlights",
          "title" "Highlights",
          "description" "Company milestones or accomplishments",
          "body" "<p><em>What new company milestones or accomplishments are you most proud of? List up to three.</em></p>
          <p>We've grown as a team recently, both literally and figuratively.</p>
          <ul>
            <li>Made 2 new hires for the engineering team. Welcome John and Erica!</li>
            <li>We made a shift toward adding more structure and making roles and commitments explicit,
            everyone got a title</li>
          </ul>",
          "core" false
        },

        {
          "name" "challenges",
          "title" "Key Challenges",
          "description" "Challenges facing the company",
          "body" "<p><em>What are you most disappointed about? Mhat’s something you recently learned
          and your plan to fix it going forward? What is the company stuck on? What are systemic or existential
          threats everyone needs to be focused on?</em></p>
          <ul>
            <li>We churned 4 new customers last month due to the payment bug. In response the engineering team
            has implemented a new deployment checklist process.</li>
            <li>Our content marketing effort has been slow to gain traction. We need everyone to really stick
            to their content creation goals to help us execute on our plan to create buzz.</li>
            <li>We're still looking to validate our market focus on fruit and seafood vendors. This is our
            3rd attempt at focusing our market.</li>
          </ul>",
          "core" true
        },

        {
          "name" "growth",
          "title" "Growth",
          "description" "Growth metrics and key performance indicators",
          "prompt" "Pick the most important growth metric for the stakeholders of your company. You'll be able
          to add more later.",
          "metrics" [
            {
              "slug" "dau",
              "name" "Daily active users"
            },
            {
              "slug" "mau",
              "name" "Monthly active users"
            },
            {
              "slug" "wau",
              "name" "Weekly active users"
            },
            {
              "slug" "mrr",
              "name" "Monthly recurring revenue"
            },
            {
              "slug" "arr",
              "name" "Annual recurring revenue"
            },
            {
              "slug" "subscribers",
              "name" "Subscribers"
            },
            {
              "slug" "subscribers",
              "name" "Registrations"
            },
            {
              "slug" "followers",
              "name" "Followers"
            },
            {
              "slug" "views",
              "name" "Views"
            }
          ],
          "intervals" [
            "weekly",
            "monthly",
            "quarterly"
          ],
          "units" [
            "users",
            "currency",
            "registrations",
            "people",
            "customers",
            "subscribers",
            "followers",
            "likes",
            "transactions",
            "views"
          ],          
          "targets" [
            "high",
            "low"
          ],
          "notes" "<p><em>Provide context about recent growth with a note.</em></p>",
          "core" true
        },

        {
          "name" "team",
          "title" "Team and Hiring",
          "description" "Team and hiring updates",
          "body" "<p><em>Announce and welcome new hires. Discuss what positions are currently open and what's being
          done to fill them. How many candidates are applying for the positions? Which employees are no longer
          with the company and why they left.</em></p>
          <p>It's 2 steps forward and 1 step back with the size of the team.</p>
          <ul>
            <li>We made 2 great new hires for the engineering team. Welcome John and Erica!</li>
            <li>We're looking to hire a VP of Sales and have no leads yet. We're looking for recommendations
            and are interviewing 3 head hunters this week.</li>
            <li>Our office manager Sam left to pursue a great new opportunity with Walmart Labs. We won't
            be backfilling the position so everyone needs to help out.</li>
          </ul>",
          "core" true
        }

      ]
    },
    {
      "name" "company",
      "title" "Company",
      "sections" [
      
      ]      
    },
    {
      "name" "financial",
      "title" "Financial",
      "sections" [
      
      ]      
    }
  ]
})

(def new-section (keywordize-keys new-section-plain))