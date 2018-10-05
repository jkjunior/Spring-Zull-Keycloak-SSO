package br.unb.redes.cloudapps.zuul.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import br.unb.redes.cloudapps.zuul.util.ArquivosUtil;
import br.unb.redes.cloudapps.zuul.util.DBConexao;

@Repository
public class AuthDao {

	/**
	 * Verifica se o usuário encontra-se no grupo de servidores ativos, os quais
	 * farão login utilizando o Keycloak
	 * 
	 * @return email do servidor, ou null, caso não seja servidor ativo
	 * @author joao.esper
	 */
	public String servidorAtivo(String cpf) {
		String consulta = ArquivosUtil.getInstance().lerSql("auth_servidor");
		String email = null;

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = DBConexao.getInstance().getConnection();
			stmt = con.prepareStatement(consulta);
			stmt.setString(1, cpf);
			rs = stmt.executeQuery();

			while (rs.next()) {
				email = rs.getString("EMAIL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return email;
	}

	/**
	 * Verifica se o usuário encontra-se no grupo de aposentados e pensionistas,
	 * os quais farão login utilizando o sistema de login do sistema <b>Acesso
	 * Comprovantes</b>
	 * 
	 * @return cpf do usuario, ou null, quando não encontrar o usuário
	 * @author joao.esper
	 */
	public String aposentadoOuPensionista(String pCpf) {
		String consulta = ArquivosUtil.getInstance().lerSql("auth_aposentado_pensionista");
		String cpf = null;

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = DBConexao.getInstance().getConnection();
			stmt = con.prepareStatement(consulta);
			stmt.setString(1, pCpf);
			rs = stmt.executeQuery();

			while (rs.next()) {
				cpf = rs.getString("CPF");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return cpf;
	}

	/**
	 * Recupera as credenciais do usuário do sistema acesso comprovantes para
	 * validação futura
	 * 
	 * @return array de String contendo [0]: Usuário, [1]: Senha
	 * @author joao.esper
	 */
	public String[] dadosLoginAcessoPensionista(String cpf) {
		String consulta = ArquivosUtil.getInstance().lerSql("auth_usuario_acesso_pensionista");
		String[] dadosUsuario = new String[2];

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = DBConexao.getInstance().getConnection();
			stmt = con.prepareStatement(consulta);
			stmt.setString(1, cpf);
			rs = stmt.executeQuery();

			while (rs.next()) {
				dadosUsuario[0] = rs.getString("CPF");
				dadosUsuario[1] = rs.getString("SENHA");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return dadosUsuario;
	}

	/**
	 * Recebe os dados da sessão e executa operação de inserção no banco de
	 * dados
	 * 
	 * @param authModel
	 * 
	 * @author joao.esper
	 */
	public void gravarDadosSessao(AuthModel authModel) {
		String sql = ArquivosUtil.getInstance().lerSql("auth_nova_sessao");

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = DBConexao.getInstance().getConnection();
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sql);
			stmt.setString(1, authModel.getCpfUsuario());
			stmt.setString(2, authModel.getKeycloakToken());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			if (con != null) {
				try {
					System.err.print("Erro! Executando rollback da transação");
					con.rollback();
				} catch (SQLException excep) {
					excep.printStackTrace();
				}
			}
		} finally {
			try {
				stmt.close();
				con.setAutoCommit(true);
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
