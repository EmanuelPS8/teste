from flask import Flask, request, jsonify
from flask_cors import CORS 
import pandas as pd

app = Flask(__name__)
CORS(app) 
df = pd.read_csv('Relatorio_cadop.csv', sep=';', encoding='utf-8')

@app.route('/operadoras')
def buscar_operadoras():
    termo = request.args.get('busca', '').lower()
    resultado = df[df.apply(lambda row: termo in row.astype(str).str.lower().to_string(), axis=1)]
    resultado = resultado.rename(columns={
        'Razao Social': 'Razao_Social',
        'Registro ANS': 'Registro_ANS',
        'Nome Fantasia': 'Nome_Fantasia',
        'CNPJ': 'CNPJ',
        'UF': 'UF',
        'Cidade': 'Cidade'
    })
    resultado = resultado.fillna('') 




    return resultado.to_json(orient='records', force_ascii=False)

if __name__ == '__main__':
    app.run(debug=True)