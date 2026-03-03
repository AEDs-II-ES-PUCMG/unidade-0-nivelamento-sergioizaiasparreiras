import java.text.NumberFormat;

public class ProdutoNaoPerecivel extends Produto {

    public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro){
        super(desc, precoCusto, margemLucro);
    }
    
    public ProdutoNaoPerecivel(String desc, double precoCusto){
        super(desc, precoCusto);
    }
    
    @Override
    public double valorDeVenda() {
       return (this.precoCusto * (1.0 + this.margemLucro));
    }

    @Override
    public String gerarDadosTexto(){
        return String.format(java.util.Locale.US, "1;%s;%.2f;%.2f", this.descricao, this.precoCusto, this.margemLucro);
    }

    @Override
    public String toString() {
        NumberFormat moeda = NumberFormat.getCurrencyInstance();
        return "NOME: " + this.descricao + " - Valor de Venda: " + moeda.format(valorDeVenda());
    }
}