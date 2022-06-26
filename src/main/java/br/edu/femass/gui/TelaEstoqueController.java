package br.edu.femass.gui;

import br.edu.femass.LojaDiscos;
import br.edu.femass.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static br.edu.femass.LojaDiscos.*;


public class TelaEstoqueController implements Initializable {

    Disco discoSelecionado;

    //List Views
    @FXML
    private ListView<Disco> LstDiscos;

    //Botões

    @FXML
    private Button BtnRetornar;




    //Text Fields
    @FXML
    private TextField TxtPreco;

    @FXML
    private TextField TxtAno;

    @FXML
    private TextField TxtGenero;

    //Labels
    @FXML
    private Label LblEstoque;

    @FXML
    private Label LblTituloEstoque;

    //Métodos Auxiliares
    private void atualizarLista(){
        Set<Disco> discos = null;

        try {
            discos = discoDao.listar();
        } catch (Exception e) {
            e.printStackTrace();
            discos = new HashSet<>();
        }

        ObservableList<Disco> discosOb = FXCollections.observableArrayList(discos);
        LstDiscos.setItems(discosOb);
    }

    private void selecionaDisco(){
        discoSelecionado = LstDiscos.getSelectionModel().getSelectedItem();
        TxtPreco.setText(discoSelecionado.getPrecoVenda().toString());
        TxtAno.setText(discoSelecionado.getAno().toString());
        TxtGenero.setText(discoSelecionado.getGenero().getNome());
        LblTituloEstoque.setFont(new Font("Arial", 39));
        LblTituloEstoque.setText("Estoque:");
        LblEstoque.setStyle("-fx-font-family: 'serif'");
        LblEstoque.setText(discoSelecionado.getEstoque().toString());
    }

    //Actions

    @FXML
    private void BtnRetornar_Action(ActionEvent event) {
        LojaDiscos.irTelaPrincipal();
    }

    @FXML
    private void LstDiscos_KeyPressed(KeyEvent event){
        selecionaDisco();
    }

    @FXML
    private void LstDiscos_MouseClicked(MouseEvent event){
        selecionaDisco();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }
}
