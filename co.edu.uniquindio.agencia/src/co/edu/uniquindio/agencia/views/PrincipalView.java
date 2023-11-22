package co.edu.uniquindio.agencia.views;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Destino;
import co.edu.uniquindio.agencia.model.GuiaTuristico;
import co.edu.uniquindio.implementation.Agencia;

public class PrincipalView extends ApplicationWindow {
	private static final String PATH_IMAGENES = "archivos/imagenes/";
	
	private ModelFactoryController modelFactoryController;
    private Agencia agencia;
    private Cliente clienteSession;
	
    List<Destino> destinosRecomendados;
    int index_destino;
    
    List<GuiaTuristico> guiasTuristicos;
    int index_guia;
    
    GridData data;
    
    Label puntuacionLabel;
    Button comentariosButton;
    Label imagenLabel;
    Label destinosRecomendadosLabel;
    
    Label guiasRecomendadosLabel;    		
	Label puntuacionGuiasLabel;
    Button comentariosGuiasButton;
    
    public PrincipalView(Shell parentShell) {
        super(parentShell);
        modelFactoryController = ModelFactoryController.getInstance();
        agencia = modelFactoryController.getAgencia();        
        destinosRecomendados = agencia.recomendarDestinos(agencia.getClienteSession());
        guiasTuristicos = agencia.getListaGuiasTuristicos();
        clienteSession = agencia.getClienteSession();
    }

    @Override
    protected Control createContents(Composite parent) { 
    	getShell().setText("Agencia");        
    	
    	parent.setLayout(new GridLayout(3, false));
    	fillBlank(parent, 2);
    	
    	Composite labelTitleComposite = new Composite(parent, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        data.horizontalSpan = 3;        
        labelTitleComposite.setLayoutData(data);
        labelTitleComposite.setLayout(new GridLayout(1, false)); 
        labelTitleComposite.setBackground(new Color(parent.getDisplay(), 153, 221, 255)); 
        
	    Label titleLabel = new Label(labelTitleComposite, SWT.NONE);
	    titleLabel.setText("Agencia"); 
	    titleLabel.setFont(new Font(parent.getDisplay(), "Arial", 20, SWT.BOLD));
	    titleLabel.setBackground(new Color(parent.getDisplay(), 153, 221, 255)); 
	    GridData data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
	    titleLabel.setLayoutData(data);
	    	    
	    Label separador = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
	    data.horizontalSpan = 3; 
	    data.verticalIndent = 10;
	    separador.setLayoutData(data);
    	
        Button miPerfilButton = new Button(parent, SWT.PUSH);
        miPerfilButton.setText("Mi perfil");
        data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        data.widthHint = 100;
	    miPerfilButton.setLayoutData(data);
	    miPerfilButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {            	
            	if (clienteSession != null) {
            		close();
                	PerfilView perfilView = new PerfilView(getShell());
                	perfilView.open();                	
            	} else {
                	MessageDialog.openInformation(getShell(), "Aviso", "Inicia sesión o registrate antes.");
            	}
            }
        });

