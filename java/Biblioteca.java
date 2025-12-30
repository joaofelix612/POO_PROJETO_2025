import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Biblioteca {

    private ArrayList<Livro> livros;
    private int proximoId;
    private static final String PYTHON_SCRIPT = "python/gestor_json.py"; // caminho do script python

    public Biblioteca() {
        livros = new ArrayList<>();
        proximoId = 1;
        carregarLivrosDoJSON(); // carrega livros do json ao iniciar
    }

    //adicionar livro
    public void adicionarLivro(String titulo, String autor) {
        Livro livro = new Livro(proximoId, titulo, autor);
        livros.add(livro);
        proximoId++;
        System.out.println("Livro adicionado com sucesso!");
        guardarLivrosNoJSON(); // guarda automaticamente
    }

    //listar livros
    public void listarLivros() {
        if (livros.isEmpty()) {
            System.out.println("Nao existem livros registados.");
            return;
        }

        for (Livro livro : livros) {
            System.out.println(livro);
        }
    }

    // emprestar livro
    public void emprestarLivro(int id) {
        Livro livro = procurarLivroPorId(id);

        if (livro == null) {
            System.out.println("Livro nao encontrado.");
        } else if (!livro.isDisponivel()) {
            System.out.println("Livro ja se encontra emprestado.");
        } else {
            livro.emprestar();
            System.out.println("Livro emprestado com sucesso.");
            guardarLivrosNoJSON(); // guarda depois do emprestimo
        }
    }

    // devolver livro
    public void devolverLivro(int id) {
        Livro livro = procurarLivroPorId(id);

        if (livro == null) {
            System.out.println("Livro nao encontrado.");
        } else if (livro.isDisponivel()) {
            System.out.println("Este livro ja esta disponivel.");
        } else {
            livro.devolver();
            System.out.println("Livro devolvido com sucesso.");
            guardarLivrosNoJSON(); // guarda depois de devolver
        }
    }

    //metodo auxiliar para procurar livro pelo id
    private Livro procurarLivroPorId(int id) {
        for (Livro livro : livros) {
            if (livro.getId() == id) {
                return livro;
            }
        }
        return null;
    }

    // -------------------------
    // integração do python
    // -------------------------

    private void carregarLivrosDoJSON() {
        try {
            // chama o python para carregar livros
            ProcessBuilder pb = new ProcessBuilder("python", PYTHON_SCRIPT, "carregar");
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String linha;
            while ((linha = reader.readLine()) != null) {
                // cada linha é um livro no formato id,titulo,autor,disponivel
                String[] partes = linha.split(",");
                if (partes.length == 4) {
                    int id = Integer.parseInt(partes[0]);
                    String titulo = partes[1];
                    String autor = partes[2];
                    boolean disponivel = Boolean.parseBoolean(partes[3]);

                    Livro livro = new Livro(id, titulo, autor);
                    if (!disponivel) {
                        livro.emprestar(); // se não disponível, marca como emprestado
                    }
                    livros.add(livro);

                    if (id >= proximoId) {
                        proximoId = id + 1; // atualiza proximoId automaticamente
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Nao foi possivel carregar os livros do JSON.");
            // e.printStackTrace(); // opcional para debug
        }
    }

    private void guardarLivrosNoJSON() {
    try {
        ProcessBuilder pb = new ProcessBuilder("python", PYTHON_SCRIPT, "guardar");
        Process process = pb.start();

        // escreve os livros para o stdin do Python
        StringBuilder sb = new StringBuilder();
        for (Livro livro : livros) {
            sb.append(livro.getId()).append(",")
              .append(livro.getTitulo()).append(",")
              .append(livro.getAutor()).append(",")
              .append(livro.isDisponivel()).append("\n");
        }

        process.getOutputStream().write(sb.toString().getBytes());
        process.getOutputStream().flush();
        process.getOutputStream().close();

        process.waitFor();

    } catch (Exception e) {
        System.out.println("Nao foi possivel guardar os livros no JSON.");
        // e.printStackTrace(); // opcional debug
    }
}
}