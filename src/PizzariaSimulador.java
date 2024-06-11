import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PizzariaSimulador {
    private Queue<Pedido> filaDePedidos = new LinkedList<>();
    private Nodo raizABP = null;
    private int tempoTotal = 0;
    private int totalPedidos = 0;
    private Pedido pedidoMaisDemorado = null;

    public Pedido[] lerArquivoCSV(String caminho) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(caminho));
        String linha;
        Pedido[] pedidos = new Pedido[100];
        int index = 0;
        while((linha = br.readLine()) != null){
            String[] dados = linha.split(",");
            int codigo = Integer.parseInt(dados[0]);
            String sabor = dados[1];
            int instante = Integer.parseInt(dados[2]);
            int tempoPreparo = Integer.parseInt(dados[3]);
            pedidos[index++] = new Pedido(codigo, sabor, instante, tempoPreparo);
        }
        br.close();
        return pedidos;
    }

    public void processarSimulacao(Pedido[] pedidos){
        int tempo = 0;
        Pedido pedidoAtual = null;
        int tempoPreparoRestante = 0;
        int indexPedidos = 0;


        while(indexPedidos < pedidos.length || !filaDePedidos.isEmpty() || pedidoAtual != null){
            while(indexPedidos < pedidos.length && pedidos[indexPedidos] != null && pedidos[indexPedidos].instante == tempo){
                filaDePedidos.add(pedidos[indexPedidos++]);
            }
            if(pedidoAtual == null && !filaDePedidos.isEmpty()){
                pedidoAtual = filaDePedidos.poll();
                tempoPreparoRestante = pedidoAtual.tempoPreparo;
            }

            if(pedidoAtual !=null){
                tempoPreparoRestante--;
                if(tempoPreparoRestante == 0){
                    inserirNaArvore(pedidoAtual);
                    totalPedidos++;
                    if(pedidoMaisDemorado == null || pedidoAtual.tempoPreparo > pedidoMaisDemorado.tempoPreparo){
                        pedidoMaisDemorado = pedidoAtual;
                    }
                    pedidoAtual = null;
                }
            }  
            tempo++;
        }
        tempoTotal = tempo;
    }

    private void inserirNaArvore(Pedido pedido){
        raizABP = inserirNaArvoreRec(raizABP,pedido);
    }

    private Nodo inserirNaArvoreRec(Nodo raiz, Pedido pedido){
        if(raiz == null){
            return new Nodo(pedido);
        }   
        if(pedido.codigo < raiz.pedido.codigo){
            raiz.esquerda = inserirNaArvoreRec(raiz.esquerda, pedido);
        }else if(pedido.codigo > raiz.pedido.codigo){
            raiz.direita = inserirNaArvoreRec(raiz.direita, pedido);
        }
        return raiz;
    }
    private void gerarCSVsituacaoFila() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("situacao_fila.csv"));
        bw.write("Instante,Pedidos Em Espera,Pedido Em Produção,Pedidos Prontos\n");

        bw.close();
    }
    public void gerarResultados() throws IOException{
        System.out.println("total de pedidos processados:" + totalPedidos);
        System.out.println("total de tempo executado" + tempoTotal);
        System.out.println("pedido mais demorado" + (pedidoMaisDemorado != null ? pedidoMaisDemorado.codigo : "nenhum"));


        gerarCSVsituacaoFila();
        gerarCSVArvore();
    }
    private void gerarCSVArvore() throws IOException{
        BufferedWriter bw = new BufferedWriter(new  FileWriter("Arvore.csv"));
        inOrder(raizABP, bw);
        bw.close();
    }

    private void inOrder(Nodo nodo, BufferedWriter bw) throws IOException{
        if(nodo != null){
            inOrder(nodo.esquerda, bw);
            bw.write(nodo.pedido.codigo + ",");
            inOrder(nodo.direita, bw);
        }
    }

}