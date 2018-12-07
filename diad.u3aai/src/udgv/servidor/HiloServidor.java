/**
 * 
 */
package udgv.servidor;

import java.net.*;



import java.io.*;

/**
 * @author MARIANO
 *
 */
@SuppressWarnings("deprecation")
public class HiloServidor extends Thread {
	   private ServidorChat       servidor    = null;
	   private Socket           socket    = null;
	   private int              ID        = -1;
	   private DataInputStream  entrada  =  null;
	   private DataOutputStream salida = null;

	   public HiloServidor(ServidorChat _server, Socket _socket)
	   {  super();
	      servidor = _server;
	      socket = _socket;
	      ID     = socket.getPort();
	   }
	   public void send(String msg)
	   {   try
	       {  salida.writeUTF(msg);
	          salida.flush();
	       }
	       catch(IOException ioe)
	       {  System.out.println(ID + "--- Lo sentimos pero algo salio mal no pudimos enviar el mensaje :( ... ---" + ioe.getMessage());
	          servidor.remove(ID);
	          stop();
	       }
	   }
	   public int getID()
	   {  return ID;
	   }
	   public void run()
	   {  System.out.println("--- Hemos arrancado el hilo es el numero " + ID + " ---");
	      while (true)
	      {  try
	         {  servidor.handle(ID, entrada.readUTF());
	         }
	         catch(IOException ioe)
	         {  System.out.println(ID + "--- Lo sentimos pero algo salio mal :( ---"  + ioe.getMessage());
	            servidor.remove(ID);
	            stop();
	         }
	      }
	   }
	   public void open() throws IOException
	   {  entrada = new DataInputStream(new 
	                        BufferedInputStream(socket.getInputStream()));
	      salida = new DataOutputStream(new
	                        BufferedOutputStream(socket.getOutputStream()));
	   }
	   public void close() throws IOException
	   {  if (socket != null)    socket.close();
	      if (entrada != null)  entrada.close();
	      if (salida != null) salida.close();
	   }
}
