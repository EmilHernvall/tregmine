Tregmine 2
========

This is the rabil.org [Tregmine 2] tregmine plugin. Pull requests are appreciated :-)
Don't ask why the plugin version is 3.X but its called Tregmine 2.
Index
-----

The main plugin is in the src directory. In addition, there are a few utilities
and tools in this repository:

 * blocklog - Logs changes to blocks, the log can be accessed with paper
 * delegate_gen - Generates a class from an interface that can be extended.
 * eventdebug - Development tool for when Bukkit events get triggered.
 * gamemagic - Stuff specific to the main tregmine world
 * world_importer - Tool for transitioning from the old tregmine database
 format.
 * fix_broken - Simple tool for scanning mc region files and discarding broken chunks
 * mojang_nbt - Mojangs lib for parsing nbt files
 * zone_exporter - Tool for exporting tregmine zones as single player levels
 * zone_mapper - Tool for generating maps from tregmine zones

Installation
-----------

Installation has been simplified with Tregmine 2.
All you need to do is copy the contents of the plugins folder into your server plugins folder, and import tregminedb.sql.
NOTE: You *MUST* configure the SQL settings in config.yml __at the minimum__ in order for the plugin to function.

Current Coders
-------
 * Robby Catron <rcatron10@gmail.com> - Current maintainer
 * Eric Rabil <ericjrabil@gmail.com> - Current maintainer
 
Special Thanks To
------------
 * Ein Andersson - Original author
 * Emil Hernvall
 * Josh Morgan
 * Joe Notaro
 * James Sherlock