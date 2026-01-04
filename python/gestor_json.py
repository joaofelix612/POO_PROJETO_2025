import json
import sys
import os

# descobre em que pasta está este script
BASE_DIR = os.path.dirname(os.path.abspath(__file__))

# define o caminho completo do ficheiro JSON
FICHEIRO_JSON = os.path.join(BASE_DIR, "dados.json")

# função que carrega os dados do json
def carregar_dados():
    # se o ficheiro não existir, devolve listas vazias
    if not os.path.exists(FICHEIRO_JSON):
        return {"livros": [], "utilizadores": []}

    # abre e lê o json
    with open(FICHEIRO_JSON, "r", encoding="utf-8") as f:
        return json.load(f)

# função que guarda os dados no json
def guardar_dados(dados):
    with open(FICHEIRO_JSON, "w", encoding="utf-8") as f:
        json.dump(dados, f, indent=4, ensure_ascii=False)

# imprime os livros no formato que o Java consegue ler
def carregar_livros():
    dados = carregar_dados()

    for l in dados.get("livros", []):
        # converte boolean para true/false 
        disponivel = str(l["disponivel"]).lower()

        # converte None para null
        emprestado = "null" if l.get("emprestadoPara") is None else l.get("emprestadoPara")

        print(f'{l["id"]},{l["titulo"]},{l["autor"]},{disponivel},{emprestado}')

# imprime os utilizadores no formato que o java lê
def carregar_utilizadores():
    dados = carregar_dados()

    for u in dados.get("utilizadores", []):
        print(f'{u["id"]},{u["nome"]}')

# guarda livros e utilizadores vindos do Java
def guardar():
    dados = carregar_dados()
    livros = []
    utilizadores = []

    # lê cada linha enviada pelo Java
    for linha in sys.stdin:
        linha = linha.strip()
        if not linha:
            continue

        if linha.startswith("U,"):
            # utilizador
            p = linha.split(",")
            utilizadores.append({
                "id": int(p[1]),
                "nome": p[2]
            })
        else:
            # livro
            p = linha.split(",")
            livros.append({
                "id": int(p[0]),
                "titulo": p[1],
                "autor": p[2],
                "disponivel": p[3] == "true",
                "emprestadoPara": int(p[4]) if p[4] != "null" else None
            })

    # atualiza o json
    dados["livros"] = livros
    dados["utilizadores"] = utilizadores
    guardar_dados(dados)

# main
if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Comando obrigatorio")
        sys.exit(1)

    comando = sys.argv[1]

    if comando == "carregar_livros":
        carregar_livros()
    elif comando == "carregar_utilizadores":
        carregar_utilizadores()
    elif comando == "guardar":
        guardar()
