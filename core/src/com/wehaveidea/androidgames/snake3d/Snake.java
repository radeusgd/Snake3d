package com.wehaveidea.androidgames.snake3d;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;


public class Snake implements ApplicationListener, InputProcessor {
	
	GameplayManager gameplay;
	public Model snakeModel;
	public Model candyModel;
	public Model[] planes = new Model[4];
	ModelBatch modelBatch;
	PerspectiveCamera cam;
	Environment environment;
	
	SpriteBatch spriteBatch;
	BitmapFont font;
	
	public int score = 0;
	
	private double accumulator;
    private double currentTime;
    private float step = 1.0f / 60.0f;
	
	private Array<ModelInstance> instances = new Array<ModelInstance>(); 
	
	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		
		modelBatch = new ModelBatch();
		
		spriteBatch = new SpriteBatch();
		
		font = new BitmapFont();
		
		ModelBuilder modelBuilder = new ModelBuilder();
		
		float gameArea = 16.5f*2f;
		
		snakeModel = modelBuilder.createSphere(4,4,4,
				6, 6,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position | Usage.Normal);
		candyModel = modelBuilder.createSphere(3, 3, 3,
				4, 4,
				new Material(ColorAttribute.createDiffuse(Color.RED)),
				Usage.Position | Usage.Normal);
		
		planes[0] = modelBuilder.createBox(gameArea, 0.1f, gameArea,
				new Material(ColorAttribute.createDiffuse(Color.MAROON)),
				Usage.Position | Usage.Normal);
		
		planes[1] = modelBuilder.createBox(gameArea, 0.1f, gameArea,
				new Material(ColorAttribute.createDiffuse(new Color(0.2f,1f,0.4f,0.4f))),
				Usage.Position | Usage.Normal);
		
		planes[2] = modelBuilder.createBox(gameArea, 4f*1.5f*2f, 0.1f,
				new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				Usage.Position | Usage.Normal);
		
		planes[3] = modelBuilder.createBox(0.1f, 4f*1.5f*2f, gameArea,
				new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				Usage.Position | Usage.Normal);
		
		planes[1].materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(20f, 28f, 26f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		gameplay = new GameplayManager(this);
	}

	@Override
	public void render () {
		//update
		double newTime = TimeUtils.millis() / 1000.0;
        double frameTime = Math.min(newTime - currentTime, 0.25);
        //float deltaTime = (float)frameTime;
        
        accumulator+=frameTime;

        currentTime = newTime;

        while (accumulator >= step) {
            gameplay.update(step);
            accumulator -= step;
        }
        //gameplay.interpolate((float)accumulator);//(float)(accumulator/step));//breaks the animation :(
		
		//render
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
 
        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
        
        spriteBatch.begin();
        font.setScale(2f);
        font.draw(spriteBatch, "Score: "+Integer.toString(score), 50f, 50f);
        spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		snakeModel.dispose();
		candyModel.dispose();
		
	}
	
	public void addEntity(ModelInstance instance){
		instances.add(instance);
	}
	
	public void removeEntity(ModelInstance instance){
		instances.removeValue(instance, true);
	}
	
	public void resetScene(){
		instances.clear();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.R){
			resetScene();
			gameplay = new GameplayManager(this);
		}
		if(keycode == Keys.SPACE){
			gameplay.addNode();
		}
		gameplay.rotate(keycode);
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
