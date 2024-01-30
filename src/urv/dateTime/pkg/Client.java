/**

 * @author Mike Meding
 *
 */
package urv.dateTime.pkg;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

    public static void main(String[] args) throws Exception {

        while (true) {
            /**
             * Creem un nou socket per a rebre datagrams
             */
            DatagramSocket cs = new DatagramSocket();
            /**
             * Obtenim la direcció IP a on volem enviar el nostre datagram. Utilitzem una funció que
             * ens transforma un nom de domini en una direcció IP. En aquest cas busca
             * la direcció de localhost, que es la màquina local. Aquesta direcció és sempre 127.0.0.1.
             * Per tant, aquest client/servidor no funcionaria en ordinadors diferents, sinó que és una
             * comunicació utilitzant la mateix màquina.
             *
             * La funció estàtica getByName també pot rebre una IP literal d'una altra màquina a la que
             * ens volem conectar. Podem obtenir la IP de la màquina executant ifconfig en linux, ipconfig -all
             * en Windows o conectant-nos a un servidor que ens digues la nostra IP.
             */

            InetAddress ip = InetAddress.getByName(args[0]);
            //allocate packet data space
            byte[] rd = new byte[100]; //received data
            byte[] sd = new byte[100]; //sending data

            /**
             * Create empty data packet to be sent. Dins del datagram incloem la IP i port a on enviarem
             * les dades. Aquest port ha de ser el mateix port que rep el servidor per paràmetre (el
             * servidor està fent listen en aquell port). El port és el segon paràmetre que rep (args[1])
             */
            DatagramPacket sp = new DatagramPacket(sd, sd.length, ip, Integer.parseInt(args[1]));
            // Printegem el contingut del paquet
            System.out.println("\n\nDatagram Packet sent to server contains the next data:" + "\nAdress: " + sp.getAddress() + "\nData (Empty for client): " + sp.getData() + "\nPort: " + sp.getPort() + "\nLength: " + sp.getLength() + "\nOffset: " + sp.getOffset() + "\nSocketAdress: " + sp.getSocketAddress());
            // Creem una instància del datagram per a guardar el datagram que rebem
            DatagramPacket rp = new DatagramPacket(rd, rd.length);

            /**
             * Enviem el paquet sp. Aquest paquet està buit, només enviem el paquet per a que el servidor
             * sàpiga qui som (IP) i a quin port enviem. El port des d'on enviem a la IP de client s'assigna
             * automàticament. I és el mateix port a on el client farà listen per esperar la resposta del
             * servidor. És un port més gran que 30000.
             */
            cs.send(sp);
            /**
             * Just després d'enviar les dades, ens posem a fer listen, que automàticament serà al port
             * on hem fet el send desde la IP local
             */
            cs.receive(rp);

            /**
             * Reconversió de la informació rebuda del servidor i print a pantalla de la data del servidor
             */
            String time = new String(rp.getData()); //get epoch time as a string
            try{
                System.out.println("\nPackage received!\nDatagram Packet received from server contains the next data:" + "\nAdress: " + rp.getAddress() + "\nData (Empty for client): " + rp.getData() + "\nPort: " + rp.getPort() + "\nLength: " + rp.getLength() + "\nOffset: " + rp.getOffset() + "\nSocketAdress: " + rp.getSocketAddress());
                time = time.substring(0, 29); // retallem per a no obtenir caracters brossa
                System.out.print("\nTime from Server is: " +time); // Mostra hora per pantalla
               /** //parse string to long
                //System.out.println(time);
                SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
                //long epoch = Long.parseLong(time,10);
                //create accurate time object
                Time tm = new Time(epoch);
                //Print out nicely formatted time.
                System.out.println(tm.getHours() +":"+ tm.getMinutes() +":"+ tm.getSeconds());
                **/
            } catch(NumberFormatException nfe) {
                nfe.printStackTrace();
            }

            // tanquem soquet
            cs.close();
            // aturem el thread de java durant 60 segons per a que la velocitat d'execució sigui a temps humà
            Thread.sleep(60000); //pause for 60 seconds
        }

    }
}