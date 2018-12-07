/**
 * 
 * java -cp diad.u3aai.servidor.jar udgv.servidor.ServidorChat <PuertoLocal> <Bcklog> <IPv4Local> 
 *
 */
package udgv.servidor;

import java.net.*;  //Abstracción de la capa y protocolos de red disponibles en Sistema Operativo



import java.io.*;   //Abstracción del sistema de entrada y salida



/**
 * @reviewer 
 * @author MARIANO SOTO MS213239444
 *
 */

@SuppressWarnings("deprecation")
public class ServidorChat implements Runnable {
	

	private HiloServidor clients[] = new HiloServidor[50];
	private ServerSocket server = null;
	private Thread       thread = null;
	private int clientCount     = 0;  

//--- Método para inicializar el Servidor---//
	@Override
	public void run() {  
		System.out.println("--- Por favor espera mientras arrancamos el servicio... XD ---");
		while (thread != null)
		{  try
		    {  System.out.println("--- Estamos a la espera denuevos usuarios ...  ---"); 
		       addThread(server.accept()); }
		    catch(IOException ioe)
		    {  System.out.println("--- Ups! hubo un error al conectar con un nuevo usuario: " + ioe); stop(); }
		 }		
	}
	
	//--- Constructor mediante asignación de puerto, backlog y dirección local de escucha --//
	   public ServidorChat(int port, int backlog, InetAddress bindAddr)  {  
		  try
	      {  System.out.println("---  Por favor espera estamos construyendo el servicio en el puerto " + port + "... Que emocion XD !!  ---");
	         server = new ServerSocket(port);  
	         System.out.println("Estamos listos para arrancar , la dirección local es [" + server.getLocalSocketAddress()+":"+server.getLocalPort()+"] ---");
	         start(); }
	      catch(IOException ioe)
	      {  System.out.println("Diantres! algo salio mal... Parece que el puerto " + port + ":" + " esta en uso por alguien mas --- " + "\n" + ioe.getMessage()); }
	   }
		
   //--- Constructor mediante sólo la asignación de puerto --//
		   public ServidorChat(int port)  {  
			  try
		      {  System.out.println("---  Por favor espera estamos construyendo el servicio en el puerto " + port + "... Que emocion XD !!  ---");
		         server = new ServerSocket(port);  
		         System.out.println("Estamos listos para arrancar , la dirección local es [" + server.getLocalSocketAddress()+":"+server.getLocalPort()+"] ---");
		         start(); }
		      catch(IOException ioe)
		      {  System.out.println("Diantres! algo salio mal... Parece que el puerto " + port + ":" + " esta en uso por alguien mas --- " + "\n" + ioe.getMessage()); }
		   }
	   
	//---- comenzar el servicio ----//
	   public void start() {
		   if (thread == null)
		      {  thread = new Thread(this); 
		         thread.start();
		      }
	   }
	   
    //---- detener el servicio ----//
	   public void stop() {
		   if (thread != null)
		      {  thread.stop(); 
		         thread = null;
		      }
	   }
	   
	   
	   private int findClient(int ID)
	   {  for (int i = 0; i < clientCount; i++)
	         if (clients[i].getID() == ID)
	            return i;
	      return -1;
	   }

	   public synchronized void handle(int ID, String input)
	   {  if (input.equals(".Adios!"))
	      {  clients[findClient(ID)].send(".Adios!");
	         remove(ID); }
	      else
	         for (int i = 0; i < clientCount; i++)
	            clients[i].send(ID+ ": " + input);   
	   }
	   
	   public synchronized void remove(int ID)
	   {  int pos = findClient(ID);
	      if (pos >= 0)
	      {  HiloServidor toTerminate = clients[pos];
	         System.out.println("--- Cerrando la sesión de Usuario"+pos+"@"+ID+" ---");
	         if (pos < clientCount-1)
	            for (int i = pos+1; i < clientCount; i++)
	               clients[i-1] = clients[i];
	         clientCount--;
	         try
	         {  toTerminate.close(); }
	         catch(IOException ioe)
	         {  System.out.println("--- Algo salio mal durante la operacion : " + ioe); }
	         toTerminate.stop(); }
	   }
	   
	   private void addThread(Socket socket)
	   {  if (clientCount < clients.length)
	      {  System.out.println("Client accepted: " + socket);
	         clients[clientCount] = new HiloServidor(this, socket);
	         try
	         {  clients[clientCount].open(); 
	            clients[clientCount].start();  
	            clientCount++; }
	         catch(IOException ioe)
	         {  System.out.println("Diantres! Eso no salio como esperabamos :" + ioe); } }
	      else
	         System.out.println("--- Tuvimos que rechazar a un cliente, la sala esta llena " + clients.length + " usuarios conectados ---");
	   }
	   
	   public static void main(String args[]) { 

		   @SuppressWarnings("unused")
		ServidorChat server = null;
		      if (args.length < 1) {
		         System.out.println("--- Para iniciar un servicio: \njava -cp diad.servicio.chat.servidor.jar udgv.servidor.ServidorChat <PuertoLocal> <BackLog> <IPv4Local>");}
		      
		      else {
		    	  switch (args.length) {
				case 1:
					server = new ServidorChat(Integer.parseInt(args[0]));
					break;
				case 3:
					try {
						server = new ServidorChat(Integer.parseInt(args[0]), Integer.parseInt(args[1]), InetAddress.getByName(args[2]));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
		         }
	   }
	   
	   

	//-------------- Medios de acceso a atributos -----------------------//	   

	public HiloServidor[] getClients() {
		return clients;
	}

	public void setClients(HiloServidor[] clients) {
		this.clients = clients;
	}

	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public int getClientCount() {
		return clientCount;
	}

	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}
	   
	   
}
