# Handle-Android-
## Mobile Service and Assistance Application

### Login Screen

The first screen of the Handle program is this screen as seen on Figure 1. Users can log 
in if they have an account or create one if they do not. To create a user, click the "SIGN 
UP" button and go to the SIGN UP intent.

![image](https://user-images.githubusercontent.com/20724649/121866781-ef687e80-cd07-11eb-8219-8ab07f421144.png)
>Figure 1: Login Screen


### Sign Up Screen

On the SIGN UP Screen, the user can fill in the necessary information completely, add a 
photo and register. The screen is seen on Figure 1.1.
If the program has not been given a request to access the gallery before, permission is 
requested to access the gallery. After the permission is given, the photo is selected and 
after the required information is entered, it returns to the Login screen to login.

![image](https://user-images.githubusercontent.com/20724649/121866946-1d4dc300-cd08-11eb-843e-1f51e1ed5215.png)
>Figure 1.1: Sign Up Screen


### Home Screen

When user logs in by entering the user information, the page that we call Home, which 
includes all active posts, is displayed. In this and many pages, addSnapshotListener, which 
is the feature offered by Firebase, is used to drop jobs on the page as soon as they are 
opened synchronously.
This page contains works that have not expired. It can be listed according to different 
features according to user request. In addition, the user can see all posts on a map, 
including their location. We benefited from Google Maps in this program.
To view the details of a post or to make an offer for that post, the user simply clicks on the 
post. Home screen is as seen on Figure 1.2.

![image](https://user-images.githubusercontent.com/20724649/121867158-4bcb9e00-cd08-11eb-9fd1-6b2eb8098fef.png)
>Figure 1.2: Tasks listed on Home Screen


### New Task Screen

Users in need can create a post by clicking the New Task button on the navigator at the 
bottom of the Home page after signing up and logging in.
Before a job is created, the address is selected on the map and this address can be changed 
on the next screen. After the location is properly selected, information about the job is 
entered as seen on Figure 1.3. The most important of this information is the date and time 
of work. It is chosen between what day and at what time a job will be done. As expected, 
past dates cannot be selected, and if the selected day is today, the past time cannot be 
selected.
The title is written, and then the description field should be written in detail about the work 
to be done. It is important that the users who will make a bid fully understand the business.
A picture relevant to the work is chosen and this makes it very easy to distinguish visually.

![image](https://user-images.githubusercontent.com/20724649/121867244-69006c80-cd08-11eb-96b6-aa87a1c8624b.png)
>Figure 1.3: New Task Screen in Action

As is the main purpose of this application, people in need can create help posts free of 
charge. To do this, instead of entering a fee in the "Price in TL" section, the Free option 
below it is ticked, indicating that they expect a free help.
After the post is created, it is now open to offers.


### Job Detail Screen

The Post Detail Page can be Intent from many places. This page is one of the most 
important. Users can view almost all of the Post's information here, and can view the Post's 
location information over the map as seen on Figure 1.4.
One of the things that makes this page important is that not only can you look at the details 
of the Post, but you can place an offer for that Post, or the amount of payment chosen for 
that job can be accepted. Many users can bid or accept this job.

![image](https://user-images.githubusercontent.com/20724649/121867381-92b99380-cd08-11eb-91ff-6a939afc8484.png)
>Figure 1.4: Job Detail Screen


### All-Jobs-on-Map Screen

This page, which shows the location of all posts and the user, allows the user to more 
easily see and select the jobs they are close to. On this page, Google Maps, the service 
provided by Android, was used as seen on Figure 1.5.
If the user wishes, they can connect to the navigation feature of Android and get route 
information to the location of the Post they want.
As it provides convenience in job selection, an intent can be made to the Post Detail page 
by clicking the description on the markers. On the Post Detail page, the job can be 
accepted or offered as described above.

![image](https://user-images.githubusercontent.com/20724649/121867465-a82ebd80-cd08-11eb-910a-13505c25b210.png)
>Figure 1.5: All-Jobs-on-Map Screen


### My Profile Screen

The profile page contains many transitions. The main of these is Account details.
All information about the account can be changed in the Account Details section.
My Posts page allows us to go to the Posts we create and manage there.
My Offers Page is the tab where we can see the offers we have given to the posts and the 
help works we accept. Related images can be seen on Figure 1.6.

![image](https://user-images.githubusercontent.com/20724649/121867566-c0064180-cd08-11eb-8aa6-935613daabdf.png)
>Figure 1.6: My Profile Screens


### My Posts Screen

My Posts Page contains all the posts we have opened. We can change the price of all the 
posts we have opened on this page or delete these posts.
We can see all the offers submitted to the posts by clicking on the post again. Apart from 
the information of the offerers, their phones are also visible here and the parties can be 11
contacted. In addition, the users' score is visible, making it easy to choose the person to do 
this job.

![image](https://user-images.githubusercontent.com/20724649/121867665-ddd3a680-cd08-11eb-8d95-941b52dd1c66.png)
>Figure 1.7: My Posts Screen

We can accept or reject offers. In order to give the score of the offer we accepted, it is 
necessary to be informed that it was made by the user as seen on Figure 1.7. After the 
bidder has done the job, he / she says that he / she is doing the job by pressing the "I’ve 
Done" button and the user who opens the job becomes able to give points.

### My Offers Screen

The status of the offers can be learned and the offer prices can be updated from the My 
Offers Page. Also, the offer can be canceled.
If the offer is rejected, it will appear as "Offer is declined", if accepted, it will appear as 
"Offer is accepted". And this process works synchronously and the information is updated 
instantly. When the user cancels his / her offer, it becomes colored by typing "Offer is 
canceled". The phrase "offer is pending" means that no reply has been received for the 
offer yet.

![image](https://user-images.githubusercontent.com/20724649/121867786-fd6acf00-cd08-11eb-9d0a-f32b4ebceada.png)
>Figure 1.8: My Offers Screen


### Flow Diagram

![image](https://user-images.githubusercontent.com/20724649/121867949-23906f00-cd09-11eb-881a-1f903e56b2f0.png)
>Figure 2: Flow Chart









