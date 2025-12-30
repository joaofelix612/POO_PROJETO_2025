import json
import sys
import os

FICHEIRO = "dados.json"

def carregar():
    if not os.path.exists(FICHEIRO):
        print("")  # devolve vazio
        return

    with open(FICHEIRO, "r", encoding="utf-8") as f:
        dados = json.load(f)
        for livro in dados.get("livros", []):
            # imprime cada livro numa linha id,titulo,autor,disponivel
            print(f"{livro['id']},{livro['titulo']},{livro['autor']},{livro['disponivel']}")

def guardar():
    # lê stdin (as linhas que Java envia)
    linhas = sys.stdin.read().splitlines()
    livros = []
    for linha in linhas:
        if not linha.strip():
            continue
        partes = linha.split(",")
        if len(partes) == 4:
            id = int(partes[0])
            titulo = partes[1]
            autor = partes[2]
            disponivel = partes[3].lower() == "true"
            livros.append({
                "id": id,
                "titulo": titulo,
                "autor": autor,
                "disponivel": disponivel
            })
    with open(FICHEIRO, "w", encoding="utf-8") as f:
        json.dump({"livros": livros}, f, indent=4, ensure_ascii=False)
    print("OK")

if __name__ == "__main__":
    acao = sys.argv[1]
    if acao == "carregar":
        carregar()
    elif acao == "guardar":
        guardar()
