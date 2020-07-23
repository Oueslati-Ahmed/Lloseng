# Lloseng
This repository contains code available to students using the book Object-Oriented Software Engineering: Practical Software Development using UML and Java, 2nd Edition, 2004 McGraw Hill.  by [Timothy C. Lethbridge](http://www.eecs.uottawa.ca/~tcl) and [Robert Laganière](http://www.eecs.uottawa.ca/~laganier). 

The material originally appeared on the website: http://www.lloseng.com, but is being moved here to be more accessible

Some relevant websites related to this repository:

* The book's website: http://www.site.uottawa.ca/school/research/lloseng/ 

* Book website at McGraw Hill: https://www.mheducation.ca/highereducation/products/9780077109080/object-oriented+software+engineering:+practical+software+development+using+uml+and+java/

* Book site at Amazon: https://www.amazon.ca/Object-Oriented-Software-Engineering-Practical-Development/dp/0077109082

* Student solutions to exercises (first edition): http://highered.mheducation.com/sites/0077097610/student_view0/student_solutions.html

Professors may obtain solutions to exercises including code by contacting the authors. They must show a web presence confirming their email address and that they are indeed an academic at that same domain.


# V2 FEATURES

* Added logs recording to make a transcript of the current conversation, simply type `#save` and a log#timestamp.txt file will be saved to the current dir

* Logs are automatically deleted (if not saved by typing `save`) when the server shuts down

* Server can now send private messages by typing `@@@id:message` where id is the user's id and the message is the message to send, the user will get the message in the format of `####PRIVATE SERVER MESSAGE#### message` , private messages are not recorded in logs

* Clients can also send private messages to eachother by typing `$id:message` , the receipient will receive the message in the format of 
`#Private msg from sender_id # " message` , private messages are not recorded in logs

* From the server console type `#list` to get a list of online connected clients

* Clients can do the same thing by typing `#list` and a list of online users will be sent to them privately

* Added a shutdown hook to handle unexpected server shutdown (pressing X, alt f4, ^C..) to delete logs
