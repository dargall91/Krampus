### Quality Policy
> Describe your Quality Policy in detail for this Sprint (remember what I ask you to do when I talk about the "In your Project" part in the lectures and what is mentioned after each assignment (in due course you will need to fill out all of them, check which ones are needed for each Deliverable). You should keep adding things to this file and adjusting your policy as you go.
> Check in Project: Module Concepts document on Canvas in the Project module for more details 

**GitHub Workflow** (start Sprint 1)
  > - The Master branch shall be protected, and always a working and stable version of the program.
  > - The Dev branch shall also be protected and stable, but does not need to be a complete product.
  > - One branch will be created for each user story, with additional branches created for individual tasks at the programmer's discretion.
  > - Branch naming for stories and tasks shall follow the format: US#-Task#
  > - Developers may not be a tester for their own commits.
  > - Pull requests in to Dev require one tester. Pull requests in to Master require two testers.
  > - Pull requests in to Dev must be approved by another programmer, and may not be approved by the submitter.
  > - Pull requests in to Master may **only** be submitted and approved by the sprint git master.
  > - Pull requests must pass all TravisCI tests without error.
  > - **All pull requests in to Dev and Master should be fast-forwards**.
  > - Commit messages shall follow the guidelines outlined in [this post](https://chris.beams.io/posts/git-commit/)
  > - Pull requests shall have meaningful names and descriptions, and the default naming is to be avoided.

**Unit Tests Blackbox** (start Sprint 2)
  > - A developer is responsible for writing the unit tests for their own commits.
  > - If a bug is found that was not caught by the original unit tests, then the discoverer of the bug shall develop additional unit tests as needed to identify and catch the error.
  > - Unit tests are required for all non-GUI and non-trivial methods. Examples of trivial methods are getters and setters.
  > - Each developer will implement a minimuim of four unit tests per sprint.
  > - Commits for unit tests shall be clearly identified in the commit message (example: US# Task# Unit Test: )

 **Unit Tests Whitebox** (online: start Sprint 2, campus: start Sprint 3)
  > - Whitebox tests shall be used:
  > -- at the developers discretion, depending on the complexity of the method.
  > -- when code coverage of the method with blackbox testing is less than 50%.
  > - When used, whitebox tests shall follow the same naming and responsibility guidelines listed above for blackbox files.

**Code Review** (online: due start Sprint 2, campus: start Sprint 3)
  > - Each developer shall perform at least two code reviews per sprint.

**Developer Checklist**

- [ ] Commit name follows naming guidelines described in QualityPolicy
- [ ] Unit tests are in separate commits and named correctly per QualityPolicy
- [ ] Code for completed stories meets all acceptance criteria for the user story
- [ ] All source code files have a proper javadoc header
- [ ] All public methods have a javadoc banner
- [ ] All attributes are private
- [ ] All literal values (except loop indices) should be constants
- [ ] All variables, methods, and classes are named properly
  - [ ] Variable and method names are in lowerCamelCase
  - [ ] Class names are in UpperCamelCase
  - [ ] Constants and enums are in CAPS
- [ ] All complex statements, including single line statements, must use explicit brackets {}

**Reviewer Checklist**
- [ ] Code passes all TravisCI tests
- [ ] No spotbugs SA errors in 
- [ ] Commit name follows naming guidelines described in QualityPolicy
- [ ] Unit tests are in separate commits and named correctly per QualityPolicy
- [ ] Code for completed stories meets all acceptance criteria for the user story
- [ ] All source code files have a proper javadoc header
- [ ] All public methods have a javadoc banner
- [ ] All attributes are private
- [ ] All literal values (except loop indices) should be constants
- [ ] All variables, methods, and classes are named properly
  - [ ] Variable and method names are in lowerCamelCase
  - [ ] Class names are in UpperCamelCase
  - [ ] Constants and enums are in CAPS
- [ ] All complex statements, including single line statements, must use explicit brackets {}

**Static Analysis**  (start Sprint 3)
 > PRs shall contain no spotbugs errors in newly written code.  PRs shall contain minimal checkstyle errors in newly written code. Code review shall include SA checks before PRs are approved into Dev or master. SA with spotbugs and checkstyle is already set up and configured to run with each TravisCI build.

**Continuous Integration**  (start Sprint 3)
 > We have already configured TravisCI to run with each commit to the Krampus repository.  Successful CI build is required for approval of any PR into Dev or master (as was our policy in Sprint 2).

