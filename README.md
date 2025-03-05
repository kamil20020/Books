# Books
I am still working on this project, so it may seem like unfinished yet.

Basic crud web app to manage basic data related to books (very simplified). The user will manage users, roles, books, authors, publishers and relations between them.
Functional and nonfunctional requirements:
* Implementation of authentication and authorization based on JWT from scratch with both access and refresh tokens. I also implemented revocating of refresh tokens. Revocated refresh tokens are stored in the additional table. I assumed that token is revocated when backend checked, that it is expired or when a user log out. Backend checks if there are expired tokens every 24 hours,
* An admin can view and manage data about users and roles e.g. creating accounts and assigning roles to users
* A unlogged users can view books, authors and publishers,
* A logged user can view and manage books, authors and publishers.

Technologies:
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
