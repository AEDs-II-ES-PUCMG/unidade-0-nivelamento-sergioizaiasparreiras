import java.util.Locale;

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
        return String.format(Locale.US, "1;%s;%.2f;%.2f", this.descricao, this.precoCusto, this.margemLucro);
    }

   @Override
    public String toString() {
        String valorFormatado = String.format(Locale.of("pt", "BR"), "%.2f", valorDeVenda());
        return "NOME: " + this.descricao + " - Valor de Venda: R$ " + valorFormatado;
    }
}