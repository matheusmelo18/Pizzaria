import java.io.*;
import java.util.*;

class Pedido {
    int codigo;
    String sabor;
    int instante;
    int tempoPreparo;

    public Pedido(int codigo, String sabor, int instante, int tempoPreparo) {
        this.codigo = codigo;
        this.sabor = sabor;
        this.instante = instante;
        this.tempoPreparo = tempoPreparo;
    }
}

class Nodo {
    Pedido pedido;
    Nodo esquerda;
    Nodo direita;

    public Nodo(Pedido pedido) {
        this.pedido = pedido;
    }
}