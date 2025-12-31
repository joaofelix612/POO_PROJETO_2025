import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();

        int opcao;

        do {
            System.out.println("\n| Sistema de Gestao de Biblioteca |");
            System.out.println("1 - Listar livros");
            System.out.println("2 - Adicionar livro");
            System.out.println("3 - Listar utilizadores");
            System.out.println("4 - Adicionar utilizador");
            System.out.println("5 - Emprestar livro");
            System.out.println("6 - Devolver livro");
            System.out.println("7 - Gerar relatorio (Python)");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1: // Listar livros
                    biblioteca.listarLivros();
                    break;

                case 2: // Adicionar livro
                    System.out.print("Titulo: ");
                    String titulo = scanner.nextLine();

                    System.out.print("Autor: ");
                    String autor = scanner.nextLine();

                    biblioteca.adicionarLivro(titulo, autor);
                    break;

                case 3: // Listar utilizadores
                    biblioteca.listarUtilizadores();
                    break;

                case 4: // adicionar utilizador
                    System.out.print("Nome do utilizador: ");
                    String nome = scanner.nextLine();
                    biblioteca.adicionarUtilizador(nome);
                    break;

                case 5: // Emprestar livro
                    System.out.print("ID do livro: ");
                    int idLivroEmp = scanner.nextInt();
                    System.out.print("ID do utilizador: ");
                    int idUserEmp = scanner.nextInt();
                    biblioteca.emprestarLivro(idLivroEmp, idUserEmp);
                    break;

                case 6: // Devolver livro
                    System.out.print("ID do livro: ");
                    int idLivroDev = scanner.nextInt();
                    System.out.print("ID do utilizador: ");
                    int idUserDev = scanner.nextInt();
                    biblioteca.devolverLivro(idLivroDev, idUserDev);
                    break;

                case 7: // gerar relatório Python
    try {
        ProcessBuilder pb = new ProcessBuilder("python", "python/relatorio.py");
        Process process = pb.start();

        // ler a saída
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String linha;
        while ((linha = reader.readLine()) != null) {
            System.out.println(linha);
        }

        process.waitFor();
    } catch (Exception e) {
        System.out.println("Erro ao gerar relatorio.");
        e.printStackTrace();
    }
    break;

                case 0:
                    System.out.println("A sair do sistema...");
                    break;

                default:
                    System.out.println("Opcao invalida!");
            }

        } while (opcao != 0);

        scanner.close();
    }
}