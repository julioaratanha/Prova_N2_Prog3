package br.edu.femass.gui;

import br.edu.femass.LojaDiscos;
import br.edu.femass.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static br.edu.femass.LojaDiscos.*;


public class CadDiscoController implements Initializable {

    private Disco discoSelecionado;

    //List Views
    @FXML
    private ListView<Disco> LstDiscos;

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

    @FXML
    private TextField TxtEstoque;

    @FXML
    private TextField TxtPreco;

    //Métodos Auxiliares

    private void limparTela(){
        TxtTitulo.setText("");
        TxtArtista.setText("");
        TxtAno.setText("");
        TxtGenero.setText("");
        TxtEstoque.setText("");
        TxtPreco.setText("");
    }

    private void habilitarInterface(Boolean incluir){
        TxtTitulo.setDisable(!incluir);
        TxtTitulo.setEditable(incluir);

        TxtArtista.setDisable(!incluir);
        TxtArtista.setEditable(incluir);

        TxtAno.setDisable(!incluir);
        TxtAno.setEditable(incluir);

        TxtGenero.setDisable(!incluir);
        TxtGenero.setEditable(incluir);

        TxtPreco.setDisable(!incluir);
        TxtPreco.setEditable(incluir);

        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        BtnIncluir.setDisable(incluir);
        BtnExcluir.setDisable(incluir);
        BtnRetornar.setDisable(incluir);
        LstDiscos.setDisable(incluir);
    }

    public void atualizarLista(){
        Set<Disco> discos = new HashSet<>();
        try {
            discos = discoDao.listar();
        } catch (Exception e){
            e.printStackTrace();
        }
        ObservableList<Disco> discosOb = FXCollections.observableArrayList(discos);
        LstDiscos.setItems(discosOb);
    }

    private void  exibirDisco(){
        discoSelecionado = LstDiscos.getSelectionModel().getSelectedItem();
        if (discoSelecionado==null) return;
        TxtTitulo.setText(discoSelecionado.getTitulo());
        TxtArtista.setText(discoSelecionado.getArtista().getNome());
        TxtAno.setText(discoSelecionado.getAno().toString());
        TxtGenero.setText(discoSelecionado.getGenero().getNome());
        TxtEstoque.setText(discoSelecionado.getEstoque().toString());
        TxtPreco.setText(discoSelecionado.getPrecoVenda().toString());
        ativaBtnAlterar(true);
    }

    private void ativaBtnAlterar(boolean ativa){
        BtnAlterar.setVisible(ativa);
        BtnAlterar.setDisable(!ativa);
    }

    private void ativaBtnConfirmarAlteracao(boolean ativa){
        BtnConfirmarAlteracao.setVisible(ativa);
        BtnConfirmarAlteracao.setDisable(!ativa);
        BtnGravar.setVisible(!ativa);
        BtnGravar.setDisable(ativa);
    }

    public Gênero verificaGeneroExistente(String nome){
        Set<Gênero> generos = new HashSet<>();
        Gênero novo = new Gênero();
        novo.setNome(nome);
        try {
            generos = generoDao.listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Gênero genero: generos){
            if (novo.equals(genero)) {
                novo.setId(genero.getId());
                return novo;
            }
        }
        try {
            generoDao.gravar(novo);
            generos = generoDao.listar();
            for (Gênero genero: generos){
                if (novo.equals(genero)) {
                    novo.setId(genero.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return novo;
    }

    public Artista verificaArtistaExistente(String nome){
        Set<Artista> artistas = new HashSet<>();
        Artista novo = new Artista();
        novo.setNome(nome);
        try {
            artistas = artistaDao.listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Artista artista: artistas){
            if (novo.equals(artista)) {
                novo.setId(artista.getId());
                return novo;
            }
        }
        try {
            artistaDao.gravar(novo);
            artistas = artistaDao.listar();
            for (Artista artista: artistas){
                if (novo.equals(artista)) {
                    novo.setId(artista.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return novo;
    }

    public void incluir_ou_alterar_disco(Disco disco, String tipo){
        disco.setTitulo(TxtTitulo.getText());
        disco.setPrecoVenda(Double.parseDouble(TxtPreco.getText()));
        disco.setAno(Integer.parseInt(TxtAno.getText()));
        disco.setGenero(verificaGeneroExistente(TxtGenero.getText()));
        disco.setArtista(verificaArtistaExistente(TxtArtista.getText()));
        try {
            if (tipo=="inserir") discoDao.gravar(disco);
            else if (tipo=="alterar") {
                discoDao.alterar(disco);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.show();
        }

    }

    //Actions
    @FXML
    private void BtnIncluir_Action(ActionEvent event) {
        atualizarLista();
        habilitarInterface(true);
        limparTela();
        TxtTitulo.requestFocus();
        ativaBtnAlterar(false);
    }

    @FXML
    private void BtnAlterar_Action(ActionEvent event) {
        atualizarLista();
        habilitarInterface(true);
        TxtTitulo.requestFocus();
        ativaBtnAlterar(false);
        ativaBtnConfirmarAlteracao(true);
    }

    @FXML
    private void BtnConfirmarAlteracao_Action(ActionEvent event) {
        incluir_ou_alterar_disco(discoSelecionado, "alterar");
        atualizarLista();
        ativaBtnConfirmarAlteracao(false);
        habilitarInterface(false);
    }

    @FXML
    private void BtnExcluir_Action(ActionEvent event) {
        discoSelecionado = LstDiscos.getSelectionModel().getSelectedItem();
        if (discoSelecionado==null) return;
        try {
            discoDao.excluir(discoSelecionado);
        } catch (Exception e) {
            e.printStackTrace();
        }
        atualizarLista();
        limparTela();
        ativaBtnAlterar(false);
    }

    @FXML
    private void BtnGravar_Action(ActionEvent event) {
        Disco disco = new Disco();
        incluir_ou_alterar_disco(disco, "inserir");
        atualizarLista();
        habilitarInterface(false);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }
}
