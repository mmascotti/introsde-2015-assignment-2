#Assignment 2
##Server url
The server url on Heroku is:  ```https://introsde-assignment2-server.herokuapp.com```   

I worked alone, so the server and the client code are both in this [repository](https://github.com/mmascotti/introsde-2015-assignment-2).  

##Structure  
The client code is in the package *client*, the server code is in the other packages:    
 * *dao*: access to the database  
 *  *model*: entity classes for the database  
 *  *transfer*: transfer objects that represents the classes in *model* for the client  
 *  *rest*: implementation of the RESTful webservice  
 *  *server*: the standalone server for the webservice  
 
