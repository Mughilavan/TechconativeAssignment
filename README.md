# TechconativeAssignment

Social Media Platform:

Project Description: 

Develop a Spring Boot based RESTful API service for a basic social media platform with the
option to use in-memory data source or a legacy database. Include detailed HTTP
methods and URL structures. Authentication is optional.
1. POST /api/users - Register a new user. 
2. POST /api/auth - Authenticate a user and return a token.
3. GET /api/users/{userId} - Retrieve a user's profile.
4. POST /api/posts - Create a new post.
5. GET /api/posts - Retrieve all posts with pagination.
6. GET /api/posts/{postId} - Retrieve a specific post.
7. PUT /api/posts/{postId} - Update a specific post.
8. DELETE /api/posts/{postId} - Delete a specific post.
9. POST /api/posts/{postId}/comments - Add a comment to a post.
10. PUT /api/posts/{postId}/likes - Like a post.

Tech Stack Used:

Java version used: 17       
Spring boot version used: 3.1.7   
Database used: MySQL (8.0.33)
Maven used: 3.9.5

MySQL database details :

	Database Used : techconativedb
	
	Tables Used :
	1.user_details - consists of details of user 
	Primary Key - id
	2.posts - consists of details of posts
	Primary Key - id
    foreign key - user_id
    Child table of user_details
	3.comments - consists of details of comments
	Primary Key - id
	Foreign Keys - post_id
    Child table of posts
    4. post_likes - consists of details of likes
    Foreign Key - post_id, user_id 

How to run:

1. Clone the repository in your IDE and load the project as a maven project. 
   git clone https://github.com/Mughilavan/TechconativeAssignment.git

2. Edit the below properties in application.properties file.
   spring.datasource.url=jdbc:mysql://localhost:<MySQL port>/techconativedb?useSSL=false
   spring.datasource.username=<Your database user name>
   spring.datasource.password=<Your database password>
   server.port=<port> (8080 is default port. You can change the port)
3. Build the project using below comment.
   mvn clean install
4. Run the project using below comment.
   mvn spring-boot:run
5. If the step 4 is not working, try the below comment in the project location.
   java -jar target/techconativeSamp-0.0.1-SNAPSHOT.jar
