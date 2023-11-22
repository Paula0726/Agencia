package co.edu.uniquindio.agencia.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Usuario;
import co.edu.uniquindio.implementation.Agencia;

public class IniciarSesionView extends ApplicationWindow {
	private ModelFactoryController modelFactoryController;
    private Agencia agencia;
	
    Text textUsuario;
    Text textContrasena;
    
    public IniciarSesionView(Shell parentShell) {
        super(parentShell);
        modelFactoryController = ModelFactoryController.getInstance();
        agencia = modelFactoryController.getAgencia();
    }

    @Override
    protected Control createContents(Composite parent) {
    	getShell().setText("Iniciar sesión");        
    	getShell().setSize(300, 150);
    	
    	parent.setLayout(new GridLayout(2, false));    	
    	fillBlank(parent, 1);
    	
    	Label lblUsuario = new Label(parent, SWT.NONE);
        lblUsuario.setText("Usuario:");

        textUsuario = new Text(parent, SWT.BORDER);
        textUsuario.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Label lblContrasena = new Label(parent, SWT.NONE);
        lblContrasena.setText("Contraseña:");

        textContrasena = new Text(parent, SWT.BORDER | SWT.PASSWORD);
        textContrasena.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    	
        Button btnIniciarSesion = new Button(parent, SWT.NONE);
        btnIniciarSesion.setText("Iniciar Sesión");
        btnIniciarSesion.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	boolean loginCliente = agencia.getUsuario().verificarInicioSesion(textUsuario.getText(), textContrasena.getText());	
            	boolean loginAdmin = agencia.getAdministrador().verificarInicioSesion(textUsuario.getText(), textContrasena.getText());

            	if (loginCliente) {
            		Usuario usuario = agencia.obtenerUsuario(textUsuario.getText());            		
            		Cliente cliente = agencia.obtenerClienteId(usuario.getId());            		
            		
            		agencia.setClienteSession(cliente);
            		modelFactoryController.setAgencia(agencia);
            		
            		close();
            		PrincipalView principalView = new PrincipalView(getShell());
            		principalView.open();
            		
                 	MessageDialog.openInformation(getShell(), "Inicio de Sesión", "¡Inicio de sesión exitoso como usuario!");
            		
            	} else if (loginAdmin) {
            		close();
            		PaqueteTuristicoView paqueteTuristicoView = new PaqueteTuristicoView(getShell());
            		paqueteTuristicoView.open();
                 	MessageDialog.openInformation(getShell(), "Inicio de Sesión", "¡Inicio de sesión exitoso como administrador!");            		
            	} else {
            		
            		MessageDialog.openInformation(getShell(), "Inicio de Sesión", "Por favor, verifica las credenciales.");
            	}
            }
        });
        
    	return parent;     	
    }
    
    private Label fillBlank(Composite parent, int times) {
    	Label blank = null;
    	for (int i = 0; i < times; i++) {
    		blank = new Label(parent, SWT.NONE);
            blank.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
    	}
    	
    	return blank;
    }
}