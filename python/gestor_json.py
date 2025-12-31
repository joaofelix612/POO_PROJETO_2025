import json
import sys
import os

#descobre em que pasta está este script
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
# define o caminho completo do ficheiro JSON
FICHEIRO_JSON = os.path.join(BASE_DIR, "dados.json")

# função que carrega os dados do json
def carregar_dados():
    # Se o ficheiro não existir, devolve vazio
    if not os.path.exists(FICHEIRO_JSON):
        return {"livros": [], "utilizadores": []}
    # abre e lê o json
    with open(FICHEIRO_JSON, "r", encoding="utf-8") as f:
        return json.load(f)

# Função que guarda os dados no json
def guardar_dados(dados):
    with open(FICHEIRO_JSON, "w", encoding="utf-8") as f:
        json.dump(dados, f, indent=4, ensure_ascii=False)

# Função que imprime os livros no formato que o java vai ler
def carregar_livros():
    dados = carregar_dados()
    for l in dados.get("livros", []):
        emprestado = l.get("emprestadoPara", "")
        print(f'{l["id"]},{l["titulo"]},{l["autor"]},{l["disponivel"]},{emprestado}')

#Função que imprime os utilizadores no formato que o java vai ler
def carregar_utilizadores():
    dados = carregar_dados()
    for u in dados.get("utilizadores", []):
        print(f'{u["id"]},{u["nome"]}')

#função que guarda livros e utilizadores no json
def guardar():
    dados = carregar_dados()  # carrega dados atuais
    livros = []
    utilizadores = []

    # lê cada linha do Java via stdin
    for linha in sys.stdin:
        linha = linha.strip()  # tira espaços extra
        if not linha:
            continue
        if linha.startswith("U,"):
            # se começa com U, é um utilizador
            p = linha.split(",")
            utilizadores.append({
                "id": int(p[1]),
                "nome": p[2]
            })
        else:
            # senão é um livro
            p = linha.split(",")
            livros.append({
                "id": int(p[0]),
                "titulo": p[1],
                "autor": p[2],
                "disponivel": p[3] == "true",
                "emprestadoPara": int(p[4]) if p[4] and p[4] != "null" else None
            })

    # substitui dados antigos pelos novos
    dados["livros"] = livros
    dados["utilizadores"] = utilizadores
    # guarda tudo no JSON
    guardar_dados(dados)

# main
if __name__ == "__main__":
    # precisa de pelo menos um argumento para saber o comando
    if len(sys.argv) < 2:
        print("Comando obrigatorio")
        sys.exit(1)

    comando = sys.argv[1]

    # escolhe o que fazer com base no argumento
    if comando == "carregar_livros":
        carregar_livros()
    elif comando == "carregar_utilizadores":
        carregar_utilizadores()
    elif comando == "guardar":
        guardar()
