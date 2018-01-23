/*
 * Copyright (C) 2017 Max 'Libra' Kersten
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package capricorn;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.security.SecureRandom;

/**
 * Manages the honeypots and all honeypot related activities
 *
 * @author Max 'Libra' Kersten
 */
public class HoneypotManager {

    /**
     * This class manages the arguments that have been given in the Command Line
     * Interface (CLI).
     *
     * @param argument the argument that was passed, determining the function
     * which should be called
     * @param fileExtension the file extension used when scanning. Use 'null' if
     * the scan option is not selected
     */
    public HoneypotManager(Arguments argument, String fileExtension) {
        switch (argument) {
            case INSTALL:
                install();
                break;
            case UNINSTALL:
                uninstall();
                break;
            case REPAIR:
                repair();
                break;
            case GUARD:
                guard();
                break;
            case SCAN:
                scan(fileExtension);
                break;
            case STATUS:
                status();
            default:
                break;
        }
    }

    /**
     * Using the directory list from the static Utilities class, the honeypot
     * directories are shown to the user
     */
    private static void status() {
        System.out.println("[+]The following directories are on the monitor list:");
        for (Path path : Utilities.getDirectories(false)) {
            System.out.println(path);
        }
    }

    /**
     * Guard starts the monitoring process during which the machine is protected
     */
    private void guard() {
        Watcher watcher = new Watcher(Utilities.getDirectories(true));
        watcher.start();
    }

    /**
     * Scans the honeypot directories to count how many files contain the given
     * extension
     *
     * @param extension the extension which should be used to scan with
     */
    private void scan(String extension) {
        //Check if the given String is not empty
        if (extension == null || extension.isEmpty() || extension.trim().isEmpty()) {
            System.out.println("[+]The given extension to search for is either empty or only consists of whitespace, which makes scanning impossible. Capricorn will now shut down.");
            System.exit(0);
        }
        int countEncryptedMain = 0;
        int countNormalMain = 0;
        File folder;
        File[] listOfFiles;
        for (Path path : Utilities.getDirectories(false)) {
            folder = path.toFile();
            listOfFiles = folder.listFiles();
            int countEncrypted = 0;
            int countNormal = 0;
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    if (listOfFiles[i].getName().contains(extension)) {
                        countEncrypted++;
                    } else {
                        countNormal++;
                    }
                }
            }
            if (countEncrypted > 0) { //Only show honeypot folders which have been affected
                System.out.println("Folder:                       " + path.toString());
                System.out.println("Amount of encrypted files:    " + countEncrypted);
                System.out.println("Amount of unencrypted files:  " + countNormal);
                System.out.println("Total amount of files:        " + (countEncrypted + countNormal));
                System.out.println("");
                countEncryptedMain += countEncrypted;
            }
            countNormalMain += countNormal;
        }
        System.out.println("Total");
        System.out.println("Amount of encrypted files:    " + countEncryptedMain);
        System.out.println("Amount of unencrypted files:  " + countNormalMain);
        System.out.println("Total amount of files:        " + (countEncryptedMain + countNormalMain));
        System.exit(0);
    }

    /**
     * Repair the honeypot folders by emptying them and refilling them with new
     * honeypot files
     */
    private void repair() {
        Utilities.emptyHoneypots();
        install();
    }

    /**
     * Remove all the files and folders of the honeypots
     */
    private void uninstall() {
        Utilities.emptyHoneypots();
        Utilities.removeHoneypotFolders();
        System.out.println("\n[+]Capricorn has succesfully been uninstalled!");
    }

    /**
     * Create all the honeypot folders and fill the honeypots with files
     */
    private void install() {
        SecureRandom secureRandom = new SecureRandom();
        //Integer used to keep count of the currently written honeypot folder
        double progressHoneypotFolders = 0;

        //Loop through every honeypot location
        for (Path honeypotFolder : Utilities.getDirectories(true)) {
            //Notify the user which folder is being filled
            System.out.println("[+]Currently writing honeypot files to " + honeypotFolder.toString());
            //Obtain all the extensions from the Library
            List<String> extensions = Library.getExtensionList();
            //Create a string object here to prevent the creation of a new object every loop (extensions.size() * factor)
            String honeypotFileName;

            for (int i = 0; i < extensions.size(); i++) { //Loop through all the extensions
                for (int factor = 0; factor < 15; factor++) { //factor is amount of files created for each extension
                    List<String> fileContent = new ArrayList<>();
                    //Generate new content for every file that is written to the disk
                    for (int wordCount = 0; wordCount < secureRandom.nextInt(2001) + 1000; wordCount++) { //between 1000 and 3000 words
                        fileContent.add(Utilities.getRandomListEntry(Library.getFileContentList()));
                    }
                    honeypotFileName = Utilities.getRandomListEntry(Library.getFileContentList()) + String.valueOf(i) + "-" + String.valueOf(factor) + extensions.get(i);

                    //Write the header of the file
                    String extension = extensions.get(i);
                    //The header required for this extension
                    byte[] header = Library.getHeaderMap().get(extension);

                    //Check if header equalls null (not present in the library) is done in the write function itself, if an error occurs, it is either a permission problem (less likely) or the given header is missing in the library (more likely)
                    if (!Utilities.writeByteArray(header, honeypotFolder.toString(), honeypotFileName)) {
                        //Because the header list is not created yet, the error log is currently disabled
//                        System.out.println("[+]An exception occurred when the file was written to the disk. Check if you have the right permissions to write in the directory (" + honeypotFolder.toString() + ")");
//                        System.out.println("[+]Capricorn will now exit.");
                        //Because of the absence of a header list, the exit of the program is currently disabled, this way the files are written the same way was they were before, yet the functionality to write the headers is present for headers that exist
//                        System.exit(0);
                    }
                    //Write the randomly generated content of the file
                    if (!Utilities.writeFile(fileContent, honeypotFolder.toString(), honeypotFileName)) {
                        System.out.println("[+]An exception occurred when the file was written to the disk. Check if you have the right permissions to write in the directory (" + honeypotFolder.toString() + ")");
                        System.out.println("[+]Capricorn will now exit.");
                        System.exit(0);
                    }
                }
            }
            //Calculate the progress percentage, parameter 'false' because we already created the folders
            double total = Utilities.getDirectories(false).size();
            //Add one to the current honeypot folders
            progressHoneypotFolders++;    
            //Calculate the percentage
            double percentage = progressHoneypotFolders/total*100;
            
            //Display the progress as a percentage to the user, the percentage is casted as an int to only show whole percentages
            System.out.println("[+]Progress: " + (int)percentage + "%");
            System.out.println("[+]Finished writing in " + honeypotFolder.toString() + ", moving to the next folder!\n");
        }
    }
}
