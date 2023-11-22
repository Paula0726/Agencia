package co.edu.uniquindio.agencia.application;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Destino;
import co.edu.uniquindio.agencia.model.Reserva;
import co.edu.uniquindio.implementation.Agencia;
import co.edu.uniquindio.agencia.views.StarterView;
import co.edu.uniquindio.agencia.views.GraficosView;
import co.edu.uniquindio.agencia.views.PaqueteTuristicoView;
import co.edu.uniquindio.agencia.views.PrincipalView;
import co.edu.uniquindio.util.util;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

public class AgenciaApplication {	
	public static void main(String[] args) {		
		try (
			Socket socket = new Socket("localhost", 12345);
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
		) {
			init(outputStream, inputStream);			
		} catch (IOException e) {
			e.printStackTrace();
		}		        
	}		
	
	private static void init(ObjectOutputStream outputStream, ObjectInputStream inputStream) {		
        try {
        	Object serverMessage = inputStream.readObject();

            ModelFactoryController modelFactoryController = new ModelFactoryController();
            modelFactoryController = ModelFactoryController.getInstance();
			modelFactoryController.inicializarDatos(serverMessage, inputStream, outputStream);
	        modelFactoryController.start();
	        

	        Display display = Display.getDefault();	        
			Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
				public void run() {
					try {
						StarterView starterView = new StarterView(Display.getCurrent().getActiveShell());
						starterView.open();
						starterView.setBlockOnOpen(true);
						starterView.open();
						
						Display.getCurrent().dispose();				
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
						
	        
	        //Agencia agencia = modelFactoryController.getAgencia();
    		//System.out.println(agencia.getListaClientes().get(0).getUsuario());
	        //agencia.generarInforme("pepito.txt");
			//System.out.println(agencia.getListaGuiasTuristicos().get(0));
	        //Cliente cliente = agencia.getClienteSession();
			//System.out.println(cliente.getId());
			//System.out.println(agencia.listarReservasFuturasCliente(cliente));
			
			
			//System.out.print(agencia.obtenerPaquetesMasReservados(5));
			
	        /*
    		Agencia agencia = modelFactoryController.getAgencia();
    		System.out.println(modelFactoryController.getAgencia().getUsuario().recomendarDestinos(
    				modelFactoryController.getAgencia().getListaClientes().get(0)
    				));
    		
    		modelFactoryController.getAgencia().getAdministrador().generarInforme("reportes/informe_1.txt");
    		//agencia.crearCliente("11@@689745362@@Juan Pérez@@juan.perez@email.com@@1234567890@@Calle 123, Ciudad");
    		        
    		Cliente cliente = modelFactoryController.getAgencia().getListaClientes().stream()
		            .filter(cli -> cli.getId().equals("11"))
		            .findFirst()
		            .orElse(null);;
    		//agencia.actualizarCliente(cliente, "11@@689745362@@Ronaldo@@juan.perez@email.com@@1234567890@@Calle 123, Ciudad");
		      
		    //List<Entry<String, Integer>> a = agencia.obtenerDestinosMasBuscados();
		    //System.out.print(a);    
		    		   
		    //agencia.eliminarCliente("10");
		     * 		
		     */
		    
        } catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
