import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();

        int opcao;

        do {
            System.out.println("\n=== Sistema de Gestão de Biblioteca ===");
            System.out.println("1 - Listar livros");
            System.out.println("2 - Adicionar livro");
            System.out.println("3 - Emprestar livro");
            System.out.println("4 - Devolver livro");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    biblioteca.listarLivros();
                    break;

                case 2:
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();

                    System.out.print("Autor: ");
                    String autor = scanner.nextLine();

                    biblioteca.adicionarLivro(titulo, autor);
                    break;

                case 3:
                    System.out.print("ID do livro: ");
                    int idEmprestar = scanner.nextInt();
                    biblioteca.emprestarLivro(idEmprestar);
                    break;

                case 4:
                    System.out.print("ID do livro: ");
                    int idDevolver = scanner.nextInt();
                    biblioteca.devolverLivro(idDevolver);
                    break;

                case 0:
                    System.out.println("A sair do sistema...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);

        scanner.close();
    }
}
