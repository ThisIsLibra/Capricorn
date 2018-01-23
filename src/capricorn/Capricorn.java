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

/**
 * The lay-out of this main class is kept rather simple. The current user
 * interface is a command line interface. In the future it is possible to make a
 * GUI for the program by using the lay-out of this class.
 *
 * @author Max 'Libra' Kersten
 */
public class Capricorn {

    private static final String VERSION = "1.1 (stable release)";

    /**
     * Using the argument(s), Capricorn will execute specific tasks. If the
     * argument is unknown, the help menu is shown.
     *
     * @param args The argument supplied by the user on the commandline
     */
    public static void main(String[] args) {
        System.out.println("Capricorn " + VERSION);
        System.out.println("Developed by Max 'Libra' Kersten <info[at]maxkersten.nl>");
        System.out.println("For more information, visit https://www.maxkersten.nl/capricorn \n");
        if (args.length <= 0) {
            help();
        } else if (args.length > 0) {
            if (args[0].equals("-scan") && args.length == 2) {
                scan(args[1]);
            } else if (args[0].equals("-uninstall")) {
                //Remove the files in the honeypot folders and delete the folders afterwards
                uninstall();
            } else if (args[0].equals("-install")) {
                //Create the honeypot folders and fille the folders with honeypot files
                install();
            } else if (args[0].equals("-repair")) {
                //Empty the honeypots and fill them again
                repair();
            } else if (args[0].equals("-guard")) {
                //Guard system: monitoring the honeypot folders
                guard();
            } else if (args[0].equals("-status")) {
                //Show the location of the honeypot folders
                status();
            } else {
                //Show help
                help();
            }
        }
    }

    /**
     * Show the honeypot folder location
     */
    private static void status() {
        HoneypotManager honeypotManager = new HoneypotManager(Arguments.STATUS, null);
    }

    /**
     * The help function is called whenever an unknown parameter is registered.
     */
    private static void help() {
        System.out.println("Unknown parameter(s) detected. Use one of the following arguments: \n");
        System.out.println(" -guard        monitor the system");
        System.out.println(" -install      install the honeypot folders and monitor the system afterwards");
        System.out.println(" -repair       replace all the honeypot files, this should be done after a blocked ransomware attack");
        System.out.println(" -scan         scan for files with a specific file extension, which should be supplied as an additional parameter");
        System.out.println(" -uninstall    remove all the honeypot files and folders from the system");
        System.out.println(" -status       show the location of the honeypot folders");
        System.out.println(" -help         to show this information\n");
    }

    /**
     * The repair function empties the honeypots and fills them again.
     */
    private static void repair() {
        System.out.println("[+]The bees are repairing the honeypots. Please give them a minute.");
        HoneypotManager honeypotManager = new HoneypotManager(Arguments.REPAIR, null);
        System.out.println("[+]The honeypots have succesfully been repaired! The bees can now guard your system again.");
    }

    /**
     * The uninstall function removes the files from the honeypots and also
     * deletes the honeypot folders
     */
    private static void uninstall() {
        System.out.println("[+]Emptying the honeypots and removing the honeypot folders");
        HoneypotManager honeypotManager = new HoneypotManager(Arguments.UNINSTALL, null);
        System.out.println("[+]Uninstalling is now complete, Capricorn's honeypots have been removed from the system.");
    }

    /**
     * The install function creates the honeypot folders and fills them with
     * honeypot files
     */
    private static void install() {
        System.out.println("[+]Setting up the honeypots. Bees can easily be distracted, so give them a minute.");
        HoneypotManager honeypotManager = new HoneypotManager(Arguments.INSTALL, null);
        System.out.println("[+]The bees have succesfully filled the honeypots!");
    }

    /**
     * When a ransomware attack has been blocked, there are honeypot files which
     * are infected. Using Google or the system's filebrowser, a user can find
     * the extensions of the given ransomware. The scan functions uses the
     * provided file extension to search in all the honeypot files. A report
     * will be shown to the user with information about the honeypot folders.
     *
     * @param fileExtension the file extension used to scan with
     */
    private static void scan(String fileExtension) {
        System.out.println("[+]Scanning for encrypted files in the honeypot folders \n");
        HoneypotManager honeypotManager = new HoneypotManager(Arguments.SCAN, fileExtension);
    }

    /**
     * The guard function monitors the honeypot folders on changes and protects
     * the system whenever needed. After installing, this is the only function
     * that needs to be called to start the monitoring process
     */
    private static void guard() {
        System.out.println("[+]The guards are spread out and are will alert the hive if there is danger ahead! You can continue to work safely!");
        HoneypotManager honeypotManager = new HoneypotManager(Arguments.GUARD, null);
    }

    /**
     * The Dalvik VM properties are shown as a system.out message. This is used
     * during development and is not used in the program
     */
    private static void printArgs() {
        System.getProperties().list(System.out);
    }
}
