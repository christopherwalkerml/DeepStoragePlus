# DeepStoragePlus
Adds the Applied Energistics 2 feature of storing bulk items

Dependency: CustomRecipeAPI
You need to download CustomRecipeAPI and put it in your plugins folder for this plugin to run
DeepStoragePlus Is a mini-clone of the very popular mod Applied Energistics. This plugin includes the bulk storage feature of AE, and may have more features coming in the future.
DeepStoragePlus helps you store bulk items in a Deep Storage Unit, and implements "Storage Containers" which are super easy to move in your inventory.

In the above image, we can see that a Storage Container with 1K storage space is full with 7 types, and has more space for items. Storage containers are super easy to move around, as mentioned above, which means you can just take the container out of the DSU and keep it elsewhere. (Note, only the DSU can manipulate the items inside the Storage Container)
Features:
- Hopper Input/Output
- Inventory Sorting
- Custom Recipes
- Wireless Terminal
- Locking + Whitelist
- Speed Upgrades
- Easy Item Movement (using storage containers)
- Realistic, not too OP


There are Many important recipes to know, so I've made a docs page for more info here: DOCS PAGE (not being updated anymore. CustomRecipeAPI 1.1.0 has a recipe book)

If you're running the latest versions of DeepStoragePlus and CustomRecipeAPI, there is a recipe book that shows all these recipes. Check it out on the CustomRecipeAPI page.


deepstorageplus.create:
  description: Allows the user to create a DSU
  default: op
deepstorageplus.adminopen:
  description: Allows the user to open locked DSUs
  default: op
crapi.command:
  description: Allows the use of CustomRecipeAPI commands
  default: op
crapi.book:
  description: Allows the use of CustomRecipeAPI recipe book
  default: op
deepstorageplus.wireless:
  description: Allows the user to use the wireless terminal
  default: op



Following in the spirit of AE, Storage Containers have a max storage, and a max data types amount. Due to the size of a double chest, I've limited the types to 7. This cannot be changed to anything higher.
There's also a custom texture pack that will be prompted upon joining the server with the plugin enabled. I HIGHLY recommend using it. This plugin is very ugly without it enabled, and it's super light. Takes about 3 seconds to load.
if the player breaks the DSU, the DSU will be lost. The Storage Containers will drop on the floor, and no data will be lost, however the DSU feature will be lost, and another Loader Wrench will be required to make another.
To use the Wireless terminal, right click on a Deep Storage Unit. Then go anywhere in the same dimension as the DSU, and right click your Wireless Terminal. To unlink it, crouch and swap to offhand at the same time.


Planned Potential Features:
- IO Speed Upgrades - Done! Version 1.7.0
- Recipe Book - Done! Supported by CustomRecipeAPI
- Maximum DSUs per person? (will be optional)
- DSU Locking feature - Done! Version 1.6.0
- Crafting terminal upgrade - will look into possibility
- Wireless terminal - Done! Version 1.5.0

Donate to support my work here -> DONATE

Join my discord server for help/suggestions. I'm active there every day.
https://discord.gg/AET9mWj

Tutorial Video - Basics:
Kw5qMgHamTk

Can conflict with auto-sorting inventory plugins
