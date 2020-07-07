# Home Automation
![lucifer](https://img.shields.io/badge/-LuCiFeR-orange) ![Google Phone](https://img.shields.io/badge/Android-Lollipop+-blue.svg?logo=google&longCache=true&style=flat-square) ![java](https://img.shields.io/badge/-JAVA-red) ![arduino](https://img.shields.io/badge/-Arduino-blue) ![HomeAutomation](https://img.shields.io/badge/-Home%20Automation-lightgrey) ![c++](https://img.shields.io/badge/-C%2B%2B-brightgreen)
- Micro-Controller Based Home Automation for Smart Homes
## To-Do List
- ~~Initial Build By 20-June-2020~~
  - Added Build_v1 on 17 June
  - Added Build_v3 on 24 June
  - Added Build_v5 on 7 July

- ~~Basic App Interface by 24-Jun-2020~~
  - Added Build_v3 on 24 june
  - Added Build_v5 on 7 July
- To use Job Scheduler API rather than Alarm Manager in upcoming Builds for Switch on/off scheduling.**(Pending)**
- ~~Future Build will contain Scheduling task which will send turn on/off command to arduino on a Particular time selected by user.~~
  - Done in Build_v5
- Future Build to include Device Number Addition as app supports 4 Devices Only till now.


## Changelog
- Build_v1
  - Added On/Off functionality to buttons by means of single and long click respectively.
  - Can be paired with the device whose address is hardcoded in app.
  - Added Digital Clock on main screen.
  <br/>

- Build_v3
  - UI Revamping.
  - Can be paired with any device through app.
  - Removed clock.
  - Added Room Functionality.
  - Device Switching on/off scheduling(not enabled in this build).
  - New Bluetooth devices can be searched from within app.
  - App data is stored in Local Database.
  - Added functionality to connect to last connected device Seamlessly!!
  - New Rooms can be added from within app.
   <br/>
- Build_v5
  - App only supports 5 Rooms.
  - Removed Room Table from Database. From now Room name will be carried by Shared Prefs file.
  - Added Device On_Off Scheduling (One time each for each device).
  - Added [Time Picker Dialog for selecting On/Off Time for scheduling.](https://github.com/arshanwar/Home-    Automation/blob/master/Screensshots%20Build_v5/Scheduling%20time%20Dialog.jpg)
  - Removed Room Add Fragment.
  - Added [Room Settings](https://github.com/arshanwar/Home-Automation/blob/master/Screensshots%20Build_v5/Room%20Settings(Item%20Long%20Click%20).jpg).
  - Added [Extra Settings for buttons.](https://github.com/arshanwar/Home-Automation/blob/master/Screensshots%20Build_v5/Button%20Extra%20settings%20Alert%20Dialog.jpg)
    <br/>
  

 


## Working
- This app utilizes 2 Activities. One is for Device Control and Other for device pairing Stuff.
- This application makes use of:
  - **Service Class: [Bt_Connection](https://github.com/arshanwar/Home-Automation/blob/master/Build_v3/app/src/main/java/com/lucifer/h_a_t_3/Bluetooth_Service/Bt_connection.java)** which is used to Pair to device and sending Data over Bluetooth.
  - Data to Arduino based Device is sent in strings over Bluetooth.
  - A Database **HomeAutomation.db** is created upon Install. This Database contains two Tables Room and Address. 
    - Room Table Stores Room Names which are shown in Main Activity.
    - Address Table Stores paired device **address** and its **name**. Address table is used to store previous connection Data. A prompt for connecting to last device connected with app [Automatically](https://github.com/arshanwar/Home-Automation/blob/master/Screenshots%20Build_v3/Build_v3Previous%20Connection%20Alert%20Dialog.jpg) will be shown after restarting app. 
    - Address Table gets deleted after new data being entered.
   - A **custom [ListAdapter](https://github.com/arshanwar/Home-Automation/blob/master/Build_v3/app/src/main/java/com/lucifer/h_a_t_3/Custom_ListAdapter/myListAdapter.java)** is used to display Room name Stored in Database on Main Activity.
 - **As of Build_v3 All rooms send same commands in [Device Control Fragment](https://github.com/arshanwar/Home-Automation/blob/master/Build_v3/app/src/main/java/com/lucifer/h_a_t_3/Fragments/UI_Fragments/DeviceControlFragment.java). I'll be fixing that in Future.**
 - Single Press on button in [Device Control Fragment](https://github.com/arshanwar/Home-Automation/blob/master/Build_v3/app/src/main/java/com/lucifer/h_a_t_3/Fragments/UI_Fragments/DeviceControlFragment.java) will send turn on command to [Arduino](https://github.com/arshanwar/Home-Automation/blob/master/Arduino%20Code/home_automation_test_2.ino). A long press will send turn off command to [Arduino](https://github.com/arshanwar/Home-Automation/blob/master/Arduino%20Code/home_automation_test_2.ino).
 - As of Build_v5 **Shared Prefs files** are introduced so as to carry **Button name** and **Room name**.
    



## ScreenShots
- **Build_v1**
  - ![screenshot](https://github.com/arshanwar/Home-Automation/blob/master/Screenshots%20Build_v1/ScreenShot_Build_v1.jpg)
  
- **Build_v3**
  - Second Activity
    - ![SecondActivity](https://github.com/arshanwar/Home-Automation/blob/master/Screenshots%20Build_v3/Build_v3Second%20Activity.jpg)
    <br/>
  - Pair New Device Fragment
    - ![New Device](https://github.com/arshanwar/Home-Automation/blob/master/Screenshots%20Build_v3/Build_v3Pair%20New%20Device%20Fragment.jpg) 
    <br/>
  - Location Access Alert Dialog
    - ![Alert Dialog](https://github.com/arshanwar/Home-Automation/blob/master/Screenshots%20Build_v3/Build_v3Location%20Access%20Enable%20Alert%20Dialog.jpg)
    <br/>
  - Add New Room Fragmnet 
    - ![New Room](https://github.com/arshanwar/Home-Automation/blob/master/Screenshots%20Build_v3/Build_v3New%20Room%20Fragment.jpg)
    <br/>
  - Paired Devices Fragment
    - ![Paired Deivice](https://github.com/arshanwar/Home-Automation/blob/master/Screenshots%20Build_v3/Build_v3Paired%20Device%20%20Fragment.jpg)
    <br/>
  - Device Control Fragment 
    - ![Device Control](https://github.com/arshanwar/Home-Automation/blob/master/Screenshots%20Build_v3/Build_v3%20Device%20Control%20Fragment.jpg)
    <br/>
  - Last Connection Alert Dialog
    - ![Last Connection](https://github.com/arshanwar/Home-Automation/blob/master/Screenshots%20Build_v3/Build_v3Previous%20Connection%20Alert%20Dialog.jpg)
    <br/>
- **Build_v5**
   <br/>
   - Room Show
     - ![Room](https://github.com/arshanwar/Home-Automation/blob/master/Screensshots%20Build_v5/Room%20Show%20Fragment.jpg)
   <br/>
   - Room Settings
   
     - ![Settings](https://github.com/arshanwar/Home-Automation/blob/master/Screensshots%20Build_v5/Room%20Settings(Item%20Long%20Click%20).jpg)
   <br/>
   - Room Deletion Dialog
   
     - ![Deletion](https://github.com/arshanwar/Home-Automation/blob/master/Screensshots%20Build_v5/Room%20Deletion%20Alert%20Dialog.jpg)
   <br/>
   - Room Name Updation Alert Dialog
   
     - ![Updation](https://github.com/arshanwar/Home-Automation/blob/master/Screensshots%20Build_v5/Updating%20Room%20Name.jpg)
   <br/>
   
   - Button Extra Settings Alert Dialog
   
      - ![extra](https://github.com/arshanwar/Home-Automation/blob/master/Screensshots%20Build_v5/Button%20Extra%20settings%20Alert%20Dialog.jpg)
      
   <br/>
   
   - Room Configurations Alert Dialog
   
      - ![conf](https://github.com/arshanwar/Home-Automation/blob/master/Screensshots%20Build_v5/Updating%20Button%20Configurations.jpg)
   <br/>
   
   - Device On/Off Scheduling Alert Dialog
   
      - ![on_off](https://github.com/arshanwar/Home-Automation/blob/master/Screensshots%20Build_v5/Scheduling%20On_Off.jpg)
   <br/>
   
   - Device On/Off Time Picker Dialog
   
      - ![time](https://github.com/arshanwar/Home-Automation/blob/master/Screensshots%20Build_v5/Scheduling%20time%20Dialog.jpg)
   <br/>
   
   - Second Activity
   
      - ![second](https://github.com/arshanwar/Home-Automation/blob/master/Screensshots%20Build_v5/Second%20Activity.jpg)
   <br/>
      

