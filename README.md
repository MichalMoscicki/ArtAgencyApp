# ArtAgency App

This project provides a functional app for managing an art agency with back end in Java, 
and front end in JavaScript. 

(In the future, it will be containerized, and Docker images will be shared.)

# Technologies

BE:
- Java 18
- Maven
- Spring Boot
- Hibernate
- MySQL
- Lombok
- Mockito
- JUnit
- Integration Tests
- JSON Web Token 

FE:
- JavaScript
- React
- (Material UI - in progress)
- Redux
    
# Main functionalities

Unauthorized users are able to access the main page and log in 
(Authentication is managed by Spring Security - in progress).

Users are divided into two roles: USER and ADMIN. 
The program provides various functionalities based on the role:

NOT AUTHORIZED:
- Logging in
- Registration

USERS:
- Managing business contacts
  - Managing tasks, scheduling and adding attachments to them
  - Managing bands and musicians 
  - Managing music scores
  - Managing assignments for the music bands
- (Email automation - in progress)

ADMIN:
- (Assigning tasks to users - in progress)
- (Managing users - in progress)
- Exporting contacts
- Importing contacts - in progress

# API

Authentication (in progress)


BUSINESS CONTACT ENDPOINT

Properties:
- id (Long) - autogenerated by database.
- title (String) - must be not blank.
- description (String).
- alreadyCooperated (Boolean).
- events (List<Event>) - connected entities.
- contactPerson (List<contactPerson>) - connected entities.
- institution (List<institution>) - connected entities.

   
    GET api/v1/contacts     Returns page of ten contacts, ordered descending by update date.
     Optional Query Parameters:
    - `page` (integer): Specify the page number to retrieve a specific page of contacts. Defaults to page 1 if omitted.
    - `sortDir` (String): Specify the sorting direction. Can be "asc" or "desc". Defaults is descending if omitted.
    - `sortBy` (String): Specify the field used to sort contacts. Type in field name. Defaults is updated.
    
    GET api/v1/contacts/export-json    Exports all contacts as an JSON file. (Admin only).
    GET api/v1/contacts/{id}
    POST api/v1/contacts
    POST api/v1/contacts/import    Imports contacts form local JSON file. The JSON file should be included in 
    the request body with a key named "file", and the value should be the local file path.
    Response details:
    - timestamp;
    - savedConntacts - number of successfully saved contacts
    - dupliatedContacts - a list of duplicated contacts (if any).
    - contactsWithError:  a list of contacts that couldn't be saved, along with corresponding error messages (if any).

    PUT api/v1/contacts/{id}
    DELETE api/v1/contacts/{id}     Deletes contact and all connected data.



CONTACT PERSON ENDPOINT

Properties:
- id (Long) - autogenerated by database.
- firstName (String) - must be not blank.
- lastName (String) - must be not blank.
- role (String).
- email (String) - must be valid, can be null.
- phone (String) - must be valid ( ^(?:[\\+]?[(]?[0-9]{2}[)]?-?[0-9]{3}[-\\s\\.]?[0-9]{4,6})?$ ), can be null.


    GET /api/v1/contacts/{contactId}/contact-people
    GET /api/v1/contacts/{contactId}/contact-people/{contactPersonId}
    POST /api/v1/contacts/{contactId}/contact-people
    PUT /api/v1/contacts/{contactId}/contact-people/{contactPersonId}
    DELETE /api/v1/contacts/{contactId}/contact-people/{contactPersonId}

EVENT ENDPOINT

Properties:
- id (Long) - autogenerated by database.
- firstName (String) - must be not blank.
- description (String) - max size 500 characters.
- monthWhenOrganised (int)- number from 1 to 12. 


    GET /api/v1/contacts/{contactId}/events/{eventId}
    POST /api/v1/contacts/{contactId}/events
    PUT /api/v1/contacts/{contactId}/events/{eventId}
    DELETE /api/v1/contacts/{contactId}/events/{eventId}


INSTITUTION ENDPOINT

Properties:
- id (Long) - autogenerated by database.
- name (String) - must be not blank.
- city (String) - must be not blank.
- category (String) - must be not blank.
- notes (String) - max size 500 characters.
- webPage (String)
- email (String) - must be valid, can be null.
- phone (String) - must be valid ( ^(?:[\\+]?[(]?[0-9]{2}[)]?-?[0-9]{3}[-\\s\\.]?[0-9]{4,6})?$ ), can be null.


    POST /api/v1/contacts/{contactId}/institutions
    GET /api/v1/contacts/{contactId}/institutions/{eventId}
    PUT /api/v1/contacts/{contactId}/institutions/{eventId}
    DELETE /api/v1/contacts/{contactId}/institutions/{eventId}


TASK ENDPOINT

