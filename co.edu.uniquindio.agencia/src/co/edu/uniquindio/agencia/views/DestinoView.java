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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DestinoView extends ApplicationWindow {
    private TableViewer tableViewer;
    private List<Destino> destinos;
    private ModelFactoryController modelFactoryController;
    private Agencia agencia;
    
    private Destino destinoSeleccionado;
    private GridData data;
    
    private Map<String, String> accionesDeshacerMapa = new LinkedHashMap<>();
    private Map<String, String> accionesRehacerMapa = new LinkedHashMap<>();
    
    Text txtNombre;
    Text txtCiudad;
    Text txtDescripción;
    Text txtImagenes;
    Text txtClima;
    Text txtCantidadBusquedas;   
    
    public DestinoView(Shell parentShell) {
        super(parentShell);
        modelFactoryController = ModelFactoryController.getInstance();
        agencia = modelFactoryController.getAgencia();
        destinos = agencia.getListaDestinos();
    }
    
    @Override
    protected Control createContents(Composite parent) {  
    	getShell().setText("Destinos");        
    	    	
    	parent.setLayout(new GridLayout(6, false));
    	fillBlank(parent, 5);
    	
    	Label titleLabel = new Label(parent, SWT.NONE);
	    titleLabel.setText("Destinos"); 
	    titleLabel.setFont(new Font(parent.getDisplay(), "Arial", 20, SWT.BOLD));
        data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
	    data.horizontalSpan = 6; 
	    titleLabel.setLayoutData(data);
	    
	    Group grpTopButtons = new Group(parent, SWT.NONE);
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalSpan = 6;
        grpTopButtons.setLayoutData(data);
        grpTopButtons.setLayout(new GridLayout(4, false));
    	
    	Button btnPaquete = new Button(grpTopButtons, SWT.NONE);
    	btnPaquete.setText("Paquetes Turisticos");
    	btnPaquete.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false, false));
    	btnPaquete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	close();
            	PaqueteTuristicoView paqueteView = new PaqueteTuristicoView(getShell());
            	paqueteView.open();
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
        grpTables.setText("Tabla de guías turísticos:");
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalSpan = 6;
        grpTables.setLayoutData(data);
        grpTables.setLayout(new GridLayout(1, false));
        
    	tableViewer = new TableViewer(grpTables, SWT.BORDER);
        Table table = createTable();
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());
        tableViewer.setInput(destinos); // Establece la lista como fuente de datos
        data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
    	data.horizontalSpan = 6;
    	data.verticalIndent = 10;
    	table.setLayoutData(data);    	    	

    	Group grpFields = new Group(parent, SWT.NONE);
    	grpFields.setText("Tabla de destinos:");
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalSpan = 6;
        grpFields.setLayoutData(data);
        grpFields.setLayout(new GridLayout(6, false));
    	
    	Label lblNombre = new Label(grpFields, SWT.NONE);
        lblNombre.setText("Nombre:");
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblNombre.setLayoutData(data);
    	
    	txtNombre = new Text(grpFields, SWT.BORDER);    	 	    	    	
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtNombre.setLayoutData(data);
    	    	
    	Label lblCiudad = new Label(grpFields, SWT.NONE);
    	lblCiudad.setText("Duración:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblCiudad.setLayoutData(data);    	
    	
    	txtCiudad = new Text(grpFields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtCiudad.setLayoutData(data);    	
    	
    	Label lblDescripcion = new Label(grpFields, SWT.NONE);
    	lblDescripcion.setText("Precio:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblDescripcion.setLayoutData(data);    	
    	
    	txtDescripción = new Text(grpFields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtDescripción.setLayoutData(data);    	
    	    	
    	Label lblImagenes = new Label(grpFields, SWT.NONE);
    	lblImagenes.setText("Imágenes:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblImagenes.setLayoutData(data);    	
    	
    	txtImagenes = new Text(grpFields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtImagenes.setLayoutData(data);    	    	    	
    	
    	Label lblClima = new Label(grpFields, SWT.NONE);
    	lblClima.setText("Clima:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblClima.setLayoutData(data);    	
    	
    	txtClima = new Text(grpFields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtClima.setLayoutData(data);    	
    	    	
    	Label lblCantidadBusquedas = new Label(grpFields, SWT.NONE);
    	lblCantidadBusquedas.setText("Cantidad de búsquedas:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblCantidadBusquedas.setLayoutData(data);    	
    	
    	txtCantidadBusquedas = new Text(grpFields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtCantidadBusquedas.setLayoutData(data);    	
    	  	
    	fillBlank(parent, 2);
    	
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
            	guardarAccionDeshacer("eliminar-destino", nombre);                 	
            	String mensaje = agencia.crearDestino(fieldsToString());
            	guardarAccionRehacer("crear-destino", fieldsToString());
            	
            	MessageDialog.openInformation(getShell(), "Crear destino", mensaje);
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
            	guardarAccionDeshacer("actualizar-destino", destinoSeleccionado.convertirEnCadena());                            	            	
            	String mensaje = agencia.actualizarDestino(destinoSeleccionado, fieldsToString());
            	guardarAccionRehacer("actualizar-destino", fieldsToString());
            	
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
            	txtCiudad.setText("");
            	txtImagenes.setText("");
            	txtDescripción.setText("");
            	txtClima.setText("");
            	txtCantidadBusquedas.setText("");            	
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
            	guardarAccionDeshacer("crear-destino", destinoSeleccionado.convertirEnCadena());            	            	
            	String mensaje = agencia.eliminarDestino(destinoSeleccionado.getNombre());
                guardarAccionRehacer("eliminar-destino", destinoSeleccionado.getNombre());

            	MessageDialog.openInformation(getShell(), "Eliminar paquete", mensaje);
                tableViewer.refresh();
            }
        });
        
        Label blancoLabel = new Label(parent, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        data.horizontalSpan = 6;
        data.verticalIndent = 10;
        blancoLabel.setLayoutData(data);
        
        return parent;
    }
    
    private String fieldsToString() {
        String nombre = txtNombre.getText();
        String ciudad = txtCiudad.getText();
        String descripcion = txtDescripción.getText();
        String clima = txtClima.getText();
        String cantidadBusquedas = txtCantidadBusquedas.getText();
        
        String id = destinoSeleccionado.getId();     
        String imagenesRepresentativas = destinoSeleccionado.getImagenesRepresentativas().stream().collect(Collectors.joining(","));  
        String estrellas = destinoSeleccionado.getEstrellas().stream().map(Object::toString).collect(Collectors.joining(","));        
        String comentarios = destinoSeleccionado.getComentarios().stream().collect(Collectors.joining(","));  
        
        // Construir el formato de cadena
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id).append("@@");  
        stringBuilder.append(nombre).append("@@"); 
        stringBuilder.append(ciudad).append("@@");  
        stringBuilder.append(descripcion).append("@@");  
        stringBuilder.append(imagenesRepresentativas).append("@@"); 
        stringBuilder.append(clima).append("@@");  
        stringBuilder.append(cantidadBusquedas).append("@@"); 
        stringBuilder.append(estrellas).append("@@");
        stringBuilder.append(comentarios);  

        return stringBuilder.toString();
    }


    private Table createTable() {
    	Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));

        TableViewerColumn idColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        idColumn.getColumn().setWidth(100);
        idColumn.getColumn().setText("ID");
        idColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((Destino) element).getId();
            }
        });

        TableViewerColumn nombreColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        nombreColumn.getColumn().setWidth(100);
        nombreColumn.getColumn().setText("Nombre");
        nombreColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((Destino) element).getNombre();
            }
        });

        TableViewerColumn ciudadColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        ciudadColumn.getColumn().setWidth(100);
        ciudadColumn.getColumn().setText("Ciudad");
        ciudadColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((Destino) element).getCiudad();
            }
        });

        TableViewerColumn descripcionColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        descripcionColumn.getColumn().setWidth(100);
        descripcionColumn.getColumn().setText("Descripcion");
        descripcionColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return String.valueOf(((Destino) element).getDescripcion());
            }
        });
        
        TableViewerColumn imagenesColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        imagenesColumn.getColumn().setWidth(100);
        imagenesColumn.getColumn().setText("Imagenes");
        imagenesColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return convertirImagenesString((Destino) element);
            }
        });

        TableViewerColumn climaColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        climaColumn.getColumn().setWidth(100);
        climaColumn.getColumn().setText("Clima");
        climaColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return String.valueOf(((Destino) element).getClima());
            }
        });
        
        TableViewerColumn busquedasColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        busquedasColumn.getColumn().setWidth(100);
        busquedasColumn.getColumn().setText("Cantidad de Busquedas");
        busquedasColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return String.valueOf(((Destino) element).getCantidadBusquedas());
            }
        });
        
        TableViewerColumn estrellasColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        estrellasColumn.getColumn().setWidth(100);
        estrellasColumn.getColumn().setText("Calificación");
        estrellasColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	double promedio = ((Destino) element).getEstrellas().stream()
            		    .mapToDouble(Integer::doubleValue)
            		    .average()
            		    .orElse(0.0);
            	String resultadoFormateado = String.format("%.2f", promedio);
                return resultadoFormateado;
            }
        });
        
        table.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
            	destinoSeleccionado = (Destino) table.getItem(table.getSelectionIndex()).getData();
            	String imagenes = convertirImagenesString(destinoSeleccionado);
            	
        	    txtNombre.setText(destinoSeleccionado.getNombre());
        	    txtCiudad.setText(destinoSeleccionado.getCiudad());
        	    txtDescripción.setText(String.valueOf(destinoSeleccionado.getDescripcion()));
        	    txtImagenes.setText(imagenes);
        	    txtClima.setText(String.valueOf(destinoSeleccionado.getClima()));            
        	    txtCantidadBusquedas.setText(String.valueOf(destinoSeleccionado.getCantidadBusquedas()));                            
            }
        });
        
        return table;
    }
    
    private String convertirImagenesString(Destino destino) {
    	List<String> imagenes = destino.getImagenesRepresentativas();
        return String.join(",", imagenes);
    }
    
    private Label fillBlank(Composite parent, int times) {
    	Label blank = null;
    	for (int i = 0; i < times; i++) {
    		blank = new Label(parent, SWT.NONE);
            blank.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
    	}
    	
    	return blank;
    }
    
    public void guardarAccionDeshacer(String accion, String informacionAccion) {
        // Método para almacenar una acción en el mapa de acciones para deshacer.
        accionesDeshacerMapa.put(accion, informacionAccion);
    }

    public void guardarAccionRehacer(String accion, String informacionAccion) {
        // Método para almacenar una acción en el mapa de acciones para rehacer.
        accionesRehacerMapa.put(accion, informacionAccion);
    }

    public void deshacer() {
        if (!accionesDeshacerMapa.isEmpty()) {
            // Obtiene la última acción realizada y su información asociada.
            Map.Entry<String, String> ultimaAccion = accionesDeshacerMapa.entrySet().iterator().next();
            String accion = ultimaAccion.getKey();
            String informacionAccion = ultimaAccion.getValue();

            // Elimina la última acción del mapa de acciones para deshacer.
            accionesDeshacerMapa.remove(accion);

            // Llama al método para almacenar la acción y su información.
            almacenarAccion(accion, informacionAccion);

            // Muestra un mensaje informativo de éxito.
            MessageDialog.openInformation(getShell(), "Deshacer", "Acción retornada con éxito.");
        } else {
            // Muestra un mensaje informativo indicando que no hay acciones para deshacer.
            MessageDialog.openInformation(getShell(), "Deshacer", "No hay acciones para deshacer.");
        }
    }

    public void rehacer() {
        if (!accionesRehacerMapa.isEmpty()) {
            // Obtiene la última acción deshecha y su información asociada.
            Map.Entry<String, String> ultimaAccion = accionesRehacerMapa.entrySet().iterator().next();
            String accion = ultimaAccion.getKey();
            String informacionAccion = ultimaAccion.getValue();

            // Elimina la última acción del mapa de acciones para rehacer.
            accionesRehacerMapa.remove(accion);

            // Llama al método para almacenar la acción y su información.
            almacenarAccion(accion, informacionAccion);

            // Muestra un mensaje informativo de éxito.
            MessageDialog.openInformation(getShell(), "Rehacer", "Acción retornada con éxito.");
        } else {
            // Muestra un mensaje informativo indicando que no hay acciones para rehacer.
            MessageDialog.openInformation(getShell(), "Rehacer", "No hay acciones para rehacer.");
        }
    }

    private void almacenarAccion(String accion, String informacionAccion) {
        // Método para determinar qué acción realizar en función del tipo de acción proporcionada.
        switch(accion) {
            case "crear-destino":
                // Llama al método para crear un destino en la agencia.
                agencia.crearDestino(informacionAccion);
                break;
            case "actualizar-destino":
                // Llama al método para actualizar un destino en la agencia.
                agencia.actualizarDestino(null, informacionAccion);
                break;
            case "eliminar-destino":
                // Llama al método para eliminar un destino en la agencia.
                agencia.eliminarDestino(informacionAccion);
                break;
        }
    }

}