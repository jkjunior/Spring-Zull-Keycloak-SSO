SELECT MES, ANO, DESCONTO, LIQUIDO, BRUTO, TETO, BASE_IR, NUM_FOLHA, DESCRICAO, TIPO, PREVIA
FROM FOLHA.FL_VW_MOBILE_DADOSCONTRACHEQUE
WHERE CPF = ?
AND MES = ?
AND ANO = ?
AND NUM_FOLHA = ?