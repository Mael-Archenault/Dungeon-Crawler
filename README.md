In this document, I will review all features that were added to the dungeon crawler.

CONTEXT : I created the base of the project choosing the "Super Sayian" path. In the aftermath, some parts of the
    code are a bit different with what was on the Github of Mr. Tauvel.

ADDED FEATURES :
- by pressing two directions keys, the player is able to move diagonally, without even getting a speed boost (general speed is conserved but diagonally).

- debugging tool : pressing H will show the hitbox of every SolidSprite. Press again to make them disappear

- I added the possibility to change hitbox position relative to the top-left corner of the image : that way, the hitbox is not locked with the sprite.

- by pressing K, the hero sets a bomb which explode after a certain amount of time. The explosion will remove life points to each figure in the bomb field. Moreover, if a bomb explodes, it will trigger other bombs that are in the field and make them explode instantaneously. If the bomb touches a solid element like a rock or a tree, it destroys it. The hitbox of the bomb really fit the deflagration (press H).

- by pressing J, the hero sends a fireball, which explodes after touching a solid element

- by pressing Space, the hero attacks with a sword. The range is very limited.

- added enemy patterns. The enemy has a list of point it had to visit. At each point, it settles for a short moment, then go to the next point. The bot is able to avoid solid obstacles. When the player arrives in its vision field or attack it, the bot targets the player to attack him. If the distance is to big, the target is lost.
   
- 
