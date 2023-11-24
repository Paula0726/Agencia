package co.edu.uniquindio.agencia.views;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Destino;
import co.edu.uniquindio.agencia.model.EstadoReserva;
import co.edu.uniquindio.agencia.model.GuiaTuristico;
import co.edu.uniquindio.agencia.model.PaqueteTuristico;
import co.edu.uniquindio.agencia.model.Reserva;
import co.edu.uniquindio.implementation.Agencia;

public class MisReservasView extends ApplicationWindow {
	private ModelFactoryController modelFactoryController;
    private Agencia agencia;
	private Cliente clienteSession;
    
	private List<Reserva> pendientes;
    private List<Reserva> confirmadas;
    private List<Reserva> pasadas;
	    	    
    private Reserva pendienteSeleccionada;
    private Reserva confirmadaSeleccionada;
    private Reserva pasadaSeleccionada;
	private Destino destinoSeleccionado;
    
    GridData data;
    TableViewer tablePasadasViewer; 
    TableViewer tableConfirmadasViewer;
    
    // Clase MisReservasView que extiende de la clase Dialog de SWT
    public MisReservasView(Shell parentShell) {
        // Llama al constructor de la clase base (Dialog)
        super(parentShell);

        // Obtiene la instancia del controlador de la fábrica de modelos
        modelFactoryController = ModelFactoryController.getInstance();

        // Obtiene la instancia de la agencia a través del controlador de la fábrica de modelos
        agencia = modelFactoryController.getAgencia();

        // Obtiene la sesión del cliente desde la instancia de la agencia
        clienteSession = agencia.getClienteSession();

        // Filtra y recoge en la lista 'pendientes' las reservas futuras del cliente con estado 'PENDIENTE'
        pendientes = agencia.listarReservasFuturasCliente(clienteSession).stream()
            .filter(reserva -> reserva.getEstadoReserva() == EstadoReserva.PENDIENTE)
            .collect(Collectors.toList());

        // Filtra y recoge en la lista 'confirmadas' las reservas futuras del cliente con estado 'CONFIRMADA'
        confirmadas = agencia.listarReservasFuturasCliente(clienteSession).stream()
            .filter(reserva -> reserva.getEstadoReserva() == EstadoReserva.CONFIRMADA)
            .collect(Collectors.toList());;

        // Obtiene y guarda en la lista 'pasadas' las reservas pasadas del cliente
        pasadas = agencia.listarReservasPasadasCliente(clienteSession);
    }


