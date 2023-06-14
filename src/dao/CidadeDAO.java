package dao;

import apoio.ConexaoBD;
import apoio.IDAO_T;
import entidade.Cidade;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class CidadeDAO implements IDAO_T<Cidade> {

    ResultSet resultadoQ = null;

    @Override
    public String salvar(Cidade o) {

        try {
            String sql = ""
                    + "INSERT INTO cidades (descricao, estado_id) VALUES ("
                    + "'" + o.getDescricao() + "',"
                    + "" + o.getEstadoId()+ " "
                    + ")";

            System.out.println("sql: " + sql);

            ConexaoBD.executeUpdate(sql);

            return null;
        } catch (SQLException e) {
            System.out.println("Erro inserir cidades = " + e);
            return e.toString();
        }

    }

    @Override
    public String atualizar(Cidade o) {
        try {
            String sql = ""
                    + "UPDATE cidades "
                    + "SET "
                    + "descricao = '" + o.getDescricao() + "',"
                    + "estado_id = " + o.getEstadoId()+ " "
                    + "WHERE id = " + o.getId();

            System.out.println("sql: " + sql);

            ConexaoBD.executeUpdate(sql);

            return null;
        } catch (SQLException e) {
            System.out.println("Erro atualizar cidades = " + e);
            return e.toString();
        }
    }

    @Override
    public String excluir(int id) {
        try {
            String sql = ""
                    + "DELETE FROM cidades "
                    + "WHERE id = " + id;

            System.out.println("sql: " + sql);

            ConexaoBD.executeUpdate(sql);

            return null;
        } catch (SQLException e) {
            System.out.println("Erro excluir/inativar cidades = " + e);
            return e.toString();
        }
    }

    @Override
    public ArrayList<Cidade> consultarTodos() {
        ArrayList<Cidade> cidades = new ArrayList<>();

        try {
            String sql = ""
                    + "SELECT * FROM cidades ";

            resultadoQ = ConexaoBD.executeQuery(sql);

            while (resultadoQ.next()) {
                Cidade cidade = new Cidade();

                cidade.setId(resultadoQ.getInt("id"));
                cidade.setDescricao(resultadoQ.getString("descricao"));
                cidade.setEstadoId(resultadoQ.getInt("estado_id"));
               

                cidades.add(cidade);
            }
        } catch (SQLException e) {
            System.out.println("Erro consultar todos cidades = " + e);
        }

        return cidades;
    }

    @Override
    public ArrayList<Cidade> consultar(String criterio) {
        ArrayList<Cidade> cidades = new ArrayList<>();

        try {
            String sql = ""
                    + "SELECT * FROM cidades"
                    + "WHERE  "
                    + "descricao ILIKE '%" + criterio + "%' "
                    + "ORDER BY descricao";

            resultadoQ = ConexaoBD.executeQuery(sql);

            while (resultadoQ.next()) {
                Cidade cidade = new Cidade();

                cidade.setId(resultadoQ.getInt("id"));
                cidade.setDescricao(resultadoQ.getString("descricao"));
                cidade.setEstadoId(resultadoQ.getInt("estado_id"));


                cidades.add(cidade);
            }
        } catch (SQLException e) {
            System.out.println("Erro consultar todos estados = " + e);
        }

        return cidades;
    }

    @Override
    public Cidade consultarId(int id) {
        Cidade u = null;

        try {
            String sql = ""
                    + "SELECT * FROM cidades "
                    + "WHERE  "
                    + "id = " + id;

            System.out.println("sql: " + sql);

            resultadoQ = ConexaoBD.executeQuery(sql);

            if (resultadoQ.next()) {
                u = new Cidade();

                u.setId(id);
                u.setDescricao(resultadoQ.getString("descricao"));
                u.setEstadoId(resultadoQ.getInt("estado_id"));
            }

        } catch (SQLException e) {
            System.out.println("Erro consultar cidades = " + e);
        }
        return u;
    }

    public void popularTabela(JTable tabela, String criterio) {
        // dados da tabela
        Object[][] dadosTabela = null;
        int colunas = 3;

        // cabecalho da tabela
        Object[] cabecalho = new Object[colunas];
        cabecalho[0] = "id";
        cabecalho[1] = "descricao";
        cabecalho[2] = "estado_id";

        // cria matriz de acordo com nº de registros da tabela
        try {
            String sql = ""
                    + "SELECT count(*) FROM cidades "
                    + "WHERE  "
                    + "descricao ILIKE '%" + criterio + "%'";

            resultadoQ = ConexaoBD.executeQuery(sql);

            resultadoQ.next();

            dadosTabela = new Object[resultadoQ.getInt(1)][colunas];

        } catch (Exception e) {
            System.out.println("Erro ao consultar cidades: " + e);
        }

        int lin = 0;

        // efetua consulta na tabela
        try {
            String sql = ""
                    + "SELECT id, descricao, estado_id FROM cidades "
                    + "WHERE  "
                    + "descricao ILIKE '%" + criterio + "%' "
                    + "ORDER BY descricao";

            resultadoQ = ConexaoBD.executeQuery(sql);

            while (resultadoQ.next()) {

                dadosTabela[lin][0] = resultadoQ.getInt("id");
                dadosTabela[lin][1] = resultadoQ.getString("descricao");
                dadosTabela[lin][2] = resultadoQ.getInt("estado_id");

                // caso a coluna precise exibir uma imagem
//                if (resultadoQ.getBoolean("Situacao")) {
//                    dadosTabela[lin][2] = new ImageIcon(getClass().getClassLoader().getResource("Interface/imagens/status_ativo.png"));
//                } else {
//                    dadosTabela[lin][2] = new ImageIcon(getClass().getClassLoader().getResource("Interface/imagens/status_inativo.png"));
//                }
                lin++;
            }
        } catch (SQLException e) {
            System.out.println("problemas para popular tabela...");
            System.err.println(e);
        }

        // configuracoes adicionais no componente tabela
        tabela.setModel(new DefaultTableModel(dadosTabela, cabecalho) {
            @Override
            // quando retorno for FALSE, a tabela nao é editavel
            public boolean isCellEditable(int row, int column) {
                return false;
                /*  
                 if (column == 3) {  // apenas a coluna 3 sera editavel
                 return true;
                 } else {
                 return false;
                 }
                 */
            }

            // alteracao no metodo que determina a coluna em que o objeto ImageIcon devera aparecer
            @Override
            public Class getColumnClass(int column) {

                if (column == 2) {
//                    return ImageIcon.class;
                }
                return Object.class;
            }
        });

        // permite seleção de apenas uma linha da tabela
        tabela.setSelectionMode(0);

        // redimensiona as colunas de uma tabela
        TableColumn column = null;
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            column = tabela.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(17);
                    break;
                case 1:
                    column.setPreferredWidth(140);
                    break;
//                case 2:
//                    column.setPreferredWidth(14);
//                    break;
            }
        }
        // renderizacao das linhas da tabela = mudar a cor
//        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
//
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value,
//                    boolean isSelected, boolean hasFocus, int row, int column) {
//                super.getTableCellRendererComponent(table, value, isSelected,
//                        hasFocus, row, column);
//                if (row % 2 == 0) {
//                    setBackground(Color.GREEN);
//                } else {
//                    setBackground(Color.LIGHT_GRAY);
//                }
//                return this;
//            }
//        });
    }

}
