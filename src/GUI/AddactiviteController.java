/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import dbconnection.Dbconnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.Activite;

/**
 * FXML Controller class
 *
 * @author karbo
 */
public class AddactiviteController implements Initializable {

    @FXML
    private TextField nomFid;
    @FXML
    private TextField typeFid;
    @FXML
    private TextField dureeFid;
    
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Activite activite = null ;
    private boolean update ;
    int activiteId;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void save(MouseEvent event) {
        
        connection = Dbconnection.getInstance().getCnx();
        String name = nomFid.getText();
        String type = typeFid.getText();
        String duree = dureeFid.getText().toString();

        if (name.isEmpty() || type.isEmpty() || duree.isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Remplir tous les champs !");
            alert.showAndWait();

        } else {
            getQuery();
            insert();
            clean();

        }
    }

    @FXML
    private void clean() {
        nomFid.setText(null);
        typeFid.setText(null);
        dureeFid.setText(null);
    }

    void setUpdate(boolean b) {
        this.update = b;
        
    }

    private void getQuery() {
        if (update == false) {
            
            query = "INSERT INTO `activite`( `nom`, `type`, `duree`) VALUES (?,?,?)";

        }else{
            query = "UPDATE `activite` SET "
                    + "`nom`=?,"
                    + "`type`=?,"
                    + "`duree`= ? WHERE id = '"+activiteId+"'";
        }
        
    }

    private void insert() {
        try {

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nomFid.getText());
            preparedStatement.setString(2, typeFid.getText());
            preparedStatement.setString(3, dureeFid.getText().toString());
            preparedStatement.execute();

        } catch (SQLException ex) {
            Logger.getLogger(AddactiviteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setTextField(int id, String nom, String type, float duree) {
        activiteId = id;
        nomFid.setText(nom);
        typeFid.setText(type);
        dureeFid.setText(Float.toString(duree));
    }
    
   
    
}
