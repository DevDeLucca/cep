package cep.src;

public class Pessoa {

    private String nome;
    private String sobrenome;
    private String cep;

    public Pessoa(String nome, String sobrenome, String cep) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cep = cep;
    }

    public String getCep() {
        return cep;
    }
}
