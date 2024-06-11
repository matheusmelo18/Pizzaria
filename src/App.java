import java.io.IOException;

public class App {
    public static void main(String[] args) {
        PizzariaSimulador simulador = new PizzariaSimulador();
        try {
            Pedido[] pedidos = simulador.lerArquivoCSV("pedidos.csv");
            simulador.processarSimulacao(pedidos);
            simulador.gerarResultados();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
