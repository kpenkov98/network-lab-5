import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {

  public static void main(String argv[]) throws Exception {
    String clientSentence;
    String capitalizedSentence;
    String wordToCompare;
    String clientResponse;

    //read in the text file and add the words to a database
    //ArrayList in this instance
    ArrayList<String> words = new ArrayList<String>();
    String filepath = "words.txt";
    Scanner input = new Scanner(new File(filepath));

    while (input.hasNext()) {
      String word = input.next();
      words.add(word);
    }
    //ArrayList for matched words
    ArrayList<String> matchedWords = new ArrayList<String>();

    ServerSocket welcomeSocket = new ServerSocket(6789);
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
          //clear the matchedWords ArrayList
          matchedWords.clear();
          //read in from client
          clientSentence = inFromClient.readLine();
          //search for the word
          while (words.contains(clientSentence)) {
            matchedWords.add(clientSentence);
          }

          if (!matchedWords.isEmpty()) {
            clientResponse =
              "List of matched words: \n" + String.join("\n", matchedWords);
          } else clientResponse = "No words were found";
          //capitalizedSentence = clientSentence.toUpperCase() + '\n';

          outToClient.writeBytes(clientResponse);
        }
      } catch (Exception e) {
        // TODO: handle exception, if client closed connection, print:
        System.out.println("Client closed connection.");
      }
    }
  }
}
