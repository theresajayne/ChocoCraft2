package uk.co.haxyshideout.chococraft2.spawner;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.biome.BiomeGenBase;
import uk.co.haxyshideout.chococraft2.entities.EntityChocobo;
import uk.co.haxyshideout.haxylib.utils.RandomHelper;
import uk.co.haxyshideout.haxylib.utils.WorldHelper;

import java.util.List;

/**
 * Created by clienthax on 2/6/2015.
 */
public class ChocoboSpawner {//TODO This whole thing was adapted from the ancient code, could prob do with a whole new system at some point

	static final int MAX_ATTEMPTS = 10;
	static final int INNER_SPAWN_RADIUS = 32;
	static final int NORMAL_OUTER_SPAWN_RADIUS = 64;
	static final int NETHER_OUTER_SPAWN_RADIUS = 48;


	public static void doChocoboSpawning(World world, BlockPos pos) {

		if(!RandomHelper.getChanceResult(50))//TODO config options
			return;

		int outerSpawnRadius = NORMAL_OUTER_SPAWN_RADIUS;


		if(WorldHelper.isHellWorld(world))
		{
			outerSpawnRadius = NETHER_OUTER_SPAWN_RADIUS;
		}

		// check up to maxTries random positions for correct spawn biome
		int randDeltaX = 0;
		int randDeltaZ = 0;
		boolean canSpawnHere = false;

		for(int i = 0; i < MAX_ATTEMPTS; i++) {
			randDeltaX = world.rand.nextInt(outerSpawnRadius) + INNER_SPAWN_RADIUS;
			randDeltaZ = world.rand.nextInt(outerSpawnRadius) + INNER_SPAWN_RADIUS;
			//select random quadrant
			if(world.rand.nextBoolean())
				randDeltaX *= -1;
			if(world.rand.nextBoolean())
				randDeltaZ *= -1;

			if(WorldHelper.isHellWorld(world))
				canSpawnHere = true;//purple chocobos only..
			else
				canSpawnHere = canChocoboSpawnInBiome(world, pos.add(randDeltaX, 0, randDeltaZ));//Normal chocobos

			if(isOtherPlayerNear(world, pos.add(randDeltaX, 0, randDeltaZ), INNER_SPAWN_RADIUS / 2))
				canSpawnHere = false;
			if(canSpawnHere)
				break;//Position found
		}

		if(!canSpawnHere)
			return;

		int chunkPosX = MathHelper.floor_double(pos.getX() + randDeltaX);
		int chunkPosZ = MathHelper.floor_double(pos.getZ() + randDeltaZ);
		BlockPos chunkPos = new BlockPos(chunkPosX, 0, chunkPosZ);
		int wildInChunks = countWildInChunkRadius(world, chunkPos, 2);//TODO configs
		if(wildInChunks > 5)//TODO configs
			return;

		int distanceNextWild = (int) distanceToNextWild(world, chunkPos);
		if(distanceNextWild < 80)//TODO configs
			return;

		int spawnedChocobos = 0;
		int randomGroupSize = 5;//TODO configs
		int groupSizeDelta = 10 - 5;//TODO configs
		if(groupSizeDelta > 0)
			randomGroupSize += world.rand.nextInt(groupSizeDelta);

		for(int i = 0; i < MAX_ATTEMPTS; i++) {
			BlockPos chocoPos = pos.add(randDeltaX + world.rand.nextInt(6), 0, randDeltaZ + world.rand.nextInt(6));
			if(WorldHelper.isHellWorld(world))
				chocoPos = WorldHelper.getFirstSolidWithAirAbove(world, chocoPos);
			else
				chocoPos = world.getTopSolidOrLiquidBlock(chocoPos);

			if(chocoPos == null || !canChocoboSpawnAtLocation(world, chocoPos))
				continue;

			int chocoRotYawn = world.rand.nextInt(360);
			EntityChocobo.ChocoboColor chocoboColor = WorldHelper.isHellWorld(world) ? EntityChocobo.ChocoboColor.PURPLE : EntityChocobo.ChocoboColor.YELLOW;
			EntityChocobo newChocobo = new EntityChocobo(world);
			newChocobo.setColor(chocoboColor);
			newChocobo.setLocationAndAngles(chocoPos.getX(), chocoPos.getY() + 1, chocoPos.getZ(), chocoRotYawn, 0.0F);
			world.spawnEntityInWorld(newChocobo);
//			System.out.println("spawned chobobo at "+chocoPos.toString());
			spawnedChocobos++;
			if(spawnedChocobos >= randomGroupSize)
				break;
		}

	}

	private static int countWildInChunkRadius(World world, BlockPos pos, int chunkRadius) {
		int amount = 0;
		List<EntityChocobo> chocoboList = world.getEntitiesWithinAABB(EntityChocobo.class, new AxisAlignedBB(pos.add(-chunkRadius * 16, 0, -chunkRadius * 16), pos.add(chunkRadius * 16, 0, chunkRadius * 16)));
		for(EntityChocobo chocobo: chocoboList)
			if(!chocobo.isTamed())
				amount++;
		return amount;
	}

	private static boolean isOtherPlayerNear(World world, BlockPos pos, int distance) {
		if(world.provider instanceof WorldProviderHell)
			pos = WorldHelper.getFirstSolidWithAirAbove(world, pos);
		else
			pos = world.getTopSolidOrLiquidBlock(pos);
		if(pos == null)
			return true;//Cancels the spawn
		return world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), distance) != null;
	}

	private static boolean canChocoboSpawnAtLocation(World world, BlockPos pos) {
		int isValid = 0;
		if(!WorldHelper.isNormalCubesAround(world,pos.up()) && !WorldHelper.isNormalCubesAround(world,pos.up()))
		{
			//Clear up
			isValid += 2;
		}
		if(WorldHelper.isNormalCubesAround(world,pos.down()) && !WorldHelper.isBlockAtPositionLiquid(world,pos))
		{
			//Clear Down
			isValid += 2;
		}
		if(isValid == 4)
		{
			System.out.println("Spawned Chocobo");
			return true;
		}
		return false;
	}

	private static boolean canChocoboSpawnInBiome(World world, BlockPos pos) {
		BiomeGenBase biomeGenBase = world.getBiomeGenForCoords(pos);
		//TODO check the names against a list in config, + autogen the list
		return true;
	}

	public static double distanceToNextWild(World world, BlockPos pos) {
		double closest = 10000d;
		List<EntityChocobo> chocoboList = world.getEntitiesWithinAABB(EntityChocobo.class, new AxisAlignedBB(new BlockPos(pos.getX() - 100, 0, pos.getZ() - 100), new BlockPos(pos.getX() + 100, 255, pos.getZ() + 100)));//get a list of all chocobos in a 100 radius
		for(EntityChocobo chocobo : chocoboList) {
			if (!chocobo.isTamed()) {
				double tempDistance = chocobo.getDistanceSq(pos);
				if(tempDistance < closest)
					closest = tempDistance;
			}
		}
		return Math.floor(Math.sqrt(closest));
	}

}
