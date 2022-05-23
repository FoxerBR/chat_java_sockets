package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import common.Utils;

public class Server {
	
	public static final String HOST = "127.0.0.1";
	public static final int PORT = 4444;
	
	private ServerSocket server;
	private Map<String, ClientListener> clients;
	
	
	public Server() {
		try {
			String connection_info;
			clients = new HashMap<String, ClientListener>();
			server = new ServerSocket(PORT);
			System.out.println("Servidor iniciado no host: " + HOST + " e porta: " + PORT);
			while(true) {
				Socket connection = server.accept();
				connection_info = Utils.receiveMessage(connection);
				if(checkLogin(connection_info)) {
					ClientListener cl = new ClientListener(connection_info, connection, this);
					clients.put(connection_info, cl);
					Utils.sendMessage(connection, "SUCESS");
					new Thread(cl).start();
				}else {
					Utils.sendMessage(connection, "ERRO");
				}
			}
			
		}catch (IOException e) {
			System.err.println("[ERRO: Server] -> "+e.getMessage());
		}
	}
	
	public Map<String, ClientListener> getClients(){
		return clients;
	}
	
	private boolean checkLogin(String connection_info) {

		String[] splited = connection_info.split(":");
		for (Map.Entry<String, ClientListener> pair : clients.entrySet()) {

			String[] parts = pair.getKey().split(":");
			if (parts[0].toLowerCase().equals(splited[0].toLowerCase())) {
				return false;
			} else if ((parts[1] + parts[2]).equals(splited[1] + splited[2])) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		Server s = new Server();
	}
}
