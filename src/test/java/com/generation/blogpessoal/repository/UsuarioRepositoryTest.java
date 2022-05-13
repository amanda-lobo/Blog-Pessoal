package com.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogpessoal.model.Usuario;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) 
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //tem um ciclo de vida por classe (método beforeAll, afterAll ou os dois)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll //ele vai ser executado antes de rodar antes dos testes -> primeira coisa a ser executada
	void start() //start -> existir 
	{
        usuarioRepository.deleteAll(); //garante que a tabela vai estar sem registros para fazer testes controlados -> delete sem where

        //inserindo registros na tabela de Usuario
		usuarioRepository.save(new Usuario(0L, "João da Silva", "joao@email.com.br", "13465278",
                                           "https://i.imgur.com/FETvs2O.jpg")); //0L -> 0 = não sabe o id | L = tipo Long 
		
		usuarioRepository.save(new Usuario(0L, "Manuela da Silva", "manuela@email.com.br", "13465278", 
                                           "https://i.imgur.com/NtyGneo.jpg"));
		
		usuarioRepository.save(new Usuario(0L, "Adriana da Silva", "adriana@email.com.br", "13465278",
                                           "https://i.imgur.com/mB3VM2N.jpg"));

        usuarioRepository.save(new Usuario(0L, "Paulo Antunes", "paulo@email.com.br", "13465278", 
                                           "https://i.imgur.com/JR7kUFU.jpg"));
	}
	
	//Primeiro metodo de teste
	@Test // indica que é um teste
	@DisplayName("Retorna 1 usuario") //nome do teste
	public void deveRetornarUmUsuario() 
	{
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@email.com.br"); //passa um usuário como parametro
		assertTrue(usuario.get().getUsuario().equals("joao@email.com.br")); //verifica se o usuario existe, se encontrar o teste funcionou.
	}
	
	//Segundo teste
	@Test
	@DisplayName("Retorna 3 usuarios")
	public void deveRetornarTresUsuarios() {

		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva"); //teste de busca especifica
		assertEquals(3, listaDeUsuarios.size()); //se o tamanho da lista for 3 com usuarios com o sobrenome lista, ele funciona
		assertTrue(listaDeUsuarios.get(0).getNome().equals("João da Silva"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Manuela da Silva"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Adriana da Silva")); //está checando se a ordem dos usuarios da lista são esses -> ordem de cadastro no banco
		
	}
}
