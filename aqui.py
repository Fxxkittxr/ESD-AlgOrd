import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import os

# Configuração dos gráficos (deixa bonito)
sns.set(style="whitegrid", palette="pastel", font_scale=1.2)

# Parâmetros
tamanhos = [1000, 10000]
distribuicoes = ['aleatorio', 'crescente', 'decrescente']
algoritmos = ['Bubble', 'Insertion', 'Selection', 'Quick', 'Merge']

# Função para carregar e juntar os dados de todos os CSVs
def carregar_dados():
    dados = []
    for tam in tamanhos:
        for dist in distribuicoes:
            for alg in algoritmos:
                nome_arquivo = f'{alg}_{dist}_{tam}.csv'
                if os.path.exists(nome_arquivo):
                    df = pd.read_csv(nome_arquivo)
                    df['Algoritmo'] = alg
                    df['Distribuicao'] = dist
                    df['Tamanho'] = tam
                    df['Tempo_ms'] = df['tempo_ns'] / 1_000_000  # converte nanosegundos p/ milissegundos
                    dados.append(df)
                else:
                    print(f'⚠️ Arquivo não encontrado: {nome_arquivo}')
    return pd.concat(dados, ignore_index=True)

# Carregar todos os dados
df = carregar_dados()

# Criação dos gráficos
for dist in distribuicoes:
    for tam in tamanhos:
        subset = df[(df['Distribuicao'] == dist) & (df['Tamanho'] == tam)]
        
        plt.figure(figsize=(10, 6))
        ax = sns.boxplot(x='Algoritmo', y='Tempo_ms', data=subset)
        sns.swarmplot(x='Algoritmo', y='Tempo_ms', data=subset, color=".25", size=3)  # pontos individuais
        
        plt.title(f'{dist.capitalize()} - {tam} elementos', fontsize=14, pad=15)
        plt.ylabel('Tempo (ms)')
        plt.xlabel('Algoritmo de Ordenação')
        plt.xticks(rotation=45)
        plt.tight_layout()
        
        nome_grafico = f'grafico_{dist}_{tam}.png'
        plt.savefig(nome_grafico, dpi=300)
        plt.close()
        print(f'✅ Gráfico salvo: {nome_grafico}')
