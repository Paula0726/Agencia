package co.edu.uniquindio.agencia.views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Destino;
import co.edu.uniquindio.agencia.model.GuiaTuristico;
import co.edu.uniquindio.agencia.model.PaqueteTuristico;
import co.edu.uniquindio.implementation.Agencia;

public class CrearReservaView extends ApplicationWindow {
	private ModelFactoryController modelFactoryController;
    private Agencia agencia;
    private Cliente clienteSession;
    
    GridData data;
    DateTime dtFechaPlanificada;
    Text txtCantidadPersonas;
    
    private PaqueteTuristico paqueteSeleccionado;
    private GuiaTuristico guiaSeleccionado;
    
    private Text searchPaquetesText;
    private Text searchGuiasText;
    TableViewer tablePaquetesViewer;
    TableViewer tableGuiasViewer;
    
    private List<PaqueteTuristico> paquetes;
    private List<GuiaTuristico> guias;
	    
    public CrearReservaView(Shell parentShell) {
        super(parentShell);
        modelFactoryController = ModelFactoryController.getInstance();
        agencia = modelFactoryController.getAgencia();
        paquetes = agencia.getListaPaquetesTuristicos();
        guias = agencia.getListaGuiasTuristicos();
        clienteSession = agencia.getClienteSession();
    }

