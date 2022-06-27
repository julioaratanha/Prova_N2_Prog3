package br.edu.femass.gui;

import br.edu.femass.LojaDiscos;
import br.edu.femass.model.Artista;
import br.edu.femass.model.Disco;
import br.edu.femass.model.Gênero;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static br.edu.femass.LojaDiscos.*;


public class CadGeneroController implements Initializable {

    private Disco discoSelecionado;
    private Gênero generoSelecionado;

    //List Views
    @FXML
    private ListView<Disco> LstDiscos;

    @FXML
    private ListView<Gênero> LstGeneros;

    //Botões
    @FXML
    private Button BtnIncluir;

    @FXML
    private Button BtnAlterar;

    @FXML
    private Button BtnExcluir;

    @FXML
    private Button BtnGravar;

    @FXML
    private Button BtnCancelar;

    @FXML
    private Button BtnRetornar;

    @FXML
    private Button BtnConfirmarAlteracao;

    //Text Fields

    @FXML
    private TextField TxtTitulo;

    @FXML
    private TextField TxtGenero;

    @FXML
    private TextField TxtAno;

    @FXML
    private TextField TxtArtista;

    //Métodos Auxiliares

    private void limparTela(){
        TxtTitulo.setText("");
        TxtArtista.setText("");
        TxtAno.setText("");
        TxtGenero.setText("");
    }

    private void habilitarInterface(Boolean incluir){

        TxtGenero.setDisable(!incluir);
        TxtGenero.setEditable(incluir);

        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        BtnIncluir.setDisable(incluir);
        BtnExcluir.setDisable(incluir);
        BtnRetornar.setDisable(incluir);

        LstGeneros.setDisable(incluir);
        LstDiscos.setDisable(incluir);
    }

    public void atualizarLista(){
        Set<Gênero> generos = new HashSet<>();
        try {
            generos = generoDao.listar();
        } catch (Exception e){
            e.printStackTrace();
        }
        ObservableList<Gênero> generosOb = FXCollections.observableArrayList(generos);
        LstGeneros.setItems(generosOb);
        ObservableList<Disco> discosOb = FXCollections.observableArrayList();
        LstDiscos.setItems(discosOb);
    }

    private void  exibirDisco(){
        discoSelecionado = LstDiscos.getSelectionModel().getSelectedItem();
        if (discoSelecionado==null) return;
        TxtTitulo.setText(discoSelecionado.getTitulo());
        TxtAno.setText(discoSelecionado.getAno().toString());
        TxtArtista.setText(discoSelecionado.getArtista().getNome());
        ativaBtnAlterar(true);
    }

    private void  exibirGenero(){
        generoSelecionado = LstGeneros.getSelectionModel().getSelectedItem();
        if (generoSelecionado==null) return;
        TxtGenero.setText(generoSelecionado.getNome());
        ativaBtnAlterar(true);
        Set<Disco> discos = new HashSet<>();
        Set<Disco> discosDoGenero = new HashSet<>();
        try{
            discos = discoDao.listar();
        }catch (Exception e){
            e.printStackTrace();
        }
        for (Disco disco : discos){
            if (disco.getGenero().equals(generoSelecionado)) discosDoGenero.add(disco);
        }
        ObservableList<Disco> discosOb = FXCollections.observableArrayList(discosDoGenero);
        LstDiscos.setItems(discosOb);
    }



    private void ativaBtnAlterar(boolean ativa){
        BtnAlterar.setVisible(ativa);
        BtnAlterar.setDisable(!ativa);
    }

    private void ativaBtnConfirmarAlteracao(boolean ativa){
        atualizarLista();
        BtnConfirmarAlteracao.setVisible(ativa);
        BtnConfirmarAlteracao.setDisable(!ativa);
        BtnGravar.setVisible(!ativa);
        BtnGravar.setDisable(ativa);
    }

    public Boolean generoExistente(String nome){
        Set<Gênero> generos = new HashSet<>();
        Gênero novo = new Gênero();
        novo.setNome(nome);
        try {
            generos = generoDao.listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Gênero genero: generos){
            if (novo.equals(genero)) return true;
        }
        return false;
    }

    public void incluir_ou_alterar_genero(Gênero genero, String tipo){
        genero.setNome(TxtGenero.getText());
        try {
            if (tipo=="inserir"){
                if (generoExistente(genero.getNome())) return;
                generoDao.gravar(genero);
            }
            else if (tipo=="alterar") {
                generoDao.alterar(genero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Actions
    @FXML
    private void BtnIncluir_Action(ActionEvent event) {
        atualizarLista();
        habilitarInterface(true);
        limparTela();
        TxtGenero.requestFocus();
        ativaBtnAlterar(false);
    }

    @FXML
    private void BtnAlterar_Action(ActionEvent event) {
        atualizarLista();
        habilitarInterface(true);
        TxtGenero.requestFocus();
        ativaBtnAlterar(false);
        ativaBtnConfirmarAlteracao(true);
    }

    @FXML
    private void BtnConfirmarAlteracao_Action(ActionEvent event) {
        incluir_ou_alterar_genero(generoSelecionado, "alterar");
        atualizarLista();
        ativaBtnConfirmarAlteracao(false);
        habilitarInterface(false);
    }

    @FXML
    private void BtnExcluir_Action(ActionEvent event) {
        generoSelecionado = LstGeneros.getSelectionModel().getSelectedItem();
        if (generoSelecionado==null) return;
        try {
            generoDao.excluir(generoSelecionado);
        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            DialogPane dp = errorAlert.getDialogPane();
            dp.setStyle("-fx-font-family: 'serif'");
            errorAlert.setContentText(e.getMessage());
            errorAlert.show();
        }
        atualizarLista();
        limparTela();
        ativaBtnAlterar(false);
    }

    @FXML
    private void BtnGravar_Action(ActionEvent event) {
        Gênero genero = new Gênero();
        incluir_ou_alterar_genero(genero, "inserir");
        atualizarLista();
        habilitarInterface(false);
        limparTela();
    }

    @FXML
    private void BtnCancelar_Action(ActionEvent event) {
        ativaBtnConfirmarAlteracao(false);
        habilitarInterface(false);
        limparTela();
    }

    @FXML
    private void BtnRetornar_Action(ActionEvent event) {
        LojaDiscos.irTelaPrincipal();
    }

    @FXML
    private void LstDiscos_KeyPressed(KeyEvent event){
        exibirDisco();
    }

    @FXML
    private void LstDiscos_MouseClicked(MouseEvent event){
        exibirDisco();
    }

    @FXML
    private void LstGeneros_KeyPressed(KeyEvent event){
        exibirGenero();
    }

    @FXML
    private void LstGeneros_MouseClicked(MouseEvent event){
        exibirGenero();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }
}
