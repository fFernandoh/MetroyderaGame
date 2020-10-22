package metroydera.com.entitys;


import java.awt.Graphics;
import java.util.ArrayList;

public class Items{
	
	private String name = "";
	private int damage = 0;
	private int defense = 0;
	public static ArrayList items = new ArrayList();

	public Items() {
		

	}
	
	public void render(Graphics g) {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

}
