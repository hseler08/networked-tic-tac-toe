
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.*;
import java.lang.ClassNotFoundException;


public class Polacz implements Runnable {

    ServerSocket server;
    public static int port = 4998;
    public static Socket socket;
    public static ObjectInputStream ois;
    public static ObjectOutputStream oos;
    public static String msg;
    Thread t;
    Okno oknoGlowne;

    Polacz(Okno o){
        t=new Thread(this);
        t.start();
        oknoGlowne=o;
    }
    public void run(){

        try{
            System.out.println("server start");
            socket=server.accept();
            System.out.println("server accepted");
        }catch(Exception e){
            e.printStackTrace();
        }
        msg="";
        try{
            oos=new ObjectOutputStream(socket.getOutputStream());
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            ois=new ObjectInputStream(socket.getInputStream());
        }catch(Exception e){
            e.printStackTrace();
        }

        while(!msg.equals("exit")){

            try{
                msg =(String) ois.readObject();
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("brak klienta");
            }
            if(msg !=""){

                if(msg.equals("restart")){
                    oknoGlowne.restart();
                }
                else if(msg.equals("exit")){
                    try{
                        oos.close();
                        ois.close();
                        socket.close();
                        server.close();
                        System.out.println("server closed");
                        System.exit(0);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                else{
                    System.out.println(msg);
                    oknoGlowne.pole[Integer.parseInt(msg)].setText("X");
                    oknoGlowne.pole[Integer.parseInt(msg)].doClick();
                    oknoGlowne.ruch = !oknoGlowne.ruch;
                }
            }

        }
    }
}
