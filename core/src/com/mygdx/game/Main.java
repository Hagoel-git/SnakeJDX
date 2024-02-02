package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.Stack;
import java.util.Random;

public class Main extends ApplicationAdapter {
	BitmapFont font;
	SpriteBatch batch;

	int x;
	int y;
	Stack<SnakeBody> snake = new Stack<SnakeBody>(); 
	int snakeSize;

	Food food;
	Random random = new Random();
	int score;

	int sizeOfPixel;
	int gridSize;
	ShapeRenderer shapeRenderer;
	int key;

	boolean gameOver = false;
	
	@Override
	public void create () {
		font = new BitmapFont();
		batch = new SpriteBatch();

		snakeSize = 4;
		gridSize = 50;
		sizeOfPixel = Gdx.graphics.getWidth() / gridSize;
		shapeRenderer = new ShapeRenderer();

		x = gridSize / 2;
		y = gridSize / 2;

		food = new Food(random.nextInt(gridSize), random.nextInt(gridSize));
		
	}
	
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		
		Gdx.graphics.setTitle("SnakeJDX | Score: " + score);
		
		if (gameOver) {
			if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
			batch.begin();
			String text = "Game Over! Your score is: " + score + "\n Press Esc to exit";
			font.getData().setScale(2);
			GlyphLayout layout = new GlyphLayout(font, text);
			float x = (Gdx.graphics.getWidth() - layout.width) / 2;
			float y = (Gdx.graphics.getHeight() + layout.height) / 2;
			font.draw(batch, layout, x, y);
			batch.end();
			return;
		}

		if (x == food.x && y == food.y) {
			snakeSize++;
			score++;
			food.x = random.nextInt(gridSize);
			food.y = random.nextInt(gridSize);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.UP) && key != Input.Keys.DOWN) key = Input.Keys.UP;
		else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && key != Input.Keys.UP) key = Input.Keys.DOWN;
		else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && key != Input.Keys.RIGHT) key = Input.Keys.LEFT;
		else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && key != Input.Keys.LEFT) key = Input.Keys.RIGHT;
		else if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
		switch (key) {
			case Input.Keys.UP: 	if (y < gridSize-1) y += 1; else gameOver = true; break;
			case Input.Keys.DOWN: 	if (y > 0) 			y -= 1; else gameOver = true; break;
			case Input.Keys.LEFT: 	if (x > 0) 			x -= 1; else gameOver = true; break;
			case Input.Keys.RIGHT: 	if (x < gridSize-1) x += 1; else gameOver = true; break;
		}
		SnakeBody head = new SnakeBody(x, y);

		for (SnakeBody body : snake) {
			if (x == body.x && y == body.y && key != 0){
				gameOver = true;
			}
		}

		snake.push(head);
		if (snake.size() > snakeSize) {
			snake.remove(0);
		}
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		for (SnakeBody body : snake) {
			shapeRenderer.rect(body.x * sizeOfPixel, body.y * sizeOfPixel, sizeOfPixel, sizeOfPixel);
		}
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(food.x * sizeOfPixel, food.y * sizeOfPixel, sizeOfPixel, sizeOfPixel);
		shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();
		batch.dispose();
		font.dispose();

	}
}
class SnakeBody {
	int x;
	int y;
	SnakeBody(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
class Food {
	int x;
	int y;
	Food(int x, int y) {
		this.x = x;
		this.y = y;
	}
}