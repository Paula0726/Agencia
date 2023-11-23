package co.edu.uniquindio.agencia.views;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.implementation.Agencia;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EmailView extends ApplicationWindow {
	private ModelFactoryController modelFactoryController;
    private Agencia agencia;
    private Cliente clienteSession;
    
    private List<Email> listaCorreos;
    
    GridData data;
    TableViewer tableCorreoViewer;
    
    // Clase EmailView que extiende de la clase Dialog de SWT
    public EmailView(Shell parentShell) {
        // Llama al constructor de la clase padre con el Shell padre proporcionado
        super(parentShell);

        // Obtiene la instancia del controlador de fábrica de modelos
        modelFactoryController = ModelFactoryController.getInstance();

        // Obtiene la instancia de la agencia a través del controlador de fábrica de modelos
        agencia = modelFactoryController.getAgencia();

        // Obtiene la sesión del cliente asociado a la agencia
        clienteSession = agencia.getClienteSession();

        // Filtra la lista de reservas de la agencia para obtener solo las del cliente actual
        listaCorreos = agencia.getListaReservas().stream()
                .filter(reserva -> reserva.getCliente().getId().equals(clienteSession.getId()))
                .map(reserva -> {
                    // Crea un objeto Email y asigna los valores correspondientes de la reserva
                    Email correo = new Email();
                    correo.setFechaSolicitud(reserva.getFechaSolicitud());
                    correo.setCantidadPersonas(reserva.getCantidadPersonas());
                    correo.setGuiaNombre(reserva.getGuiaTuristico().getNombreCompleto());
                    correo.setPaqueteNombre(reserva.getPaqueteTuristico().getNombre());
                    correo.setTotal(reserva.getGuiaTuristico().getPrecio() + reserva.getPaqueteTuristico().getPrecio());
                    return correo; // Devuelve el objeto Email creado
                })
                .collect(Collectors.toList()); // Convierte el resultado en una lista y asigna a listaCorreos

    } // Fin del constructor EmailView

    
    @Override
    protected Control createContents(Composite parent) {   
    	parent.setLayout(new GridLayout(2, false));
    	fillBlank(parent, 1);
    	
    	Label lblTitulo = new Label(parent, SWT.NONE);
        lblTitulo.setText("Correo");
        Font font = new Font(Display.getDefault(), "Arial", 15, SWT.BOLD);
        lblTitulo.setFont(font);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        lblTitulo.setLayoutData(data);
    	
        Group grpPaquetes = new Group(parent, SWT.NONE);
        grpPaquetes.setText("Bandeja de entrada");
        data = new GridData(SWT.FILL, SWT.NONE, true, false);
        data.horizontalSpan = 2;
        grpPaquetes.setText("");
        grpPaquetes.setLayoutData(data);
        grpPaquetes.setLayout(new GridLayout(2, false));
        
        tableCorreoViewer = new TableViewer(grpPaquetes, SWT.BORDER);
        Table tablePaquetes = createBandejaTable();
        tableCorreoViewer.setContentProvider(ArrayContentProvider.getInstance());        
        tableCorreoViewer.setInput(listaCorreos);
        data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
    	data.horizontalSpan = 2;
    	data.heightHint = 85;
    	tablePaquetes.setLayoutData(data);
    	
    	return parent;
    } 
    
    private Table createBandejaTable() {
        Table table = tableCorreoViewer.getTable();
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));

        // Columna Fecha de Solicitud
        TableViewerColumn idColumn = new TableViewerColumn(tableCorreoViewer, SWT.NONE);
        idColumn.getColumn().setWidth(100);
        idColumn.getColumn().setText("Fecha de Solicitud");
        idColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                LocalDate fechaSolicitud = ((Email) element).getFechaSolicitud();
                return fechaSolicitud.toString();
            }
        });

        // Columna Cantidad de Personas
        TableViewerColumn cantidadPersonasColumn = new TableViewerColumn(tableCorreoViewer, SWT.NONE);
        cantidadPersonasColumn.getColumn().setWidth(100);
        cantidadPersonasColumn.getColumn().setText("Cantidad de Personas");
        cantidadPersonasColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return Integer.toString(((Email) element).getCantidadPersonas());
            }
        });

        // Columna Paquete Nombre
        TableViewerColumn paqueteNombreColumn = new TableViewerColumn(tableCorreoViewer, SWT.NONE);
        paqueteNombreColumn.getColumn().setWidth(150);
        paqueteNombreColumn.getColumn().setText("Paquete Nombre");
        paqueteNombreColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((Email) element).getPaqueteNombre();
            }
        });

        // Columna Guía Nombre
        TableViewerColumn guiaNombreColumn = new TableViewerColumn(tableCorreoViewer, SWT.NONE);
        guiaNombreColumn.getColumn().setWidth(150);
        guiaNombreColumn.getColumn().setText("Guía Nombre");
        guiaNombreColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((Email) element).getGuiaNombre();
            }
        });

        // Columna Total
        TableViewerColumn totalColumn = new TableViewerColumn(tableCorreoViewer, SWT.NONE);
        totalColumn.getColumn().setWidth(100);
        totalColumn.getColumn().setText("Total");
        totalColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return Integer.toString(((Email) element).getTotal());
            }
        });
        
        return table;
    }

    
    private Label fillBlank(Composite parent, int times) {
    	Label blank = null;
    	for (int i = 0; i < times; i++) {
    		blank = new Label(parent, SWT.NONE);
            blank.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
    	}
    	
    	return blank;
    }
    
    @Data
    @NoArgsConstructor
    public class Email {
        private LocalDate fechaSolicitud;
        private int cantidadPersonas;
        private String paqueteNombre;
        private String guiaNombre;
        private int total;
    }
}