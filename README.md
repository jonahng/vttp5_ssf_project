## For Assessor

1. Must handle HTTP POST and GET request
Answer: PlaceController.java uses both HTTP Post and Get

2. Must include parameterized routes (@PathVariable)
Answer: PathVariable is used in both PlaceController.java and PlaceRestController.java

3. Must include form validation
Answer: Form validation is used in the model SessionData.java, to ensure that the user fills in a username in the view sessioncreate.html. Error message is shown if left blank.

4. Must include both MVC (@Controller) and REST endpoints (@RestController)
Answer: @Controller is in PlaceController.java, @RestController is in PlaceRestController.java

5. POST must consume either form-urlencoded or JSON payload
Answer: Post in PlaceController.java consume form-urlencoded, the forms in sessioncreate.html and locationselection.html post in form-urlencoded

6.  Must support more than 1 user
Answer: multiple users are supported, httpsession is created for each user, tagged to their username

7. Must include a minimum of 3 views, not including REST endpoints
Answer: sessioncreate.html, locationselection.html, suggestionpage.html are all used by the user. Admins can access redisData.html and embedmap.html

8. Making HTTP request to external RESTful API
Answer: HTTP request is made to Google's nearby place API, to get back a Json Array of restaurants near a given location.

9. REST endpoints must not be those that have been discussed or used in class or assessment (eg. Open Weather Map, Giphy, News, etc)
Answer: This application uses Google's nearby place API to get a JSON array of restaurants near a given location. Google's place autocomplete api is also used., these were not covered in class

10. Use CSS to style your user interface
Answer: BootStrap is used in the html pages

11. Redis database must be running in the ‘cloud’
Answer: Redis is running on railway

12. You must deploy your application to Railway or any public cloud provider.
Answer: Application is running in railway

13. Spring Boot application must be deployed on JDK 22
Answer: JDK version can easily be modified in pom.xml if needed

14. Redis database must be in the 'cloud.’
Answer: Redis database is on railway

