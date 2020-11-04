package metroydera.com.entitys;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import metroydera.com.main.Game;
import metroydera.com.worlds.Camera;
import metroydera.com.worlds.World;

public class BulletShoot  extends Entity{
	
	private double dx;
	private double dy;
	private double speed = 2;
	private int life = 50 , curLife = 0;
	
	
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite , double dx , double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		if(World.isFreeDynamic((int)(x+(dx*speed)), (int) (y+(dy*speed)), 3, 3)) {
			x+= dx * speed;
			y+= dy * speed;		
		}else {
			Game.bullets.remove(this);
			return;
		}
		
		
		curLife++;
		if(curLife == life) {

		}
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width,height);
	}
	

}
