package principal;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import bots.BotSencillo;
import bots.BotVenderMinado;
import eventos.EventoMenuCredencialesBittrex;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import modelo.Cartera;
import modelo.Historial;
import modelo.Mercado;
import modelo.Orden;
import servicios.ServicioCarteras;
import servicios.ServicioHistoriales;
import servicios.ServicioJSON;
import servicios.ServicioMercados;
import servicios.ServicioOrdenes;
import servicios.ServicioPropiedades;
import servicios.ServicioRESTBittrex;

public class ControladorFormularioPrincipal implements Initializable {

	/*************************************/
	/***** Declaración de Atributos ******/
	/*************************************/

	/* Satoshis por BTC = 1*10^8 */
	static final double SATOSHI = 100000000;

	/* Formato decimal en BTC-Satoshi 1*10^-8 */
	static final String SATOSHI_DECIMAL_FORMAT = "%.8f";

	/* Mercado por defecto */
	private String mercadoDefecto = "BTC-ZEC";
	
	/* Moneda por defecto */
	private String monedaDefecto = "ZEC";

	/* Código API y Key Secreta */
	private String codigoApi;
	private String keySecret;

	/* Milisegundos antes de refrescar */
	private long milisegundosParaActualizar = 30000;

	/* Servicios */
	ServicioRESTBittrex servicioBittrex;
	ServicioJSON servicioJSON;
	ServicioMercados servicioMercados;
	ServicioCarteras servicioCarteras;
	ServicioOrdenes servicioOrdenes;
	ServicioHistoriales servicioHistoriales;
	ServicioPropiedades servicioPropiedades;

	/* Atributos acivos */
	private Cartera carteraActiva = null;
	private Mercado mercadoActivo = null;
	private Timer temporizador;
	private double mayor;
	private double menor;

	ArrayList<Cartera> listaCarteras;
	ObservableList<Cartera> listaObservableCarteras;
	ArrayList<Mercado> listaMercados;
	ObservableList<Mercado> listaObservableMercados;
	ArrayList<Orden> listaOrdenesCompra;
	ObservableList<Orden> listaObservableOrdenesCompra;
	ArrayList<Orden> listaOrdenesVenta;
	ObservableList<Orden> listaObservableOrdenesVenta;
	ArrayList<Historial> listaHistorialMercado;
	ObservableList<Historial> listaObservableHistorialMercado;
	ArrayList<Historial> listaMisOrdenesAbiertas;
	ObservableList<Historial> listaObservableMisOrdenesAbiertas;
	ArrayList<Historial> listaMiHistorial;
	ObservableList<Historial> listaObservableMiHistorial;
	XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

	/*********************************************************************/
	/******* Anotaciones JavaFX2 de los controles del formulario *********/
	/*********************************************************************/

	/* Texto Balance Total */
	@FXML
	private TextField textoBalanceTotal;

	/* Tabla Carteras */
	@FXML
	private TableView<Cartera> tablaCarteras;

	/* Tabla Mercado */
	@FXML
	private TableView<Mercado> tablaMercados;

	/* Tabla Órdenes de Compra */
	@FXML
	private TableView<Orden> tablaOrdenesCompra;

	/* Tabla Órdenes de Venta */
	@FXML
	private TableView<Orden> tablaOrdenesVenta;

	/* Tabla Historial del Mercado */
	@FXML
	private TableView<Historial> tablaHistorialMercado;

	/* Tabla Mis Ordenes Abiertas */
	@FXML
	private TableView<Historial> tablaMisOrdenesAbiertas;

	/* Tabla Mi Historial */
	@FXML
	private TableView<Historial> tablaMiHistorial;

	/* Anotaciones Sección Compra */
	@FXML
	private Label labelDisponibleCompra;

	@FXML
	private Label labelMonedaDisponibleCompra;

	@FXML
	private TextField textoCantidadCompra;

	@FXML
	private Label labelMonedaCantidadCompra;

	@FXML
	private TextField textoPrecioCompra;

	@FXML
	private Label labelMonedaPrecioCompra;

	@FXML
	private TextField textoTotalCompra;

	@FXML
	private Label labelMonedaTotalCompra;

	@FXML
	private Button botonBidCompra;

	@FXML
	private Button botonAskCompra;

	@FXML
	private Button botonLastCompra;

	@FXML
	private Button botonMaxCompra;

	@FXML
	private Button botonComprar;

	/* Anotaciones Sección Venta */
	@FXML
	private Label labelDisponibleVenta;

	@FXML
	private Label labelMonedaDisponibleVenta;

	@FXML
	private TextField textoCantidadVenta;

	@FXML
	private Label labelMonedaCantidadVenta;

	@FXML
	private TextField textoPrecioVenta;

	@FXML
	private Label labelMonedaPrecioVenta;

	@FXML
	private TextField textoTotalVenta;

	@FXML
	private Label labelMonedaTotalVenta;

	@FXML
	private Button botonBidVenta;

	@FXML
	private Button botonAskVenta;

	@FXML
	private Button botonLastVenta;

	@FXML
	private Button botonMaxVenta;

	@FXML
	private Button botonVender;

	/* Anotación del botón Cancelar Orden */
	@FXML
	private Button botonCancelarOrden;

