package metroydera.com.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import metroydera.com.entitys.Player;
import metroydera.com.main.Game;

public class UI {

	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(5, 4, 70 , 7);
		g.setColor(Color.green);
		g.fillRect(5 , 4, (int)((Game.player.life/Game.player.maxLife) * 70) , 7);
		g.setColor(Color.black);
		g.setFont(new Font("arial" , Font.BOLD , 8));
		g.drawString((int)Game.player.life + "/" + (int)Game.player.maxLife, 14 , 10);
		
		
		//XP BAR
		g.setColor(Color.gray);
		g.fillRect(60, 155, 100 , 1);
		g.setColor(Color.green);
		g.fillRect(60 , 155, ((Game.player.xpNextlevel * 100) / Game.player.xpMaxNextLevel) , 1);
		g.setColor(Color.black);
		g.setFont(new Font("arial" , Font.BOLD , 10));
		g.drawString("Level : ("+ Game.player.level + ")     " + Game.player.xpNextlevel + "/" + Game.player.xpMaxNextLevel, 60 , 155);
	}
}
