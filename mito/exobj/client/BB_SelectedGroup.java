package com.mito.exobj.client;

import java.util.ArrayList;
import java.util.List;

import com.mito.exobj.common.main.mitoClientProxy;
import com.mito.exobj.network.SyncPacketProcessor;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.mito.exobj.client.render.BB_Render;
import com.mito.exobj.BraceBase.BB_RenderHandler;
import com.mito.exobj.BraceBase.BB_ResisteredList;
import com.mito.exobj.BraceBase.CreateVertexBufferObject;
import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.BraceBase.LoadClientWorldHandler;
import com.mito.exobj.BraceBase.VBOHandler;
import com.mito.exobj.BraceBase.Brace.Brace;
import com.mito.exobj.common.Main;
import com.mito.exobj.network.GroupPacketProcessor;
import com.mito.exobj.network.GroupPacketProcessor.EnumGroupMode;
import com.mito.exobj.network.PacketHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class BB_SelectedGroup {

	public List<ExtraObject> list = new ArrayList<>();
	public mitoClientProxy proxy;
	public Vec3d set = new Vec3d(0, 0, 0);
	public boolean activated = false;
	private boolean modecopy = false;
	public int pasteNum = 0;
	public int size = 100;
	public int rot = 0;
	public boolean modeMove = false;
	public boolean modeBlock = false;

	public BB_SelectedGroup(mitoClientProxy px) {
		this.proxy = px;
	}

	public void initNum() {
		size = 100;
		rot = 0;
	}

	public void addShift(ExtraObject... bases) {
		initNum();
		for (int n = 0; n < bases.length; n++) {
			if (this.list.contains(bases[n])) {
				this.list.remove(bases[n]);
			} else {
				this.list.add(bases[n]);
			}
		}
	}

	public void addShift(List<ExtraObject> bases) {
		initNum();
		for (int n = 0; n < bases.size(); n++) {
			if (this.list.contains(bases.get(n))) {
				this.list.remove(bases.get(n));
			} else {
				this.list.add(bases.get(n));
			}
		}
	}

	public void replace(List<ExtraObject> bases) {
		initNum();
		this.list = bases;
	}

	public void replace(ExtraObject base) {
		initNum();
		this.list.clear();
		this.list.add(base);
	}

	public void remove(ExtraObject... bases) {
		initNum();
		for (int n = 0; n < bases.length; n++) {
			this.list.remove(bases[n]);
		}
	}

	public void delete() {
		initNum();
		this.list.clear();
	}

	public List<ExtraObject> getList() {
		return this.list;
	}

	public boolean drawHighLightGroup(EntityPlayer player, float partialticks) {
		if (this.list.isEmpty()) {
			return false;
		}
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialticks),
				-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialticks),
				-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialticks));
		float size = 2.0F;

		BraceHighLightHandler data = ((mitoClientProxy) Main.proxy).bh;
		if (data.key == null || !data.key.equals(this)) {
			data.buffer.delete();
			CreateVertexBufferObject c = CreateVertexBufferObject.INSTANCE;
			c.beginRegist(GL15.GL_STATIC_DRAW, GL11.GL_TRIANGLES);
			c.setColor(1.0F, 1.0F, 1.0F, 1.0F);
			for (ExtraObject base : list) {
				BB_Render render = BB_ResisteredList.getBraceBaseRender(base);
				render.updateRender(c, base);
			}
			data.key = this;
			VBOHandler vbo = c.end();
			data.buffer.add(vbo);
		}

		GL11.glPushMatrix();

		GL11.glLineWidth(size);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
		BB_RenderHandler.enableClient();
		data.buffer.draw(GL11.GL_LINE_LOOP);
		BB_RenderHandler.disableClient();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();

		GL11.glPopMatrix();
		return true;
	}

	public boolean drawHighLightCopy(EntityPlayer player, float partialticks, RayTraceResult mop) {
		if (this.list.isEmpty()) {
			return false;
		}
		for (int n = 0; n < this.list.size(); n++) {
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialticks),
					-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialticks),
					-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialticks));
			Vec3d pos = this.getDistance(mop);
			GL11.glTranslated(pos.xCoord, pos.yCoord, pos.zCoord);
			BB_Render render = BB_ResisteredList.getBraceBaseRender(this.list.get(n));
			render.drawHighLight(this.list.get(n), partialticks);//4.0F
			GL11.glPopMatrix();
		}
		return true;
	}

	public boolean modeCopy() {
		return modecopy;
	}

	public void setcopy(boolean modecopy) {
		this.modecopy = modecopy;
	}

	public Vec3d getDistance(RayTraceResult mop) {
		if (list.isEmpty()) {
			return null;
		}
		Vec3d c = getCenter();
		return mop.hitVec.addVector(-c.xCoord, -c.yCoord, -c.zCoord);
	}

	public void applyProperty(Block tex, int color, String shape) {
		for (ExtraObject e : list) {
			if (e instanceof Brace) {
				Brace brace = ((Brace) e);
				if (tex != null) {
					brace.texture = tex;
				}
				if (shape != null) {
					brace.shape = shape;
					//brace.shouldUpdateRender = true;
				}
				if (color >= 0 && color < 16) {
					brace.color = color;
				} else if (color == 16) {
				} else {
					brace.color = 0;
				}
				PacketHandler.INSTANCE.sendToServer(new SyncPacketProcessor(e));
			}
		}
	}

	public void applyColor(int color) {
		for (ExtraObject e : list) {
			if (e instanceof Brace) {
				Brace brace = ((Brace) e);

				if (color >= 0 && color < 16) {
					brace.color = color;
				} else if (color == 16) {
				} else {
					brace.color = 0;
				}
				PacketHandler.INSTANCE.sendToServer(new SyncPacketProcessor(e));
			}
		}
	}

	public void init() {
		this.delete();
		this.activated = false;
		this.modecopy = false;
		this.modeMove = false;
	}

	public void applySize(int isize) {
		for (ExtraObject e : list) {
			if (e instanceof Brace) {
				Brace brace = ((Brace) e);
				brace.size = (double) isize * 0.05;
				LoadClientWorldHandler.INSTANCE.data.shouldUpdateRender = true;
				PacketHandler.INSTANCE.sendToServer(new SyncPacketProcessor(e));
			}
		}
	}

	public int getSize() {
		if (this.list.isEmpty()) {
			return -1;
		} else if (this.list.size() == 1 && list.get(0) instanceof Brace) {
			return (int) (((Brace) list.get(0)).size * 20);
		} else {
			int is = (int) (((Brace) list.get(0)).size * 20);
			for (ExtraObject e : list) {
				if (e instanceof Brace) {
					Brace brace = ((Brace) e);
					if (is != (int) (brace.size * 20)) {
						return -1;
					}
				}
			}
			return is;
		}
	}

	public void applyRoll(int iroll) {
		for (ExtraObject e : list) {
			if (e instanceof Brace) {
				Brace brace = ((Brace) e);
				brace.setRoll(iroll);
				LoadClientWorldHandler.INSTANCE.data.shouldUpdateRender = true;
				PacketHandler.INSTANCE.sendToServer(new SyncPacketProcessor(e));
			}
		}
	}

	public void applyGroupSize(int isize) {
		Vec3d c = getCenter();
		for (ExtraObject e : list) {
			ExtraObject brace = e;
			brace.resize(c, (double) isize / this.size);
			LoadClientWorldHandler.INSTANCE.data.shouldUpdateRender = true;
			PacketHandler.INSTANCE.sendToServer(new SyncPacketProcessor(e));

		}
		this.size = isize;
	}

	public void applyGroupRot(int irot) {
		Vec3d c = getCenter();
		for (ExtraObject e : list) {
			ExtraObject brace = e;
			brace.rotation(c, -this.rot + irot);
			LoadClientWorldHandler.INSTANCE.data.shouldUpdateRender = true;
			PacketHandler.INSTANCE.sendToServer(new SyncPacketProcessor(e));

		}
		this.rot = irot;
	}

	public Vec3d getCenter() {
		//return Vec3d.createVectorHelper(bases.get(0).pos.xCoord, this.getMinY(), bases.get(0).pos.zCoord);
		return set;
	}

	public void setmove(boolean b) {
		this.modeMove = b;
	}

	public void breakGroup() {
		/*for (int n = 0; n < getList().size(); n++) {
			ExtraObject base = getList().get(n);
			base.breakBrace(Main.proxy.getClientPlayer());
		}*/
		PacketHandler.INSTANCE.sendToServer(new GroupPacketProcessor(EnumGroupMode.DELETE, getList()));
	}

	public void grouping() {
		PacketHandler.INSTANCE.sendToServer(new GroupPacketProcessor(EnumGroupMode.GROUPING, getList()));
		this.delete();
	}

	public void setblock(boolean b) {
		this.modeBlock = b;
	}

}
