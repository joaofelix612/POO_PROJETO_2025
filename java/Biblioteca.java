import java.io.*;
import java.util.ArrayList;

public class Biblioteca {

    private ArrayList<Livro> livros;
    private ArrayList<Utilizador> utilizadores;
    private int proximoLivroId;
    private int proximoUtilizadorId;
    private static final String PYTHON_SCRIPT =
        new File("python/gestor_json.py").getAbsolutePath();

    public Biblioteca() {
        livros = new ArrayList<>();
        utilizadores = new ArrayList<>();
        proximoLivroId = 1;
        proximoUtilizadorId = 1;
        carregarDadosJSON();
    }

    // Livros
    public void adicionarLivro(String titulo, String autor) {
        Livro livro = new Livro(proximoLivroId++, titulo, autor);
        livros.add(livro);
        System.out.println("Livro adicionado com sucesso!");
        guardarDadosJSON();
    }

    public void listarLivros() {
        if(livros.isEmpty()) {
            System.out.println("Nao existem livros registados.");
            return;
        }
        for(Livro l : livros) System.out.println(l);
    }

    public void emprestarLivro(int livroId, int utilizadorId) {
        Livro livro = procurarLivroPorId(livroId);
        Utilizador user = procurarUtilizadorPorId(utilizadorId);

        if(livro == null) { System.out.println("Livro nao encontrado."); return; }
        if(user == null) { System.out.println("Utilizador nao encontrado."); return; }
        if(!livro.isDisponivel()) { System.out.println("Livro ja esta emprestado."); return; }

        livro.emprestar(utilizadorId);
        System.out.println("Livro emprestado com sucesso.");
        guardarDadosJSON();
    }

    public void devolverLivro(int livroId, int utilizadorId) {
        Livro livro = procurarLivroPorId(livroId);
        if(livro == null) { System.out.println("Livro nao encontrado."); return; }
        if(livro.isDisponivel() || livro.getEmprestadoPara() != utilizadorId) {
            System.out.println("Este livro nao esta emprestado a este utilizador.");
            return;
        }

        livro.devolver();
        System.out.println("Livro devolvido com sucesso.");
        guardarDadosJSON();
    }

    private Livro procurarLivroPorId(int id) {
        for(Livro l : livros) if(l.getId() == id) return l;
        return null;
    }

    // Utilizadores
    public void adicionarUtilizador(String nome) {
        Utilizador u = new Utilizador(proximoUtilizadorId++, nome);
        utilizadores.add(u);
        System.out.println("Utilizador adicionado com sucesso!");
        guardarDadosJSON();
    }

    public void listarUtilizadores() {
        if(utilizadores.isEmpty()) {
            System.out.println("Nao existem utilizadores registados.");
            return;
        }
        for(Utilizador u : utilizadores) System.out.println(u);
    }

    private Utilizador procurarUtilizadorPorId(int id) {
        for(Utilizador u : utilizadores) if(u.getId() == id) return u;
        return null;
    }

    // Integracao Python
 private void carregarDadosJSON() {
    try {
        livros.clear();
        utilizadores.clear();

        //livros
        ProcessBuilder pbLivros = new ProcessBuilder(
                "python",
                PYTHON_SCRIPT,
                "carregar_livros"
        );

        pbLivros.redirectErrorStream(true); 
        Process pLivros = pbLivros.start();

        BufferedReader readerLivros =
                new BufferedReader(new InputStreamReader(pLivros.getInputStream()));

        String linha;
        while ((linha = readerLivros.readLine()) != null) {
            System.out.println("DEBUG LIVRO -> " + linha); //debuging

            String[] p = linha.split(",");
            int id = Integer.parseInt(p[0]);
            String titulo = p[1];
            String autor = p[2];

            boolean disponivel = p[3].equalsIgnoreCase("true");
            Integer emprestadoPara =
                    (p.length > 4 && !p[4].equals("null")) ? Integer.parseInt(p[4]) : null;

            Livro l = new Livro(id, titulo, autor);
            if (!disponivel) l.emprestar(emprestadoPara);
            livros.add(l);

            if (id >= proximoLivroId) proximoLivroId = id + 1;
        }

        pLivros.waitFor();

        // utilizador
        ProcessBuilder pbUsers = new ProcessBuilder(
                "python",
                PYTHON_SCRIPT,
                "carregar_utilizadores"
        );

        pbUsers.redirectErrorStream(true);
        Process pUsers = pbUsers.start();

        BufferedReader readerUsers =
                new BufferedReader(new InputStreamReader(pUsers.getInputStream()));

        while ((linha = readerUsers.readLine()) != null) {
            System.out.println("DEBUG USER -> " + linha); //debugging

            String[] p = linha.split(",");
            int id = Integer.parseInt(p[0]);
            String nome = p[1];

            utilizadores.add(new Utilizador(id, nome));

            if (id >= proximoUtilizadorId) proximoUtilizadorId = id + 1;
        }

        pUsers.waitFor();

    } catch (Exception e) {
        System.out.println("Erro ao carregar dados do JSON.");
        e.printStackTrace();
    }
}



   private void guardarDadosJSON() {
    try {
        ProcessBuilder pb = new ProcessBuilder("python", PYTHON_SCRIPT, "guardar");
        Process process = pb.start();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        // envia livros
        for(Livro l : livros) {
            writer.write(l.getId() + "," + l.getTitulo() + "," + l.getAutor() + "," + l.isDisponivel() + "," +
                         (l.getEmprestadoPara() == null ? "null" : l.getEmprestadoPara()));
            writer.newLine();
        }

        // envia utilizadores
        for(Utilizador u : utilizadores) {
            writer.write("U," + u.getId() + "," + u.getNome());
            writer.newLine();
        }

        writer.flush();
        writer.close();
        process.waitFor();

    } catch(Exception e) {
        System.out.println("Nao foi possivel guardar os dados no JSON.");
        e.printStackTrace();
    }
 }
}