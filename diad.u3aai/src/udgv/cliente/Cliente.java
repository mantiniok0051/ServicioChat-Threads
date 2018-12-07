/**
 * cliente de chat basado en hilos lo que permite crear más de una instancia dentro de un mismo entorno
 * 
 * java -cp diad.servicio.chat.cliente.jar udgv.cliente.Cliente <IPv4Destino> <PuertoRemoto> <PuertoLocal>
 */
package udgv.cliente;

/**
 * @author MARIANO
 *
 */

//Procedemos a importar las librerías que contienen las clases a emplear
import java.net.*; // Abstracción del servicio de red del Sistema Operativo
import java.io.*;  // Abstracción del subsistema de entrada / salida de Sistema operativo

@SuppressWarnings("deprecation")
public class Cliente implements Runnable {

	   private Socket socket              = null; //Canal mediante el cual se accede a la red
	   private Thread thread              = null; //Hilo de ejecución dedicado para esta instancia
	   private DataInputStream  teclado   = null; //Permite leer la entrada del teclado 
	   private DataOutputStream salida    = null; //Permite encpsular la entrada del teclado en un arreglo de bytes 
	   private HiloCliente cliente	      = null;

	   public Cliente(String IPv4Destino, int puerto)  {

		   System.out.println("--- Por favor espera mientras preparamos todo... :D ---");
	    
		   try {  
			   	 socket = new Socket(IPv4Destino, puerto);
		         System.out.println("--- Cliente inicializado, la dirección local es [" + socket.getLocalAddress() +":"+socket.getLocalPort() +"] ---" + 
			   	 "\n--- Hemos iniciado sessión en el servidor ["+socket.getInetAddress() +":"+socket.getPort()+"] ---");
		         start();
		         System.out.println();
	      }
	      catch(UnknownHostException uhe)
	      {  System.out.println("--- Hum! parece que no hay un servidor disponible ---" + uhe.getMessage()); }
	      catch(IOException ioe)
	      {  System.out.println("Ups! hemos encontrado un problemita: " + ioe.getMessage()); }
	   }
	   
		/* 
		 */
		
		@Override
	   public void run()
	   {  while (thread != null)
	      {  try
	         {  salida.writeUTF(teclado.readLine());
	            salida.flush();
	         }
	         catch(IOException ioe)
	         {  System.out.println("Ups! hemos encontrado un problemita: " + ioe.getMessage());
	            stop();
	         }
	      }
	   }
	   public void handle(String msg)
	   {  if (msg.equals(".Adios!"))
	      {  System.out.println("--- La sesión ha sido finalizada. Presiona 'ENTER' para cerrar la ventana ... ---");
	         stop();
	      }
	      else
	         System.out.println(msg);
	   }
	   public void start() throws IOException
	   {  teclado   = new DataInputStream(System.in);
	      salida = new DataOutputStream(socket.getOutputStream());
	      if (thread == null)
	      {  cliente = new HiloCliente(this, socket);
	         thread = new Thread(this);                   
	         thread.start();
	      }
	   }
	   public void stop()
	   {  if (thread != null)
	      {  thread.stop();  
	         thread = null;
	      }
	      try
	      {  if (teclado   != null)  teclado.close();
	         if (salida != null)  salida.close();
	         if (socket    != null)  socket.close();
	      }
	      catch(IOException ioe)
	      {  System.out.println("--- Lo sentimos pero algo salio mal :( ... ---"); }
	      cliente.close();  
	      cliente.stop();
	   }


	/**
	 * @param args
	 */
	   public static void main(String args[])
	   {  Cliente cliente = null;
	      if (args.length != 2)
	         System.out.println("--- Para comenzar una sesión: \njava -cp diad.servicio.chat.cliente.jar udgv.cliente.Cliente <IPv4Destino> <PuertoRemoto> ");
	      else
	         cliente = new Cliente(args[0], Integer.parseInt(args[1]));
	   }
	

}
