package com.mito.exobj.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

public abstract class TransInfo implements Opcodes {

	public String targetMethodName;
	public String targetDeobfMethodName;
	public String targetMethoddesc;
	public String targetClassName;

	public void transform(MethodNode mnode) {
	}

}