Properties:
- id (Long) - autogenerated by database.
- title (String) - must be not blank.
- description (String).
- active (boolean).
- finished (boolean) - if task is finished, it cannot be active.
- activationDate (LocalDate) - date, when "active" field will be automatically set to "true".
- updated (Date).
- priority (int) - number from 1 to 3.
- taskAttachment - each task can only have one attachment.


    GET api/v1/tasks  Returns page of ten tasks, ordered descending by update date.
    Optional Query Parameters:
    - `page` (integer): Specify the page number to retrieve a specific page of tasks. Defaults to page 1 if omitted.
    - `sortDir` (String): Specify the sorting direction. Can be "asc" or "desc". Defaults is descending if omitted.
    - `sortBy` (String): Specify the field used to sort tasks. Type in field name. Defaults is updated.
    - `status` (String): Filter tasks based on their status (case insensitive). Possible status: active, future, finished, all. 
    Retunrs all tasks if omitted.

    GET api/v1/tasks/{taskId}
    POST api/v1/tasks  
    PUT api/v1/tasks/{taskId}
    DELETE api/v1/tasks/{taskId} Deletes also attachemnts.

TASK ATTACHMENT ENDPOINT

Properties:
- id (Long) - autogenerated by database.
- contacts (Set<Contacts>).

    
    POST api/v1/tasks/{taskId}/attachments
    GET api/v1/tasks/{taskId}/attachments/{attachmentId}
    PUT api/v1/tasks/{taskId}/attachments/{attachmentId}
    DELETE api/v1/tasks/{taskId}/attachments/{attachmentId}


INSTRUMENTS ENDPOINT

- id (Long) - autogenerated by database.
- name (String).


    POST api/v1/instruments
    GET api/v1/instruments Returns a list on instruments.
    GET api/v1/instruments/{intrumentId}
    PUT api/v1/instruments/{intrumentId}
    DELETE api/v1/instruments/{intrumentId}

MUSICIANS ENDPOINT

- id (Long) - autogenerated by database.
- firstName (String) - must not be blank.
- lastName (String) - must not be blank.
- notes (String) - max 500 characters.
- phone (String) - must be valid ( ^(?:[\\+]?[(]?[0-9]{2}[)]?-?[0-9]{3}[-\\s\\.]?[0-9]{4,6})?$ ), can not be null.
- email (String) - must be valid, can not be null.
- instruments (List<Instruments>) - list of Instruments object.



    POST api/v1/musicians
    GET api/v1/musicians Returns a list on instruments.
    Optional Query Parameters:
    - `page` (integer): Specify the page number to retrieve a specific page of musicians. Defaults to page 1 if omitted.
    - `sortDir` (String): Specify the sorting direction. Can be "asc" or "desc". Defaults is descending if omitted.
    - `sortBy` (String): Specify the field used to sort. Type in field name. Defaults is lastName.
    - `instrument` (String): Filter musicians based on their instruments (case insensitive). Returns list of all musicians if omitted. 

    GET api/v1/musicians/{musicianId}
    PUT api/v1/musicians/{musicianId}
    DELETE api/v1/musicians/{musicianId} - deletes musician without connected data.


SONGS ENDPOINT

- id (Long) - autogenerated by database.
- title (String) - must not be blank.
- description (String) - max 500 characters.
- composers (String).
- textAuthors (String).
- parts (List<Part>) - list of Part object.


    POST api/v1/songs
    GET api/v1/songs Returns a list on instruments.
    Optional Query Parameters:
    - `page` (integer): Specify the page number to retrieve a specific page of songs. Defaults to page 1 if omitted.
    - `sortDir` (String): Specify the sorting direction. Can be "asc" or "desc". Defaults is descending if omitted.
    - `sortBy` (String): Specify the field used to sort. Type in field name. Defaults is title.
    - `title` (String): Filter songs based on their title (case insensitive). Returns list of all songs if omitted. 

    GET api/v1/musicians/{songsId}
    PATCH api/v1/musicians/{songsId}
    DELETE api/v1/musicians/{songsId} - deletes songs with connected parts.


PARTS ENDPOINT

- id (Long) - autogenerated by database.
- name (String) - autogenerated by database (the same as file name).
- type (String) - autogenerated by database (the same as file type).
- instrument (String) - autogenerated by database.
- url (String) - autogenerated by database (link dor downloading file).

    
    POST /api/v1/songs/{songId}/parts - stores part in database and connect it with song and instrument.
    Request body should be multipart/form-data and include two keys:
    - "file" : the value should be the local file path.
    - "instrument" " the value should be an exsisting in DB instrument object (id, name).
    Instruments in song must be unique - if violated, endpoint returns resourceAlreadyexistsException.
    GET /api/v1/parts/{partId} - downloads part file.
    DELETE /api/v1/songs/{songId}/parts/{partId} - removes part form song adn deletes it form database.


(Other endpoints will be provided with further development).

ERROR HANDLING 

If request cause an error, response will contain:
- timestamp
- custom message
- status code


Screenshots FE (work in progress):

Login form:
![login.png](screenshots%2Flogin.png)

Musicians component with pagination and sorting:
![musiciansSorting.png](screenshots%2FmusiciansSorting.png)

Musicians add and update form (same component with different prop values):
![musiciansAddAndUpdateForm.png](screenshots%2FmusiciansAddAndUpdateForm.png)

Instruments form:
![instrumentsForm.png](screenshots%2FinstrumentsForm.png)

