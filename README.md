# BattleSimulation
Battle Simulation Report

Performed by: Mikołaj Kardyś Dominik Sulik

The task of the model is to simulate a medieval battle using an agent simulation. Two teams participate in the battle and it ends when all members of one of them disappear from the board (by death or escape).

In the simulation, the distance is always calculated in a Cartesian way (the root of the sum of squares differ in coordinates). Simulation Action: In each iteration of the simulation, we perform the following operations for all agents (in random order):

We check by our action points whether we can make a move in this turn (whether their value is less than 1); if not, subtract 1 and wait; If we can perform the action, we start by calculating our current morale: For all the blows received from the previous action, we get a minus to morale equal to: For each visible ally at a distance of max. 3 we get a bonus to morale equal to: wartość_jednostki / distance. For each visible opponent at a distance of max. 3 we get a minus to morale equal to: wartość_jednostki / distance. Then, depending on the ownership of the unit and the morale parameter, we choose one of the available actions: 0)MOVE – this action can only be performed as a result of the ESCAPE or ATTACK action. It consists in moving to the given field, for the appropriate cost of action points.* 1)ATTACK – we start by choosing a target depending on the following criteria: It is close to us: the power of the criterion: 1 / distance The retort – attacked us from our last action; criterion power: 4 Its service life is lower than ours; criterion power: 2 Since his last action, he has been attacked by other members of our team (number of attacks: ); criterion power: The target in the set of opponents will be the one for whom the value of the product of these criteria is the highest. If no enemy is within our range, we choose the target from a set of all visible enemies and perform the MOVE action on the field closest to it; If there are enemies within our range, we choose the target from their collection. After the blow, * we get a number of morale points equal to the parameter wartość_jednostki this unit. If the opponent dies from this blow, we get the second as much. 2) ESCAPE – we consider all fields in our immediate vicinity. For each of them, we count its average distance from all our visible opponents. We choose the one that is the farthest away and perform the MOVEMENT action on this place. If we are directly on the edge of the board, we set the escapes flag to true; in the next iteration of the simulation we will be removed from the board. Note: If it is not possible to perform this action, and we want to perform it, then if there are enemies in our immediate vicinity, by default we will try to perform the ATTACK action. Otherwise, we will perform the WAITING action. 3) WAITING – we do not perform any of the above actions. I renew 10% of my health points and stay with the same amount of action points.

Unit types:

Unit types are based on the types of soldiers taking part in battles around the Battle of Hastings (1066).

Infantry Light troops fighting in hand. They were not professional soldiers, so their arsenal and usefulness on the battlefield were very limited.
Additional behavior: none

Interaction with other units: Due to low level of training and poor armament, units of this type will be weak against all other melee units.

Terrain interaction: Light armament makes it easy to get around. Units of this type receive a bonus for action in ordinary terrain and average penalties in the forest and in the river.

Heavy infantry Units modeled on the troops of Anglo-Saxon huskrills, dressed in heavy chain mail and armed with spears, short weapons (usually an ax, less often a sword) and a large shield, and sometimes also long Danish axes.
Additional behavior: FORMATION – units of this type in our simulation will move differently than ordinary infantry. Agents of this type will put more emphasis on holding the formation in the first stage of the clash, simulating the behavior of the formation called the Wall of Shields.

Interaction with other units: Good training, armament and discipline give them a high bonus to fight light infantry and cavalry. The slower rate of movement, both by loading and moving in formation, makes them weak against archers.

Terrain interaction: The weight of the armament means that in the open area and in the forest, units of this type receive slightly higher penalties than ordinary infantry. However, this affects the shares in the river the worst, giving them a very high penalty.

Cavalry At the turn of the first and second millennium, cavalry slowly began to take shape in knights in plate armor known to us from the end of the era. During this period, however, heavy cavalry was still not a constant sight on the battlefield. Both in the Byzantine and Norman armies there were light mounted units, performing mainly reconnaissance functions.
Additional behavior: LOOSE ARRAY – units of this type will try to keep a small distance from other allies, which makes it easier for them to maintain the ability to maneuver CONTROLLED RETREAT – due to the dominant speed of this type of troops on the battlefield, they did not have to be too afraid of chasing from other types of units. So they will gradually regain morale if they are far enough away from the enemy when trying to escape.

