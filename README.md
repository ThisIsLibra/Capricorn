# Capricorn
An anti-ransomware tool using honeypot folders and files to protect your system.

## Set-up
To install Capricorn, one must have the Java Runtime Environment installed. Executing the JAR file should be done using a terminal such as bash (on Linux) or via commandprompt/powershell (on Windows). Mac OS X is not supported, although future this is considered for future releases.

Simply executing the JAR will provide you with a list of arguments that can be used. In this readme, all the arguments will be explained with examples.

When a file in a honeypot is created, edited or deleted, the path to the file is logged on a file on the user's desktop and the computer will shut down. Because ransomware can also be executed at the boot of the OS, it is advised to use a live CD to recover files before the computer is booted from the possible infected filesystem.

## Installation on different operating systems
On Windows, the program can be executed as any user and will work. On Linux, there are some prerequisites.
Before executing the program, the user should run two commands.

1.	sudo mkdir /A
2.	sudo chmod 777 /A 
For those unfamiliar with the commands: mkdir stands for ‘make directory’ on the location /A. “sudo” is required to create this folder. Changing the permissions of the folder is done with chmod (changemod).  777 allows any user to do anything with the given files in the /A folder.

See the “-install” argument for the installation of Capricorn after the previous step has been successfully executed.

A download link to the JAR can be found in this repository, which is located at dist/Capricorn.jar in the repository.

### -guard
The guard argument will start the monitoring process. The honeypot folders are monitored and a change in these folders will cause your computer to shut down.

### -install
To install Capricorn, use this command. The honeypot folders will be created and the files will be placed in them.

### -repair
After a ransomware attack has successfully been evaded, the honeypot folders are not as effective anymore since some files have been encrypted. To repair the honeypots, issue this command. The folders will be emptied and filled again.

### -scan
After a ransomware attack, this option enables the user to scan the honeypot directory. An additional argument should be specified: the file extension that should be searched for. To search for files encrypted by SageCrypt ranswomare, one would use the “.sage” extension. Using “-scan .sage”, Capricorn will locate all sage files that reside in the honeypot folders.

### -uninstall
To remove Capricorn, deleting the JAR is enough. Yet, the honeypot folders are still on the computer. To remove these, issue the “-uninstall” argument.

### -status
To obtain a list of all the honeypot folder locations, use the “-status” argument.

### -help
Using no argument, an unknown argument or the “-help” argument, the help menu will be displayed. This also provides information about the possible arguments.

## Questions, bugs and feedback
If you’ve got a question, a bug report or feedback, please send me an e-mail on [info][at][maxkersten[dot][nl] and I’ll get back to you! 
