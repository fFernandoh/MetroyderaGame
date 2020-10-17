package metroydera.com.entitys;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;


import metroydera.com.main.Game;
import metroydera.com.worlds.Camera;
import metroydera.com.worlds.World;

public class Enemy extends Entity{
	
	private double speed = 1;
	
	private int maskX = 8 , maskY = 8 , maskW = 10 , maskH = 10 ;
	
	private int  frames =  0 , maxFrames = 15 , index = 0 , maxIndex = 1;
	private int life = 10;
	
	private boolean isDamaged = false;
	private int damageFrames = 10 , damageCurrent = 0;
	private int exp = 10;
	private BufferedImage image;

	private BufferedImage[] sprites;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[3];
		sprites[0]  = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1]  = Game.spritesheet.getSprite(112+16, 16, 16, 16);
		//sprites[2]  = Game.spritesheet.getSprite(6*16, 32, 16, 16);
		//sprites[3]  = Game.spritesheet.getSprite(7*16, 32, 16, 16);

	}

	public void tick() {
		
			if(Game.rand.nextInt(100) < 75) {
				if(this.isColiddingWithPlayer() == false) {
	
				if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY())
						&& !isColidding((int)(x+speed), this.getY())) {
					x+= speed;
				}else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())
						&& !isColidding((int)(x-speed), this.getY())) {
					x-= speed;
				}
				if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))
						&& !isColidding(this.getX(), (int)(y+speed))) {
					y+= speed;
				}else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))
						&& !isColidding(this.getX(), (int)(y-speed)))	 {
					y-= speed;
				}
				
				}else {
					//estamos colidindo
					
					if(Game.rand.nextInt(100) < 20 ) {
					Game.player.life -= 1;
					Game.player.isDamaged = true;
					//System.out.println("vida " + Game.player.life);
					
					if(Game.player.life <= 100) {
					}
				}
				
				frames++;
				if(frames == maxFrames) {
					frames = 0;
					index++;
					if(index > maxIndex) {
						index = 0;
					}
				}
	
					}
					}
			
			
			collidingBullet();
		
			if(life <= 0) {
				Player.xpNextlevel += 70;
				destroySelf();
				
				System.out.println("XP " + Player.xpNextlevel+"/"+Player.xpMaxNextLevel);
				
				
				if(Player.xpNextlevel >= Player.xpMaxNextLevel) {
					Player.level++;
					Player.maxLife+=15;
					Player.minDamage++;
					Player.life = Player.maxLife;
					System.out.println("Level : " + Player.level);
					System.out.println("MinDamage : " +Player.minDamage);
					System.out.println("MaxDamage : " + Player.maxDamage);
					Player.xpMaxNextLevel = Player.xpMaxNextLevel * 2;
					Player.xpNextlevel = 0;
					
					
				}
				
				return;
			}
			
			if(isDamaged) {
				this.damageCurrent++;
				if(this.damageCurrent == this.damageFrames) {
					this.damageCurrent = 0;
					this.isDamaged = false;
				}
			}
		}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	
	public void collidingBullet() {
		image = new BufferedImage(Game.HEIGHT, Game.HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.red);
		for(int i = 0; i < Game.bullets.size() ; i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof BulletShoot) {
				if(Entity.isColidding(this, e)) {
					
					Random rand = new Random();
					int nextDamage = Player.level * rand.nextInt(Player.maxDamage);
					System.out.println("dano : " + nextDamage);
					
					if(nextDamage == Player.maxDamage) {
						nextDamage = Player.maxDamage * 2;
						System.out.println("dano critico : " + nextDamage);
					}
					isDamaged = true;
					life = life - (int)nextDamage;
					Game.bullets.remove(i);
				
					return;
				}
			}
		}
			
	}
	
	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskY , this.getY() + maskY, maskW , maskH);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY() , 16, 16);
		
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColidding(int xNext , int yNext) {
		Rectangle enemyCurrent = new Rectangle(xNext + maskX, yNext + maskY , maskW , maskH);
		
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e  = Game.enemies.get(i);
			if(e == this) 
				continue;

			Rectangle targetEnemy = new Rectangle(e.getX() + maskX , e.getY() + maskY , maskW , maskH);
			
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void render(Graphics g) {
		if(!isDamaged) {
			g.drawImage(sprites[index], this.getX()  - Camera.x, this.getY() - Camera.y , null);
		}else {
		g.drawImage(Entity.ENEMY_FEEDBACK, this.getX()  - Camera.x, this.getY() - Camera.y , null);
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskY - Camera.x, this.getY() + maskY - Camera.y, maskW, maskH);
	}
	}
}
