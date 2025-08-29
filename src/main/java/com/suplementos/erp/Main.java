package com.suplementos.erp;

import com.suplementos.erp.model.*;
import com.suplementos.erp.repository.ProdutoRepository;
import com.suplementos.erp.repository.UsuarioRepository;
import com.suplementos.erp.repository.VendaRepository;
import com.suplementos.erp.service.EstoqueService;
import com.suplementos.erp.service.VendasService;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Inicialização dos repositórios
        ProdutoRepository produtoRepository = new ProdutoRepository();
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        VendaRepository vendaRepository = new VendaRepository();

        // Inicialização dos serviços (injeção de dependências)
        EstoqueService estoqueService = new EstoqueService(produtoRepository);
        VendasService vendasService = new VendasService(vendaRepository, produtoRepository);


        // Criando usuários para o exemplo com diferentes papéis e senhas
        Usuario administrador = new Usuario(1, "Admin", "senhaadmin", TipoUsuario.ADMINISTRADOR);
        Usuario gerente = new Usuario(2, "Gerente", "senhagerente", TipoUsuario.GERENTE);
        Usuario funcionario = new Usuario(3, "Funcionario", "senhafunc", TipoUsuario.FUNCIONARIO);
        usuarioRepository.salvar(administrador.codigo(), administrador);
        usuarioRepository.salvar(gerente.codigo(), gerente);
        usuarioRepository.salvar(funcionario.codigo(), funcionario);

        Scanner scanner = new Scanner(System.in);

        Usuario usuarioLogado = realizarLogin(scanner, usuarioRepository);
        if (usuarioLogado == null) {
            System.out.println("Login falhou. Encerrando o sistema.");
            scanner.close();
            return;
        }

        System.out.println("\nBem-vindo(a), " + usuarioLogado.nome() + " (" + usuarioLogado.tipo() + ")!");
        estoqueService.verificarAlertasDeEstoque();

        // Exibir o menu apropriado
        if (usuarioLogado.tipo() == TipoUsuario.FUNCIONARIO) {
            menuFuncionario(scanner, estoqueService, vendasService, usuarioLogado);
        } else {
            menuAdminGerente(scanner, estoqueService, vendasService, usuarioLogado);
        }

        scanner.close();
    }

    private static Usuario realizarLogin(Scanner scanner, UsuarioRepository usuarioRepository) {
        System.out.println("\n--- TELA DE LOGIN ---");
        System.out.print("Digite seu código de usuário (1, 2 ou 3):   ");
        int codigo = obterOpcaoUsuario(scanner);
        System.out.print("Digite sua senha: ");
        String senha = scanner.next();

        Usuario usuario = usuarioRepository.buscarPorId(codigo);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            return null;
        }

        // Verifica a senha principal
        if (!usuario.senha().equals(senha)) {
            System.out.println("Senha incorreta.");
            return null;
        }

        // Verifica a senha secundária para administradores e gerentes
        if (usuario.tipo() == TipoUsuario.ADMINISTRADOR || usuario.tipo() == TipoUsuario.GERENTE) {
            System.out.print("Acesso privilegiado. Digite a senha secundária: (dica: 1234) ");
            String senhaSecundaria = scanner.next();
            // Para este exemplo, usamos uma senha fixa. Em um sistema real, seria mais seguro.
            if (!senhaSecundaria.equals("1234")) {
                System.out.println("Senha secundária incorreta. Acesso negado.");
                return null;
            }
        }
        return usuario;
    }

    private static void menuFuncionario(Scanner scanner, EstoqueService estoqueService, VendasService vendasService, Usuario usuarioLogado) {
        int opcaoPrincipal;
        do {
            System.out.println("\n--- MENU FUNCIONÁRIO ---");
            System.out.println("1. Gerenciar Produtos (Estoque)");
            System.out.println("2. Gerenciar Vendas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcaoPrincipal = obterOpcaoUsuario(scanner);

            switch (opcaoPrincipal) {
                case 1:
                    menuCadastroProdutos(scanner, estoqueService);
                    break;
                case 2:
                    menuGerenciamentoVendas(scanner, estoqueService, vendasService, usuarioLogado);
                    break;
                case 0:
                    System.out.println("Saindo do sistema. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcaoPrincipal != 0);
    }

    private static void menuAdminGerente(Scanner scanner, EstoqueService estoqueService, VendasService vendasService, Usuario usuarioLogado) {
        int opcaoPrincipal;
        do {
            System.out.println("\n--- MENU ADMINISTRADOR/GERENTE ---");
            System.out.println("1. Gerenciar Produtos (Cadastro e Estoque)");
            System.out.println("2. Gerenciar Vendas");
            System.out.println("3. Gerenciar Fornecedores e Compras");
            System.out.println("4. Relatórios Financeiros");
            System.out.println("5. Gerenciar Usuários");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcaoPrincipal = obterOpcaoUsuario(scanner);

            switch (opcaoPrincipal) {
                case 1:
                    menuCadastroProdutos(scanner, estoqueService);
                    break;
                case 2:
                    menuGerenciamentoVendas(scanner,estoqueService, vendasService, usuarioLogado);
                    break;
                case 3:
                    System.out.println("Opção 'Fornecedores e Compras' ainda não implementada.");
                    break;
                case 4:
                    System.out.println("Opção 'Relatórios Financeiros' ainda não implementada.");
                    break;
                case 5:
                    System.out.println("Opção 'Gerenciar Usuários' ainda não implementada.");
                    break;
                case 0:
                    System.out.println("Saindo do sistema. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcaoPrincipal != 0);
    }

    private static void adicionarNovoProduto(Scanner scanner, EstoqueService estoqueService) {
        System.out.println("\n--- ADICIONAR NOVO PRODUTO ---");

        System.out.print("Nome do produto: ");
        scanner.nextLine(); // Consome a nova linha pendente
        String nome = scanner.nextLine();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Preço: ");
        double preco = scanner.nextDouble();

        System.out.print("Quantidade em estoque: ");
        int quantidade = scanner.nextInt();

        System.out.print("Estoque mínimo: ");
        int estoqueMinimo = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Categoria: ");
        String categoriaNome = scanner.nextLine();

        System.out.print("Fornecedor: ");
        String fornecedorNome = scanner.nextLine();

        // Gerando o ID e criando o objeto produto
        // AQUI ESTÁ A MUDANÇA: PEGAMOS O REPOSITÓRIO E PEDIMOS O PRÓXIMO ID.
        int novoId = estoqueService.getProdutoRepository().getNextId();

        Categoria categoria = new Categoria(0, categoriaNome);
        Fornecedor fornecedor = new Fornecedor(0, fornecedorNome, "");

        Produto novoProduto = new Produto(novoId, nome, descricao, preco, quantidade, estoqueMinimo, categoria, fornecedor);

        // Salvando o produto no banco de dados
        estoqueService.adicionarProduto(novoProduto);
        System.out.println("Produto '" + nome + "' adicionado com sucesso!");
    }

    private static List<Integer> escolherProdutos(Scanner scanner) {
        List<Integer> ids = new ArrayList<>();
        System.out.println("\nDigite os IDs dos produtos que deseja vender (digite '0' para finalizar):");
        int id;
        while (true) {
            try {
                System.out.print("ID do produto: ");
                id = scanner.nextInt();
                if (id == 0) {
                    break;
                }
                ids.add(id);
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine(); // Limpa o buffer do scanner
            }
        }
        return ids;
    }

    private static FormaPagamento escolherFormaPagamento(Scanner scanner) {
        System.out.println("\nEscolha a forma de pagamento:");
        int i = 1;
        for (FormaPagamento fp : FormaPagamento.values()) {
            System.out.println(i + ". " + fp.name());
            i++;
        }

        FormaPagamento formaPagamento = null;
        int opcaoPagamento;
        while (formaPagamento == null) {
            try {
                System.out.print("Opção: ");
                opcaoPagamento = scanner.nextInt();
                if (opcaoPagamento > 0 && opcaoPagamento <= FormaPagamento.values().length) {
                    formaPagamento = FormaPagamento.values()[opcaoPagamento - 1];
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
            }
        }
        return formaPagamento;
    }

    // Métodos auxiliares de menu, mantidos do código anterior...
    private static void menuCadastroProdutos(Scanner scanner, EstoqueService estoqueService) {
        // ... (código existente)
        int opcaoProdutos;
        do {
            System.out.println("\n--- MENU DE PRODUTOS ---");
            System.out.println("1. Listar todos os produtos");
            System.out.println("2. Verificar alertas de estoque");
            System.out.println("3. Adicionar novo produto");
            System.out.println("4. Editar produto existente");
            System.out.println("5. Remover produto");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcaoProdutos = obterOpcaoUsuario(scanner);

            switch (opcaoProdutos) {
                case 1:
                    estoqueService.listarTodosOsProdutos();
                    break;
                case 2:
                    estoqueService.verificarAlertasDeEstoque();
                    break;
                case 3:
                    adicionarNovoProduto(scanner, estoqueService);
                    break;
                case 4:
                    System.out.println("Funcionalidade 'Editar Produto' ainda não implementada.");
                    break;
                case 5:
                    System.out.println("Funcionalidade 'Remover Produto' ainda não implementada.");
                    break;
                case 0:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcaoProdutos != 0);
    }

    private static void menuGerenciamentoVendas(Scanner scanner,EstoqueService estoqueService, VendasService vendasService, Usuario usuarioLogado) {
        int opcaoVendas;
        do {
            System.out.println("\n--- MENU DE VENDAS ---");
            System.out.println("1. Realizar nova venda");
            System.out.println("2. Listar histórico de vendas");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcaoVendas = obterOpcaoUsuario(scanner);

            switch (opcaoVendas) {
                case 1:
                    System.out.println("\n--- REALIZAR NOVA VENDA ---");
                    estoqueService.listarTodosOsProdutos();
                    List<Integer> idsProdutos = escolherProdutos(scanner);
                    if (idsProdutos.isEmpty()) {
                        System.out.println("Venda cancelada. Nenhum produto foi selecionado.");
                        break;
                    }
                    // 3. Coletar a forma de pagamento
                    FormaPagamento formaPagamento = escolherFormaPagamento(scanner);

                    // 4. Realizar a venda
                    vendasService.realizarVenda(null, usuarioLogado, idsProdutos, formaPagamento);
                    break;
                case 2:
                    vendasService.listarHistoricoDeVendas();
                    break;
                case 0:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcaoVendas != 0);
    }

    private static int obterOpcaoUsuario(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.next(); // Limpa o buffer do scanner
            return -1; // Retorna um valor inválido para o loop
        }



    }
}