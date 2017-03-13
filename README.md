Stups-Toolbox-2.0
==============
a toolbox for visualizing algorithms and structures of theoretical computer science
--------------


How to build this project with Gradle
==============

You can build this project using the provided gradle executables

- at first call **gradlew sableCC**, which generetates the parser from the .scc-files and does some adjustments for gradle
- for an executable jar call: **gradlew shadowJar** You find the jar "stups-toolbox-2.0-all.jar" in build/libs
- to build the project call **gradlew build**

How to use this program
==============

You can start this program by starting the .jar. 
- View all command by calling *help*. 
- Start the gui by calling *gui*. 
- Exit the programm by calling *exit*. 


How to further develope this project using SableCC
==============
If you want to further develope this project, follow these steps

- To import the project in IntelliJ or Eclipse, simply call **gradlew idea** or **gradlew eclipse**
- **ATTENTION** if you want to add a new type of storables (e.g. *turing machine*) read the following paragraph very carefully
	
	+ let us call the new typ *Turing*
	+ create a File *TuringParser.scc* in src/main/sablecc
	+ write *Package TuringParser* in the first line.
	+ it is very important, that the file has exactly this name, because otherwise the sablecc-gradle-workaround won't work
	+ create a package *TuringSimulator* (optional)
	+ and in this package a class *Turing.class*
	+ at the beginning of the *init*-method of Main.Content add your new type to the lookup-table, otherwise the GUI won't work
	+ to do so, call *lookUpTable.put("turing",Turing.class)*
	+ to enable the gui to display your new type, create a *TuringGUI.class* inside the package *GUIPlugins.DisplayPlugins* and
	let it extend *DisplayPlugin*.
	
How to further develope this project without using SableCC
==============
If you want to further develope this project without using SableCC simply skip the step with the .scc*
