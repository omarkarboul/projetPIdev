/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import dbconnection.Dbconnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Activite;

/**
 * FXML Controller class
 *
 * @author karbo
 */
public class TableViewController implements Initializable {

    @FXML
    private TableColumn<Activite, String> idCol;
    @FXML
    private TableColumn<Activite, String> nomCol;
    @FXML
    private TableColumn<Activite, String> typeCol;
    @FXML
    private TableColumn<Activite, String> dureeCol;
    @FXML
    private TableView<Activite> activiteTable;
    @FXML
    private TableColumn<Activite, String> editCol;
    
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Activite activite = null ;
    
    ObservableList<Activite>  activiteList = FXCollections.observableArrayList();
    @FXML
    private AnchorPane chercherTid;
    @FXML
    private TextField searchFid;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        loaddate();
    }    

    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void getAddreclamation(MouseEvent event) {
        //activite non pas reclamation
         try {
            Parent parent = FXMLLoader.load(getClass().getResource("/GUI/Addactivite.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void refresh() {
        
        try {
            activiteList.clear();
            
            query = "SELECT * FROM `activite`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                activiteList.add(new  Activite(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("type"),
                        resultSet.getFloat("duree")));
                activiteTable.setItems(activiteList);
                
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loaddate() {
        
        connection = Dbconnection.getInstance().getCnx();
        refresh();
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dureeCol.setCellValueFactory(new PropertyValueFactory<>("duree"));
        
        
        
        //add cell of button edit 
         Callback<TableColumn<Activite, String>, TableCell<Activite, String>> cellFoctory = (TableColumn<Activite, String> param) -> {
            // make cell containing buttons
            final TableCell<Activite, String> cell = new TableCell<Activite, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#00E676;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            try {
                                activite = activiteTable.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `activite` WHERE id  ="+activite.getId();
   
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refresh();
                                
                            } catch (SQLException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                           

                          

                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            activite = activiteTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/GUI/Addactivite.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            AddactiviteController addactiviteController = loader.getController();
                            addactiviteController.setUpdate(true);
                            addactiviteController.setTextField(activite.getId(), activite.getNom(), 
                                    activite.getType(), activite.getDuree());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                            

                           

                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
         editCol.setCellFactory(cellFoctory);
         activiteTable.setItems(activiteList);
         
         
 }

    @FXML
    private void sort(MouseEvent event) {
        
        try {
            activiteList.clear();
            
            query = "SELECT * FROM `activite` ORDER BY nom  ";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                activiteList.add(new  Activite(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("type"),
                        resultSet.getFloat("duree")));
                activiteTable.setItems(activiteList);
                
            }
            System.out.println("sort execute");
            
            
        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void chercher(MouseEvent event) {
        
        try {
            activiteList.clear();
            
            query = "SELECT * FROM `activite` WHERE (nom LIKE ?)";
            preparedStatement = connection.prepareStatement(query);
            String x = "%";
            String r = x+searchFid.getText()+x;
            preparedStatement.setString(1, r);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                activiteList.add(new  Activite(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("type"),
                        resultSet.getFloat("duree")));
                activiteTable.setItems(activiteList);
                
            }
            System.out.println("search execute");
            
            
        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void getreclamations(MouseEvent event) {
        
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/GUI/reclamation.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

        
        
        
    
    
}