	/* Anotación del gráfico del Mercado */
	@FXML
	private LineChart<String, Number> graficoMercado;

	/* Anotación del eje X del gráfico */
	@FXML
	private CategoryAxis xAxis;

	/* Anotación del eje X del gráfico */
	@FXML
	private NumberAxis yAxis;

	/* Anotación del botón Actualizar */
	@FXML
	private Button botonAutoActualizar;

	/* Anotación del botón Parar el Actualizar */
	@FXML
	private Button botonPararAutoActualizar;

	@FXML
	private MenuItem menuClose;

	@FXML
	private MenuItem menuCredentialsBittrex;

	@FXML
	private MenuItem menuAbout;
	
	/*************************************/
	/**** Métodos de las propiedades *****/
	/*************************************/
	
	public String getCodigoApi() {
		return codigoApi;
	}

	public void setCodigoApi(String codigoApi) {
		this.codigoApi = codigoApi;
	}

	public String getKeySecret() {
		return keySecret;
	}

	public void setKeySecret(String keySecret) {
		this.keySecret = keySecret;
	}

	public ServicioPropiedades getServicioPropiedades() {
		return servicioPropiedades;
	}

	public void setServicioPropiedades(ServicioPropiedades servicioPropiedades) {
		this.servicioPropiedades = servicioPropiedades;
	}

	

	/*************************************/
	/***** Métodos del formulario ********/
	/*************************************/

	/* Método que inicializa el formulario */
	@Override
	public void initialize(URL localizacion, ResourceBundle recursos) {

		/*
		 * Definimos como localización por defecto los EE.UU. para que coja el
		 * punto como punto decimal en lugar de la coma
		 */
		Locale.setDefault(Locale.US);

		/*************************************/
		/**** Inicialización de Servicios ****/
		/*************************************/

		servicioPropiedades = new ServicioPropiedades();

		codigoApi = servicioPropiedades.getPropiedad("bittrex-codigo-api");
		keySecret = servicioPropiedades.getPropiedad("bittrex-key-secret");
		System.out.println(codigoApi + " - " + keySecret);
		servicioBittrex = new ServicioRESTBittrex(codigoApi, keySecret);

		servicioJSON = new ServicioJSON();
		servicioMercados = new ServicioMercados();
		servicioCarteras = new ServicioCarteras();
		servicioOrdenes = new ServicioOrdenes();
		servicioHistoriales = new ServicioHistoriales();

		/*************************************/
		/** Declaración y manejo de Eventos **/
		/*************************************/

		/* Evento Click de la tabla Carteras */
		tablaCarteras.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				carteraActiva = tablaCarteras.getSelectionModel().getSelectedItem();
			}
		});
		
		/* Evento Click de la tabla de Mercados */
		tablaMercados.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				mercadoActivo = tablaMercados.getSelectionModel().getSelectedItem();
				actualizarMercado();
			}
		});

		/* Evento Action del Total de Venta (ocurre al pulsar Enter) */
		textoCantidadCompra.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				calcularCompra();
			}
		});

		/* Evento Action del Total de Compra (ocurre al pulsar Enter) */
		textoPrecioCompra.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				calcularCompra();
			}
		});

		botonBidCompra.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String precio = String.format(SATOSHI_DECIMAL_FORMAT, tablaOrdenesCompra.getItems().get(0).getRate());
				textoPrecioCompra.setText(precio);
			}
		});

		botonAskCompra.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String precio = String.format(SATOSHI_DECIMAL_FORMAT, tablaOrdenesVenta.getItems().get(0).getRate());
				textoPrecioCompra.setText(precio);
			}
		});

		botonLastCompra.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String precio = String.format(SATOSHI_DECIMAL_FORMAT,
						tablaHistorialMercado.getItems().get(0).getPrice());
				textoPrecioCompra.setText(precio);
			}
		});

		botonMaxCompra.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				calcularMaximoCompra();
			}
		});

		/* Evento Click del botón Comprar */
		botonComprar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (comprar()) {
					// System.out.println("Orden de Compra puesta con éxito.");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Order Confirmation");
					alert.setHeaderText(null);
					alert.setContentText("Your buy order was succesfull added!");
					alert.showAndWait();
					actualizarMercado();
				} else {
					// System.out.println("No se pudo poner la Orden de
					// Compra.");
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Order Error");
					alert.setHeaderText(null);
					alert.setContentText("It wasn't possible to execute your sell order!");
					alert.showAndWait();
				}
			}
		});

		/* Evento Action del Total de Venta (ocurre al pulsar Enter) */
		textoCantidadVenta.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				calcularVenta();
			}
		});

		/* Evento Action del Total de Venta (ocurre al pulsar Enter) */
		textoPrecioVenta.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				calcularVenta();
			}
		});

		botonBidVenta.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String precio = String.format(SATOSHI_DECIMAL_FORMAT, tablaOrdenesCompra.getItems().get(0).getRate());
				textoPrecioVenta.setText(precio);
			}
		});

		botonAskVenta.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String precio = String.format(SATOSHI_DECIMAL_FORMAT, tablaOrdenesVenta.getItems().get(0).getRate());
				textoPrecioVenta.setText(precio);
			}
		});

		botonLastVenta.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String precio = String.format(SATOSHI_DECIMAL_FORMAT,
						tablaHistorialMercado.getItems().get(0).getPrice());
				textoPrecioVenta.setText(precio);
			}
		});

		botonMaxVenta.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				calcularMaximoVenta();
			}
		});

		/* Evento Click del botón Vender */
		botonVender.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (vender()) {
					// System.out.println("Orden de Venta puesta con éxito.");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Order Confirmation");
					alert.setHeaderText(null);
					alert.setContentText("Your sell order was succesfull executed!");
					alert.showAndWait();
					actualizarMercado();
				} else {
					// System.out.println("No se pudo poner la Orden de
					// Venta.");
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Order Error");
					alert.setHeaderText(null);
					alert.setContentText("It wasn't possible to execute your sell order!");
					alert.showAndWait();
				}
			}
		});

		/* Evento Click del botón Cancelar Orden */
		botonCancelarOrden.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Historial historial = tablaMisOrdenesAbiertas.getSelectionModel().getSelectedItem();
				String ordenUuid = historial.getOrderUuid();
				if (cancelarOrden(ordenUuid)) {
					// System.out.println("Orden cancelada con éxito.");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Order Confirmation");
					alert.setHeaderText(null);
					alert.setContentText("Your order was succesfull cancelled!");
					alert.showAndWait();
					actualizarMercado();
				} else {
					// System.out.println("No se pudo cancelar la orden.");
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Order Error");
					alert.setHeaderText(null);
					alert.setContentText("It wasn't possible to cancel your order!");
					alert.showAndWait();
				}

			}
		});

		/* Evento Click del botón AutoActualizar */
		botonAutoActualizar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				temporizador = new Timer();
				temporizador.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						Platform.runLater(() -> {
							try {
								actualizar();
							} catch (Exception e) {
								e.printStackTrace();
							}
						});
					}
				}, 0, milisegundosParaActualizar);
				botonAutoActualizar.setDisable(true);
				botonPararAutoActualizar.setDisable(false);
			}
		});

		/* Evento Click del botón Parar AutoActualizar */
		botonPararAutoActualizar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				temporizador.cancel();
				botonAutoActualizar.setDisable(false);
				botonPararAutoActualizar.setDisable(true);
			}
		});

		menuClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		});

		menuCredentialsBittrex.setOnAction(new EventoMenuCredencialesBittrex(this));

		menuAbout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("About");
				alert.setHeaderText("Cryptocurrency Tracker " + Constantes.VERSION);
				alert.setContentText("Developed by " + Constantes.AUTHOR);
				alert.showAndWait();
			}
		});

		/* Primera inicialización */
		inicializarControles();
		
		//Prueba BotSencillo
