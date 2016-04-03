This project requires the use of a WAMP server with the correct files in the www directory under the folder app. 
The ip for communication is set to localhost to prevent inconsistent ips. This will be changed in the future to instead
be linked to our database that is not local. The files tables.php and createDatabase.php in app/test/"learning mysql"
were made to create the database used. Occasional testing with android monkey was done using the command
"adb shell monkey -p group14.tutoru -v 500" to run 500 tests of random inputs in our app. The location of adb.exe
can be found in sdk\platform-tools. 