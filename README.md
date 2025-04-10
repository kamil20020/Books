# Books
Basic crud web app to manage basic data related to books (very simplified). The user will manage users, roles, books, authors, publishers and relations between them.

Parts of the system e.g. frontend and backnd are independently developed on individual branches. The main branch has all the code from the project.

## Running app
I propose to run this app by using command `docker-compose up` in the directory with the docker-compose.yml file. To use this command you need to have Docker (e.g. Docker Desktop on Windows) installed on your
computer and Docker Compose will be installed with it.

Frontend will be available on the http://localhost:80.

Backend will be available on the http://localhost:9000 and you can access swagger (api doc and you can try to send some requests on this page)
from the http://localhost:9000/swagger-ui/index.html. From swagger you can send requests to public endpoints, mainly gets from authors, books and publishers.
It is that, because I didn't configure jwt in swagger yet, but it is possible because of extension based nature of swagger.
I propose to use Postman to have possibility to test all endpoints of the app.

On the start of the app should be created few accounts. These accounts can be used to quickly test the app main functions.

Created accounts details:
| Role      | Admin    | Normal user |
|-----------|----------|-------------|
| Username  | admin    | kamil       |
| Password  | admin123 | nowak       |


## Functional requirements
* Register,
* Login,
* Logout,
* Users:
  * Search,
  * Add,
  * Remove,
  * View roles,
  * Grant/revoke roles.
* Roles:
  * View,
  * Add,
  * Remove.
* Books:
  * Search,
  * Simple and detailed views,
  * Add,
  * Edit,
  * Remove.
* Authors:
  * View,
  * Add,
  * Remove,
  * View author's books.
* Publishers:
  * View,
  * Add,
  * Remove,
  * View publisher's books,
  * View publisher's authors.

## Functional requirements for actors:
* A logged user can view and manage books, authors and publishers,
* A logged user can additionally add and edit books, add authors and add publishers,
* An admin can additionally view and manage data about users and roles e.g. creating accounts and assigning roles to users.

## Nonfunctional requirements:

### General:
* Test backend by unit and integration tests in close to 100% test coverage.

### Auth:
* Auth and authorization based on JWT - from scratch in Spring Security with both access and refresh tokens,
* Logout by revoking refresh tokens - revocated refresh tokens are stored in the additional table. I assumed that token is revocated when backend have checked, that the token is expired or when the user log out. Backend checks if there are expired tokens every 24 hours,

### Technologies:
* Backend:
  * REST,
  * Java,
  * Spring,
  * Spring Boot,
  * Spring Data JPA,
  * Spring Security,
  * Testcontainers.
* Frontend:
  * React,
  * Redux Toolkit,
  * Context Api. 
* DevOps:
  * Docker,
  * Docker Compose,
  * Kubernetes,
  * Helm (I don't know very much about this technology yet)
  * ArgoCD,
  * Github Actions.
* Database - PostgreSQL.

## ERD
<p align="center">
    <img src="project/erd.png">
<p>

## API
<p align="center">
    <img src="swagger/swagger-1.png">
<p>

<p align="center">
    <img src="swagger/swagger-2.png">
<p>

<p align="center">
    <img src="swagger/swagger-3.png">
<p>

## Screenshots

### Page layout
Header, content and footer:
<p align="center">
    <img src="screenshots/layout.png">
<p>

The app is responsive to about 500px width:
<p align="center">
    <img src="screenshots/responsive-responsive.png">
<p>

Open menu:
<p align="center">
    <img src="screenshots/responsive-menu.png">
<p>

### Login:
Login:
<p align="center">
    <img src="screenshots/login.png">
<p>

Success login:
<p align="center">
    <img src="screenshots/login-1.png">
<p>

### Users
Users view:
<p align="center">
    <img src="screenshots/users/users.png">
<p>

Add user:
<p align="center">
    <img src="screenshots/users/add-user.png">
<p>

Assign role to user:
<p align="center">
    <img src="screenshots/users/assign-role.png">
<p>

Edit user:
<p align="center">
    <img src="screenshots/users/edit-user.png">
<p>

Delete user:
<p align="center">
    <img src="screenshots/users/remove-user.png">
<p>


### Roles:
Roles view:
<p align="center">
    <img src="screenshots/roles/roles.png">
<p>

Add role:
<p align="center">
    <img src="screenshots/roles/add-role.png">
<p>


### Authors:
Authors view:
<p align="center">
    <img src="screenshots/authors/authors.png">
<p>

Add author:
<p align="center">
    <img src="screenshots/authors/add-author.png">
<p>

Author's books:
<p align="center">
    <img src="screenshots/authors/author-books.png">
<p>


### Books:
Books view:
<p align="center">
    <img src="screenshots/books/books.png">
<p>

Book's details:
<p align="center">
    <img src="screenshots/books/book-details.png">
<p>

Add book (1/2):
<p align="center">
    <img src="screenshots/books/add-book.png">
<p>

Add book (2/2):
<p align="center">
    <img src="screenshots/books/add-book-1.png">
<p>

Edit book:
<p align="center">
    <img src="screenshots/books/edit-book.png">
<p>


### Publishers:
Publishers view:
<p align="center">
    <img src="screenshots/publishers/publishers.png">
<p>

Add publisher:
<p align="center">
    <img src="screenshots/publishers/add-publisher.png">
<p>

Publisher's authors:
<p align="center">
    <img src="screenshots/publishers/publisher-authors.png">
<p>

Publisher's books:
<p align="center">
    <img src="screenshots/publishers/publisher-books.png">
<p>


### General details:
Almost all data returned from backend are paged:
<p align="center">
    <img src="screenshots/pagination.png">
<p>

Operations create notifications:
<p align="center">
    <img src="screenshots/notification.png">
<p>

Most important forms are validated:
<p align="center">
    <img src="screenshots/validation.png">
<p>

Before deletes, there is required confirmation:
<p align="center">
    <img src="screenshots/delete-confirmation.png">
<p>




