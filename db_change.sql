ALTER TABLE  `item` ADD  `x` INT NULL DEFAULT NULL ,
ADD  `y` INT NULL DEFAULT NULL ,
ADD  `layer` INT NULL DEFAULT NULL

UPDATE `item` join room_thing on item.interior_id=room_thing.id SET item.`x`=room_thing.x,item.`y`=room_thing.y,item.`layer`=room_thing.layer where item.place='home' or item.place='building' ;
