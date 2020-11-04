package metroydera.com.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import metroydera.com.entitys.Entity;
import metroydera.com.entitys.Player;

public class Inventory{
	
	public int selected = 0;
	public boolean isPressed = false;
	public int mx , my;
	public int inventoryBoxSize = 40;
	public String[] items = {"lifePack", "Weapon", "Bullets" , "Axe" , "" , ""};
	public int initialPosition = ((Game.WIDHT * Game.SCALE) / 2 - ((items.length * inventoryBoxSize) / 2));
	
	public void tick() {
		
		if(isPressed) {
			isPressed = false;
			if(mx >= 0 && mx <= (inventoryBoxSize *  items.length)) {
				if(my >= Game.HEIGHT - inventoryBoxSize * Game.SCALE + 1 && my < Game.HEIGHT - inventoryBoxSize * Game.SCALE + 1 + inventoryBoxSize) {
					selected = mx / inventoryBoxSize;
					System.out.println("selecionou");
					System.out.println(selected);
				}
				
			}
		}
		
	}

	public void render(Graphics g) {
		
		for (int i = 0; i < items.length; i++) {
			g.setColor(Color.black);
			g.fillRect((i * inventoryBoxSize) + 1, Game.HEIGHT - inventoryBoxSize * Game.SCALE + 1, inventoryBoxSize, inventoryBoxSize);
			g.setColor(Color.gray);
			g.drawRect((i * inventoryBoxSize) + 1, Game.HEIGHT - inventoryBoxSize * Game.SCALE + 1, inventoryBoxSize, inventoryBoxSize);
			
			if(items[i] == "lifePack") {
				g.drawImage(Entity.LIFEPACK_EN, (i * inventoryBoxSize) + 5, Game.HEIGHT - inventoryBoxSize * Game.SCALE + 5, 32, 32, null);
				g.setColor(Color.yellow);
				g.setFont(new Font("arial" , Font.BOLD, 15));
				g.drawString(""+Game.lifepacks ,  30, 55);
			}else if(items[i] == "Weapon") {
				g.drawImage(Entity.WEAPON_EN, (i * inventoryBoxSize) + 5, Game.HEIGHT - inventoryBoxSize * Game.SCALE + 5, 32, 32, null);
				g.setColor(Color.yellow);
				g.setFont(new Font("arial" , Font.BOLD, 15));
				g.drawString(""+Game.lifepacks ,  inventoryBoxSize * 2 - 10, 55);
			}else if(items[i] == "Bullets") {
				g.drawImage(Entity.BULLET_EN, (i * inventoryBoxSize) + 5, Game.HEIGHT - inventoryBoxSize * Game.SCALE + 5, 32, 32, null);
				g.setColor(Color.yellow);
				g.setFont(new Font("arial" , Font.BOLD, 15));
				g.drawString(""+Player.ammo ,  inventoryBoxSize * 3 - 20, 55);
			}else if(items[i] == "Axe") {
				g.drawImage(Entity.BULLET_EN, (i * inventoryBoxSize) + 5, Game.HEIGHT - inventoryBoxSize * Game.SCALE + 5, 32, 32, null);
			}
			
			if(selected == i) {
				g.setColor(Color.green);
				g.drawRect((i * inventoryBoxSize) , Game.HEIGHT - inventoryBoxSize * Game.SCALE + 1, inventoryBoxSize, inventoryBoxSize);
			}
		
		}
	}
}
