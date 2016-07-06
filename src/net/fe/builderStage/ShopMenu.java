package net.fe.builderStage;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.IntSupplier;

import net.fe.FEResources;
import net.fe.Session;
import net.fe.modifier.Modifier;
import net.fe.overworldStage.ItemMenu;
import net.fe.unit.HealingItem;
import net.fe.unit.Item;
import net.fe.unit.ItemDisplay;
import net.fe.unit.RiseTome;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;
import net.fe.unit.WeaponFactory;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class ShopMenu.
 */
public class ShopMenu extends Entity {
	
	/** The shops. */
	private ItemMenu[] shops;
	
	/** The shop icons. */
	private Texture[] shopIcons;
	
	/** The shop inventory */
	private ShopInventory shopInventory;
	
	/** The selected. */
	private int selected;
	
	/** The camera x. */
	private int cameraX;
	
	/** The instant. */
	private boolean instant;
	
	/** The Constant HEIGHT. */
	public static final int HEIGHT = 200;
	
	/** The Constant WIDTH. */
	public static final int WIDTH = 135;
	
	/**
	 * Instantiates a new shop menu.
	 *
	 * @param x the x
	 * @param y the y
	 * @param ms the modifiers 
	 * @param si the shop inventory limitations. 
	 * @param currentFunds the team's current funds. 
	 */
	public ShopMenu(float x, float y, Set<Modifier> ms, ShopInventory si, IntSupplier currentFunds) {
		super(x, y);
		System.out.println("new shop");
		shops = new ItemMenu[9];
		shopIcons = new Texture[9];
		shopInventory = si;
		for(int i = 0; i < shops.length; i++){
			shops[i] = new ItemMenu(x + 140*i,y){{
				drawCost = true;
				drawAsDisabled = itemIsAvaliable(si, currentFunds).negate();
				setWidth(135);
			}};
			shopIcons[i] = FEResources.getTexture("shop" + i);
		}
		
		Iterable<Item> items = Item.getAllItems();
		for (Modifier m : ms) { items = m.modifyShop(items); }
		
		for (Item i : items) {
			ItemDisplay display = new ItemDisplay(0, 0, i.getCopy(), false);
			
			if (i instanceof Weapon) {
				Weapon w = (Weapon) i;
				if(w.pref != null || w.name.startsWith("Debug") || w.getCost() == 1) continue;
				switch(w.type){
					case SWORD: shops[0].addItem(display); break;
					case LANCE: shops[1].addItem(display); break;
					case AXE: shops[2].addItem(display); break;
					case BOW: shops[3].addItem(display); break;
					case CROSSBOW: shops[3].addItem(display); break;
					case LIGHT: shops[4].addItem(display); break;
					case ANIMA: shops[5].addItem(display); break;
					case DARK: shops[6].addItem(display); break;
					case STAFF: shops[7].addItem(display); break;
				}
			} else if (i instanceof RiseTome) {
				shops[6].addItem(display);
			} else { // including HealingItems
				shops[8].addItem(display);
			}
		}
		
		for(ItemMenu shop: shops){
			shop.sortItems();
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep(){
		int shouldX = selected*140;
		float dx = Math.signum(shouldX - cameraX) * Game.getDeltaSeconds() * 600;
		cameraX+= dx;
		if(dx * (shouldX - cameraX) < 0 || instant){
			cameraX = shouldX;
			instant = false;
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		Renderer.addClip(x, y-16, getSelectedShop().getWidth(), HEIGHT+16, true);
		for(int i = 0; i < shops.length; i++){
			shops[i].x = x + 140*i - cameraX;
			if(shops[i].x >= x - 140 && shops[i].x <= x + 140)
				shops[i].render();
			
			Color c = new Color(0.5f,0.5f,0.5f);
			if(i == selected) c = new Color(Color.white);
			float iconX = x + i*15;
			Renderer.setColor(c);
			Renderer.render(shopIcons[i], 0, 0, 1, 1, iconX, y-15, iconX + 15, y, renderDepth);
			Renderer.setColor(Color.white);
		}
		Renderer.removeClip();
		
		{
			float textY = y + HEIGHT;
			final Item selectedItem = this.getSelectedShop().getSelection().getItem();
			{
				final String itemName = selectedItem.name;
				final int limit = shopInventory.getWeaponCount(itemName);
				if (limit != -1) {
					textY += 16;
					final String text = itemName + ": " + limit;
					final float textX = this.getSelectedShop().x + this.getSelectedShop().getWidth() - FEResources.getBitmapFont("default_med").getStringWidth(text);
					Renderer.drawString("default_med", text, textX, textY, 0);
				}
			}
			{
				final String categoryName = selectedItem.getItemClass();
				final int limit = shopInventory.getCategoryCount(categoryName);
				if (categoryName != "" && limit != -1) {
					textY += 16;
					final String text = categoryName + ": " + limit;
					final float textX = this.getSelectedShop().x + this.getSelectedShop().getWidth() - FEResources.getBitmapFont("default_med").getStringWidth(text);
					Renderer.drawString("default_med", text, textX, textY, 0);
				}
			}
		}
	}
	
	/**
	 * Gets the selected shop.
	 *
	 * @return the selected shop
	 */
	private ItemMenu getSelectedShop(){
		return shops[selected];
	}
	
	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public Item getItem(){
		return getSelectedShop().getSelection().getItem().getCopy();
	}
	
	/**
	 * Reduce the number available by 1.  False if there isn't enough stock left.
	 * 
	 * @return
	 */
	public boolean buyItem(){
		return shopInventory.buyItem(getSelectedShop().getSelection().getItem());
	}
	
	/**
	 * Returns true if the currently selected item can be gotten (if the inventory is present)
	 * 
	 * @return
	 */
	public boolean canGetItem(){
		return shopInventory.itemInStock(getSelectedShop().getSelection().getItem());
	}
	
	/**
	 * Returns the item i to the shop's inventory.
	 * 
	 * @param i
	 */
	public void returnItem(Item i){
		shopInventory.returnItem(i);
	}
	
	
	/**
	 * Returns all items possessed by this unit to the shop's inventory.
	 * 
	 * @param u
	 */
	public void massReturnItem(Unit u){
		shopInventory.refundItems(u);
	}
	
	/**
	 * Left.
	 */
	public void left(){
		selected--;
		if(selected< 0){
			selected+= shops.length;
			instant = true;
		}
		getSelectedShop().setSelection(0);
	}
	
	/**
	 * Right.
	 */
	public void right(){
		selected++;
		if(selected >= shops.length){
			selected -= shops.length;
			instant = true;
		}
		getSelectedShop().setSelection(0);
	}
	
	/**
	 * Up.
	 */
	public void up(){
		getSelectedShop().up();
	}
	
	/**
	 * Down.
	 */
	public void down(){
		getSelectedShop().down();
	}
	
	/**
	 * Clear selection.
	 */
	public void clearSelection(){
		for(ItemMenu shop: shops){
			shop.clearSelection();
		}
	}
	
	/**
	 * Restore selection.
	 */
	public void restoreSelection(){
		for(ItemMenu shop: shops){
			shop.restoreSelection();
		}
	}
	
	/**
	 * Gets the shops.
	 *
	 * @return the shops
	 */
	public ItemMenu[] getShops() {
		return shops;
	}
	
	/**
	 * Returns a Predicate that returns true if the item is able to be purchased
	 */
	private static Predicate<Item> itemIsAvaliable(ShopInventory si, IntSupplier currentFunds) {
		return ((Item i) -> si.itemInStock(i) && i.getCost() <= currentFunds.getAsInt());
	}

}
