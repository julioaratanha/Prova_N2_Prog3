package br.edu.femass;

import br.edu.femass.dao.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class LojaDiscos extends Application {

    private static Stage stage;
    public static ArtistaDao artistaDao;
    public static PessoaDao pessoaDao;
    public static GeneroDao generoDao;
    public static DiscoDao discoDao;
    public static CompraDao compraDao;
    public static VendaDao vendaDao;


    @Override
    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage;
        this.artistaDao = new ArtistaDao();
        this.pessoaDao = new PessoaDao();
        this.generoDao = new GeneroDao();
        this.discoDao = new DiscoDao();
        this.compraDao = new CompraDao();
        this.vendaDao = new VendaDao();
        DaoPostgres.getConexao();
        irTelaPrincipal();
    }

    private static void trocarTela(String arquivoFXML, String titulo, Integer largura, Integer altura ){
        FXMLLoader loader = new FXMLLoader(LojaDiscos.class.getResource(arquivoFXML));
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), largura, altura);
            scene.getRoot().setStyle("-fx-font-family: 'serif'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle(titulo);
        stage.setScene(scene);
        stage.show();
    }

    public static void irTelaPrincipal(){
        trocarTela("/fxml/Inicial_View.fxml", "Loja de Discos", 400, 400);
    }

    public static void irTelaCadastroPessoas(){
        trocarTela("/fxml/CadPessoa_View.fxml", "Cadastro de Clientes e Fornecedores", 786, 600);
    }

    public static void irTelaCadastroDisco(){
        trocarTela("/fxml/CadDisco_View.fxml", "Cadastro de Discos", 786, 600);
    }

    public static void irTelaCadastroArtista(){
        trocarTela("/fxml/CadArtista_View.fxml", "Cadastro de Artistas", 786, 600);
    }

    public static void irTelaCadastroGenero(){
        trocarTela("/fxml/CadGenero_View.fxml", "Cadastro de Artistas", 786, 600);
    }

    public static void irTelaCadastroCompra(){
        trocarTela("/fxml/CadCompra_View.fxml", "Cadastro de Compras", 1250, 600);
    }

    public static void irTelaCadastroVenda(){
        trocarTela("/fxml/CadVenda_View.fxml", "Cadastro de Vendas", 1250, 600);
    }

    public static void irTelaEstoque(){
        trocarTela("/fxml/TelaEstoque_View.fxml", "Estoque", 786, 600);
    }

    public static void irTelaFechamentoDiario(){
        trocarTela("/fxml/TelaFechamentoDiario_View.fxml", "Estoque", 1250, 650);
    }
    //Main
    public static void main(String[] args) {
        launch(args);
    }

}