Interaction with other units: Armament and training give these troops an advantage over ordinary infantry, and the speed of movement and loose formation – against archers. Despite this, in melee they will not be as effective as heavy infantry, so they receive a penalty when they clash with it.

Terrain interaction: Moving on the horse's back provides these units with the highest efficiency of all in the open ground, as well as the easiest way to cross the river. However, it is an obstacle when moving around the forest, giving a high penalty in this area.

4)Archers Archers have been a popular type of unit on the battlefield since antiquity. During the invasion of England, Norman archers were an important part of William the Conqueror's army. It will be the only unit in our simulation fighting at a distance, which will allow it to reach opponents before they can inflict damage on it.

Additional behavior: HOLD THE POSITION – units of this type will not charge like the others, retaining energy to be able to attack the enemy as soon as he enters their range. STEP BACK – Archers will avoid hand-to-hand combat at all costs, performing the ESCAPE action at one time if the enemy gets too close, giving a chance to be reached by the attacks of the rest of the allies. When there are no more enemies within range, the unit will return to its place.

Interaction with other units: Units of this type receive a bonus to fight heavy infantry due to their low pace of movement, but for the same reason they also receive a penalty to fight the cavalry.

Terrain Interaction: Light armament gives these units the same mobility as light infantry. However, due to the way of attack, units of this type are the only ones to receive an additional minus to its strength when fighting in the forest.

To generate terrain, we used the following algorithms: 1)Marching squares algorithm 2)Perlin noise 3)Bresenham algorithm 4)Algorithm A*

1.Altitude Each cell on the map has an altitude parameter that determines the height of this point above sea level. We used Perlin noise to generate this feature. For each cell with x and y coordinates, altitude = noise(xterrainFrequency, yterrainFrequency), where terrainFrequency is a number in the range (0;1). The height of the terrain affects the speed of movement of all units. The rate of movement decreases when the unit goes uphill and increases when the unit goes down.

2.Forest For each of the nodal points of the cells on the map, we generated a Perlin noise jw. (with a different frequency value - forestFrequency). Then we checked all the generated values with the condition noise > = sparseForestThresh, to those points for which the condition turned out to be true we assigned the value 1, the remaining 0. We treated the resulting grid of binary values with the marching squares algorithm. As a result of this operation, we obtained structures resembling a forest, which we divided into denseForestThresh and sparse forest using the same algorithm.

3.River For each of the nodal points of the cells on the map, we generated Perlin noise, which we then subjected to threshold binarization (with obstaclesFrequency and obstaclesThresh, respectively). Then we drew two points on the borders of the map. The last step was to use the A* algorithm, which found the shortest (if any) path between points on the edges of the map. This is how the path was created is a river.

4.Visibility To move units, a function responsible for determining whether another point can be seen from a given point on the map is necessary.

To implement this method, we used the Bresenham algorithm (used to determine cells between two positions on the grid). For the points so determined, we calculated the following visibility value (initialized to zero):

if the point is in a dense forest then subtract from visibility 2 if the point is in a rare forest then subtract from visibility 1 otherwise do not subtract from the sum Then, depending on the location of the unit that observes the location and the resulting sum, we have determined the visibility of the point: if the unit is in the forest rare, and the position observed by it in the meadow, then the threshold (fromForestVisibility) responsible for noticing the field is medium, if the unit is in a meadow and the position observed by it in the forest, then the threshold (toForestVisibility) responsible for noticing the field is high, in other cases the threshold (throughForestVisibility) of visibility is low Simulation We carried out a simulation of the battle for the random terrain and with the following distribution of units: image image image

After a dozen or so seconds, the situation on the battlefield is as follows:

In the battle simulated by us, the Reds won.
![image](pic1.png)
![image](pic2.png)
![image](pic3.png)




Development opportunities 
1)adding more units from the historical period we are considering 
2)selecting more accurate unit parameters using an evolutionary algorithm 
3)introducing a command layer in which units are given top-down orders based on the situation on the entire battlefield and thanks to which units are assigned to appropriate units 
4)differentiation of terrain types

