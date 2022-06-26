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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static br.edu.femass.LojaDiscos.*;


public class TelaFechamentoDiarioController implements Initializable {

    Venda venda;
    DetalheVenda detalheVendaSelecionado;
    Compra compra;
    DetalheCompra detalheCompraSelecionado;

    //List Views

    @FXML
    private ListView<Venda> LstVendas;

    @FXML
    private ListView<DetalheVenda> LstDetalheVenda;

    @FXML
    private ListView<Compra> LstCompras;

    @FXML
    private ListView<DetalheCompra> LstDetalheCompra;

    //Botões
    @FXML
    private Button BtnRetornar;

    @FXML
    private Button BtnSelecionaDataHoje;

    @FXML
    private Button BtnSelecionarOutraData;

    //Text Fields
    @FXML
    private TextField TxtData;

    //Labels
    @FXML
    private Label LblTituloVendas;

    @FXML
    private Label LblTituloMenos;

    @FXML
    private Label LblTituloCompras;

    @FXML
    private Label LblTituloIgual;

    @FXML
    private Label LblTituloSaldo;

    @FXML
    private Label LblVendas;

    @FXML
    private Label LblMenos;

    @FXML
    private Label LblCompras;

    @FXML
    private Label LblIgual;

    @FXML
    private Label LblSaldo;

