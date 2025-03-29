---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a MainWindow that is made up of several parts:
- Command Interface: CommandBox, ResultDisplay
- List Panels:
    - `PersonListPanel` with `PersonCard` and `PreferenceListPanel` with `PreferenceCard`
    - `ListingListPanel` with `ListingCard` and `OwnerListPanel` with `OwnerCard`
    - `TagListPanel` 
- Utility Components: `HelpWindow`, `StatusBarFooter`

All these components, including the `MainWindow`, `inherit` from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("deletePerson 1")` API call as an example.

<puml src="diagrams/DeletePersonSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `deletePerson 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeletePersonCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.

</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeletePersonCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeletePersonCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddPersonCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddPersonCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddPersonCommandParser`, `DeletePersonCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />

**Note:** The attributes of `Person`, `Tag`, `PriceRange` that have been promoted to a class are abstracted into separate class diagrams for ease of reading.

The `Model` component,

* stores the match estate data composed of `Person`, `PropertyPreference`,  `Listing` and `Tag` and `PriceRange` objects.
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the currently 'selected' `PropertyPreference` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<PropertyPreference>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the currently 'selected' `Listing` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Listing>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the currently 'selected' `Tag` objects (e.g., results of a search query) as a separate _filtered_ set which is exposed to outsiders as an unmodifiable `ObservableList<Tag>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

`Person`

<puml src="diagrams/PersonAttributesClassDiagram.puml" width="450" />

* Stores person-related data as `Person` objects contained in a `UniquePersonList` object.
* Each `Person` may have multiple`PropertyPreference` and `Listing` associations.
* Each `Person` is identified their phone number.

</box>

<box type="info" seamless>

`PropertyPreference`

* Each `PropertyPreference` may have a `PriceRange`, and multiple `Tag` associations.
**Note:** A `PropertyPreference` can only be tied to 1 `Person` to group criterias for a property that are looking for under a representative person responsible for the purchase.

</box>

<box type="info" seamless>

`Listing`

<puml src="diagrams/ListingAttributesClassDiagram.puml" width="450" />

* Stores listing-related data as `Listing` objects contained in a `UniqueListingList` object.
* Each `Listing` may have a `PriceRange`, and multiple `Tag`  and `Owner` associations.
* Each `Listing` should have either a `Unit Number` or a `House Number` but not both.
* Each `Listing` is identified by a combination of the `Postal Code` and either the `Unit Number` or the `House Number` depending on which one is present.
**Note:** A `Listing` can have multiple `Person` to reflect co-ownership scenarios. Additionally in certain cases joint approval of a listing is required for a purchase.

</box>

<box type="info" seamless>

`PriceRange`

<puml src="diagrams/PriceRangeAttributesClassDiagram.puml" width="450" />

* Each `PriceRange` may have a lower bound price and a upper bound price.
* If neither bound is specified, the `PriceRange` is unbounded.

</box>

<box type="info" seamless>

`Tag`
* Stores tag-related data as `Tag` objects contained in a `TagRegistry` object.
* Each `Tag` may have multiple `Listing`  and `PropertyPreference` associations.
* Each `Tag` is identified by a string tag name.
**Note:** An arguably better representation of Tag is to split the tag that stores the association from the tag name, promoting the tag name into a class.

<puml src="diagrams/BetterTagAttributesClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Edit Tag feature

The Edit Tag feature allows users to edit the name of an existing tag. This feature is useful when users want to change the name of a tag that they have already created. The following sequence diagram shows how the Edit Tag feature works:

<puml src="diagrams/EditTagSequenceDiagram.puml" alt="EditTagSequenceDiagram" />

The associated Listings and PropertyPreferences will be updated to reflect the new tag name.

### \[Proposed\] Edit Listing feature

The Edit Listing feature allows users to edit the details of an existing listing. This feature is useful when users want to update information such as postal code, unit/house number, price range, or property name of a listing. The following sequence diagram shows how the Edit Listing feature works:

<puml src="diagrams/EditListingSequenceDiagram.puml" alt="EditListingSequenceDiagram" />

The edit operation will update the listing's details while maintaining its existing tags and availability status. The system ensures that no duplicate listings can be created by checking the postal code and unit/house number combination.

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

### Target User Profile:

**Profile**
Tim is a busy, independent real estate agent working in Singapore, managing multiple buyers and sellers simultaneously.

He often communicates with clients through messaging applications and understands their preferences and specifications from chat logs. During client meetups, open houses, and property tours, he may also take notes on common likes and dislikes among clients.

He records these details in an Excel sheet on his laptop, sometimes on the fly, and is highly experienced in fast typing.

Tim has noticed that many client requests can be matched with specific listings he has, but manually filtering and matching these requests with listings can be cumbersome.

**Work Patterns**

- Primarily works alone but may collaborate with other agents to gain information on additional listings, so he needs to send and receive listing and client information easily.
- Commonly works with clients, many of whom have preferences that overlap with those of other clients.
- Uses both a laptop and a smartphone for work.
- Prefers typing and, by extension, CLI tools for speed and efficiency, as he frequently needs to quickly note down client preferences.
- Regularly shares the tools he uses with other agents he is familiar with.
- Struggles to manage client details across messaging apps and spreadsheets.
- Needs to match buyers with sellers quickly and accurately.
- Works on the go, handling leads from different sources.
- Wants to keep client contacts separate from personal contacts for **PDPA** compliance.
- Prefers offline tools due to security concerns regarding potential customer data leaks.
- Also favors offline tools due to the possibility of an unstable internet connection.
- Does not use **CRM** or organizational tools, as they are beyond his job scope as an independent real estate agent.

### Value Proposition:
Real estate agents often struggle to manage buyers and sellers through messaging apps.
MatchEstate allows tracking of buyers and sellers easily as well as their preferences and offerings respectively.
It enables fast matching of buyers and sellers. It is tailored for those who prefer CLIs.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority  | As a …​                                                        | I want to …​                                                                                      | So that I can…​                                                                               |
|-----------|----------------------------------------------------------------|---------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| `* *`     | potential user                                                 | see a list of all available commands on startup                                                   | quickly learn how to use the app                                                              |
| `* *`     | forgetful real estate agent                                    | view a help menu                                                                                  | reference how to use the system effectively                                                   |
| `* *`     | potential user                                                 | have the application come preloaded with sample data                                              | easily try all the commands before adding my real clients                                     |
| `* *`     | new user                                                       | clear the sample data                                                                             | populate the application with actual data                                                     |
| `* *`     | real estate agent                                              | clear all data                                                                                    | reset the system when necessary                                                               |
| `* * *`   | new user                                                       | add my previously accumulated buyer and seller profile information                                | populate the data in the app with my previously recorded data                                 |
| `* * *`   | new user                                                       | remove the app's default tags that are created                                                    | eventually replace it with my own personalized tags                                           |
| `* * *`   | real estate agent                                              | add buyer information                                                                             | manage my buyers easily                                                                       |
| `* * *`   | real estate agent                                              | add seller information                                                                            | manage my sellers easily                                                                      |
| `* * *`   | real estate agent                                              | view all clients                                                                                  | easily track the clients I have                                                               |
| `* *`     | real estate agent                                              | edit a person's details                                                                           | update their information when needed                                                          |
| `* * *`   | real estate agent                                              | delete a client                                                                                   | only track relevant clients                                                                   |
| `* *`     | real estate agent                                              | find a client by name                                                                             | quickly retrieve the details of a specific client                                             |
| `* * *`   | real estate agent                                              | add property information                                                                          | manage my listings easily                                                                     |
| `* * *`   | real estate agent                                              | add a property to a person                                                                        | track how many properties they are selling                                                    |
| `* * *`   | real estate agent                                              | add a property to a person                                                                        | track the property's current seller                                                           |
| `* * *`   | real estate agent                                              | add the preferences of a person                                                                   | track what type of properties they are interested in purchasing                               |
| `* * *`   | real estate agent                                              | add more than one property preference                                                             | simultaneously track multiple distinct properties a client is interested in purchasing        |
| `* * *`   | real estate agent                                              | add tags to a property preference                                                                 | categorize a person's buying interests more effectively                                       |
| `* * *`   | real estate agent                                              | remove a tag from a property preference                                                           | adjust my information to match a person's interests as they change                            |
| `* *`     | real estate agent                                              | overwrite tags in a property preference                                                           | completely update a person's buying criteria as necessary                                     |
| `* * *`   | real estate agent                                              | delete a property preference from a person                                                        | remove no longer relevant buying interests from the systemy                                   |
| `* * *`   | real estate agent                                              | add a property                                                                                    | track the information in the system                                                           |
| `* * *`   | real estate agent                                              | view all listings                                                                                 | easily track the properties that are listed for sale                                          |
| `* * *`   | real estate agent                                              | add tags to a property                                                                            | categorize the property by its key details                                                    |
| `* * *`   | careless real estate agent                                     | remove a tag from a property                                                                      | correct the details if necessary                                                              |
| `* *`     | careless real estate agent                                     | overwrite tags in a property                                                                      | completely update its categorization if necessary                                             |
| `* * *`   | real estate agent                                              | delete a property                                                                                 | remove no longer relevant listings from the system                                            |
| `* * *`   | real estate agent                                              | mark a property as unavailable                                                                    | ensure that listings that are on hold are no longer considered for matching                   |
| `* * `    | real estate agent                                              | mark a property as available when the listing is no longer on hold                                | ensure that listings that are no longer on hold can be considered for matching.               |
| `* *   `  | real estate agent                                              | match a person with properties based on the number of common tags and budget                      | quickly find listings that fit their preferences                                              |
| `* *  `   | real estate agent                                              | match a property with potential buyers based on the number of common tags and their budget        | identify interested persons efficiently.                                                      |
| `* * `    | real estate agent                                              | filter properties based on a person's preferences                                                 | provide targeted property recommendations.                                                    |
| `* * * `  | real estate agent                                              | add custom tags                                                                                   | customize the categorization of property preferences and properties according to my workflow. |
| `* * *  ` | real estate agent                                              | add custom tags                                                                                   | capture niche preferences (such as pet-friendly).                                             |
| `* * * `  | real estate agent                                              | list all tags                                                                                     | see the available categories in the system.                                                   |
| `* * * `  | real estate agent                                              | delete a tag                                                                                      | remove outdated and unnecessary categorization from the system.                               |
| `* * *  ` | careless real estate agent                                     | delete a tag                                                                                      | remove tags that I have mistakenly added.                                                     |
| `* *   `  | real estate agent                                              | search for properties by tags                                                                     | quickly find relevant listings.                                                               |
| `* *  `   | real estate agent                                              | search for persons by tags                                                                        | find buyers based on their property preferences                                               |
| `*   `    | real estate agent                                              | attach notes to my clients                                                                        | keep track of specific client requirements beyond common tags                                 |
| `* *   `  | real estate agent                                              | update my buyer's information when new information is available                                   | ensure that my information remains accurate and up to date                                    |
| `* *   `  | real estate agent                                              | update my seller's information when new information is available                                  | ensure that my information remains accurate and up to date                                    |
| `* *   `  | real estate agent                                              | update my listing's information when new information is available                                 | ensure that my information remains accurate and up to date.                                   |
| `*     `  | inexperienced user                                             | undo accidental deletions or edits                                                                | revert mistakes quickly                                                                       |
| `* *   `  | real estate agent                                              | filter listings based on price range                                                              | focus on properties within the buyer's budget.                                                |
| `*     `  | real estate agent                                              | be able to list properties that clients have already toured                                       | avoid re-touring them on the same listing by accident.                                        |
| `*     `  | real estate agent                                              | be able to add properties that the clients liked                                                  | keep track of the properties that they are interested in.                                     |
| `*     `  | independent real estate agent                                  | be able to export a list of buyers                                                                | share them with other agents for collaboration.                                               |
| `*     `  | frequent user                                                  | archive buyers and sellers who are inactive                                                       | reduce clutter when matching/searching.                                                       |
| `*     `  | efficient user who may not have access to the app at all times | write bulk commands in a text file to add, edit or delete client details at the end of the day    | update information without having the application on hand.                                    |
| `*     `  | real estate agent                                              | view the status of a deal (e.g., Inquiry, Viewing, Offer, Closed)                                 | understand the deal's progression at a glance.                                                |
| `*     `  | real estate agent                                              | be able to create backups and restore data via CLI commands                                       | do not risk losing client information in the event of data corruption.                        |
| `*     `  | real estate agent                                              | be able to view a log of all commands executed                                                    | review my past commands.                                                                      |
| `*     `  | power user                                                     | create custom command aliases (e.g., mb for match --batch)                                        | customize the commands to my workflow and work more efficiently.                              |
| `*     `  | real estate agent                                              | store notes for each deal                                                                         | keep track of important discussions.                                                          |
| `*     `  | real estate agent                                              | create a simple summary of all buyers and listings                                                | use it for basic data analytics purposes and understand my portfolio demographic.             |
| `*     `  | real estate agent                                              | auto-generate templated messages for matched buyers and sellers and export them to WhatsApp/Email | ensure that the process of writing electronic direct mails is simplified.                     |


### Use cases

(For all use cases below, the **System** is the `MatchEstate` and the **Actor** is the `user`, unless specified otherwise)

#### Use case: UC01 - Add a person

**MSS**

1.  User requests to <u>list persons (UC02)</u>
2.  User specifies a person to add
3.  MatchEstate adds the person
4.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The specified person is invalid.

    * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2b. The specified person already exists.

    * 2b1. MatchEstate shows an error message.

      Use case ends.

#### Use case: UC02 - List persons

**MSS**

1.  User requests to list persons
2.  MatchEstate shows a list of persons
3.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 1a. The list is empty.

    * 1a1. MatchEstate displays a message for an empty list.

      Use case resumes at 3.

#### Use case: UC03 - Delete a person by index

**MSS**

1.  User requests to <u>list persons (UC02)</u>
2.  User requests to delete a specific person by index
3.  MatchEstate deletes the person's property preferences
4.  MatchEstate updates the usage number of tags used by the person's property preferences
5.  MatchEstate deletes ownership of property for all of the person's properties
6.  MatchEstate deletes the person
7.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The index is invalid.

    * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2a. Person has no property preferences.

  Use case resumes at 5.

* 3a. None of the person's property preferences have tags.

  Use case resumes at 5.

* 4a. Person has no properties.

  Use case resumes at 6.

#### Use case: UC04 - Add property to person

**MSS**

1.  User requests to <u>list persons (UC02)</u>
2.  User requests to <u>list properties (UC11)</u>
3.  User requests to assign property to a specific person by index
4.  MatchEstate updates ownership of property for that person
5.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The person index is invalid.

    * 3a1. MatchEstate shows an error message.

  Use case ends

* 3b. The property index is invalid.

    * 3b1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC05 - Delete property from person

**MSS**

1.  User requests to <u>list properties (UC11)</u>
2.  User requests to unassign a property from a specific person by index
3.  MatchEstate updates ownership of property for that person
4.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The property index is invalid.

    * 2a1. MatchEstate shows an error message.

  Use case ends

* 2b. The owner index is invalid.

    * 2b1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC06 - Add property preference to person

**MSS**

1.  User requests to <u>list persons (UC02)</u>
2.  User requests to <u>list tags (UC16)</u>
3.  User requests to add a property preference to a specific person by index
4.  MatchEstate creates new tags
5.  MatchEstate updates the usage number of the tags
6.  MatchEstate adds a property preference to the person
7.  MatchEstate displays a success message

    Use case ends.

**Extensions**

* 3a. The person index is invalid.

    * 3a1. MatchEstate shows an error message.

  Use case ends

* 3b. Necessary fields (price range or a tag) not specified.

    * 3b1. MatchEstate shows an error message.

  Use case ends.

* 3c. Tag to be made already exists.

    * 3c1. MatchEstate shows an error message.

  Use case ends.

* 3d. Tag specified does not  exist.

    * 3d1. MatchEstate shows an error message.

  Use case ends.

* 3e. No new tags to be created.

  Use case resumes at 5

* 3f. No existing and new tags specified.

  Use case resumes at 6

#### Use case: UC07 - Add tag to property preference

**MSS**

1.  User requests to <u>list persons (UC02)</u>
2.  User requests to <u>list tags (UC16)</u>
3.  User requests to add a tag to a specific property preference by index
4.  MatchEstate creates new tags
5.  MatchEstate updates the usage number of the tags
6.  MatchEstate updates the property preference
7.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The person index is invalid.

    * 3a1. MatchEstate shows an error message.

  Use case ends

* 3b. The property preference index is invalid.

    * 3b1. MatchEstate shows an error message.

  Use case ends.

* 3c. Tag to be made already exists.

    * 3c1. MatchEstate shows an error message.

  Use case ends.

* 3d. Tag specified does not exist.

    * 3d1. MatchEstate shows an error message.

  Use case ends.

* 3e. No new tags to be created.

  Use case resumes at 5

* 3f. No existing and new tags specified.

  Use case resumes at 6

#### Use case: UC08 - Delete tag from property preference

**MSS**

1.  User requests to <u>list persons (UC02)</u>
2.  User requests to <u>list tags (UC16)</u>
3.  User requests to delete a tag from a specific property preference by index
4.  MatchEstate updates the usage number of the tags
5.  MatchEstate updates the property preference
6.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The person index is invalid.

    * 3a1. MatchEstate shows an error message.

  Use case ends

* 3b. The property preference index is invalid.

    * 3b1. MatchEstate shows an error message.

  Use case ends.

* 3c. The tag does not exist.

    * 3c1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC09 - Delete property preference

**MSS**

1.  User requests to <u>list persons(UC02)</u>
2.  User requests to delete a property preference from a specific person by index
3.  MatchEstate deletes the person's property preferences
4.  MatchEstate updates the usage number of tags used by the person's property preferences
5.  MatchEstate displays a success message

    Use case ends.

**Extensions**

* 2a. The person index is invalid.

    * 2a1. MatchEstate shows an error message.

  Use case ends

* 2b. The preference index is invalid.

    * 2b1. MatchEstate shows an error message.

  Use case ends.

* 3a. None of the person's property preferences have tags.

  Use case resumes at 5.

#### Use case: UC010 - Add a property

**MSS**

1.  User requests to <u>list property (UC11)</u>
2.  User requests to <u>list tags (UC16)</u>
3.  User specifies a property to add
4.  MatchEstate adds the property
5.  MatchEstate creates new tags
6.  MatchEstate updates the usage number of the tags
7.  MatchEstate updates the property's tags
8.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The specified property is invalid.

    * 3a1. MatchEstate shows an error message.

  Use case ends.

* 3b. The specified property already exists.

    * 3b1. MatchEstate shows an error message.

      Use case ends.

* 3c. Tag to be made already exists.

    * 3c1. MatchEstate shows an error message.

  Use case ends.

* 3d. Tag specified does not exist.

    * 3d1. MatchEstate shows an error message.

  Use case ends.

* 4a. No new tags to be created.

  Use case resumes at 6.

* 4b. No existing and new tags specified.

  Use case resumes at 8

#### Use case: UC11 - List property

**MSS**

1.  User requests to list property
2.  MatchEstate shows a list of properties
3.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 1a. The list is empty.

    * 1a1. MatchEstate displays a message for an empty list.

      Use case resumes at 3.

#### Use case: UC12 - Add tag to property

**MSS**

1.  User requests to <u>list property(UC11)</u>
2.  User requests to <u>list tags (UC16)</u>
3.  User requests to add a tag to property
4.  MatchEstate creates new tags
5.  MatchEstate updates the usage number of the tags
6.  MatchEstate updates the property's tags
7.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The property index is invalid.

    * 3a1. MatchEstate shows an error message.

  Use case ends

* 3b. Tag to be made already exists.

    * 3b1. MatchEstate shows an error message.

  Use case ends.

* 3c. Tag specified does not exist.

    * 3c1. MatchEstate shows an error message.

  Use case ends.

* 3d. No new tags to be created.

  Use case resumes at 5

* 3e. No existing and new tags specified.

    * 3e1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC13 - Delete tag from property

**MSS**

1.  User requests to <u>list property (UC11)</u>
2.  User requests to <u>list tags (UC16)</u>
3.  User requests to delete a tag from a specific property by index
4.  MatchEstate updates the usage number of the tags
5.  MatchEstate updates the property
6.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The property index is invalid.

    * 3a1. MatchEstate shows an error message.

  Use case ends

* 3b. The tag does not exist.

    * 3b1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC14 - Delete property

**MSS**

1.  User requests to <u>list property (UC11)</u>
2.  User requests to delete a property by index
3.  MatchEstate deletes the property
4.  MatchEstate updates the usage number of tags used by the property
5.  MatchEstate updates the property list of all its owners
6.  MatchEstate displays a success message

    Use case ends.

**Extensions**

* 2a. The property index is invalid.

    * 2a1. MatchEstate shows an error message.

  Use case ends.

* 3a. The property does not have tags.

  Use case resumes at 5.

* 3b. The property is not assigned to any person

  Use case resumes at 6.

#### Use case: UC15 - Add a tag

**MSS**

1.  User requests to <u>list tags (UC16)</u>
2.  User specifies new tags to create
3.  MatchEstate creates new tags
4.  MatchEstate updates the usage number of new tags
5.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. Tag to be made already exists.

    * 2a1. MatchEstate shows an error message.

  Use case ends.


#### Use case: UC16 - List tag

**MSS**

1.  User requests to list tag
2.  MatchEstate shows a list of tags
3.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 1a. The list is empty.

    * 1a1. MatchEstate displays a message for an empty list.

      Use case resumes at 3.

#### Use case: UC17 - Delete tag

**MSS**

1.  User requests to <u>list tags (UC16)</u>
2.  User requests to delete a tag
3.  MatchEstate deletes the tag
4.  MatchEstate updates the property preferences with that tag
5.  MatchEstate updates the properties with that tag
6.  MatchEstate displays a success message

    Use case ends.

**Extensions**

* 2a. Tag specified does not exist.

    * 2a1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC18 - Viewing help

**MSS**

1.  User requests to view list of commands
2.  MatchEstate displays help window
3.  MatchEstate displays a success message

    Use case ends.

#### Use case: UC19 - Exit MatchEstate

**MSS**

1.  User requests to exit MatchEstate
2.  MatchEstate closes

    Use case ends.

*{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to support at least 500 buyers and 500 sellers without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The user interface should be intuitive enough that non-tech savvy users can use it.
5. Should complete all user commands within 2 seconds.
6. Should automatically save data to prevent data loss.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Listing**: Entry of a property that is currently on the market. Contains property address information. Used interchangeably with Property.
* **Property**: The physical real estate itself. Used interchangeably with Listing.
* **Property Preference**: The preference of people based on price range and tags.
* **Tag Registry**: A Singleton used to store Tags to be put on properties and property preferences.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
