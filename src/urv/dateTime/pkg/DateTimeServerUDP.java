package urv.dateTime.pkg;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class DateTimeServerUDP {

	public static void main(String[] args) throws Exception {

		try {
			/**
			 * Socket: Classe de java que internament implementa tots els protocols per a poder establir
			 * la comunicació (sent/receive).
			 **/

			/**
			 * 	Li passem el primer argument per a crear el datagram socket, que consisteix en el port
			 * 	a on el servidor farà el "listen". Aquest ha de ser més gran que 1024, ja que per sota
			 * 	els ports estàn reservats
			 */
			DatagramSocket ss = new DatagramSocket(Integer.parseInt(args[0]));
			while (true) {	// Entrem en bucle infinit

				System.out.println("Server is listening....");

				// Reservem memòria per a buffers
				byte[] rd = new byte[100];	// receive
				byte[] sd = new byte[100];	// send

				/**
				 * DataGram: Un paquet Datagram és aquell paquet que s'envia a través d'internet. Per a
				 * construir el paquet li hem de passar una ED per a
				 * guardar la informació, en aquest cas, li passem un vector de char de 100 posicions,
				 * junt amb la seva longitud
				 **/
				DatagramPacket rp = new DatagramPacket(rd, rd.length);

				// El servidor fa listen en el port que li hem passat per paràmetre per a construir el socket
				ss.receive(rp);
				// Ja hem rebut el paquet, que sobreescriu el datagram rp
				System.out.print("\nPackage received!");
				/**
				 * Obtenim la direcció IP origen del paquet.
				 * Dins del datagram, a la capçalera hi ha tota la info sobre la xarxa, la IP i el port.
				 * La vertadera informació es troba a dins, en el vector de char de rebuda.
				 * Podem observar que el servidor no utilitza la informació del buffer de char que ha
				 * rebut en el datagram, ja que només interessa el paquet en si per a obtenir la
				 * informació del reemitent i enviar-li la hora
				 *
				 */
				InetAddress ip = rp.getAddress(); // obtenim IP del remitent
				/**
				 * Obtenim Port reemitent. Aquest port sol ser un port identificat amb un número més gran
				 * que 30000, que vindrien a ser ports d'usar i tirar. Aquest port és el que ha utilitzat
				 * el client per enviar i el servidor sap que després estarà fent listen en aquest, per
				 * això no cal especificar-lo, ja que el client al fer el send, se'l assigna automàticament
				 */
				int port = rp.getPort();


				Date d = new Date(); // obtenim l'hora del sistema
				String time = d + ""; // converting it to String
				sd = time.getBytes(); // converting that String to byte
				System.out.println("\n\nDatagram Packet received from client contains the next data:" + "\nAdress: " + rp.getAddress() + "\nData: " + rp.getData() + "\nPort: " + rp.getPort() + "\nLength: " + rp.getLength() + "\nOffset: " + rp.getOffset() + "\nSocketAdress: " + rp.getSocketAddress());

				/**
				 * Creem un datagram per a enviar-li al client (remitent). Incloem la ip, el port
				 * i el buffer d'enviament, que conté el time
				 */
				DatagramPacket sp = new DatagramPacket(sd, sd.length, ip, port);
				System.out.println("\n\nDatagram Packet sent to client contains the next data:" + "\nAdress: " + sp.getAddress() + "\nData: " + sp.getData() + "\nPort: " + sp.getPort() + "\nLength: " + sp.getLength() + "\nOffset: " + sp.getOffset() + "\nSocketAdress: " + sp.getSocketAddress());

				ss.send(sp);

				//rp=null;

				System.out.println("\nPackage sent!! ");
				//falgorisme
			}
			
		} catch (Exception e) {
			System.out.println("S'ha produ�t un error: "+e.toString());
			e.printStackTrace();
		}
	}

}