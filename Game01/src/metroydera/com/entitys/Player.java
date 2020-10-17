package metroydera.com.entitys;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import metroydera.com.main.Game;
import metroydera.com.worlds.Camera;
import metroydera.com.worlds.World;

public class Player extends Entity{
	
	public boolean left,right,up,down;
	public int right_dir = 0 , left_dir = 1;
	public int dir = right_dir;
	public double speed = 1;
	
	private int frames = 0 , maxframes = 5 , index = 0 , maxIndex = 3;
	private boolean moved = false;
	
	public static double life =  100000;
	public static double maxLife =  100000;
	public static int level = 1;
	public static int xpNextlevel = 1;
	public static int xpMaxNextLevel = 100;
	public static int minDamage = 2;
	public static int maxDamage = minDamage * 5;
	public int mx , my;
	
	public static int ammo = 0;
	
	public boolean isDamaged = false;
	private int damagedFrames = 0;
	
	private boolean hasGun = false;
	public boolean shoot = false;
	public boolean mouseShoot = false;
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage playerDamaged;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamaged = Game.spritesheet.getSprite(128, 0, 16, 16);
		
		for(int i = 0 ; i < 4 ; i++) {
		rightPlayer[i] = Game.spritesheet.getSprite(32 +(i*16), 0, 16, 16);
		}
		
		for(int i = 0 ; i < 4 ; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 +(i*16), 16, 16, 16);
			}
	}
	
	public void tick() {
		moved = false;
		if(right && World.isFree((int)(x+speed) , this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}else if(left && World.isFree((int)(x-speed) , this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX() , (int)(y-speed))) {
			moved = true;
			y-=speed;
		}else if(down && World.isFree(this.getX() ,(int)(y+speed))) {
			moved = true;
			y+=speed;
		}	
		
		if(moved) {
			frames++;
			if(frames == maxframes) {
				frames = 0;
				index++;
				if(index > maxIndex) 
					index = 0;
			}
		}
		
		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionWeapon();
		
		if(isDamaged) {
			this.damagedFrames++;
			if(this.damagedFrames == 8) {
				this.damagedFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot) {
			shoot = false;
			
			if(hasGun && ammo > 0) {
				ammo --;
				//criar bala e atirar
				shoot = false;
				int dx = 0;
				int px = 0;
				int py = 7;
				if(dir == right_dir) {
					px = 15;
					dx = 1;
				}else {
					dx = -1;
					px = -4;
				}
				BulletShoot bullet = new BulletShoot(this.getX() + px,this.getY() +  py , 3 , 3, null , dx , 0);
				Game.bullets.add(bullet);
			}
		}
		
		if(mouseShoot) {
			mouseShoot = false;
			
			
			if(hasGun && ammo > 0) {
				ammo --;
				

				int px = 0;
				int py = 8;
				double angle = 0;
				if(dir == right_dir) {
					px = 20;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() - 8  - Camera.x));
				}else {
					px = -4;
					angle = Math.atan2(my - (this.getY()+ py - Camera.y), mx - (this.getX() - 8 - Camera.x));
				}
				
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				
				BulletShoot bullet = new BulletShoot(this.getX() + px,this.getY() +  py , 3 , 3, null , dx , dy);
				Game.bullets.add(bullet);
			}
			
		}
		
		
		
		if(life <= 0) {
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		//Camera seguindo o player centralizado
		//sistema de clamp (camera não aparece mais as laterais preta)
		
		updateCamera();
		
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDHT/2), 0 , World.WIDTH*16 - Game.WIDHT);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0 , World.HEIGHT*16 - Game.HEIGHT);
	}
	
	
	public void render(Graphics g) {
		if(!isDamaged) {
			if(dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
				//desenhar arma para direita
					g.drawImage(Entity.WEAPON_RIGHT, this.getX() + 4 - Camera.x , this.getY() - Camera.y , null);
				}
			}else if(dir == left_dir) {
				if(hasGun) {
					g.drawImage(Entity.WEAPON_LEFT, this.getX()  - 5 - Camera.x , this.getY() - Camera.y , null);
					//desenhar arma para esquerda
				}
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}else{
				g.drawImage(playerDamaged, this.getX() - Camera.x, this.getY() - Camera.y,null);
			}
	}
	
	public void checkCollisionLifePack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			
			if(atual instanceof Lifepack) {
				if(Entity.isColidding(this, atual)) {
					life+= 8;
					
					if(life >= maxLife) 
						life = maxLife;
					
					Game.entities.remove(atual);
					return;
				}
			}
		}
	}
	
	public void checkCollisionWeapon() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			
			if(atual instanceof Weapon) {
				if(Entity.isColidding(this, atual)) {
					hasGun = true;
					//System.out.println("pegou arma");
					Game.entities.remove(atual);
					return;
				}
			}
		}
	}
	
	public void checkCollisionAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			
			if(atual instanceof Bullet) {
				if(Entity.isColidding(this, atual)) {
					ammo+=1000;
					Game.entities.remove(atual);
					return;
				}
			}
		}
	}

}
