package br.unb.redes.cloudapps.zuul.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Classe responsável pela leitura e configuração de arquivos de resources
 * utilizados na aplicação
 * @author joao.esper
 * 
 */
public class ArquivosUtil {
	//SQL
	private static final String SQL_EXT = ".sql";
	private static final String SQL_PATH = "./templates/sql/";
	
	//TXT
	//private static final String TXT_EXT = ".txt";
	
	private ArquivosUtil() {
		//Singleton Pattern
	}
	
	static class Holder{
		static final ArquivosUtil INSTANCE = new ArquivosUtil();
	}
	
	public static ArquivosUtil getInstance(){
		return Holder.INSTANCE;
	}
	
	public String lerSql(String nomeArquivoSql){
		String sql = null;
		String caminhoCompleto = null;
		
		caminhoCompleto = SQL_PATH.concat(nomeArquivoSql.concat(SQL_EXT));
		sql = ArquivosUtil.getInstance().lerArquivo(caminhoCompleto);
		return sql;
	}
	
	private String lerArquivo(String caminho) {		
		String texto = null;
		try {			
			byte [] textoBytes = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(caminho).toURI()));
			texto = new String(textoBytes, StandardCharsets.UTF_8);
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return texto;
	}
	
}