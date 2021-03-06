package com.mito.exobj.common.main;

import com.mito.exobj.BraceBase.CreateVertexBufferObject;
import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.client.BB_Key;
import com.mito.exobj.client.BB_SelectedGroup;
import com.mito.exobj.common.block.TileObjects;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class mitoCommonProxy {

	public BB_SelectedGroup sg;

	public mitoCommonProxy() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	public boolean isControlKeyDown() {
		return false;
	}

	public boolean isShiftKeyDown() {
		return false;
	}

	public boolean isAltKeyDown() {
		return false;
	}

	public World getClientWorld() {
		return null;
	}

	public BB_Key getKey() {
		return new BB_Key(0);
	}

	public void init() {
		GameRegistry.registerTileEntity(TileObjects.class, "TileObjects");
	}

	public void preInit() {
	}

	public EntityPlayer getClientPlayer() {
		return null;
	}

	public void playSound(ResourceLocation rl, float vol, float pitch, float x, float y, float z) {
	}

	public abstract void playSound(SoundEvent se, SoundCategory sc, float vol, float pitch, float x, float y, float z);

	public void addDiggingEffect(World world, Vec3d center, double d0, double d1, double d2, Block block, int color) {
	}

	public void particle(ExtraObject brace) {
	}
}
