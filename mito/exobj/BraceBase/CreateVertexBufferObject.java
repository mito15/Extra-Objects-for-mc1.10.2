package com.mito.exobj.BraceBase;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import com.mito.exobj.client.render.model.Mat4;
import com.mito.exobj.client.render.model.Vertex;
import com.mito.exobj.common.MyLogger;

import net.minecraft.util.math.Vec3d;

public class CreateVertexBufferObject {

	public static CreateVertexBufferObject INSTANCE = new CreateVertexBufferObject();
	public List<Float> vertexDataList = new ArrayList<Float>();
	public List<Float> brightnessDataList = new ArrayList<Float>();
	private int size = 0;
	private double scale = 1;
	private float[] colorArray = new float[4];
	private float[] textureArray = new float[2];
	private float[] normalArray = new float[3];
	private float[] brightnessArray = new float[3];
	// 0:all 1:no color 2:no texture
	private int mode;
	private int usage;
	private VBOHandler p;
	private boolean hasBrightness = false;
	public Mat4 trans = new Mat4();
	private List<Mat4> stack = new ArrayList<Mat4>();

	//usage : glBufferData
	//mode : gldrawMode
	public void beginRegist(int usage, int mode) {
		this.p = new VBOHandler();
		this.size = 0;
		this.vertexDataList.clear();
		this.brightnessDataList.clear();
		this.hasBrightness = false;
		this.usage = usage;
		this.p.usage = usage;
		this.mode = mode;
		this.p.mode = mode;
		this.scale = 1;
		trans = new Mat4();
	}

	public VBOHandler end() {
		float[] vertexData = ArrayUtils.toPrimitive(vertexDataList.toArray(new Float[0]));

		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexData.length);
		vertexBuffer.put(vertexData);
		vertexBuffer.flip();

