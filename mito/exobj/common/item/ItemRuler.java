package com.mito.exobj.common.item;

import java.util.List;

import com.mito.exobj.BraceBase.ChunkAndWorldManager;
import com.mito.exobj.BraceBase.BB_DataWorld;
import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.BraceBase.Brace.GuideBrace;
import com.mito.exobj.client.BB_Key;
import com.mito.exobj.client.render.RenderHighLight;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemRuler extends ItemSet {

	public byte key = 0;


	public ItemRuler() {
		super();
		this.setMaxDamage(0);
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
	}

	public void onCreated(ItemStack itemstack, World world, EntityPlayer player) {

		if (itemstack.getTagCompound() == null) {
			NBTTagCompound nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
		}
		itemstack.getTagCompound().setByte("pressedKey", (byte) 0);
	}

	public boolean activate(World world, EntityPlayer player, ItemStack itemstack, RayTraceResult movingOP, NBTTagCompound nbt, BB_Key key) {
		if (key.isShiftPressed()) {
			BB_DataWorld data = ChunkAndWorldManager.getWorldData(world);
			List<ExtraObject> list = data.braceBaseList;
			for (int n = 0; n < list.size(); n++) {
				ExtraObject base = list.get(n);
				if (base instanceof GuideBrace) {
					GuideBrace guide = (GuideBrace) base;
					if (guide.name.equals(player.getDisplayNameString())) {
						guide.setDead();
					}
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		return ("" + net.minecraft.util.text.translation.I18n.translateToLocal(this.getUnlocalizedNameInefficiently(itemstack) + ".list")).trim();
	}

	public int getDiv(ItemStack itemstack) {
		return (itemstack.getItemDamage() & (16 - 1)) + 1;
	}

	public void onActiveClick(World world, EntityPlayer player, ItemStack itemstack, RayTraceResult movingOP, Vec3d set, Vec3d end, NBTTagCompound nbt) {
		GuideBrace guide = new GuideBrace(world, set, end, 0.2, player);
		guide.addToWorld();

	}

	@Override
	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, float partialTicks, RayTraceResult mop) {
		NBTTagCompound nbt = getNBT(itemstack);
		if (mop == null)
			return false;
		Vec3d set = mop.hitVec;

		RenderHighLight rh = RenderHighLight.INSTANCE;
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().getBoolean("activated")) {
			Vec3d end = new Vec3d(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
			rh.drawRuler(player, set, end, this.getDiv(itemstack), partialTicks);
		} else {
			rh.drawCenter(player, set, partialTicks);
		}

		return true;

	}

	/*public boolean wheelEvent(EntityPlayer player, ItemStack stack, BB_Key key, int dwheel) {
		if (key.isShiftPressed()) {
			int w = dwheel / 120;
			int div = stack.getItemDamage() + w;
			if (div < 0) {
				div = 128;
			} else if (div > 128) {
				div = 0;
			}

			stack.setItemDamage(div);
			return true;
		}
		return false;
	}*/
}
