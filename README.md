# Books
I am still working on this project, so it may seem like unfinished yet.

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
    <img src="swagger/api-1.png">
<p>

<p align="center">
    <img src="swagger/api-2.png">
<p>

<p align="center">
    <img src="swagger/api-3.png">
<p>
