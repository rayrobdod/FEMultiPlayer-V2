package chu.engine;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.LinkedList;

import net.fe.network.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class Stage.
 */
public abstract class Stage {
	
	/** The entities. */
	protected final LinkedList<Entity> entities;  // can't be a sorted set because it's impossible to make a compare that is consistant with equals
	
	/** The add stack. */
	protected final Stack<Entity> addStack;
	
	/** The remove stack. */
	protected final Stack<Entity> removeStack;
	
	/** The sound track. */
	public final String soundTrack;
	
	/**
	 * Instantiates a new stage.
	 *
	 * @param soundTrack the sound track
	 */
	public Stage(String soundTrack) {
		entities = new LinkedList<Entity>();
		addStack = new Stack<Entity>();
		removeStack = new Stack<Entity>();
		this.soundTrack = soundTrack;
	}
	
	/**
	 * Gets the all entities.
	 *
	 * @return the all entities
	 */
	public final List<Entity> getAllEntities() {
		return entities;
	}
	
	/**
	 * Adds the entity.
	 *
	 * @param e the e
	 */
	public final void addEntity(Entity e) {
		addStack.push(e);
		e.willBeRemoved = false;
	}
	
	
	/**
	 * Removes the entity.
	 *
	 * @param e the e
	 */
	public final void removeEntity(Entity e) {
		if(e != null) {
			e.flagForRemoval();
			if(removeStack.contains(e)){
				return;
			}
			removeStack.push(e);
		}
	}
	
	/**
	 * Update.
	 */
	public void update() {
		for(Entity e : entities) {
			e.onStep();
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
	}

	/**
	 * Render.
	 */
	public void render() {
		SortByRender comparator = new SortByRender();
		PriorityQueue<Entity> renderQueue = new PriorityQueue<Entity>(entities.size()+1, comparator);
		renderQueue.addAll(entities);
		while(!renderQueue.isEmpty()) {
			renderQueue.poll().render();
		}
	}
	
	
	/**
	 * Process add stack.
	 */
	public final void processAddStack() {
		while(!addStack.isEmpty()) {
			Entity e = addStack.pop();
			entities.add(e);
			e.stage = this;
		}
	}
	
	/**
	 * Will be removed.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	public final boolean willBeRemoved(Entity e) {
		return removeStack.contains(e);
	}
	
	/**
	 * Process remove stack.
	 */
	public final void processRemoveStack() {
		while(!removeStack.isEmpty()) {
			Entity e = removeStack.pop();
			entities.remove(e);
			addStack.remove(e);		//Otherwise some weird shit happens and entities get stuck in limbo
		}
	}

	/**
	 * Begin step.
	 */
	public abstract void beginStep(List<Message> messages);

	/**
	 * On step.
	 */
	public abstract void onStep();
	
	/**
	 * End step.
	 */
	public abstract void endStep();
	
}
