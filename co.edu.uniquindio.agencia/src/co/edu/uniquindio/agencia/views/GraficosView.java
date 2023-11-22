package co.edu.uniquindio.agencia.views;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ISeries.SeriesType;

import java.util.List;
import java.util.Map.Entry;

import co.edu.uniquindio.agencia.controllers.ModelFactoryController;
import co.edu.uniquindio.implementation.Agencia;

public class GraficosView extends ApplicationWindow {

	    private ModelFactoryController modelFactoryController;
	    private Agencia agencia;

	    private Chart chart;
	    private GridData data;
	    
	    public GraficosView(Shell parentShell) {
	        super(parentShell);
	        
	        modelFactoryController = ModelFactoryController.getInstance();
	        agencia = modelFactoryController.getAgencia();
	    }
	    
	    @Override
	    protected Control createContents(Composite parent) {
	        getShell().setSize(800, 600);    
	        getShell().setText("Gráficos");
	       
	        parent.setLayout(new GridLayout(4, false));
	    	fillBlank(parent, 3);
	    	
	    	Label titleLabel = new Label(parent, SWT.NONE);
		    titleLabel.setText("Gráfico de barras"); 
		    titleLabel.setFont(new Font(parent.getDisplay(), "Arial", 20, SWT.BOLD));
	        data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		    data.horizontalSpan = 4; 
		    titleLabel.setLayoutData(data);
	    			    
	    	Button btnPaquete = new Button(parent, SWT.NONE);
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

	    	Button btnDestino = new Button(parent, SWT.NONE);
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
	    	
	        Button btnGuia = new Button(parent, SWT.NONE);
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
	        	        
	        Button btnCerrar = new Button(parent, SWT.NONE);
	        btnCerrar.setText("Cerrar");
	        data = new GridData(SWT.RIGHT, SWT.NONE, false, false);
	        data.widthHint = 150;
	        btnCerrar.setLayoutData(data);
	        btnCerrar.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(SelectionEvent e) {
	                close(); 
	                StarterView view = new StarterView(getShell());
	                view.open();
	            }
	        });
	        
	        Combo combo = new Combo(parent, SWT.READ_ONLY);
	        combo.setItems(new String[] {"Destinos más buscados", "Destinos más reservados", "Paquetes más reservados", "Guias mejor puntuados"});
	        combo.select(0);
	        data = new GridData(SWT.FILL, SWT.FILL, true, false);
	        data.verticalIndent = 20;
	        data.horizontalSpan = 4;
	        combo.setLayoutData(data);	        
	        combo.addListener(SWT.Selection, new Listener() {
	            public void handleEvent(Event e) {
	                int index = combo.getSelectionIndex();
	                switch (index) {
	                    case 0:
	                        createChart(parent, agencia.obtenerDestinosMasBuscados(), "Destinos", "Búsquedas");
	                        break;
	                    case 1:
	                        createChart(parent, agencia.obtenerDestinosMasReservados(10), "Destinos", "Reservas");
	                        break;
	                    case 2:
	                        createChart(parent, agencia.obtenerPaquetesMasReservados(10), "Paquetes", "Reservas");
	                        break;
	                    case 3:
	                        createChart(parent, agencia.obtenerGuiasMejorPuntuados(), "Guías", "Calificación");
	                        break;
	                }
	            }
	        });
	        
	        createChart(parent, agencia.obtenerDestinosMasBuscados(), "Destinos", "Búsquedas");
	        
	        return parent;
	    }
	    
	    private void createChart(Composite parent, List<Entry<String, Integer>> list, String X_title, String Y_title) {
	    	if (chart != null) {
	            chart.dispose();
	        }

	        chart = new Chart(parent, SWT.NONE);
	        
	        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
	        data.horizontalSpan = 4;
	        chart.setLayoutData(data);
	        parent.layout();

	        // Configurar el título del gráfico
	        chart.getTitle().setText(" ");

	        // Crear la serie de datos
	        IBarSeries series = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "Datos");       

	        chart.getAxisSet().getXAxis(0).getTitle().setText(X_title);
	        chart.getAxisSet().getYAxis(0).getTitle().setText(Y_title);

	        // Extraer los datos de la lista
	        String[] categories = new String[list.size()];
	        double[] values = new double[list.size()];
	        for (int i = 0; i < list.size(); i++) {
	            categories[i] = list.get(i).getKey();
	            values[i] = list.get(i).getValue();
	        }

	        // Configurar los datos de la serie
	        series.setYSeries(values);

	        // Acortar los nombres de las categorías
	        String[] shortCategories = new String[categories.length];
	        for (int i = 0; i < categories.length; i++) {
	            if (categories[i].length() > 10) {
	                shortCategories[i] = categories[i].substring(0, 10) + "...";
	            } else {
	                shortCategories[i] = categories[i];
	            }
	        }

	        // Configurar las categorías
	        chart.getAxisSet().getXAxis(0).enableCategory(true);
	        chart.getAxisSet().getXAxis(0).setCategorySeries(shortCategories);

	        // Ajustar los ejes
	        chart.getAxisSet().adjustRange();

	        chart.getPlotArea().addMouseMoveListener(new MouseMoveListener() {
	            public void mouseMove(MouseEvent e) {
	                IAxisSet axisSet = chart.getAxisSet();
	                IAxis xAxis = axisSet.getXAxis(0);
	                IAxis yAxis = axisSet.getYAxis(0);
	                int x = (int) xAxis.getDataCoordinate(e.x);
	                int y = (int) yAxis.getDataCoordinate(e.y);
	                
	                boolean flag = false;
	                for (int i = 0; i < categories.length; i++) {
	                    if (x == i && y <= values[i]) {
	                        chart.getAxisSet().getXAxis(0).getTitle().setText("\n"+categories[i]);
	                        flag = true;
	                    }
	                }
	                if (!flag) {
                        chart.getAxisSet().getXAxis(0).getTitle().setText("\n"+X_title);
	                }
	            } 
	        });
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
