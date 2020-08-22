Network Software Engineering
Ben Mottershead - b7003892

README for client assignment

Archive Contents (Original from server task)
- TwitterServer.java (Server host)
- TwitterClient.java (Individual client program)
- TwitterClient.java Handler (Multithreaded client handler)
- Request.java (Parent class for all request objects, allows toJSON of common variables (identity, _class))
- GetRequest.java (Class to be sent to server via JSON when client asks to view messages)
- OpenRequest.java (Class to be sent to server via JSONwhen client asks to open a channel)
- PubishRequest.java (Class to be sent to server via JSON when client asks to publish a new message)
- SubscribeRequest.java (Class to be sent to the server via JSON when a client asks to subscribe to a channel)
- UnsubscribeReqest.java (Class to be sent to the server via JSON when a client asks to unsubscribe from a channel)
- Response.java (Parent class for all response objects, allows toJSON of common variables
- SuccessResponse.java (Class to be sent to client from handler)
- ErrorResponse.java (Class to be sent to client from handler, contains error text outlining issues)
- Channel.java (Class for channel objects when users open channels)
- Message.java (Class containing information regarding posts by users)
json-simple-1.1.jar (Jar file to allow JSON serialisation)

Additions to archive for client task (In  addition to the contents above)
- ClientGUI.java (Main Class, to be ran by user. This then opens a client connection to the server)
- ChannelSearchRequest.java (Child of Request class for searching a channel)
- MessageSearchRequest.java (Child class of request for searching a hashtag within a message)
- NewMessageBoolRequest.java (Boolean request for whether the current client has any new messages (for live feed updating))
- GetSubsRequest.java (Returns list of channels current client is subscribed to)
- ImageMessage.java (Extension of message class to hold an image (in 64bit bytecode))
- Post.java (Extension of VBox to show the client contents of a Message)
- ImagePost.java (Extension of Post class to show an image message to the client)
- Nav.java (Extension  of VBox to contain navigational and settings buttons)
- ChannelBox.java (Extension of VBox to contain channel name and button for subscribing/unsubscribing)
- OpenChannelScene.java (Extension of scene class for opening a new channel)
- ViewScene.java (Parent class for all windows that can be navigated to. Extends BorderPane and holds common elements)
- PostScene.java (Center window to be navigated to when on the feed (viewing images))
- SearchScene.java (Center window to be navigated to when on the search page)
- ScrollScene.java (Parent class for scenes with a scrolling flowpane (Such as PostScene and teh subscriptions page)
(JavaFX jar files to allow compilation at commmand line as well as within eclipse debugger)
javafx-swt.jar
javafx.base.jar
javafx.controls.jar
javafx.fxml.jar
javafx.graphics.jar
javafx.media.jar
javafx.properties
javafx.swing.jar
javafx.web.jar
- module-info.java  (List of required/exports jar files for running)
- darkstyles.css (Dark theme for colours)
- lightstyles.css (Light theme for colours)

MESSAGE PERSISTANCE IS INCLUDED, EXISTING MESSAGES WILL BE LOADED ONTO SERVER (With appropriate channels created)

EXTENSION:
-Client can search for hashtags, required both client side and server side changes

To compile from command line (Inside assignment3final package folder):
javac -cp .;json-simple-1.1.jar --module-path "C:\Users\Ben\Documents\openjfx-11.0.2_windows-x64_bin-sdk-1\javafx-sdk-11.0.2\lib" --add-modules javafx.controls  *.java

To run from command line (Outside of assignment3final package(Just in src folder))
First, Compile Server:
java -cp .;json-simple-1.1.jar --module-path "C:\Users\Ben\Documents\openjfx-11.0.2_windows-x64_bin-sdk-1\javafx-sdk-11.0.2\lib" --add-modules javafx.controls assignment3final.TwitterServer 12345

Second, Compile GUI:
java -cp .;json-simple-1.1.jar --module-path "C:\Users\Ben\Documents\openjfx-11.0.2_windows-x64_bin-sdk-1\javafx-sdk-11.0.2\lib" --add-modules javafx.controls assignment3final.ClientGUI

Run this command multiple times to open multiple UI/Clients
Connect with localhost and port number 12345