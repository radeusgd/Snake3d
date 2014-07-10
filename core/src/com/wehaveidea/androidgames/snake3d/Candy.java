package com.wehaveidea.androidgames.snake3d;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Candy {
	public Candy(Vector3 p, ModelInstance mdl) {
		model = mdl;
		pos = p;
		model.transform.set(model.nodes.get(0).globalTransform);
		model.nodes.get(0).translation.set(pos);
		model.calculateTransforms();
	}
	public ModelInstance model;
	public Vector3 getPosition(){
		return pos;
	}
	private boolean shouldDestroy = false;
	public void mark(){
		shouldDestroy = true;
	}
	public boolean shouldDestroy(){
		return shouldDestroy;
	}
	private Vector3 pos;
}