//		BotSencillo botSencillo = new BotSencillo(mercadoActivo.getMarketName(), 0.00003000, 0.00007000, 2000, this);
//		botSencillo.start();
		
		//Prueba BotVenderMinado
		BotVenderMinado botVenderMinado = new BotVenderMinado(mercadoActivo.getMarketName(), 3.25d, 2000, this);
		botVenderMinado.start();
		
		/* Prueba de transferencia de dinero */
		//transferirDinero();
		
	}

	private void inicializarControles() {
		if ((codigoApi == null || !codigoApi.equals("")) && (keySecret == null || !keySecret.equals(""))) {
			try {
				mostrarBalanceTotal();
				actualizarCarteras();
				mostrarCarteras();
				actualizarMisOrdenesAbiertas(mercadoDefecto);
				mostrarMisOrdenesAbiertas(mercadoDefecto);
				actualizarMiHistorial(mercadoDefecto);
				mostrarMiHistorial(mercadoDefecto);
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Credentials error");
				alert.setContentText(
						"Api code or secret key are no correct and request was refused by the exchanger. Please, review your settings.");
				alert.showAndWait();
			}
		}
		actualizarMercados();
		mostrarMercados();
		actualizarOrdenesCompra(mercadoDefecto);
		mostrarOrdenesCompra(mercadoDefecto);
		actualizarOrdenesVenta(mercadoDefecto);
		mostrarOrdenesVenta(mercadoDefecto);
		actualizarHistorialMercado(mercadoDefecto);
		mostrarHistorialMercado(mercadoDefecto);
		actualizarGraficoMercado();
		mostrarGraficoMercado();
		actualizarDisponibles(mercadoDefecto);
		for (Mercado mercado : listaMercados) {
			if (mercado.getMarketName().equals(mercadoDefecto))
				mercadoActivo = mercado;
		}
		if(listaCarteras.size() > 0) {
			for (Cartera cartera : listaCarteras) {
				if (cartera.getCurrency().equals(monedaDefecto))
					carteraActiva = cartera;
			}
		}
	}

	public void mostrarBalanceTotal() {
		JSONObject json = servicioJSON.leerJSON(servicioBittrex.getBalances());
		double balanceTotal = servicioJSON.getBalanceTotal(json);
		Task<Void> taskActualizarBalanceTotal = new Task<Void>() {
			@Override
			public Void call() {
				textoBalanceTotal.setText(String.valueOf(String.format(SATOSHI_DECIMAL_FORMAT, balanceTotal)));
				this.cancel();
				return null;
			}
		};
		Platform.runLater(taskActualizarBalanceTotal);
	}

	public void actualizarCarteras() {
		JSONArray carteras = servicioJSON.getResultadoArrayBittrex(servicioBittrex.getBalances());
		listaCarteras = new ArrayList<Cartera>();
		for (Object obj : carteras) {
			Cartera cartera = servicioCarteras.convertirJSON(obj);
			listaCarteras.add(cartera);
		}
		if (listaObservableCarteras != null) {
			listaObservableCarteras.clear();
			listaObservableCarteras.addAll(listaCarteras);
		}
		tablaCarteras.sort();
	}

	@SuppressWarnings("unchecked")
	private void mostrarCarteras() {

		listaObservableCarteras = FXCollections.observableArrayList(listaCarteras);

		tablaCarteras.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn<Cartera, String> currencyCol = new TableColumn<Cartera, String>("Currency");
		currencyCol.setMinWidth(75);
		currencyCol.setCellValueFactory(new PropertyValueFactory<Cartera, String>("currency"));

		TableColumn<Cartera, Double> balanceCol = new TableColumn<Cartera, Double>("Balance");
		balanceCol.setMinWidth(75);
		balanceCol.setComparator(balanceCol.getComparator().reversed());
		balanceCol.setCellValueFactory(new PropertyValueFactory<Cartera, Double>("balance"));

		TableColumn<Cartera, Double> availableCol = new TableColumn<Cartera, Double>("Available");
		availableCol.setMinWidth(75);
		availableCol.setCellValueFactory(new PropertyValueFactory<Cartera, Double>("available"));

		TableColumn<Cartera, Double> pendingCol = new TableColumn<Cartera, Double>("Pending");
		pendingCol.setMinWidth(75);
		pendingCol.setCellValueFactory(new PropertyValueFactory<Cartera, Double>("pending"));

		TableColumn<Cartera, String> cryptoAddressCol = new TableColumn<Cartera, String>("CryptoAddress");
		cryptoAddressCol.setMinWidth(290);
		cryptoAddressCol.setCellValueFactory(cellData -> cellData.getValue().getCryptoAddressProperty());
		cryptoAddressCol.setCellFactory(column -> {
			TableCell<Cartera, String> cell = new TableCell<Cartera, String>() {
				@Override
				protected void updateItem(String s, boolean bln) {
					super.updateItem(s, bln);
					setText(s);
					setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent t) {
							ClipboardContent content = new ClipboardContent();
							content.putString(s);
							Clipboard.getSystemClipboard().setContent(content);
							setStyle("-fx-text-fill: yellow; -fx-font-weight:bold;");
							t.consume();
						}
					});
					setOnMouseExited(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent t) {
							setStyle(".table-row-cell:selected {-fx-text-fill: black; -fx-font-weight:normal;}");
							t.consume();
						}
					});
				}
			};
			return cell;
		});

		tablaCarteras.setItems(listaObservableCarteras);

		tablaCarteras.getColumns().clear();
		tablaCarteras.getColumns().addAll(currencyCol, balanceCol, availableCol, pendingCol, cryptoAddressCol);
		tablaCarteras.getSortOrder().clear();
		tablaCarteras.getSortOrder().add(balanceCol);
		tablaCarteras.sort();
	}

	public void actualizarMercados() {
		JSONArray mercados = servicioJSON.getResultadoArrayBittrex(servicioBittrex.getMarketSummaries());
		listaMercados = new ArrayList<Mercado>();
		for (Object obj : mercados) {
			Mercado mercado = servicioMercados.convertirJSON(obj);
			listaMercados.add(mercado);
		}
		if (listaObservableMercados != null) {
			listaObservableMercados.clear();
			listaObservableMercados.addAll(listaMercados);
		}
		tablaMercados.sort();
	}

	@SuppressWarnings("unchecked")
	private void mostrarMercados() {

		listaObservableMercados = FXCollections.observableArrayList(listaMercados);

		TableColumn<Mercado, String> marketNameCol = new TableColumn<Mercado, String>("MarketName");
		marketNameCol.setMinWidth(50);
		marketNameCol.setCellValueFactory(new PropertyValueFactory<Mercado, String>("marketName"));

		TableColumn<Mercado, String> lastCol = new TableColumn<Mercado, String>("Last");
		lastCol.setMinWidth(75);
		// lastCol.setCellValueFactory(
		// new PropertyValueFactory<Mercado, Double>("last"));
		lastCol.setCellValueFactory(cellData -> Bindings.format(SATOSHI_DECIMAL_FORMAT, cellData.getValue().getLast()));

		TableColumn<Mercado, Double> volumeCol = new TableColumn<Mercado, Double>("Volume");
		volumeCol.setMinWidth(75);
		volumeCol.setComparator(volumeCol.getComparator().reversed());
		volumeCol.setCellValueFactory(new PropertyValueFactory<Mercado, Double>("volume"));

		TableColumn<Mercado, String> bidCol = new TableColumn<Mercado, String>("Bid");
		bidCol.setMinWidth(75);
		// bidCol.setCellValueFactory(
		// new PropertyValueFactory<Mercado, Double>("bid"));
		bidCol.setCellValueFactory(cellData -> Bindings.format(SATOSHI_DECIMAL_FORMAT, cellData.getValue().getBid()));

		TableColumn<Mercado, String> askCol = new TableColumn<Mercado, String>("Ask");
		askCol.setMinWidth(75);
		// askCol.setCellValueFactory(
		// new PropertyValueFactory<Mercado, Double>("ask"));
		askCol.setCellValueFactory(cellData -> Bindings.format(SATOSHI_DECIMAL_FORMAT, cellData.getValue().getAsk()));

		TableColumn<Mercado, String> highCol = new TableColumn<Mercado, String>("High");
		highCol.setMinWidth(75);
		// highCol.setCellValueFactory(
		// new PropertyValueFactory<Mercado, Double>("high"));
		highCol.setCellValueFactory(cellData -> Bindings.format(SATOSHI_DECIMAL_FORMAT, cellData.getValue().getHigh()));

		TableColumn<Mercado, String> lowCol = new TableColumn<Mercado, String>("Low");
		lowCol.setMinWidth(75);
		// lowCol.setCellValueFactory(
		// new PropertyValueFactory<Mercado, Double>("low"));
		lowCol.setCellValueFactory(cellData -> Bindings.format(SATOSHI_DECIMAL_FORMAT, cellData.getValue().getLow()));

		tablaMercados.setItems(listaObservableMercados);

		tablaMercados.getColumns().clear();
		tablaMercados.getColumns().addAll(marketNameCol, lastCol, volumeCol, bidCol, askCol, highCol, lowCol);
		tablaMercados.getSortOrder().clear();
		tablaMercados.getSortOrder().add(volumeCol);
		tablaMercados.sort();
	}

	public void actualizarOrdenesCompra(String market) {
		JSONArray ordenesCompra = servicioJSON.getResultadoArrayBittrex(servicioBittrex.getBuyOrders(market, "10"));
		listaOrdenesCompra = new ArrayList<Orden>();
		for (Object obj : ordenesCompra) {
			Orden orden = servicioOrdenes.convertirJSON(obj);
			listaOrdenesCompra.add(orden);
		}
		if (listaObservableOrdenesCompra != null) {
			listaObservableOrdenesCompra.clear();
			listaObservableOrdenesCompra.addAll(listaOrdenesCompra);
		}
		tablaOrdenesCompra.sort();
	}

	@SuppressWarnings("unchecked")
	private void mostrarOrdenesCompra(String market) {

		listaObservableOrdenesCompra = FXCollections.observableArrayList(listaOrdenesCompra);

		TableColumn<Orden, Double> quantityCompraCol = new TableColumn<Orden, Double>("Quantity");
		quantityCompraCol.setMinWidth(50);
		quantityCompraCol.setCellValueFactory(new PropertyValueFactory<Orden, Double>("Quantity"));

		TableColumn<Orden, String> rateCompraCol = new TableColumn<Orden, String>("Rate");
		rateCompraCol.setMinWidth(75);
		rateCompraCol.setCellValueFactory(
				cellData -> Bindings.format(SATOSHI_DECIMAL_FORMAT, cellData.getValue().getRate()));

		tablaOrdenesCompra.setItems(listaObservableOrdenesCompra);
		tablaOrdenesCompra.getColumns().clear();
		tablaOrdenesCompra.getColumns().addAll(quantityCompraCol, rateCompraCol);

		String moneda = market.substring(market.indexOf("-") + 1);
		labelMonedaCantidadCompra.setText(moneda);
	}

	public void actualizarOrdenesVenta(String market) {
		JSONArray ordenesVenta = servicioJSON.getResultadoArrayBittrex(servicioBittrex.getSellOrders(market, "10"));
		listaOrdenesVenta = new ArrayList<Orden>();
		for (Object obj : ordenesVenta) {
			Orden orden = servicioOrdenes.convertirJSON(obj);
			listaOrdenesVenta.add(orden);
		}
		if (listaObservableOrdenesVenta != null) {
			listaObservableOrdenesVenta.clear();
			listaObservableOrdenesVenta.addAll(listaOrdenesVenta);
		}
		tablaOrdenesVenta.sort();
	}

	@SuppressWarnings("unchecked")
	public void mostrarOrdenesVenta(String market) {

		listaObservableOrdenesVenta = FXCollections.observableArrayList(listaOrdenesVenta);

		TableColumn<Orden, Double> quantityVentaCol = new TableColumn<Orden, Double>("Quantity");
		quantityVentaCol.setMinWidth(50);
		quantityVentaCol.setCellValueFactory(new PropertyValueFactory<Orden, Double>("Quantity"));

		TableColumn<Orden, String> rateVentaCol = new TableColumn<Orden, String>("Rate");
		rateVentaCol.setMinWidth(75);
		rateVentaCol.setCellValueFactory(
				cellData -> Bindings.format(SATOSHI_DECIMAL_FORMAT, cellData.getValue().getRate()));

		tablaOrdenesVenta.setItems(listaObservableOrdenesVenta);
		tablaOrdenesVenta.getColumns().clear();
		tablaOrdenesVenta.getColumns().addAll(quantityVentaCol, rateVentaCol);

		String moneda = market.substring(market.indexOf("-") + 1);
		labelMonedaDisponibleVenta.setText(moneda);
		labelMonedaCantidadVenta.setText(moneda);
	}

	public void actualizarHistorialMercado(String market) {
		JSONArray historialMercado = servicioJSON
				.getResultadoArrayBittrex(servicioBittrex.getMarketHistory(market, "2"));
		listaHistorialMercado = new ArrayList<Historial>();
		for (Object obj : historialMercado) {
			Historial historial = servicioHistoriales.convertirJSON(obj);
			listaHistorialMercado.add(historial);
		}
		if (listaObservableHistorialMercado != null) {
			listaObservableHistorialMercado.clear();
			listaObservableHistorialMercado.addAll(listaHistorialMercado);
		}
		tablaHistorialMercado.sort();
	}

	@SuppressWarnings("unchecked")
	private void mostrarHistorialMercado(String market) {

		listaObservableHistorialMercado = FXCollections.observableArrayList(listaHistorialMercado);

		TableColumn<Historial, Double> quantityHistorialMercadoCol = new TableColumn<Historial, Double>("Quantity");
		quantityHistorialMercadoCol.setMinWidth(50);
		quantityHistorialMercadoCol.setCellValueFactory(new PropertyValueFactory<Historial, Double>("Quantity"));

		TableColumn<Historial, String> priceHistorialMercadoCol = new TableColumn<Historial, String>("Price");
		priceHistorialMercadoCol.setMinWidth(75);
		priceHistorialMercadoCol.setCellValueFactory(
				cellData -> Bindings.format(SATOSHI_DECIMAL_FORMAT, cellData.getValue().getPrice()));

		TableColumn<Historial, String> orderTypeHistorialMercadoCol = new TableColumn<Historial, String>("Order Type");
		orderTypeHistorialMercadoCol.setMinWidth(50);
		orderTypeHistorialMercadoCol.setCellValueFactory(new PropertyValueFactory<Historial, String>("orderType"));

		tablaHistorialMercado.setItems(listaObservableHistorialMercado);
		tablaHistorialMercado.getColumns().clear();
		tablaHistorialMercado.getColumns().addAll(quantityHistorialMercadoCol, priceHistorialMercadoCol,
				orderTypeHistorialMercadoCol);

	}

	public void actualizarMisOrdenesAbiertas(String market) {
		JSONArray misOrdenesAbiertas = servicioJSON.getResultadoArrayBittrex(servicioBittrex.getOpenOrders(market));
		listaMisOrdenesAbiertas = new ArrayList<Historial>();
		for (int i = misOrdenesAbiertas.size() - 1; i >= 0; i--) {
			Object obj = misOrdenesAbiertas.get(i);
			Historial historial = servicioHistoriales.convertirJSON(obj);
			listaMisOrdenesAbiertas.add(historial);
		}
		if (listaObservableMisOrdenesAbiertas != null) {
			listaObservableMisOrdenesAbiertas.clear();
			listaObservableMisOrdenesAbiertas.addAll(listaMisOrdenesAbiertas);
		}
		tablaMisOrdenesAbiertas.sort();
	}

	@SuppressWarnings("unchecked")
	private void mostrarMisOrdenesAbiertas(String market) {

		listaObservableMisOrdenesAbiertas = FXCollections.observableArrayList(listaMisOrdenesAbiertas);

		TableColumn<Historial, Double> quantityMisOrdenesAbiertasCol = new TableColumn<Historial, Double>("Quantity");
		quantityMisOrdenesAbiertasCol.setMinWidth(50);
		quantityMisOrdenesAbiertasCol.setCellValueFactory(new PropertyValueFactory<Historial, Double>("Quantity"));

		TableColumn<Historial, String> limitMisOrdenesAbiertasCol = new TableColumn<Historial, String>("Rate");
		limitMisOrdenesAbiertasCol.setMinWidth(75);
		limitMisOrdenesAbiertasCol.setCellValueFactory(
				cellData -> Bindings.format(SATOSHI_DECIMAL_FORMAT, cellData.getValue().getLimit()));

		TableColumn<Historial, String> orderTypeMisOrdenesAbiertasCol = new TableColumn<Historial, String>(
				"Order Type");
		orderTypeMisOrdenesAbiertasCol.setMinWidth(75);
		orderTypeMisOrdenesAbiertasCol.setCellValueFactory(new PropertyValueFactory<Historial, String>("orderType"));

		tablaMisOrdenesAbiertas.setItems(listaObservableMisOrdenesAbiertas);
		tablaMisOrdenesAbiertas.getColumns().clear();
		tablaMisOrdenesAbiertas.getColumns().addAll(quantityMisOrdenesAbiertasCol, limitMisOrdenesAbiertasCol,
				orderTypeMisOrdenesAbiertasCol);
	}

	public void actualizarMiHistorial(String market) {
		JSONArray miHistorial = servicioJSON.getResultadoArrayBittrex(servicioBittrex.getOrderHistory(market, "5"));
		listaMiHistorial = new ArrayList<Historial>();
		for (Object obj : miHistorial) {
			Historial historial = servicioHistoriales.convertirJSON(obj);
			listaMiHistorial.add(historial);
		}
		if (listaObservableMiHistorial != null) {
			listaObservableMiHistorial.clear();
			listaObservableMiHistorial.addAll(listaMiHistorial);
		}
		tablaMiHistorial.sort();
	}

	@SuppressWarnings("unchecked")
	private void mostrarMiHistorial(String market) {

		listaObservableMiHistorial = FXCollections.observableArrayList(listaMiHistorial);

		TableColumn<Historial, Double> quantityMiHistorialCol = new TableColumn<Historial, Double>("Quantity");
		quantityMiHistorialCol.setMinWidth(50);
		quantityMiHistorialCol.setCellValueFactory(new PropertyValueFactory<Historial, Double>("Quantity"));

		TableColumn<Historial, String> limitMiHistorialCol = new TableColumn<Historial, String>("Rate");
		limitMiHistorialCol.setMinWidth(75);
		limitMiHistorialCol.setCellValueFactory(
				cellData -> Bindings.format(SATOSHI_DECIMAL_FORMAT, cellData.getValue().getLimit()));

		TableColumn<Historial, String> orderTypeMiHistorialCol = new TableColumn<Historial, String>("Order Type");
		orderTypeMiHistorialCol.setMinWidth(75);
		orderTypeMiHistorialCol.setCellValueFactory(new PropertyValueFactory<Historial, String>("orderType"));

		tablaMiHistorial.setItems(listaObservableMiHistorial);
		tablaMiHistorial.getColumns().clear();
		tablaMiHistorial.getColumns().addAll(quantityMiHistorialCol, limitMiHistorialCol, orderTypeMiHistorialCol);
	}

	private boolean comprar() {
		String market = mercadoActivo.getMarketName();
		String quantity = textoCantidadCompra.getText();
		String rate = textoPrecioCompra.getText();
		JSONObject objetoJSON = servicioJSON.leerJSON(servicioBittrex.buyLimit(market, quantity, rate));
		return (Boolean) objetoJSON.get("success");
	}
	
	public boolean comprar(String mercado, double cantidad, double precio) {
		String market = mercado;
		String quantity = String.format(SATOSHI_DECIMAL_FORMAT, cantidad);
		String rate = String.format(SATOSHI_DECIMAL_FORMAT,precio);
		JSONObject objetoJSON = servicioJSON.leerJSON(servicioBittrex.buyLimit(market, quantity, rate));
		return (Boolean) objetoJSON.get("success");
	}

	private boolean vender() {
		String market = mercadoActivo.getMarketName();
		String quantity = textoCantidadVenta.getText();
		String rate = textoPrecioVenta.getText();
		JSONObject objetoJSON = servicioJSON.leerJSON(servicioBittrex.sellLimit(market, quantity, rate));
		return (Boolean) objetoJSON.get("success");
	}
	
	public boolean vender(String mercado, double cantidad, double precio) {
		String market = mercado;
		String quantity = String.format(SATOSHI_DECIMAL_FORMAT, cantidad);
		String rate = String.format(SATOSHI_DECIMAL_FORMAT,precio);
		JSONObject objetoJSON = servicioJSON.leerJSON(servicioBittrex.sellLimit(market, quantity, rate));
		return (Boolean) objetoJSON.get("success");
	}

	public boolean cancelarOrden(String orderUuid) {
		JSONObject objetoJSON = servicioJSON.leerJSON(servicioBittrex.cancel(orderUuid));
		return (Boolean) objetoJSON.get("success");
	}

	private void actualizarDisponibles(String market) {
		Task<Void> taskActualizarDisponibles = new Task<Void>() {
			@Override
			public Void call() {
				String monedaCompra = market.substring(0, market.indexOf("-"));
				String monedaVenta = market.substring(market.indexOf("-") + 1);
				labelDisponibleCompra.setText(String.format(SATOSHI_DECIMAL_FORMAT, getDisponible(monedaCompra)));
				labelDisponibleVenta.setText(String.valueOf(getDisponible(monedaVenta)));
				this.cancel();
				return null;
			}
		};
		Platform.runLater(taskActualizarDisponibles);
	}

	private void actualizarMercado() {
		String market;
		if (mercadoActivo != null)
			market = mercadoActivo.getMarketName();
		else
			market = mercadoDefecto;
		// System.out.println(market);
		if ((codigoApi == null || !codigoApi.equals("")) && (keySecret == null || !keySecret.equals(""))) {
			actualizarMisOrdenesAbiertas(market);
			actualizarMiHistorial(market);
			actualizarDisponibles(market);
		}
		actualizarOrdenesCompra(market);
		actualizarOrdenesVenta(market);
		actualizarHistorialMercado(market);
		Task<Void> taskActualizar = new Task<Void>() {
			@Override
			public Void call() {
				actualizarGraficoMercado();
				mostrarGraficoMercado();
				this.cancel();
				return null;
			}
		};
		Platform.runLater(taskActualizar);

	}

	public void actualizar() {
		Task<Void> taskActualizar = new Task<Void>() {
			@Override
			public Void call() {
				if ((codigoApi == null || !codigoApi.equals("")) && (keySecret == null || !keySecret.equals(""))) {
					mostrarBalanceTotal();
					actualizarCarteras();
				}
				actualizarMercados();
				actualizarMercado();
				this.cancel();
				return null;
			}
		};
		// Platform.runLater(taskActualizar);
		new Thread(taskActualizar).run();
	}

	private void actualizarGraficoMercado() {
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
		mayor = 0;
		menor = Double.POSITIVE_INFINITY;
		try {
			series.getData().clear();
			series.setName("Market Chart");
			for (int i = listaObservableHistorialMercado.size() - 1; i >= 0; i -= 2) {
				Date fecha = new Date();
				double precio;
				if (!listaObservableHistorialMercado.get(i).getTimeStamp().contains("."))
					listaObservableHistorialMercado.get(i)
							.setTimeStamp(listaObservableHistorialMercado.get(i).getTimeStamp() + ".000");
				fecha = formatoFecha
						.parse(listaObservableHistorialMercado.get(i).getTimeStamp().replaceFirst("T", " "));
				precio = listaObservableHistorialMercado.get(i).getPrice() * SATOSHI;
				if (precio > mayor)
					mayor = precio;
				if (precio < menor)
					menor = precio;
				// populating the series with data
				series.getData().add(new Data<String, Number>(formatoHora.format(fecha), precio));
			}
			// for (Data<String, Number> dato : series.getData()) {
			// System.out.println(dato.getXValue() + " -> " + dato.getYValue());
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void mostrarGraficoMercado() {
		try {
			graficoMercado.setCreateSymbols(false);
			yAxis.setAutoRanging(false);
			yAxis.setLowerBound(menor);
			yAxis.setUpperBound(mayor);
			yAxis.setTickUnit((mayor - menor) / 10);
			if(graficoMercado.getData().size() == 0)
				graficoMercado.getData().add(series);
			graficoMercado.setLegendVisible(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Double getDisponible(String moneda) {
		for (Cartera cartera : listaObservableCarteras) {
			if (cartera.getCurrency().equals(moneda)) {
				return cartera.getAvailable();
			}
		}
		return 0d;
	}

	private void calcularCompra() {
		if (textoCantidadCompra.getText() != null && textoPrecioCompra.getText() != null) {
			double cantidad = Double.parseDouble(textoCantidadCompra.getText());
			double precio = Double.parseDouble(textoPrecioCompra.getText());
			double total = cantidad * precio;
			total += total * Constantes.BITTREX_FEE;
			textoTotalCompra.setText(String.format(SATOSHI_DECIMAL_FORMAT, total));
		}
	}

	public double calcularMaximoCompra() {
		double cantidad = 0;
		if (textoPrecioCompra.getText() != null) {
			double precio = Double.parseDouble(textoPrecioCompra.getText());
			double total = Double.parseDouble(labelDisponibleCompra.getText());
			cantidad = total / precio;
			cantidad -= cantidad * Constantes.BITTREX_FEE;
			textoCantidadCompra.setText(String.format(SATOSHI_DECIMAL_FORMAT, cantidad));
			textoTotalCompra.setText(String.format(SATOSHI_DECIMAL_FORMAT, total));
		}
		return cantidad;
	}

	private void calcularVenta() {
		if (textoCantidadVenta.getText() != null && textoPrecioVenta.getText() != null) {
			double cantidad = Double.parseDouble(textoCantidadVenta.getText());
			double precio = Double.parseDouble(textoPrecioVenta.getText());
			double total = cantidad * precio;
			total -= total * Constantes.BITTREX_FEE;
			textoTotalVenta.setText(String.format(SATOSHI_DECIMAL_FORMAT, total));
		}
	}

	private void calcularMaximoVenta() {
		if (textoPrecioVenta.getText() != null) {
			double cantidad = Double.parseDouble(labelDisponibleVenta.getText());
			double precio = Double.parseDouble(textoPrecioVenta.getText());
			double total = cantidad * precio;
			total -= total * Constantes.BITTREX_FEE;
			textoCantidadVenta.setText(String.format(SATOSHI_DECIMAL_FORMAT, cantidad));
			textoTotalVenta.setText(String.format(SATOSHI_DECIMAL_FORMAT, total));
		}
	}
	
	public double getPrecioActual(String tipo) {
		double precioActual = 0;
		switch(tipo) {
		case "Bid": precioActual = tablaOrdenesCompra.getItems().get(0).getRate(); break;
		case "Ask": precioActual = tablaOrdenesVenta.getItems().get(0).getRate(); break;
		}
		return precioActual;
	}
	
	public boolean transferirDinero() {
		boolean resultado = false;
		String moneda = null;
		String cantidad = null;
		String direccion = null;
		if(carteraActiva != null) {
			moneda = carteraActiva.getCurrency();
			cantidad = String.valueOf(carteraActiva.getAvailable());
			switch(moneda) {
			case "EGC": direccion = "EcchgQUuHBmQXmQM2hUSVVkaXrGUNd496R"; break;
			}
		} else return false;
		JSONObject objetoJSON = null;
		if(moneda != null && cantidad != null && direccion != null)
			objetoJSON = servicioJSON.leerJSON(servicioBittrex.withdraw(moneda, cantidad, direccion, null));
		if(objetoJSON != null)
			resultado = (Boolean) objetoJSON.get("success");
		return resultado;
	}

}
