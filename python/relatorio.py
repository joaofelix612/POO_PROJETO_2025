import json
import os

#determina a pasta onde este script se encontra e cria o caminho absoluto
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
FICHEIRO_JSON = os.path.join(BASE_DIR, "dados.json")


def main():
   
    if not os.path.exists(FICHEIRO_JSON):
        print("Nao existem dados para gerar relatorio.")
        return

    #abre o ficheiro json e carrega os dados
    with open(FICHEIRO_JSON, "r", encoding="utf-8") as f:
        dados = json.load(f)

    #Obtem a lista de livros
    livros = dados.get("livros", [])

    #cria um dicionário de utilizadores para referência rápida
    utilizadores = {u["id"]: u["nome"] for u in dados.get("utilizadores", [])}

    #cabeçalho do relatório
    print("\n-----------------------------")
    print("        RELATORIO")
    print("-------------------------------\n")

    # Seção de Livros
    print("Livros:")
    if not livros:
        print("Nenhum livro registado.")
    else:
        for l in livros:
            # define o estado do livro
            if l["disponivel"]:
                estado = "Disponivel"
            else:
                #procura o nome do utilizador que tem o livro emprestado
                nome = utilizadores.get(l.get("emprestadoPara"), "Desconhecido")
                estado = f"Emprestado a: {nome}"

            # Imprime cada livro no formato: [ID] Titulo - Autor (Estado)
            print(f'[{l["id"]}] {l["titulo"]} - {l["autor"]} ({estado})')

    print("\n-------------------------------\n")

    # seção de utilizadores
    print("Utilizadores:")
    if not utilizadores:
        print("Nenhum utilizador registado.")
    else:
        for u in dados.get("utilizadores", []):
            print(f'[{u["id"]}] {u["nome"]}')

    print("\n-------------------------------\n")


# main
if __name__ == "__main__":
    main()
