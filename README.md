How to build this project with Gradle
==============

You can build this project using the provided gradle executables
 - at first call "gradlew sableCC", which generetates the parser from the .scc-files and does some adjustments for gradlew
 - for an executable jar call: "gradlew shadowJar". You find the jar "stups-toolbox-2.0-all.jar" in build/libs

(- to build the project call gradlew build)

How to further develope this project
==============

To develope this project further, there are some things for you to consider:
-
Changelog
==============
-Release 1.0 (22-09-2016):<br>
&nbsp;&nbsp;&nbsp;-initial release<br><br>

-Release 1.0.1 (07-11-2016):<br>
&nbsp;&nbsp;&nbsp;-fixed a bug in the Grammar Main.GUI, where a nonterminal would be shown as a terminal and a nonterminal at the same time<br>
&nbsp;&nbsp;&nbsp;-fixed a bug in the Grammar Main.GUI, where a rule would be duplicated, after it has been edited in the Main.GUI<br>
&nbsp;&nbsp;&nbsp;-fixed a bug, where the algorithm for calculating the follow sets of a grammar would throw a NullPointerException in some cases<br>
&nbsp;&nbsp;&nbsp;-fixed a bug in the Grammar Main.GUI, where it was possible to add an empty symbol<br>

Release Notes
==============
STUPS-Toolbox Release 1.0.1 (07-11-2016)<br>
Written and developed by Fabian Ruhland.<br><br>
This program uses the JUNG2-library to display automatons.<br>
JUNG2 is licensed under the BSD open-source license.<br>
See http://jung.sourceforge.net/site/license.html or the file "lib/JUNG2/JUNG-license.txt" for more information.
