import java.io.*;
import java.util.ArrayList;

public class Biblioteca {

    private ArrayList<Livro> livros;
    private ArrayList<Utilizador> utilizadores;
    private int proximoLivroId;
    private int proximoUtilizadorId;
    private static final String PYTHON_SCRIPT = "python/gestor_json.py";

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
            ProcessBuilder pb = new ProcessBuilder("python", PYTHON_SCRIPT, "carregar");
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String linha;
            while((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if(partes.length >= 5) { // id,titulo,autor,disponivel,emprestadoPara
                    int id = Integer.parseInt(partes[0]);
                    String titulo = partes[1];
                    String autor = partes[2];
                    boolean disponivel = Boolean.parseBoolean(partes[3]);
                    String emprestadoParaStr = partes[4];
                    Integer emprestadoPara = emprestadoParaStr.equals("null") ? null : Integer.parseInt(emprestadoParaStr);

                    Livro livro = new Livro(id, titulo, autor);
                    if(!disponivel) livro.emprestar(emprestadoPara);
                    livros.add(livro);

                    if(id >= proximoLivroId) proximoLivroId = id+1;
                }
            }

        } catch(Exception e) {
            System.out.println("Nao foi possivel carregar os dados do JSON.");
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