package metroydera.com.worlds;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import metroydera.com.entitys.Bullet;
import metroydera.com.entitys.Enemy;
import metroydera.com.entitys.Entity;
import metroydera.com.entitys.Gate;
import metroydera.com.entitys.GateWall;
import metroydera.com.entitys.Lifepack;
import metroydera.com.entitys.Player;
import metroydera.com.entitys.Weapon;
import metroydera.com.graphics.Spritesheet;
import metroydera.com.main.Game;


public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;


	
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0 , map.getWidth() , map.getHeight() , pixels , 0 , map.getWidth());
			
		for( int xx = 0; xx < map.getWidth() ; xx ++) {
			for(int yy = 0 ; yy < map.getHeight() ; yy++) {
				int pixelAtual = pixels[xx + (yy * map.getWidth())];
				tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
				if(pixelAtual == 0xFF000000) {
					//floor
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
				}else if( pixelAtual == 0xFFFFFFFF) {
					//wall
					tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
				}else if( pixelAtual == 0XFF0026FF) {
					//inicializa o player no local com cor 0026FF
					Game.player.setX(xx*16);
					Game.player.setY(yy*16);
					//player
				}else if(pixelAtual == 0xFFFF0000) {
					//enemies
					Enemy en = new Enemy(xx*16,yy*16,16,16,Entity.ENEMY_EN);
					Game.entities.add(en);
					Game.enemies.add(en);
					
				}else if(pixelAtual == 0xFFFF006E) {
					Game.entities.add(new Weapon(xx*16, yy*16, 16 ,16 , Entity.WEAPON_EN));
					//weapow
				}else if(pixelAtual == 0xFF00FF90) {
					Lifepack lifePack = new Lifepack(xx*16, yy*16, 16 ,16 , Entity.LIFEPACK_EN);
					Game.entities.add(lifePack);
					//lifepack
				}else if(pixelAtual == 0xFFFFD800) {
					Game.entities.add(new Bullet(xx*16, yy*16, 16 ,16 , Entity.BULLET_EN));
					//bullet
				}else if (pixelAtual == 0xFFFF00FF){
					//gatewall lvl 01
					Game.entities.add(new Gate(xx*16, yy*16, 16 ,16 , Entity.GATE_WALL_LEVEL01));
				}else if (pixelAtual == 0xFF6100FF){
					//gate
					Game.entities.add(new GateWall(xx*16, yy*16, 16, 16, Entity.GATE_LEVEL01));
				}
			}
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static boolean isFreeDynamic(int xNext , int yNext , int width , int height) {
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE; 
		
		int x2 = (xNext + width - 1) / TILE_SIZE;
		int y2 = yNext / TILE_SIZE; 
		
		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext + height - 1) / TILE_SIZE;
		
		int x4 = (xNext + width - 1) / TILE_SIZE;
		int y4 = (yNext + height - 1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
	}
	
	public static boolean isFree(int xNext , int yNext) {
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE; 
		
		int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = yNext / TILE_SIZE; 
		
		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		
		int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1 * World.HEIGHT)] instanceof WallTile) ||
				(tiles[x2 + (y2 * World.HEIGHT)] instanceof WallTile) ||
				(tiles[x3 + (y3 * World.HEIGHT)] instanceof WallTile) ||
				(tiles[x4 + (y4 * World.HEIGHT)] instanceof WallTile));
	}
	
	public static void restartGame(String level) {
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		Game.minimap = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Game.minimapPixels = ((DataBufferInt)Game.minimap.getRaster().getDataBuffer()).getData();
		
		return;
	}
	
	public void render(Graphics g) {
		int xstart = Camera.x / 16;
		int ystart = Camera.y / 16;
		
		int xfinal = xstart + (Game.WIDHT / 16);
		int yfinal = ystart + (Game.HEIGHT / 16);
		
		for(int xx = xstart ; xx <= xfinal ; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
	public static void renderMiniMap() {
		
		for (int i = 0; i < Game.minimapPixels.length; i++) {
			Game.minimapPixels[i] = 0;
		}
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				if(tiles[xx + (yy*WIDTH)] instanceof WallTile) {
					Game.minimapPixels[xx + (yy*WIDTH)] = 0xffffff;
				}
			}
		}		
		for(int i = 0; i < Game.enemies.size(); i++){
		       Enemy en = Game.enemies.get(i);
		       int enX = en.getX()/16;
		       int enY = en.getY()/16;
		      
		       Game.minimapPixels[enX + (enY * WIDTH)] = 0xFF0000;
		}
		
		int xPlayer = Game.player.getX() / 16;
		int yPlayer = Game.player.getY() / 16;
		Game.minimapPixels[xPlayer + (yPlayer*WIDTH)] = 0x00FF90;
		
		
	}
}
