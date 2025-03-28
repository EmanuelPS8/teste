CREATE TABLE demonstracoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data DATE,
    ano INT,
    trimestre INT,
    reg_ans INT,
    cd_conta_contabil VARCHAR(20),
    descricao TEXT,
    vl_saldo_inicial VARCHAR(20),
    vl_saldo_final VARCHAR(20)
);

