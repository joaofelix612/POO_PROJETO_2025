public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private boolean disponivel;

    public Livro(int id, String titulo, String autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.disponivel = true;
    }

    // getters necessários para a Biblioteca
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    // métodos para emprestar/devolver
    public void emprestar() {
        disponivel = false;
    }

    public void devolver() {
        disponivel = true;
    }

    @Override
    public String toString() {
        return "ID: " + id +
               " | Titulo: " + titulo +
               " | Autor: " + autor +
               " | Disponivel: " + (disponivel ? "Sim" : "Nao");
    }
}
