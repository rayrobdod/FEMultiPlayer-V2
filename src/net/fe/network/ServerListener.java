package net.fe.network;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;
import java.time.LocalDateTime;

import net.fe.lobbystage.LobbyStage;
import net.fe.network.message.ClientInit;
import net.fe.network.message.CommandMessage;
import net.fe.network.message.JoinTeam;
import net.fe.network.message.PartyMessage;
import net.fe.network.message.KickMessage;
import net.fe.network.message.QuitMessage;
import net.fe.network.message.ReadyMessage;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving server events.
 * The class that is interested in processing a server
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addServerListener<code> method. When
 * the server event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ServerEvent
 */
public final class ServerListener extends Thread {
	
	/** The socket. */
	private final Socket socket;
	
	/** The out. */
	private ObjectOutputStream out;
	
	/** The in. */
	private ObjectInputStream in;
	
	/** The main. */
	private final Server main;
	
	/** The client quit. */
	private volatile boolean clientQuit;
	
	/** The client that this is linked to. */
	private final byte clientId;
	
	/**
	 * Instantiates a new server listener.
	 *
	 * @param main the main
	 * @param socket the socket
	 */
	public ServerListener(Server main, Socket socket, byte clientId) {
		super("Listener "+ clientId);
		this.clientId = clientId;
		this.socket = socket;
		this.main = main;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			Server.logger.fine("LISTENER: I/O streams initialized");
			sendMessage(new ClientInit((byte) 0, clientId, main.getSession()));
		} catch (IOException e) {
			Server.logger.throwing("ServerListener", "<init>", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			Server.logger.fine("LISTENER: Start");
			Message message;
			clientQuit = false;
			while(!clientQuit) {
				message = (Message) in.readObject();
				Server.logger.fine("[RECV]" + message);
				processInput(message);
			}
			Server.logger.fine("LISTENER: Exit");
			main.clients.remove(this);
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			Server.logger.throwing("ServerListener", "run", e);
		} finally {
			main.clients.remove(this);
		}
	}
	
	/**
	 * Process input.
	 *
	 * @param message the message
	 */
	public void processInput(Message message) {
		synchronized(main.messagesLock) {
			if (message.origin == clientId) {
				if (message instanceof QuitMessage) {
					clientQuit = true;
				}
				main.messages.add(message);
				main.messagesLock.notifyAll();
			}
		}
	}
	
	/**
	 * Send message.
	 *
	 * @param message the message
	 */
	public void sendMessage(Message message) {
		try {
			out.writeObject(message);
			out.flush();
			Server.logger.fine("SERVER sent message: [" + message.toString() + "]");
			if (message instanceof KickMessage && ((KickMessage) message).player == clientId) {
				clientQuit = true;
			}
		} catch (IOException e) {
			Server.logger.severe("SERVER Unable to send message!");
			Server.logger.throwing("ServerListener", "sendMessage", e);
		}
	}

}
