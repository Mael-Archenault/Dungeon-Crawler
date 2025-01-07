# Dungeon Crawler

## Context
One of my school project consisted in creating a Dungeon Crawler game using Object-Oriented Programming with Java.

The course made us build the base of the Dungeon Crawler. In the aftermath we were given some hours to add improvements and special features to our games.
This repository is what I came up with.

## Improvements Added

### Actions
- by pressing two directions keys, the player is able to move diagonally

- H : triggers a debugging tool, shows the hitbox of every SolidSprite. Press again to make them disappear

- J : the hero sends a fireball, which explodes after touching a solid element

- K : the hero sets a bomb which explode after a certain amount of time. The explosion will remove life points to each figure in the bomb field. Moreover, if a bomb explodes, it will trigger other bombs that are in the field and make them explode instantaneously. If the bomb touches a solid element like a rock or a tree, it destroys it. The hitbox of the bomb really fit the deflagration (press H).

- Space : the hero attacks with a sword. The range is very limited.

### Enemies

- added enemy patterns. The enemy has a list of point it had to visit. At each point, it settles for a short moment, then go to the next point. The bot is able to avoid solid obstacles

- when the player enters in the vision field of the bot or attack it, the bot targets the player to attack him. If the distance is to big, the target is lost.

### Map

- added a mode to generate a random map procedurally. The method used is called "wave function collapsing" (which generates a coherent map using adjacency rules).


### Camera 

- changed the static camera to a dynamic one that follows the player, with ease animations
  
- created a trigger zone to change the zoom of the camera (with smooth transition)

### Menu

- added a start menu with one mode to choose among two (obviously I'm not a graphic designer, but it works)

### Modes

- random map : generates a random map with some normal enemies within it, you can explore the map
- boss fight : with this mode you can fight in an adapted map a boss (high life, high damages,, ...)

### Other changes

- added the possibility to change hitbox position relative to the top-left corner of the image : that way, the hitbox is not locked with the sprite and we can make it more accurate

- added a field to change the framerate of the game (in the start menu)





   

