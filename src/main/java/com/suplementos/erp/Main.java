// Pacote principal para o sistema ERP
package com.suplementos.erp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// ---------------------------------------------------
// CLASSE: Produto
// ---------------------------------------------------
class Produto {
    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidadeEmEstoque;
    private int estoqueMinimo;
    private Categoria categoria;
    private Fornecedor fornecedor;

    public Produto(int id, String nome, String descricao, double preco, int quantidadeEmEstoque, int estoqueMinimo, Categoria categoria, Fornecedor fornecedor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
        this.estoqueMinimo = estoqueMinimo;
        this.categoria = categoria;
        this.fornecedor = fornecedor;
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getQuantidadeEmEstoque() { return quantidadeEmEstoque; }
    public void setQuantidadeEmEstoque(int quantidade) { this.quantidadeEmEstoque = quantidade; }
    public int getEstoqueMinimo() { return estoqueMinimo; }
    public Categoria getCategoria() { return categoria; }
    public Fornecedor getFornecedor() { return fornecedor; }
}

// ---------------------------------------------------
// CLASSE: Categoria
// ---------------------------------------------------
class Categoria {
    private int id;
    private String nome;

    public Categoria(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
}

// ---------------------------------------------------
// CLASSE: Fornecedor
// ---------------------------------------------------
class Fornecedor {
    private int id;
    private String nome;
    private String contato;

    public Fornecedor(int id, String nome, String contato) {
        this.id = id;
        this.nome = nome;
        this.contato = contato;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
}

// ---------------------------------------------------
// CLASSE: Usuario
// ---------------------------------------------------
enum TipoUsuario {
    ADMINISTRADOR, GERENTE, FUNCIONARIO, CLIENTE
}

class Usuario {
    private int codigo;
    private String nome;
    private String senha;
    private TipoUsuario tipo;

    public Usuario(int codigo, String nome, String senha, TipoUsuario tipo) {
        this.codigo = codigo;
        this.nome = nome;
        this.senha = senha;
        this.tipo = tipo;
    }

    // Getters
    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public TipoUsuario getTipo() { return tipo; }
    public String getSenha() { return senha; }

    public void setSenha(String novaSenha) { this.senha = novaSenha; }
}

// ---------------------------------------------------
// CLASSE: Venda
// ---------------------------------------------------
enum FormaPagamento {
    CREDITO, DEBITO, PIX, DINHEIRO
}

class Venda {
    private int id;
    private Date dataVenda;
    private Usuario cliente;
    private Usuario funcionario;
    private List<Produto> produtosVendidos;
    private double valorTotal;
    private FormaPagamento formaPagamento;

    public Venda(int id, Usuario cliente, Usuario funcionario, List<Produto> produtos, FormaPagamento formaPagamento) {
        this.id = id;
        this.dataVenda = new Date();
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.produtosVendidos = produtos;
        this.formaPagamento = formaPagamento;
        this.valorTotal = calcularValorTotal();
    }

    private double calcularValorTotal() {
        double total = 0;
        for (Produto p : produtosVendidos) {
            total += p.getPreco();
        }
        return total;
    }

    public int getId() { return id; }
    public Date getDataVenda() { return dataVenda; }
    public double getValorTotal() { return valorTotal; }
    public List<Produto> getProdutosVendidos() { return produtosVendidos; }
    public Usuario getFuncionario() { return funcionario; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
}

// ---------------------------------------------------
// CLASSE: EstoqueManager (Gerenciamento de Produtos e Estoque)
// Estoque é uma coleção de objetos, como um Map ou List
// ---------------------------------------------------
class EstoqueManager {
    private Map<Integer, Produto> estoque;

    public EstoqueManager() {
        this.estoque = new HashMap<>();
    }

    public void adicionarProduto(Produto produto) {
        estoque.put(produto.getId(), produto);
    }

