package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Utils {
	
	public static boolean sendMessage(Socket connection, String message) {
		
		try {
			ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			output.writeObject(message);
			return true;
			
		}catch (IOException e) {
			System.err.println("[ERRO: sendMessage] -> "+e.getMessage());
		}
		return false;
	}
	
	public static String receiveMessage (Socket connection)	{
		
		String response = null;
		
		try {
			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			response = (String) input.readObject();
			
		} catch (IOException e) {
			System.err.println("[ERRO: receiveMessage] -> "+e.getMessage());
		}catch (ClassNotFoundException e) {
			System.err.println("[ERRO: receiveMessage] -> "+e.getMessage());
		}
		return response;
	}
}
