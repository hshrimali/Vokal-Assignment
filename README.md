# NotifyMe App

This repository holds the code of the mobile app that extracts the messages from your phone's default messaging application and
then filter those according to the received timing i.e., 1 hour ago, 2 hour ago, 3 hour ago, 6 hour ago, 12 hour ago and lastly
1 day ago. It ignores the messages beyond 1 day. It also notifies the user of the new sms coming as long as app is in the background.

## Getting Started

You need to clone this repository on your system to run the code.

#### Prerequisites

You must have a text editor to view the files. 

#### Installation

To install the app, you need to download the apk of this app on your phone. To get the apk in this repository, follow the following 
path :
```
app/build/outputs/apk/debug/
```
There you'll find a file named `app-debug.apk`. Download this file on your phone and test the application.

## Running of the app

It starts with asking you to allow the app to read and receive messages from your default messaging app. If you tap on `Deny`, app 
is of no use then. It will do it's work only when you tap on `Allow` button. So, after granting the permission to the app, it will
show an acitivity with a list of all the filtered messages.
Now as soon as a new msg comes, the app will give a notification to the user and then clicking on that notification will open the app.
