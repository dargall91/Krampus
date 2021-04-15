# Deliverable Information

## 1: Basic Information (needed before you start with your Sprint -- Sprint Planning)

**Topic you chose:**
> Bus Scheduling (#4)

**Sprint Number:**
> 2

**Scrum Master**:
> Derek Argall

**Git Master**:
> Brian Pape

### Sprint Planning (For Sprint 1-3)
Document your Sprint Planning here. Also check the kickoff document for more details on what needs to be done. This is just the documentation.

**Sprint Goal:**
> Our overall goal for this sprint is to complete the functionality requirements of the Bus Scheduling system, including the route planning algorithm, and to have basic displays available for each of the modules.

**How many User Stories did you add to the Product Backlog:**
> We added 2 stories to the product backlog, and had one story rollover from sprint 1.

**How many User Stories did you add to this Sprint:**
> We added 7 stories to the sprint 2 backlog for a total of 37 points.

**Why did you add these US, why do you think you can get them done in the next Sprint?**
> We selected these stories because they are directly related to our sprint goal. We believe we can get them done within this sprint because the total points is equal to the number of points we completed last sprint.

**Why do you think these fit well with your Sprint goal? (details)**
> The stories for sprint 2 cover each of the core entities -- nodes, routes, tours, busses, and drivers -- and their associated UI module. Completion of these stories would mark the completion of the basic requirements of the system, and allow us to focus on polish in sprint 3.

**Do you have a rough idea what you need to do? (if the answer is no then please let me know on Slack)**
> Yes, our team has a complete understanding of what needs to be done for this sprint and feels confident.


## 2: During the Sprint
> Fill out the Meeting minutes during your Sprint and keep track of things. Update your Quality policies when needed, as explained in the lectures and in the Quality Policy documents on Canvas.
I would also advise you to already fill out the Contributions section (End of sprint) as you go, to create less work at the end.

### Meeting minutes of your Daily Scrums (3 per week, should not take longer than 10 minutes):
> Add as many rows as needed and fill out the table. (Burndown starts with Sprint 2 and Travis CI starts with Sprint 3, not needed before that).

| Date  | Attendees  | Meeting notes | Burndown Info  | TravisCI info  | Additional Info  |
|---|---|---|---|---|---|
| 31MAR21 | -John | Sprint1 retrospective and Sprint2 planning | n/a | n/a | via Slack/Zoom, no blockers |
| 02APR21  | All  | Sprint2 status updates | Some tasks completed, but US yet  | n/a | via Slack,  no blockers |
| 04APR21 | Brian, Derek, John | Discussing progress, check TravisCI, check Taiga | On track | All builds are good | Slack/zoom, no blockers |
| 07APR21 | All | Slack check-in | On track | All builds are good | Slack/zoom, Personal and vaccination issues for some team members |
| 09APR21 | Brian, Derek | Discussing progress, check TravisCI, check Taiga | Catch-up required | All builds are good | Slack/zoom, Personal and vaccination issues for some team members |
| 11APR21 | All (-John in Slack) | End of Sprint Meeting | Will be completed tonight | All builds are good | via Slack/Zoom, no blockers |

### Meeting Summary:

| Name | Meeting Attended This Sprint | Sprint 1 | Project Total |
|---|---|---|---|
|Brian Pape| 5| 8| 13|
|Chris Boveda| 3| 8| 11|
|Derek Argall| 5| 8| 13|
|John Thurstonson| 5| 7| 12|
|Kevin Dolan| 5| 8| 13|


## 3: After the Sprint

### Sprint Review
Answer as a team!

**Screen Cast link**:

> TBD

**What do you think is the value you created this Sprint?**

> The system performs all of the basic functions outlined in Sprint2 - this entails displaying all required data except the actual computed short-path route.

**Do you think you worked enough and that you did what was expected of you?**

> We accomplished all of the USs for Sprint 2, but aim to improve the consistency of work for the next Sprint.  Our burndown chart on Taiga demonstrates this.

**Would you say you met the customers’ expectations? Why, why not?**

> We completed implementation of the primary bus scheduling system UI and back-end components.  The changes are visible and tangible, which will allow the customer to provide us with feedback on the implementation.


### Sprint Retrospective

> Include your Sprint retrospective here and answer the following questions in an evidence based manner as a team (I do not want each of your individuals opinion here but the team perspective). By evidence-based manner it means I want a Yes or No on each of these questions, and for you to provide evidence for your answer. That is, don’t just say "Yes we did work at a consistent rate because we tried hard"; say "we worked at a consistent rate because here are the following tasks we completed per team member and the rate of commits in our Git logs."

**Did you meet your sprint goal?**

> Yes, all User Stories were completed, and we implemented all the functionality requirements.

**Did you complete all stories on your Sprint Backlog?**

> Yes, we completed all the user stories as is evidenced by the Taiga Sprint 2 board.

**If not, what went wrong?**

> N/A

**Did you work at a consistent rate of speed, or velocity? (Meaning did you work during the whole Sprint or did you start working when the deadline approached.)**

> No, there were a few days were no stories or tasks were completed.

**Did you deliver business value?**

> Yes, basics of the Bus Scheduling system were implemented and working. This implementation provides the business value presented to the customer during Sprint 2 planning.

**Are there things the team thinks it can do better in the next Sprint?**

> Work more consistently and communicate better with the team when a problem arises.

**How do you feel at this point? Get a pulse on the optimism of the team.**

> Our team feels good about our progress so far and we're confident we can improve the system in the next Sprint.

### Contributions:

> In this section I want you to point me to your main contributions (each of you individually). Some of the topcs are not needed for the first deliverables (you should know which things you should have done in this Sprint, if you don't then you have probably missed something):

#### Brian Pape:
**Links to GitHub commits with main code contribution (up to 5 links) - all Sprints:**

- [US8-Task106: Add NodeMapper class to scale nodes for plotting](https://github.com/amehlhase316/Krampus/commit/34f122ed5449362c5b74039d541075cf6f4a32ba)
- [US37-Task107: Add Bus/Tour interaction and tour length functions](https://github.com/amehlhase316/Krampus/commit/301fe2db74db912c602de79b5a3ec813e0693b36)
- [US82-Task109: database singleton class](https://github.com/amehlhase316/Krampus/pull/82/files)
- [US37/US82: Add Bus and tour functionality](https://github.com/amehlhase316/Krampus/commit/b3f74ed4307f774f91a46772a24da950830b7360)
- [US82-Task87: Driver serialization](https://github.com/amehlhase316/Krampus/commit/38586b6bf0ec95bc80f1afefe0751e84c2ccc9a5)

**GitHub links to your Unit Tests (up to 3 links) -- Sprint 2 and 3:**

- [NodeMapper unit tests](https://github.com/amehlhase316/Krampus/pull/78/commits/d58645c3c8c049261c16f14ed6db1f13ca1ce0c7)
- [Tour/driver relations](https://github.com/amehlhase316/Krampus/pull/78/commits/a19fbe0b3052489642dbdfab9ffedc1f98cb52d5)
- [Bus/Tour](https://github.com/amehlhase316/Krampus/pull/78/commits/c07b6e46fe445bcf7dfa45b4d5126ea8d546ba95)

**GitHub links to your Code Reviews (up to 3 links) -- Sprint 2 and 3:**

- [Standard approval using team QP checklist on code requiring no changes](https://github.com/amehlhase316/Krampus/pull/84#partial-pull-merging)
- [Code review with Change request](https://github.com/amehlhase316/Krampus/pull/64#partial-pull-merging)

**How did you contribute to Static Analysis -- Sprint 3:**

-

**What was your main contribution to the Quality Policy documentation?:**

 - Our team worked collaboratively as a group to develop our Quality Policy documentation and each team member contributed to its development.


#### Chris Boveda:
**Links to GitHub commits with main code contribution (up to 5 links) - all Sprints:**

  - [Completion of US90 (Pathfinder)](https://github.com/amehlhase316/Krampus/commit/b9868a308492531ce450c3a144e859d0b760e90b)
  - [Progress on US90 (Pathfinder)](https://github.com/amehlhase316/Krampus/commit/92717973b46acc8996aca316332dcba3d512fe8b)

**GitHub links to your Unit Tests (up to 3 links) -- Sprint 2 and 3:**

  - [Tests for the shortpath algorithm and helper](https://github.com/amehlhase316/Krampus/commit/24cdd73c5dce824ccb49ddb643b24dee08dd00e0)
  - [Tests for the optimized shortpath algorithm](https://github.com/amehlhase316/Krampus/commit/4a4da37f5b74f2bf1abbb81ac3c3e2493b78e768)

**GitHub links to your Code Reviews (up to 3 links) -- Sprint 2 and 3:**

  - [Code Review 1](https://github.com/amehlhase316/Krampus/pull/88)
  - [Code Review 2](https://github.com/amehlhase316/Krampus/pull/82)
  - [Code Review 3](https://github.com/amehlhase316/Krampus/pull/81)

**How did you contribute to Static Analysis -- Sprint 3:**

 -

**What was your main contribution to the Quality Policy documentation?:**

- Our group worked together and each member contributed equally to development of our quality policy.

#### Derek Argall:
**Links to GitHub commits with main code contribution (up to 5 links) - all Sprints:**

 - [Completion of Drivers Tab](https://github.com/amehlhase316/Krampus/commit/953316fe9e433d8cae1e23d2e48e6bd54825bd6d)
 - [Completion of Buses Tab](https://github.com/amehlhase316/Krampus/commit/6fbdc2363612a96511678f09918c2f8d7367ae35)
 - [Changing projects reloads Bus and Driver data for the new project](https://github.com/amehlhase316/Krampus/commit/6fbdc2363612a96511678f09918c2f8d7367ae35)

**GitHub links to your Unit Tests (up to 3 links) -- Sprint 2 and 3:**

 - N/A (worked on UI components only)

**GitHub links to your Code Reviews (up to 3 links) -- Sprint 2 and 3:**

 - [Review #1](https://github.com/amehlhase316/Krampus/pull/81)
 - [Review #2](https://github.com/amehlhase316/Krampus/pull/83)

**How did you contribute to Static Analysis -- Sprint 3:**

 - link1
 - link2

**What was your main contribution to the Quality Policy documentation?:**

 - I contributed alongside the whole team to create the Quality Policy.

#### John Thurstonson:
**Links to GitHub commits with main code contribution (up to 5 links) - all Sprints:**

 - [Implement user ability to add, remove and edit Tours](https://github.com/amehlhase316/Krampus/commit/ed940ab2bc20d7fd43ee63a8bcb9258c7db5e5ea)   
 - [Create Tour Dialog](https://github.com/amehlhase316/Krampus/commit/ac470d296e587df47ed822bc65ac3ee58ffa2fd2)   
 - [Create Tour Panel](https://github.com/amehlhase316/Krampus/commit/be88a8de14df67b7713599c45f08841578cd4411)

**GitHub links to your Unit Tests (up to 3 links) -- Sprint 2 and 3:**

 - N/A
 - 

**GitHub links to your Code Reviews (up to 3 links) -- Sprint 2 and 3:**

 - [Code Review](https://github.com/amehlhase316/Krampus/pull/86)
 - 

**How did you contribute to Static Analysis -- Sprint 3:**

 - link1
 - link2

**What was your main contribution to the Quality Policy documentation?:**

 - Our team worked together to produce our quality policy.

#### Kevin Dolan:
**Links to GitHub commits with main code contribution (up to 5 links) - all Sprints:**

- [Implement the graphical structure for the route map](https://github.com/amehlhase316/Krampus/commit/0de17586822637dd0a62ee8d482a6d3cdb4736f2)
- [Refactor the RouteMap into two classes](https://github.com/amehlhase316/Krampus/commit/369422719a5ef073dd110b4bc14c45a5a2c2285c)
- [Fix bugs in RouteMap and Stops](https://github.com/amehlhase316/Krampus/commit/cc3e1ea8a6bb765e359a40a7c23980ff4bfcdb59)

**GitHub links to your Unit Tests (up to 3 links) -- Sprint 2 and 3:**

- [Unit Tests for RouteMap](https://github.com/amehlhase316/Krampus/commit/f261f77874a8220d4ffd017bf71983c0e18c7e0d)

**GitHub links to your Code Reviews (up to 3 links) -- Sprint 2 and 3:**

- [Code Review 1](https://github.com/amehlhase316/Krampus/pull/88)
- [Code Review 2](https://github.com/amehlhase316/Krampus/pull/89)

**How did you contribute to Static Analysis -- Sprint 3:**

- N/A (SPRINT 3)

**What was your main contribution to the Quality Policy documentation?:**

- The group worked collectively in a zoom meeting to establish our quality policy.

## 4: Checklist for you to see if you are done
- [x] Filled out the complete form from above, all fields are filled and written in full sentences
- [x] Read the kickoff again to make sure you have all the details
- [x] User Stories that were not completed, were left in the Sprint and a copy created
- [x] Your Quality Policies are accurate and up to date
- [x] **Individual** Survey was submitted **individually** (create checkboxes below -- see Canvas to get link)
  - [x] Brian Pape|
  - [x] Chris Boveda
  - [x] Derek Argall
  - [x] John Thurstonson
  - [x] Kevin Dolan
- [x] The original of this file was copied for the next Sprint (needed for all but last Sprint where you do not need to copy it anymore)
  - [x] Basic information (part 1) for next Sprint was included (meaning Spring Planning is complete)
  - [x] All User Stories have acceptance tests
  - [x] User Stories in your new Sprint Backlog have initial tasks which are in New
  - [x] You know how to proceed
