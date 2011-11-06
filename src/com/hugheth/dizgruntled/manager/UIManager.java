package com.hugheth.dizgruntled.manager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.ShadowEffect;

import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;
import com.hugheth.dizgruntled.player.HumanPlayer;

public class UIManager extends Manager {

	private UIManagerState _state;
	
	private UnicodeFont _smallFont;
	private UnicodeFont _largeFont;
	private Image _diamondBar;
	private Image _restart;
	private Image _checkpoint;
	private Image _music;
	
	private Fade _fade = Fade.None;
	private long _fadeStart = 0;
	
	private int _score = 0;
	
	private String _message;
	private long _messageTime;
	
	private String _consoleTitle;
	private String _console;
	private int _fadeLoad = -1;
	
	public UIManager(Level level) {
		super(level);
		
		_state = new UIManagerState();
		
		// Load the diamond bar
		try {
			_diamondBar = new Image("graphicz/ui/diamondBar.png");
			_restart = new Image("graphicz/ui/refresh.png");
			_checkpoint = new Image("graphicz/ui/checkpoint.png");
			_music = new Image("graphicz/ui/music.png");
			
		} catch (Exception e) {
			System.out.println("IGNORE: Some UI graphics are missing.");
		}
		
		try {
			
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
			
		} catch (SlickException e) {
			System.out.println("ABORT: Couldn't load the fonts successfully.");
		}
	}
	
	public UIManagerState getState() {
		return _state;
	}
	
	public void displayMessage(String text) {
		_messageTime = _level.getTimeManager().getCurrentFrame() + 15000 / TimeManager.FRAME_RATE;
		_message = text;
	}

	@Override
	public String getName() {
		return "ui";
	}
	
	public void addDiamonds(int score) {
		// Change
		_state.score += score;
	}
	
	public int getDiamonds() {
		return _state.score;
	}
		
	public boolean click(int x, int y) {
		
		if (_console != null) return false;
		
		if (y > 3 && y < 32) {
			if (x > 735 && x < 766) {
				_level.playSound("click.ogg", 40);
				_consoleTitle = "Return to Checkpoint";
				_console = "Are you sure you want to return to the last checkpoint? \n Press enter to continue or any other key to cancel.";
				darken();
				
				return false;
			}
			if (x > 765 && x < 796) {
				_level.playSound("click.ogg", 40);
				_consoleTitle = "Quit Game";
				_console = "Are you sure you want to quit the game? \n Press escape to continue or any other key to cancel.";
				darken();
				
				return false;
			}
			
			if (x > 705 && x < 736) {
				_level.playSound("click.ogg", 40);
				_level.toggleMusic();
				return false;
			}
		}
		
		return true;
	}
	
	public boolean key(int key) {
		
		if (_console == null) return true;
		
		if (_consoleTitle.equals("Quit Game")) {
			if (key == 1)
				System.exit(0);
			
			_console = null;
			_fade = Fade.None;
			
			return false;
		}
		
		if (_fade == Fade.FadeOut) System.exit(0);
		
		if (key != 28) {
			
			_console = null;
			_fade = Fade.None;
			
			return false;
		}
		
		_fadeLoad = 1;	
		
		crossFade();
		// TODO: Human player not just first one
		((HumanPlayer)_level.getPlayer(0)).deselectGrunts();
		
		_level.playSound("crossfade.ogg", 40);
		
		_console = null;
		
		return false;
	}
	
	public void renderUI(Graphics g) {
		
		if (_score > _state.score)
			_score--;
			
		else if (_score < _state.score)
			_score++;
		
		if (_message != null) {
			
			g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
			g.setColor(new Color(20, 20, 40));
			g.fillRect(0, 566 - _smallFont.getHeight(_message), 800, _smallFont.getHeight(_message) + 40);
			g.setDrawMode(Graphics.MODE_NORMAL);
			
			_smallFont.drawString(170, 586 - _smallFont.getHeight(_message), _message);
			
			if (_level.getTimeManager().getCurrentFrame() == _messageTime)
				_message = null;
		}
		
		_diamondBar.draw(5, 536);
		_restart.draw(768, 4);
		_checkpoint.draw(736, 4);
		_music.draw(704, 4);
		
		
		_largeFont.drawString(140 - _largeFont.getWidth(String.valueOf(_score)), 550, String.valueOf(_score));
		
		if (_fade != Fade.None)
			renderFade(g);

		_smallFont.drawString(5, 5, "Dizgruntled Alpha 2.0");
			
		if (_console != null) {
		
			_largeFont.drawString(50, 200, _consoleTitle);
			_smallFont.drawString(80, 300, _console);
		}
	}
	
