/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package start;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author lefoly
 */
public class Principal {

    public static void main(String[] args) {

        int opcao = -1; // para ter um valor inicial

        String menu = "CADASTRO DE CONTATOS\n"
                + "====================\n"
                + "1. Inclusão\n"
                + "2. Edição\n"
                + "3. Exclusão\n"
                + "4. Listagem\n"
                + "0. SAIR\n"
                + "--------------------\n"
                + "Digite uma opção:";

        while (opcao != 0) {
            opcao = Integer.parseInt(JOptionPane.showInputDialog(menu));

            switch (opcao) {
                case 1:
                    incluir();
                    break;
                case 2:
                    editar();
                    break;
                case 3:
                    excluir();
                    break;
                case 4:
                    listar();
                    break;
                case 0:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida.");
                    break;
            }
        }
    }

    public static boolean contatoExiste(int id) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/agenda", "root", "mysql");

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id FROM Contato WHERE id = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next(); // retorna true se existe contato

            conn.close();
            return existe;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar a existência do contato: " + ex.getMessage());
            return false; // contato não existe
        }
    }

    public static void incluir() {
        try {
            String nome = JOptionPane.showInputDialog("Digite o nome do contato:");
            String email = JOptionPane.showInputDialog("Digite o email do contato:");
            String telefone = JOptionPane.showInputDialog("Digite o telefone do contato:");

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/agenda", "root", "mysql");

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Contato (nome, email, telefone) VALUES (?, ?, ?)");
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, telefone);
            ps.execute();

            JOptionPane.showMessageDialog(null, "Contato incluído com sucesso!");
            conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao incluir contato: " + ex.getMessage());
        }
    }

    public static void editar() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do contato que deseja editar:"));

            // verifica se o ID existe antes de editar
            if (contatoExiste(id)) {
                String nome = JOptionPane.showInputDialog("Digite o novo nome:");
                String email = JOptionPane.showInputDialog("Digite o novo email:");
                String telefone = JOptionPane.showInputDialog("Digite o novo telefone:");

                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost/agenda", "root", "mysql");

                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE Contato SET nome = ?, email = ?, telefone = ? WHERE id = ?");
                ps.setString(1, nome);
                ps.setString(2, email);
                ps.setString(3, telefone);
                ps.setInt(4, id);
                ps.execute();

                JOptionPane.showMessageDialog(null, "Contato editado com sucesso!");
                conn.close();
            } else {
                JOptionPane.showMessageDialog(null, "ID de contato não encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao editar contato: " + ex.getMessage());
        }
    }

    public static void excluir() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do contato que deseja excluir:"));

            // verifica se o ID existe antes de excluir
            if (contatoExiste(id)) {
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost/agenda", "root", "mysql");

                PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM Contato WHERE id = ?");
                ps.setInt(1, id);
                ps.execute();

                JOptionPane.showMessageDialog(null, "Contato excluído com sucesso!");
                conn.close();
            } else {
                JOptionPane.showMessageDialog(null, "ID de contato não encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir contato: " + ex.getMessage());
        }
    }

    public static void listar() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/agenda", "root", "mysql");
            PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM Contato");
            ResultSet rs = ps1.executeQuery();
            List<Contato> lista = new ArrayList();
            while (rs.next()) {
                Contato c1 = new Contato();
                c1.setId(rs.getInt("id"));
                c1.setNome(rs.getString("nome"));
                c1.setEmail(rs.getString("email"));
                c1.setTelefone(rs.getString("telefone"));
                lista.add(c1);
            }

            StringBuilder listaContatos = new StringBuilder("Lista de Contatos:\n");
            for (Contato c1 : lista) {
                listaContatos.append("ID: ").append(c1.getId()).append("\n");
                listaContatos.append("Nome: ").append(c1.getNome()).append("\n");
                listaContatos.append("Email: ").append(c1.getEmail()).append("\n");
                listaContatos.append("Telefone: ").append(c1.getTelefone()).append("\n\n");
            }

            JOptionPane.showMessageDialog(null, listaContatos.toString());

            conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao listar contatos: " + ex.getMessage());
        }
    }
}