package practiceClientServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Server
{
	public static void main(String argv[]) throws Exception
	{
		String clientPrefix;
		String clientPrefixLower;
		ArrayList<String> words = new ArrayList<String>();
		String fileName = "words.txt"; 

		ServerSocket welcomeSocket = new ServerSocket(6789);

		Scanner scanner = new Scanner (new File(fileName));
		//load array with words in list
		while (scanner.hasNextLine()) 
		{
			String line = scanner.nextLine();
			words.add(line);
		}
/* check array is loaded right Works
		for(String word : words)
		{
			System.out.println(word);

		}
		*/

		while (true) {

			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			System.out.println("Accepted TCP connection from" 
					+ connectionSocket.getInetAddress() 
					+ ":" + connectionSocket.getPort());
			try {
				while (true) {
					clientPrefix = inFromClient.readLine();
					clientPrefixLower = clientPrefix.toLowerCase(); 
					StringBuilder wordsOut = new StringBuilder();
					boolean firstWord = true;
					for(String word : words)
					{
						if(word.toLowerCase().startsWith(clientPrefixLower))
						{
							if(!firstWord)
							{
								wordsOut.append(", ");
							}
							else
							{
								firstWord = false;
							}

							wordsOut.append(word);
						}

					}//inefficient goes through all words either way but good enough...

					
					outToClient.writeBytes(wordsOut.toString() + "\n");
					
				}
			} catch (Exception e) {
				// TODO: handle exception, if client closed connection, print:
				System.out.println("Client closed connection.");
			}
		}
	}
}
