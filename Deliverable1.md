# Deliverable Information

## 1: Basic Information (needed before you start with your Sprint -- Sprint Planning)

**Topic you chose:** 
> Bus Scheduling (#4)

**Sprint Number:** 
> 1

**Scrum Master**: 
> Chris Boveda

**Git Master**: 
> Kevin Dolan

### Sprint Planning (For Sprint 1-3)
Document your Sprint Planning here. Also check the kickoff document for more details on what needs to be done. This is just the documentation. 

**Sprint Goal:** 
> Our goal for Sprint 1 is to gain a more thorough understanding of the existing Memoranda system, resolve some critical bugs in the existing system, and lay the foundation for future changes.

**How many User Stories did you add to the Product Backlog:**
> We added 9 stories from the example Taiga board, and 8 additional stories that we developed ourselves.

**How many User Stories did you add to this Sprint:** 
> We added the 7 stories from the example Taiga board Sprint 1, and 3 stories that we developed ourselves.

**Why did you add these US, why do you think you can get them done in the next Sprint?**
> We believe that we can complete all of the stories we have added. In addition to ensuring that the original requirements are met, we also wanted to build a foundation of the potential dependencies for future stories.

**Why do you think these fit well with your Sprint goal? (details)**
> These stories fit our sprint goal well, and follow the theme of setting up for future success.

**Do you have a rough idea what you need to do? (if the answer is no then please let me know on Slack)**
> Yes, each of our team members confirmed in our pre-sprint standup that they have a rough idea of what needs to be done. 



## 2: During the Sprint
> Fill out the Meeting minutes during your Sprint and keep track of things. Update your Quality policies when needed, as explained in the lectures and in the Quality Policy documents on Canvas. 
I would also advise you to already fill out the Contributions section (End of sprint) as you go, to create less work at the end.

### Meeting minutes of your Daily Scrums (3 per week, should not take longer than 10 minutes):
> Add as many rows as needed and fill out the table. (Burndown starts with Sprint 2 and Travis CI starts with Sprint 3, not needed before that). 

| Date  | Attendees  |Meeting notes | Burndown Info  | TravisCI info  | Additional Info  |
|---|---|---|---|---|---|
| 14MAR21  | All  |  Complete "Icebreaker" and discuss sprint1 planning | n/a | n/a  | n/a |
| 17MAR21  | All  |  Complete Sprint1 Planning            | n/a  | n/a | n/a |
| 19MAR21  | All  | Sprint1 status updates       | n/a  | n/a | via Slack,  no blockers |
| 21MAR21  | All  | Sprint1 status updates  | n/a  | n/a | via Slack/Zoom,  no blockers |
| 24MAR21  | All  | Sprint1 status updates  | n/a  | n/a | via Slack/Zoom,  no blockers |
| 26MAR21  | All  | Sprint1 status updates  | n/a  | n/a | via Slack,  no blockers |
| 28MAR21  | All  | Sprint1 wrap-up | n/a  | n/a | via Slack/Zoom,  no blockers |
| 31MAR21 | -John | Sprint1 retrospective and Sprint2 planning | n/a | n/a | via Slack/Zoom, no blockers |

### Meeting Summary:

| Name | Meeting Attended |
|---|---|
|Brian Pape| 8|
|Chris Boveda| 8|
|Derek Argall| 8|
|John Thurstonson| 7|
|Kevin Dolan| 8|


## 3: After the Sprint

### Sprint Review
Answer as a team!

**Screen Cast link**: [Screencast](https://youtu.be/hYWZSmgaFKs)

> Answer the following questions as a team. 

**What do you think is the value you created this Sprint?**

> We resolved many of the critical bugs in the existing memoranda software, laid the foundation for the data structures that will be used in the bus scheduling system, and through our UI mockups started a plan for implementation of the bus system.

**Do you think you worked enough and that you did what was expected of you?**

> Yes, as a whole our team felt that we worked enough and did what was expected of us. Our intial estimation of the workload was below what we were capable of competing, and we pulled in one additional story from the product backlog.

**Would you say you met the customers’ expectations? Why, why not?**

> Yes, we completed all but one of the prescribed user stories, and the user story that will be rolling over in to Sprint 2 is part of our larger implementation plan for the UI.


### Sprint Retrospective

> Include your Sprint retrospective here and answer the following questions in an evidence based manner as a team (I do not want each of your individuals opinion here but the team perspective). By evidence-based manner it means I want a Yes or No on each of these questions, and for you to provide evidence for your answer. That is, don’t just say "Yes we did work at a consistent rate because we tried hard"; say "we worked at a consistent rate because here are the following tasks we completed per team member and the rate of commits in our Git logs."

**Did you meet your sprint goal?**

> Yes, our team met our sprint goal. Our code base is stable and the major bugs have been removed. Each of our team members understands the architecture of the memoranda software. The back-end data structure foundation has been established for our future changes.

**Did you complete all stories on your Sprint Backlog?**

> No, we did not complete all of our stories on our sprint backlog, as explained in the next bullet point. US#50 has been left on the sprint board to document the state the of the board at the end of the sprint.

**If not, what went wrong?**

> While most of our user stories related to the UI were focused on mockups and planning, user story 50 included acceptance criteria related to implementation. The planning tasks of US50 were completed, but the implementation was not completed in time for the end of the sprint.

**Did you work at a consistent rate of speed, or velocity? (Meaning did you work during the whole Sprint or did you start working when the deadline approached.)**

> Yes; our burndown chart showed a consistent rate of task and story completion throughout the sprint.

**Did you deliver business value?**

> Yes; though the conversion from memoranda to the bus scheduling system is not readily apparent yet, we are ending sprint 1 with a great foundation for those future changes.

**Are there things the team thinks it can do better in the next Sprint?**

> Our team agrees that we could do a better job of estimating the story points better. Most of our stories were classified as 3-points, but that does not accurately represent their complexity or effort required.

**How do you feel at this point? Get a pulse on the optimism of the team.**

> Our team is optimistic and confident that we can complete the overall goal of converting memoranda to a bus scheduling system by the end of the three sprints.

### Contributions:

> In this section I want you to point me to your main contributions (each of you individually). Some of the topcs are not needed for the first deliverables (you should know which things you should have done in this Sprint, if you don't then you have probably missed something):

#### Brian Pape:
  **Links to GitHub commits with main code contribution (up to 5 links) - all Sprints:**

    - https://github.com/amehlhase316/Krampus/commit/d0f80e7f6373ca1e9483365fe93eba3621084e2c
        - Complete refactoring of indexed data and collection classes
    - https://github.com/amehlhase316/Krampus/commit/c782291f28187e41befd9e3d091ceea40541bb4f
        - Route storage
    - https://github.com/amehlhase316/Krampus/commit/d18755bd81ba65060787c015293e08331203b9ab
        - JSON library integration with decorators and native conversion, JUnit5, gradle wrapper

  **GitHub links to your Unit Tests (up to 3 links) -- Sprint 2 and 3:**

    - link1
    - link2

  **GitHub links to your Code Reviews (up to 3 links) -- Sprint 2 and 3:**

    - link1
    - link2

  **How did you contribute to Static Analysis -- Sprint 3:**

    - link1
    - link2

  **What was your main contribution to the Quality Policy documentation?:**

    I felt that the development of our Quality Policy was a very egalitarian effort and I contributed equally as a team member.

#### Chris Boveda:
  **Links to GitHub commits with main code contribution (up to 5 links) - all Sprints:**

>   - [Correct all instances of incorrect language](https://github.com/amehlhase316/Krampus/commit/310f2ff3e89b6852ecfb269da70f5f55d792f122)
>   - [Fix additional instances of incorrect language](https://github.com/amehlhase316/Krampus/commit/c736f7c2fd09b79ba1777e43d64d5157260cf382)
>   - [Add class diagrams for core, ui, util](https://github.com/amehlhase316/Krampus/commit/281c594369edbbfda6499f41124cda1e8fdd3104)

  **GitHub links to your Unit Tests (up to 3 links) -- Sprint 2 and 3:**

    - link1
    - link2

  **GitHub links to your Code Reviews (up to 3 links) -- Sprint 2 and 3:**

    - link1
    - link2

  **How did you contribute to Static Analysis -- Sprint 3:**

    - link1
    - link2

  **What was your main contribution to the Quality Policy documentation?:**

    - Our group worked together and each member contributed equally to development of our quality policy.

#### Derek Argall:
  **Links to GitHub commits with main code contribution (up to 5 links) - all Sprints:**

    - [Window close operation fix](https://github.com/amehlhase316/Krampus/commit/71c0920954fe15bcafabbf6960a15ba6d9617440)
    - [Window minimize operation fix](https://github.com/amehlhase316/Krampus/commit/1d924c2536a5acdd0928c04dcc1964515ee302f1)
    - [Drivers tab UI Prototype](https://github.com/amehlhase316/Krampus/commit/5f0bf92d8dded53f3a41ecc538da04ab4dddd308)

  **GitHub links to your Unit Tests (up to 3 links) -- Sprint 2 and 3:**

    - link1
    - link2

  **GitHub links to your Code Reviews (up to 3 links) -- Sprint 2 and 3:**

    - link1
    - link2

  **How did you contribute to Static Analysis -- Sprint 3:**

    - link1
    - link2

  **What was your main contribution to the Quality Policy documentation?:**

    - I feel that I contributed more to the review and retrospective portion than I did to the planing portion

#### John Thurstonson:
  **Links to GitHub commits with main code contribution (up to 5 links) - all Sprints:**

    - [link1](https://github.com/amehlhase316/Krampus/commit/5c31da02de25736b2988cb575bce645b5c0461f8)
    - [link2](https://github.com/amehlhase316/Krampus/commit/60ad1ec1291faed580687494bb98280e7db1642d)

  **GitHub links to your Unit Tests (up to 3 links) -- Sprint 2 and 3:**

    - link1
    - link2

  **GitHub links to your Code Reviews (up to 3 links) -- Sprint 2 and 3:**

    - link1
    - link2

  **How did you contribute to Static Analysis -- Sprint 3:**

    - link1
    - link2

  **What was your main contribution to the Quality Policy documentation?:**

    - I helped in the discussion during the meeting where we went over the quality policy.

#### Kevin Dolan:
  **Links to GitHub commits with main code contribution (up to 5 links) - all Sprints:**

>   - [Create logo and place in repo](https://github.com/amehlhase316/Krampus/commit/c9d6202a725877bd6b5985bf06d768ca9697749a)
>   - [Create and replace Splash Screen](https://github.com/amehlhase316/Krampus/commit/69b0b5851b5496bdee1869d17c8d432c0a867f4b)

   **GitHub links to your Unit Tests (up to 3 links) -- Sprint 2 and 3:**

    - link1
    - link2

  **GitHub links to your Code Reviews (up to 3 links) -- Sprint 2 and 3:**

    - link1
    - link2

  **How did you contribute to Static Analysis -- Sprint 3:**

    - link1
    - link2
 
 **What was your main contribution to the Quality Policy documentation?:**

    - Our group worked togther to build the Quality Policy document in one of our first zoom meetings.


  
## 4: Checklist for you to see if you are done
- [x] Filled out the complete form from above, all fields are filled and written in full sentences
- [X] Read the kickoff again to make sure you have all the details
- [X] User Stories that were not completed, were left in the Sprint and a copy created
- [x] Your Quality Policies are accurate and up to date
- [X] **Individual** Survey was submitted **individually** (create checkboxes below -- see Canvas to get link)
  - [x] Brian Pape|
  - [x] Chris Boveda
  - [x] Derek Argall
  - [X] John Thurstonson
  - [X] Kevin Dolan
- [x] The original of this file was copied for the next Sprint (needed for all but last Sprint where you do not need to copy it anymore)
  - [x] Basic information (part 1) for next Sprint was included (meaning Spring Planning is complete)
  - [x] All User Stories have acceptance tests
  - [x] User Stories in your new Sprint Backlog have initial tasks which are in New
  - [x] You know how to proceed
