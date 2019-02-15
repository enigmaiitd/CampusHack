# Library Portal
Library Portal is an Android app to show the list of currently issued books from IIT Library, Delhi.

## Download
[Click here to download the latest release!](https://github.com/enigmaiitd/CampusHack/releases/latest)

## About EnigmaIITD
We are a team of undergraduates that aim to use our development skills to develop apps that could help solve
problems people might face and ease their work through software.

## Workflow
- We need the user to enter their IIT Delhi LDAP User ID and password, which is used to fetch their email messages
- Only the relevant emails from Library containing Issue/Return info are fetched
- These emails are then parsed to create meaningful objects containing the book name, accession number, and most importantly, the Due date.
- These objects are stored in a local database to allow quick retrieval of info.
- On further runs of the app, only the new emails are fetched from the server and rest of the info is taken from the local database.
- This info is displayed in a convenient list, which user can see to determine which books due date is near

## Features
- Forget the hassle of checking your email again and again to see which book is due when
- See all the consolidated info in one place
- Minimal network usage as only the new, required emails are fetched

## Future Plans
- Send reminder to user at their preferred time when due date is approaching
- Once data has been fetched, show information offline
- Sync book information from mail in background if required
