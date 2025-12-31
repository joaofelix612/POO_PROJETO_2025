public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private boolean disponivel;
    private Integer emprestadoPara; // id do utilizador que tem o livro, null se disponível

    public Livro(int id, String titulo, String autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.disponivel = true;
        this.emprestadoPara = null;
    }

    // getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public boolean isDisponivel() { return disponivel; }
    public Integer getEmprestadoPara() { return emprestadoPara; }

    // métodos
    public void emprestar(int utilizadorId) {
        disponivel = false;
        emprestadoPara = utilizadorId;
    }

    public void devolver() {
        disponivel = true;
        emprestadoPara = null;
    }

    @Override
    public String toString() {
        String status = disponivel ? "Disponivel" : "Emprestado a ID " + emprestadoPara;
        return "ID: " + id + " | " + titulo + " | " + autor + " | " + status;
    }
}
