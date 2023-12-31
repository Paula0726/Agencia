package co.edu.uniquindio.agencia.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;
import co.edu.uniquindio.agencia.model.Destino;
import co.edu.uniquindio.agencia.model.GuiaTuristico;
import co.edu.uniquindio.agencia.model.PaqueteTuristico;
import co.edu.uniquindio.implementation.Agencia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PaqueteTuristicoView extends ApplicationWindow {
    private TableViewer tableViewer;
    private List<PaqueteTuristico> paquetes;
    private ModelFactoryController modelFactoryController;
    private Agencia agencia;
    
    private PaqueteTuristico paqueteSeleccionado;
    private GridData data;
    
    private Map<String, String> accionesDeshacerMapa = new LinkedHashMap<>();
    private Map<String, String> accionesRehacerMapa = new LinkedHashMap<>();
    
    Text txtNombre;
    Text txtDuracion;
    Text txtDestinos;
    Text txtServicios;
    Text txtPrecio;
    Text txtCupoMaximo;
    Text txtFechas;
    
    public PaqueteTuristicoView(Shell parentShell) {
        super(parentShell);
        modelFactoryController = ModelFactoryController.getInstance();
        agencia = modelFactoryController.getAgencia();
        paquetes = agencia.getListaPaquetesTuristicos();
    }
    

    @Override
    protected Control createContents(Composite parent) {   
    	getShell().setText("Paquetes Turísticos");        
    	
    	parent.setLayout(new GridLayout(6, false));
    	fillBlank(parent, 5);
    	
    	Label titleLabel = new Label(parent, SWT.NONE);
	    titleLabel.setText("Paquetes Turísticos"); 
	    titleLabel.setFont(new Font(parent.getDisplay(), "Arial", 20, SWT.BOLD));
        data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
	    data.horizontalSpan = 6; 
	    titleLabel.setLayoutData(data);
    	
	    Group grpTopButtons = new Group(parent, SWT.NONE);
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalSpan = 6;
        grpTopButtons.setLayoutData(data);
        grpTopButtons.setLayout(new GridLayout(4, false));
    	
    	Button btnDestino = new Button(grpTopButtons, SWT.NONE);
        btnDestino.setText("Destino");
        btnDestino.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false, false));
        btnDestino.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	close();
            	DestinoView destinoView = new DestinoView(getShell());
                destinoView.open();
            }
        });

        Button btnGuia = new Button(grpTopButtons, SWT.NONE);
        btnGuia.setText("Guías");
        btnGuia.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false, false));
        btnGuia.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	close();
            	GuiaTuristicoView guiaView = new GuiaTuristicoView(getShell());
            	guiaView.open();
            }
        });

        Button btnGrafico = new Button(grpTopButtons, SWT.NONE);
        btnGrafico.setText("Gráficos");
        btnGrafico.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false, false));
        btnGrafico.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	close();
            	GraficosView graficoView = new GraficosView(getShell());
            	graficoView.open();
            }
        });

        Button btnCerrar = new Button(grpTopButtons, SWT.NONE);
        btnCerrar.setText("Cerrar");
        btnCerrar.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false, false));
        btnCerrar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                close(); 
                StarterView view = new StarterView(getShell());
                view.open();
            }
        });       
    	
        Group grpTables = new Group(parent, SWT.NONE);
        grpTables.setText("Tabla de paquetes turísticos:");
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalSpan = 6;
        grpTables.setLayoutData(data);
        grpTables.setLayout(new GridLayout(1, false));
        
    	tableViewer = new TableViewer(grpTables, SWT.BORDER);
        Table table = createTable();
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());
        tableViewer.setInput(paquetes);
        data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
    	table.setLayoutData(data);    	    	
    	
    	Group grpfields = new Group(parent, SWT.NONE);
    	grpfields.setText("Ingresa los datos:");
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalSpan = 6;
        grpfields.setLayoutData(data);
        grpfields.setLayout(new GridLayout(8, false));
    	
    	Label lblNombre = new Label(grpfields, SWT.NONE);
        lblNombre.setText("Nombre:");
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblNombre.setLayoutData(data);
    	
    	txtNombre = new Text(grpfields, SWT.BORDER);    	 	    	    	
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtNombre.setLayoutData(data);
    	    	
    	Label lblDuracion = new Label(grpfields, SWT.NONE);
        lblDuracion.setText("Duración:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblDuracion.setLayoutData(data);    	
    	
    	txtDuracion = new Text(grpfields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtDuracion.setLayoutData(data); 
    	
    	Label lblDestinos = new Label(grpfields, SWT.NONE);
    	lblDestinos.setText("Destinos:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblDestinos.setLayoutData(data);    	
    	
    	txtDestinos = new Text(grpfields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtDestinos.setLayoutData(data);   
    	
    	Label lblServicios = new Label(grpfields, SWT.NONE);
    	lblServicios.setText("Servicios:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblServicios.setLayoutData(data);    	
    	
    	txtServicios = new Text(grpfields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtServicios.setLayoutData(data);
    	
    	Label lblPrecio = new Label(grpfields, SWT.NONE);
    	lblPrecio.setText("Precio:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblPrecio.setLayoutData(data);    	
    	
    	txtPrecio = new Text(grpfields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtPrecio.setLayoutData(data);    	
    	    	
    	Label lblCupoMaximo = new Label(grpfields, SWT.NONE);
    	lblCupoMaximo.setText("Cupo Maximo:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblCupoMaximo.setLayoutData(data);    	
    	
    	txtCupoMaximo = new Text(grpfields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtCupoMaximo.setLayoutData(data);    	
    	    
    	Label lblFechas = new Label(grpfields, SWT.NONE);
    	lblFechas.setText("Fechas:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblFechas.setLayoutData(data);    	
    	
    	txtFechas = new Text(grpfields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtFechas.setLayoutData(data);
    	    	
    	Group grpBottomButtons = new Group(parent, SWT.NONE);
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalSpan = 6;
        data.verticalIndent = 20;
        grpBottomButtons.setLayoutData(data);
        grpBottomButtons.setLayout(new GridLayout(6, false));
        
        Button btnAgregar = new Button(grpBottomButtons, SWT.NONE);
        btnAgregar.setText("Agregar");        
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalIndent = 5;
        btnAgregar.setLayoutData(data);
        btnAgregar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	String nombre = fieldsToString().split("@@")[1];
            	guardarAccionDeshacer("eliminar-paquete", nombre);                            	
            	String mensaje = agencia.crearPaqueteTuristico(fieldsToString());
            	guardarAccionRehacer("crear-paquete", fieldsToString());
            	
            	MessageDialog.openInformation(getShell(), "Crear paquete", mensaje);
            	tableViewer.refresh();
            }
        });
        
        Button btnEditar = new Button(grpBottomButtons, SWT.NONE);
        btnEditar.setText("Editar");        
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalIndent = 5;
        btnEditar.setLayoutData(data);
        btnEditar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	guardarAccionDeshacer("actualizar-paquete", paqueteSeleccionado.convertirEnCadena());                            	
            	String mensaje = agencia.actualizarPaqueteTuristico(paqueteSeleccionado, fieldsToString());
            	guardarAccionRehacer("actualizar-paquete", fieldsToString());
            	            	
            	MessageDialog.openInformation(getShell(), "Editar paquete", mensaje);
            	tableViewer.refresh();
            }
        });                
        
        Button btnLimpiar = new Button(grpBottomButtons, SWT.NONE);
        btnLimpiar.setText("Limpiar");        
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalIndent = 5;
        btnLimpiar.setLayoutData(data);
        btnLimpiar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	txtNombre.setText("");
                txtDuracion.setText("");
                txtDestinos.setText("");
        		txtServicios.setText("");
                txtPrecio.setText("");
                txtCupoMaximo.setText("");
        		txtFechas.setText("");
            }
        });
        
        Button btnDeshacer = new Button(grpBottomButtons, SWT.NONE);
        btnDeshacer.setText("Deshacer");        
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalIndent = 5;
        btnDeshacer.setLayoutData(data);
        btnDeshacer.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                deshacer();
            	tableViewer.refresh();
            }
        });
        
        Button btnRehacer = new Button(grpBottomButtons, SWT.NONE);
        btnRehacer.setText("Rehacer");        
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalIndent = 5;
        btnRehacer.setLayoutData(data);
        btnRehacer.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                rehacer();
            	tableViewer.refresh();
            }
        });
        
        Button btnEliminar = new Button(grpBottomButtons, SWT.NONE);
        btnEliminar.setText("Eliminar");        
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalIndent = 5;
        btnEliminar.setLayoutData(data);
        btnEliminar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                guardarAccionDeshacer("crear-paquete", paqueteSeleccionado.convertirEnCadena());
            	String mensaje = agencia.eliminarPaqueteTuristico(paqueteSeleccionado.getNombre());
                guardarAccionRehacer("eliminar-paquete", paqueteSeleccionado.getNombre());
            	
                MessageDialog.openInformation(getShell(), "Eliminar paquete", mensaje);                
                tableViewer.refresh();
            }
        });
                
        Label blank = fillBlank(parent, 1);
        blank.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 10));

        return parent;
    }
    
    private String fieldsToString() {
        String id = paqueteSeleccionado != null ? paqueteSeleccionado.getId() : "0";
    	String nombre = txtNombre.getText();
        String duracion = txtDuracion.getText();
        String destinos = txtDestinos.getText();
		String servicios = txtServicios.getText();
        String precio = txtPrecio.getText();
        String cupoMaximo = txtCupoMaximo.getText();
		String fechas = txtFechas.getText();
                
        // Construir el formato de cadena
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id).append("@@");  
        stringBuilder.append(nombre).append("@@"); 
        stringBuilder.append(duracion).append("@@");  
        stringBuilder.append(destinos).append("@@");  
        stringBuilder.append(servicios).append("@@"); 
        stringBuilder.append(precio).append("@@");  
        stringBuilder.append(cupoMaximo).append("@@"); 
        stringBuilder.append(fechas);  

        return stringBuilder.toString();
    }


    private Table createTable() {
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
                return ((PaqueteTuristico) element).getId();
            }
        });

        // Columna Nombre
        TableViewerColumn nombreColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        nombreColumn.getColumn().setWidth(100);
        nombreColumn.getColumn().setText("Nombre");
        nombreColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((PaqueteTuristico) element).getNombre();
            }
        });

        // Columna Duración
        TableViewerColumn duracionColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        duracionColumn.getColumn().setWidth(80);
        duracionColumn.getColumn().setText("Duración");
        duracionColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((PaqueteTuristico) element).getDuracion();
            }
        });
        
        TableViewerColumn destinosColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        destinosColumn.getColumn().setWidth(70);
        destinosColumn.getColumn().setText("Destinos");
        destinosColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	return convertirDestinosString(((PaqueteTuristico) element));
            }
        });
        
        TableViewerColumn serviciosAdicionalesColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        serviciosAdicionalesColumn.getColumn().setWidth(130);
        serviciosAdicionalesColumn.getColumn().setText("Servicios adicionales");
        serviciosAdicionalesColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	return convertirServiciosString(((PaqueteTuristico) element));
            }
        });

        // Columna Precio
        TableViewerColumn precioColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        precioColumn.getColumn().setWidth(80);
        precioColumn.getColumn().setText("Precio");
        precioColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return String.valueOf(((PaqueteTuristico) element).getPrecio());
            }
        });
        
        // Columna Cupo Máximo
        TableViewerColumn cupoMaximoColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        cupoMaximoColumn.getColumn().setWidth(100);
        cupoMaximoColumn.getColumn().setText("Cupo Máximo");
        cupoMaximoColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return String.valueOf(((PaqueteTuristico) element).getCupoMaximo());
            }
        });

        TableViewerColumn fechasDisponiblesColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        fechasDisponiblesColumn.getColumn().setWidth(100);
        fechasDisponiblesColumn.getColumn().setText("Fechas disponibles");
        fechasDisponiblesColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return convertirFechasString(((PaqueteTuristico) element));
            }
        });                
        
        table.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
        		paqueteSeleccionado = (PaqueteTuristico) table.getItem(table.getSelectionIndex()).getData();
        		String destinos = convertirDestinosString(paqueteSeleccionado);
        		String servicios = convertirServiciosString(paqueteSeleccionado); 
        		String fechas = convertirFechasString(paqueteSeleccionado);
        		
                txtNombre.setText(paqueteSeleccionado.getNombre());
                txtDuracion.setText(paqueteSeleccionado.getDuracion());
                txtDestinos.setText(destinos);
                txtServicios.setText(servicios);
                txtPrecio.setText(String.valueOf(paqueteSeleccionado.getPrecio()));
                txtCupoMaximo.setText(String.valueOf(paqueteSeleccionado.getCupoMaximo()));            
                txtFechas.setText(fechas);
            }
        });
        
        return table;
    }
    
    private String convertirDestinosString(PaqueteTuristico paquete) {
    	List<String> destinos = paquete.getDestinos().stream()
                .map(Destino::getId)
                .collect(Collectors.toList());

        return String.join(",", destinos); 
    }
    
    private String convertirServiciosString(PaqueteTuristico paquete) {
    	List<String> servicios = paquete.getServiciosAdicionales();
        return String.join(",", servicios);
    }
    
    private String convertirFechasString(PaqueteTuristico paquete) {	
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<String> fechas = paquete.getFechasDisponibles().stream()
		    .map(date -> date.format(formatter))
		    .collect(Collectors.toList()); 

		return String.join(",", fechas);
    }
    
    private Label fillBlank(Composite parent, int times) {
    	Label blank = null;
    	for (int i = 0; i < times; i++) {
    		blank = new Label(parent, SWT.NONE);
            blank.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
    	}
    	
    	return blank;
    }
    
 // Método para guardar una acción en el mapa de acciones a deshacer.
    private void guardarAccionDeshacer(String accion, String informacionAccion) {
        accionesDeshacerMapa.put(accion, informacionAccion);
    }

    // Método para guardar una acción en el mapa de acciones a rehacer.
    private void guardarAccionRehacer(String accion, String informacionAccion) {
        accionesRehacerMapa.put(accion, informacionAccion);        
    }

    // Método para deshacer la última acción realizada.
    private void deshacer() {
        if (!accionesDeshacerMapa.isEmpty()) {
            // Obtener la última acción del mapa de acciones a deshacer.
            Map.Entry<String, String> ultimaAccion = accionesDeshacerMapa.entrySet().iterator().next();
            String accion = ultimaAccion.getKey();
            String informacionAccion = ultimaAccion.getValue();

            // Eliminar la acción deshecha del mapa y almacenarla.
            accionesDeshacerMapa.remove(accion);
            almacenarAccion(accion, informacionAccion);            

            // Mostrar un mensaje informativo.
            MessageDialog.openInformation(getShell(), "Deshacer", "Acción retornada con éxito.");
        } else {
            // Mostrar un mensaje informativo si no hay acciones para deshacer.
            MessageDialog.openInformation(getShell(), "Deshacer", "No hay acciones para deshacer.");                
        }
    }

    // Método para rehacer la última acción deshecha.
    private void rehacer() {
        if (!accionesRehacerMapa.isEmpty()) {
            // Obtener la última acción del mapa de acciones a rehacer.
            Map.Entry<String, String> ultimaAccion = accionesRehacerMapa.entrySet().iterator().next();
            String accion = ultimaAccion.getKey();
            String informacionAccion = ultimaAccion.getValue();

            // Eliminar la acción rehecha del mapa y almacenarla.
            accionesRehacerMapa.remove(accion);
            almacenarAccion(accion, informacionAccion);            

            // Mostrar un mensaje informativo.
            MessageDialog.openInformation(getShell(), "Rehacer", "Acción retornada con éxito.");
        } else {
            // Mostrar un mensaje informativo si no hay acciones para rehacer.
            MessageDialog.openInformation(getShell(), "Rehacer", "No hay acciones para rehacer.");                
        }
    }

    // Método para almacenar la acción realizada en función de su tipo.
    private void almacenarAccion(String accion, String informacionAccion) {
        switch(accion) {
            case "crear-paquete":
                agencia.crearPaqueteTuristico(informacionAccion);
                break;
            case "actualizar-paquete":
                agencia.actualizarPaqueteTuristico(null, informacionAccion);
                break;
            case "eliminar-paquete":
                agencia.eliminarPaqueteTuristico(informacionAccion);
                break;
        }
    }

}