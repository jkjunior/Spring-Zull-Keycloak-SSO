SELECT 
  EXERCICIO, 
  CONCAT(CONCAT(NUM_ORDEM_PARCELA, ' - ') , TIPO_PARCELA) PARCELA_TIPO, 
  TO_CHAR(TO_DATE(DT_INICIO_PARCELA, 'DD/MM/RRRR')) DT_INICIO_PARCELA,
  TO_CHAR(TO_DATE(DT_FIM_PARCELA, 'DD/MM/RRRR')) DT_FIM_PARCELA, 
  QTD_DIAS_PARCELA, 
  SITUACAO  
FROM SRH2.VW_FERIAS 
WHERE CPF_SERVIDOR = ?