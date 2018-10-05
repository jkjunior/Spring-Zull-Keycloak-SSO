--seleciona o email do servidor ATIVO, caso o cpf passado por parâmetro esteja dentro dos ativos
SELECT S.E_MAIL EMAIL
FROM SRH2.SERVIDOR S
WHERE 1=1
AND S.NUM_CPF = ? --:CPF
AND S.CD_SI_FUNC IN (
  6,  --EFETIVO
  7,  --EFETIVO CEDIDO
  8,  --REQUISITADO
  14  --EXERCÍCIO PROVISÓRIO
)