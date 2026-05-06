package com.example.ondecomer.model;

public class Restaurante {
    private int id;
    private String nome;
    private String resumo;
    private float nota;
    private String categoria;
    private String usuarioId;

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public Restaurante(int id, String nome, String resumo, float nota, String categoria, String usuarioId) {
        this.id = id;
        this.nome = nome;
        this.resumo = resumo;
        this.nota = nota;
        this.categoria = categoria;
        this.usuarioId = usuarioId;
    }


}
