import java.net.*;
import java.io.*;

public class SimpleServer {
  public static final int DEFAULT_PORT = 3100;

  public static void main(String[] args) throws IOException {

    ServerSocket socket = new ServerSocket(DEFAULT_PORT);

    while (true) {
      new Thread(
        new Worker(socket.accept())
      ).start();
    }
  }

}

class Worker implements Runnable {
  private Socket socket;

  public Worker(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      outputRequest(socket);
      Thread.sleep((int)(Math.random()* 10000));
      sendResponse(socket);
      socket.close();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  protected void outputRequest(Socket client) throws IOException {
    String line;

    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

    while (true) {
      line = reader.readLine();
      System.out.println("> " + line);

      // Why do we need to do this?
      if (line.contains("END")) {
        break;
      }
    }
    outputLineBreak();
  }

  protected void outputLineBreak() {
    System.out.println("-------------------------");
  }

  protected void sendResponse(Socket client) throws IOException {
    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
    int gift = (int) Math.ceil(Math.random() * 100);
    String response = "Gee, thanks, this is for you: " + gift;

    out.println(response);

    outputLineBreak();
    System.out.println("I sent: " + response);
  }
}
