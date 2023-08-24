-------------------------------------------
Source installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "unrenamed" MCP source code (aka
srgnames) - this means that you will not be able to read them directly against
normal code.

Source pack installation information:

Standalone source installation
==============================

See the Forge Documentation online for more detailed instructions:
http://mcforge.readthedocs.io/en/latest/gettingstarted/

Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: You're left with a choice.
If you prefer to use Eclipse:
1. Run the following command: "gradlew genEclipseRuns" (./gradlew genEclipseRuns if you are on Mac/Linux)
2. Open Eclipse, Import > Existing Gradle Project > Select Folder 
   or run "gradlew eclipse" to generate the project.
(Current Issue)
4. Open Project > Run/Debug Settings > Edit runClient and runServer > Environment
5. Edit MOD_CLASSES to show [modid]%%[Path]; 2 times rather then the generated 4.

If you prefer to use IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Run the following command: "gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not affect your code} and then start the processs again.

Should it still not work, 
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.
or the Forge Project Discord discord.gg/UvedJ9m

Forge source installation
=========================
MinecraftForge ships with this code and installs it as part of the forge
installation process, no further action is required on your part.

LexManos' Install Video
=======================
https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be

For more details update more often refer to the Forge Forums:
http://www.minecraftforge.net/forum/index.php/topic,14048.0.html


This mod is based on Alen becker stickfigures vs minecraft Video
with Lucky Blocks
"Lucky Blocks - Animation vs. Minecraft Shorts Ep 19 - YouTube"

Features
- Items
	- Nether ward wand
		- spawn nether ward on ground when right clicking a solid block with air above
		- it spawn default minecraft nether ward on soul sand else it spawn our
		custom block, this was in case if i would remove this mod
		- it also has a ugly version, but this if for creative users
		- immune to fire
	- Edible Nether wart
		- need to cook normal nether ward on a soul sand campfire
		- nutrition 2
		- saturation 0.6
		- immune to fire
	- Butter
		- nutrition 3
		- saturation 0.8
		- crafted by butter ingot = 3 Dandelions on a row
- Blocks
	- Placeable Nether wart blocks
- Entities
	- Pig (altered)
		- added Tempt item Nether wart
		- pick up potions
		- drink potions
			- difficulty peaceful
				- no effect
			- difficulty easy
				- gets effect from potion
			- difficulty normal
				- effect: speed, FIRE_RESISTANCE, haste, STRENGTH, INSTANT_HEALTH, JUMP_BOOST,
				REGENERATION, RESISTANCE, WATER_BREATHING, NIGHT_VISION, HEALTH_BOOST, ABSORPTION,
				SATURATION, LUCK, SLOW_FALLING, CONDUIT_POWER, DOLPHINS_GRACE
			- default same as DOLPHINS_GRACE, but adds INVISIBILITY
		- drop empty potion
		- when player attacks a pig that drunk potions it will attack the player
		- it uses HERO_OF_THE_VILLAGE to determine if it was from us
		this is also to prevent pig from keep attacking or start attacking

Planned features
- render potion in mouth if not already

Known bugs
