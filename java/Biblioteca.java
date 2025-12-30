import java.util.ArrayList;

public class Biblioteca {

    private ArrayList<Livro> livros;
    private int proximoId;

    public Biblioteca() {
        livros = new ArrayList<>();
        proximoId = 1;
    }

    //adicionar livro
    public void adicionarLivro(String titulo, String autor) {
        Livro livro = new Livro(proximoId, titulo, autor);
        livros.add(livro);
        proximoId++;

        System.out.println("Livro adicionado com sucesso!");
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
        }
    }

    //metodo auxiliar
    private Livro procurarLivroPorId(int id) {
        for (Livro livro : livros) {
            if (livro.getId() == id) {
                return livro;
            }
        }
        return null;
    }
}