    public void editarProduto(int id, Produto produtoAtualizado) {
        if (estoque.containsKey(id)) {
            estoque.put(id, produtoAtualizado);
            System.out.println("Produto editado com sucesso.");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public void removerProduto(int id) {
        if (estoque.containsKey(id)) {
            estoque.remove(id);
            System.out.println("Produto removido com sucesso.");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public List<Produto> listarProdutos() {
        return new ArrayList<>(estoque.values());
    }

    public void verificarAlertasDeEstoque() {
        System.out.println("\n--- ALERTA DE ESTOQUE ---");
        for (Produto produto : estoque.values()) {
            if (produto.getQuantidadeEmEstoque() < produto.getEstoqueMinimo()) {
                System.out.println("ALERTA: O produto '" + produto.getNome() + "' está abaixo do estoque mínimo!");
            }
        }
    }

    public Produto getProduto(int id) {
        return estoque.get(id);
    }
}

// ---------------------------------------------------
// CLASSE: VendasManager (Gerenciamento de Vendas e Checkout)
// ---------------------------------------------------
class VendasManager {
    private List<Venda> historicoVendas;
    private EstoqueManager estoqueManager;

    public VendasManager(EstoqueManager estoqueManager) {
        this.historicoVendas = new ArrayList<>();
        this.estoqueManager = estoqueManager;
    }

    public Venda realizarVenda(int idVenda, Usuario cliente, Usuario funcionario, List<Integer> idsProdutos, FormaPagamento formaPagamento) {
        List<Produto> produtosParaVenda = new ArrayList<>();
        boolean estoqueSuficiente = true;

        for (Integer id : idsProdutos) {
            Produto p = estoqueManager.getProduto(id);
            if (p != null && p.getQuantidadeEmEstoque() > 0) {
                produtosParaVenda.add(p);
            } else {
                estoqueSuficiente = false;
                System.out.println("Erro: Produto '" + p.getNome() + "' sem estoque.");
                break;
            }
        }

        if (estoqueSuficiente) {
            // Atualiza o estoque
            for (Produto p : produtosParaVenda) {
                p.setQuantidadeEmEstoque(p.getQuantidadeEmEstoque() - 1);
            }

            Venda novaVenda = new Venda(idVenda, cliente, funcionario, produtosParaVenda, formaPagamento);
            historicoVendas.add(novaVenda);
            System.out.println("\n--- Recibo da Venda #" + novaVenda.getId() + " ---");
            System.out.println("Data: " + novaVenda.getDataVenda());
            System.out.println("Cliente: " + (cliente != null ? cliente.getNome() : "Não Identificado"));
            System.out.println("Funcionário: " + novaVenda.getFuncionario().getNome());
            System.out.println("Itens:");
            for (Produto p : produtosParaVenda) {
                System.out.println("- " + p.getNome() + " | Preço: R$" + String.format("%.2f", p.getPreco()));
            }
            System.out.println("Forma de Pagamento: " + novaVenda.getFormaPagamento());
            System.out.println("Total: R$" + String.format("%.2f", novaVenda.getValorTotal()));
            System.out.println("-----------------------------\n");

            return novaVenda;
        }

        return null;
    }

    public List<Venda> getHistoricoVendas() {
        return historicoVendas;
    }
}

// ---------------------------------------------------
// CLASSE: FinanceiroManager (Relatórios e Lucro)
// ---------------------------------------------------
class FinanceiroManager {
    public void gerarRelatorioLucro(List<Venda> vendas) {
        double lucroTotal = 0;
        System.out.println("\n--- RELATÓRIO FINANCEIRO ---");
        System.out.println("Nome do Produto | Preço de Venda | Data da Venda");
        System.out.println("-------------------------------------------------");

        for (Venda venda : vendas) {
            for (Produto produto : venda.getProdutosVendidos()) {
                System.out.println(String.format("%-15s | R$%-15.2f | %s",
                        produto.getNome(), produto.getPreco(), venda.getDataVenda()));
                lucroTotal += produto.getPreco(); // Simplificado, assumindo que preço de custo é 0 para o exemplo
            }
        }
        System.out.println("-------------------------------------------------");
        System.out.println("Lucro Total: R$" + String.format("%.2f", lucroTotal));
    }
}

// ---------------------------------------------------
// CLASSE: Main (Ponto de entrada e exemplo de uso)
// ---------------------------------------------------
public class ErpSuplementos {
    public static void main(String[] args) {
        // Inicialização dos gerentes de sistema
        EstoqueManager estoque = new EstoqueManager();
        VendasManager vendas = new VendasManager(estoque);
        FinanceiroManager financeiro = new FinanceiroManager();

        // Criando categorias e fornecedores
        Categoria categoriaWhey = new Categoria(1, "Whey Protein");
        Categoria categoriaCreatina = new Categoria(2, "Creatina");
        Fornecedor fornecedorIntegral = new Fornecedor(101, "Integralmedica", "contato@integral.com");
        Fornecedor fornecedorGrowth = new Fornecedor(102, "Growth Supplements", "contato@growth.com");

        // Cadastro de produtos e estoque
        Produto wheyIntegral = new Produto(1, "Whey Pro Integral", "Whey Protein Concentrado", 99.90, 50, 10, categoriaWhey, fornecedorIntegral);
        Produto creatinaGrowth = new Produto(2, "Creatina Pura Growth", "Creatina Monohidratada", 59.90, 5, 5, categoriaCreatina, fornecedorGrowth); // Estoque baixo

        estoque.adicionarProduto(wheyIntegral);
        estoque.adicionarProduto(creatinaGrowth);

        // Verificação de estoque com alerta
        estoque.verificarAlertasDeEstoque();

        // Criando usuários
        Usuario func1 = new Usuario(10, "João da Silva", "senha123", TipoUsuario.FUNCIONARIO);
        Usuario cliente1 = new Usuario(1, "Maria Souza", "senha456", TipoUsuario.CLIENTE);

        // Simulação de venda com checkout
        System.out.println("\n--- REALIZANDO VENDA ---");
        List<Integer> carrinho = new ArrayList<>();
        carrinho.add(1); // Adiciona Whey
        carrinho.add(2); // Adiciona Creatina

        vendas.realizarVenda(1, cliente1, func1, carrinho, FormaPagamento.CREDITO);

        // Verificação de estoque após a venda
        estoque.verificarAlertasDeEstoque();

        // Geração de relatório financeiro
        financeiro.gerarRelatorioLucro(vendas.getHistoricoVendas());
    }
}