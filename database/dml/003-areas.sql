--------------------------------------------------------------------------------
-- Portal de Comunicação | dml/003-areas.sql
-- Pré-requisito: dml/002-singulares.sql
--------------------------------------------------------------------------------

SET DEFINE OFF

INSERT INTO AREA (COD_AREA, COD_SINGULAR, NOM_AREA, SIG_AREA, DSC_AREA, FLG_ATIVO, DAT_CADASTRO)
SELECT SQ_AREA_COD_AREA.NEXTVAL,
       2,
       'TECNOLOGIA DA INFORMAÇÃO',
       'TI',
       'Com foco na excelência operacional e na inovação contínua, o setor de TI da UNIMED CEARÁ se consolida como um pilar estratégico 
       para o crescimento e a sustentabilidade da cooperativa. Atuando de forma proativa e integrada, nossa missão é garantir a segurança, 
       a confiabilidade e a evolução constante dos sistemas e infraestruturas tecnológicas, assegurando que colaboradores, prestadores e, 
       principalmente, nossos clientes, tenham acesso a serviços de saúde ágeis e de alta qualidade. Através da modernização e da 
       gestão eficiente dos dados, fortalecemos não apenas a operação interna, mas também a experiência do usuário, 
       reafirmando diariamente nosso compromisso com a saúde e o bem-estar de todos no Ceará.',
       'S',
       SYSTIMESTAMP
FROM federacao f,
     singular s
WHERE 
 f.cod_federacao = s.cod_federacao
 AND s.cod_unimed    = 979
 AND s.cod_singular  = 2
 AND s.sig_singular  = 'UNMCEA'
 AND f.cod_federacao = 1
 ;


INSERT INTO AREA (COD_AREA, COD_SINGULAR, NOM_AREA, SIG_AREA, DSC_AREA, FLG_ATIVO, DAT_CADASTRO)
VALUES (
    SQ_AREA_COD_AREA.NEXTVAL,
    2,
    'MARKETING',
    'MKT',
    'O Marketing da Unimed Ceará é um elo que conecta propósitos, pessoas e resultados. Mais do que criar campanhas, nossa atuação 
    fortalece relações, impulsiona projetos e amplia o alcance das ações que transformam a vida de quem faz e de quem é cuidado pela Unimed. 
    Com um olhar estratégico e colaborativo, estamos presentes em cada iniciativa — da Federação às singulares — construindo, juntos, 
    a marca que há 40 anos cuida de histórias em todo o Ceará.',
    'S',
    SYSTIMESTAMP
)
;


INSERT INTO AREA (COD_AREA, COD_SINGULAR, NOM_AREA, SIG_AREA, DSC_AREA, FLG_ATIVO, DAT_CADASTRO)
VALUES (
    SQ_AREA_COD_AREA.NEXTVAL,
    2,
    'ATENDIMENTO',
    'ATD',
    'Com grande profissionalismo e um compromisso genuíno com o bem-estar de cada associado, a Equipe de Atendimento da Unimed Ceará se 
    destaca como um pilar fundamental na prestação de serviços de saúde. Diariamente, nossos colaboradores dedicam-se para oferecer um 
    atendimento ágil, humano e resolutivo, seja esclarecendo dúvidas, orientando sobre procedimentos ou facilitando o acesso à nossa vasta 
    rede de credenciados. Mais do que uma central de informações, somos a voz acolhedora e a mão amiga que guia e tranquiliza, reforçando que, 
    na Unimed Ceará, a nossa maior prioridade é cuidar de quem cuida da sua saúde.',
    'S',
    SYSTIMESTAMP
)
;

COMMIT;