		this.p.size = this.size;
		this.p.buffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.p.buffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, this.usage);

		if (this.hasBrightness) {
			float[] brightnessData = ArrayUtils.toPrimitive(brightnessDataList.toArray(new Float[0]));

			FloatBuffer brightnessBuffer = BufferUtils.createFloatBuffer(brightnessData.length);
			brightnessBuffer.put(brightnessData);
			brightnessBuffer.flip();

			this.p.brightness = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.p.brightness);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, brightnessBuffer, GL15.GL_DYNAMIC_DRAW);
		} else {
			this.p.brightness = 0;
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		return this.p;
	}

	public void registVertex(float f, float g, float h) {
		float n1 = this.normalArray[0];
		float n2 = this.normalArray[1];
		float n3 = this.normalArray[2];

		Vec3d vec = new Vec3d(f, g, h);
		Vec3d vec1 = new Vec3d(n1, n2, n3);
		vec = trans.transformVec3d(vec);
		vec1 = trans.transformVec3d(vec1).normalize();
		f = (float) vec.xCoord;
		g = (float) vec.yCoord;
		h = (float) vec.zCoord;
		n1 = (float) vec1.xCoord;
		n2 = (float) vec1.yCoord;
		n3 = (float) vec1.zCoord;
		MyLogger.info("cvbo normal " + n1 + " " + n2 + " " + n3);

		List<Float> list;
		this.size++;
		list = Arrays.asList(f, g, h, this.textureArray[0], this.textureArray[1], n1, n2, n3, this.colorArray[0], this.colorArray[1], this.colorArray[2], this.colorArray[3]);
		vertexDataList.addAll(list);
		brightnessDataList.add(this.brightnessArray[0]);
		brightnessDataList.add(this.brightnessArray[1]);
	}

	public void registVertex(double f, double g, double h) {
		registVertex((float) f, (float) g, (float) h);
	}

	public void registVertexWithUV(float f, float g, float h, float i, float j) {
		float n1 = this.normalArray[0];
		float n2 = this.normalArray[1];
		float n3 = this.normalArray[2];

		Vec3d vec = new Vec3d(f, g, h);
		Vec3d vec1 = new Vec3d(n1, n2, n3);
		vec = trans.transformVec3d(vec);
		vec1 = trans.transformNormal(vec1).normalize();
		f = (float) vec.xCoord;
		g = (float) vec.yCoord;
		h = (float) vec.zCoord;
		n1 = (float) vec1.xCoord;
		n2 = (float) vec1.yCoord;
		n3 = (float) vec1.zCoord;

		List<Float> list;
		this.size++;
		list = Arrays.asList(f, g, h, i, j, n1, n2, n3, this.colorArray[0], this.colorArray[1], this.colorArray[2], this.colorArray[3]);
		vertexDataList.addAll(list);
		brightnessDataList.add(this.brightnessArray[0]);
		brightnessDataList.add(this.brightnessArray[1]);
	}

	public void registVertexWithUV(double f, double g, double h, double i, double j) {
		registVertexWithUV((float) f, (float) g, (float) h, (float) i, (float) j);
	}

	public void registVertexWithUV(Vec3d vec, double u, double v) {
		registVertexWithUV(vec.xCoord, vec.yCoord, vec.zCoord, u, v);
	}

	public void registVertexWithUV(Vertex v) {
		registVertexWithUV(v.pos, v.u, v.v);
	}

	public void setBrightness(float f, float g) {
		this.hasBrightness = true;
		this.brightnessArray[0] = f;
		this.brightnessArray[1] = g;
	}

	public void setBrightness(int i) {
		int j = i % 65536;
		int k = i / 65536;
		this.hasBrightness = true;
		this.brightnessArray[0] = (float) j;
		this.brightnessArray[1] = (float) k;
	}

	public void setTextureUV(float f, float g) {
		this.textureArray[0] = f;
		this.textureArray[1] = g;
	}

	public void setNormal(float f, float g, float h) {
		this.normalArray[0] = f;
		this.normalArray[1] = g;
		this.normalArray[2] = h;
	}

	public void setNormal(double f, double g, double h) {
		this.normalArray[0] = (float) f;
		this.normalArray[1] = (float) g;
		this.normalArray[2] = (float) h;
	}

	public void setColor(float f, float g, float h, float i) {
		this.colorArray[0] = f;
		this.colorArray[1] = g;
		this.colorArray[2] = h;
		this.colorArray[3] = i;
	}

	public void setColor(float f, float g, float h) {
		this.colorArray[0] = f;
		this.colorArray[1] = g;
		this.colorArray[2] = h;
		this.colorArray[3] = 1.0f;
	}

	public void setColor(int j) {
		float f = (float) (j >> 16 & 255) / 255.0F;
		float g = (float) (j >> 8 & 255) / 255.0F;
		float h = (float) (j & 255) / 255.0F;
		setColor(f, g, h);
	}

	public void setScale(double size) {
		this.scale = size;

	}

	public void rpyRotate(double r, double p, double y) {
		trans.rpyRotation(r, p, y);
	}

	public void translate(double x, double y, double z) {
		trans.translate(x, y, z);
	}

	public void translate(Vec3d v) {
		trans.translate(v);
	}

	public void scale(double x, double y, double z) {
		trans.scale(x, y, z);
	}

	public void rotate(double a, double x, double y, double z) {
		trans.rotate(a, x, y, z);
	}

	public void rotate(double a, Vec3d v) {
		trans.rotate(a, v);
	}

	public void setNormal(Vec3d v) {
		this.setNormal(v.xCoord, v.yCoord, v.zCoord);
	}

	public void popMatrix() {
		int n = stack.size() - 1;
		if (n >= 0) {
			trans = stack.get(n);
			stack.remove(n);
		}
	}

	public void pushMatrix() {
		stack.add(trans.copy());
	}

	public void transform(Mat4 rotationMatrix) {
		trans.transMat(rotationMatrix);
	}

}
