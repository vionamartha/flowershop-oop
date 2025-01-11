package FlowerShop;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author LENOVO
 * 
 * 
 */
public class DashboardController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Label username;

    @FXML
    private Button home_btn;

    @FXML
    private Button availableFlowers_btn;

    @FXML
    private Button purchase_btn;

    @FXML
    private Button logoutBtn;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_availableFlowers;

    @FXML
    private Label home_totalIncome;

    @FXML
    private Label home_totalCustomers;

    @FXML
    private BarChart<?, ?> home_chart;

    @FXML
    private AnchorPane availableFlowers_form;

    @FXML
    private ImageView availableFlowers_imageView;

    @FXML
    private Button availableFlowers_importBtn;

    @FXML
    private TextField availableFlowers_flowerID;

    @FXML
    private TextField availableFlowers_flowerName;

    @FXML
    private ComboBox<?> availableFlowers_status;

    @FXML
    private Button availableFlowers_addBtn;

    @FXML
    private Button availableFlowers_updateBtn;

    @FXML
    private Button availableFlowers_clearBtn;

    @FXML
    private Button availableFlowers_deleteBtn;

    @FXML
    private TextField availableFlowers_price;

    @FXML
    private TextField availableFlowers_search;

    @FXML
    private TableView<flowersData> availableFlowers_tableView;

    @FXML
    private TableColumn<flowersData, String> availableFlowers_col_flowerID;

    @FXML
    private TableColumn<flowersData, String> availableFlowers_col_flowerName;

    @FXML
    private TableColumn<flowersData, String> availableFlowers_col_status;

    @FXML
    private TableColumn<flowersData, String> availableFlowers_col_price;

    @FXML
    private AnchorPane purchase_form;

    @FXML
    private ComboBox<?> purchase_flowerID;

    @FXML
    private ComboBox<?> purchase_flowerName;

    @FXML
    private Spinner<Integer> purchase_quantity;

    @FXML
    private Button purchase_addCart;

    @FXML
    private Label purchase_total;

    @FXML
    private Button purchase_payBtn;

    @FXML
    private TableView<customerData> purchase_tableView;

    @FXML
    private TableColumn<customerData, String> purchase_col_flowerID;

    @FXML
    private TableColumn<customerData, String> purchase_col_flowerName;

    @FXML
    private TableColumn<customerData, String> purchase_col_quantity;

    @FXML
    private TableColumn<customerData, String> purchase_col_price;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;

    public void homeAF(){
        
        String sql = "SELECT COUNT(id) FROM flowers WHERE status = 'Tersedia'";
        
        connect = database.connectDb();
        
        try{
            int countAF = 0;
            statement = connect.createStatement();
            result = statement.executeQuery(sql);
            
            if(result.next()){
                countAF = result.getInt("COUNT(id)");
            }
            home_availableFlowers.setText(String.valueOf(countAF));
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void homeTI(){
        
        String sql = "SELECT SUM(total) FROM customer_info";
        
        connect = database.connectDb();
        
        try{
            int countTI = 0;
            statement = connect.createStatement();
            result = statement.executeQuery(sql);
            
            if(result.next()){
                countTI = result.getInt("SUM(total)");
            }
            
            home_totalIncome.setText("Rp." + String.valueOf(countTI));
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    
    public void homeTC(){
        
        String sql = "SELECT COUNT(id) FROM customer_info";
        
        connect = database.connectDb();
        
        try{
            int countTC = 0;
            
            statement = connect.createStatement();
            result = statement.executeQuery(sql);
            
            if(result.next()){
                countTC = result.getInt("COUNT(id)");
            }
            home_totalCustomers.setText(String.valueOf(countTC));
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    
    public void homeChart(){
        
        home_chart.getData().clear();
        
        String sql = "SELECT date, SUM(total) FROM customer_info GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 7";
        
        connect = database.connectDb();
        
        try{
            XYChart.Series chart = new XYChart.Series();
            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
            }
            
            home_chart.getData().add(chart);
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    // TAMBAH DATA BUNGA
    public void availableFlowersAdd() {

        String sql = "INSERT INTO flowers (flower_id, name, status, price, image, date) "
                + "VALUES(?,?,?,?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;

            if (availableFlowers_flowerID.getText().isEmpty()
                    || availableFlowers_flowerName.getText().isEmpty()
                    || availableFlowers_status.getSelectionModel().getSelectedItem() == null
                    || availableFlowers_price.getText().isEmpty()
                    || getData.path == null || getData.path == "") {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Harap isi semua kolom yang kosong");
                alert.showAndWait();

            } else {
                // MEMERIKSA APAKAH ID BUNGA SUDAH ADA
                String checkData = "SELECT flower_id FROM flowers WHERE flower_id = '"
                        + availableFlowers_flowerID.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Flower ID: " + availableFlowers_flowerID.getText() + " sudah ada!");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, availableFlowers_flowerID.getText());
                    prepare.setString(2, availableFlowers_flowerName.getText());
                    prepare.setString(3, (String) availableFlowers_status.getSelectionModel().getSelectedItem());
                    prepare.setString(4, availableFlowers_price.getText());

                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");
                    prepare.setString(5, uri);

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(6, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Berhasil Ditambahkan!");
                    alert.showAndWait();

                    // SHOW UPDATED TABLEVIEW
                    availableFlowersShowListData();

                    // CLEAR ALL FIELDS
                    availableFlowersClear();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    // PERBARUI DATA BUNGA
    public void availableFlowersUpdate() {

        String uri = getData.path;
        if (!(uri == null || uri == "")) {
            uri = uri.replace("\\", "\\\\");
        }

        String sql = "UPDATE flowers SET name = '"
                + availableFlowers_flowerName.getText() + "', status = '"
                + availableFlowers_status.getSelectionModel().getSelectedItem() + "', price = '"
                + availableFlowers_price.getText() + "', image = '"
                + uri + "' WHERE flower_id = '" + availableFlowers_flowerID.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;

            if (availableFlowers_flowerID.getText().isEmpty()
                    || availableFlowers_flowerName.getText().isEmpty()
                    || availableFlowers_status.getSelectionModel().getSelectedItem() == null
                    || availableFlowers_price.getText().isEmpty()
                    || uri == null || uri == "" || getData.path == null || getData.path == "") {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Apakah Anda yakin ingin memperbarui ID Bunga: " + availableFlowers_flowerID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Berhasil Diperbarui!");
                    alert.showAndWait();

                    // SHOW UPDATED TABLEVIEW
                    availableFlowersShowListData();

                    // CLEAR ALL FIELDS
                    availableFlowersClear();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    // HAPUS DATA BUNGA
    public void availableFlowersDelete() {

        String sql = "DELETE FROM flowers WHERE flower_id = '"
                + availableFlowers_flowerID.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;

            if (availableFlowers_flowerID.getText().isEmpty()
                    || availableFlowers_flowerName.getText().isEmpty()
                    || availableFlowers_status.getSelectionModel().getSelectedItem() == null
                    || availableFlowers_price.getText().isEmpty()
                    || getData.path == null || getData.path == "") {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Harap isi semua kolom yang kosong:");
                alert.showAndWait();

            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Apakah Anda Yakin Ingin Menghapus: " + availableFlowers_flowerID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Berhasil Dihapus!");
                    alert.showAndWait();

                    // SHOW UPDATED TABLEVIEW
                    availableFlowersShowListData();

                    // CLEAR ALL FIELDS
                    availableFlowersClear();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    // CLEAR DATA BUNGA
    public void availableFlowersClear() {

        availableFlowers_flowerID.setText("");
        availableFlowers_flowerName.setText("");
        availableFlowers_status.getSelectionModel().clearSelection();
        availableFlowers_price.setText("");
        getData.path = "";

        availableFlowers_imageView.setImage(null);

    }

    String listStatus[] = {"Tersedia", "Tidak Tersedia"};

    public void availableFlowersStatus() {

        List<String> listS = new ArrayList<>();

        for (String data : listStatus) {
            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        availableFlowers_status.setItems(listData);

    }

    public void availableFlowersInsertImage() {

        FileChooser open = new FileChooser();
        open.setTitle("Open Image File");
        open.getExtensionFilters().add(new ExtensionFilter("Image File", "*jpg", "*png"));

        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {

            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 116, 139, false, true);
            availableFlowers_imageView.setImage(image);

        }

    }

    public ObservableList<flowersData> availableFlowersListData() {

        ObservableList<flowersData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM flowers";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            flowersData flower;

            while (result.next()) {
                flower = new flowersData(result.getInt("flower_id"),
                         result.getString("name"), result.getString("status"),
                         result.getInt("price"), result.getString("image"),
                         result.getDate("date"));

                listData.add(flower);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<flowersData> availableFlowersList;

    public void availableFlowersShowListData() {
        availableFlowersList = availableFlowersListData();

        availableFlowers_col_flowerID.setCellValueFactory(new PropertyValueFactory<>("flowerId"));
        availableFlowers_col_flowerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        availableFlowers_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        availableFlowers_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        availableFlowers_tableView.setItems(availableFlowersList);
    }

    public void availableFlowersSearch() {

        FilteredList<flowersData> filter = new FilteredList<>(availableFlowersList, e -> true);

        availableFlowers_search.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(PrediateFlowerData -> {

                if (newValue.isEmpty() || newValue == null) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (PrediateFlowerData.getFlowerId().toString().contains(searchKey)) {
                    return true;
                } else if (PrediateFlowerData.getName().toString().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (PrediateFlowerData.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (PrediateFlowerData.getPrice().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });

        });

        SortedList<flowersData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(availableFlowers_tableView.comparatorProperty());

        availableFlowers_tableView.setItems(sortList);

    }

    public void availableFlowersSelect() {

        flowersData flower = availableFlowers_tableView.getSelectionModel().getSelectedItem();
        int num = availableFlowers_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        availableFlowers_flowerID.setText(String.valueOf(flower.getFlowerId()));
        availableFlowers_flowerName.setText(flower.getName());
        availableFlowers_price.setText(String.valueOf(flower.getPrice()));

        getData.path = flower.getImage();

        String uri = "file:" + flower.getImage();

        image = new Image(uri, 116, 139, false, true);
        availableFlowers_imageView.setImage(image);

    }

    public void purchaseAddToCart() {
        purchaseCustomerId();

        String sql = "INSERT INTO customer (customer_id, flower_id, name, quantity, price, date) "
                + "VALUES(?,?,?,?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;

            if (purchase_flowerID.getSelectionModel().getSelectedItem() == null
                    || purchase_flowerName.getSelectionModel().getSelectedItem() == null
                    || qty == 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Harap pilih produk terlebih dahulu");
                alert.showAndWait();
            } else {
                int priceData = 0;
                int totalPrice;
                String checkPrice = "SELECT name, price FROM flowers WHERE name = '"
                        + purchase_flowerName.getSelectionModel().getSelectedItem() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(checkPrice);

                if (result.next()) {
                    priceData = result.getInt("price");
                }

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, String.valueOf(customerId));
                prepare.setInt(2, (Integer) purchase_flowerID.getSelectionModel().getSelectedItem());
                prepare.setString(3, (String) purchase_flowerName.getSelectionModel().getSelectedItem());
                prepare.setString(4, String.valueOf(qty));
                
                totalPrice = (priceData * qty);

                prepare.setString(5, String.valueOf(totalPrice));

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                prepare.setString(6, String.valueOf(sqlDate));
                
                prepare.executeUpdate();
                
                purchaseShowListData();
                purchaseDisplayTotal();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void purchasePay(){
        
        String sql = "INSERT INTO customer_info (customer_id, total, date) VALUES(?,?,?)";
        
        connect = database.connectDb();
        
        try{
            Alert alert;
            
            if(totalP == 0){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Ada yang salah :3");
                alert.showAndWait();
            }else{
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Apakah Anda yakin?");
                Optional<ButtonType> option = alert.showAndWait();
                
                if(option.get().equals(ButtonType.OK)){
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, String.valueOf(customerId));
                    prepare.setString(2, String.valueOf(totalP));

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(3, String.valueOf(sqlDate));

                    prepare.executeUpdate();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Berhasil! Terima kasih atas pembeliannya.");
                    alert.showAndWait();
                    
                    totalP = 0;
                }
            }
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    private int totalP = 0;
    public void purchaseDisplayTotal(){
        purchaseCustomerId();
        String sql = "SELECT SUM(price) FROM customer WHERE customer_id = '"+customerId+"'";
        
        connect = database.connectDb();
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            if(result.next()){
                totalP = result.getInt("SUM(price)");
            }
            
            purchase_total.setText("Rp." + String.valueOf(totalP));
            
        }catch(Exception e){e.printStackTrace();}
        
    }

    public void purchaseFlowerId() {
        String sql = "SELECT status, flower_id FROM flowers WHERE status = 'Tersedia'";

        connect = database.connectDb();

        try {
            ObservableList listData = FXCollections.observableArrayList();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                listData.add(result.getInt("flower_id"));
            }
            purchase_flowerID.setItems(listData);

            purchaseFlowerName();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void purchaseFlowerName() {

        String sql = "SELECT flower_id, name FROM flowers WHERE flower_id = '"
                + purchase_flowerID.getSelectionModel().getSelectedItem() + "'";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("name"));
            }
            purchase_flowerName.setItems(listData);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private SpinnerValueFactory<Integer> spinner;

    public void purchaseSpinner() {
        spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
        purchase_quantity.setValueFactory(spinner);
    }

    private int qty;

    public void purchaseQuantity() {
        qty = purchase_quantity.getValue();
    }

    public ObservableList<customerData> purchaseListData() {
        purchaseCustomerId();

        ObservableList<customerData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM customer WHERE customer_id = '" + customerId + "'";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            customerData customer;

            while (result.next()) {
                customer = new customerData(result.getInt("customer_id"),
                         result.getInt("flower_id"), result.getString("name"),
                         result.getInt("quantity"), result.getInt("price"),
                         result.getDate("date"));

                listData.add(customer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<customerData> purchaseListD;

    public void purchaseShowListData() {
        purchaseListD = purchaseListData();

        purchase_col_flowerID.setCellValueFactory(new PropertyValueFactory<>("flowerId"));
        purchase_col_flowerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        purchase_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        purchase_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        purchase_tableView.setItems(purchaseListD);
    }

    private int customerId;

    public void purchaseCustomerId() {

        String sql = "SELECT MAX(customer_id) FROM customer";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                customerId = result.getInt("MAX(customer_id)");
            }

            int countData = 0;

            String checkInfo = "SELECT MAX(customer_id) FROM customer_info";

            prepare = connect.prepareStatement(checkInfo);
            result = prepare.executeQuery();

            if (result.next()) {
                countData = result.getInt("MAX(customer_id)");
            }

            if (customerId == 0) {
                customerId += 1;
            } else if (customerId == countData) {
                customerId = countData + 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void switchForm(ActionEvent event) {

        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            availableFlowers_form.setVisible(false);
            purchase_form.setVisible(false);

            home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #ACB1E8, #B6DCF2)");
            availableFlowers_btn.setStyle("-fx-background-color: transparent");
            purchase_btn.setStyle("-fx-background-color: transparent");

            homeAF();
            homeTI();
            homeTC();
            homeChart();
            
        } else if (event.getSource() == availableFlowers_btn) {
            home_form.setVisible(false);
            availableFlowers_form.setVisible(true);
            purchase_form.setVisible(false);

            availableFlowers_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #ACB1E8, #B6DCF2)");
            home_btn.setStyle("-fx-background-color: transparent");
            purchase_btn.setStyle("-fx-background-color: transparent");

            // UNTUK MENAMPILKAN TABEL YANG DIPERBARUI SAAT ANDA MENGKLIK TOMBOL AVAILABLE FLOWERS
            availableFlowersShowListData();
            availableFlowersStatus();
            availableFlowersSearch();

        } else if (event.getSource() == purchase_btn) {
            home_form.setVisible(false);
            availableFlowers_form.setVisible(false);
            purchase_form.setVisible(true);

            purchase_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #ACB1E8, #B6DCF2)");
            availableFlowers_btn.setStyle("-fx-background-color: transparent");
            home_btn.setStyle("-fx-background-color: transparent");

            purchaseShowListData();
            purchaseFlowerId();
            purchaseFlowerName();
            purchaseSpinner();
            purchaseDisplayTotal();

        }

    }
    
    private double x = 0;
    private double y = 0;

    public void logout() {

        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin keluar?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                // HIDE DASHBOARD FORM
                logoutBtn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("/FlowerShop/dashboard.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        homeAF();
        homeTI();
        homeTC();
        homeChart();
        
        availableFlowersShowListData();
        availableFlowersStatus();

        purchaseShowListData();
        purchaseFlowerId();
        purchaseFlowerName();
        purchaseSpinner();
        purchaseDisplayTotal();
        
    }

}
