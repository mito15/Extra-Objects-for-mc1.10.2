package com.mito.exobj.BraceBase;

import com.mito.exobj.common.MyLogger;

import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

public class BB_EventHandler {

	@SubscribeEvent
	public void onLoadWorld(WorldEvent.Load e) {
		if (e.getWorld().isRemote) {
			LoadClientWorldHandler.INSTANCE.onLoadWorld(e);
		} else {
			LoadWorldHandler.INSTANCE.onLoadWorld(e);
		}
	}

	@SubscribeEvent
	public void onUnloadWorld(WorldEvent.Unload e) {
		if (e.getWorld().isRemote) {
			LoadClientWorldHandler.INSTANCE.onUnloadWorld(e);
		} else {
			LoadWorldHandler.INSTANCE.onUnloadWorld(e);
		}
	}

	@SubscribeEvent
	public void onChunkDataSave(ChunkDataEvent.Save e) {
		if (e.getWorld().isRemote) {
		} else {
			LoadWorldHandler.INSTANCE.onChunkDataSave(e);
		}
	}

	@SubscribeEvent
	public void onChunkDataLoad(ChunkDataEvent.Load e) {
		if (e.getWorld().isRemote) {
		} else {
			LoadWorldHandler.INSTANCE.onChunkDataLoad(e);
		}

	}

	@SubscribeEvent
	public void onUpdate(TickEvent.ServerTickEvent e) {
		if (e.phase == Phase.END) {
			LoadWorldHandler.INSTANCE.onUpdate(e);
		}
	}

	@SubscribeEvent
	public void onUpdate(TickEvent.PlayerTickEvent e) {
		if (e.phase == Phase.END) {
			if (e.player.worldObj.isRemote)
				LoadClientWorldHandler.INSTANCE.onUpdate(e);
		}
	}

	@SubscribeEvent
	public void onWorldTickEvent(TickEvent.WorldTickEvent e) {
		if (e.phase == Phase.END) {
			LoadWorldHandler.INSTANCE.onWorldTickEvent(e);
		}
		if (e.side == Side.CLIENT)
			MyLogger.info("" + e.getPhase());
	}

	// 驥崎､�縺ｫ縺､縺�縺ｦ縺ｯ譛ｪ蜃ｦ逅�  unload -> save

	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load e) {
		if (e.getWorld().isRemote) {
			LoadClientWorldHandler.INSTANCE.onChunkLoad(e);
		} else {
			LoadWorldHandler.INSTANCE.onChunkLoad(e);
		}
	}

	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload e) {
		if (e.getWorld().isRemote) {
			LoadClientWorldHandler.INSTANCE.onChunkUnload(e);
		} else {
			LoadWorldHandler.INSTANCE.onChunkUnload(e);
		}
	}

	/*@SubscribeEvent
	public void PlayerInteractEvent(PlayerInteractEvent e) {
		if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			BB_RayTraceResult m2 = MitoUtil.rayTraceIncl(e.entityPlayer, 5.0, 1.0f);
			if (m2 != null) {
				if (m2.typeOfHit == BB_RayTraceResult.MovingObjectType.BRACEBASE) {
					if (m2.braceHit.rightClick(e.entityPlayer, m2.hitVec, e.entityPlayer.getCurrentEquippedItem())) {
						e.setCanceled(true);
					}
					PacketHandler.INSTANCE.sendToServer(new BB_ClickPacketProcessor(BB_ClickPacketProcessor.Mode.CLICK_RIGHT, m2.braceHit.BBID, m2.hitVec));
				}
			}
		} else if (e.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
			BB_RayTraceResult m2 = MitoUtil.rayTraceIncl(e.entityPlayer, 5.0, 1.0f);
			if (m2 != null) {
				if (m2.typeOfHit == BB_RayTraceResult.MovingObjectType.BRACEBASE) {
					if (m2.braceHit.leftClick(e.entityPlayer, m2.hitVec, e.entityPlayer.getCurrentEquippedItem())) {
						e.setCanceled(true);
					}
					PacketHandler.INSTANCE.sendToServer(new BB_ClickPacketProcessor(BB_ClickPacketProcessor.Mode.CLICK_LEFT, m2.braceHit.BBID, m2.hitVec));
				}
			}
		}
	}*/

	/*@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void GuiOpenEvent(GuiOpenEvent e) {
		if (e.gui != null) {
			mitoLogger.info("go" + e.gui.getClass().getName() + "  " + e.getPhase().toString());
			if (e.gui.getClass().getName().equals("mfw.gui.GUIFerrisConstructor")) {
			}
		} else {
			mitoLogger.info("ig" + "  " + e.getPhase().toString());
		}
	}*/

	@SubscribeEvent
	public void ContainerOpenEvent(PlayerContainerEvent e) {
		BB_GUIHandler.openEvent(e);
	}

	/*@SubscribeEvent
	public void GuiOpenEvent(InitGuiEvent e) {
		if (e.gui != null) {
			mitoLogger.info("ig" + e.gui.getClass().getName() + "  " + e.getPhase().toString());
		} else {
			mitoLogger.info("ig" + "  " + e.getPhase().toString());
		}
	}*/

}
