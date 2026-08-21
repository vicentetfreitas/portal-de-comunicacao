export default {
  app: {
    name: "Portal de Comunicação"
  },
  common: {
    loading: "Carregando...",
    notFound: "Página não encontrada",
    close: "Fechar"
  },
  layout: {
    nav: {
      home: "Início",
      app: "Área autenticada",
      showcase: "Design System",
      auth: "Autenticação",
      admin: "Administração"
    },
    header: {
      toggleMenu: "Alternar menu",
      searchPlaceholder: "Buscar..."
    },
    footer: {
      copyright: "© {year} Unimed Ceará",
      foundation: "Frontend Foundation — Sprint 0"
    },
    sidebar: {
      title: "Navegação",
      adminSection: "Administração",
      collapse: "Recolher menu",
      toggleCollapse: "Alternar menu colapsado",
      profileGreeting: "Olá,",
      profileName: "Colaborador",
      profileEdit: "Editar perfil"
    },
    home: {
      title: "Portal de Comunicação",
      subtitle: "Infraestrutura frontend — Sprint 0",
      foundationCard: "Fundação",
      foundationSubtitle: "Bootstrap e ambiente",
      foundationText:
        "Base técnica inicializada para implementação das Features.",
      environment: "Ambiente",
      exploreCard: "Explorar layouts"
    },
    auth: {
      title: "Autenticação",
      subtitle: "Acesse o Portal de Comunicação",
      description:
        "Utilize suas credenciais corporativas do Zimbra para entrar no portal.",
      loginAction: "Entrar com Zimbra",
      rememberMe: "Lembrar-me neste dispositivo",
      logout: "Sair",
      editProfile: "Editar perfil",
      footer: "Autenticação corporativa — Unimed Ceará",
      figma: {
        titlePortal: "Portal de",
        titleComunicacao: "Comunicação",
        userLabel: "Usuário:",
        passwordLabel: "Senha:",
        submit: "entrar",
        errors: {
          userRequired: "Informe o usuário.",
          passwordHint: "Verifique sua senha do e-mail"
        }
      },
      errors: {
        unauthorized: "Sua sessão expirou. Faça login novamente.",
        invalidCredentials: "Usuário ou senha inválidos.",
        forbidden: "Você não possui permissão para executar esta ação.",
        portalAccessDenied:
          "Seu cadastro não está autorizado a acessar o Portal. Entre em contato com o administrador.",
        unavailable: "O serviço encontra-se temporariamente indisponível.",
        unknown:
          "Ocorreu um erro inesperado. Tente novamente em alguns instantes."
      },
      context: {
        title: "Contexto organizacional",
        subtitle: "Selecione o contexto em que você atuará nesta sessão.",
        optionLabel:
          "Federação {federation} · Singular {singular} · Área {area} · Equipe {team}"
      }
    },
    admin: {
      badge: "Admin",
      title: "Área administrativa",
      subtitle: "Shell administrativo sem menus de negócio",
      cardTitle: "Placeholder administrativo",
      placeholder:
        "Estrutura de layout administrativo pronta para extensão pelas Features."
    },
    app: {
      title: "Área autenticada",
      subtitle: "Bem-vindo ao Portal de Comunicação",
      cardTitle: "Sessão ativa",
      placeholder: "Área autenticada protegida por FT-AUTH.",
      welcome: "Olá, {name}"
    },
    unauthorized: {
      title: "Acesso não autorizado",
      subtitle: "Você não possui permissão para acessar este recurso.",
      message:
        "Se você acredita que deveria ter acesso, entre em contato com o administrador do portal."
    },
    primeiroAcesso: {
      title: "Primeiro acesso",
      subtitle: "Complete seu vínculo organizacional para usar o Portal.",
      message:
        "Sua identidade foi autenticada. Selecione a Área da sua Singular para concluir o cadastro.",
      blockedTitle: "Primeiro acesso bloqueado",
      blockedSubtitle: "Não foi possível determinar sua Singular.",
      blockedMessage:
        "Não foi possível determinar a Singular a partir do domínio autenticado.",
      areaLabel: "Área",
      areaPlaceholder: "Selecione sua área",
      confirm: "Confirmar",
      areaRequired: "Selecione uma área para continuar.",
      emptyAreas: "Nenhuma área ativa está disponível para a sua Singular.",
      completeSuccess: "Primeiro acesso concluído."
    },
    notFound: {
      goHome: "Voltar ao início"
    }
  },
  showcase: {
    title: "Design System",
    subtitle: "Componentes base da infraestrutura frontend — alinhamento Figma",
    typography: "Escala tipográfica",
    atoms: "Átomos",
    molecules: "Moléculas",
    organisms: "Organismos",
    navigation: "Navegação",
    profile: "Perfil",
    content: "Conteúdo",
    primary: "Primário",
    secondary: "Secundário",
    ghost: "Ghost",
    outline: "Outline",
    danger: "Perigo",
    link: "Link",
    loading: "Carregando",
    inputLabel: "Campo de texto",
    inputHint: "Texto de ajuda",
    inputError: "Campo com erro",
    inputErrorMessage: "Este campo é obrigatório",
    inputFilled: "Campo preenchido",
    selectLabel: "Seleção",
    cardTitle: "Card de exemplo",
    cardSubtitle: "Molécula reutilizável",
    cardContent: "Conteúdo do card utilizando tokens de design.",
    cardFlat: "Card flat",
    cardOutlined: "Card outlined",
    cancel: "Cancelar",
    confirm: "Confirmar",
    openDialog: "Abrir diálogo",
    dialogTitle: "Diálogo de exemplo",
    dialogSubtitle: "Modal reutilizável",
    dialogContent: "Conteúdo do diálogo sem regras de negócio.",
    formCardTitle: "Formulário base",
    formName: "Nome",
    formEmail: "E-mail",
    save: "Salvar",
    tableTitle: "Tabela base",
    notifySuccess: "Notificar sucesso",
    notifyError: "Notificar erro",
    notifySuccessMessage: "Operação concluída com sucesso.",
    notifyErrorMessage: "Ocorreu um erro inesperado.",
    navHome: "Início",
    navNews: "Notícias",
    navProfile: "Perfil",
    profileGreeting: "Olá,",
    profileName: "Maria Silva",
    profileSubtitle: "Área de Comunicação",
    profileEdit: "Editar perfil",
    sectionTitle: "Últimas notícias",
    sectionSubtitle: "Conteúdo recente do portal",
    contentCardTitle: "Comunicado interno",
    contentCardDescription:
      "Resumo do comunicado com informações relevantes para a equipe.",
    contentCardMeta: "Há 2 dias",
    compactCardTitle: "Documento compartilhado",
    compactCardDescription: "Relatório mensal — Q2 2026",
    serviceCardTitle: "Downloads",
    serviceCardDescription: "Acesse documentos e materiais da área.",
    actionCardLabel: "Equipe",
    actionCardDescription: "Gerenciar colaboradores",
    actionCardAreas: "Áreas",
    actionCardServices: "Serviços"
  },
  singular: {
    hub: {
      title: "Singulares",
      subtitle: "Gestão de singulares organizacionais",
      cardTitle: "Ações rápidas",
      listAction: "Listar singulares",
      listDescription:
        "Consulte singulares cadastradas com filtros e paginação",
      createAction: "Cadastrar singular",
      createDescription: "Inclua uma nova singular organizacional"
    },
    list: {
      title: "Listagem de singulares",
      subtitle: "Consulte e filtre singulares cadastradas",
      cardTitle: "Resultados",
      createAction: "Nova singular",
      viewAction: "Ver detalhe",
      emptyTitle: "Nenhuma singular encontrada",
      emptyDescription: "Ajuste os filtros ou cadastre uma nova singular.",
      columns: {
        status: "Status",
        actions: "Ações"
      },
      filters: {
        title: "Filtros",
        status: "Status",
        apply: "Aplicar filtros",
        clear: "Limpar"
      }
    },
    create: {
      title: "Cadastrar singular",
      subtitle: "Nova singular organizacional",
      cardTitle: "Dados cadastrais",
      placeholder: "Placeholder — implementação visual em PKG-FE-02.",
      success: "Singular cadastrada com sucesso."
    },
    detail: {
      title: "Detalhe da singular",
      subtitle: "Identificador: {id}",
      cardTitle: "Informações",
      editAction: "Editar",
      activateAction: "Ativar",
      deactivateAction: "Inativar",
      changeStatusAction: "Alterar status",
      backToList: "Voltar para listagem",
      notFoundTitle: "Singular não encontrada",
      notFoundDescription:
        "O identificador informado não corresponde a um registro existente.",
      fields: {
        id: "Identificador",
        createdAt: "Cadastro",
        updatedAt: "Última atualização",
        notAvailable: "—"
      }
    },
    edit: {
      title: "Editar singular",
      subtitle: "Identificador: {id}",
      cardTitle: "Formulário de edição",
      success: "Singular atualizada com sucesso."
    },
    form: {
      federationId: "Federação",
      federationHint: "Vínculo fixo conforme seed até FT-FEDERACAO",
      name: "Nome",
      acronym: "Sigla",
      unimedCode: "Código Unimed",
      registroAns: "Registro ANS",
      submitCreate: "Cadastrar singular",
      submitEdit: "Salvar alterações",
      cancel: "Cancelar"
    },
    status: {
      ACTIVE: "Ativa",
      INACTIVE: "Inativa"
    },
    statusDialog: {
      cancel: "Cancelar",
      successActivate: "Singular ativada com sucesso.",
      successDeactivate: "Singular inativada com sucesso.",
      activate: {
        title: "Ativar singular",
        subtitle: "Confirme a reativação de {name}",
        message:
          "A singular voltará ao status ativo e poderá ser utilizada normalmente.",
        confirm: "Ativar singular"
      },
      deactivate: {
        title: "Inativar singular",
        subtitle: "Confirme a inativação de {name}",
        message:
          "A singular será marcada como inativa. A operação falhará se existirem áreas ativas vinculadas.",
        confirm: "Inativar singular"
      }
    }
  },
  equipe: {
    hub: {
      title: "Equipes",
      subtitle: "Gestão de equipes organizacionais",
      cardTitle: "Ações rápidas",
      listAction: "Listar equipes",
      listDescription: "Consulte equipes cadastradas com filtros e paginação",
      createAction: "Cadastrar equipe",
      createDescription: "Inclua uma nova equipe vinculada a uma área"
    },
    list: {
      title: "Listagem de equipes",
      subtitle: "Consulte e filtre equipes cadastradas",
      cardTitle: "Resultados",
      createAction: "Nova equipe",
      viewAction: "Ver detalhe",
      emptyTitle: "Nenhuma equipe encontrada",
      emptyDescription: "Ajuste os filtros ou cadastre uma nova equipe.",
      columns: {
        status: "Status",
        actions: "Ações"
      },
      filters: {
        title: "Filtros",
        status: "Status",
        apply: "Aplicar filtros",
        clear: "Limpar"
      }
    },
    create: {
      title: "Cadastrar equipe",
      subtitle: "Nova equipe organizacional",
      cardTitle: "Dados cadastrais",
      success: "Equipe cadastrada com sucesso."
    },
    detail: {
      title: "Detalhe da equipe",
      subtitle: "Identificador: {id}",
      cardTitle: "Informações",
      editAction: "Editar",
      activateAction: "Ativar",
      deactivateAction: "Inativar",
      changeStatusAction: "Alterar status",
      backToList: "Voltar para listagem",
      notFoundTitle: "Equipe não encontrada",
      notFoundDescription:
        "O identificador informado não corresponde a um registro existente.",
      fields: {
        id: "Identificador",
        createdAt: "Cadastro",
        updatedAt: "Última atualização",
        notAvailable: "—"
      }
    },
    edit: {
      title: "Editar equipe",
      subtitle: "Identificador: {id}",
      cardTitle: "Formulário de edição",
      success: "Equipe atualizada com sucesso."
    },
    form: {
      areaId: "Área",
      areaHint: "Selecione a área ativa à qual a equipe pertence",
      name: "Nome",
      description: "Descrição",
      leaderId: "Líder",
      leaderHint: "Opcional — identificador do colaborador líder",
      submitCreate: "Cadastrar equipe",
      submitEdit: "Salvar alterações",
      cancel: "Cancelar"
    },
    status: {
      ACTIVE: "Ativa",
      INACTIVE: "Inativa"
    },
    statusDialog: {
      cancel: "Cancelar",
      successActivate: "Equipe ativada com sucesso.",
      successDeactivate: "Equipe inativada com sucesso.",
      activate: {
        title: "Ativar equipe",
        subtitle: "Confirme a reativação de {name}",
        message:
          "A equipe voltará ao status ativo e poderá ser utilizada normalmente.",
        confirm: "Ativar equipe"
      },
      deactivate: {
        title: "Inativar equipe",
        subtitle: "Confirme a inativação de {name}",
        message:
          "A equipe será marcada como inativa. A operação falhará se existirem colaboradores ativos vinculados.",
        confirm: "Inativar equipe"
      }
    },
    stub: {
      placeholder: "Placeholder — implementação visual em PKG-FE-02 em diante."
    }
  },
  colaborador: {
    hub: {
      title: "Colaboradores",
      subtitle: "Gestão de colaboradores organizacionais",
      cardTitle: "Ações rápidas",
      listAction: "Listar colaboradores",
      listDescription: "Consulte colaboradores com filtros e paginação",
      createAction: "Cadastrar colaborador",
      createDescription: "Inclua um novo colaborador com vínculo organizacional"
    },
    list: {
      title: "Listagem de colaboradores",
      subtitle: "Consulte e filtre colaboradores cadastrados"
    },
    create: {
      title: "Cadastrar colaborador",
      subtitle: "Novo colaborador organizacional"
    },
    detail: {
      title: "Detalhe do colaborador",
      subtitle: "Identificador: {id}",
      notFoundTitle: "Colaborador não encontrado",
      notFoundDescription:
        "Não foi possível localizar um colaborador com este identificador.",
      backToList: "Voltar para listagem",
      editAction: "Editar colaborador",
      fields: {
        id: "Identificador",
        email: "E-mail",
        zimbraId: "Identificador Zimbra",
        singularId: "Singular",
        areaId: "Área",
        teamId: "Equipe",
        managerId: "Gestor",
        biography: "Biografia",
        birthDate: "Data de nascimento",
        hireDate: "Data de admissão",
        lastAccessAt: "Último acesso",
        createdAt: "Criado em",
        updatedAt: "Atualizado em",
        notAvailable: "Não informado"
      }
    },
    edit: {
      title: "Editar colaborador",
      subtitle: "Identificador: {id}"
    },
    status: {
      ACTIVE: "Ativo",
      INACTIVE: "Inativo"
    },
    stub: {
      placeholder: "Placeholder — implementação visual em PKG-FE-02 em diante."
    }
  },
  areaColaborador: {
    hub: {
      title: "Área",
      subtitle: "Visão geral da área do seu contexto ativo",
      notFoundTitle: "Área não encontrada",
      notFoundDescription:
        "Não foi possível localizar os dados da sua área. As demais seções continuam disponíveis.",
      cardTitle: "Seções",
      equipeAction: "Equipe",
      equipeDescription: "Consulte as equipes vinculadas à sua área",
      arquivosAction: "Arquivos e Documentos",
      arquivosDescription: "Acesse os arquivos e documentos da sua área"
    }
  }
};
