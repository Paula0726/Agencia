package co.edu.uniquindio.agencia.views;

import java.util.stream.Collectors;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.implementation.Agencia;

public class PerfilView extends ApplicationWindow {	
	private ModelFactoryController modelFactoryController;
    private Agencia agencia;
    private Cliente clienteSession;
    
    GridData data;
    Text txtIdentificacion;
    Text txtNombreCompleto;
    Text txtCorreoElectronico;
    Text txtNumeroTelefono;
    Text txtDireccionResidencia;
	
    public PerfilView(Shell parentShell) {
        super(parentShell);
        modelFactoryController = ModelFactoryController.getInstance();
        agencia = modelFactoryController.getAgencia();
        clienteSession = agencia.getClienteSession();
    }

    @Override
    protected Control createContents(Composite parent) {  
    	parent.setLayout(new GridLayout(2, false));
    	fillBlank(parent, 1);

        Label lblTitulo = new Label(parent, SWT.NONE);
        lblTitulo.setText("Mi Perfil");
        Font font = new Font(Display.getDefault(), "Arial", 15, SWT.BOLD);
        lblTitulo.setFont(font);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        lblTitulo.setLayoutData(data);

        Button btnRegresar = new Button(parent, SWT.PUSH);
        btnRegresar.setText("Regresar");
        data = new GridData(SWT.RIGHT, SWT.FILL, true, false);
        data.widthHint = 200;
        btnRegresar.setLayoutData(data);
        btnRegresar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	close();
            	PrincipalView principalView = new PrincipalView(getShell());
            	principalView.open();
            }
        });

        Label separador = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
	    data.horizontalSpan = 2; 
	    data.verticalIndent = 10;
	    separador.setLayoutData(data);
        
        Label lblIdentificacion = new Label(parent, SWT.NONE);
        lblIdentificacion.setText("Identificación:");
        txtIdentificacion = new Text(parent, SWT.BORDER);
        data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        data.widthHint = 300;
        txtIdentificacion.setLayoutData(data);

        Label lblNombreCompleto = new Label(parent, SWT.NONE);
        lblNombreCompleto.setText("Nombre Completo:");
        txtNombreCompleto = new Text(parent, SWT.BORDER);
        data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        data.widthHint = 10;
        txtNombreCompleto.setLayoutData(data);

        Label lblCorreoElectronico = new Label(parent, SWT.NONE);
        lblCorreoElectronico.setText("Correo Electrónico:");
        txtCorreoElectronico = new Text(parent, SWT.BORDER);
        data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        data.widthHint = 10;
        txtCorreoElectronico.setLayoutData(data);

        Label lblNumeroTelefono = new Label(parent, SWT.NONE);
        lblNumeroTelefono.setText("Número de Teléfono:");
        txtNumeroTelefono = new Text(parent, SWT.BORDER);
        data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        data.widthHint = 10;
        txtNumeroTelefono.setLayoutData(data);

        Label lblDireccionResidencia = new Label(parent, SWT.NONE);
        lblDireccionResidencia.setText("Dirección de Residencia:");
        txtDireccionResidencia = new Text(parent, SWT.BORDER);
        data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        data.widthHint = 10;
        txtDireccionResidencia.setLayoutData(data);
        
        separador = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
	    data.horizontalSpan = 2; 
	    data.verticalIndent = 10;
	    separador.setLayoutData(data);

	    Composite buttonsComposite = new Composite(parent, SWT.NONE);
        data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        data.horizontalSpan = 2;        
        data.verticalIndent = 20;
        buttonsComposite.setLayoutData(data);
        buttonsComposite.setLayout(new GridLayout(2, false)); 
	    
        Button btnActualizar = new Button(buttonsComposite, SWT.PUSH);
        btnActualizar.setText("Actualizar perfil");
        data = new GridData(SWT.NONE, SWT.CENTER, false, false);
        btnActualizar.setLayoutData(data);
        btnActualizar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	String mensaje = agencia.actualizarCliente(clienteSession, fieldsToString());
            	MessageDialog.openInformation(getShell(), "Actualizar perfil", mensaje);
            }
        });

        Button btnEliminar = new Button(buttonsComposite, SWT.PUSH);
        btnEliminar.setText("Eliminar perfil");
        data = new GridData(SWT.NONE, SWT.CENTER, false, false);
        data.horizontalIndent = 150;
        btnEliminar.setLayoutData(data);
        btnEliminar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	String mensaje = agencia.eliminarClienteUsuario(clienteSession.getIdentificacion());
                MessageDialog.openInformation(getShell(), "Eliminar perfil", mensaje);
            }
        });

        separador = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
	    data.horizontalSpan = 2; 
	    data.verticalIndent = 10;
	    separador.setLayoutData(data);
        
	    loadSessionData();
	    
		return parent;
    }
    
    private void loadSessionData() {
    	txtIdentificacion.setText(clienteSession.getIdentificacion());
    	txtNombreCompleto.setText(clienteSession.getNombreCompleto());
    	txtCorreoElectronico.setText(clienteSession.getCorreoElectronico());
    	txtNumeroTelefono.setText(clienteSession.getNumeroTelefono());
    	txtDireccionResidencia.setText(clienteSession.getDireccionResidencia());    	
    }
    
    private String fieldsToString() {
        String identificacion = txtIdentificacion.getText();
        String nombre = txtNombreCompleto.getText();
        String correo = txtCorreoElectronico.getText();
        String telefono = txtNumeroTelefono.getText();
        String direccion = txtDireccionResidencia.getText();
        
        String id = clienteSession.getId();     
        String destinosBuscados = clienteSession.getDestinosBuscados().stream().collect(Collectors.joining(","));  

        // Construir el formato de cadena
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id).append("@@");  
        stringBuilder.append(identificacion).append("@@"); 
        stringBuilder.append(nombre).append("@@");  
        stringBuilder.append(correo).append("@@");  
        stringBuilder.append(telefono).append("@@"); 
        stringBuilder.append(direccion).append("@@");
        stringBuilder.append(id).append("@@");
        stringBuilder.append(destinosBuscados);

        return stringBuilder.toString();
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
