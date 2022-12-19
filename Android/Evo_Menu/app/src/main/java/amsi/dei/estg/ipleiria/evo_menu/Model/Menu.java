package amsi.dei.estg.ipleiria.evo_menu.Model;

import android.widget.ImageView;

import java.io.Serializable;

public class Menu implements Serializable
{
    private int id, id_categoria;
    private String nome;
    private ImageView imagem;
    private double desconto;

    public Menu(int id, String nome, ImageView imagem, double desconto, int id_categoria) {
        this.id = id;
        this.nome = nome;
        this.imagem = imagem;
        this.desconto = desconto;
        this.id_categoria = id_categoria;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ImageView getImagem() {
        return imagem;
    }

    public void setImagem(ImageView imagem) {
        this.imagem = imagem;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }
}
