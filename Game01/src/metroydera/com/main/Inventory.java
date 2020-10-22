package metroydera.com.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Inventory {
	
	private int invWidth = 65;
	private int invHeight = 90;
	private int invX = (Game.WIDHT * Game.SCALE) /2;
	private int invY = (Game.HEIGHT * Game.SCALE) / 2;

	
	
	public Inventory() {
		
	}
	
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,120));
		g.fillRect(470, 100, invWidth*Game.SCALE,invHeight*Game.SCALE);
		g.setColor(Color.yellow);
		g.setFont(new Font("arial" , Font.BOLD, 30));
		g.drawString("Inventory", 500, 95);
		for (int i = 1; i < 4; i++) {
			for (int j = 1; j < 5; j++) {
				g.setColor(Color.darkGray);
				g.fillRect(415 + ( 16 * Game.SCALE + 16)*i, 60 + ( 16 * Game.SCALE + 16)*j, 16*Game.SCALE , 16*Game.SCALE);
			}
		}
	}

}
