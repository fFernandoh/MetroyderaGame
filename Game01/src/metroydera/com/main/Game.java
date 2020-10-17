package metroydera.com.main;

import metroydera.com.entitys.*;
import metroydera.com.graphics.Spritesheet;
import metroydera.com.graphics.UI;
import metroydera.com.worlds.Camera;
import metroydera.com.worlds.World;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;


public class Game extends Canvas implements Runnable , KeyListener , MouseListener{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDHT = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	private BufferedImage image;
	public static BufferedImage minimap;
	private int CUR_LEVEL = 1;
	private int MAX_LEVEL = 2;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;
	public static Player player;
	public static World world;
	public static Random rand;
	public UI ui;
	public Menu menu;
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0; 
	private boolean restartGame = false;
	public boolean saveGame = false;
	public static int[] minimapPixels;
	
	public Game() {
		
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDHT*SCALE, HEIGHT*SCALE));
		initFrame();
		//inicializando objetos
		
		ui = new UI();
		image = new BufferedImage(WIDHT, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();

		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
		minimap  = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		minimapPixels = ((DataBufferInt)minimap.getRaster().getDataBuffer()).getData();
		
		
		
		menu = new Menu();
		//menu.tick();
		
		
	}
	
	public void initFrame() {
		frame = new JFrame("Meu Jogo");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new  Thread(this);
		isRunning = true;
		thread.start(); 
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String [] arg) {
		Game game = new Game();
		game.start();
		
	}
	
	public void tick() {
		
		if(gameState == "NORMAL") {
			if(this.saveGame) {
				this.saveGame = false;
				//ADICIONAR MAIS COISAS PARA SALVAR NA VARIAVEL opt1 e opt2 
				// depois ir em Menu.applySave() , adicionar no switch case;
				
				String[] opt1 = {"level" , "life"};
				int[] opt2 = {this.CUR_LEVEL , (int)player.life};
				Menu.saveGame(opt1 , opt2 , 10);
				System.out.println("Jogo salvo!");
			}
		this.restartGame = false;
		for(int i = 0 ; i < entities.size(); i++) {
			Entity e = entities.get(i);			
			e.tick();
		}
		
		for(int i = 0 ; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}
		
		//CARREGAR NOVO MAPA AO MATAR TODOS OS MOB
		
		if(enemies.size() == 0) {
			//proximo level
			CUR_LEVEL++;
			if(CUR_LEVEL > MAX_LEVEL) {
				CUR_LEVEL = 1;
			}
			
			String newWorld = "Level"+CUR_LEVEL+".png";
			//System.out.println(newWorld);
			World.restartGame(newWorld);
		}
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver > 25) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver) 
					this.showMessageGameOver = false;
				else 
					this.showMessageGameOver = true;
			}
			
			if(restartGame) {
				this.restartGame = false;
				this.gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "Level"+CUR_LEVEL+".png";
				//System.out.println(newWorld);
				World.restartGame(newWorld);
		
			}
		}else if(gameState == "MENU") {
			menu.tick();
		}

	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDHT, HEIGHT);
		
		//Graphics2D g2 = (Graphics2D) g;
		//renderizar
		world.render(g);
		for(int i = 0 ; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		for(int i = 0 ; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		
		ui.render(g);
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDHT*SCALE, HEIGHT*SCALE, null);
		g.setFont(new Font("arial" , Font.BOLD, 16));
		g.setColor(Color.white);
		g.drawString("Ammo " + Player.ammo, 600, 30);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0, 100));
			g2.fillRect(0, 0, WIDHT*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial" , Font.BOLD, 30));
			g.setColor(Color.white);
			g.drawString("GAME OVER", (WIDHT*SCALE) / 2 - 50 , (HEIGHT*SCALE /2) - 20);	
			if(showMessageGameOver) 
				g.drawString(">Pressione enter para reiniciar<", 150 , 250);
		}else if(gameState == "MENU") {
				menu.render(g);
			}
		World.renderMiniMap();
		g.drawImage(minimap , 615,375,World.HEIGHT * 5 , World.HEIGHT * 5, null);
		bs.show();
		
	}

	@Override
	public void run() {
		long lasttime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lasttime) / ns; 
			lasttime = now;
			
			if(delta >= 1 ) {
				 tick();
				 render();
				 frames++;
				 delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				//System.out.println("FPS : " + frames);
				frames = 0;
				timer+= 1000;
			}
		}
		
		stop();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
			// move para a direita
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) { 
			player.left = true;
			//move pra esquerda
		}
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		
			if(gameState == "MENU") {
				menu.up = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) { 
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.shoot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(gameState == "NORMAL") 
			this.saveGame = true; 
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) { 
			player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) { 
			player.down = false;
		}			
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX() / 3);
		player.my = (e.getY() / 3);		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	

}

