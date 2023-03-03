## ModUpdater

ModUpdater is a tool/app for Downloading and Updating Minecraft Mods

Current Version: 1.3.0

### Quick Note

Since version 1.3.0 we have moved over to the new Curseforge API, since the old one was disabled/shutdown. 
The new API requires you to have an API key. You can get a key at https://console.curseforge.com/


## Install

Download the ModUpdater-1.3.0-SNAPSHOT-jar-with-dependencies.jar which is located at the 'downloads' folder


## Usage 

If you have java installed on your pc, you can just open the .jar file.

If you are on a computer with a screen, you should see a message show up and when you click Ok, it should show you a GUI.

If you are not having a screen, you should still follow the guide down here, but then skip to the "Commandline Usage" afterwards.


### API Key

If it is the first time you run, you need to set your Curseforge API key.

When having a screen, you will have a menu to set your key and it will automaticly save. When it is saved, go follow the "GUI Usage"
Else it will just ask you in the console!

### GUI Usage

In the GUI you have four options: "Check Mod", "Install Mod", "Update All Mods" and "Install Modpack"

#### Check Mod

Here you can check if there is a download for the requested mod for the requested version.

Options:
- Project ID
- Minecraft Version
- Modloader

At Project ID you need to fill in the Project ID of the mod. 
At the mod '[Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu)' you can see at the "About Project" information that the Project ID is '308702'
At Modloader, use Fabric or Forge. Or if you want to check if it exists at any modloader, use '-1'


#### Install Mod

Here you can install the requested mod for the requested version in the requested .minecraft location

Options:
- Project ID
- Minecraft Version
- Modloader
- .minecraft Folder Location

At Project ID you need to fill in the Project ID of the mod. 
At the mod '[Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu)' you can see at the "About Project" information that the Project ID is '308702'
At Modloader, use Fabric or Forge. Or if you want to check if it exists at any modloader, use '-1'

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

The 'modLoader' field should be one of these (you set the number): (1 = forge, 4 = fabric)


Quick Note: this script will always make a backup of your current mods folder by moving it to folder '.minecraft/backup_mods/yyyy_M_dd-hh_mm_ss/' (`yyyy_M_dd-hh_mm_ss` is an timestamp format)


#### Install Modpack

Here you can install a modpack.

Options: 
- .minecraft folder location

At .minecraft Folder Location you need to select the '.minecraft' folder for your minecraft client or server. 
Note: This is NOT the mods folder, this is the folder where the mods folder is located at!!!

Note: You need to have a modpack 'manifest.json' file in your .minecraft folder!


Quick Note: this script will always make a backup of your current mods folder by moving it to folder '.minecraft/backup_mods/yyyy_M_dd-hh_mm_ss/' (`yyyy_M_dd-hh_mm_ss` is an timestamp format)


### Command Line Usage

If you don't have an interface on your computer, you will still be able to use this ModUpdater!

Here are the possible commands with their params:
- checkMod <pid> <version> <modloader>
- installMod <pid> <version> <modloader> <.minecraft folder>
- updateAllMods <.minecraft folder>
- installModpack <.minecraft folder>
- version
- help

These commands work the same as with the GUI one, but the GUI just makes it simpler to use this ModUpdater
