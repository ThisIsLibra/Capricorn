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

import java.io.IOException;

/**
 * Manages the shutdown functions based on the operating system that is used
 *
 * @author Max 'Libra' Kersten
 */
public class Shutdowner {

    public Shutdowner() {

    }

    /**
     * Depending on the platform, the shutdown command is different. Using this
     * class, the correct OS shutdown call is made and extra checks are
     * executed. I used part of the code provided in the source below to create
     * this function. Source:
     * https://www.linglom.com/programming/java/how-to-run-command-line-or-execute-external-application-from-java/
     *
     */
    public void shutdown() {
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr;
            if (Utilities.checkWindows()) {
                pr = rt.exec("cmd /c shutdown /a"); //abort any pending shutdown which prevents the shutdown command (thanks Jurjen de Jonge)
                pr = rt.exec("cmd /c shutdown /f /p"); // /f /p to shutdown without warning or waiting on 'Do you want to save this document' prompts
            } else {
                pr = rt.exec("halt -p -f"); // -f -p to shutdown the system instantly. Possible downside of this method is that files that are currently being written are not saved. The ransomware is most likely affected by this, as it was during my testing with one sample.
                pr = rt.exec("shutdown now"); //The halt option needs root, which works faster on distirbutions like Kali where everything runs as root. On other Linux systems, the shutdown system needs to be used.
            }
        } catch (IOException e) {
            //If the system doesn't shut down, we have to try again
            shutdown();
        }
    }
}
