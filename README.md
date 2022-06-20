# Module3_REST

![image](https://user-images.githubusercontent.com/87644515/170667756-3a9ee545-86a8-44f5-b78f-06c844269b1c.png)
=======
![img.png](img.png)

This module is an extension of REST API Advanced module and covers following topics:

1. Spring Security framework
2. Oauth2 and OpenId Connect
3. JWT token

#### Application requirements

1. Spring Security should be used as a security framework.
2. Application should support only stateless user authentication and verify integrity of JWT token.
3. Users should be stored in a database with some basic information and a password.

User Permissions:

     - Guest:
        * Read operations for main entity.
        * Signup.
        * Login.
     - User:
        * Make an order on main entity.
        * All read operations.
     - Administrator (can be added only via database call):
        * All operations, including addition and modification of entities.

4. Get acquainted with the concepts Oauth2 and OpenId Connect
5. (Optional task) Use Oauth2 as an authorization protocol.
    a. OAuth2 scopes should be used to restrict data.
    b. Implicit grant and Resource owner credentials grant should be implemented.
6. (Optional task) It's allowed to use Spring Data. Requirement for this task - all repository (and existing ones) should be migrated to Spring Data.

## Demo
### Practical part

1. Generate for demo at least 
    a. 1000 users
    b. 1000 tags
    c. 10’000 gift certificates (should be linked with tags and users)
All values should look like more -or-less meaningful: random words, but not random letters 
2. Demonstrate API using Postman tool (prepare for demo Postman collection with APIs)  
3. (Optional) Build & run application using command line

### Theoretical part

Mentee should be able to answer questions during demo session.
