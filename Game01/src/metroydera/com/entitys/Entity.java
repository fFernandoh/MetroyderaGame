package metroydera.com.entitys;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import metroydera.com.main.Game;
import metroydera.com.worlds.Camera;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7*16, 16, 16, 16);
	public static BufferedImage GATE_LEVEL01 = Game.spritesheet.getSprite(0, 16, 16, 16);
	public static BufferedImage GATE_WALL_LEVEL01 = Game.spritesheet.getSprite(16, 16, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(9*16, 16, 16, 16);
	public static BufferedImage WEAPON_RIGHT = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage WEAPON_LEFT = Game.spritesheet.getSprite(9*16, 0, 16, 16);

	public int  maskX;
	public int maskY;
	public int mWidth;
	public int mHeight;
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	public boolean debug = false;
	
	public Entity(int x , int y , int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskX = 0;
		this.maskY = 0;
		this.mWidth = width;
		this.mHeight = height;
	}
	
	public void setMask(int maskX , int maskY , int mWidth , int mHeight) {
		this.maskX = maskX;
		this.maskY = maskY;
		this.mWidth = mWidth;
		this.mHeight = mHeight;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}

	public int getX() {
		return (int)this.x;
	}

	public int getY() {
		return  (int)this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
	
	
	public void render(Graphics g) {
		g.drawImage(sprite,this.getX() - Camera.x ,this.getY() - Camera.y , null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskX - Camera.x ,this.getY() + maskY - Camera.y, mWidth, mHeight);
	}
	
	public void tick() {

		}	
	
	public static boolean isColidding(Entity e1 , Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.mWidth , e1.mHeight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.mWidth , e2.mHeight);
		
		return e1Mask.intersects(e2Mask);
	}
	
	


	
	

}
