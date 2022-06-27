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


public class CadArtistaController implements Initializable {

    private Disco discoSelecionado;
    private Artista artistaSelecionado;

    //List Views
    @FXML
    private ListView<Disco> LstDiscos;

    @FXML
    private ListView<Artista> LstArtistas;

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
    private TextField TxtArtista;

    @FXML
    private TextField TxtAno;

    @FXML
    private TextField TxtGenero;

    //Métodos Auxiliares

    private void limparTela(){
        TxtTitulo.setText("");
        TxtArtista.setText("");
        TxtAno.setText("");
        TxtGenero.setText("");
    }

    private void habilitarInterface(Boolean incluir){

        TxtArtista.setDisable(!incluir);
        TxtArtista.setEditable(incluir);

        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        BtnIncluir.setDisable(incluir);
        BtnExcluir.setDisable(incluir);
        BtnRetornar.setDisable(incluir);

        LstArtistas.setDisable(incluir);
        LstDiscos.setDisable(incluir);
    }

    public void atualizarLista(){
        Set<Artista> artistas = new HashSet<>();
        try {
            artistas = artistaDao.listar();
        } catch (Exception e){
            e.printStackTrace();
        }
        ObservableList<Artista> artistasOb = FXCollections.observableArrayList(artistas);
        LstArtistas.setItems(artistasOb);
        ObservableList<Disco> discosOb = FXCollections.observableArrayList();
        LstDiscos.setItems(discosOb);
    }

    private void  exibirDisco(){
        discoSelecionado = LstDiscos.getSelectionModel().getSelectedItem();
        if (discoSelecionado==null) return;
        TxtTitulo.setText(discoSelecionado.getTitulo());
        TxtAno.setText(discoSelecionado.getAno().toString());
        TxtGenero.setText(discoSelecionado.getGenero().getNome());
        ativaBtnAlterar(true);
    }

    private void  exibirArtista(){
        artistaSelecionado = LstArtistas.getSelectionModel().getSelectedItem();
        if (artistaSelecionado==null) return;
        TxtArtista.setText(artistaSelecionado.getNome());
        ativaBtnAlterar(true);
        Set<Disco> discos = new HashSet<>();
        Set<Disco> discosDoArtista = new HashSet<>();
        try{
            discos = discoDao.listar();
        }catch (Exception e){
            e.printStackTrace();
        }
        for (Disco disco : discos){
            if (disco.getArtista().equals(artistaSelecionado)) discosDoArtista.add(disco);
        }
        ObservableList<Disco> discosOb = FXCollections.observableArrayList(discosDoArtista);
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

    public Boolean artistaExistente(String nome){
        Set<Artista> artistas = new HashSet<>();
        Artista novo = new Artista();
        novo.setNome(nome);
        try {
            artistas = artistaDao.listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Artista artista: artistas){
            if (novo.equals(artista)) return true;
        }
        return false;
    }

    public void incluir_ou_alterar_artista(Artista artista, String tipo){
        artista.setNome(TxtArtista.getText());
        try {
            if (tipo=="inserir"){
                if (artistaExistente(artista.getNome())) return;
                artistaDao.gravar(artista);
            }
            else if (tipo=="alterar") {
                artistaDao.alterar(artista);
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
        TxtArtista.requestFocus();
        ativaBtnAlterar(false);
    }

    @FXML
    private void BtnAlterar_Action(ActionEvent event) {
        atualizarLista();
        habilitarInterface(true);
        TxtArtista.requestFocus();
        ativaBtnAlterar(false);
        ativaBtnConfirmarAlteracao(true);
    }

    @FXML
    private void BtnConfirmarAlteracao_Action(ActionEvent event) {
        incluir_ou_alterar_artista(artistaSelecionado, "alterar");
        atualizarLista();
        ativaBtnConfirmarAlteracao(false);
        habilitarInterface(false);
    }

    @FXML
    private void BtnExcluir_Action(ActionEvent event) {
        artistaSelecionado = LstArtistas.getSelectionModel().getSelectedItem();
        if (artistaSelecionado==null) return;
        try {
            artistaDao.excluir(artistaSelecionado);
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
        Artista artista = new Artista();
        incluir_ou_alterar_artista(artista, "inserir");
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
    private void LstArtistas_KeyPressed(KeyEvent event){
        exibirArtista();
    }

    @FXML
    private void LstArtistas_MouseClicked(MouseEvent event){
        exibirArtista();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }
}
