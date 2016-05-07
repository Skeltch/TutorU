The apk file to run this is TutorU.apk, use an android device or emulator.
This project connects to our digital ocean droplet at the ip TutorU.mooo.com.
Source code for the apk can be found in TutorU/app/src/main.
From there java is for the application and res is for the xml files.
The server files can be found at https://github.com/Skeltch/Server.
These are the files that are currently being used in the droplet.
There is also a CRON task and Email service setup for the droplet.
Occasional testing with android monkey was done using the command
"adb shell monkey -p group14.tutoru -v 500" to run 500 tests of random inputs in our app. The location of adb.exe
can be found in sdk\platform-tools. 