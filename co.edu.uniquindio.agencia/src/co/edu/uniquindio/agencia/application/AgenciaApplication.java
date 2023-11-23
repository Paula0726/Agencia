package co.edu.uniquindio.agencia.application;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;
import co.edu.uniquindio.agencia.views.StarterView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

public class AgenciaApplication {
    public static void main(String[] args) {
        try (
            // Se establece la conexión con el servidor en el puerto 12345
            Socket socket = new Socket("localhost", 12345);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        ) {
            // Se inicia la aplicación
            init(outputStream, inputStream);
        } catch (IOException e) {
            // Manejo de excepciones en caso de error de entrada/salida
            e.printStackTrace();
        }
    }

    private static void init(ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        try {
            // Se lee el mensaje inicial del servidor
            Object serverMessage = inputStream.readObject();

            // Se crea una instancia del controlador de fábrica de modelos
            ModelFactoryController modelFactoryController = ModelFactoryController.getInstance();
            modelFactoryController.inicializarDatos(serverMessage, inputStream, outputStream);
            modelFactoryController.start();

            // Se inicia el hilo de la interfaz de usuario SWT
            Display display = Display.getDefault();
            Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
                public void run() {
                    try {
                        // Se abre la vista de inicio y se configura para bloquear hasta que se cierre
                        StarterView starterView = new StarterView(Display.getCurrent().getActiveShell());
                        starterView.open();
                        starterView.setBlockOnOpen(true);
                        starterView.open();

                        // Se cierra la instancia actual de la pantalla
                        Display.getCurrent().dispose();
                    } catch (Exception e) {
                        // Manejo de excepciones al abrir la vista
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException | ClassNotFoundException e) {
            // Manejo de excepciones en caso de error de entrada/salida o clase no encontrada
            e.printStackTrace();
        }
    }
}
