package net.fe.unit;


import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.Color;

import net.fe.FEResources;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemDisplay.
 */
public final class ItemDisplay extends Entity{
	
	/** The item to display */
	private final Item item;
	
	/** whether to display item as equipped */
	private final boolean equip;
	
	/** The weapon icons */
	private static final Texture weaponIcon = FEResources.getTexture("gui_weaponIcon");
	
	/** The "equipped" texture */
	private static final Texture e = FEResources.getTexture("e");
	
	/**
	 * Instantiates a new item display.
	 *
	 * @param f the x-coordinate
	 * @param g the y-coordinate
	 * @param i the item
	 * @param equip whether to display item as equipped
	 */
	public ItemDisplay(float f, float g, Item i, boolean equip){
		super(f,g);
		renderDepth = 0.05f;
		item = i;
		this.equip = equip;
	}
	
	/**
	 * Render.
	 */
	public void render(){
		render(null, false, 0, false);
	}
	
	/**
	 * Render.
	 *
	 * @param disabled whether to display as disabled
	 */
	public void render(boolean disabled){
		render(null, false, 0, disabled);
	}
	
	/**
	 * Render.
	 *
	 * @param t a transform to apply
	 * @param effective whether to display as effective
	 * @param timer a timer value
	 */
	public void render(Transform t, boolean effective, float timer) {
		render(t, effective, timer, false);
	}
	
	/**
	 * Render.
	 *
	 * @param t a transform to apply
	 * @param effective whether to display as effective
	 * @param disabled whether to display as disabled
	 * @param timer a timer value
	 */
	public void render(Transform t, boolean effective, float timer, boolean disabled) {
		if(item == null) return;
		final int row = item.id/8;
		final int col = item.id%8;
		ShaderArgs args;
		if(effective) {
			float exp = (float) (Math.sin(timer)/2 + .5f);
			args = new ShaderArgs("lighten", exp);
		} else if(disabled) {
			args = new ShaderArgs("greyscale");
		} else {
			args = new ShaderArgs();
		}
		t = (t == null ? new Transform() : t);
		final Color initialTransformColor = t.color;
		if(disabled) {
			t.setColor(new Color(160,160,160));
		}
		Renderer.render(weaponIcon, 
				col/8.0f, row/10.0f, (col+1)/8.0f, (row+1)/10.0f,
				x-1, y, x+16, y+17, renderDepth, t, args, chu.engine.anim.BlendModeArgs.ALPHA_BLEND);
		FEResources.getBitmapFont("default_med").render(item.name, x+16, y+3, renderDepth, t);
		t.setColor(initialTransformColor);
		if(equip){
			Renderer.render(e, 
					0, 0, 1, 1,
					x+10, y+10, x+16, y+17, renderDepth, t);
		}
	}
	
	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public Item getItem(){
		return item;
	}
}
