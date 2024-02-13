package practiceClientServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

  public static void main(String argv[]) throws Exception {
    String clientPrefix;
    String clientPrefixLower;
    ArrayList<String> words = new ArrayList<String>();
    String fileName = "words.txt";

    ServerSocket welcomeSocket = new ServerSocket(6789);

    Scanner scanner = new Scanner(new File(fileName));
    //load array with words in list
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      words.add(line);
    }

    while (true) {
      Socket connectionSocket = welcomeSocket.accept();
      BufferedReader inFromClient = new BufferedReader(
        new InputStreamReader(connectionSocket.getInputStream())
      );
      DataOutputStream outToClient = new DataOutputStream(
        connectionSocket.getOutputStream()
      );
      System.out.println(
        "Accepted TCP connection from" +
        connectionSocket.getInetAddress() +
        ":" +
        connectionSocket.getPort()
      );
      try {
        while (true) {
          //reads client message
          clientPrefix = inFromClient.readLine();
          //cleans up the input to lower case
          clientPrefixLower = clientPrefix.toLowerCase();
          StringBuilder wordsOut = new StringBuilder();
          boolean firstWord = true;
          //iterate through the array
          for (String word : words) {
            //check if the word provided by client exists
            //then add variations of the word to the string builder
            if (word.toLowerCase().startsWith(clientPrefixLower)) {
              if (!firstWord) {
                wordsOut.append(", ");
              } else {
                firstWord = false;
              }

              wordsOut.append(word);
            }
          }

          outToClient.writeBytes(wordsOut.toString() + "\n");
        }
      } catch (Exception e) {
        // TODO: handle exception, if client closed connection, print:
        System.out.println("Client closed connection.");
      }
    }
  }
}
