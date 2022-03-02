GUI Usage
---------------

In the GUI you have three options: "Check Mod", "Install Mod" and "Update All Mods"

### Check Mod

Here you can check if there is a download for the requested mod for the requested version.

Options:
- Project ID
- Minecraft Version

At Project ID you need to fill in the Project ID of the mod. 
At the mod '[Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu)' you can see at the "About Project" information that the Project ID is '308702'


### Install Mod

Here you can install the requested mod for the requested version in the requested .minecraft location

Options:
- Project ID
- Minecraft Version
- .minecraft Folder Location

At Project ID you need to fill in the Project ID of the mod. 
At the mod '[Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu)' you can see at the "About Project" information that the Project ID is '308702'

At .minecraft Folder Location you need to select the '.minecraft' folder for your minecraft client or server. 
Note: This is NOT the mods folder, this is the folder where the mods folder is located at!!!


### Update All Mods

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
