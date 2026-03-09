import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Comercio {
    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a cada execução */
    static Produto[] produtosCadastrados;

    /** Quantidade produtos cadastrados atualmente no vetor */
    static int quantosProdutos;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa(){
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho(){
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
     * @return Um inteiro com a opção do usuário.
    */
    static int menu(){
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no formato
     * N  (quantiade de produtos) <br/>
     * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
        Produto[] vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
        quantosProdutos = 0;

        try (Scanner leitorArquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("ISO-8859-2"))) {
            if (leitorArquivo.hasNextLine()) {
                int qtdArquivo = Integer.parseInt(leitorArquivo.nextLine().trim());
                vetorProdutos = new Produto[qtdArquivo + MAX_NOVOS_PRODUTOS];

                while (leitorArquivo.hasNextLine() && quantosProdutos < qtdArquivo) {
                    String linha = leitorArquivo.nextLine();
                    if (!linha.isBlank()) {
                        vetorProdutos[quantosProdutos] = Produto.criarDoTexto(linha);
                        quantosProdutos++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de dados não encontrado. Iniciando sistema com vetor vazio.");
        } catch (Exception e) {
            System.out.println("Erro ao ler dados: " + e.getMessage());
        }

        return vetorProdutos;
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos(){
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < produtosCadastrados.length; i++) {
            if(produtosCadastrados[i]!=null)
                System.out.println(String.format("%02d - %s", (i+1),produtosCadastrados[i].toString()));
        }
    }

    /** Localiza um produto no vetor de cadastrados, a partir do nome, e imprime seus dados. 
     * A busca não é sensível ao caso.  Em caso de não encontrar o produto, imprime mensagem padrão */
    static void localizarProdutos(){
        cabecalho();
        System.out.print("Digite a descrição (nome) do produto que deseja localizar: ");
        String nomeBusca = teclado.nextLine();

        // CORREÇÃO: Passando 1.0 no preço e 0.1 na margem para não estourar a sua validação da classe Produto
        Produto produtoBusca = new ProdutoNaoPerecivel(nomeBusca, 1.0, 0.1);
        boolean encontrou = false;

        for (int i = 0; i < produtosCadastrados.length; i++) {
            if (produtosCadastrados[i] != null && produtosCadastrados[i].equals(produtoBusca)) {
                System.out.println("\nProduto Encontrado:");
                System.out.println(produtosCadastrados[i].toString());
                encontrou = true;
                break;
            }
        }

        if (!encontrou) {
            System.out.println("\nProduto não encontrado.");
        }
    }

    //#region Cadastrar Prduto

    /**
     * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui no vetor. Este método pode ser feito com um nível muito 
     * melhor de modularização. As diversas fases da lógica poderiam ser encapsuladas em outros métodos. 
     * Uma sugestão de melhoria mais significativa poderia ser o uso de padrão Factory Method para criação dos objetos.
     */
    static void cadastrarProduto(){
        cabecalho();
        System.out.println("CADASTRAR NOVO PRODUTO\n");

        if (quantosProdutos >= produtosCadastrados.length) {
            System.out.println("Capacidade máxima atingida. Não é possível cadastrar mais produtos.");
            return;
        }

        try {
            int tipo = lerTipoProduto();
            if (tipo != 1 && tipo != 2) {
                System.out.println("Tipo inválido. Operação cancelada.");
                return;
            }

            String descricao = lerDescricao();
            double precoCusto = lerValor("Preço de custo (ex: 10.50): ");
            double margemLucro = lerValor("Margem de lucro (ex: 0.25 para 25%): ");

            Produto novoProduto;
            if (tipo == 1) {
                novoProduto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
            } else {
                LocalDate validade = lerDataValidade();
                novoProduto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, validade);
            }

            produtosCadastrados[quantosProdutos] = novoProduto;
            quantosProdutos++;
            System.out.println("\nProduto cadastrado com sucesso!");

        } catch (IllegalArgumentException | java.time.format.DateTimeParseException e) {
            System.out.println("\nErro de validação: Verifique os dados inseridos e tente novamente.");
            System.out.println("Detalhe: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nErro inesperado no cadastro.");
        }
    }

    private static int lerTipoProduto() {
        System.out.print("Qual o tipo do produto? (1 - Não Perecível, 2 - Perecível): ");
        return Integer.parseInt(teclado.nextLine());
    }

    private static String lerDescricao() {
        System.out.print("Descrição do produto: ");
        return teclado.nextLine();
    }

    private static double lerValor(String mensagem) {
        System.out.print(mensagem);
        return Double.parseDouble(teclado.nextLine().replace(",", "."));
    }

    private static LocalDate lerDataValidade() {
        System.out.print("Data de validade (dd/mm/aaaa): ");
        return LocalDate.parse(teclado.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    //#endregion

   /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo){
        try (FileWriter escritor = new FileWriter(nomeArquivo, Charset.forName("ISO-8859-2"))) {
            
            escritor.write(quantosProdutos + "\n");
            
            for (int i = 0; i < quantosProdutos; i++) {
                if (produtosCadastrados[i] != null) {
                    escritor.write(produtosCadastrados[i].gerarDadosTexto() + "\n");
                }
            }
            
        } catch (IOException e) {
            System.out.println("Erro Crítico: Não foi possível salvar os dados no arquivo: " + e.getMessage());
        }  
    }

    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }
            pausa();
        }while(opcao !=0);       

        salvarProdutos(nomeArquivoDados);
        teclado.close();    
    }
}
