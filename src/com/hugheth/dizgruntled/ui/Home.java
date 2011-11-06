package com.hugheth.dizgruntled.ui;

import java.io.FileWriter;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.ShadowEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.hugheth.dizgruntled.GruntzGame;
import com.hugheth.dizgruntled.ResourceLoader;
import com.hugheth.dizgruntled.manager.UIManagerState;
import com.hugheth.dizgruntled.tools.URLReader;

public class Home extends BasicGameState {

	private long _start = 0;
	private UnicodeFont _smallFont;
	private UnicodeFont _largeFont;
	private Sound _warp;
	
	private GruntzGame _game;
	
	private Image _title;
	private Image _logo;
	
	private Animation _purple;
	private Animation _blue;
	private Animation _green;
	private Image _bronze;
	private Image _silver;
	private Image _gold;
	private Animation _sparkle;
	
	private JSONObject _score;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		
		_game = (GruntzGame) arg1;
		
		_start = arg0.getTime();
		
		_warp = new Sound("soundz/warpstone.ogg");
		
		_title = new Image("graphicz/ui/header.jpg");
		_logo = new Image("graphicz/ui/logo.jpg");
		
		_purple = ResourceLoader.getAnimationForPath("diamondz/Purple");
		_blue = ResourceLoader.getAnimationForPath("diamondz/Blue");
		_green = ResourceLoader.getAnimationForPath("diamondz/Green");
		_sparkle = ResourceLoader.getAnimationForPath("sparkle");
		_sparkle.setLooping(true);
		
		_bronze = new Image("graphicz/diamondz/Bronze.png");
		_silver = new Image("graphicz/diamondz/Silver.png");
		_gold = new Image("graphicz/diamondz/Gold.png");
		
		// Load the fonts
		_smallFont = new UnicodeFont("lib/sundaycomicsbb_bold.ttf", 16, true, false);
		_smallFont.getEffects().add(new ColorEffect(java.awt.Color.black)); 
		
		ShadowEffect outline = new ShadowEffect(java.awt.Color.white, -1, -2, 1);
		
		_smallFont.getEffects().add(outline);
		_smallFont.addAsciiGlyphs(); 
		_smallFont.loadGlyphs();
		
		_largeFont = new UnicodeFont("lib/sundaycomicsbb_bold.ttf", 32, true, false);
		_largeFont.getEffects().add(new ColorEffect(java.awt.Color.black)); 
		
		_largeFont.getEffects().add(outline);
		_largeFont.addAsciiGlyphs(); 
		_largeFont.loadGlyphs();
		
		// Load the highscore
		try {
			
			_score = ResourceLoader.getJSONForPath("lib/highscore");
			
		} catch (Exception e) {
			System.out.println("Couldn't load highscore");
		}
		
		_game.music.play(1, 0.5f);
		
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		
		long now = arg0.getTime() - _start;
				
		if (now > 1000) {
			
			String text1 = null;
			String text2 = null; 
			
			if (now < 2500) {
				text1 = "Hugheth";
				text2 = "Presents";
				
				_logo.draw(315, 100);
			}
			
			if (now > 3750 && now < 5050) {
				text1 = "An Open Source";
				text2 = "Copyleft Experience";
			}
			
			if (now > 6350 && now < 7650) {
				text1 = "inspiration & media from";
				text2 = "Monotlith (Gruntz 1999)";
			}
			
			if (now > 8950 && now < 10000 && !_warp.playing())
				_warp.play(1, 10);
				
			if (now > 8950 && now < 10250) {
				text1 = "dizGruntled";
				text2 = "ALPHA 2.0";
			}
			
			if (now > 10250) {
				_title.draw(180, 50);
				
				try {
					
					String score = Math.floor(Float.parseFloat(_score.getString("score"))) + "%";
					
					_largeFont.drawString(400 - _largeFont.getWidth("Highscore") / 2, 320, "Highscore");
					_largeFont.drawString(400 - _largeFont.getWidth(score) / 2, 360, score);
					
					_smallFont.drawString(240, 430, _score.get("green") + " / 78");
					_smallFont.drawString(390, 430, _score.get("blue") + " / 34");
					_smallFont.drawString(540, 430, _score.get("purple") + " / 5");
					
					_green.draw(190, 420);
					_blue.draw(340, 420);
					_purple.draw(490, 420);
					
					if (_score.getBoolean("bronze")) {
						_sparkle.draw(205, 255);
						_bronze.draw(200, 250);
					}
					
					if (_score.getBoolean("silver")) {
						_sparkle.draw(575, 255);
						_silver.draw(570, 250);
					}
					
					if (_score.getBoolean("gold")) {
						_sparkle.draw(395, 255);
						_gold.draw(390, 250);
					}
						
				} catch (Exception e) {
					
					e.printStackTrace();
					
				}
				
				if (now % 1000 < 500)
					_smallFont.drawString(280, 500, "Press any key to start");
			}
			
			// Draw string
			if (text1 != null)
				_largeFont.drawString(400 - _largeFont.getWidth(text1) / 2, 300, text1);
			
			if (text2 != null)
				_largeFont.drawString(400 - _largeFont.getWidth(text2) / 2, 360, text2);
		}
	}
	
	public void updateScore(long time, UIManagerState state) {
		
		state.score = state.purple * 20 + state.blue * 5 + state.green;
		
		String text = "{time: " + time + ", score:" + Math.floor((float) state.score / 348.0f * 100) + ", purple: " + state.purple + ", blue: " + state.blue + ", green: " + state.green + ", bronze: " + state.bronze + ", silver: " + state.silver + ", gold: " + state.gold + "}";
		
		// Save score
		try {
		
			// Upload to interwebs
			if (GruntzGame.name != null && GruntzGame.legit && !GruntzGame.name.equals("")) {
				
				String hash = hash(text + "!?!AIShdcacH2q4q", "MD5");				
				
				URLReader.saveResult(URLEncoder.encode(text, "UTF-8"), URLEncoder.encode(GruntzGame.name, "UTF-8"), hash);
			}
			
			if (((float) state.score / 348.0f * 100) < Float.parseFloat(_score.getString("score")))
				return;
					
			FileWriter write = new FileWriter("lib/highscore.json");
			write.write(text);
			write.close();
						
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Couldn't write the score :(");
		}
	}
	
	private static String hash(String text, String algorithm) throws NoSuchAlgorithmException {
		
		byte[] hash = MessageDigest.getInstance(algorithm).digest(text.getBytes());
		
		BigInteger bi = new BigInteger(1, hash);
		String result = bi.toString(16);
		
		if (result.length() % 2 != 0)
		    return "0" + result;

		return result;
	}
	
	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int delta) throws SlickException {
		
					
	}
	
	@Override
	public void keyPressed(int key, char c) {

		long now = _game.getContainer().getTime() - _start;
		
		if (now > 10250) _game.startLevel();
	}

	@Override
	public int getID() {
		return 0;
	}

}
