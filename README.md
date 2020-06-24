# Home Automation

- Micro-Controller Based Home Automation for Smart Homes
## To-Do List
- ~~Initial Build By 20-June-2020~~
  - Added Build_v1 on 17 June

- ~~Basic App Interface by 24-Jun-2020~~
  - Added Build_v3 on 24 june

- UI enhancement by 30-Jun-2020
- Future Build will contain Scheduling task which will send turn on/off command to arduino on a Particular time selected by user.
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
 - **As of Build_v3 All rooms call same [Device Control Fragment](https://github.com/arshanwar/Home-Automation/blob/master/Build_v3/app/src/main/java/com/lucifer/h_a_t_3/Fragments/UI_Fragments/DeviceControlFragment.java). I'll be fixing that in Future.**
 - Single Press on button in [Device Control Fragment](https://github.com/arshanwar/Home-Automation/blob/master/Build_v3/app/src/main/java/com/lucifer/h_a_t_3/Fragments/UI_Fragments/DeviceControlFragment.java) will send turn on command to [Arduino](https://github.com/arshanwar/Home-Automation/blob/master/Arduino%20Code/home_automation_test_2.ino). A long press will send turn off command to [Arduino](https://github.com/arshanwar/Home-Automation/blob/master/Arduino%20Code/home_automation_test_2.ino).
    



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
    

