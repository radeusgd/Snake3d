package com.wehaveidea.androidgames.snake3d;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameplayManager {
	public GameplayManager(Snake game) {
		this.game = game;
		//for(int i=0;i<3;i++)
			addCandy();
		for(int i=0;i<3;i++)//init size 3
			addNode();
		ModelInstance [] planes = new ModelInstance[4];
		for(int i=0;i<4;i++){
			planes[i] = new ModelInstance(game.planes[i]);
			game.addEntity(planes[i]);
		}
		planes[0].nodes.get(0).translation.set(0f,0f,0f);
		planes[1].nodes.get(0).translation.set(0f,4f*1.5f,0f);
		planes[2].nodes.get(0).translation.set(0f,4f*1.5f,-15f);
		planes[3].nodes.get(0).translation.set(-15f,4f*1.5f,0f);
		for(int i=0;i<4;i++){
			planes[i].calculateTransforms();
		}
	}
	
	float gameSize = 10;//XZ size, Y=4
	Random random = new Random();
	
	public void addCandy(){
		ModelInstance model = new ModelInstance(game.candyModel);
		Vector3 pos = new Vector3(random.nextFloat()*(gameSize*speed*0.5f)+0.5f*gameSize,random.nextFloat()*(gameSize*speed*0.5f)+0.5f*gameSize,random.nextFloat()*(4f*speed*0.5f));
		pos.x -= pos.x % speed;
		pos.y -= pos.y % speed;
		pos.z -= pos.z % speed;
		Candy candy = new Candy(pos, model);
		candies.add(candy);
		game.addEntity(candy.model);
	}
	
	public void addNode(){
		SnakeNode next = null;
		if(!nodes.isEmpty()){
			next = nodes.get(nodes.size()-1);
		}
		ModelInstance model = new ModelInstance(game.snakeModel);
		SnakeNode s = new SnakeNode(next, model);
		game.addEntity(model);
		nodes.add(s);
		
	}
	
	int counter = 0;
	int moves = 0;
	
	public void update(float dt){
		if(counter == 0){
			if(moves>3)
				//update collisions
				checkCollisions();
			else
				moves++;
			
			//addNode();//
			
			counter = 30;
			anim = 0f;
			//move nodes
			for(SnakeNode node : nodes){
				node.move(direction);
			}
			
		}
		counter--;
		//would do physics
		interpolate(dt);
	}
	
	private boolean collideObjects(Vector3 o1, Vector3 o2, float r){
		if(o1.dst2(o2)<r*r) return true;
		return false;
	}
	
	private void checkCollisions(){
		//TODO check for out of bounds
		int scored = 0;
		for(SnakeNode node : nodes){
			//check for self-collide
			boolean gameover = false;
			for(SnakeNode other : nodes){
				if(node==other) continue;
				if(collideObjects(node.getPosition(), other.getPosition(),1f)){
					System.out.println("GameOver");
					gameover = true;
					break;
				}
			}
			if(gameover){
				game.resetScene();
				nodes.clear();
				break;
			}
			
			//check for candys
			for(Candy candy : candies){
				//sphere
				if(collideObjects(node.getPosition(), candy.getPosition(),1.5f+2.5f)){
					scored++;
					candy.mark();
				}
			}
		}
		if(scored>0){
			System.out.println("Scored!");
			addNode();
			Array<Candy> toRemove = new Array<Candy>();
			for(Candy candy : candies){
				if(!candy.shouldDestroy()) continue;
				toRemove.add(candy);
				//TODO score
			}
			for(Candy candy : toRemove){
				candies.remove(candy);
				game.removeEntity(candy.model);
				addCandy();
			}
		}
	}
	
	private float anim = 0f;
	
	public void interpolate(float dt){
		//update animation counter
		anim+=1f/30f;//dt*speed;//why this value??
		//if(anim>1f)
			anim = 1f;//anim-=1f;
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
