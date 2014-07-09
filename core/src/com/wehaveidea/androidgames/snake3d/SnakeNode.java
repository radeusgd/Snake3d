package com.wehaveidea.androidgames.snake3d;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class SnakeNode {
	private SnakeNode nextNode = null;
	public ModelInstance model;
	private Vector3 gridPosition;
	private Vector3 previousPosition;
	
	public SnakeNode(SnakeNode next, ModelInstance model) {
		this.model = model;
		nextNode = next;
		Vector3 pos;
		if(nextNode==null)
			pos = new Vector3(0,0,0);
		else
			pos = nextNode.gridPosition.cpy();//TODO or current?
		gridPosition = pos;
		previousPosition = pos;
	}
	
	public void move(Vector3 dir){
		previousPosition = gridPosition.cpy();
		if(nextNode == null){//if it's head
			gridPosition.add(dir);
		}else{
			gridPosition = nextNode.previousPosition.cpy();
		}
	}
	
	public void update(float anim){
		Vector3 pos = previousPosition.cpy();
		pos.lerp(gridPosition.cpy(), anim);
		//for(int i=0;i<model.nodes.size;i++){//should be 1?
			com.badlogic.gdx.graphics.g3d.model.Node n = model.nodes.get(0);
			model.transform.set(n.globalTransform);
			n.translation.set(pos);
			n.rotation.idt();
			model.calculateTransforms();
		//}
	}
	
}
