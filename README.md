## ModUpdater

ModUpdater is a tool/app for Downloading and Updating Minecraft Mods


## Install

Download the ModUpdater-1.2.0-SNAPSHOT-jar-with-dependencies.jar which is located at the 'target' folder


## Usage 

If you have java installed on your pc, you can just open the .jar file.

If you are on a computer with a screen, you will see a GUI popup. If that is correct, then follow the GUI thing here

### GUI Usage

In the GUI you have three options: "Check Mod", "Install Mod" and "Update All Mods"

#### Check Mod

Here you can check if there is a download for the requested mod for the requested version.

Options:
- Project ID
- Minecraft Version

At Project ID you need to fill in the Project ID of the mod. 
At the mod '[Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu)' you can see at the "About Project" information that the Project ID is '308702'


#### Install Mod

Here you can install the requested mod for the requested version in the requested .minecraft location

Options:
- Project ID
- Minecraft Version
- .minecraft Folder Location

At Project ID you need to fill in the Project ID of the mod. 
At the mod '[Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu)' you can see at the "About Project" information that the Project ID is '308702'

At .minecraft Folder Location you need to select the '.minecraft' folder for your minecraft client or server. 
Note: This is NOT the mods folder, this is the folder where the mods folder is located at!!!


#### Update All Mods

Here you can update a lot of mods at the same time. 

Options: 
- .minecraft folder location

At .minecraft Folder Location you need to select the '.minecraft' folder for your minecraft client or server. 
Note: This is NOT the mods folder, this is the folder where the mods folder is located at!!!

Note: You need to have an 'mods.json' file in your .minecraft folder!

Here is an example:
```json
{
        "version":"1.18.1",
        "modLoader":4,
        "mods":[
                {"name":"Sodium","pid":"360438"},
                {"name":"Modmenu","pid":"308702"}
        ]
}
```

At the moment of writing this, it doesn't matter what you enter at the 'modLoader' field, but make sure it is a number (1 = forge, 4 = fabric)


### Command Line Usage

If you don't have an interface on your computer, you will still be able to use this ModUpdater!

Here are the possible commands with their params:
- checkMod <pid> <version>
- installMod <pid> <version> <.minecraft folder>
- updateAllMods <.minecraft folder>
- version
- help

These commands work the same as with the GUI one, but the GUI just makes it simpler to use this ModUpdater
