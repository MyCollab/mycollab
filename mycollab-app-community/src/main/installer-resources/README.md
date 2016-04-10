
Release Notes
==============
MyCollab is an open source Collaboration Platform of Project Management, CRM and Document.

MyCollab is licensed under the Affero GPL 3 terms.

You can checkout our latest development source codes at [https://github.com/esofthead/mycollab](https://github.com/esofthead/mycollab)

For further information, please visit:

* [https://www.mycollab.com/](https://www.mycollab.com/)
* [https://community.mycollab.com/](https://community.mycollab.com/)

Contact the MyCollab team at:

* Our Q&A page [https://www.mycollab.com/qa/](https://www.mycollab.com/qa/)
* Our web form [https://www.mycollab.com/contact/](https://www.mycollab.com/contact/)

System requirements
===================

MyCollab requires a running Java Runtime Environment (7 or greater), Java command should be presented in PATH environment and MySQL (InnoDB support recommended).


Please have a look at MyCollab requirements

* [https://community.mycollab.com/installing-mycollab/](https://community.mycollab.com/installing-mycollab/)

Installation
============

1. Download MyCollab Binary File mycollab-x.xxx-dist
2. Follow installation guideline at [https://community.mycollab.com/installing-mycollab/](https://community.mycollab.com/installing-mycollab/)
3. If you already installed MyCollab and you want to upgrade to MyCollab latest version, this link [https://community.mycollab.com/administration/upgrade/](https://community.mycollab.com/administration/upgrade/) will instruct steps to make your bug easily

If you want to understand more MyCollab advanced configuration settings, please visit the link [https://community.mycollab.com/administration/configuration/](https://community.mycollab.com/administration/configuration/). You will finish reading and
understanding in a matter of minutes.

Last changelog
==============

Version 5.2.12
-----------------------------
**Library Upgrades**

* Other libraries

**Improvements & New Features**

* [Premium] Manage companies
* [Premium] Print invoice and send to client
* [Premium] Allow to upload client logo
* Allow to upload project logo
* New Project list view
* Support crowdsourcing translation
* Other minor improvements

**Bug Fixes**

* None


Version 5.2.11
-----------------------------
**Library Upgrades**

* Joda Time 2.9.3
* Mybatis spring 1.2.5
* Other libraries

**Improvements & New Features**

* [Premium] Export timesheet into PDF and Excel
* [Premium] Edit overtime field of time tracking
* Optimize dependency libraries to reduce the distribution file
* Support print function
* Other minor improvements

**Bug Fixes**

* Report still throws error in some machines due to the order of loading jar files. This version remove this issue 
completely.


Version 5.2.10
-----------------------------
**Library Upgrades**

* Hiraki CP 2.4.5
* Other libraries

**Improvements & New Features**

* [Premium] Invoice management in project
* [Premium] Favorite view in project
* [Premium] Project members can enter overtime hours per assignment
* [Premium] Add 'Can read financial data' permission in project
* [Premium] Tag management supports for components and versions
* [Premium] Favorites management support for components and versions
* [Premium] Can calculate the financial data in timing view
* Support install MyCollab as windows service
* Other minor improvements

**Bug Fixes**

* Form display badly when the field shows long text
* Can not export pdf report at the first time
* File management is more stable
* Re-invite user does not update the latest user information
* Project Member still receive his watched items updates though he leaved the project already

Version 5.2.9
-----------------------------
**Library Upgrades**

* Vaadin 7.6.4
* Spring framework 4.2.5.RELEASE
* Jackrabbit 2.12.1
* Jetty 9.2.15.v20160210
* Flyway 4.0
* Commons Collection 3.2.2
* Other libraries

**Improvements & New Features**

* [Premium] Tag management is supported
* [Premium] Project client management is added
* [Premium] Support tag for phase and risk
* New user group for task and bug views
* Allow to change color column of Kanban board
* Improve error handling page mechanism
* Other minor improvements

**Bug Fixes**

* [Premium] Display wrong list of history fields in several special cases
* [Premium] Save the new project throws error
* Rename option column may cause the error in trending chart
* Websocket timeout error

Version 5.2.8
-----------------------------
**Improvements & New Features**

* [Premium] Revise the Calendar view
* Support project template
* Add 'Won't Fix' status to bug workflow
* Other minor improvements

**Bug Fixes**

* [Premium] Export project timelog is failed
* Delete the selected resource is failed

Version 5.2.7
-----------------------------
**Library Upgrades**

* Vaadin 7.6.3
* MyBatis 3.3.1
* Other libraries

**Improvements & New Features**

* Display issues, risks in Gantt chart besides tasks, milestones as well
* Several minor improvements
* Add lead field to the project
* Display delete action in the activity stream
* Enhance and improve stability of Gantt chart
* Improve the stability of upgrade process on Windows
* [Premium] Support calendar event types are issues, risks, milestones

**Bug Fixes**

* [Mobile] Throws silent exception when display the empty activities in account
* Throws exception when assign the Project Owner role to the existing user
* Not display deleted entries in the activity stream

Version 5.2.6
-----------------------------
**Library Upgrades**

* Jetty 9.2.14.v20151106
* Other libraries

**Improvements & New Features**

* Lighter email template
* Send overdue assignments email
* Notify project members when the new member join

**Bug Fixes**

* Can not assign user as the account owner
* Can not load fonts of reporting
* [Premium] Display events with no project throws exception
* [Mobile] Can not redirect the login view when the obsolete password id stored in the local storage

Version 5.2.5
-----------------------------
**Library Upgrades**

* Vaadin 7.6.1
* Other libraries

**Improvements & New Features**

* The new mobile UI revised
* Minor UI improvements
* Improve error handling

**Bug Fixes**

* None


Version 5.2.4
-----------------------------
**Library Upgrades**

* Gson 2.5, Guava 19, Jackrabbit 2.11.3, Spring framework 4.2.4.RELEASE
* Other libraries

**Improvements & New Features**

* Add upcoming tasks in the project dashboard
* Customize color of task statuses in kanban board
* Display detail watchers in the sidebar
* The new project dashboard
* Improve theme compatibility
* Several minor improvements


Version 5.2.3
-----------------------------
**Library Upgrades**

* Joda 2.9.1, Hiraki CP 2.4.2, SLF4J 1.7.13
* Spring framework 4.2.3.RELEASE
* Dynamic Reports 4.0.2

**Improvements & New Features**

* Allow inline edit task, milestone and bug summary field in dashboard view
* Minor UI updates

**Bug Fixes**

* Upgrade process still keeps the old version value of class path items

Version 5.2.2
-----------------------------

**Library Upgrades**

* Joda 2.9
* Quartz 2.2.2
* Http component 4.4.4, other libraries

**Improvements & New Features**

* Add timeline tracking chart
* Major UI updates includes animations, consistent UI controls and colors
* Detect more potential issues of installation and give the solutions to users

**Bug Fixes**

* Fix several issues of push events in the corporate networks
* Advanced user layout does not keep values of the simple user layout
* Pretty time text is wrong with several timezones

Version 5.2.1
-----------------------------

**Library Upgrades**

* Spring framework 4.2.2.RELEASE and others

**Improvements & New Features**

* Predefined query for tasks, bugs
* Allow user rename or delete column in the task kanban board
* Do minor UI improvements
* Validate the file permission in the installation script
* Add shutdown script to allow admin can shutdown MyCollab properly
* The new modal window
* Faster form binding and improve the performance a little bit
* Autofill username and password for the login form
* Allow user can switch from push to poll method if their network proxy prohibit the push method

**Bug Fixes**

* Wrong user avatar of assignments in project roadmap view
* Validate date constraints of entity is wrong in several cases
* Calendar information is not refresh if the new set is empty
* Update search query is not effected

Version 5.2.0
-----------------------------

**Library Upgrades**

* Upgrade Gson 2.4, HirakiCP 2.4.1 and more

**Improvements & New Features**

* Make bug management is easier
* Introduce the new roadmap view
* Introduce the calendar of tasks management

**Bug Fixes**

* Word-wrap title for long text of several views
* Task duration calculating with timezone

Version 5.1.4
-----------------------------

**Library Upgrades**

* Upgrade Vaadin 7.5.7
* Upgrade Hiraki CP 2.3.11, Hibernate Validator 5.2.2 and others

**Improvements & New Features**

* Gantt chart bug fixes
* Calculating end date by start date and duration
* Toggle project menu in MS Edge, Firefox is not effective
* Other minor UI improvements
* A lot of reporting enhancements: better layout, more detail information, font awesome and more
* New installer tool, clean release notes and license view

**Bug Fixes**

* Can not change the role of project member
* Several minor bug fixes of reporting

Version 5.1.3
-----------------------------

**Library Upgrades**

* Vaadin 7.5.5
* Spring framework 4.2.1.RELEASE
* Scala 2.11.7, Jackson 2.6.2

**Improvements & New Features**

* Reduce the startup time
* Several minor UI improvements
* Support task predecessors
* Edit inline on Gantt chart
* Many Gantt chart improvements
* Site response is faster by using the proper server push
* More reliable upgrade process

**Bug Fixes**

* Has redundant scrollbar on internet explorer
* Navigate folder by clicking file breadcrumb throws exception in several cases
* Notification setting minimal may cause can not send email
* Better diagnosis error message to the end users


Version 5.1.2
-----------------------------

**Improvements & New Features**

* Menu navigator is easy to understand for geeks
* Improve the product layout general

**Bug Fixes**

* Search in project throws error
* Remove redundant warning message of permission when user access the profile page


Version 5.1.1
-----------------------------
**Library Upgrades**

* Jackson library 2.6.1, DynamicReports 4.0.1, MyBatis 3.3.0, Spring 4.2.0.RELEASE, Infinispan 7.2.4
* Vaadin 7.5.3
* JQuery 2.1.4

**Improvements & New Features**

* New kanban board for project tasks, bugs
* Update attachment display
* Generate thumbnail for images
* Update the task management workflow to remove the task group !!!
* Reduce the time response for heavy loading page
* Improve Gantt chart

**Bug Fixes**

* General bug fixes of file module
* Can not download file folder
* The bug widgets do not refresh when bug status changed
* Can not display tooltip of CRM bug
* Upload files do not show the progress properly


Version 5.1.0
-----------------------------
**Library Upgrades**

* Spring framework upgrade to 4.1.7.RELEASE, Infinispan, MySQL Connector

**Improvements & New Features**

* Redesign the email template
* Can resend the invitation
* Optimize Vaadin widgetsets
* Remove Camel library and reduce the number of threads daemons
* Reduce chart generator memory footprint
* Theme color consistent fixed
* Add outlook smtp help message
* Many minor UI improvements

**Bug Fixes**

* Fix gzip response since the version 5.0.10 uses Jetty 9.3 deprecate servlet gzip filter
* Can not change the default port 8080 on Windows
* MyCollab can not run with Java 7
* Can not run MyCollab in some rare cases due to the order of service beans is not controlled
* Refresh page of bug list and gantt view shows internal error


Version 5.0.10
-----------------------------
**Library Upgrades**

* Upgrade Vaadin to 7.5.0
* Upgrade other libraries: Tika

**Improvements & New Features**

* Make the UI more consistent and minor UI improvements
* Other general improvements

**Bug Fixes**

* System error when user accept the project invitation with project owner role
* Modal window overlays on tooltip


Version 5.0.9
-----------------------------
**Library Upgrades**

* Upgrade Vaadin to 7.4.8
* Upgrade libraries: AspectJ, Jetty, MyBatis, Joda-Time, etc

**Bug Fixes**

* User can not upload non english file name document
* Proper handling system roles
* The resource paths still throw error in several cases
* Email notification could be sent multiple times to the recipients
* Some assets has wrong paths


Version 5.0.8
-----------------------------
**Library Upgrades**

* Upgrade Vaadin to 7.4.7
* Upgrade libraries: HirakiCP, Jackrabbit, Commons-Email, Jetty

**Improvements & New Features**

* Parse html better, display rich html text smoothly
* Improve code quality overall
* Optimize the hash query and improve the site performance a little bit
* Revise the Gantt chart display
* Remove redundant assets and libraries to reduce the size of downloaded file size
* Support send email via StartTls or Ssl/Tls protocols

**Bug Fixes**

* Search project may throw exception in some special case
* Export report document is sometimes failed
* Upload file in page editor throws exception
* Add a new project page throws exception
* Fix several bugs relate to Gantt chart
* Display the new user information when user re-login by another account
* Fix missing assets in email template
* Some views can not display well on internet explorer browser
* Crm menu does not refresh if user logout then login with two different accounts

Version 5.0.7
-----------------------------
**Library Upgrades**

* Upgrade Vaadin to 7.4.6
* Upgrade other libraries

**Improvements & New Features**

* Revise the runner process. Important for auto upgrade feature
* Remove redundant images assets
* Change the default cdn url
* Change the default browser cache of vaadin
* Add context support for project
* Improve sending email with various SMTP settings
* Add warning message if user ask retrieve password without configuring SMTP (#fix per user's feedback)

**Bug Fixes**

* Minor bug fixes on IE browsers when user press button
* Email subject of bug group has project name is null

Version 5.0.6
-----------------------------
**Library Upgrades**

* Upgrade other libraries

**Improvements & New Features**

* Redirect to the server host instead localhost after installing successfully
* Minor change in bug display
* Add help menu in view
* More comprehensive message to instruct user understand field value better
* Request user change username for the first time login after installation
* Increase the size of database connection pool to 50 for heavy load sites
* Improve the build process

**Bug Fixes**

* Display the error message when user upgrade MyCollab failed due to the file permission
* Did not display detail error message when validate form but the empty string
* Install MyCollab in Internet Explorer has several issues

Version 5.0.5.1
-----------------------------
**Library Upgrades**

* Upgrade Vaadin to 7.4.5

**Improvements & New Features**

* Revise the notification window to makes it looks nicer

**Bug Fixes**

* Can not send email in port different 25 in several cases
* Fix log location
* Exception when get the default user avatar


Version 5.0.5
-----------------------------
**Library Upgrades**

* Upgrade Vaadin to 7.4.4
* Upgrade other libraries

**Improvements & New Features**

* Revise CRM comments and history features to make it similar than project module
* Revise the layout of CRM module
* Display comment in CRM activity stream
* Support font awesome in emails
* Minor bug fixes in reporting
* Cache reflection fields hence upgrade the app performance up to 5%
* Add time summary for bug group, milestone, component, version views
* Allow system admin can change the SMTP setting on the fly
* Enable gzip compression for assets by default
* Replace the old log4j library by logback

**Bug Fixes**

* Checkbox in Chrome, IE has the unintended border
* Other minor CSS issues on IE, Firefox

Version 5.0.4
-----------------------------

**Library Upgrades**

* Upgrade Vaadin to 7.4.3
* Upgrade Jetty Server version
* Upgrade other libraries

**Improvements & New Features**

* Auto update the new MyCollab version
* Ask system admin or warn users need to set up smtp setting for features which need to send email
* The new project file module
* Revise the application variable scopes
* Support quick review with instant tooltip
* Revise the bug list display
* Minor improve on page view
* Display uploaded file size
* Add detail information for uploaded file in tooltip
* Display the friendly timezone name in tooltip

**Bug Fixes**

* The default created time of user should match with the default timezone
* The empty list view of bug, component and version has the horizontal scrollbar
* Fix links of MyCollab document


Version 5.0.3
-----------------------------

**Library Upgrades**

* Upgrade Vaadin to 7.4.2
* Upgrade other libraries: Spring framework, Jackrabbit, SLF4J etc

**Improvements**

* Easier to navigate among projects
* Add search of generic assignments to project
* Revise and do several bug fixes the file module
* Support tooltip for multi select component
* Display pretty time on label and detail date tooltip
* Add description for the project generic assignment
* Revise the invite project member view

**Bug Fixes**

* Edit project member throws exception
* Display bug context menu properly according to coordination of mouse
* Can not change the project information
* Correct year format of contact birthday
* Prevent the issue of can not view bug or bug because the project short name is invalid
* Do not display year in birthday combo box

Version 5.0.2
-----------------------------

**Library Upgrades**

* Upgrade Vaadin to 7.4.1
* Upgrade other libraries

**Improvements**

* Add help message for project views

**Bug Fixes**

* Template select the default locale if the user locale is not existed
* The activity stream service throws exception when saving project page in several special cases
* Export project page include the title in the document
* Saving comment may throw exception in several cases
* Email notification for the created item does not display the detail of information


Version 5.0.1
-----------------------------
**Library Upgrades**

* Upgrade libraries version

**Improvements**

* Optimize SQL query and there are some processes are 2x faster !!!
* Add custom notifiers
* Support default button in windows
* Improve search result of bug list
* Support tag for tasks, bugs
* User is able to manage the favorites
* User is able to select the notifiers when he creates the new bug
* Display number of items in the search panel
* Support tooltip for project message widget
* Screen navigator works more efficiently
* Add more strictly validation constraint to project bug

**Bug Fixes**

* Attachment box in Safari display wrongly
* Query project member tasks show wrong items
* Query project roles did not work
* Export bug list to document throws exception
* Can not sending email in several cases

Version 5.0.0
-----------------------------
**Library Upgrades**

* Upgrade Vaadin to 7.3.10
* Upgrade Spring frameworks to 4.1.5.RELEASE
* Upgrade several Vaadin addons
* Upgrade all libraries up-to-date, there are few name are Infinispan, Apache commons, etc

**Improvements**

* Check the latest version notification
* Replace icons by font awesome
* Revise bug group layout
* Support sub tasks for project bug
* Simplify application configuration (for developers) to utilize convention over configuration
* Using Scala for several back-end components such as scheduler
* Allow assign user in bug view
* Allow deselect assignee in bug or bug
* Revise project and crm views
* Revise the related bug relationship
* Remove redundant css elements
* Fix layout issues in the Internet Explorer browsers

**Bug Fixes**

* Load the empty list of projects throws error in some special cases
* Internal issue causes the lazy views load infinitely
* Save crm call throws exception
* Update time logging but not update the project information immediately
* Tooltip of several entities do not display properly
* Time logging display the double value wrongly some times
* Gantt chart display to miss the last day of bug
* Default permission of guest role is wide open


Version 4.5.5
-----------------------------
**Library Upgrades**

* Upgrade Vaadin to 7.3.5
* Upgrade HirakiCP to 2.2.5
* Upgrade Jackson to 2.4.4
* Upgrade Infinispan to 7.0.2
* Upgrade several Vaadin addons

**Improvements**

* Add user comment to the activity stream

**Bug Fixes**

* Display the activity stream in CRM dashboard may show the duplicated items
* Not thread safe when saving bugs, tasks
* Navigate user account throws exception
* Display full comment content are enclosed with crm note


Version 4.5.4
-----------------------------

**Library Upgrades**

* Upgrade Spring framework to 4.1.2.RELEASE
* Upgrade HirakiCP to 2.2.4
* Upgrade Vaadin to 7.3.4
* Upgrade AspectJ library to 1.8.4

**Improvements**

* Make the right widgets fly over when user scroll to the bottom in the bug list view
* Revise Task, Bug, Risk, Problem, Milestone read views
* [Mobile] Make the back button associates to native back action to improve the navigation performance
* [Mobile] Support thumbnail for attachment
* [Mobile] User can preview attachment easily
* [Mobile] Support display hyperlinks for assignee, bug, bug group, milestone etc
* [Mobile] Revise time logging component
* [Mobile] Revise the mobile toolbar display
* [Mobile] Replace icons for better UI experience

**Bug Fixes**

* Fix timezone issue for datefield component
* Can not send email if user switch to the locale not in the supported list
* Can not get the log date for time logging in several rare browsers
* [Mobile] Back button does not work properly
* [Mobile] View title is not aligned center in iOS
* [Mobile] Can only upload one file in iOS

Version 4.5.3
-----------------------------

**Library Upgrades**

* Upgrade AspectJ library to 1.8.3

**Improvements**

* The new MyCollab mobile is released! This is the alpha release, use it at your own risk.
* All description supports rich text
* Clean html in rich text to help html display html string correctly
* Display the crm activity stream properly when there is several items user can not access
* Display attachments in thumbnail mode
* Allow users can search in their following ticket list

**Bug Fixes**

* Custom layout throws NPE
* Fix NPE when user navigate the project time tracking list
* Throws error if user save the empty page content
* Minor fixes in set the overdue title colors in project and crm items
* Error when user upload the invalid file name

Version 4.5.2
-----------------------------

**Library Upgrades**

* Upgrade Spring framework to 4.1.1.RELEASE
* Upgrade Vaadin to 7.3.3
* Upgrade Camel to 2.14.0

**New Features**

* Make the new MyCollab installer for every platform: Windows, MacOS, Linux
* Install MyCollab as window service
* The new notification to remind user upload his avatar if he did not

**Improvements**

* Remove unused libraries for the community edition such as Dropbox, Solr to reduce the size of installer
* Tweak the error handling while init view
* Refactor the file module management
* Add more unit tests for crm, content, project services and use assertj instead of standard junit assert statements
* Revise the localization and date format associate to locale. It helps external developers can translate MyCollab to other languages easily
* Refactor the test module, using assertJ and JUnit TestRule instead of the custom JUnitClassRunner to make unit tests more flexible in enhancements
* Remove remote resources in setup assets. User can install MyCollab in their LAN network, we do not need an internet connection requires
* Remove the warning message of Jackrabbit

**Bug Fixes**

* Project Notification can not be sent in several special cases
* Minor bug fixes and new features for mobile edition
* Can not save audit log in several special cases
* Search time logging throws exception in several special input cases
* Fix NPE exception when there is several options has null value
* Minor bug fixes for file module
* Fix issue of can not reload the web context when we upgrade Jetty to 9.2.3
* Fix migration scripts could be failed with several MySQL versions

Version 4.5.1
-----------------------------
**Improvements**

* Upgrade Spring framework to 4.1.0.RELEASE
* Upgrade Vaadin libraries
* Adding scheduler unit tests
* Project page could be exported into pdf format
* Project time logging is displayed in multiple layouts
* User can add comment to project component and project version
* Validate project shortname is unique in account
* Make links of bug and bug are readable
* Add tooltip to pretty time display

**Bug Fixes**

* Save bug in bug read view throws exception
* Inconsistent time logging permission
* Throws exception if user saves an empty search criteria
* Return redundant bug element for empty bug list
* Switch view causes the right widget in bug, bug display in the wrong position

Version 4.5.0
-----------------------------
**Improvements**

* Upgrade libraries versions
* Revise project time tracking view
* Support version management in project page
* Support date format associate to user's locale
* Break mobile app into 2 sub apps: CRM and Project Management
* Reload site after user change information to make this change is applied immediately
* Improve error handling in event bus implementation
* The project time tracking views support multiple views
* Create the default bug group when user create the new project
* Support more search criteria for bug
* Support display inline more image file types different than jpg/png

**Bug Fixes**

* Page path is cached across projects
* Milestone simple view display wrongly
* Display date time field correctly with user custom timezone
* Can not display username in phase detail view if the display name is empty
* Log user is removed when user update bug status
* Can not trace activity when user update several kinds of items

Version 4.4.0
-----------------------------
**Improvements**

* Add new page composer in project module
* Allow users can archive project
* Revise milestone list view to allow user can edit, delete milestone
* Revise invite project member that displays user avatar in suggested list
* Display total open bugs, due bugs in bug dashboard
* Clean user session resources properly
* Optimize image sizes

**Bug Fixes**

* Tooptip use the default system language instead of user defined language
* Component and Version do not show the marker of complete right
* Ambiguous email field search criteria in CRM account search page
* Project permissions are cached across projects
* Show project permissions wrongly in Japanese locale

Version 4.3.3
-----------------------------
**Improvements**

* Update Japanese localization
* Update mobile module
* Support chart localization
* Support gantt chart localization
* Improve page template
* Upgrade Vaadin version to 7.2.6
* Improve UIs of projet views includes Bug, Task, Version, Component, Milestone
* Allow users delete comments
* Having the new Time Tracking feature

**Bug Fixes**

* Remove border of Create Project button
* Fix assignment table header misses border
* Fix several broken unit tests
* Fix css issues of email template
* Edit project view is not consistent to other views
* Can not display Japanese in several servlets
* Can not display well on Safari browser due to Vaadin profiler

Version 4.3.2
-----------------------------
**Improvements**

* Update Japanese localization
* Upgrade Jasypt library up to 1.9.2
* Upgrade Vaadin to 7.2.5
* Upgrade Spring framework to 4.0.6.RELEASE
* Add the generic search module by using Solr
* Add permission review when user select role
* Simplify servlet error handler

**Bug Fixes**

* Add more tracked fields in version notification email.
* Fixed NPE when browser cookie is disabled.
* Fixed bug view render wrongly in some rare cases.
* Fixed overflow tooltip title if it is too long
* Minor bug fix of project activity stream
* Fixed confirm dialog body text size calculation wrongly
* Validate email fails in several email formats
* Fix bug view layout display wrongly in several cases
* Email misspelling issues


Version 4.3.1
-----------------------------
**Improvements**

* Upgrade Reflections to 0.9.9-RC2
* Upgrade Guava to 15.0
* Support Japanese in reporting
* Internalization reporting module
* Improve UIs
* Update mobile edition

**Bug Fixes**

* Can not save user in some special case
* Minor bug fixes in inviting project member
* Can not assign new role to project member
* Notification Dialog has useless header
* Minor UI bugs on IE
* Can not 'remember password' in IE


Version 4.3.0
-----------------------------
**Improvements**

* Upgrade Vaadin to 7.2.4
* Upgrade HirakiCP to 4.2.0
* Update mobile edition
* Improve Gantt chart display
* Add Simple view of phase
* Support Japanese localization
* Emails are localized per language
* Support lazy load view
* Allow user can select the default system language
* Focus in system stable by adding many unit tests and refactor the system

**Bug Fixes**

* Exception is thrown when user save customized view
* Exception is thrown when user save search result
* Can not save login credential in mobile edition
* Can not add new role or edit role name of existing item
* Save duplicate monitor items

Version 4.2.0
-----------------------------
**Improvements**

* Upgrade Vaadin to 7.2.2
* Upgrade Apache Camel to 2.13.1
* Refactor reporting engine and improve the output of report
* Adjust JVM fieldBuilder to avoid the PerGem error
* Update icons to make UI more intuitive
* Add welcome message for view that has empty retrieved items.
* Add push service in Vaadin to lazy load heavy bug on UI
* Remove border of buttons
* Texts of emails

**Bug Fixes**

* Invite and accept invitation causes NPE in several exceptional cases.
* Can not assign role to new user
* Several wrong link in emails
* Calendar style issue
* Can not save edit role
* Link of following ticket is wrong


Version 4.1.1
-----------------------------
**Improvements**

* Refactor tooltip generator for Project and CRM module
* Allow user choose create new item in read view
* Improve localization service, and move texts in localization files
* Upgrade Spring framework to 4.0.5.RELEASE
* Upgrade Jackrabbit to 2.8.0
* Upgrade Flyway to 3.0
* Upgrade Resteasy to 3.0.7
* Upgrade Vaadin to 7.2.0

**Bug Fixes**

* Align button and text on the top in read view
* Customized view display wrongly

Version 4.1.0
-----------------------------
**Improvements**

* Boot time of application is much faster than previous version in some OSes
* Email template of bug and bug are re-ordered fields more logically
* Cache spring service classes to init application context faster
* Support user localization.common
* Make install process easier for non-tech users
* Add gantt chart
* Upgrade Vaadin library to 7.1.15

**Bug Fixes**

* Application can not allow users upload big file size
* Fix NPE of email sending when user create then delete item before the email is sent out
* Fix date issue in email update of project milestone, and crm meeting
* Cut username display name if it is long too in bug dashboard
* Align user name center in standup report display
* Display empty string if user does not enter their last name

Version 4.0.0
-----------------------------
**Improvements**

* Redesign application user interface
* Redesign email template
* Dynamic query builder and let user can save query fieldBuilder for next search
* Upgrade MyCollab libraries to higher version
* Revise standup report and time logging feature
* Improve code structure and simplify email processing procedure

**Bug Fixes**

* Mass update in CRM module throws exception in several special cases
* Fix exception throw when user back to previous screen
* MyCollab cache views even when user sign out the site
* Upload file in Chrome display upload window two times
* Fix several wrong links in email
* Improve datasource configuration to avoid site open many connections if the site is under heavy load
* Fix database connection bottleneck in unit test cases if we run all batch tests

Version 3.0.0
-----------------------------
**Improvements**

* Upgrade UI architect from Vaadin 6 to Vaadin 7
* Refactor to share common codes for both web desktop and web mobile.
* Update icons for CRM types and tabsheet
* Update icons for Project types and tabsheet
* Make icons more consistent of size and color
* Add Convert Lead feature
* Add decision role field to Contact Opportunity relationship and add new edit view for this relationship
* Remove print button in all pages
* Add mobile implementation (not ready for production yet)
* Upgrade MyCollab libraries to higher version

**Bug Fixes**

* Mass update account, contact in CRM module throws exception
* Permission check in Case view is wrong
* Navigate previous and next project member throws exception
* Update user account throws exception
* Export risk, problem list throws exception
* Schedule runner can not send reports in some cases
* Display activities throws exception in some cases
* Invite member does not send when first name is null
* Do not display tooltip of lead and contact if first name is null
* Can not save search result in CRM module
* Fix spelling mistakes