    //Métodos Auxiliares
    private void atualizarLista(Date data){
        Set<Venda> vendas = null;
        Set<Compra> compras = null;
        Set<Venda> vendasDataSelecionada = new HashSet<>();
        Set<Compra> comprasDataSelecionada = new HashSet<>();
        try {
            vendas = vendaDao.listar();
            for (Venda venda : vendas){
                if ((venda.getData().getYear()==data.getYear()) && (venda.getData().getMonth()==data.getMonth()) && (venda.getData().getDay()==data.getDay())) {
                    System.out.println("Data venda: "+venda.getData()+" Data escolhida: "+data);
                    vendasDataSelecionada.add(venda);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            vendas = new HashSet<>();
        }

        ObservableList<Venda> vendasOb = FXCollections.observableArrayList(vendasDataSelecionada);
        LstVendas.setItems(vendasOb);

        try {
            compras = compraDao.listar();
            for (Compra compra : compras){
                if ((compra.getData().getYear()==data.getYear()) && (compra.getData().getMonth()==data.getMonth()) && (compra.getData().getDay()==data.getDay())) {
                    System.out.println("Data compra: "+compra.getData()+" Data escolhida: "+data);
                    comprasDataSelecionada.add(compra);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            compras = new HashSet<>();
        }

        ObservableList<Compra> comprasOb = FXCollections.observableArrayList(comprasDataSelecionada);
        LstCompras.setItems(comprasOb);

        compra=null;
        venda=null;
        atualizaDetalhesVendas();
        atualizaDetalhesCompras();
        SimpleDateFormat formatadorDeData = new SimpleDateFormat("dd/MM/yyyy");
        TxtData.setText(formatadorDeData.format(data));
        Double totalVendas=0.0;
        for (Venda venda : vendasDataSelecionada){
            for (DetalheVenda item : venda.getItens()){
                totalVendas=totalVendas+(item.getQuantidade()*item.getPrecoUnitario());
            }
        }
        Double totalCompras=0.0;
        for (Compra compra : comprasDataSelecionada){
            for (DetalheCompra item : compra.getItens()){
                totalCompras=totalCompras+(item.getQuantidade()*item.getPrecoUnitario());
            }
        }
        Double saldo = totalVendas-totalCompras;
        Font arial = new Font("Arial", 21);
        LblVendas.setFont(arial);
        LblVendas.setText("R$"+totalVendas);

        LblCompras.setFont(arial);
        LblCompras.setText("R$"+totalCompras);

        LblSaldo.setFont(arial);
        LblSaldo.setText("R$"+saldo);

        if (saldo>=0) LblSaldo.setTextFill(Color.color(0,1,0));
        else LblSaldo.setTextFill(Color.color(1,0,0));

    }

    private void atualizaDetalhesVendas(){
        if(venda!=null){
            ObservableList<DetalheVenda> detalhevendasOb = FXCollections.observableArrayList(venda.getItens());
            LstDetalheVenda.setItems(detalhevendasOb);
        }else{
            ObservableList<DetalheVenda> detalhevendasOb = FXCollections.observableArrayList();
            LstDetalheVenda.setItems(detalhevendasOb);
        }
    }

    private void atualizaDetalhesCompras(){
        if(compra!=null){
            ObservableList<DetalheCompra> detalhecomprasOb = FXCollections.observableArrayList(compra.getItens());
            LstDetalheCompra.setItems(detalhecomprasOb);
        }else{
            ObservableList<DetalheCompra> detalhecomprasOb = FXCollections.observableArrayList();
            LstDetalheCompra.setItems(detalhecomprasOb);
        }
    }

    private void selecionaDetalheVenda(){
        detalheVendaSelecionado = LstDetalheVenda.getSelectionModel().getSelectedItem();
    }

    private void selecionaDetalheCompra(){
        detalheCompraSelecionado = LstDetalheCompra.getSelectionModel().getSelectedItem();
    }

    private void selecionaVenda(){
        venda=LstVendas.getSelectionModel().getSelectedItem();
        if (venda==null) return;
        atualizaDetalhesVendas();
    }

    private void selecionaCompra(){
        compra=LstCompras.getSelectionModel().getSelectedItem();
        if (compra==null) return;
        atualizaDetalhesCompras();
    }

    //Actions
    @FXML
    private void BtnRetornar_Action(ActionEvent event) {
        LojaDiscos.irTelaPrincipal();
    }

    @FXML
    private void BtnSelecionaDataHoje_Action(ActionEvent event){
        Date hoje = new Date(System.currentTimeMillis());
        atualizarLista(hoje);
        BtnSelecionaDataHoje.setVisible(false);
        BtnSelecionaDataHoje.setDisable(true);
    }

    @FXML
    private void BtnSelecionarOutraData_Action(ActionEvent event){
        TextInputDialog textoAno = new TextInputDialog();
        DialogPane dp = textoAno.getDialogPane();
        dp.setStyle("-fx-font-family: 'serif'");
        Integer ano;
        do{
            textoAno.setHeaderText("Digite o ano:");
            textoAno.showAndWait();
            String resultado = textoAno.getEditor().getText();
            try{
                ano = Integer.parseInt(resultado);
                ano = ano - 1900;
            }catch(Exception e){
                ano=null;
            }
        }while(ano==null);

        TextInputDialog textoMes = new TextInputDialog();
        dp = textoMes.getDialogPane();
        dp.setStyle("-fx-font-family: 'serif'");
        Integer mes;
        do{
            textoMes.setHeaderText("Digite o mês:");
            textoMes.showAndWait();
            String resultado = textoMes.getEditor().getText();
            try{
                mes = Integer.parseInt(resultado);
                mes--;
            }catch(Exception e){
                mes=null;
            }

        }while((mes==null) || (mes<1) || (mes>12));

        TextInputDialog textoDia = new TextInputDialog();
        dp = textoDia.getDialogPane();
        dp.setStyle("-fx-font-family: 'serif'");
        Integer dia;
        do{
            textoDia.setHeaderText("Digite o dia:");
            textoDia.showAndWait();
            String resultado = textoDia.getEditor().getText();
            try{
                dia = Integer.parseInt(resultado);
            }catch (Exception e){
                dia=null;
            }
        }while((dia==null) | (dia<1) | (dia>31));
        System.out.println("Ano: "+ano+" Mês: "+mes+" Dia: "+dia);
        Date data = new Date(ano, mes, dia);
        atualizarLista(data);
        BtnSelecionaDataHoje.setVisible(true);
        BtnSelecionaDataHoje.setDisable(false);
    }

    @FXML
    private void LstDetalheVenda_KeyPressed(KeyEvent event){
        selecionaDetalheVenda();
    }

    @FXML
    private void LstDetalheVenda_MouseClicked(MouseEvent event){
        selecionaDetalheVenda();
    }

    @FXML
    private void LstVendas_KeyPressed(KeyEvent event){
        selecionaVenda();
    }

    @FXML
    private void LstVendas_MouseClicked(MouseEvent event){
        selecionaVenda();
    }

    @FXML
    private void LstDetalheCompra_KeyPressed(KeyEvent event){
        selecionaDetalheCompra();
    }

    @FXML
    private void LstDetalheCompra_MouseClicked(MouseEvent event){
        selecionaDetalheCompra();
    }

    @FXML
    private void LstCompras_KeyPressed(KeyEvent event){
        selecionaCompra();
    }

    @FXML
    private void LstCompras_MouseClicked(MouseEvent event){
        selecionaCompra();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Font arial = new Font("Arial", 21);
        LblTituloVendas.setFont(arial);
        LblTituloVendas.setText("Vendas");
        LblTituloMenos.setFont(arial);
        LblTituloMenos.setText("-");
        LblTituloCompras.setFont(arial);
        LblTituloCompras.setText("Compras");
        LblTituloIgual.setFont(arial);
        LblTituloIgual.setText("=");
        LblTituloSaldo.setFont(arial);
        LblTituloSaldo.setText("Saldo");
        LblMenos.setFont(arial);
        LblMenos.setText("-");
        LblIgual.setFont(arial);
        LblIgual.setText("=");
        Date hoje = new Date(System.currentTimeMillis());
        atualizarLista(hoje);
    }
}
