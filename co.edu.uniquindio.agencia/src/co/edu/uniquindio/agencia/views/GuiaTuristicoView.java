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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuiaTuristicoView extends ApplicationWindow {
    private TableViewer tableViewer;
    private List<GuiaTuristico> guias;
    private ModelFactoryController modelFactoryController;
    private Agencia agencia;
    
    private GuiaTuristico guiaSeleccionado;
    private GridData data;
    
    private Map<String, String> accionesDeshacerMapa = new LinkedHashMap<>();
    private Map<String, String> accionesRehacerMapa = new LinkedHashMap<>();    
    
    Text txtIdentificacion;
    Text txtNombre;
    Text txtLenguajes;
    Text txtExperiencia;
    Text txtPrecio;
    Text txtCalificacion;
    
    public GuiaTuristicoView(Shell parentShell) {
        super(parentShell);
        modelFactoryController = ModelFactoryController.getInstance();
        agencia = modelFactoryController.getAgencia();
        guias = agencia.getListaGuiasTuristicos();
    }
    
    @Override
    protected Control createContents(Composite parent) {     
    	getShell().setText("Guías Turísticos");
        
    	parent.setLayout(new GridLayout(6, false));
    	fillBlank(parent, 5);
    	
    	Label titleLabel = new Label(parent, SWT.NONE);
	    titleLabel.setText("Guías Turísticos"); 
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

        Button btnDestino = new Button(grpTopButtons, SWT.NONE);
        btnDestino.setText("Destinos");
        btnDestino.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false, false));
        btnDestino.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	close();
            	DestinoView destinoView = new DestinoView(getShell());
            	destinoView.open();
            }
        });

        Button btnGrafico = new Button(grpTopButtons, SWT.NONE);
        btnGrafico.setText("Graficos");
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
        tableViewer.setInput(guias);
        GridData data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
    	table.setLayoutData(data);    	    	    	    	
    	
    	Group grpFields = new Group(parent, SWT.NONE);
    	grpFields.setText("Ingresa los datos:");
        data = new GridData(SWT.CENTER, SWT.NONE, false, false);
        data.horizontalSpan = 6;
        grpFields.setLayoutData(data);
        grpFields.setLayout(new GridLayout(6, false));
    	
    	Label lblIdentificacion = new Label(grpFields, SWT.NONE);
    	lblIdentificacion.setText("Identificación:");
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblIdentificacion.setLayoutData(data);
    	
    	txtIdentificacion = new Text(grpFields, SWT.BORDER);    	 	    	    	
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtIdentificacion.setLayoutData(data);
    	    	
    	Label lblNombre = new Label(grpFields, SWT.NONE);
    	lblNombre.setText("Nombre:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblNombre.setLayoutData(data);    	
    	
    	txtNombre = new Text(grpFields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtNombre.setLayoutData(data);    	
    	
    	Label lblLenguajes = new Label(grpFields, SWT.NONE);
    	lblLenguajes.setText("Lenguajes:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblLenguajes.setLayoutData(data);    	
    	
    	txtLenguajes = new Text(grpFields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtLenguajes.setLayoutData(data);

    	Label lblExperiencia = new Label(grpFields, SWT.NONE);
    	lblExperiencia.setText("Experiencia:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblExperiencia.setLayoutData(data);    	
    	
    	txtExperiencia = new Text(grpFields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtExperiencia.setLayoutData(data);

    	Label lblPrecio = new Label(grpFields, SWT.NONE);
    	lblPrecio.setText("Precio:");    	
    	data = new GridData(SWT.CENTER, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	lblPrecio.setLayoutData(data);    	
    	
    	txtPrecio = new Text(grpFields, SWT.BORDER);   
    	data = new GridData(SWT.LEFT, SWT.NONE, true, false);
    	data.verticalIndent = 20;
    	txtPrecio.setLayoutData(data);  
    	
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
            	String identificacion = fieldsToString().split("@@")[1];
            	guardarAccionDeshacer("eliminar-guia", identificacion);                            	
            	String mensaje = agencia.crearGuiaTuristico(fieldsToString());
            	guardarAccionRehacer("crear-guia", fieldsToString());
            	
            	MessageDialog.openInformation(getShell(), "Crear guía", mensaje);
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
            	guardarAccionDeshacer("actualizar-guia", guiaSeleccionado.convertirEnCadena());                            	            	
            	String mensaje = agencia.actualizarGuiaTuristico(guiaSeleccionado, fieldsToString());
            	guardarAccionRehacer("actualizar-guia", fieldsToString());

            	MessageDialog.openInformation(getShell(), "Editar guía", mensaje);
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
            	txtIdentificacion.setText("");
            	txtNombre.setText("");
            	txtLenguajes.setText("");
            	txtExperiencia.setText("");
            	txtPrecio.setText("");
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
                guardarAccionDeshacer("crear-guia", guiaSeleccionado.convertirEnCadena());            	            	
                String mensaje = agencia.eliminarGuiaTuristico(guiaSeleccionado.getIdentificacion());
                guardarAccionRehacer("eliminar-guia", guiaSeleccionado.getIdentificacion());
            	                
                MessageDialog.openInformation(getShell(), "Eliminar guía", mensaje);
                tableViewer.refresh();
            }
        });
                
        Label blank = fillBlank(parent, 1);
        blank.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 10));

        return parent;
    }
    
    private String fieldsToString() {
        String id = guiaSeleccionado.getId();        
    	String identificacion = txtIdentificacion.getText();
        String nombre=  txtNombre.getText();
        String lenguajesAdicionales = txtLenguajes.getText();     
        String experiencia = txtExperiencia.getText();
        String precio = txtPrecio.getText();
        String calificacion = guiaSeleccionado != null ? 
        		guiaSeleccionado.getEstrellas().stream().map(Object::toString).collect(Collectors.joining(",")) : ""; 
        String comentarios = guiaSeleccionado != null ? guiaSeleccionado.getComentarios().stream().collect(Collectors.joining(",")) : "";
        
        // Construir el formato de cadena
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id).append("@@");  
        stringBuilder.append(identificacion).append("@@"); 
        stringBuilder.append(nombre).append("@@");  
        stringBuilder.append(lenguajesAdicionales).append("@@");  
        stringBuilder.append(experiencia).append("@@"); 
        stringBuilder.append(precio).append("@@"); 
        stringBuilder.append(calificacion).append("@@"); 
        stringBuilder.append(comentarios);         

        return stringBuilder.toString();
    }


    private Table createTable() {
    	Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));

        // Columna ID
        TableViewerColumn idColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        idColumn.getColumn().setWidth(100);
        idColumn.getColumn().setText("ID");
        idColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((GuiaTuristico) element).getId();
            }
        });
        
        TableViewerColumn identificacionColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        identificacionColumn.getColumn().setWidth(100);
        identificacionColumn.getColumn().setText("Identificación");
        identificacionColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((GuiaTuristico) element).getIdentificacion();
            }
        });

        TableViewerColumn nombreColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        nombreColumn.getColumn().setWidth(100);
        nombreColumn.getColumn().setText("Nombre");
        nombreColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((GuiaTuristico) element).getNombreCompleto();
            }
        });
        
        TableViewerColumn lenguajesColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        lenguajesColumn.getColumn().setWidth(100);
        lenguajesColumn.getColumn().setText("Lenguajes");
        lenguajesColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return convertirLenguajesString((GuiaTuristico) element);
            }
        });

        TableViewerColumn experienciaColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        experienciaColumn.getColumn().setWidth(100);
        experienciaColumn.getColumn().setText("Experiencia");
        experienciaColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((GuiaTuristico) element).getExperienciaAcumulada();
            }
        });

        TableViewerColumn precioColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        precioColumn.getColumn().setWidth(100);
        precioColumn.getColumn().setText("Precio");
        precioColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return String.valueOf(((GuiaTuristico) element).getPrecio());
            }
        });
        
        TableViewerColumn calificacionColumn = new TableViewerColumn(tableViewer, SWT.NONE);
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
        
        table.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
        		guiaSeleccionado = (GuiaTuristico) table.getItem(table.getSelectionIndex()).getData();
        		String lenguajes = convertirLenguajesString(guiaSeleccionado);
        		
                txtIdentificacion.setText(guiaSeleccionado.getIdentificacion());
                txtNombre.setText(guiaSeleccionado.getNombreCompleto());
                txtLenguajes.setText(lenguajes);
                txtExperiencia.setText(String.valueOf(guiaSeleccionado.getExperienciaAcumulada()));
                txtPrecio.setText(String.valueOf(guiaSeleccionado.getPrecio()));                            
            }
        });
        
        return table;
    }
    
    private String convertirLenguajesString(GuiaTuristico guia) {
    	List<String> lenguajes = guia.getLenguajesComunicacion();
        return String.join(",", lenguajes);
    }
    
    private Label fillBlank(Composite parent, int times) {
    	Label blank = null;
    	for (int i = 0; i < times; i++) {
    		blank = new Label(parent, SWT.NONE);
            blank.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
    	}
    	
    	return blank;
    }
    
 // Método para guardar una acción en el mapa de acciones a deshacer
    private void guardarAccionDeshacer(String accion, String informacionAccion) {
        accionesDeshacerMapa.put(accion, informacionAccion);
    }

    // Método para guardar una acción en el mapa de acciones a rehacer
    private void guardarAccionRehacer(String accion, String informacionAccion) {
        accionesRehacerMapa.put(accion, informacionAccion);        
    }

    // Método para deshacer la última acción realizada
    private void deshacer() {
        if (!accionesDeshacerMapa.isEmpty()) {
            // Obtener la última acción realizada y su información
            Map.Entry<String, String> ultimaAccion = accionesDeshacerMapa.entrySet().iterator().next();
            String accion = ultimaAccion.getKey();
            String informacionAccion = ultimaAccion.getValue();

            // Eliminar la acción deshecha del mapa
            accionesDeshacerMapa.remove(accion);

            // Llamar al método para almacenar la acción (realizar la acción inversa)
            almacenarAccion(accion, informacionAccion);            

            // Mostrar un mensaje de éxito
            MessageDialog.openInformation(getShell(), "Deshacer", "Acción retornada con éxito.");
        } else {
            // Mostrar un mensaje indicando que no hay acciones para deshacer
            MessageDialog.openInformation(getShell(), "Deshacer", "No hay acciones para deshacer.");                
        }
    }

    // Método para rehacer la última acción deshecha
    private void rehacer() {
        if (!accionesRehacerMapa.isEmpty()) {
            // Obtener la última acción deshecha y su información
            Map.Entry<String, String> ultimaAccion = accionesRehacerMapa.entrySet().iterator().next();
            String accion = ultimaAccion.getKey();
            String informacionAccion = ultimaAccion.getValue();

            // Eliminar la acción a rehacer del mapa
            accionesRehacerMapa.remove(accion);

            // Llamar al método para almacenar la acción (realizar la acción inversa)
            almacenarAccion(accion, informacionAccion);
            
            // Mostrar un mensaje de éxito
            MessageDialog.openInformation(getShell(), "Rehacer", "Acción retornada con éxito.");
        } else {
            // Mostrar un mensaje indicando que no hay acciones para rehacer
            MessageDialog.openInformation(getShell(), "Rehacer", "No hay acciones para rehacer.");                
        }
    }

    // Método para almacenar una acción, realizando la acción inversa según el tipo de acción
    private void almacenarAccion(String accion, String informacionAccion) {
        switch(accion) {
            case "crear-guia":
                agencia.crearGuiaTuristico(informacionAccion);
                break;
            case "actualizar-guia":
                agencia.actualizarGuiaTuristico(null, informacionAccion);
                break;
            case "eliminar-guia":
                agencia.eliminarGuiaTuristico(informacionAccion);
                break;
            // Agregar más casos según sea necesario para otros tipos de acciones
        }
    }

}