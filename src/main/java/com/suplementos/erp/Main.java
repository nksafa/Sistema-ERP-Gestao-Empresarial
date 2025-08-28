package com.suplementos.erp;

import com.suplementos.erp.model.*;
import com.suplementos.erp.repository.ProdutoRepository;
import com.suplementos.erp.repository.UsuarioRepository;
import com.suplementos.erp.repository.VendaRepository;
import com.suplementos.erp.service.EstoqueService;
import com.suplementos.erp.service.VendasService;

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

        // Dados de exemplo para iniciar o sistema
        Fornecedor fornecedorGrowth = new Fornecedor(1, "Growth Supplements", "contato@growth.com");
        Categoria categoriaWhey = new Categoria(1, "Whey Protein");
        Categoria categoriaCreatina = new Categoria(2, "Creatina");
        Produto whey = new Produto(101, "Whey Isolado", "Whey Protein 90%", 180.00, 5, 2, categoriaWhey, fornecedorGrowth);
        Produto creatina = new Produto(102, "Creatina Pura", "Creatina Monohidratada", 10, 5, 2, categoriaCreatina, fornecedorGrowth);
        estoqueService.adicionarProduto(whey);
        estoqueService.adicionarProduto(creatina);

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
            System.out.print("Acesso privilegiado. Digite a senha secundária: ");
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
                    menuGerenciamentoVendas(scanner, vendasService, usuarioLogado);
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
                    menuGerenciamentoVendas(scanner, vendasService, usuarioLogado);
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

    // Métodos auxiliares de menu, mantidos do código anterior...
    private static void menuCadastroProdutos(Scanner scanner, EstoqueService estoqueService) {
        // ... (código existente)
        int opcaoProdutos;
        do {
            System.out.println("\n--- MENU DE PRODUTOS E ESTOQUE ---");
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
                    System.out.println("Funcionalidade 'Adicionar Produto' ainda não implementada.");
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

    private static void menuGerenciamentoVendas(Scanner scanner, VendasService vendasService, Usuario funcionario) {
        // ... (código existente)
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
                    System.out.println("Realizando nova venda (usando dados de exemplo)...");
                    vendasService.realizarVenda(null, funcionario, List.of(101, 102), FormaPagamento.CREDITO);
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