package co.edu.uniquindio.agencia.views;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ChatView extends ApplicationWindow {

    private Socket socket;
	private Timer timer;
	
	ObjectOutputStream outputStream;
	ObjectInputStream inputStream;
	
    private int cooldown;
    
    private Text chatText;
    private Text mensajeText;

    public ChatView(Shell parentShell) {
        super(parentShell);
    
        cooldown = 10000; // 10 segundos
    }

    @Override
    protected Control createContents(Composite parent) {
    	getShell().setText("Chat");        

        parent.setLayout(new GridLayout(1, false));
    	
        Group container_chat = new Group(parent, SWT.NONE);
        container_chat.setLayout(new GridLayout(1, false));
        container_chat.setText("Ventana de chat");
        chatText = new Text(container_chat, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);        
        
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        data.heightHint = 50;
        data.widthHint = 300;
        chatText.setLayoutData(data);

        Group container_grp = new Group(parent, SWT.NONE);
        container_grp.setText("Escribe un mensaje:");
        container_grp.setLayout(new GridLayout(1, false)); 
        
        mensajeText = new Text(container_grp, SWT.BORDER);
        data = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        data.heightHint = 50;
        data.widthHint = 300;
        mensajeText.setLayoutData(data);
        
        Button enviarButton = new Button(parent, SWT.PUSH);
        enviarButton.setText("Enviar");
        enviarButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        enviarButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enviarMensaje(outputStream);
            }
        });

        iniciarConexion();

        return parent;
    }

    private void iniciarConexion() {
        try {
            socket = new Socket("localhost", 50000);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        	inputStream = new ObjectInputStream(socket.getInputStream());

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    cerrarConexion();
                }
            }, cooldown); 

            new Thread(() -> escucharMensajes(inputStream)).start();
        } catch (IOException e) {
        	//e.printStackTrace();
        }
    }

    private void escucharMensajes(ObjectInputStream inputStream) {
        try {
            while (true) {
                Object mensaje = inputStream.readObject();                
                if (mensaje != null) {
                    Display.getDefault().asyncExec(() -> chatText.append("Servidor: " + mensaje + "\n"));
                    reiniciarTemporizador();
                }
            }
        } catch (IOException e) {
        	System.out.println("Conexión cerrada");
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    }

    private void enviarMensaje(ObjectOutputStream outputStream) {
        String mensaje = mensajeText.getText();
        if (!mensaje.isEmpty()) {
        	try {
				outputStream.writeObject(mensaje);
				chatText.append("Cliente: " + mensaje + "\n");
	            mensajeText.setText("");
	            reiniciarTemporizador();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			};            
        }
    }

    private void cerrarConexion() {
        try {
            timer.cancel();
            if (socket != null && !socket.isClosed()) {
                Display.getDefault().asyncExec(() -> {
                    if (!getShell().isDisposed()) {
                        getShell().close();
                    }
                });
                socket.close();
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }


    private void reiniciarTemporizador() {
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                cerrarConexion();
            }
        }, cooldown);
    }
}


