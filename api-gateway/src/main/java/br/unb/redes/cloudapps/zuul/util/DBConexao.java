package br.unb.redes.cloudapps.zuul.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.springframework.stereotype.Component;

/**
 * Classe para gerar a conexão com o banco de dados configurado com base nas
 * strings presentes no arquivo de propriedades da aplicação.
 * 
 * @author joao.esper
 */
@Component
public class DBConexao {

	private static String dbClass;
	private static String dbUrL;
	private static String dbUsername;
	private static String dbPassword;

	private DBConexao() {
		// Singleton pattern
	}

	static class Holder {
		static final DBConexao INSTANCE = new DBConexao();
	}

	public static DBConexao getInstance() {
		return Holder.INSTANCE;
	}

	/**
	 * Carrega os dados da conexão do arquivo de configuração, e retorna um
	 * objeto Connection, o qual representa uma conexão com o banco de dados
	 * 
	 * @return um objeto Connection, representando a conexão com o banco de
	 *         dados
	 */
	public Connection getConnection() {
		Connection con = null;
		carregarAtributos();
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(dbUrL, dbUsername, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

	private static void carregarAtributos() {
		Properties props = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(Constantes.PROPERTIES_FILE);

		try {
			props.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dbClass = props.getProperty("db.class");
		dbUrL = props.getProperty("db.url");
		dbUsername = props.getProperty("db.username");
		dbPassword = props.getProperty("db.password");
	}
}
