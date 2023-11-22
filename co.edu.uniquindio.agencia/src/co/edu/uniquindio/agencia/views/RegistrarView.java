package co.edu.uniquindio.agencia.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Usuario;
import co.edu.uniquindio.implementation.Agencia;

public class RegistrarView extends ApplicationWindow {
	private ModelFactoryController modelFactoryController;
    private Agencia agencia;
	
    private Text textUsuario;
    private Text textContrasena;
    private Text textIdentificacion;
    private Text textNombreCompleto;
    private Text textCorreoElectronico;
    private Text textNumeroTelefono;
    private Text textDireccionResidencia;

    public RegistrarView(Shell parentShell) {
        super(parentShell);
        modelFactoryController = ModelFactoryController.getInstance();
        agencia = modelFactoryController.getAgencia();
    }

    @Override
    protected Control createContents(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(2, false));

        Label lblUsuario = new Label(container, SWT.NONE);
        lblUsuario.setText("Usuario:");

        textUsuario = new Text(container, SWT.BORDER);
        textUsuario.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

        Label lblContrasena = new Label(container, SWT.NONE);
        lblContrasena.setText("Contraseña:");

        textContrasena = new Text(container, SWT.BORDER | SWT.PASSWORD);
        textContrasena.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

        Label lblIdentificacion = new Label(container, SWT.NONE);
        lblIdentificacion.setText("Identificación:");

        textIdentificacion = new Text(container, SWT.BORDER);
        textIdentificacion.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
        
        Label lblNombreCompleto = new Label(container, SWT.NONE);
        lblNombreCompleto.setText("Nombre Completo:");

        textNombreCompleto = new Text(container, SWT.BORDER);
        textNombreCompleto.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

        Label lblCorreoElectronico = new Label(container, SWT.NONE);
        lblCorreoElectronico.setText("Correo Electrónico:");

        textCorreoElectronico = new Text(container, SWT.BORDER);
        textCorreoElectronico.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

        Label lblNumeroTelefono = new Label(container, SWT.NONE);
        lblNumeroTelefono.setText("Número de Teléfono:");

        textNumeroTelefono = new Text(container, SWT.BORDER);
        textNumeroTelefono.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

        Label lblDireccionResidencia = new Label(container, SWT.NONE);
        lblDireccionResidencia.setText("Dirección de Residencia:");

        textDireccionResidencia = new Text(container, SWT.BORDER);
        textDireccionResidencia.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

        Button btnRegistrar = new Button(container, SWT.NONE);
        btnRegistrar.setText("Registrar");
        btnRegistrar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	agencia.crearUsuario(usuarioFieldsToString());      	
            	String mensaje = agencia.crearCliente(clienteFieldsToString());
            	MessageDialog.openInformation(getShell(), "Registrar", mensaje);    
            	
            	Cliente cliente = agencia.obtenerCliente(textIdentificacion.getText());
            	agencia.setClienteSession(cliente);
            	            	
            	close();
            	PrincipalView principalView = new PrincipalView(getShell());
            	principalView.open();
            }
        });

        return container;
    }

    private String clienteFieldsToString() {
        String id = "0";
        String identificacion = textIdentificacion.getText();
        String nombreCompleto = textNombreCompleto.getText();
        String correoElectronico = textCorreoElectronico.getText();
        String numeroTelefono = textNumeroTelefono.getText();
        String direccionResidencia = textDireccionResidencia.getText();
                
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id).append("@@");  
        stringBuilder.append(identificacion).append("@@"); 
        stringBuilder.append(nombreCompleto).append("@@");  
        stringBuilder.append(correoElectronico).append("@@");  
        stringBuilder.append(numeroTelefono).append("@@"); 
        stringBuilder.append(direccionResidencia).append("@@");  
        stringBuilder.append(id).append("@@"); 
        stringBuilder.append("1,2,3"); 

        return stringBuilder.toString();
    }
    
    private String usuarioFieldsToString() {
        String id = "0";
        String usuario = textUsuario.getText();
        String contrasena = textContrasena.getText();
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id).append("@@");  
        stringBuilder.append(usuario).append("@@");  
        stringBuilder.append(contrasena);  

        return stringBuilder.toString();

    }
}
