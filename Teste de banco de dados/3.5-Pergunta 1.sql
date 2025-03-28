SELECT 
    reg_ans,
    SUM(vl_saldo_final - vl_saldo_inicial) AS total_despesa
FROM 
    teste_banco_dados.demonstracoes
WHERE 
    UPPER(descricao) LIKE '%EVENTOS%ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR%'
    AND ano = 2024
    AND trimestre = 4
GROUP BY 
    reg_ans
ORDER BY 
    total_despesa DESC
LIMIT 10;