        Button misReservasButton = new Button(parent, SWT.PUSH);
        misReservasButton.setText("Mis reservas");
        data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        data.widthHint = 100;
        misReservasButton.setLayoutData(data);
        misReservasButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	if (clienteSession != null) {
            		close();
                	MisReservasView reservasView = new MisReservasView(getShell());
                	reservasView.open();
            	} else {
                	MessageDialog.openInformation(getShell(), "Aviso", "Inicia sesión o registrate antes.");
            	}
            }
        });
        
        if (agencia.getClienteSession() == null) {
        	Composite botonLoginRegistroComposite = new Composite(parent, SWT.NONE);
            data = new GridData(SWT.RIGHT, SWT.RIGHT, true, false);
            data.horizontalSpan = 1;
            botonLoginRegistroComposite.setLayoutData(data);
            botonLoginRegistroComposite.setLayout(new GridLayout(2, false));
        	
        	Button iniciarSesionButton = new Button(botonLoginRegistroComposite, SWT.PUSH);
        	iniciarSesionButton.setText("Iniciar sesión");
            data = new GridData(SWT.RIGHT, SWT.RIGHT, false, false);
            iniciarSesionButton.setLayoutData(data);
            iniciarSesionButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                	close();
                    IniciarSesionView iniciarSesionView = new IniciarSesionView(getShell());
                    iniciarSesionView.open();
                }
            });
            
            Button registrarButton = new Button(botonLoginRegistroComposite, SWT.PUSH);
            registrarButton.setText("Registrarse");
            data = new GridData(SWT.RIGHT, SWT.RIGHT, false, false);
            registrarButton.setLayoutData(data);
            registrarButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                	close();
                    RegistrarView registroView = new RegistrarView(getShell());
                    registroView.open();      
                }
            });
        } else {
        	Button cerrarSesionButton = new Button(parent, SWT.PUSH);
            cerrarSesionButton.setText("Cerrar sesión");
            data = new GridData(SWT.RIGHT, SWT.RIGHT, false, false);
            cerrarSesionButton.setLayoutData(data);
            cerrarSesionButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                	agencia.setClienteSession(null);
                	
                	close();
                    PrincipalView principalView = new PrincipalView(getShell());
                    principalView.open();                    
                }
            });
        }
        
        
        separador = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
	    data.horizontalSpan = 3; 
	    separador.setLayoutData(data);
        
	    Composite destinosLabelComposite = new Composite(parent, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        data.horizontalSpan = 3;        
        destinosLabelComposite.setLayoutData(data);
        destinosLabelComposite.setLayout(new GridLayout(1, false)); 
        destinosLabelComposite.setBackground(new Color(parent.getDisplay(), 153, 221, 255)); 
        	    
        destinosRecomendadosLabel = new Label(destinosLabelComposite, SWT.NONE);
        destinosRecomendadosLabel.setText("Destinos recomendados: "+"");
        destinosRecomendadosLabel.setBackground(new Color(parent.getDisplay(), 153, 221, 255)); 	    
        Font font = new Font(Display.getDefault(), "Arial", 10, SWT.BOLD);
        destinosRecomendadosLabel.setFont(font);
	    data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        destinosRecomendadosLabel.setLayoutData(data);        
        
        Group grpLabels = new Group(parent, SWT.NONE);
    	data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        data.horizontalSpan = 3;
        grpLabels.setLayoutData(data);
        grpLabels.setLayout(new GridLayout(1, false));    	
        
        puntuacionLabel = new Label(grpLabels, SWT.NONE);
        puntuacionLabel.setText("");    
        font = new Font(Display.getDefault(), "Arial", 8, SWT.BOLD);
        puntuacionLabel.setFont(font);
        data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        puntuacionLabel.setLayoutData(data);
        
        comentariosButton = new Button(grpLabels, SWT.PUSH);
        comentariosButton.setText("Ver comentarios");
        data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        comentariosButton.setLayoutData(data);
        
        imagenLabel = new Label(grpLabels, SWT.NONE);
        setImagen(imagenLabel);        
        data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        imagenLabel.setLayoutData(data);
        
        Composite botonesImagenComposite = new Composite(parent, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        data.horizontalSpan = 3;        
        botonesImagenComposite.setLayoutData(data);
        botonesImagenComposite.setLayout(new GridLayout(2, false));
                
        Button imagenAnteriorButton = new Button(botonesImagenComposite, SWT.PUSH);
        imagenAnteriorButton.setText("Destino siguiente");
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        imagenAnteriorButton.setLayoutData(data);
        imagenAnteriorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	if ((index_destino - 1) >= 0) {
            		index_destino -= 1;
            		setImagen(imagenLabel);
            	}
                
            }
        });
        
        Button imagenSiguienteButton = new Button(botonesImagenComposite, SWT.PUSH);
        imagenSiguienteButton.setText("Destino anterior");
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        imagenSiguienteButton.setLayoutData(data);
        imagenSiguienteButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	if ((index_destino + 1) < destinosRecomendados.size()) {
            		index_destino += 1;
            		setImagen(imagenLabel);
            	}
            }
        });
        
        /*
        Composite guiasLabelComposite = new Composite(parent, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        data.horizontalSpan = 3;        
        guiasLabelComposite.setLayoutData(data);
        guiasLabelComposite.setLayout(new GridLayout(1, false)); 
        guiasLabelComposite.setBackground(new Color(parent.getDisplay(), 153, 221, 255)); 
        	    
        Label guiasTitleLabel = new Label(guiasLabelComposite, SWT.NONE);
        guiasTitleLabel.setText("Guia recomendado");
	    data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
	    guiasTitleLabel.setLayoutData(data);        
        	    	    
	    guiasRecomendadosLabel = new Label(guiasLabelComposite, SWT.NONE);
	    guiasRecomendadosLabel.setText("");   
	    setGuia();
	    guiasRecomendadosLabel.setBackground(new Color(parent.getDisplay(), 153, 221, 255)); 	    
        font = new Font(Display.getDefault(), "Arial", 10, SWT.BOLD);
        guiasRecomendadosLabel.setFont(font);
	    data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
	    guiasRecomendadosLabel.setLayoutData(data);        
        
        Group grpGuiasLabels = new Group(parent, SWT.NONE);
    	data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        data.horizontalSpan = 3;
        grpLabels.setLayoutData(data);
        grpLabels.setLayout(new GridLayout(1, false));    	
        
        puntuacionGuiasLabel = new Label(grpGuiasLabels, SWT.NONE);
        puntuacionGuiasLabel.setText("");    
        font = new Font(Display.getDefault(), "Arial", 8, SWT.BOLD);
        puntuacionGuiasLabel.setFont(font);
        data = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        puntuacionGuiasLabel.setLayoutData(data);
        
        comentariosGuiasButton = new Button(grpGuiasLabels, SWT.PUSH);
        comentariosGuiasButton.setText("Ver comentarios");
        data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        comentariosGuiasButton.setLayoutData(data);
        
        Composite botonesGuiasComposite = new Composite(parent, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        data.horizontalSpan = 3;        
        botonesGuiasComposite.setLayoutData(data);
        botonesGuiasComposite.setLayout(new GridLayout(2, false));
                
        Button guiaAnteriorButton = new Button(botonesGuiasComposite, SWT.PUSH);
        guiaAnteriorButton.setText("Guía siguiente");
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        guiaAnteriorButton.setLayoutData(data);
        guiaAnteriorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	if ((index_guia - 1) >= 0) {
            		index_guia -= 1;
            		setGuia();
            	}
                
            }
        });
                
        Button guiaSiguienteButton = new Button(botonesGuiasComposite, SWT.PUSH);
        guiaSiguienteButton.setText("Guía anterior");
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        guiaSiguienteButton.setLayoutData(data);
        guiaSiguienteButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	if ((index_guia + 1) < guiasTuristicos.size()) {
            		index_guia += 1;
            		setGuia();
            	}
            }
        });
        */                
                        
        Group grpBottomButtons = new Group(parent, SWT.NONE);
    	data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        data.horizontalSpan = 3;
        data.verticalIndent = 20;
        grpBottomButtons.setLayoutData(data);
        grpBottomButtons.setLayout(new GridLayout(2, false));
        
        Button crearReservaButton = new Button(grpBottomButtons, SWT.PUSH);
        crearReservaButton.setText("Crear reserva");
        data = new GridData(SWT.FILL, SWT.CENTER, true, false);        
        data.widthHint = 100;
        data.heightHint = 25;
        crearReservaButton.setLayoutData(data);
        crearReservaButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	if (clienteSession != null) {
            		close();
                	CrearReservaView crearReservaView = new CrearReservaView(getShell());
                	crearReservaView.open();
            	} else {
                	MessageDialog.openInformation(getShell(), "Aviso", "Inicia sesión o registrate antes.");
            	}            	
            }
        });
        
        Button soporteButton = new Button(grpBottomButtons, SWT.PUSH);
        soporteButton.setText("Pedir soporte");
        data = new GridData(SWT.FILL, SWT.CENTER, true, false);        
        data.widthHint = 100;
        data.heightHint = 25;
        soporteButton.setLayoutData(data);
        soporteButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	if (clienteSession != null) {
            		close();
                	ChatView chatView = new ChatView(getShell());
                	chatView.open();	
            	} else {
                	MessageDialog.openInformation(getShell(), "Aviso", "Inicia sesión o registrate antes.");
            	}
            }
        });
                
        separador = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
	    data.horizontalSpan = 3;
	    data.verticalIndent = 10;
	    separador.setLayoutData(data);
                
        return parent;
    }
    
    private void setImagen(Label imagenLabel) {
    	Random rand = new Random();
    	int index_image = rand.nextInt(destinosRecomendados.get(index_destino).getImagenesRepresentativas().size());
    	Destino destinoSeleccionado = destinosRecomendados.get(index_destino);
    	
        double promedio = destinoSeleccionado.getEstrellas().stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
        DecimalFormat df = new DecimalFormat("#.##");
        String promedioFormateado = df.format(promedio);        
    	puntuacionLabel.setText("Puntuación media: "+promedioFormateado);
    	
    	comentariosButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	abrirComentariosDestino(Display.getCurrent(), destinoSeleccionado);
            }
        });
    	
    	String nombreDestino = destinoSeleccionado.getNombre();
        destinosRecomendadosLabel.setText("Destinos recomendados: "+nombreDestino);
    	
        String imagenUbicacion = destinoSeleccionado.getImagenesRepresentativas().get(index_image);
        Image imagenOriginal = new Image(Display.getDefault(), PATH_IMAGENES + imagenUbicacion);
        Image imagenRedimensionada = redimensionarImagen(imagenOriginal, 500, 200);
        imagenLabel.setImage(imagenRedimensionada);
        imagenOriginal.dispose();
    }
    
    private Image redimensionarImagen(Image imagenOriginal, int nuevoAncho, int nuevoAlto) {
        Image imagenRedimensionada = new Image(Display.getDefault(), nuevoAncho, nuevoAlto);
        GC gc = new GC(imagenRedimensionada);
        gc.setAntialias(SWT.ON);
        gc.drawImage(imagenOriginal, 0, 0, imagenOriginal.getBounds().width, imagenOriginal.getBounds().height,
                0, 0, nuevoAncho, nuevoAlto);
        gc.dispose();
        return imagenRedimensionada;
    }
    
    private void abrirComentariosDestino(Display display, Destino destinoSeleccionado) {    	
    	Shell shell = new Shell(display);
    	shell.setText("Comentarios: "+destinoSeleccionado.getNombre());
    	shell.setLayout(new GridLayout(1, false));
    	
    	Label nombreLabel = new Label(shell, SWT.BOLD);
        nombreLabel.setText("Nombre del Destino: ");
        Label nombreDestinoLabel = new Label(shell, SWT.NONE);
        nombreDestinoLabel.setText(destinoSeleccionado.getNombre());

        Label commentLabel = new Label(shell, SWT.BOLD);
        commentLabel.setText("Comentarios: ");
        
        List<String> comentarios = destinoSeleccionado.getComentarios();
        String comentariosInline = String.join(",", comentarios);
        Label stringComments = new Label(shell, SWT.BOLD);
        stringComments.setText(comentariosInline.replace(",", "\n"));               

        shell.pack();
        shell.open();
    }
    
    private void setGuia() {
    	GuiaTuristico guiaSeleccionado = guiasTuristicos.get(index_guia);
    	
    	String nombreGuia = guiaSeleccionado.getNombreCompleto();
    	guiasRecomendadosLabel.setText(nombreGuia);    	
    	
    	/*
    	double promedio = guiaSeleccionado.getEstrellas().stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
        DecimalFormat df = new DecimalFormat("#.##");
        String promedioFormateado = df.format(promedio);
        puntuacionGuiasLabel.setText("Puntuación media: "+promedioFormateado);    	
    	
        comentariosGuiasButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	//abrirComentariosDestino(Display.getCurrent(), destinoSeleccionado);
            }
        });
        */
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