	public void renderFade(Graphics g) {
		
		long time = _level.getTimeManager().getRealTime();
		
		// Check for fade
		if (_fade == Fade.CrossFade) {
			
			g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
			
			// Calculate fade out colour
			int factor = 255;
			
			if (_fadeStart + 600 > time) {
				factor = 255 - (int) ((time - _fadeStart) / 1.6);
		
			} else if (_fadeStart + 1200 > time) {
				factor = (int) ((time - _fadeStart) / 1.6) - 512;
				
				if (_fadeLoad > -1) {
					_level.getSnapshotManager().load(_fadeLoad);
					_fadeLoad = -1;
				}
				
			} else
				_fade = Fade.None;
						
			g.setColor(new Color(factor, factor, factor + 50));
			g.fillRect(0, 0, 800, 600);
			g.setDrawMode(Graphics.MODE_NORMAL);
			
		} else if (_fade == Fade.Flash) {

			if (_fadeStart + 2000 > time) {
				
				int factor = 255 - (int) ((time - _fadeStart) / 4);
				
				g.setDrawMode(Graphics.MODE_SCREEN);
				g.setColor(new Color(factor, factor, factor + 50));
				g.fillRect(0, 0, 800, 600);
				g.setDrawMode(Graphics.MODE_NORMAL);
				
			} else
				_fade = Fade.None;
			
		} else if (_fade == Fade.Darken) {
			
			g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
			g.setColor(new Color(20, 20, 50));
			g.fillRect(0, 0, 800, 600);
			g.setDrawMode(Graphics.MODE_NORMAL);
			
		} else if (_fade == Fade.FadeIn) {
			
			if (_fadeStart + 4000 > time) {
				
				int factor = (int) ((time - _fadeStart) / 8);
			
				g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
				g.setColor(new Color(factor + 25, factor + 25, factor));
				g.fillRect(0, 0, 800, 600);
				g.setDrawMode(Graphics.MODE_NORMAL);
			
			} else {
				
				if (_fade == Fade.FadeOut)
					_level.getGame().finishLevel();
				
				_fade = Fade.None;
			}
		} else if (_fade == Fade.FadeOut) {
			
			int factor = 255 - (int) ((time - _fadeStart) / 12);
			
			g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
			g.setColor(new Color(factor + 25, factor + 25, factor));
			g.fillRect(0, 0, 800, 600);
			g.setDrawMode(Graphics.MODE_NORMAL);
			
			if (_fadeStart + 5000 < time)
				_console = "Thanks to: \n\nMonolith\nSlick2D\nFreesound.org\nEclipse Foundation";
			
			if (_fadeStart + 8000 < time)
				_console = "gruntz2.hugheth.com \nKeep upto date with new developments\n\nComing Soon:\n\n>    Develop your own levelz\n>    Play against friendz and the community\n>    Create your own modz";
			
			if (_fadeStart + 15000 < time)
				_console = "Press any key to exit";
		}
	}
	
	public void crossFade() {
		_fadeStart = _level.getTimeManager().getRealTime();
		_fade = Fade.CrossFade;
	}
	public void flash() {
		_fadeStart = _level.getTimeManager().getRealTime();
		_fade = Fade.Flash;
	}
	public void darken() {
		_fadeStart = _level.getTimeManager().getRealTime();
		_fade = Fade.Darken;
	}
	public void fadeIn() {
		_fadeStart = _level.getTimeManager().getRealTime();
		_fade = Fade.FadeIn;
	}
	public void finish() {
		
		_consoleTitle = "Level Complete";
		_console = "That's all for now kidz!!!";
		
		_level.getGame().getHome().updateScore(_level.getTimeManager().getRealTime(), _state);
		
		_fadeStart = _level.getTimeManager().getRealTime();
		_fade = Fade.FadeOut;
		
		_level.getGame().mute = true;
		_level.getGame().musicLoop.stop();
		_level.playSound("MusicEntry.ogg", 40);
	}

	@Override
	public State stateSave() {
		return _state.Apply();
	}

	@Override
	public void stateRestore(State state) {
		_state = (UIManagerState) state;
	}

}

enum Fade {
	CrossFade, Flash, Darken, FadeIn, None, FadeOut
}