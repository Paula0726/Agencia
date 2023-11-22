package co.edu.uniquindio.agencia.views;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import co.edu.uniquindio.agencia.model.PaqueteTuristico;

public class StarterView extends ApplicationWindow {
    public StarterView(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createContents(Composite parent) {
    	Composite container = new Composite(parent, SWT.NONE);
    	container.setLayout(new GridLayout(1, false));

    	Button btnPaquetesTuristicos = new Button(container, SWT.NONE);
    	btnPaquetesTuristicos.setText("INICIALIZAR");

    	// Crear un objeto GridData para el botón y establecer la alineación en el centro
    	GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
    	btnPaquetesTuristicos.setLayoutData(gridData);

    	btnPaquetesTuristicos.addSelectionListener(new SelectionAdapter() {
    	    @Override
    	    public void widgetSelected(SelectionEvent e) {
    	        abrirVista();
    	    }
    	});

    	return container;
    }
    
    private void abrirVista() {
    	PrincipalView view = new PrincipalView(getShell());
    	view.open();   
    
    	//PaqueteTuristicoView view = new PaqueteTuristicoView(getShell());
    	//view.open();
    }
}