    @Override
    protected Control createContents(Composite parent) { 
    	getShell().setText("Mis reservas");        
    	
    	parent.setLayout(new GridLayout(3, false));
    	fillBlank(parent, 2);    
    	
    	Composite headerComposite = new Composite(parent, SWT.NONE);
    	headerComposite.setLayout(new GridLayout(2, false));
    	headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

        Label lblCrearReserva = new Label(headerComposite, SWT.NONE);
        lblCrearReserva.setText("Mis reservas");
        Font font = new Font(Display.getDefault(), "Arial", 15, SWT.BOLD);
        lblCrearReserva.setFont(font);
        data = new GridData(SWT.LEFT, SWT.FILL, false, false);
        data.horizontalIndent = 5;
        lblCrearReserva.setLayoutData(data);

        Button btnRegresar = new Button(headerComposite, SWT.PUSH);
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
        
        Group grpPendientes = new Group(parent, SWT.NONE);
        grpPendientes.setText("Lista de reservas pendientes");
        data = new GridData(SWT.FILL, SWT.NONE, true, false);
        grpPendientes.setLayoutData(data);
        grpPendientes.setLayout(new GridLayout(1, false));
                
        TableViewer tablePendientesViewer = new TableViewer(grpPendientes, SWT.BORDER);
        Table tablePendientes = createReservasTable(tablePendientesViewer, "PENDIENTE");
        tablePendientesViewer.setContentProvider(ArrayContentProvider.getInstance());        
        tablePendientesViewer.setInput(pendientes);        
        data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
    	data.heightHint = 85;
    	data.widthHint = 500;
    	tablePendientes.setLayoutData(data);

        Composite buttonComposite = new Composite(parent, SWT.NONE);
        buttonComposite.setLayout(new GridLayout(1, false));
        data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.verticalIndent = 15;
        buttonComposite.setLayoutData(data);

        Button buttonEnviar = new Button(buttonComposite, SWT.PUSH);
        buttonEnviar.setText("--->");
        buttonEnviar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        Button buttonRegresar = new Button(buttonComposite, SWT.PUSH);
        buttonRegresar.setText("<---");
        buttonRegresar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        Group grpConfirmadas = new Group(parent, SWT.NONE);
        grpConfirmadas.setText("Lista de reservas confirmadas");
        data = new GridData(SWT.FILL, SWT.NONE, true, false);
        grpConfirmadas.setLayoutData(data);
        grpConfirmadas.setLayout(new GridLayout(1, false));
                
        TableViewer tableConfirmadasViewer = new TableViewer(grpConfirmadas, SWT.BORDER);
        Table tableConfirmadas = createReservasTable(tableConfirmadasViewer, "CONFIRMADA");
        tableConfirmadasViewer.setContentProvider(ArrayContentProvider.getInstance());        
        tableConfirmadasViewer.setInput(confirmadas);        
        data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
    	data.heightHint = 85;
    	data.widthHint = 500;
    	tableConfirmadas.setLayoutData(data);
    	
    	// Este bloque de código define un SelectionListener para el botón de enviar en un proyecto Java SWT.

    	buttonEnviar.addSelectionListener(new SelectionAdapter() {
    	    @Override
    	    public void widgetSelected(SelectionEvent e) {
    	        // Verifica si hay una reserva pendiente seleccionada.
    	        if (pendienteSeleccionada != null) { 
    	            // Crea una copia de la reserva pendiente.
    	            Reserva reservaCopy = crearReservaCopy(pendienteSeleccionada); 
    	            
    	            // Actualiza el estado de la reserva pendiente a CONFIRMADA.
    	            pendienteSeleccionada.setEstadoReserva(EstadoReserva.CONFIRMADA);
    	            
    	            // Elimina la reserva pendiente de la lista de pendientes y la agrega a la lista de confirmadas.
    	            pendientes.remove(pendienteSeleccionada);
    	            confirmadas.add(pendienteSeleccionada);

    	            // Actualiza la reserva en la agencia y muestra un cuadro de diálogo informativo.
    	            String mensaje = agencia.actualizarReserva(reservaCopy, pendienteSeleccionada.convertirEnCadena());
    	            MessageDialog.openInformation(getShell(), "Reserva actualizada", mensaje);

    	            // Reinicializa la reserva seleccionada y actualiza las vistas de las tablas.
    	            pendienteSeleccionada = null;
    	            tablePendientesViewer.refresh();
    	            tableConfirmadasViewer.refresh();
    	        } 
    	    }
    	});

    	// Este bloque de código define un SelectionListener para el botón de regresar en un proyecto Java SWT.
    	buttonRegresar.addSelectionListener(new SelectionAdapter() {
    	    @Override
    	    public void widgetSelected(SelectionEvent e) {
    	        // Verifica si hay una reserva confirmada seleccionada.
    	        if (confirmadaSeleccionada != null) {
    	            // Crea una copia de la reserva confirmada.
    	            Reserva reservaCopy = crearReservaCopy(confirmadaSeleccionada); 
    	            
    	            // Actualiza el estado de la reserva confirmada a PENDIENTE.
    	            confirmadaSeleccionada.setEstadoReserva(EstadoReserva.PENDIENTE);
    	            
    	            // Elimina la reserva confirmada de la lista de confirmadas y la agrega a la lista de pendientes.
    	            confirmadas.remove(confirmadaSeleccionada);
    	            pendientes.add(confirmadaSeleccionada);
    	            
    	            // Actualiza la reserva en la agencia y muestra un cuadro de diálogo informativo.
    	            String mensaje = agencia.actualizarReserva(reservaCopy, confirmadaSeleccionada.convertirEnCadena());
    	            MessageDialog.openInformation(getShell(), "Reserva actualizada", mensaje);

    	            // Reinicializa la reserva seleccionada y actualiza las vistas de las tablas.
    	            confirmadaSeleccionada = null;
    	            tableConfirmadasViewer.refresh();
    	            tablePendientesViewer.refresh();
    	        }
    	    }
    	});
    	
    	Group grpCancelar = new Group(parent, SWT.NONE);
    	data = new GridData(SWT.FILL, SWT.FILL, true, false);
        data.horizontalSpan = 3;
        grpCancelar.setLayoutData(data);
        grpCancelar.setLayout(new GridLayout(2, false));
    	
        Button btnCancelarReservaPendienteDestino = new Button(grpCancelar, SWT.PUSH);
        btnCancelarReservaPendienteDestino.setText("Cancelar reserva pendiente");
        btnCancelarReservaPendienteDestino.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, true, false));
        btnCancelarReservaPendienteDestino.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (pendienteSeleccionada != null) {
                	String mensaje = agencia.eliminarReserva(pendienteSeleccionada.getId());
                	pendientes.remove(pendienteSeleccionada);
                	
                    MessageDialog.openInformation(getShell(), "Cancelar reserva", mensaje);
                    tablePendientesViewer.refresh();
                } else {
                    MessageDialog.openInformation(getShell(), "Cancelar reserva", "Selecciona una reserva pendiente a cancelar.");
                }
            }
        });
        
        Button btnCancelarReservaConfirmadaDestino = new Button(grpCancelar, SWT.PUSH);
        btnCancelarReservaConfirmadaDestino.setText("Cancelar reserva confirmada");
        btnCancelarReservaConfirmadaDestino.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, true, false));
        btnCancelarReservaConfirmadaDestino.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (confirmadaSeleccionada != null) {
                    String mensaje = agencia.eliminarReserva(confirmadaSeleccionada.getId());
                    confirmadas.remove(confirmadaSeleccionada);
                    
                    MessageDialog.openInformation(getShell(), "Cancelar reserva", mensaje);
                    tableConfirmadasViewer.refresh();
                } else {
                    MessageDialog.openInformation(getShell(), "Cancelar reserva", "Selecciona una reserva confirmada a cancelar.");
                }
            }
        });
    	
    	Label separador = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
	    data.horizontalSpan = 3; 
	    data.verticalIndent = 10;
	    separador.setLayoutData(data);
    	
    	Group grpPasadas = new Group(parent, SWT.NONE);
    	grpPasadas.setText("Lista de reservas pasadas");
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalSpan = 3;
        data.verticalIndent = 20;
        grpPasadas.setLayoutData(data);
        grpPasadas.setLayout(new GridLayout(1, false));
                
        TableViewer tablePasadasViewer = new TableViewer(grpPasadas, SWT.BORDER);
        Table tablePasadas = createReservasTable(tablePasadasViewer, "PASADA");
        tablePasadasViewer.setContentProvider(ArrayContentProvider.getInstance());        
        tablePasadasViewer.setInput(pasadas);        
        data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    	data.heightHint = 85;
    	data.widthHint = 500;
    	tablePasadas.setLayoutData(data);

    	Group grpButtons = new Group(parent, SWT.NONE);
        data = new GridData(SWT.CENTER, SWT.NONE, true, false);
        data.horizontalSpan = 3;
        grpButtons.setLayoutData(data);
        grpButtons.setLayout(new GridLayout(2, false));
    	
        // Crear un botón para calificar el destino
        Button btnCalificarDestino = new Button(grpButtons, SWT.PUSH);
        btnCalificarDestino.setText("Calificar destino");
        btnCalificarDestino.setLayoutData(new GridData(SWT.NONE, SWT.NONE, true, false));

        // Agregar un listener para manejar el evento de selección del botón de calificar destino
        btnCalificarDestino.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Verificar si se ha seleccionado una reserva pasada
                if (pasadaSeleccionada != null) {
                    // Abrir la ventana de calificación del destino con la reserva pasada seleccionada
                    abrirCalificarDestino(Display.getCurrent(), pasadaSeleccionada);
                } else {
                    // Mostrar un mensaje de información si no se ha seleccionado ninguna reserva pasada
                    MessageDialog.openInformation(getShell(), "Calificar destino", "Selecciona una reserva pasada a calificar el destino.");
                }
            }
        });

        // Crear un botón para calificar al guía
        Button btnCalificarGuia = new Button(grpButtons, SWT.PUSH);
        btnCalificarGuia.setText("Calificar guía");
        btnCalificarGuia.setLayoutData(new GridData(SWT.NONE, SWT.NONE, true, false));

        // Agregar un listener para manejar el evento de selección del botón de calificar guía
        btnCalificarGuia.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Verificar si se ha seleccionado una reserva pasada
                if (pasadaSeleccionada != null) {
                    // Abrir la ventana de calificación del guía con la reserva pasada seleccionada
                    abrirCalificarGuia(Display.getCurrent(), pasadaSeleccionada);
                } else {
                    // Mostrar un mensaje de información si no se ha seleccionado ninguna reserva pasada
                    MessageDialog.openInformation(getShell(), "Calificar destino", "Selecciona una reserva pasada a calificar el guía.");
                }
            }
        });

    	
    	
    	return parent;
    }
    
    private void abrirCalificarDestino(Display display, Reserva pasadaSeleccionada) {    	
    	destinoSeleccionado = null;
    	
    	Shell shell = new Shell(display);
    	shell.setText("Calificar destino");
    	shell.setLayout(new GridLayout(2, false));

    	Group grpTabla = new Group(shell, SWT.NONE);
    	grpTabla.setText("Selecciona un destino:");
    	GridData data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        data.horizontalSpan = 2;
        grpTabla.setLayoutData(data);
        grpTabla.setLayout(new GridLayout(1, false));
    	
        TableViewer tableDestinosViewer = new TableViewer(grpTabla, SWT.BORDER);        
        Table tableDestinos = tableDestinosViewer.getTable();
        tableDestinos.setHeaderVisible(true);
        tableDestinos.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        TableViewerColumn idColumn = new TableViewerColumn(tableDestinosViewer, SWT.NONE);
        idColumn.getColumn().setWidth(150);
        idColumn.getColumn().setText("Nombre");
        idColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((Destino) element).getNombre();
            }
        });        
        tableDestinosViewer.setContentProvider(ArrayContentProvider.getInstance());        
        tableDestinosViewer.setInput(pasadaSeleccionada.getPaqueteTuristico().getDestinos());        
        data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
    	data.heightHint = 85;
    	tableDestinos.setLayoutData(data);        
    	tableDestinos.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
            	destinoSeleccionado = (Destino) tableDestinos.getItem(tableDestinos.getSelectionIndex()).getData();          	
            }
        });
    	
    	
    	Group grpPuntuacion = new Group(shell, SWT.NONE);
        data = new GridData(SWT.NONE, SWT.NONE, true, false);
        data.horizontalSpan = 2;
        grpPuntuacion.setLayoutData(data);
        grpPuntuacion.setLayout(new GridLayout(2, false));
    	
        Label labelComboBox = new Label(grpPuntuacion, SWT.NONE);
        labelComboBox.setText("Selecciona una puntuación:");

        Combo combo = new Combo(grpPuntuacion, SWT.DROP_DOWN | SWT.READ_ONLY);
        for (int i = 1; i <= 5; i++) {
            combo.add(Integer.toString(i));
        }

        Group grpComment = new Group(shell, SWT.NONE);
        grpComment.setText("Agrega un comentario:");
        data = new GridData(SWT.NONE, SWT.NONE, true, false);
        data.horizontalSpan = 2;
        grpComment.setLayoutData(data);
        grpComment.setLayout(new GridLayout(1, false));
        
        Text texto = new Text(grpComment, SWT.BORDER);
        data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        data.widthHint = 200;
        data.heightHint = 150;
        texto.setLayoutData(data);

        Button enviarButton = new Button(grpComment, SWT.PUSH);
        enviarButton.setText("Enviar");
        data = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        data.verticalIndent = 20;
        enviarButton.setLayoutData(data);

        enviarButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	if (destinoSeleccionado != null) {
            	    String mensaje = agencia.calificarDestino(pasadaSeleccionada, destinoSeleccionado, Integer.parseInt(combo.getText()), texto.getText());                	
                	MessageDialog.openInformation(getShell(), "Calificar destino", mensaje);
            	} else {
                	MessageDialog.openInformation(getShell(), "Calificar destino", "Selecciona un destino a calificar.");
            	}
            }
        });

        shell.pack();
        shell.open();
    }
    
    private void abrirCalificarGuia(Display display, Reserva pasadaSeleccionada) {    	
    	GuiaTuristico guiaSeleccionado = pasadaSeleccionada.getGuiaTuristico();
    	
    	Shell shell = new Shell(display);
    	shell.setText("Calificar destino");
    	shell.setLayout(new GridLayout(2, false));

    	Group grpLabels = new Group(shell, SWT.NONE);
    	grpLabels.setText("Tu guía para esta reserva:");
    	GridData data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        data.horizontalSpan = 2;
        grpLabels.setLayoutData(data);
        grpLabels.setLayout(new GridLayout(1, false));
    	
        Label guiaLabel = new Label(grpLabels, SWT.NONE);
        guiaLabel.setText(guiaSeleccionado.getNombreCompleto());    
        Font font = new Font(Display.getDefault(), "Arial", 10, SWT.BOLD);
        guiaLabel.setFont(font);
        data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        guiaLabel.setLayoutData(data);
        
        Label puntuacionLabel = new Label(grpLabels, SWT.NONE);
        double promedio = guiaSeleccionado.getEstrellas().stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
        puntuacionLabel.setText("Puntuación media: "+promedio);    
        font = new Font(Display.getDefault(), "Arial", 8, SWT.BOLD);
        puntuacionLabel.setFont(font);
        data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        puntuacionLabel.setLayoutData(data);
    	
    	Group grpPuntuacion = new Group(shell, SWT.NONE);
        data = new GridData(SWT.NONE, SWT.NONE, true, false);
        data.horizontalSpan = 2;
        grpPuntuacion.setLayoutData(data);
        grpPuntuacion.setLayout(new GridLayout(2, false));
    	
        Label labelComboBox = new Label(grpPuntuacion, SWT.NONE);
        labelComboBox.setText("Selecciona una puntuación:");

        Combo combo = new Combo(grpPuntuacion, SWT.DROP_DOWN | SWT.READ_ONLY);
        for (int i = 1; i <= 5; i++) {
            combo.add(Integer.toString(i));
        }

        Group grpComment = new Group(shell, SWT.NONE);
        grpComment.setText("Agrega un comentario:");
        data = new GridData(SWT.NONE, SWT.NONE, true, false);
        data.horizontalSpan = 2;
        grpComment.setLayoutData(data);
        grpComment.setLayout(new GridLayout(1, false));
        
        Text texto = new Text(grpComment, SWT.BORDER);
        data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        data.widthHint = 200;
        data.heightHint = 150;
        texto.setLayoutData(data);

        Button enviarButton = new Button(grpComment, SWT.PUSH);
        enviarButton.setText("Enviar");
        data = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        data.verticalIndent = 20;
        enviarButton.setLayoutData(data);

        enviarButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
        	    String mensaje = agencia.calificarGuia(pasadaSeleccionada, guiaSeleccionado, Integer.parseInt(combo.getText()), texto.getText());                	
            	MessageDialog.openInformation(getShell(), "Calificar guía", mensaje);            	
            }
        });

        shell.pack();
        shell.open();
    }
    
    private Table createReservasTable(TableViewer tableViewer, String tableIs) {
    	Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
        
        // Columna ID
        TableViewerColumn idColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        idColumn.getColumn().setWidth(50);
        idColumn.getColumn().setText("ID");
        idColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((Reserva) element).getId();
            }
        });

        // Columna Fecha Solicitud
        TableViewerColumn fechaSolicitudColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        fechaSolicitudColumn.getColumn().setWidth(80);
        fechaSolicitudColumn.getColumn().setText("Fecha Solicitud");
        fechaSolicitudColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                LocalDate fechaSolicitud = ((Reserva) element).getFechaSolicitud();
                return fechaSolicitud.toString();
            }
        });

        // Columna Fecha Planificada Viaje
        TableViewerColumn fechaPlanificadaViajeColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        fechaPlanificadaViajeColumn.getColumn().setWidth(120);
        fechaPlanificadaViajeColumn.getColumn().setText("Fecha Planificada Viaje");
        fechaPlanificadaViajeColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                LocalDate fechaPlanificadaViaje = ((Reserva) element).getFechaPlanificadaViaje();
                return fechaPlanificadaViaje.toString();
            }
        });

        // Columna Cantidad Personas
        TableViewerColumn cantidadPersonasColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        cantidadPersonasColumn.getColumn().setWidth(80);
        cantidadPersonasColumn.getColumn().setText("Cantidad Personas");
        cantidadPersonasColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return Integer.toString(((Reserva) element).getCantidadPersonas());
            }
        });

        // Columna Paquete Turístico
        TableViewerColumn paqueteTuristicoColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        paqueteTuristicoColumn.getColumn().setWidth(100);
        paqueteTuristicoColumn.getColumn().setText("Paquete Turístico");
        paqueteTuristicoColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                PaqueteTuristico paqueteTuristico = ((Reserva) element).getPaqueteTuristico();
                return paqueteTuristico.getNombre();
            }
        });

        // Columna Guía Turístico
        TableViewerColumn guiaTuristicoColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        guiaTuristicoColumn.getColumn().setWidth(80);
        guiaTuristicoColumn.getColumn().setText("Guía Turístico");
        guiaTuristicoColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                GuiaTuristico guiaTuristico = ((Reserva) element).getGuiaTuristico();
                return guiaTuristico.getNombreCompleto();
            }
        });

        // Columna Estado Reserva
        TableViewerColumn estadoReservaColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        estadoReservaColumn.getColumn().setWidth(90);
        estadoReservaColumn.getColumn().setText("Estado Reserva");
        estadoReservaColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                EstadoReserva estadoReserva = ((Reserva) element).getEstadoReserva();
                return estadoReserva.toString();
            }
        });
        
        
        table.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
            	if (tableIs == "PENDIENTE") {
                	pendienteSeleccionada = (Reserva) table.getItem(table.getSelectionIndex()).getData();
            	} else if (tableIs == "CONFIRMADA") {
            		confirmadaSeleccionada = (Reserva) table.getItem(table.getSelectionIndex()).getData();                	            		
            	} else if (tableIs == "PASADA") {
                	pasadaSeleccionada = (Reserva) table.getItem(table.getSelectionIndex()).getData();                	            		
            	}            	
            }
        });

        return table;
    }
    
    private Reserva crearReservaCopy(Reserva reserva) {
        Reserva reservaOld = new Reserva();
        reservaOld.setId(reserva.getId());
        reservaOld.setFechaSolicitud(reserva.getFechaSolicitud());
        reservaOld.setFechaPlanificadaViaje(reserva.getFechaPlanificadaViaje());
        reservaOld.setCliente(reserva.getCliente());
        reservaOld.setCantidadPersonas(reserva.getCantidadPersonas());
        reservaOld.setPaqueteTuristico(reserva.getPaqueteTuristico());
        reservaOld.setGuiaTuristico(reserva.getGuiaTuristico());
        reservaOld.setEstadoReserva(reserva.getEstadoReserva());
        
        return reservaOld;
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