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
	   private Socket    		socket    = null;
	   private Cliente   		cliente   = null;
	   private DataInputStream      entrada = null;
	   
	   

	   public HiloCliente(Cliente _cliente, Socket _socket)
	   {  cliente   = _cliente;
	      socket   = _socket;
	      open();  
	      start();
	   }
	   
	  
	   public void open()
	   {  try
	      {  entrada  = new DataInputStream(socket.getInputStream());
	      }
	      catch(IOException ioe)
	      {  System.out.println("--- Ups! Encontramos un problema: " + ioe);
	         cliente.stop();
	      }
	   }
	   public void close()
	   {  try
	      {  if (entrada != null) entrada.close();
	      }
	      catch(IOException ioe)
	      {  System.out.println("--- Ups! Encontramos un problema: " + ioe);
	      }
	   }
	   public void run()
	   {  while (true)
	      {  try
	         {  cliente.handle(entrada.readUTF());
	         }
	         catch(IOException ioe)
	         {  System.out.println("--- Diantres! algo sucedio durante la escucha: " + ioe.getMessage());
	            cliente.stop();
	         }
	      }
	   }

	
}
