package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;

@Entity //essa classe vai criar uma entidade no projeto -> create table
@Table(name = "tb_postagens") //definir o nome da tabela -> tb_postagens

public class Postagem {
	
	@Id // indica que esse atibuto vai ser a chave primária -> primary key (id)
	@GeneratedValue(strategy = GenerationType.IDENTITY)//como vai ser gerada a chave -> auto_increment
	
	private Long id;
	
	@NotBlank(message = "O atributo título é obrigatório!") //Não permite nulo -> NotNull permite espaço branco | NotBlank não permite espaço em branco
	@Size(min = 5, max = 100, message = "O atributo título deve conter no minimo 5 e no máximo 100 caracteres") //define o tamanho minimo e max da string -> add junto do MySQL
	private String titulo;
	
	@NotNull(message = "O atributo texto é obrigatório!")
	@Size(min = 10, max = 1000, message = "O atributo texto deve conter no minimo 10 e no máximo 1000 caracteres")
	private String texto;
	
	@UpdateTimestamp //toda vez que for criada uma nova postagem ou qnd for atualizar ele grava a hora e a data no db
	private LocalDateTime data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}
	
}
