package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) /*ajuda a ordenar a sequencia da excecução dos
														testes para respeitar a ordem de escrita, caso ao
														contrário, ele ordena por ordem alfabética.*/
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate; /*classe que cria uma requisição, e envia a requisição
	 											para a classe controladorea -> faz o papel do insomnia
	 											(Consimir uma API de terceiros, configuração parecida com essa*/

	@Autowired
	private UsuarioService usuarioService;

    @Autowired
	private UsuarioRepository usuarioRepository;
    
    @BeforeAll
	void start()
    {
		usuarioRepository.deleteAll(); //zerando a db
	}
    
    //primeiro teste
    @Test
	@Order(1) // primeiro teste que vai ser executado
	@DisplayName("Cadastrar Um Usuário")
	public void deveCriarUmUsuario() 
    {
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, 
			"Paulo Antunes", "paulo_antunes@email.com.br", "13465278", "https://i.imgur.com/JR7kUFU.jpg")); //criando a requisição

		ResponseEntity<Usuario> resposta = testRestTemplate
			.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class); //.exchange -> envia a requisição (botão send do insomnia)

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode()); //resposta da requisição
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario()); /*L62, 63 -> verifica se o que foi 
																							enviado na requisição foi o que 
																							está gravado*/
	}
    
    //segundo teste
    @Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDeveDuplicarUsuario() 
    {
		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg")); //gravando no banco

		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, 
			"Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg")); //tentando gravar novamente com uma requisição Http

		ResponseEntity<Usuario> resposta = testRestTemplate //verificação da gravação
			.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode()); //acerto esperado
	}
    
    //terceiro teste
    @Test
	@Order(3)
	@DisplayName("Alterar um Usuário")
	public void deveAtualizarUmUsuario() 
    {
		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Juliana Andrews", "juliana_andrews@email.com.br", 
			"juliana123", "https://i.imgur.com/yDRVeK7.jpg")); //salva o cadastro em um novo objeto

		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(), 
			"Juliana Andrews Ramos", "juliana_ramos@email.com.br", 
			"juliana123", "https://i.imgur.com/yDRVeK7.jpg"); //atualizando usuario
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate); //inserindo objeto na requisição

		ResponseEntity<Usuario> resposta = testRestTemplate
			.withBasicAuth("root", "root") // método protegido por token
			.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode()); 
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
	}
    
    //quarto teste
    @Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() 
    {
		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Sabrina Sanches", "sabrina_sanches@email.com.br", 
			"sabrina123", "https://i.imgur.com/5M2p5Wb.jpg"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Ricardo Marques", "ricardo_marques@email.com.br", 
			"ricardo123", "https://i.imgur.com/Sk5SjWE.jpg"));

		ResponseEntity<String> resposta = testRestTemplate
			.withBasicAuth("root", "root")
			.exchange("/usuarios/all", HttpMethod.GET, null, String.class); //get não tem corpo da requisição = null

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
    
    ///quinto teste
    @Test
	@Order(5)
	@DisplayName("Listar Um Usuário")
	public void deveListarApenasUmUsuario() 
    {
    	Optional<Usuario> buscaUsuario = usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Amanda Lobo", "amanda@email.com.br", "amanda123", "https://i.imgur.com/T12NIp9.jpg"));
    	
    	ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/" + buscaUsuario.get().getId(), HttpMethod.GET, null, String.class);
    	
    	assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
    
    //sexto teste
    @Test
	@Order(6)
	@DisplayName("Login do Usuário")
	public void deveAutenticarUsuario() 
    {
    	usuarioService.cadastrarUsuario(new Usuario(0L, 
    			"Estefani Lima", "estefani@email.com.br", "estefani123", "https://i.imgur.com/T12NIp9.jpg"));
    	
    	HttpEntity<UsuarioLogin> corpoRequisicao = new HttpEntity<UsuarioLogin>(new UsuarioLogin(0L, 
    			"", "estefani@email.com.br", "estefani123", "", ""));
    	
    	ResponseEntity<UsuarioLogin> corpoResposta = testRestTemplate
    			.exchange("/usuarios/logar", HttpMethod.POST, corpoRequisicao, UsuarioLogin.class);
    	
    	assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
    }
}
