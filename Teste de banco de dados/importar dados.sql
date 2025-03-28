LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 9.2/Uploads/{nome_do_arquivo.csv}'
INTO TABLE teste_banco_dados.demonstracoes
CHARACTER SET utf8
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(@data, reg_ans, cd_conta_contabil, descricao, vl_saldo_inicial, vl_saldo_final)
SET 
  ano = 2024,
  trimestre = 4;
