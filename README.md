# Biome Condition Extension

This extension for MythicMobs allows set biome condition on MobSpawner.

List your biomes in "biomes{b=first:biome, second:biome, etc.}":
***
***Example of MM spawner file***: 

>RandomSkeletonKing:
>MobType: SkeletonKing
>Worlds: world
>Chance: 0.001
>Priority: 1
>Action: REPLACE
>Conditions:
>- outside true
>- biomes{b=minecraft:forest,minecraft:birch_forest}
