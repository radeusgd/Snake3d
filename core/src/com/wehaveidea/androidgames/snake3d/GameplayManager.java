package com.wehaveidea.androidgames.snake3d;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class GameplayManager {
	public GameplayManager(Snake game) {
		this.game = game;
		for(int i=0;i<3;i++)//init size 3
			addNode();
	}
	
	public void addNode(){
		SnakeNode next = null;
		if(!nodes.isEmpty()){
			next = nodes.get(nodes.size()-1);
		}
		ModelInstance model = new ModelInstance(game.snakeModel);
		SnakeNode s = new SnakeNode(next, model);
		//TODO positions
		game.addEntity(model);
		nodes.add(s);
		
	}
	
	int counter = 0;
	int moves = 0;
	
	public void update(float dt){
		if(counter == 0){
			moves++;
			//addNode();//just kidding
			counter = 30;
			anim = 0f;
			//move nodes
			for(SnakeNode node : nodes){
				node.move(direction);//TODO direction
			}
			if(moves>3)
				//update collisions
				checkCollisions();
			//else blinking?
		}
		counter--;
		//would do physics
		interpolate(dt);
	}
	
	private void checkCollisions(){
		//TODO check for out of bounds
		
		for(SnakeNode node : nodes){
			//TODO
			//check for self-collide
			for(SnakeNode other : nodes){
				if(node==other) continue;
				//sphere r=3?
			}
			
			//check for candys
			for(Candy candy : candies){
				//sphere
			}
			
		}
	}
	
	private float anim = 0f;
	
	public void interpolate(float dt){
		//update animation counter
		assert(dt<1f);
		anim+=dt*0.00013f;//why this value??
		if(anim>1f)
			anim = anim % 1f;//anim-=1f;
		//update node positions
		for(SnakeNode node : nodes){
			node.update(anim);
		}
	}
	
	public void rotate(int keycode){
		if(direction.y!=0f){
			if(keycode == Keys.LEFT)
				direction = new Vector3(0,0,1);
			if(keycode == Keys.RIGHT)
				direction = new Vector3(0,0,-1);
			if(keycode == Keys.UP)
				direction = new Vector3(-1,0,0);
			if(keycode == Keys.DOWN)
				direction = new Vector3(1,0,0);
			direction.scl(speed);
		}else{
			if(keycode == Keys.LEFT)
				direction.rotate(90, 0,1,0);
			if(keycode == Keys.RIGHT)
				direction.rotate(-90, 0,1,0);
			if(keycode == Keys.UP)
				direction = new Vector3(0,1,0);
			if(keycode == Keys.DOWN)
				direction = new Vector3(0,-1,0);
		}
	}
	private float speed = 1.5f;
	private Vector3 direction = new Vector3(speed,0f,0f);
	private ArrayList<SnakeNode> nodes = new ArrayList<SnakeNode>();
	private ArrayList<Candy> candies = new ArrayList<Candy>();
	private Snake game;
}