    @Override
    protected Control createContents(Composite parent) { 
    	getShell().setText("Crear reserva");        

        parent.setLayout(new GridLayout(2, false));
    	fillBlank(parent, 1);

        Label lblCrearReserva = new Label(parent, SWT.NONE);
        lblCrearReserva.setText("Crear reserva");
        Font font = new Font(Display.getDefault(), "Arial", 15, SWT.BOLD);
        lblCrearReserva.setFont(font);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        data.horizontalIndent = 15;
        lblCrearReserva.setLayoutData(data);

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

	    Group grpFields = new Group(parent, SWT.NONE);
        data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        data.horizontalSpan = 2;
        grpFields.setLayoutData(data);
        grpFields.setLayout(new GridLayout(2, false));
	    
        Label lblFechaPlanificada = new Label(grpFields, SWT.NONE);
        lblFechaPlanificada.setText("Fecha planificada de viaje:");
        dtFechaPlanificada = new DateTime(grpFields, SWT.DATE | SWT.DROP_DOWN);
        data = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
        data.widthHint = 300;
        dtFechaPlanificada.setLayoutData(data);
        dtFechaPlanificada.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                LocalDate selectedDate = LocalDate.of(dtFechaPlanificada.getYear(), dtFechaPlanificada.getMonth() + 1, dtFechaPlanificada.getDay());
                LocalDate currentDate = LocalDate.now();

                if (selectedDate.isBefore(currentDate)) {
                    // Ajustar la fecha seleccionada al día actual si es anterior
                    dtFechaPlanificada.setDate(currentDate.getYear(), currentDate.getMonthValue() - 1, currentDate.getDayOfMonth());
                }
            }
        });

        Label lblCantidadPersonas = new Label(grpFields, SWT.NONE);
        lblCantidadPersonas.setText("Cantidad de personas:");
        txtCantidadPersonas = new Text(grpFields, SWT.BORDER);
        data = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        txtCantidadPersonas.setLayoutData(data);
        
        separador = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
	    data.horizontalSpan = 2; 
	    data.verticalIndent = 10;
	    separador.setLayoutData(data);
                
        
        Group grpPaquetes = new Group(parent, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.NONE, true, false);
        data.horizontalSpan = 2;
        data.verticalIndent = -20;
        grpPaquetes.setLayoutData(data);
        grpPaquetes.setLayout(new GridLayout(2, false));
        
        Label titleLabel = new Label(grpPaquetes, SWT.NONE);
	    titleLabel.setText("Lista de paquetes turísticos"); 
	    titleLabel.setFont(new Font(parent.getDisplay(), "Arial", 9, SWT.NORMAL));
        data = new GridData(SWT.LEFT, SWT.BOTTOM, false, false);
	    titleLabel.setLayoutData(data);
    	
        searchPaquetesText = new Text(grpPaquetes, SWT.BORDER);
        data = new GridData(SWT.RIGHT, SWT.FILL, false, false);
        data.widthHint = 150;
        searchPaquetesText.setLayoutData(data);
        searchPaquetesText.setMessage("Buscar paquetes...");
        searchPaquetesText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updatePaquetesTableFilter(searchPaquetesText.getText().trim());
			}
		});
        tablePaquetesViewer = new TableViewer(grpPaquetes, SWT.BORDER);
        Table tablePaquetes = createPaquetesTable();
        tablePaquetesViewer.setContentProvider(ArrayContentProvider.getInstance());        
        tablePaquetesViewer.setInput(paquetes);
        data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
    	data.horizontalSpan = 2;
    	data.heightHint = 85;
    	tablePaquetes.setLayoutData(data);
    	
    	separador = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
	    data.horizontalSpan = 2; 
	    separador.setLayoutData(data);
    	
    	Group grpGuias = new Group(parent, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.NONE, true, false);
        data.verticalIndent = -20;
        data.horizontalSpan = 2;
        grpGuias.setLayoutData(data);
        grpGuias.setLayout(new GridLayout(2, false));
    	
    	titleLabel = new Label(grpGuias, SWT.NONE);
	    titleLabel.setText("Lista de guías turísticos"); 
	    titleLabel.setFont(new Font(parent.getDisplay(), "Arial", 9, SWT.NORMAL));
        data = new GridData(SWT.LEFT, SWT.BOTTOM, false, false);
	    titleLabel.setLayoutData(data);
    	
        searchGuiasText = new Text(grpGuias, SWT.BORDER);
        data = new GridData(SWT.RIGHT, SWT.FILL, false, false);
        data.widthHint = 150;
        searchGuiasText.setLayoutData(data);
        searchGuiasText.setMessage("Buscar guias...");        
        searchGuiasText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateGuiasTableFilter(searchGuiasText.getText().trim());
			}
		});
    	
    	tableGuiasViewer = new TableViewer(grpGuias, SWT.BORDER);
        Table tableGuias = createGuiasTable();
        tableGuiasViewer.setContentProvider(ArrayContentProvider.getInstance());
        tableGuiasViewer.setInput(guias); 
        data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        data.horizontalSpan = 2;
    	data.heightHint = 85;
    	tableGuias.setLayoutData(data); 
    		
    	// Creación de un botón para iniciar el proceso de creación de una reserva
    	Button btnCrearReserva = new Button(parent, SWT.PUSH);
    	btnCrearReserva.setText("Crear reserva");

    	// Configuración de diseño del botón en el diseño de cuadrícula
    	GridData data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    	data.horizontalSpan = 2;
    	data.widthHint = 150;
    	data.heightHint = 25;
    	btnCrearReserva.setLayoutData(data);

    	// Agregar un escuchador de selección al botón
    	btnCrearReserva.addSelectionListener(new SelectionAdapter() {
    	    @Override
    	    public void widgetSelected(SelectionEvent e) {
    	        // Verificar si un paquete ha sido seleccionado
    	        if(paqueteSeleccionado == null) {
    	            MessageDialog.openInformation(getShell(), "Crear reserva", "Debes seleccionar un paquete.");                		
    	        } 
    	        // Verificar si se ha seleccionado un guía
    	        else if (guiaSeleccionado == null) {
    	            MessageDialog.openInformation(getShell(), "Crear reserva", "Debes seleccionar un guía.");
    	        } 
    	        // Verificar si la cantidad de personas no supera el cupo máximo del paquete
    	        else if (Integer.parseInt(txtCantidadPersonas.getText()) > paqueteSeleccionado.getCupoMaximo()) {
    	            MessageDialog.openInformation(getShell(), "Crear reserva", "La cantidad de personas suministrada supera el cupo máximo del paquete.");
    	        } 
    	        // Proceder con la creación de la reserva si todas las condiciones son satisfactorias
    	        else {
    	            // Obtener las fechas disponibles para el paquete seleccionado
    	            List<LocalDate> fechasDisponibles = paqueteSeleccionado.getFechasDisponibles();  
    	            LocalDate selectedDate = LocalDate.of(dtFechaPlanificada.getYear(), dtFechaPlanificada.getMonth() + 1, dtFechaPlanificada.getDay());

    	            // Verificar si la fecha seleccionada está entre las fechas disponibles
    	            if (fechasDisponibles.contains(selectedDate)) {
    	                // Crear una reserva y mostrar un mensaje informativo
    	                String mensaje = agencia.crearReserva(fieldsToString());            	            		
    	                MessageDialog.openInformation(getShell(), "Crear reserva", mensaje);
    	            } 
    	            // Mostrar un mensaje si la fecha de reserva no coincide con las fechas disponibles del paquete
    	            else {
    	                MessageDialog.openInformation(getShell(), "Crear reserva", "La fecha de reserva no coincide con las fechas disponibles del paquete.");
    	            }                    
    	        }
    	    }
    	});

        
        separador = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
	    data.horizontalSpan = 2; 
	    data.verticalIndent = 25;
	    separador.setLayoutData(data);
        
        return parent;
    } 
    
    private Table createPaquetesTable() {
        Table table = tablePaquetesViewer.getTable();
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));

        // Columna ID
        TableViewerColumn idColumn = new TableViewerColumn(tablePaquetesViewer, SWT.NONE);
        idColumn.getColumn().setWidth(100);
        idColumn.getColumn().setText("ID");
        idColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((PaqueteTuristico) element).getId();
            }
        });

        // Columna Nombre
        TableViewerColumn nombreColumn = new TableViewerColumn(tablePaquetesViewer, SWT.NONE);
        nombreColumn.getColumn().setWidth(100);
        nombreColumn.getColumn().setText("Nombre");
        nombreColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((PaqueteTuristico) element).getNombre();
            }
        });

        // Columna Duracion
        TableViewerColumn duracionColumn = new TableViewerColumn(tablePaquetesViewer, SWT.NONE);
        duracionColumn.getColumn().setWidth(100);
        duracionColumn.getColumn().setText("Duración");
        duracionColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((PaqueteTuristico) element).getDuracion();
            }
        });

        // Columna Destinos
        TableViewerColumn destinosColumn = new TableViewerColumn(tablePaquetesViewer, SWT.NONE);
        destinosColumn.getColumn().setWidth(150);
        destinosColumn.getColumn().setText("Destinos");
        destinosColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return getDestinosAsString((PaqueteTuristico) element);
            }
        });

        // Columna Servicios Adicionales
        TableViewerColumn serviciosColumn = new TableViewerColumn(tablePaquetesViewer, SWT.NONE);
        serviciosColumn.getColumn().setWidth(150);
        serviciosColumn.getColumn().setText("Servicios Adicionales");
        serviciosColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return getServiciosAdicionalesAsString((PaqueteTuristico) element);
            }
        });

        // Columna Precio
        TableViewerColumn precioColumn = new TableViewerColumn(tablePaquetesViewer, SWT.NONE);
        precioColumn.getColumn().setWidth(100);
        precioColumn.getColumn().setText("Precio");
        precioColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return String.valueOf(((PaqueteTuristico) element).getPrecio());
            }
        });

        // Columna Cupo Máximo
        TableViewerColumn cupoColumn = new TableViewerColumn(tablePaquetesViewer, SWT.NONE);
        cupoColumn.getColumn().setWidth(100);
        cupoColumn.getColumn().setText("Cupo Máximo");
        cupoColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return String.valueOf(((PaqueteTuristico) element).getCupoMaximo());
            }
        });

        // Columna Fechas Disponibles
        TableViewerColumn fechasColumn = new TableViewerColumn(tablePaquetesViewer, SWT.NONE);
        fechasColumn.getColumn().setWidth(150);
        fechasColumn.getColumn().setText("Fechas Disponibles");
        fechasColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return getFechasDisponiblesAsString((PaqueteTuristico) element);
            }
        });
        
        table.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
            	paqueteSeleccionado = (PaqueteTuristico) table.getItem(table.getSelectionIndex()).getData();                        
            }
        });

        return table;
    }
    
    private String getDestinosAsString(PaqueteTuristico paquete) {
        List<String> destinos = paquete.getDestinos().stream()
                .map(Destino::getNombre)
                .collect(Collectors.toList());

        return String.join(", ", destinos);
    }

    private String getServiciosAdicionalesAsString(PaqueteTuristico paquete) {
        return String.join(", ", paquete.getServiciosAdicionales());
    }

    private String getFechasDisponiblesAsString(PaqueteTuristico paquete) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> fechas = paquete.getFechasDisponibles().stream()
                .map(date -> date.format(formatter))
                .collect(Collectors.toList());

        return String.join(", ", fechas);
    }

        
    private Table createGuiasTable() {
        Table table = tableGuiasViewer.getTable();
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));

        // Columna ID
        TableViewerColumn idColumn = new TableViewerColumn(tableGuiasViewer, SWT.NONE);
        idColumn.getColumn().setWidth(100);
        idColumn.getColumn().setText("ID");
        idColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((GuiaTuristico) element).getId();
            }
        });

        // Columna Identificación
        TableViewerColumn identificacionColumn = new TableViewerColumn(tableGuiasViewer, SWT.NONE);
        identificacionColumn.getColumn().setWidth(100);
        identificacionColumn.getColumn().setText("Identificación");
        identificacionColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((GuiaTuristico) element).getIdentificacion();
            }
        });

        // Columna Nombre Completo
        TableViewerColumn nombreColumn = new TableViewerColumn(tableGuiasViewer, SWT.NONE);
        nombreColumn.getColumn().setWidth(150);
        nombreColumn.getColumn().setText("Nombre Completo");
        nombreColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((GuiaTuristico) element).getNombreCompleto();
            }
        });

        // Columna Lenguajes de Comunicación
        TableViewerColumn lenguajesColumn = new TableViewerColumn(tableGuiasViewer, SWT.NONE);
        lenguajesColumn.getColumn().setWidth(150);
        lenguajesColumn.getColumn().setText("Lenguajes de Comunicación");
        lenguajesColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                List<String> lenguajes = ((GuiaTuristico) element).getLenguajesComunicacion();
                return String.join(", ", lenguajes);
            }
        });

        // Columna Experiencia Acumulada
        TableViewerColumn experienciaColumn = new TableViewerColumn(tableGuiasViewer, SWT.NONE);
        experienciaColumn.getColumn().setWidth(150);
        experienciaColumn.getColumn().setText("Experiencia Acumulada");
        experienciaColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((GuiaTuristico) element).getExperienciaAcumulada();
            }
        });

        // Columna Calificación
        TableViewerColumn calificacionColumn = new TableViewerColumn(tableGuiasViewer, SWT.NONE);
        calificacionColumn.getColumn().setWidth(100);
        calificacionColumn.getColumn().setText("Calificación");
        calificacionColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	double promedio = ((GuiaTuristico) element).getEstrellas().stream()
            		    .mapToDouble(Integer::doubleValue)
            		    .average()
            		    .orElse(0.0);
            	String resultadoFormateado = String.format("%.2f", promedio);
                return resultadoFormateado;
            }
        });

        // Columna Precio
        TableViewerColumn precioColumn = new TableViewerColumn(tableGuiasViewer, SWT.NONE);
        precioColumn.getColumn().setWidth(100);
        precioColumn.getColumn().setText("Precio");
        precioColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return Double.toString(((GuiaTuristico) element).getPrecio());
            }
        });
        
        table.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
            	guiaSeleccionado = (GuiaTuristico) table.getItem(table.getSelectionIndex()).getData();                        
            }
        });

        return table;
    }
    
    /**
     * Actualiza el filtro de la tabla de paquetes turísticos según la cadena de búsqueda.
     *
     * @param searchString Cadena de búsqueda para filtrar los paquetes turísticos.
     */
    private void updatePaquetesTableFilter(String searchString) {
        // Crear un filtro de visor personalizado para la tabla de paquetes turísticos
        ViewerFilter filter = new ViewerFilter() {
            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                // Verificar si el elemento es una instancia de PaqueteTuristico
                if (element instanceof PaqueteTuristico) {
                    PaqueteTuristico paquete = (PaqueteTuristico) element;
                    
                    // Aplicar filtro a los atributos relevantes del paquete turístico
                    return paquete.getId().toLowerCase().contains(searchString.toLowerCase()) ||
                            paquete.getNombre().toLowerCase().contains(searchString.toLowerCase()) ||
                            paquete.getDuracion().toLowerCase().contains(searchString.toLowerCase()) ||
                            paquete.getDestinos().stream().anyMatch(destino ->
                                    destino.getNombre().toLowerCase().contains(searchString.toLowerCase())) ||
                            String.join(", ", paquete.getServiciosAdicionales()).toLowerCase().contains(searchString.toLowerCase()) ||
                            Double.toString(paquete.getPrecio()).contains(searchString) ||
                            Integer.toString(paquete.getCupoMaximo()).contains(searchString) ||
                            paquete.getFechasDisponibles().stream().anyMatch(fecha ->
                                    fecha.toString().toLowerCase().contains(searchString.toLowerCase()));
                }
                // Si no es una instancia de PaqueteTuristico, mostrar el elemento
                return true;               
            }       
        };
        
        // Aplicar el filtro a la tabla de paquetes turísticos
        tablePaquetesViewer.setFilters(new ViewerFilter[] { filter });
    }

    /**
     * Actualiza el filtro de la tabla de guías turísticos según la cadena de búsqueda.
     *
     * @param searchString Cadena de búsqueda para filtrar los guías turísticos.
     */
    private void updateGuiasTableFilter(String searchString) {
        // Crear un filtro de visor personalizado para la tabla de guías turísticos
        ViewerFilter filter = new ViewerFilter() {
            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                // Verificar si el elemento es una instancia de GuiaTuristico
                if (element instanceof GuiaTuristico) {
                    GuiaTuristico guia = (GuiaTuristico) element;
                    
                    // Calcular el promedio de estrellas para el guía
                    double promedio = guia.getEstrellas().stream()
                            .mapToDouble(Integer::doubleValue)
                            .average()
                            .orElse(0.0);
                    // Formatear el resultado a dos decimales
                    String resultadoFormateado = String.format("%.2f", promedio);
                    
                    // Aplicar filtro a los atributos relevantes del guía turístico
                    return guia.getId().toLowerCase().contains(searchString.toLowerCase()) ||
                           guia.getIdentificacion().toLowerCase().contains(searchString.toLowerCase()) ||
                           guia.getNombreCompleto().toLowerCase().contains(searchString.toLowerCase()) ||
                           String.join(", ", guia.getLenguajesComunicacion()).toLowerCase().contains(searchString.toLowerCase()) ||
                           guia.getExperienciaAcumulada().toLowerCase().contains(searchString.toLowerCase()) ||
                           resultadoFormateado.contains(searchString) ||
                           Double.toString(guia.getPrecio()).contains(searchString);
                }
                // Si no es una instancia de GuiaTuristico, mostrar el elemento
                return true;
            }
        };

        // Aplicar el filtro a la tabla de guías turísticos
        tableGuiasViewer.setFilters(new ViewerFilter[] { filter });
    }


    private String fieldsToString() {       
        String id = "0";
        String fechaSolicitud = LocalDate.now().toString();  
        String fechaPlanificadaViaje = LocalDate.of(dtFechaPlanificada.getYear(), dtFechaPlanificada.getMonth() + 1, dtFechaPlanificada.getDay()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String cliente = clienteSession.getId();
        String cantidadPersonas = txtCantidadPersonas.getText();
        String paqueteTuristico = paqueteSeleccionado.getId();
        String guiaTuristico = guiaSeleccionado.getId();
        String estado = "PENDIENTE";
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id).append("@@");  
        stringBuilder.append(fechaSolicitud).append("@@"); 
        stringBuilder.append(fechaPlanificadaViaje).append("@@");  
        stringBuilder.append(cliente).append("@@");  
        stringBuilder.append(cantidadPersonas).append("@@"); 
        stringBuilder.append(paqueteTuristico).append("@@");  
        stringBuilder.append(guiaTuristico).append("@@");
        stringBuilder.append(estado);

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