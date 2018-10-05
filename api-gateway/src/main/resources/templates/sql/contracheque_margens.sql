SELECT 
  REPLACE( REPLACE( REPLACE( TO_CHAR(DECODE(PP.VR_CALCULADO, 0, PP.VR_INFORMADO, PP.VR_CALCULADO), '999,999,999,990.00'),'.', '_'), ',', '.'), '_', ',') VALOR,
  PP.CD_ITEM 
FROM FOLHA.FL_PROCESSAMENTO_PENS PP
WHERE 1=1
  AND PP.CD_DEPEND = :cd
  AND PP.CD_GRUPO = 1 
  AND PP.CD_PROCESSAMENTO = 1 
  AND PP.CD_ITEM IN (21, 22, 41) 

UNION ALL 

SELECT
  REPLACE( REPLACE( REPLACE( TO_CHAR(DECODE(PS.VR_CALCULADO, 0, PS.VR_INFORMADO, PS.VR_CALCULADO), '999,999,999,990.00'),'.', '_'), ',', '.'), '_', ',') VALOR, 
  PS.CD_ITEM 
FROM FOLHA.FL_PROCESSAMENTO_SERV PS
WHERE 1=1
  AND PS.MAT_SERVIDOR = :mat
  AND PS.CD_GRUPO = 1 
  AND PS.CD_PROCESSAMENTO = 1 
  AND PS.CD_ITEM IN (21, 22, 41)