/**
 * 
 */
package udgv.cliente;

/**
 * @author MARIANO
 *
 */

import java.net.*;
import java.io.*;

public class HiloCliente extends Thread {
	   private Socket    		socket   = null;
	   private Cliente   		client   = null;
	   private DataInputStream  streamIn = null;
	   
	   

	   public HiloCliente(Cliente _cliente, Socket _socket)
	   {  client   = _cliente;
	      socket   = _socket;
	      open();  
	      start();
	   }
	   
	  
	   public void open()
	   {  try
	      {  streamIn  = new DataInputStream(socket.getInputStream());
	      }
	      catch(IOException ioe)
	      {  System.out.println("Error getting input stream: " + ioe);
	         client.stop();
	      }
	   }
	   public void close()
	   {  try
	      {  if (streamIn != null) streamIn.close();
	      }
	      catch(IOException ioe)
	      {  System.out.println("Error closing input stream: " + ioe);
	      }
	   }
	   public void run()
	   {  while (true)
	      {  try
	         {  client.handle(streamIn.readUTF());
	         }
	         catch(IOException ioe)
	         {  System.out.println("Listening error: " + ioe.getMessage());
	            client.stop();
	         }
	      }
	   }

	
}
