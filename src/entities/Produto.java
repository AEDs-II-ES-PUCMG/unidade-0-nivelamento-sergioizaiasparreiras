import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Produto {
	
	private static final double MARGEM_PADRAO = 0.2;
	protected String descricao;
	protected double precoCusto;
	protected double margemLucro;
	
	/**
     * Inicializador privado. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	private void init(String desc, double precoCusto, double margemLucro) {
		
		if ((desc.length() >= 3) && (precoCusto > 0.0) && (margemLucro > 0.0)) {
			descricao = desc;
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
		} else {
			throw new IllegalArgumentException("Valores inválidos para os dados do produto.");
		}
	}
	
	/**
     * Construtor completo. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	protected Produto(String desc, double precoCusto, double margemLucro) {
		init(desc, precoCusto, margemLucro);
	}
    
	
	/**
     * Construtor sem margem de lucro - fica considerado o valor padrão de margem de lucro.
     * Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00 
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     */
	protected Produto(String desc, double precoCusto) {
		init(desc, precoCusto, MARGEM_PADRAO);
	}
	

	/**
     * Cria um produto a partir de uma linha de dados em formato texto.
     * @param linha Linha com os dados do produto a ser criado.
     * @return Um produto com os dados recebidos
     */
    public static Produto criarDoTexto(String linha) { // [cite: 48]
        String[] partes = linha.split(";");
        if (partes.length < 4) {
            throw new IllegalArgumentException("Formato de linha de dados inválido.");
        }

        int tipo = Integer.parseInt(partes[0].trim());
        String desc = partes[1].trim();
        double custo = Double.parseDouble(partes[2].trim().replace(",", "."));
        double margem = Double.parseDouble(partes[3].trim().replace(",", "."));

        if (tipo == 1) { 
            return new ProdutoNaoPerecivel(desc, custo, margem);
        } else if (tipo == 2) {
            if (partes.length < 5) throw new IllegalArgumentException("Data de validade ausente no produto perecível.");
            LocalDate validade = LocalDate.parse(partes[4].trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return new ProdutoPerecivel(desc, custo, margem, validade);
        } else {
            throw new IllegalArgumentException("Tipo de produto não reconhecido (deve ser 1 ou 2).");
        }
    }

	/**
	* Gera uma linha de texto a partir dos dados do produto
	* @return Uma string no formato "tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
	*/
	public abstract String gerarDadosTexto();

	/**
	* Igualdade de produtos: caso possuam o mesmo nome/descrição.
	* @param obj Outro produto a ser comparado
	* @return booleano true/false conforme o parâmetro possua a descrição igual ou não a este produto.
	*/
	public boolean equals(Object obj){
		Produto outro = (Produto)obj;
		return this.descricao.toLowerCase().equals(outro.descricao.toLowerCase());
	}

	 /**
     * Retorna o valor de venda do produto, considerando seu preço de custo e margem de lucro.
     * @return Valor de venda do produto (double, positivo)
     */
	public abstract double valorDeVenda();
	
	/**
     * Retorna a representação textual do produto formatada para exibição.
     * * @return Uma string contendo a descrição do produto e o seu valor de venda formatado como moeda.
     */
    @Override
    public String toString() {
        NumberFormat moeda = NumberFormat.getCurrencyInstance();
        return String.format("NOME: " + this.descricao + ": " + moeda.format(valorDeVenda()));
    }
}